package cn.bucheng.miaosha.core.error;

import cn.bucheng.miaosha.core.resp.ServerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：yinchong
 * @create ：2019/7/22 12:42
 * @description：全局异常处理器
 * @modified By：
 * @version:
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({Exception.class, Throwable.class})
    @ResponseBody
    public ServerResult doError(HttpServletRequest req, HttpServletResponse resp, Exception ex) {
        log.error("响应异常:",ex);
        if (ex instanceof BizRuntimeException) {
            BizRuntimeException businessException = (BizRuntimeException) ex;
            return ServerResult.fail(1000, businessException.getMessage());
        } else if (ex instanceof ServletRequestBindingException) {
            return ServerResult.fail(1001, "路由出问题了");
        } else if (ex instanceof NoHandlerFoundException) {
            return ServerResult.fail(1002, "未找到访问路径");
        }
        return ServerResult.exception(ex.getMessage());
    }
}
