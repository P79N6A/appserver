package com.idcq.appserver.dto.plugin;

import java.io.Serializable;
import java.util.Date;

public class ShopPluginDto implements Serializable{

	private static final long serialVersionUID = 4175734609424204667L;
	
	private Integer shopPluginId;
	private Integer shopId;
	private Integer pluginId;
	private Integer buyNumber;
	private Double money;
	private Date orderTime;
	private Date beginTime;
	private Date endTime;
	private Integer isPaid;
	private Integer isSettled;
	private Integer isActive;
	public Integer getShopPluginId() {
		return shopPluginId;
	}
	public void setShopPluginId(Integer shopPluginId) {
		this.shopPluginId = shopPluginId;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getPluginId() {
		return pluginId;
	}
	public void setPluginId(Integer pluginId) {
		this.pluginId = pluginId;
	}
	public Integer getBuyNumber() {
		return buyNumber;
	}
	public void setBuyNumber(Integer buyNumber) {
		this.buyNumber = buyNumber;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Integer getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Integer isPaid) {
		this.isPaid = isPaid;
	}
	public Integer getIsSettled() {
		return isSettled;
	}
	public void setIsSettled(Integer isSettled) {
		this.isSettled = isSettled;
	}
	public Integer getIsActive() {
		return isActive;
	}
	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

}
