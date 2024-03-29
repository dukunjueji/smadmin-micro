package com.ucar.smadmin.ord.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 购物车商品信息VO
 *
 * @author hhj
 * @date 2018/10/22
 */


public class OrdCartGoodsVO implements Serializable {

    /**
     * 会员id
     */
    private Long memberId;
    /**
     * 购物车id
     */
    private Long cartId;
    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 商品图片
     */
    private String gdsUrl;

    /**
     * 商品名称
     */
    private String gdsName;

    /**
     * 商品数量
     */
    private Long num;

    /**
     * 商品规格
     */
    private String property;

    /**
     * 属性id
     */
    private Long propertyId;
    /**
     * 库存
     */
    private Long stock;
    /**
     * 商品原价
     */
    private BigDecimal salePrice;

    /**
     * 商品优惠价
     */
    private BigDecimal discountPrice;

    /**
     * 是否打折
     */
    private Long isDiscount;

    /**
     * 商品状态(是否下架)
     */
    private Long status;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGdsUrl() {
        return gdsUrl;
    }

    public void setGdsUrl(String gdsUrl) {
        this.gdsUrl = gdsUrl;
    }

    public String getGdsName() {
        return gdsName;
    }

    public void setGdsName(String gdsName) {
        this.gdsName = gdsName;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Long getIsDiscount() {
        return isDiscount;
    }

    public void setIsDiscount(Long isDiscount) {
        this.isDiscount = isDiscount;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "OrdCartGoodsVO{" +
                "memberId=" + memberId +
                ", cartId=" + cartId +
                ", goodsId=" + goodsId +
                ", gdsUrl='" + gdsUrl + '\'' +
                ", gdsName='" + gdsName + '\'' +
                ", num=" + num +
                ", property='" + property + '\'' +
                ", propertyId=" + propertyId +
                ", stock=" + stock +
                ", salePrice=" + salePrice +
                ", discountPrice=" + discountPrice +
                ", isDiscount=" + isDiscount +
                ", status=" + status +
                '}';
    }
}