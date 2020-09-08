package cn.bucheng.miaosha.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ：yinchong
 * @create ：2019/7/22 15:00
 * @description：
 * @modified By：
 * @version:
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PromoActiveEnum {
    NOT_START(0, "活动为开始"),
    DO_ING(1, "活动进行中"),
    FINISH(-1, "活动已经结束了");


    private int code;
    private String message;
}
