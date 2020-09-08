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
 * @create ：2019/7/22 13:26
 * @description：商品基础信息表
 * @modified By：
 * @version:
 */
@Alias("item")
@Table(name = "ad_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemPO extends BasePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String title;
    //原理价格
    private Integer price;
    //打折后的价格
    private Integer sales;
    private String remark;
    private String imageUrl;
}
