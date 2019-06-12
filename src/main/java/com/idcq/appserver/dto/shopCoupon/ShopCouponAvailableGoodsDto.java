package com.idcq.appserver.dto.shopCoupon;

import java.io.Serializable;

public class ShopCouponAvailableGoodsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -425439682965293801L;
	
	/**
	 * 唯一标识id
	 */
	private Integer availableGoodsId;
	
	/**
	 * 店铺优惠券id
	 */
	private Integer shopCouponId;
	
	/**
	 * 优惠券适用类型：商品分类-1，商品=2
	 */
	private Integer couponApplyType;
	
	/**
	 * 适用类型对应的主键id，比如适用类型=1则为商品分类id，适用类型=2则为商品id
	 */
	private Integer couponApplyId;
	
	private String categoryName;

	public Integer getAvailableGoodsId() {
		return availableGoodsId;
	}

	public void setAvailableGoodsId(Integer availableGoodsId) {
		this.availableGoodsId = availableGoodsId;
	}

	public Integer getShopCouponId() {
		return shopCouponId;
	}

	public void setShopCouponId(Integer shopCouponId) {
		this.shopCouponId = shopCouponId;
	}

	public Integer getCouponApplyType() {
		return couponApplyType;
	}

	public void setCouponApplyType(Integer couponApplyType) {
		this.couponApplyType = couponApplyType;
	}

	public Integer getCouponApplyId() {
		return couponApplyId;
	}

	public void setCouponApplyId(Integer couponApplyId) {
		this.couponApplyId = couponApplyId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
