package com.idcq.appserver.dto.shopCoupon;

import java.io.Serializable;
import java.util.List;

public class RequstCouponDeductModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6935691882684089276L;
	
	private Integer shopMemberId;
	public Integer getShopMemberId() {
		return shopMemberId;
	}
	public void setShopMemberId(Integer shopMemberId) {
		this.shopMemberId = shopMemberId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	private Long shopId;
	private String mobile;
	private Double payAmount;
	
	private List<GoodsCategoryInfo> goodsCategoryInfo;
	public List<GoodsCategoryInfo> getGoodsCategoryInfo() {
		return goodsCategoryInfo;
	}
	public void setGoodsCategoryInfo(List<GoodsCategoryInfo> goodsCategoryInfo) {
		this.goodsCategoryInfo = goodsCategoryInfo;
	}

}
