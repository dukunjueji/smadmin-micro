package com.uc.training.ord.service.impl;

import com.uc.training.common.enums.GoodsStatusEnum;
import com.uc.training.common.enums.OrderEnum;
import com.uc.training.common.enums.UUIDTypeEnum;
import com.uc.training.common.utils.UUIDUtil;
import com.uc.training.common.vo.RemoteResult;
import com.uc.training.ord.re.CartGoodsRE;
import com.uc.training.ord.re.OrdOrderGoodsRE;
import com.uc.training.ord.re.OrderConfirmRE;
import com.uc.training.ord.re.OrderGoodsDetailRE;
import com.uc.training.ord.re.OrderGoodsRE;
import com.uc.training.ord.re.OrderInfoRE;
import com.uc.training.ord.re.OrderRE;
import com.uc.training.ord.re.OrderSaleRE;
import com.uc.training.ord.re.OrderStatusRE;
import com.uc.training.ord.service.OrderService;
import com.uc.training.ord.vo.OrdCartGoodsVO;
import com.uc.training.ord.vo.OrdGoodsVO;
import com.uc.training.ord.vo.OrdMemberVO;
import com.uc.training.ord.vo.OrdOrderGoodsVO;
import com.uc.training.ord.vo.OrdOrderVO;
import com.uc.training.ord.vo.OrderGoodsVO;
import com.uc.training.ord.vo.OrderVO;
import com.uc.training.remote.client.OrderClient;
import com.uc.training.smadmin.bd.re.AddressRE;
import com.uc.training.smadmin.gds.re.GoodsDetailRE;
import com.uc.training.smadmin.gds.re.GoodsStokeRE;
import com.uc.training.smadmin.gds.vo.CommentVO;
import com.uc.training.smadmin.gds.vo.GoodsStokeVO;
import com.zuche.framework.remote.RemoteClientFactory;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现
 *
 * @author DK
 * @date 2018/10/19
 */
