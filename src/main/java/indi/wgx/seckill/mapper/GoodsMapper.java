package indi.wgx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import indi.wgx.seckill.pojo.Goods;
import indi.wgx.seckill.vo.GoodsVo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 商品表 Mapper 接口
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
@Component("goodsMapper")
public interface GoodsMapper extends BaseMapper<Goods> {
    /**
     * @return 获取商品列表
     */
    List<GoodsVo> findGoodsVo();

    /**
     * 根据商品Id获取商品列表
     * @param goodsId 商品Id
     * @return
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
