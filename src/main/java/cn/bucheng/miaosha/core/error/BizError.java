package cn.bucheng.miaosha.core.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ：yinchong
 * @create ：2019/7/22 14:48
 * @description：
 * @modified By：
 * @version:
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum BizError {
    CAN_NOT_FIND_RECORD(5001, "未找到对应的记录"),
    INVALID_NUMBER(5002, "非法数字"),
    EXECUTE_SQL_FAIL(5003, "未能成功执行sql语句"),
    NO_AVAILABLE_RECORD(5004,"没有可用的数据"),
    NO_AVAILABLE_OPERATION(5005,"无效操作"),
    UNKNOWN_ERROR(5006,"未知错误"),
    PLACE_ORDER_FAIL(5007,"下单失败"),
    ;
    private int code;
    private String message;
}
