package cn.bucheng.miaosha.initialze;

import cn.bucheng.miaosha.constant.PromoRedisConstant;
import cn.bucheng.miaosha.mapper.ItemStockMapper;
import cn.bucheng.miaosha.mapper.PromoMapper;
import cn.bucheng.miaosha.model.po.ItemStockPO;
import cn.bucheng.miaosha.model.po.PromoPO;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author yinchong
 * @create 2020/9/7 21:38
 * @description
 */
@Component
public class ItemPromoInitiliaze implements InitializingBean {
    @Autowired
    private PromoMapper promoMapper;
    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<PromoPO> promoList = promoMapper.selectByExample(new Example(PromoPO.class));
        if(!CollectionUtils.isEmpty(promoList)){
            for(PromoPO promo:promoList) {
                Example example = new Example(ItemStockPO.class);
                example.createCriteria().andEqualTo("itemId",promo.getItemId());
                ItemStockPO itemStock = itemStockMapper.selectOneByExample(example);
                String hashKey = String.format(PromoRedisConstant.ITEM_KEY, promo.getItemId());
                //判断redis中某个商品促销数量否大于0
                 redisTemplate.opsForHash().putIfAbsent(PromoRedisConstant.PROMO_ITEM_STOCK, hashKey,String.valueOf(itemStock.getStock()));
            }
        }

    }
}
