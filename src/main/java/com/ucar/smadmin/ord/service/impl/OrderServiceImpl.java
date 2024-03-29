package com.ucar.smadmin.ord.service.impl;

import com.ucar.smapi.base.bd.re.AddressRE;
import com.ucar.smadmin.base.bd.service.AddressService;
import com.ucar.smadmin.base.bd.service.MemberGradeService;
import com.ucar.smadmin.common.redis.RedissonManager;
import com.ucar.smadmin.common.utils.UUIDUtil;
import com.ucar.smadmin.enums.OrderEnum;
import com.ucar.smadmin.enums.OrderGoodsCommentEnum;
import com.ucar.smadmin.enums.StokeStatusEnum;
import com.ucar.smadmin.enums.UUIDTypeEnum;
import com.ucar.smapi.gds.re.CommentCountRE;
import com.ucar.smapi.gds.re.GoodsDetailRE;
import com.ucar.smapi.gds.re.GoodsStokeRE;
import com.ucar.smadmin.gds.service.CommentService;
import com.ucar.smadmin.gds.service.GoodsService;
import com.ucar.smadmin.gds.vo.GoodsStokeVO;
import com.ucar.smapi.ord.re.CartGoodsRE;
import com.ucar.smapi.ord.re.CommentRE;
import com.ucar.smapi.ord.re.OrdOrderGoodsRE;
import com.ucar.smapi.ord.re.OrderConfirmRE;
import com.ucar.smapi.ord.re.OrderGoodsDetailRE;
import com.ucar.smapi.ord.re.OrderGoodsRE;
import com.ucar.smapi.ord.re.OrderInfoRE;
import com.ucar.smapi.ord.re.OrderRE;
import com.ucar.smapi.ord.re.OrderSaleRE;
import com.ucar.smapi.ord.re.OrderStatusRE;
import com.ucar.smadmin.ord.service.OrderService;
import com.ucar.smadmin.ord.vo.OrdCartGoodsVO;
import com.ucar.smadmin.ord.vo.OrdGoodsVO;
import com.ucar.smadmin.ord.vo.OrdMemberVO;
import com.ucar.smadmin.ord.vo.OrdOrderGoodsVO;
import com.ucar.smadmin.ord.vo.OrdOrderVO;
import com.ucar.smadmin.ord.vo.OrderGoodsVO;
import com.ucar.smadmin.ord.vo.OrderVO;
import com.ucar.smadmin.remote.client.OrderClient;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Integer ORDER_INFO_LIST = 2;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private AddressService addressService;
    @Autowired
    CommentService commentService;
    @Autowired
    private MemberGradeService memberGradeService;
    @Autowired
    OrderClient orderClient;

    /**
     * 根据用户id查询购物车信息表
     *
     * @param memberId
     * @return
     */

    @Override
    public List<CartGoodsRE> getCarGoodsById(Long memberId) {
        return orderClient.getCarGoodsById(memberId);
    }

    /**
     * 更新购物车商品数量
     *
     * @param ordCartGoodsVO
     * @return 返回影响条数
     */
    @Override
    public Integer updateCarGoodsNum(OrdCartGoodsVO ordCartGoodsVO) {
        return orderClient.updateCarGoodsNum(ordCartGoodsVO);
    }

    /**
     * 通过会员id来查找
     *
     * @param ordMemberVO （订单表id）
     * @return
     */
    @Override
    public List<OrderRE> getOrderByMemberVO(OrdMemberVO ordMemberVO) {
        return orderClient.getOrderByMemberVO(ordMemberVO);
    }

    @Override
    public List<CartGoodsRE> getCarGoodsByIds(OrdGoodsVO ordGoodsVO) {
        return orderClient.getCarGoodsByIds(ordGoodsVO);
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
        List<CartGoodsRE> goodsNumList = orderClient.getCarGoodsByIds(ordGoodsVO);
        OrdOrderGoodsRE ordOrderGoodsRE;
        for (int i = 0; i < orderGodsList.size(); i++) {
            ordOrderGoodsRE = new OrdOrderGoodsRE();
            GoodsDetailRE gdDTO = goodsService.getGoodsDetailByPropertyId(orderGodsList.get(i).getPropertyId());
            if (gdDTO == null) {
                return list;
            }
            if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                ordOrderGoodsRE.setGdsUrl(gdDTO.getPicUrl().get(0).getPicUrl());
            }
            ordOrderGoodsRE.setGoodsId(orderGodsList.get(i).getGoodsId());
            ordOrderGoodsRE.setGdsName(gdDTO.getName());
            // 对我的订单中的商品重新购买进行判断
            if (orderGodsList.get(0).getOrderId() != null) {
                List<OrderGoodsRE> orderGdsList = orderClient.getOrderGoodsByOrderId(orderGodsList.get(0).getOrderId().intValue());
                if (CollectionUtils.isEmpty(orderGdsList)) {
                    return null;
                }
                for (int j = 0; j < orderGdsList.size(); j++) {
                    if (orderGdsList.get(j).getGoodsPropertyId().equals(orderGodsList.get(i).getPropertyId())
                            && orderGdsList.get(j).getGoodsNum().equals(orderGodsList.get(i).getNum())) {
                        ordOrderGoodsRE.setNum(orderGdsList.get(j).getGoodsNum());
                        break;
                    }
                }
            } else if (!CollectionUtils.isEmpty(goodsNumList)) {
                // 判斷前臺提交的商品数量參數是否與购物车後臺一致
                for (int j = 0; j < goodsNumList.size(); j++) {
                    if (goodsNumList.get(j).getGoodsPropertyId().equals(orderGodsList.get(i).getPropertyId())) {
                        ordOrderGoodsRE.setNum(goodsNumList.get(j).getGoodsNum());
                        break;
                    }
                }
            }
            if (ordOrderGoodsRE.getNum() == null) {
                return list;
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
        List<OrderGoodsRE> orderGdsList = orderClient.getOrderGoodsByOrderId(orderId.intValue());
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
        return orderClient.addCarGoods(ordCartGoodsVO);
    }

    /**
     * 删除购物车商品
     *
     * @param ordCartGoodsVO
     * @return
     */
    @Override
    public Integer deleteCarGoods(OrdCartGoodsVO ordCartGoodsVO) {
        return orderClient.deleteCarGoods(ordCartGoodsVO);
    }


    @Override
    public Integer queryOrderCount(Long memberId) {
        return orderClient.queryOrderCount(memberId);
    }

    /**
     * 判断该商品是否下架、删除、无库存
     */
    private List<OrderConfirmRE> validationGoods(List<OrdOrderGoodsVO> orderInfoListNow) {
        List<OrderConfirmRE> list = new ArrayList<>();
        OrderConfirmRE orderConfirmRE = new OrderConfirmRE();
        GoodsStokeVO goodsStokeVO;
        if (orderInfoListNow.size() <= ORDER_INFO_LIST) {
            return list;
        }
        for (int i = 0; i < orderInfoListNow.size() - ORDER_INFO_LIST; i++) {
            //更新商品对应的库存,加入分布式，利用商品属性id生成锁，防止高并发下的库存数为负数
            String lockName = orderInfoListNow.get(i).getPropertyId() + "lock";
            RLock lock = RedissonManager.getInstance().getLock(lockName, true);
            goodsStokeVO = new GoodsStokeVO();
            try {
                lock.lock(RedissonManager.DEFAULT_EXPIRED_TIME, RedissonManager.DEFAULT_TIME_UNIT);
                goodsStokeVO.setPropertyId(orderInfoListNow.get(i).getPropertyId());
                goodsStokeVO.setStock((long) orderInfoListNow.get(i).getNum());
                Integer status = goodsService.updateAndDeductStoke(goodsStokeVO);
                //更新库存表、插入用户订单表和订单商品信息表、删除购物车商品信息,判断商品是否删除或者下架和库存是否足够
                GoodsStokeRE goodsStokeRE = goodsService.selectGoodsStatus(goodsStokeVO);
                if (status.equals(StokeStatusEnum.DELETE_STATUS.getStatus())) {
                    StringBuilder temp = new StringBuilder();
                    temp.append("您的商品：" + goodsStokeRE.getGoodsName() + "\n" + "规格:");
                    temp.append(goodsStokeRE.getGoodsProperty() + "已经被删除了，点击返回购物车，再重新选择");
                    orderConfirmRE.setShowStatus(temp.toString());
                    orderConfirmRE.setGoodsStatus(OrderEnum.NOORDER.getKey());
                    list.add(orderConfirmRE);
                    return list;
                } else if (status.equals(StokeStatusEnum.SHELVED_STATUS.getStatus())) {
                    StringBuilder temp = new StringBuilder();
                    temp.append("您的商品：" + goodsStokeRE.getGoodsName() + "\n" + "规格:");
                    temp.append(goodsStokeRE.getGoodsProperty() + "已经被下架了，点击返回购物车，再重新选择");
                    orderConfirmRE.setShowStatus(temp.toString());
                    orderConfirmRE.setGoodsStatus(OrderEnum.NOORDER.getKey());
                    list.add(orderConfirmRE);
                    return list;
                } else if (status.equals(StokeStatusEnum.NOT_ENOUGH_STATUS.getStatus())) {
                    StringBuilder temp = new StringBuilder();
                    temp.append("您的商品：" + goodsStokeRE.getGoodsName() + "\n" + "规格:");
                    temp.append(goodsStokeRE.getGoodsProperty() + "已经超过商品最大库存量，点击返回购物车，请再重新选择");
                    orderConfirmRE.setShowStatus(temp.toString());
                    orderConfirmRE.setGoodsStatus(OrderEnum.NOORDER.getKey());
                    list.add(orderConfirmRE);
                    return list;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
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
        // 判断该商品是否下架、删除、无库存
        List<OrderConfirmRE> goodsStatusList = validationGoods(orderInfoListNow);
        if (CollectionUtils.isEmpty(goodsStatusList)) {
            return goodsStatusList;
        } else if (goodsStatusList.get(0).getGoodsStatus() != (int) OrderEnum.WAITPAY.getKey().longValue()) {
            return goodsStatusList;
        }
        // 判断前台传来的价格信息是否与购物车一致
        OrdGoodsVO ordGoodsVO = new OrdGoodsVO();
        OrdCartGoodsVO ordCartGoodsVO;
        ordGoodsVO.setMemberId(orderInfoListNow.get(orderInfoListNow.size() - ORDER_INFO_LIST).getMemberId());
        List<Long> propertyIds = new ArrayList<>();
        for (int i = 0; i < orderInfoListNow.size() - ORDER_INFO_LIST; i++) {
            propertyIds.add(orderInfoListNow.get(i).getPropertyId());
        }
        ordGoodsVO.setGoodsPropertyIdList(propertyIds);
        //调用远程服务查询购物车商品信息
        List<CartGoodsRE> goodsNumList = orderClient.getCarGoodsByIds(ordGoodsVO);
        Double memberDiscountPoint = memberGradeService.getDiscountByUId(orderInfoListNow.get(orderInfoListNow.size() - ORDER_INFO_LIST).getMemberId());
        if (memberDiscountPoint == null) {
            return list;
        }
        BigDecimal payPrice = new BigDecimal(0);
        for (int i = 0; i < orderInfoListNow.size() - ORDER_INFO_LIST; i++) {
            GoodsDetailRE gdDTO = goodsService.getGoodsDetailByPropertyId(orderInfoListNow.get(i).getPropertyId());
            if (gdDTO == null) {
                return list;
            }
            if (!CollectionUtils.isEmpty(goodsNumList)) {
                for (int j = 0; j < goodsNumList.size(); j++) {
                    if (goodsNumList.get(j).getGoodsPropertyId().equals(orderInfoListNow.get(i).getPropertyId())) {
                        if (gdDTO.getIsDiscount() == 1) {
                            payPrice = gdDTO.getDiscountPrice().multiply(BigDecimal.valueOf(goodsNumList.get(j).getGoodsNum() * memberDiscountPoint)).add(payPrice);
                        } else {
                            payPrice = gdDTO.getSalePrice().multiply(BigDecimal.valueOf(goodsNumList.get(j).getGoodsNum() * memberDiscountPoint)).add(payPrice);
                        }
                        payPrice = payPrice.setScale(ORDER_INFO_LIST, BigDecimal.ROUND_HALF_UP);
                        break;
                    }
                }
                //删除购物车信息表,并以商品属性id加入锁机制，防止重复提交订单
                String lockName = orderInfoListNow.get(i).getPropertyId() + "Lock";
                RLock lock = RedissonManager.getInstance().getLock(lockName, true);
                try {
                    lock.lock(RedissonManager.DEFAULT_EXPIRED_TIME, RedissonManager.DEFAULT_TIME_UNIT);
                    ordCartGoodsVO = new OrdCartGoodsVO();
                    ordCartGoodsVO.setPropertyId(orderInfoListNow.get(i).getPropertyId());
                    ordCartGoodsVO.setMemberId(orderInfoListNow.get(orderInfoListNow.size() - ORDER_INFO_LIST).getMemberId());
                    if (deleteCarGoods(ordCartGoodsVO) <= 0) {
                        return list;
                    }
                } catch (Exception e) {
                    LOGGER.info(e.getMessage());
                    return list;
                } finally {
                    lock.unlock();
                }
            } else {
                return list;
            }
        }
        if (payPrice.compareTo(orderInfoListNow.get(orderInfoListNow.size() - 1).getTotalPrice()) != 0) {
            return list;
        }
        //插入用户订单表
        OrderVO order = new OrderVO();
        order.setMemberId(orderInfoListNow.get(orderInfoListNow.size() - ORDER_INFO_LIST).getMemberId());
        order.setOrderPrice(orderInfoListNow.get(orderInfoListNow.size() - 1).getOrderPrice());
        order.setPayPrice(orderInfoListNow.get(orderInfoListNow.size() - 1).getTotalPrice());
        //插入地址信息
        AddressRE addressRE = addressService.getAddressById(orderInfoListNow.get(orderInfoListNow.size() - ORDER_INFO_LIST).getAddressId(), null);
        if (addressRE != null) {
            order.setReceiptAddress(addressRE.getProvince() + addressRE.getCity() + addressRE.getDistrict() + addressRE.getAddrDetail());
            order.setReceiptName(addressRE.getReceiver());
            order.setReceiptTel(addressRE.getTelephone());
        }
        order.setStatus(OrderEnum.WAITPAY.getKey());
        order.setIsDelete(0);
        //生成订单编号
        order.setOrderNum(UUIDUtil.getUuidByType(UUIDTypeEnum.ORDERID.getType()));
        Long orderId = orderClient.insertOrder(order);
        if (orderId != null) {
            order.setId(orderId);
        } else {
            return list;
        }
        //遍历orderInfoListNow
        OrderGoodsVO orderGoods;
        for (int i = 0; i < orderInfoListNow.size() - ORDER_INFO_LIST; i++) {
            //插入订单商品信息表
            orderGoods = new OrderGoodsVO();
            orderGoods.setOrderId(orderId);
            orderGoods.setGoodsId(orderInfoListNow.get(i).getGoodsId());
            orderGoods.setPayPrice(orderInfoListNow.get(i).getPayPrice());
            orderGoods.setGoodsPropertyId(orderInfoListNow.get(i).getPropertyId());
            orderGoods.setGoodsNum(orderInfoListNow.get(i).getNum());
            orderGoods.setSalePrice(orderInfoListNow.get(i).getSalePrice());
            orderGoods.setDiscountPrice(orderInfoListNow.get(i).getDiscountPrice());
            orderGoods.setGoodsPropertyId(orderInfoListNow.get(i).getPropertyId());
            orderClient.insertOrderGoods(orderGoods);
        }
        orderConfirmRE.setShowStatus("已成功生成订单");
        orderConfirmRE.setGoodsStatus((int) OrderEnum.WAITPAY.getKey().longValue());
        orderConfirmRE.setOrderId(order.getId());
        list.add(orderConfirmRE);
        return list;
    }

    @Override
    public List<OrderRE> getOrderPage(OrdOrderVO ordOrderVO) {
        return orderClient.getOrderPage(ordOrderVO);
    }

    @Override
    public Integer getOrderTotal(OrdOrderVO ordOrderVO) {
        return orderClient.getOrderTotal(ordOrderVO);
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
        return orderClient.logicDelOrder(list);
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
        ordMemberVO.setMemberId(ordOrderVO.getMemberId());
        List<OrderRE> getOrdOrderVO = orderClient.getOrderByMemberVO(ordMemberVO);
        if (CollectionUtils.isEmpty(getOrdOrderVO)) {
            return 0;
        }
        //如果是取消订单回退库存
        if (ordOrderVO.getStatus().equals(OrderEnum.CANCEL.getKey().longValue())) {
            //需要取消订单的状态为待付款状态
            if (!OrderEnum.WAITPAY.getKey().equals(getOrdOrderVO.get(0).getStatus())) {
                return 0;
            }
            List<OrderGoodsDetailRE> list = getOrderGdsById(ordOrderVO.getOrderId());
            if (!CollectionUtils.isEmpty(list)) {
                GoodsStokeVO goodsStokeVO = new GoodsStokeVO();
                for (OrderGoodsDetailRE orderGoodsDetailRe : list) {
                    goodsStokeVO.setStock(-(orderGoodsDetailRe.getGoodsNum().longValue()));
                    goodsStokeVO.setPropertyId(orderGoodsDetailRe.getGoodsPropertyId());
                    goodsService.updateAndDeductStoke(goodsStokeVO);
                }
            } else {
                return 0;
            }
        }
        return orderClient.updateOrder(ordOrderVO);
    }


    @Override
    public List<OrderGoodsDetailRE> getOrderGdsById(Integer id) {
        List<OrderGoodsRE> orderGdsList = orderClient.getOrderGoodsByOrderId(id);
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
        return orderClient.getTelephoneById(id);
    }

    /**
     * 根据订单id获取用户订单商品信息
     *
     * @param id
     * @return
     */
    private List<OrderGoodsDetailRE> getOrderGdsByIds(Integer id) {
        List<OrderGoodsRE> orderGdsList = orderClient.getOrderGoodsByOrderId(id);
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
        List<OrderRE> orderList = orderClient.getOrderByMemberVO(ordMemberVO);
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
        orderInfoRE.setTotalOrderNum(orderClient.getOrderTotal(ordOrderVO).longValue());
        orderInfoREList.add(orderInfoRE);
        return orderInfoREList;
    }

    @Override
    public OrderSaleRE getOrderSaleData() {
        return orderClient.getOrderSaleData();
    }

    @Override
    public Integer queryCartGoodsCount(Long memberId) {
        return orderClient.queryCartGoodsCount(memberId);
    }

    @Override
    public List<CommentRE> getCommentOrderGoodsDetail(OrdGoodsVO ordGoodsVO) {
        List<CommentRE> list = orderClient.getPropertyIdListByUid(ordGoodsVO);
        GoodsDetailRE gdDTO = new GoodsDetailRE();
        int count = 0;
        // 判空
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        // 记录上一个商品属性Id 如果相同不需要再次查库
        Long lastGoodsPropertyId = list.get(0).getGoodsPropertyId();
        // 获取未评价商品详情
        for (CommentRE commentRE : list) {
            // 第一次进入
            if (count == 0) {
                gdDTO = goodsService.getGoodsDetailByPropertyId(commentRE.getGoodsPropertyId());
                commentRE.setGoodsId(gdDTO.getGoodsId());
                commentRE.setGoodsProperty(gdDTO.getProperty());
                commentRE.setStatus(gdDTO.getStatus());
                commentRE.setGoodsName(gdDTO.getName());
                if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                    commentRE.setImgUrl(gdDTO.getPicUrl().get(0).getPicUrl());
                }
                count++;
                lastGoodsPropertyId = commentRE.getGoodsPropertyId();
                continue;
            }
            // 查找与上一次属性id相同时
            if (lastGoodsPropertyId.equals(commentRE.getGoodsPropertyId())) {
                commentRE.setGoodsId(gdDTO.getGoodsId());
                commentRE.setStatus(gdDTO.getStatus());
                commentRE.setGoodsName(gdDTO.getName());
                commentRE.setGoodsProperty(gdDTO.getProperty());
                if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                    commentRE.setImgUrl(gdDTO.getPicUrl().get(0).getPicUrl());
                }
                continue;
            }
            gdDTO = goodsService.getGoodsDetailByPropertyId(commentRE.getGoodsPropertyId());
            commentRE.setGoodsId(gdDTO.getGoodsId());
            commentRE.setGoodsProperty(gdDTO.getProperty());
            commentRE.setGoodsName(gdDTO.getName());
            commentRE.setStatus(gdDTO.getStatus());
            if (!CollectionUtils.isEmpty(gdDTO.getPicUrl())) {
                commentRE.setImgUrl(gdDTO.getPicUrl().get(0).getPicUrl());
            }
            lastGoodsPropertyId = commentRE.getGoodsPropertyId();
        }
        if (!ordGoodsVO.getCommentStatus().equals(OrderGoodsCommentEnum.NO_COMMENT.getKey())) {
            for (CommentRE commentRE : list) {
                List<CommentCountRE> list1 = commentService.getCommentCountByOrderGoodsId(commentRE.getId());
                if (!org.springframework.util.CollectionUtils.isEmpty(list1)) {
                    for (CommentCountRE comment : list1) {
                        if (comment.getParentId() == null) {
                            commentRE.setCommentId(comment.getId());
                        }
                    }
                }
            }
        }
        return list;
    }

    @Override
    public int getCommentGoodsCount(OrdGoodsVO ordGoodsVO) {
        List<CommentRE> list = orderClient.getPropertyIdListByUid(ordGoodsVO);
        if (!CollectionUtils.isEmpty(list)) {
            return list.size();
        }
        return 0;
    }

}

