package cn.bucheng.miaosha.service.impl;

import cn.bucheng.miaosha.constant.PromoRedisConstant;
import cn.bucheng.miaosha.core.error.BizError;
import cn.bucheng.miaosha.core.error.BizRuntimeException;
import cn.bucheng.miaosha.mapper.ItemStockMapper;
import cn.bucheng.miaosha.mapper.PromoMapper;
import cn.bucheng.miaosha.model.po.ItemStockPO;
import cn.bucheng.miaosha.model.po.PromoPO;
import cn.bucheng.miaosha.service.PromoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

/**
 * @author ：yinchong
 * @create ：2019/7/22 19:32
 * @description：
 * @modified By：
 * @version:
 */
@Service
public class PromoServiceImpl  implements PromoService {

    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PromoMapper promoMapper;

    @Override
    public void pushPromo(long promoId) {
        PromoPO promo = promoMapper.selectByPrimaryKey(promoId);
        if (ObjectUtils.isEmpty(promo)) {
            throw new BizRuntimeException(BizError.CAN_NOT_FIND_RECORD.getMessage());
        }
        if (promo.getEndTime().before(new Date())) {
            throw new BizRuntimeException(BizError.NO_AVAILABLE_OPERATION.getMessage());
        }

        //将数据刷新到redis中一般是5倍关系
        ItemStockPO stock = new ItemStockPO();
        stock.setItemId(promo.getItemId());
        stock = itemStockMapper.selectOne(stock);
        int stockNum = stock.getStock();
        //清除掉售空标记
        redisTemplate.opsForHash().delete(PromoRedisConstant.PROMO_SOLD_OUT, PromoRedisConstant.ITEM_KEY + promo.getItemId());
        //将商品的库存数量存放到redis中
        redisTemplate.opsForHash().put(PromoRedisConstant.PROMO_ITEM_STOCK, PromoRedisConstant.ITEM_KEY + promo.getItemId(), stockNum+"");
    }
}