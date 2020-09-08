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
 * @description：商品库存表
 * @modified By：
 * @version:
 */
@Table(name = "ad_item_stock")
@Alias("itemStock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStockPO extends BasePO  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer stock;
    private Long itemId;
}
