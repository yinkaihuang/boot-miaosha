package cn.bucheng.miaosha.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.util.Date;

/**
 * @author ：yinchong
 * @create ：2019/7/22 13:37
 * @description：活动促销对象
 * @modified By：
 * @version:
 */
@Data
@Alias("promot")
@Table(name = "ad_promo")
@NoArgsConstructor
@AllArgsConstructor
public class PromoPO extends BasePO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long itemId;
    private Date startTime;
    private Date endTime;
    private Integer promoItemPrice;
}
