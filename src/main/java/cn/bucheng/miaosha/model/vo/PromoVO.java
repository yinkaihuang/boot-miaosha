package cn.bucheng.miaosha.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @author ：yinchong
 * @create ：2019/7/22 14:42
 * @description：
 * @modified By：
 * @version:
 */
@Data
public class PromoVO  {
    private Long id;

    //秒杀活动状态 1表示还未开始，2表示进行中，3表示已结束
    private Integer status;

    //秒杀活动名称
    private String name;

    //秒杀活动的开始时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    //秒杀活动的结束时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    //秒杀活动的适用商品
    private Long itemId;

    //秒杀活动的商品价格(价格为分)
    private Integer promoItemPrice;
}
