package com.uc.training.smadmin.gds.dao;

import com.uc.training.smadmin.gds.model.GoodsPic;
import com.uc.training.smadmin.gds.model.Property;
import com.uc.training.smadmin.gds.re.AdminGoodsPicRE;

import java.util.List;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/10/29
 */
public interface GoodsPicDao {

    /**
     * 新增商品属性
     * @param goodsPic
     * @return
     */
    Long insertGoodsPic(GoodsPic goodsPic);

    /**
     * 更新商品属性
     * @param goodsPic
     * @return
     */
    Integer updateGoodsPic(GoodsPic goodsPic);

    /**
     * 根据商品属性id获取商品图片
     * @param propertyId
     * @return
     */
    List<AdminGoodsPicRE> getGoodsPicListByPropertyId(Long propertyId);
}
