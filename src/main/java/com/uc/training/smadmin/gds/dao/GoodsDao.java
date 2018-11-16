package com.uc.training.smadmin.gds.dao;

import com.uc.training.smadmin.gds.model.Goods;
import com.uc.training.smadmin.gds.model.HotTag;
import com.uc.training.smadmin.gds.re.AdminGoodsRE;
import com.uc.training.smadmin.gds.re.GoodsDetailRE;
import com.uc.training.smadmin.gds.re.GoodsRE;
import com.uc.training.smadmin.gds.re.GoodsStokeRE;
import com.uc.training.smadmin.gds.re.PropertyUrlRE;
import com.uc.training.smadmin.gds.vo.AdminPullGoodsVO;
import com.uc.training.smadmin.gds.vo.AdminUpdateGoodsVO;
import com.uc.training.smadmin.gds.vo.GoodsListVO;
import com.uc.training.smadmin.gds.vo.GoodsStokeVO;

import java.util.List;

/**
 * 请填写类注释
 *
 * @author zhongling(ling.zhong @ ucarinc.com)
 * @since 2018年10月16日 11:17
 */
public interface GoodsDao {
    /**
     * 获取热门推荐
     * @return
     */
    List<Long> getHotRecommend();

    /**
     * 获取热门推荐总数量
     * @return
     */
    Integer getHotRecommendCount();

    /**
     * 通过分类来获取商品数量
     * @param goodsListVO
     * @return
     */
    List<Long> getGoodsListCount(GoodsListVO goodsListVO);

    /**
     * 通过分类来获取商品
     * @param propertyIds
     * @return
     */
    List<GoodsRE> getGoodsList(List<Long> propertyIds);

    /**
     * 通过属性id商品详情
     * @param propertyId
     * @return
     */
    GoodsDetailRE getGoodsDetailByPropertyId(Long propertyId);

    /**
     * 通过属性id商品详情
     * @param propertyIds
     * @return
     */
    List<GoodsDetailRE> getGoodsDetailByPropertyIds(List<Long> propertyIds);

    /**
     * 更新销量
     * @param goodsStokeVO key为goodsId、sales
     * @return
     */
    int updateSales(GoodsStokeVO goodsStokeVO);

    /**
     * 通过商品id获取所有规格的商品详情
     * @param goodsId
     * @return
     */
    List<GoodsDetailRE> getGoodsDetailByGoodsId(Long goodsId);


    /**
     * 减库存之前，查看商品是否下架、删除、检查库存是否足够
     * @param goodsStokeVO
     * @return
     */
    GoodsStokeRE selectGoodsStatus(GoodsStokeVO goodsStokeVO);

    /**
     * 减库存
     * @param goodsStokeVO
     * @return
     */
    int updateAndDeductStoke(GoodsStokeVO goodsStokeVO);

    /**
     * 模糊查询商品列表总数量
     * @param goodsListVO
     * @return
     */
    Integer searchCountByGoodsName(GoodsListVO goodsListVO);

    /**
     * 模糊查询商品列表
     * @param goodsListVO
     * @return
     */
    List<Long> searchByGoodsName(GoodsListVO goodsListVO);

    /**
     * 通过属性查询商品列表
     * @param propertyIds
     * @return
     */
    List<GoodsRE> searchByPropertyId(List<Long> propertyIds);

    /**
     * 获取热门标签
     * @return
     */
    List<HotTag> selectHotTag();

    /**
     * 通过属性id获取相应的图片
     * @param propertyId
     * @return
     */
    List<PropertyUrlRE> getPicUrlByPropertyId(Long propertyId);

    /**
     * 通过属性list列表id获取相应的图片
     * @param propertyId
     * @return
     */
    List<List<PropertyUrlRE>> getPicUrlByPropertyIds(List<Long> propertyId);

    /**
     * 后台获取商品列表
     * @param goodsListVO
     * @return
     */
    List<AdminGoodsRE> getAdminGoodsList(GoodsListVO goodsListVO);

    /**
     * 新增商品
     * @param goods
     * @return
     */
    Long insertGoods(Goods goods);

    /**
     * 更新商品
     * @param adminUpdateGoodsVO
     * @return
     */
    Integer updateGoods(AdminUpdateGoodsVO adminUpdateGoodsVO);

    /**
     * 逻辑删除商品
     * @param id
     * @return
     */
    Integer logicDeleteGoods(Long id);

    /**
     * 后台获取商品数量
     * @param goodsListVO
     * @return
     */
    Long getAdminGoodsListCount(GoodsListVO goodsListVO);

    /**
     * 商品上架
     * @param adminPullGoodsVO
     * @return
     */
    Integer pullOnGoods(AdminPullGoodsVO adminPullGoodsVO);

    /**
     * 商品下架
     * @param adminPullGoodsVO
     * @return
     */
    Integer pullOffGoods(AdminPullGoodsVO adminPullGoodsVO);
}