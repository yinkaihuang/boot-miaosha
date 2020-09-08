package cn.bucheng.miaosha.service.impl;

import cn.bucheng.miaosha.constant.PromoRedisConstant;
import cn.bucheng.miaosha.constant.StockLogConstant;
import cn.bucheng.miaosha.core.error.BizError;
import cn.bucheng.miaosha.core.error.BizRuntimeException;
import cn.bucheng.miaosha.mapper.*;
import cn.bucheng.miaosha.model.po.OrderPO;
import cn.bucheng.miaosha.model.po.PromoPO;
import cn.bucheng.miaosha.model.po.StockLogPO;
import cn.bucheng.miaosha.model.vo.ItemVO;
import cn.bucheng.miaosha.mq.RockMQProducer;
import cn.bucheng.miaosha.service.ItemService;
import cn.bucheng.miaosha.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.UUID;

/**
 * @author ：yinchong
 * @create ：2019/7/22 14:40
 * @description：
 * @modified By：
 * @version:
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private PromoMapper promoMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private StockLogMapper stockLogMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RockMQProducer rockMQProducer;


    @Override
    @Transactional
    public void createNewOrder(Long itemId, Long userId, Integer amount, Long promoId) {
        //将当前用户操作加入库存流水（这里没有涉及表之间锁竞争会很快）
        long stockLogId = insertUserStockLog(itemId, userId, amount, promoId);
        //发送消息到rockmq中
        boolean flag = rockMQProducer.txAsyncReduceStock(itemId, userId, promoId, amount, stockLogId);
        if (!flag) {
            throw new BizRuntimeException(BizError.PLACE_ORDER_FAIL.getMessage());
        }
    }

    //真正执行更新库的操作
    @Override
    public void doCreateNewOrder(Long itemId, Long userId, Integer amount, Long promoId, Long stockLogId) {

        StockLogPO stockLogEntity = stockLogMapper.selectByPrimaryKey(stockLogId);
        if (null == stockLogEntity) {
            throw new BizRuntimeException(BizError.CAN_NOT_FIND_RECORD.getMessage());
        }
        //添加事务执行拦截
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {

            //这里只会在事务提交才会进入到这里
            @Override
            public void afterCommit() {
                super.afterCommit();
            }

            @Override
            public void afterCompletion(int status) {
                //status 0 表示成功 1表示回滚
                super.afterCompletion(status);
                if (status == 0) {
                    int row = stockLogMapper.updateStatusById(stockLogId, StockLogConstant.COMMIT);
                    if (row <= 0) {
                        log.error("execute order success but fail to update stockLogRecord status to commit ,stockLogId:{}", stockLogId);
                    }
                } else {
                    int row = stockLogMapper.updateStatusById(stockLogId, StockLogConstant.ROLLBACK);
                    if (row <= 0) {
                        log.error("execute order success but fail to update stockLogRecord status to rollback ,stockLogId:{}", stockLogId);
                    }
                    //将redis中库存数据恢复
                    redisTemplate.opsForHash().increment(PromoRedisConstant.PROMO_ITEM_STOCK, PromoRedisConstant.ITEM_KEY + itemId, amount);
                }
            }
        });

        ItemVO itemModel = itemService.initItem(itemId);
        if (ObjectUtils.isEmpty(itemModel)) {
            throw new BizRuntimeException(BizError.CAN_NOT_FIND_RECORD.getMessage());
        }


        PromoPO promoEntity = promoMapper.selectByPrimaryKey(promoId);
        if (ObjectUtils.isEmpty(promoEntity)) {
            throw new BizRuntimeException(BizError.CAN_NOT_FIND_RECORD.getMessage());
        }


        //生成订单
        String traceId = UUID.randomUUID().toString().replaceAll("-", "");
        OrderPO order = new OrderPO();
        order.setId(traceId);
        order.setAmount(amount);
        order.setItemId(itemId);
        order.setItemPrice(itemModel.getPrice());
        order.setOrderPrice(promoEntity.getPromoItemPrice());
        order.setUserId(userId);
        order.setPromoId(promoId);
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        orderMapper.insert(order);
    }

    private long insertUserStockLog(Long itemId, Long userId, Integer amount, Long promoId) {
        StockLogPO stockLog = new StockLogPO();
        stockLog.setAmount(amount);
        stockLog.setItemId(itemId);
        stockLog.setUserId(userId);
        stockLog.setStatus(StockLogConstant.WAIT_EXECUTE);
        stockLog.setPromoId(promoId);
        stockLog.setUpdateTime(new Date());
        stockLog.setCreateTime(new Date());
        stockLog.setExecuteNum(1);
        stockLogMapper.insert(stockLog);
        return stockLog.getId();
    }


}
