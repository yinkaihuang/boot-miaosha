package cn.bucheng.miaosha.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author ：yinchong
 * @create ：2019/7/22 13:30
 * @description：
 * @modified By：基础实体对象
 * @version:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePO {
    private Date createTime;
    private Date updateTime;
    private String remark;
}
