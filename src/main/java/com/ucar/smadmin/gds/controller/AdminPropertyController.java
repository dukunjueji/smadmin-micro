package com.ucar.smadmin.gds.controller;

import com.ucar.smadmin.common.base.controller.BaseController;
import com.ucar.smapi.common.vo.Result;
import com.ucar.smadmin.enums.GoodsStatusEnum;
import com.ucar.smapi.gds.dto.PropertyDTO;
import com.ucar.smapi.gds.re.GoodsDetailRE;
import com.ucar.smadmin.gds.service.GoodsService;
import com.ucar.smadmin.gds.service.PropertyService;
import com.ucar.smadmin.ord.service.OrderGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/10/31
 */
@Controller
@RequestMapping("admin/gds/property")
public class AdminPropertyController extends BaseController {

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private OrderGoodsService orderGoodsService;
    @Autowired
    private GoodsService goodsService;

    /**
     * 后台通过商品id获取商品属性列表
     * @param goodsId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getPropertyListByGoodsId.do_", method = RequestMethod.POST)
    public Result<List<GoodsDetailRE>> getPropertyListByGoodsId(Long goodsId) {
        return Result.getSuccessResult(propertyService.getPropertyListByGoodsId(goodsId));
    }
    /**
     * 后台新增商品属性
     * @param property
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "insertProperty.do_", method = RequestMethod.POST)
    public Result insertProperty(@Validated PropertyDTO property) {

        if (property.getIsDiscount() == 1 && property.getDiscountPrice() == null) {
            return Result.getBusinessException("请填写打折价格", null);
        }

        //获取同商品下同名称的数量
        if (propertyService.getCountByProperty(property) >= 1) {
            return Result.getBusinessException("该规格已存在，请不要重复添加！", null);
        }

        property.setCreateEmp(getUid());
        property.setModifyEmp(getUid());

        return Result.getSuccessResult(propertyService.insertProperty(property));
    }
    /**
     * 后台更新商品属性
     * @param property
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateProperty.do_", method = RequestMethod.POST)
    public Result updateProperty(@Validated PropertyDTO property) {

        if (property.getIsDiscount() == 1 && property.getDiscountPrice() == null) {
            return Result.getBusinessException("请填写打折价格", null);
        }

        //获取同商品下同名称的数量
        if (propertyService.getCountByProperty(property) > 1) {
            return Result.getBusinessException("该规格已存在，请不要重复添加！", null);
        }

        property.setModifyEmp(getUid());

        return Result.getSuccessResult(propertyService.updateProperty(property));
    }
    /**
     * 后台通过主键id删除商品属性和图片
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deletePropertyAndGoodsPicById.do_", method = RequestMethod.POST)
    public Result deletePropertyById(Long id) {

        //判断该属性是否存在待付款订单
        if (orderGoodsService.getUnPayGoodsPropertyCountByPropertyId(id) >= 1) {
            return Result.getBusinessException("该属性存在待付款的订单，不可以直接删除!", null);
        }

        PropertyDTO property = new PropertyDTO();
        property.setGoodsId(propertyService.getPropertyById(id).getGoodsId());

        //判断商品是否上架和该商品的商品属性数量
        if (goodsService.getGoodsById(propertyService.getPropertyById(id).getGoodsId()).getStatus()
        .equals(GoodsStatusEnum.GOODS_SHELVES.getType())
        && propertyService.getCountByProperty(property) == 1) {
            return Result.getBusinessException("该商品处于上架状态，不能删除商品唯一的属性!", null);
        }


        return Result.getSuccessResult(propertyService.deletePropertyAndGoodsPicById(id));
    }
}
