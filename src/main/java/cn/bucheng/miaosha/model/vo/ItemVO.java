package cn.bucheng.miaosha.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author ：yinchong
 * @create ：2019/7/22 14:41
 * @description：
 * @modified By：
 * @version:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemVO  {
    private Long id;

    @NotBlank(message = "商品名称不能为空")
    private String name;

    //商品名称
    @NotBlank(message = "商品标题不能为空")
    private String title;

    //商品价格
    @NotNull(message = "商品价格不能为空")
    @Min(value = 0,message = "商品价格必须大于0")
    private Integer price;

    //商品的库存
    @NotNull(message = "库存不能不填")
    private Integer stock;

    //商品的描述
    @NotBlank(message = "商品描述信息不能为空")
    private String describe;

    //商品的销量
    private Integer sales;

    //商品描述图片的url
    @NotBlank(message = "商品图片信息不能为空")
    private String imageUrl;

    //使用聚合模型,如果promoModel不为空，则表示其拥有还未结束的秒杀活动
    private PromoVO promoModel;
}
