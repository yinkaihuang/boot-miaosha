package cn.bucheng.miaosha.mapper;

import cn.bucheng.miaosha.model.po.StockLogPO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface StockLogMapper extends Mapper<StockLogPO> {
    int updateStatusById(@Param("id") Long id, @Param("status") Integer status);
}
