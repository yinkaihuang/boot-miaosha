package cn.bucheng.miaosha.mapper;

import cn.bucheng.miaosha.model.po.ItemStockPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface ItemStockMapper extends Mapper<ItemStockPO> {
    int decrementStock(@Param("itemStockId") long itemStockId, @Param("amount") int amount);
    int decrementStockByItemId(@Param("itemId") long itemId, @Param("amount") int amount);
}
