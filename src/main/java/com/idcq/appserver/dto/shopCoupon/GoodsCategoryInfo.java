package com.idcq.appserver.dto.shopCoupon;

import java.io.Serializable;

public class GoodsCategoryInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4163423006806200361L;
	
	private Long goodsCategoryId;
	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}
	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}
	public Double getGoodsCategoryAmount() {
		return goodsCategoryAmount;
	}
	public void setGoodsCategoryAmount(Double goodsCategoryAmount) {
		this.goodsCategoryAmount = goodsCategoryAmount;
	}
	private Double goodsCategoryAmount;
}
