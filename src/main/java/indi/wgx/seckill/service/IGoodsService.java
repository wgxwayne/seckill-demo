package indi.wgx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import indi.wgx.seckill.pojo.Goods;
import indi.wgx.seckill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 * 商品表 服务类
 * </p>
 *
 * @author xiaowei
 * @since 2022-03-20
 */
public interface IGoodsService extends IService<Goods> {

    /**
     * @return 获取商品列表
     */
    List<GoodsVo> findGoodsVo();

    /**
     *
     */
    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
