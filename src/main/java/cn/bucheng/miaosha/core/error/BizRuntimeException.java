package cn.bucheng.miaosha.core.error;

/**
 * @author ：yinchong
 * @create ：2019/7/22 12:43
 * @description：
 * @modified By：
 * @version:
 */
public class BizRuntimeException extends RuntimeException {
    public BizRuntimeException(String message) {
        super(message);
    }
}
