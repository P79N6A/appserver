package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

public class ShopCookingDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6963385303923132233L;

	private Integer shopCookingId;
	private Long shopId;
	private String detailsType;
	private String detailsName;
	private Integer typeOrder;
	private Date createTime;
	private Date lastUpdateTime;
	public Integer getShopCookingId() {
		return shopCookingId;
	}
	public void setShopCookingId(Integer shopCookingId) {
		this.shopCookingId = shopCookingId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getDetailsType() {
		return detailsType;
	}
	public void setDetailsType(String detailsType) {
		this.detailsType = detailsType;
	}
	public String getDetailsName() {
		return detailsName;
	}
	public void setDetailsName(String detailsName) {
		this.detailsName = detailsName;
	}
	public Integer getTypeOrder() {
		return typeOrder;
	}
	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
