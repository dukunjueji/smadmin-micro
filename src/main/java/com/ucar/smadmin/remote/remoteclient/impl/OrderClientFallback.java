package com.ucar.smadmin.remote.remoteclient.impl;

import com.ucar.smapi.common.vo.RemoteResult;
import com.ucar.smapi.ord.dto.OrdCartGoodsDTO;
import com.ucar.smapi.ord.dto.OrdGoodsDTO;
import com.ucar.smapi.ord.dto.OrdMemberDTO;
import com.ucar.smapi.ord.dto.OrdOrderDTO;
import com.ucar.smapi.ord.dto.OrdOrderGoodsDTO;
import com.ucar.smapi.ord.dto.OrderDTO;
import com.ucar.smapi.ord.dto.OrderGoodsDTO;
import com.ucar.smapi.ord.re.CartGoodsRE;
import com.ucar.smapi.ord.re.CommentRE;
import com.ucar.smapi.ord.re.OrdOrderGoodsRE;
import com.ucar.smapi.ord.re.OrderGoodsRE;
import com.ucar.smapi.ord.re.OrderRE;
import com.ucar.smapi.ord.re.OrderSaleRE;
import com.ucar.smadmin.remote.remoteclient.OrderClientRemote;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * order熔断接口
 *
 * @author dk
 * @date 2019年1月5日 下午1:58:32
 * @version V1.0
 */
@Component
public class OrderClientFallback implements OrderClientRemote {
    /**
     * 根据用户ID和购物车商品表ID获取购物车商品信息
     *
     * @param ordGoodsDTO
     * @return
     */
    @Override
    public RemoteResult<List<CartGoodsRE>> getCarGoodsByIds(OrdGoodsDTO ordGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 根据用户ID获取购物车商品信息
     *
     * @param memberId
     * @return
     */
    @Override
    public RemoteResult<List<CartGoodsRE>> getCarGoodsById(Long memberId) {
        return RemoteResult.getServiceError();
    }

    /**
     * 根据用户id获取订单商品信息
     *
     * @param orderGodsList
     * @return
     */
    @Override
    public RemoteResult<List<OrdOrderGoodsRE>> getOrderGoodsById(List<OrdOrderGoodsDTO> orderGodsList) {
        return RemoteResult.getServiceError();
    }

    /**
     * 通过会员信息或者订单信息来查找订单信息
     *
     * @param ordMemberDTO （订单表id）
     * @return
     */
    @Override
    public RemoteResult<List<OrderRE>> getOrderByMemberDTO(OrdMemberDTO ordMemberDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 加入购物车
     *
     * @param ordCartGoodsDTO
     * @return
     */
    @Override
    public RemoteResult<Integer> addCarGoods(OrdCartGoodsDTO ordCartGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 删除购物车商品
     *
     * @param ordCartGoodsDTO
     * @return
     */
    @Override
    public RemoteResult<Integer> deleteCarGoods(OrdCartGoodsDTO ordCartGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 更新购物车商品数量
     *
     * @param ordCartGoodsDTO
     * @return 返回影响条数
     */
    @Override
    public RemoteResult<Integer> updateCarGoodsNum(OrdCartGoodsDTO ordCartGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 查找指定会员订单总记录数
     *
     * @param memberId 会员id
     * @return 所有订单数目
     */
    @Override
    public RemoteResult<Integer> queryOrderCount(Long memberId) {
        return RemoteResult.getServiceError();
    }

    /**
     * 获取订单查询分页
     *
     * @param orderDTO
     * @return
     */
    @Override
    public RemoteResult<List<OrderRE>> getOrderPage(OrdOrderDTO orderDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 获取总记录数
     *
     * @param orderDTO
     * @return
     */
    @Override
    public RemoteResult<Integer> getOrderTotal(OrdOrderDTO orderDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 批量逻辑删除订单
     *
     * @param list
     * @return
     */
    @Override
    public RemoteResult<Integer> logicDelOrder(List<Long> list) {
        return RemoteResult.getServiceError();
    }

    /**
     * 更新订单状态
     *
     * @param ordOrderDTO
     * @return int
     */
    @Override
    public RemoteResult<Integer> updateOrder(OrdOrderDTO ordOrderDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 根据主键id获取手机号
     *
     * @param id
     * @return
     */
    @Override
    public RemoteResult<String> getTelephoneById(Long id) {
        return RemoteResult.getServiceError();
    }

    /**
     * 当年每个月销售额
     *
     * @return java.util.List<java.math.BigDecimal>
     * @version 1.0 2018/11/12 11:31 by 吴佰川（baichuan.wu@ucarinc.com）创建
     */
    @Override
    public RemoteResult<OrderSaleRE> getOrderSaleData() {
        return RemoteResult.getServiceError();
    }

    /**
     * hhj
     * 根据用户id 获取购物车商品表数据数量
     *
     * @param memberId
     * @return
     */
    @Override
    public RemoteResult<Integer> queryCartGoodsCount(Long memberId) {
        return RemoteResult.getServiceError();
    }

    /**
     * hhj
     * 根据订单id订单商品信息
     *
     * @param orderId
     * @return
     */
    @Override
    public RemoteResult<List<OrderGoodsRE>> getOrderGoodsByOrderId(Integer orderId) {
        return RemoteResult.getServiceError();
    }

    /**
     * 插入订单并返回订单id
     *
     * @param orderDTO
     * @return
     */
    @Override
    public RemoteResult<Long> insertOrder(OrderDTO orderDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 插入订单商品信息
     *
     * @param orderGoodsDTO
     * @return
     */
    @Override
    public RemoteResult<Long> insertOrderGoods(OrderGoodsDTO orderGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 逻辑删除订单
     *
     * @param ordOrderDTO
     * @return
     */
    @Override
    public RemoteResult<Integer> memberDelOrder(OrdOrderDTO ordOrderDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 根据用户ID 和 订单状态获取待评价商品详情
     *
     * @param ordGoodsDTO
     * @return
     */
    @Override
    public RemoteResult<List<CommentRE>> getPropertyIdListByUid(OrdGoodsDTO ordGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 通过商品属性id获取待支付的商品属性数量
     *
     * @param propertyId
     * @return
     */
    @Override
    public RemoteResult<Integer> getUnPayGoodsPropertyCountByPropertyId(Long propertyId) {
        return RemoteResult.getServiceError();
    }

    /**
     * 根据id 更改订单商品评论状态
     *
     * @param ordOrderGoodsDTO
     * @return
     */
    @Override
    public RemoteResult<Integer> upOrdGoodsCommentStatus(OrdOrderGoodsDTO ordOrderGoodsDTO) {
        return RemoteResult.getServiceError();
    }

    /**
     * 根据订单商品表Id获取订单表信息
     *
     * @param ordGoodsId
     * @return
     */
    @Override
    public RemoteResult<OrderRE> getOrderByOrdGoodsId(Long ordGoodsId) {
        return RemoteResult.getServiceError();
    }
}
