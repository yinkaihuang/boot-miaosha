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
 * @create ：2019/7/22 13:27
 * @description：订单表
 * @modified By：
 * @version:
 */
@Alias("order")
@Table(name = "ad_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPO extends BasePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private Long userId;
    private Long itemId;
    private Integer itemPrice;
    private Integer amount;
    private Integer orderPrice;
    private Long promoId;
}
