package cn.bucheng.miaosha.mq;

import cn.bucheng.miaosha.core.error.BizRuntimeException;
import cn.bucheng.miaosha.mapper.ItemMapper;
import cn.bucheng.miaosha.mapper.ItemStockMapper;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * @author ：yinchong
 * @create ：2019/7/22 19:18
 * @description：
 * @modified By：
 * @version:
 */
@Component
@Slf4j
@SuppressWarnings("all")
public class RockMQConsumer {

    private DefaultMQPushConsumer consumer;
    @Value("${mq.nameserver.addr}")
    private String nameAddr;

    @Value("${mq.topic.name}")
    private String topicName;
    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private ItemMapper itemMapper;


    @PostConstruct
    public void init() throws MQClientException {
        consumer = new DefaultMQPushConsumer("stock_group_consumer");
        consumer.setNamesrvAddr(nameAddr);
        consumer.subscribe(topicName, "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt msg = list.get(0);
                try {
                    String content = new String(msg.getBody());
                    Map<String, Object> map = JSON.parseObject(content, Map.class);
                    long itemId = Long.parseLong(map.get("itemId") + "");
                    int amount = (int) map.get("amount");
                    log.info("recevie message from producer itemId:{}, amount:{}", itemId, amount);
                    int row = itemStockMapper.decrementStockByItemId(itemId, amount);
                    if (row <= 0) {
                        throw new BizRuntimeException("更新失败");
                    }
                    row = itemMapper.incrementSales(itemId, amount);
                    if (row <= 0) {
                        throw new BizRuntimeException("更新失败");
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                } catch (Exception e) {
                    int time = msg.getReconsumeTimes();
                    if (time >= 4) {
                        log.error("retry consumer message,number:{}", time);
                        log.error(e.toString());
                    }
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        consumer.start();
    }
}
