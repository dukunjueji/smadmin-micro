package com.uc.training.smadmin.ord.model;

import java.io.Serializable;

/**
 * @author kun.du01@ucarinc.com
 * @date 2018-10-17 星期三 16:28
 * @description:购物车商品信息表
 */
public class CartGoods implements Serializable {

	private static final long serialVersionUID = 2687160569268677492L;

	/**
	 * 主键id
	 **/
	private Long id;

	/**
	 * 商品id
	 **/
	private Long goodsId;

	/**
	 * 商品属性id
	 **/
	private Long goodsPropertyId;

	/**
	 * 会员id
	 **/
	private Long memberId;

	/**
	 * 商品数量
	 **/
	private Integer goodsNum;

	/**
	 * 创建人
	 **/
	private Long createEmp;

	/**
	 * 创建时间
	 **/
	private java.util.Date createTime;

	/**
	 * 修改人
	 **/
	private Long modifyEmp;

	/**
	 * 修改时间
	 **/
	private java.util.Date modifyTime;


	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsPropertyId(Long goodsPropertyId) {
		this.goodsPropertyId = goodsPropertyId;
	}

	public Long getGoodsPropertyId() {
		return this.goodsPropertyId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getMemberId() {
		return this.memberId;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer getGoodsNum() {
		return this.goodsNum;
	}

	public void setCreateEmp(Long createEmp) {
		this.createEmp = createEmp;
	}

	public Long getCreateEmp() {
		return this.createEmp;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setModifyEmp(Long modifyEmp) {
		this.modifyEmp = modifyEmp;
	}

	public Long getModifyEmp() {
		return this.modifyEmp;
	}

	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}

}
