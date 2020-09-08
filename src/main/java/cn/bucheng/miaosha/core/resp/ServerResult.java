package cn.bucheng.miaosha.core.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ：yinchong
 * @create ：2019/7/22 12:44
 * @description：
 * @modified By：
 * @version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServerResult implements Serializable {
    private Integer code;
    private String message;
    private Object data;

    public static ServerResult fail(int code, String message){
        return new ServerResult(code,message,null);
    }

    public static ServerResult success(Object data){
        return new ServerResult(0,"操作成功",data);
    }

    public static ServerResult exception(Object data){
        return new ServerResult(-1,"服务器异常",data);
    }
}
