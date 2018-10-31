package com.uc.training.smadmin.ord.re;

import java.io.Serializable;

/**
 * description: TODO
 *
 * @author 黄宏俊
 * @version 1.0
 * @date 2018/10/26  18:28
 */
public class OrderGoodsDetailRe implements Serializable {
    private static final long serialVersionUID = -6912839796213052826L;
    /**
     * 商品名
     */
    private String goodsName;
    /**
     * 商品数量
     */
    private Integer goodsNum = 0;
    /**
     * 商品价格
     */
    private Double goodsPrice = 0.0;
    /**
     * 商品规格
     */
    private String goodsProperty;

    /**
     * 改商品总计价格
     */
    private Double totalPrice;

    /**
     * 商品URL
     */

    private String goodsUrl;

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public Double getTotalPrice() {
        return this.goodsPrice * this.goodsNum;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsProperty() {
        return goodsProperty;
    }

    public void setGoodsProperty(String goodsProperty) {
        this.goodsProperty = goodsProperty;
    }
}
