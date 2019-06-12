package com.idcq.appserver.dto.shopCoupon;

import java.io.Serializable;

public class ShopCouponAvailableShopDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6521960058779875357L;
	
	/**
	 * 唯一标识id
	 */
	private Integer availableShopId;
	
	/**
	 * 优惠券id
	 */
	private Integer shopCouponId;
	
	/**
	 * 可用店铺id
	 */
	private Integer shopId;

	public Integer getAvailableShopId() {
		return availableShopId;
	}

	public void setAvailableShopId(Integer availableShopId) {
		this.availableShopId = availableShopId;
	}

	public Integer getShopCouponId() {
		return shopCouponId;
	}

	public void setShopCouponId(Integer shopCouponId) {
		this.shopCouponId = shopCouponId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	
	
	
}
