package cn.bucheng.miaosha.controller;

import cn.bucheng.miaosha.constant.PromoRedisConstant;
import cn.bucheng.miaosha.core.error.BizError;
import cn.bucheng.miaosha.core.error.BizRuntimeException;
import cn.bucheng.miaosha.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.*;

/**
 * @author yinchong
 * @create 2020/9/6 21:15
 * @description
 */
@RestController
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    private ExecutorService executor = Executors.newFixedThreadPool(10);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @RequestMapping("createNewOrder")
    public String createNewOrder(@RequestParam("itemId") Long itemId,@RequestParam("promoId") Long promoId,@RequestParam("userId") Long userId,@RequestParam("amount") Integer amount) {
        String hashKey = String.format(PromoRedisConstant.ITEM_KEY ,itemId);
        //判断redis中某个商品促销数量否大于0
        String value = (String) redisTemplate.opsForHash().get(PromoRedisConstant.PROMO_ITEM_STOCK, hashKey);
        if (Integer.parseInt(value) <= 0) {
            throw new BizRuntimeException(BizError.NO_AVAILABLE_RECORD.getMessage());
        }
        //减掉redis缓存中的数据(这里是不是可以提前到发送MQ消息时)
        Long count = redisTemplate.opsForHash().increment(PromoRedisConstant.PROMO_ITEM_STOCK, hashKey, -amount);
        if (count < 0) {
            redisTemplate.opsForHash().increment(PromoRedisConstant.PROMO_ITEM_STOCK, hashKey, amount);
            throw new BizRuntimeException(BizError.NO_AVAILABLE_RECORD.getMessage());
        }
        //如果未售空进入队列中
        Future<Void> future = executor.submit(() -> {
            orderService.createNewOrder(itemId, userId, amount, promoId);
            return null;
        });

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }
}
