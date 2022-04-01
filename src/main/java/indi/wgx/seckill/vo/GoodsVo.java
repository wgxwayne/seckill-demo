package indi.wgx.seckill.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import indi.wgx.seckill.pojo.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author xiaowei
 * @Date 2022/3/20 14:45
 * @Description: 商品返回对象
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsVo extends Goods {
    @TableField("seckill_price")
    private BigDecimal seckillPrice;

    @TableField("stock_count")
    private Integer stockCount;

    @TableField("start_date")
    private Date startDate;

    @TableField("end_date")
    private Date endDate;
}
