package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;

public class GGroupQueyDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 702596804617152577L;
	private Long shopId;
	private String goodsKey;
	private Integer goodsStatus;
	private Integer pNo;
	private Integer pSize;
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getGoodsKey() {
		return goodsKey;
	}
	public void setGoodsKey(String goodsKey) {
		this.goodsKey = goodsKey;
	}
	public Integer getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	public Integer getpNo() {
		return (pNo-1)*this.pSize;
	}
	public void setpNo(Integer pNo) {
		this.pNo = pNo;
	}
	public Integer getpSize() {
		return pSize;
	}
	public void setpSize(Integer pSize) {
		this.pSize = pSize;
	}
	
	
}
