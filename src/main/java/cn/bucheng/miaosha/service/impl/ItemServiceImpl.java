package cn.bucheng.miaosha.service.impl;


import cn.bucheng.miaosha.constant.PromoActiveEnum;
import cn.bucheng.miaosha.core.error.BizError;
import cn.bucheng.miaosha.core.error.BizRuntimeException;
import cn.bucheng.miaosha.mapper.ItemMapper;
import cn.bucheng.miaosha.mapper.ItemStockMapper;
import cn.bucheng.miaosha.mapper.PromoMapper;
import cn.bucheng.miaosha.model.po.ItemPO;
import cn.bucheng.miaosha.model.po.ItemStockPO;
import cn.bucheng.miaosha.model.po.PromoPO;
import cn.bucheng.miaosha.model.vo.ItemVO;
import cn.bucheng.miaosha.model.vo.PromoVO;
import cn.bucheng.miaosha.service.ItemService;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author ：yinchong
 * @create ：2019/7/22 14:35
 * @description：
 * @modified By：
 * @version:
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class ItemServiceImpl  implements ItemService {
    @Autowired
    private PromoMapper promoMapper;
    @Autowired
    private ItemStockMapper itemStockMapper;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private ItemMapper itemMapper;


    @Override
    public List<ItemPO> listItems() {
        return itemMapper.selectByExample(new Example(ItemPO.class));
    }

    @Override
    public ItemVO initItem(Long id) {
        String item_data_str = redisTemplate.opsForValue().get("item_data_" + id);
        ItemVO item_data = null;
        if (!Objects.isNull(item_data)) {
            item_data = JSON.parseObject(item_data_str, ItemVO.class);
            return item_data;
        }
        ItemPO item = itemMapper.selectByPrimaryKey(id);
        if (Objects.isNull(item)) {
            throw new BizRuntimeException(BizError.CAN_NOT_FIND_RECORD.getMessage());
        }
        item_data = new ItemVO();
        BeanUtils.copyProperties(item, item_data);
        //获取库存
        ItemStockPO itemStock = new ItemStockPO();
        itemStock.setId(item.getId());
        itemStock = itemStockMapper.selectOne(itemStock);
        if (!Objects.isNull(itemStock)) {
            item_data.setStock(itemStock.getStock());
        }

        //获取促销
        Example example = new Example(PromoPO.class);
        example.createCriteria().andEqualTo("itemId",item.getId());
        List<PromoPO> promoEntities = promoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(promoEntities)) {
            PromoPO promoPO = promoEntities.get(0);
            PromoVO promoModel = new PromoVO();
            Date now = new Date();
            BeanUtils.copyProperties(promoPO, promoModel);
            promoModel.setItemId(promoPO.getItemId());
            if (promoPO.getStartTime().after(now)) {
                promoModel.setStatus(PromoActiveEnum.NOT_START.getCode());
            } else if (promoPO.getEndTime().before(now)) {
                promoModel.setStatus(PromoActiveEnum.FINISH.getCode());
            } else {
                promoModel.setStatus(PromoActiveEnum.DO_ING.getCode());
            }
            item_data.setPromoModel(promoModel);
        }
        //保存数据到redis中,并设置超时时间
        redisTemplate.opsForValue().set("item_data_" + id, JSON.toJSONString(item_data), 120, TimeUnit.SECONDS);
        return item_data;
    }
}
