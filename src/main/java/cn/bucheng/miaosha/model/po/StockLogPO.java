package cn.bucheng.miaosha.model.po;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author ：yinchong
 * @create ：2019/7/22 20:02
 * @description：
 * @modified By： 库存变动记录表
 * @version:
 */
@Table(name = "ad_stock_log")
@Alias("stockLog")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockLogPO extends BasePO  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //商品id
    private Long itemId;
    //商品数量
    private Integer amount;
    //执行状态
    private Integer status;
    //用户id
    private Long userId;

    private Long promoId;

    private Integer executeNum;
}