@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 更新购物车商品数量
     */
    private static final String UPDATE_CAR_GOODS_NUM = "smorder.api.updateCarGoodsNum";
    /**
     * 根据用户ID和购物车商品表ID获取购物车商品信息
     */
    private static final String GET_CAR_GOODS_BY_IDS = "smorder.api.getCarGoodsByIds";
    /**
     * 根据用户id 获取购物车商品表数据数量
     */
    private static final String GET_ORDER_GOODS_BY_ORDER_ID = "smorder.api.getOrderGoodsByOrderId";
    /**
     * 加入购物车
     */
    private static final String ADD_CAR_GOODS = "smorder.api.addCarGoods";
    /**
     * 删除购物车
     */
    private static final String DELETE_CAR_GOODS = "smorder.api.deleteCarGoods";
    /**
     * 插入订单信息并返回订单id
     */
    private static final String INSERT_ORDER = "smorder.api.insertOrder";
    /**
     * 插入订单商品信息
     */
    private static final String INSERT_ORDER_GOODS = "smorder.api.insertOrderGoods";


    /**
     * 根据用户id查询购物车信息表
     *
     * @param memberId
     * @return
     */

    @Override
    public List<CartGoodsRE> getCarGoodsById(Long memberId) {
        return OrderClient.getCarGoodsById(memberId);
    }

    /**
     * 更新购物车商品数量
     *
     * @param ordCartGoodsVO
     * @return 返回影响条数
     */
    @Override
    public Integer updateCarGoodsNum(OrdCartGoodsVO ordCartGoodsVO) {
        return OrderClient.updateCarGoodsNum(ordCartGoodsVO);
    }

    /**
     * 通过会员id来查找
     *
     * @param ordMemberVO （订单表id）
     * @return
     */
    @Override
    public List<OrderRE> getOrderByMemberVO(OrdMemberVO ordMemberVO) {
        return OrderClient.getOrderByMemberVO(ordMemberVO);
    }

    @Override
    public List<CartGoodsRE> getCarGoodsByIds(OrdGoodsVO ordGoodsVO) {
        return OrderClient.getCarGoodsByIds(ordGoodsVO);
    }

    /**
     * 获取订单商品列表(提交订单页)
     *
     * @param orderGodsList
     * @return
     */
    @Override
    public List<OrdOrderGoodsRE> getOrderGoodsById(List<OrdOrderGoodsVO> orderGodsList) {
        List<OrdOrderGoodsRE> list = new ArrayList<>();
        OrdGoodsVO ordGoodsVO = new OrdGoodsVO();
        if (!CollectionUtils.isEmpty(orderGodsList)) {
            ordGoodsVO.setMemberId(orderGodsList.get(0).getMemberId());
            List<Long> propertyIds = new ArrayList<>();
            for (int i = 0; i < orderGodsList.size(); i++) {
                propertyIds.add(orderGodsList.get(i).getPropertyId());
            }
            ordGoodsVO.setGoodsPropertyIdList(propertyIds);
        } else {
            return list;
        }
        List<CartGoodsRE> goodsNumList = OrderClient.getCarGoodsByIds(ordGoodsVO);
        OrdOrderGoodsRE ordOrderGoodsRE;
        for (int i = 0; i < orderGodsList.size(); i++) {
            ordOrderGoodsRE = new OrdOrderGoodsRE();
            GoodsDetailRE gdDTO = goodsService.getGoodsDetailByPropertyId(orderGodsList.get(i).getPropertyId());
            if (gdDTO == null) {
                return list;
            }
            ordOrderGoodsRE.setGoodsId(orderGodsList.get(i).getGoodsId());
            ordOrderGoodsRE.setGdsName(gdDTO.getName());
            if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                ordOrderGoodsRE.setGdsUrl(gdDTO.getPicUrl().get(0).getPicUrl());
            }
            if (!CollectionUtils.isEmpty(goodsNumList)) {
                for (int j = 0; j < goodsNumList.size(); j++) {
                    if (goodsNumList.get(j).getGoodsPropertyId().equals(orderGodsList.get(i).getPropertyId())) {
                        ordOrderGoodsRE.setNum(goodsNumList.get(j).getGoodsNum());
                        break;
                    }
                }
            }
            ordOrderGoodsRE.setPropertyId(orderGodsList.get(i).getPropertyId());
            ordOrderGoodsRE.setProperty(gdDTO.getProperty());
            ordOrderGoodsRE.setSalePrice(gdDTO.getSalePrice());
            ordOrderGoodsRE.setDiscountPrice(gdDTO.getDiscountPrice());
            ordOrderGoodsRE.setStatus(gdDTO.getStatus());
            ordOrderGoodsRE.setIsDiscount(gdDTO.getIsDiscount());
            list.add(ordOrderGoodsRE);
        }
        return list;
    }

    /**
     * 获取订单商品列表(已生成订单)
     *
     * @param orderGodsList
     * @return
     */
    @Override
    public List<OrdOrderGoodsRE> getOrderGoods(List<OrdOrderGoodsVO> orderGodsList, Long orderId) {
        List<OrdOrderGoodsRE> list = new ArrayList<>();
        OrdOrderGoodsRE ordOrderGoodsRE;
        OrdOrderGoodsVO ordOrderGoodsVO;
        List<OrderGoodsRE> orderGdsList = OrderClient.getOrderGoodsByOrderId(orderId.intValue());
        ;
        if (CollectionUtils.isEmpty(orderGdsList)) {
            return null;
        }
        for (int i = 0; i < orderGodsList.size(); i++) {
            ordOrderGoodsVO = new OrdOrderGoodsVO();
            ordOrderGoodsRE = new OrdOrderGoodsRE();
            for (int j = 0; j < orderGdsList.size(); j++) {
                if (orderGdsList.get(j).getGoodsPropertyId().equals(orderGodsList.get(i).getPropertyId())) {
                    ordOrderGoodsVO.setSalePrice(orderGdsList.get(j).getSalePrice());
                    ordOrderGoodsVO.setDiscountPrice(orderGdsList.get(j).getDiscountPrice());
                    ordOrderGoodsVO.setPayPrice(orderGdsList.get(j).getPayPrice());
                    ordOrderGoodsVO.setNum(orderGdsList.get(j).getGoodsNum());
                }
            }
            GoodsDetailRE gdDTO = goodsService.getGoodsDetailByPropertyId(orderGodsList.get(i).getPropertyId());
            if (gdDTO == null) {
                return list;
            }
            ordOrderGoodsVO.setGoodsId(orderGodsList.get(i).getGoodsId());
            ordOrderGoodsVO.setGdsName(gdDTO.getName());
            if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                ordOrderGoodsVO.setGdsUrl(gdDTO.getPicUrl().get(0).getPicUrl());
            }
            ordOrderGoodsVO.setPropertyId(orderGodsList.get(i).getPropertyId());
            ordOrderGoodsVO.setProperty(gdDTO.getProperty());
            ordOrderGoodsVO.setStatus(gdDTO.getStatus());
            ordOrderGoodsVO.setIsDiscount(gdDTO.getIsDiscount());
            BeanUtils.copyProperties(ordOrderGoodsVO, ordOrderGoodsRE);
            list.add(ordOrderGoodsRE);
        }
        return list;
    }

    /**
     * 加入购物车
     *
     * @param ordCartGoodsVO
     * @return
     */
    @Override
    public Integer addCarGoods(OrdCartGoodsVO ordCartGoodsVO) {
        return OrderClient.addCarGoods(ordCartGoodsVO);
    }

    /**
     * 删除购物车商品
     *
     * @param ordCartGoodsVO
     * @return
     */
    @Override
    public Integer deleteCarGoods(OrdCartGoodsVO ordCartGoodsVO) {
        return OrderClient.deleteCarGoods(ordCartGoodsVO);
    }


    @Override
    public Integer queryOrderCount(Long memberId) {
        return this.orderDao.queryOrderCount(memberId);
    }

    /**
     * 判断该商品是否下架、删除、无库存
     */
    private List<OrderConfirmRE> validationGoods(List<OrdOrderGoodsVO> orderInfoListNow) {
        List<OrderConfirmRE> list = new ArrayList<>();
        OrderConfirmRE orderConfirmRE = new OrderConfirmRE();
        int a = 2;
        GoodsStokeVO goodsStokeVO;
        if (orderInfoListNow.size() <= a) {
            return list;
        }
        for (int i = 0; i < orderInfoListNow.size() - a; i++) {
            //更新库存表、插入用户订单表和订单商品信息表、删除购物车商品信息,判断商品是否删除或者下架和库存是否足够
            goodsStokeVO = new GoodsStokeVO();
            goodsStokeVO.setPropertyId(orderInfoListNow.get(i).getPropertyId());
            goodsStokeVO.setStoke((long) orderInfoListNow.get(i).getNum());
            GoodsStokeRE goodsStokeRE = goodsService.selectGoodsStatus(goodsStokeVO);
            if (goodsStokeRE.getIsDelete().equals(GoodsStatusEnum.GOODS_DELETE.getType())) {
                StringBuilder temp = new StringBuilder();
                temp.append("您的商品：" + goodsStokeRE.getGoodsName() + "\n" + "规格:");
                temp.append(goodsStokeRE.getGoodsProperty() + "已经被删除了，点击返回购物车，再重新选择");
                orderConfirmRE.setShowStatus(temp.toString());
                orderConfirmRE.setGoodsStatus(OrderEnum.NOORDER.getKey());
                list.add(orderConfirmRE);
                return list;
            }
            if (goodsStokeRE.getStatus().equals(GoodsStatusEnum.GOODS_IS_SHELVES.getType())) {
                StringBuilder temp = new StringBuilder();
                temp.append("您的商品：" + goodsStokeRE.getGoodsName() + "\n" + "规格:");
                temp.append(goodsStokeRE.getGoodsProperty() + "已经被下架了，点击返回购物车，再重新选择");
                orderConfirmRE.setShowStatus(temp.toString());
                orderConfirmRE.setGoodsStatus(OrderEnum.NOORDER.getKey());
                list.add(orderConfirmRE);
                return list;
            }
            if (goodsStokeRE.getStoke() < orderInfoListNow.get(i).getNum()) {
                StringBuilder temp = new StringBuilder();
                temp.append("您的商品：" + goodsStokeRE.getGoodsName() + "\n" + "规格:");
                temp.append(goodsStokeRE.getGoodsProperty() + "已经被卖完了，点击返回购物车，再重新选择");
                orderConfirmRE.setShowStatus(temp.toString());
                orderConfirmRE.setGoodsStatus(OrderEnum.NOORDER.getKey());
                list.add(orderConfirmRE);
                return list;
            }
        }
        orderConfirmRE.setGoodsStatus(OrderEnum.WAITPAY.getKey());
        list.add(orderConfirmRE);
        return list;
    }

    /**
     * 提交订单验证信息
     */
    @Override
    public List<OrderConfirmRE> confirmOrderInfo(List<OrdOrderGoodsVO> orderInfoListNow) {
        OrderConfirmRE orderConfirmRE = new OrderConfirmRE();
        List<OrderConfirmRE> list = new ArrayList<>();
        GoodsStokeVO goodsStokeVO;
        // 判断该商品是否下架、删除、无库存
        List<OrderConfirmRE> goodsStatusList = validationGoods(orderInfoListNow);
        if (CollectionUtils.isEmpty(goodsStatusList)) {
            return goodsStatusList;
        } else if (goodsStatusList.get(0).getGoodsStatus() != (int) OrderEnum.WAITPAY.getKey().longValue()) {
            return goodsStatusList;
        }
        // 判断前台传来的价格信息是否与购物车一致
        OrdGoodsVO ordGoodsVO = new OrdGoodsVO();
        ordGoodsVO.setMemberId(orderInfoListNow.get(orderInfoListNow.size() - 2).getMemberId());
        List<Long> propertyIds = new ArrayList<>();
        int a = 2;
        for (int i = 0; i < orderInfoListNow.size() - a; i++) {
            propertyIds.add(orderInfoListNow.get(i).getPropertyId());
        }
        ordGoodsVO.setGoodsPropertyIdList(propertyIds);
        //调用远程服务查询购物车商品信息
        List<CartGoodsRE> goodsNumList = OrderClient.getCarGoodsByIds(ordGoodsVO);
        OrdOrderGoodsVO ordOrderGoodsVO;
        Double memberDiscountPoint = goodsService.getMemberDiscountPoint(orderInfoListNow.get(orderInfoListNow.size() - 2).getMemberId());
        if (memberDiscountPoint == null) {
            return list;
        }
        BigDecimal payPrice = new BigDecimal(0);
        for (int i = 0; i < orderInfoListNow.size() - a; i++) {
            ordOrderGoodsVO = new OrdOrderGoodsVO();
            GoodsDetailRE gdDTO = goodsService.getGoodsDetailByPropertyId(orderInfoListNow.get(i).getPropertyId());
            if (gdDTO == null) {
                return list;
            }
            if (!CollectionUtils.isEmpty(goodsNumList)) {
                for (int j = 0; j < goodsNumList.size(); j++) {
                    if (goodsNumList.get(j).getGoodsPropertyId().equals(orderInfoListNow.get(i).getPropertyId())) {
                        ordOrderGoodsVO.setNum(goodsNumList.get(j).getGoodsNum());
                        if (gdDTO.getIsDiscount() == 1) {
                            payPrice = gdDTO.getDiscountPrice().multiply(BigDecimal.valueOf(goodsNumList.get(j).getGoodsNum() * memberDiscountPoint)).add(payPrice);
                        } else {
                            payPrice = gdDTO.getSalePrice().multiply(BigDecimal.valueOf(goodsNumList.get(j).getGoodsNum() * memberDiscountPoint)).add(payPrice);
                        }
                        payPrice = payPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
                        break;
                    }
                }
            }
        }
        if (payPrice.compareTo(orderInfoListNow.get(orderInfoListNow.size() - 1).getTotalPrice()) != 0) {
            return list;
        }
        //插入用户订单表
        OrderVO order = new OrderVO();
        order.setMemberId(orderInfoListNow.get(orderInfoListNow.size() - a).getMemberId());
        order.setOrderPrice(orderInfoListNow.get(orderInfoListNow.size() - 1).getOrderPrice());
        order.setPayPrice(orderInfoListNow.get(orderInfoListNow.size() - 1).getTotalPrice());
        //插入地址信息
        AddressRE addressRE = addressService.getAddressById(orderInfoListNow.get(orderInfoListNow.size() - 2).getAddressId());
        if (addressRE != null) {
            order.setReceiptAddress(addressRE.getProvince() + addressRE.getCity() + addressRE.getDistrict() + addressRE.getAddrDetail());
            order.setReceiptName(addressRE.getReceiver());
            order.setReceiptTel(addressRE.getTelephone());
        }
        order.setStatus(OrderEnum.WAITPAY.getKey());
        order.setIsDelete(0);
        //生成订单编号
        order.setOrderNum(UUIDUtil.getUuidByType(UUIDTypeEnum.ORDERID.getType()));
        Long oderId = OrderClient.insertOrder(order);
        if (oderId != null) {
            order.setId(oderId);
        } else {
            return list;
        }
        //遍历orderInfoListNow
        OrderGoodsVO orderGoods;
        OrdCartGoodsVO ordCartGoodsVO;
        for (int i = 0; i < orderInfoListNow.size() - a; i++) {
            //插入订单商品信息表
            orderGoods = new OrderGoodsVO();
            goodsStokeVO = new GoodsStokeVO();
            orderGoods.setOrderId(oderId);
            orderGoods.setGoodsId(orderInfoListNow.get(i).getGoodsId());
            orderGoods.setPayPrice(orderInfoListNow.get(i).getPayPrice());
            orderGoods.setGoodsPropertyId(orderInfoListNow.get(i).getPropertyId());
            orderGoods.setGoodsNum(orderInfoListNow.get(i).getNum());
            orderGoods.setSalePrice(orderInfoListNow.get(i).getSalePrice());
            orderGoods.setDiscountPrice(orderInfoListNow.get(i).getDiscountPrice());
            orderGoods.setGoodsPropertyId(orderInfoListNow.get(i).getPropertyId());
            OrderClient.insertOrderGoods(ordGoodsVO);
            //更新商品对应的库存
            goodsStokeVO.setStoke(orderGoods.getGoodsNum().longValue());
            goodsStokeVO.setPropertyId(orderGoods.getGoodsPropertyId());
            goodsService.updateAndDeductStoke(goodsStokeVO);
            //删除购物车信息表
            ordCartGoodsVO = new OrdCartGoodsVO();
            ordCartGoodsVO.setPropertyId(orderInfoListNow.get(i).getPropertyId());
            ordCartGoodsVO.setMemberId(orderInfoListNow.get(orderInfoListNow.size() - a).getMemberId());
            deleteCarGoods(ordCartGoodsVO);
        }
        orderConfirmRE.setShowStatus("已成功生成订单");
        orderConfirmRE.setGoodsStatus((int) OrderEnum.WAITPAY.getKey().longValue());
        orderConfirmRE.setOrderId(order.getId());
        list.add(orderConfirmRE);
        return list;
    }

    @Override
    public List<OrderRE> getOrderPage(OrdOrderVO ordOrderVO) {
        return OrderClient.getOrderPage(ordOrderVO);
    }

    @Override
    public Integer getOrderTotal(OrdOrderVO ordOrderVO) {
        return OrderClient.getOrderTotal(ordOrderVO);
    }

    @Override
    public List<OrderStatusRE> getOrderEnum() {
        List<OrderStatusRE> list = new ArrayList<>();
        OrderEnum orderEnum;
        int max = OrderEnum.getMaxKey();
        int i = 0;
        do {
            OrderStatusRE orderStatusRe = new OrderStatusRE();
            orderEnum = OrderEnum.getEnumByKey(i);
            if (orderEnum != null) {
                orderStatusRe.setValue(i);
                orderStatusRe.setLabel(orderEnum.getValue());
                list.add(orderStatusRe);
            }
            i++;
        } while (max >= i);
        OrderStatusRE orderStatusRE = new OrderStatusRE();
        orderStatusRE.setLabel("全部");
        list.add(orderStatusRE);
        return list;
    }

    @Override
    public Integer logicDelOrder(List<Long> list) {
        return OrderClient.logicDelOrder(list);
    }

    /**
     * 更新订单状态
     *
     * @param ordOrderVO
     * @return int
     */
    @Override
    public int updateOrder(OrdOrderVO ordOrderVO) {
        OrdMemberVO ordMemberVO = new OrdMemberVO();
        ordMemberVO.setOrderNum(ordOrderVO.getOrderNum());
        List<OrderRE> getOrdOrderVO = OrderClient.getOrderByMemberVO(ordMemberVO);
        if (getOrdOrderVO == null) {
            return 0;
        }
        //如果是取消订单回退库存
        if (ordOrderVO.getStatus().equals(OrderEnum.CANCEL.getKey().longValue())) {
            //需要取消订单的状态为待付款状态
            if (!getOrdOrderVO.get(0).getStatus().equals(OrderEnum.WAITPAY.getKey().longValue())) {
                return 0;
            }
            List<OrderGoodsDetailRE> list = getOrderGdsById(ordOrderVO.getOrderId());
            if (!CollectionUtils.isEmpty(list)) {
                GoodsStokeVO goodsStokeVO = new GoodsStokeVO();
                for (OrderGoodsDetailRE orderGoodsDetailRe : list) {
                    goodsStokeVO.setStoke(-(orderGoodsDetailRe.getGoodsNum().longValue()));
                    goodsStokeVO.setPropertyId(orderGoodsDetailRe.getGoodsPropertyId());
                    goodsService.updateAndDeductStoke(goodsStokeVO);
                }
            } else {
                return 0;
            }
        }
        return OrderClient.updateOrder(ordOrderVO);
    }


    @Override
    public List<OrderGoodsDetailRE> getOrderGdsById(Integer id) {
        List<OrderGoodsRE> orderGdsList = OrderClient.getOrderGoodsByOrderId(id);
        if (CollectionUtils.isEmpty(orderGdsList)) {
            return null;
        }
        OrderGoodsDetailRE orderGoodsDetailRe;
        GoodsDetailRE gdDTO;
        List<OrderGoodsDetailRE> list = new ArrayList<>();
        for (OrderGoodsRE orderGoods : orderGdsList) {
            orderGoodsDetailRe = new OrderGoodsDetailRE();
            gdDTO = goodsService.getGoodsDetailByPropertyId(orderGoods.getGoodsPropertyId());
            if (gdDTO != null) {
                orderGoodsDetailRe.setGoodsName(gdDTO.getName());
                orderGoodsDetailRe.setGoodsNum(orderGoods.getGoodsNum());
                orderGoodsDetailRe.setGoodsProperty(gdDTO.getProperty());
                orderGoodsDetailRe.setGoodsPrice(gdDTO.getSalePrice());
                orderGoodsDetailRe.setGoodsPrice(gdDTO.getSalePrice());
                orderGoodsDetailRe.setGoodsPropertyId(orderGoods.getGoodsPropertyId());
                if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                    orderGoodsDetailRe.setGoodsUrl(gdDTO.getPicUrl().get(0).getPicUrl());
                }
                list.add(orderGoodsDetailRe);
            }
        }
        return list;
    }

    /**
     * 根据主键id获取手机号
     *
     * @param id
     * @return
     */
    @Override
    public String getTelephoneById(Long id) {
        return OrderClient.getTelephoneById(id);
    }

    /**
     * 根据订单id获取用户订单商品信息
     *
     * @param id
     * @return
     */
    private List<OrderGoodsDetailRE> getOrderGdsByIds(Integer id) {
        List<OrderGoodsRE> orderGdsList = OrderClient.getOrderGoodsByOrderId(id);
        if (CollectionUtils.isEmpty(orderGdsList)) {
            return null;
        }
        GoodsDetailRE gdDTO;
        OrderGoodsDetailRE orderGoodsDetailRE;
        List<OrderGoodsDetailRE> list = new ArrayList<>();
        for (OrderGoodsRE orderGoods : orderGdsList) {
            orderGoodsDetailRE = new OrderGoodsDetailRE();
            gdDTO = goodsService.getGoodsDetailByPropertyId(orderGoods.getGoodsPropertyId());
            if (gdDTO != null) {
                orderGoodsDetailRE.setStock(gdDTO.getStock());
                orderGoodsDetailRE.setIsDelete(gdDTO.getIsDelete());
                orderGoodsDetailRE.setStatus(gdDTO.getStatus());
                orderGoodsDetailRE.setGoodsPrice(orderGoods.getSalePrice());
                orderGoodsDetailRE.setGoodsName(gdDTO.getName());
                orderGoodsDetailRE.setDiscountPrice(orderGoods.getDiscountPrice());
                orderGoodsDetailRE.setIsDiscount(gdDTO.getIsDiscount());
                orderGoodsDetailRE.setGoodsNum(orderGoods.getGoodsNum());
                orderGoodsDetailRE.setGoodsProperty(gdDTO.getProperty());
                orderGoodsDetailRE.setPayPrice(orderGoods.getPayPrice());
                orderGoodsDetailRE.setGoodsPropertyId(orderGoods.getGoodsPropertyId());
                orderGoodsDetailRE.setGoodsId(gdDTO.getGoodsId());
                if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                    orderGoodsDetailRE.setGoodsUrl(gdDTO.getPicUrl().get(0).getPicUrl());
                }
                list.add(orderGoodsDetailRE);
            }
        }
        return list;
    }

    /**
     * 根据会员id获取订单信息
     *
     * @param ordMemberVO
     * @return
     */
    @Override
    public List<OrderInfoRE> getOrderInfoListByMemberId(OrdMemberVO ordMemberVO) {
        //先获该用户的取订单id，然后查询每条订单的状态，订单的金额 以及获取订单的商品信息
        List<OrderInfoRE> orderInfoREList = new ArrayList<>();
        List<OrderRE> orderList = OrderClient.getOrderByMemberVO(ordMemberVO);;
        if (CollectionUtils.isEmpty(orderList)) {
            return orderInfoREList;
        }
        OrderInfoRE orderInfoRE;
        for (OrderRE order : orderList) {
            orderInfoRE = new OrderInfoRE();
            orderInfoRE.setOrderNum(order.getOrderNum());
            orderInfoRE.setStatus(order.getStatus().longValue());
            orderInfoRE.setPayPrice(order.getPayPrice());
            orderInfoRE.setOrderPrice(order.getOrderPrice());
            orderInfoRE.setOrderId(order.getId().intValue());
            orderInfoRE.setReceiptName(order.getReceiptName());
            orderInfoRE.setReceiptTel(order.getReceiptTel());
            orderInfoRE.setReceiptAddress(order.getReceiptAddress());
            List<OrderGoodsDetailRE> orderGodsList = getOrderGdsByIds(order.getId().intValue());
            orderInfoRE.setOrderGoodsDetailRE(orderGodsList);
            orderInfoREList.add(orderInfoRE);
        }
        OrdOrderVO ordOrderVO = new OrdOrderVO();
        ordOrderVO.setMemberId(ordMemberVO.getMemberId());
        ordOrderVO.setStatus(ordMemberVO.getStatus());
        orderInfoRE = new OrderInfoRE();
        orderInfoRE.setTotalOrderNum(OrderClient.getOrderTotal(ordOrderVO).longValue());
        orderInfoREList.add(orderInfoRE);
        return orderInfoREList;
    }

    @Override
    public OrderSaleRE getOrderSaleData() {
        return OrderClient.getOrderSaleData();
    }

    @Override
    public Integer queryCartGoodsCount(Long memberId) {
        return OrderClient.queryCartGoodsCount(memberId);
    }

    @Override
    public List<CommentVO> getCommentOrderGoodsDetail(OrdGoodsVO ordGoodsVO) {

        List<CommentVO> list = orderDao.getPropertyIdListByUid(ordGoodsVO);
        GoodsDetailRE gdDTO = new GoodsDetailRE();
        int conut = 0;
        // 判空
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 记录上一个商品属性Id 如果相同不需要再次查库
        Long recomentId = list.get(0).getGoodsPropertyId();
        // 获取未评价商品详情
        for (CommentVO commentVO : list) {
            // 第一次进入
            if (conut == 0) {
                gdDTO = goodsService.getGoodsDetailByPropertyId(commentVO.getGoodsPropertyId());
                commentVO.setGoodsId(gdDTO.getGoodsId());
                commentVO.setGoodsProperty(gdDTO.getProperty());
                commentVO.setStatus(gdDTO.getStatus());
                commentVO.setGoodsName(gdDTO.getName());
                if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                    commentVO.setImgUrl(gdDTO.getPicUrl().get(0).getPicUrl());
                }
                conut++;
                recomentId = commentVO.getGoodsPropertyId();
                continue;
            }
            // 查找与上一次属性id相同时
            if (recomentId.equals(commentVO.getGoodsPropertyId())) {
                commentVO.setGoodsId(gdDTO.getGoodsId());
                commentVO.setStatus(gdDTO.getStatus());
                commentVO.setGoodsName(gdDTO.getName());
                commentVO.setGoodsProperty(gdDTO.getProperty());
                if (CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                    commentVO.setImgUrl(gdDTO.getPicUrl().get(0).getPicUrl());
                }
                continue;
            }
            gdDTO = goodsService.getGoodsDetailByPropertyId(commentVO.getGoodsPropertyId());
            commentVO.setGoodsId(gdDTO.getGoodsId());
            commentVO.setGoodsProperty(gdDTO.getProperty());
            commentVO.setGoodsName(gdDTO.getName());
            commentVO.setStatus(gdDTO.getStatus());
            if (CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                commentVO.setImgUrl(gdDTO.getPicUrl().get(0).getPicUrl());
            }
            recomentId = commentVO.getGoodsPropertyId();
        }
        return list;
    }
}

