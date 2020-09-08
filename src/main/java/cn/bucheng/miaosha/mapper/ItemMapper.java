package cn.bucheng.miaosha.mapper;

import cn.bucheng.miaosha.model.po.ItemPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author ：yinchong
 * @create ：2019/7/22 13:57
 * @description：
 * @modified By：
 * @version:
 */
public interface ItemMapper extends Mapper<ItemPO> {
    int incrementSales(@Param("itemId") long itemId, @Param("amount") int amount);
}
