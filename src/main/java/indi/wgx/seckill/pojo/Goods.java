package indi.wgx.seckill.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商品表
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods")
//@ApiModel(value="TGoods对象", description="商品表")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

//    @ApiModelProperty(value = "商品ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

//    @ApiModelProperty(value = "商品名称")
    @TableField("goods_name")
    private String goodsName;

//    @ApiModelProperty(value = "商品标题")
    @TableField("goods_title")
    private String goodsTitle;

//    @ApiModelProperty(value = "商品图片")
    @TableField("goods_img")
    private String goodsImg;

//    @ApiModelProperty(value = "商品详情")
    @TableField("goods_detail")
    private String goodsDetail;

//    @ApiModelProperty(value = "商品价格")
    @TableField("goods_price")
    private BigDecimal goodsPrice;

//    @ApiModelProperty(value = "商品库存，-1表示没有限制")
    @TableField("goods_stock")
    private Integer goodsStock;


}
