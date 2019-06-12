package com.idcq.idianmgr.dto.order;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateTimeSerializer;

/**
 * 商铺订单dto
 * @author shengzhipeng
 * @date:2015年7月30日 下午3:00:00
 */
public class ShopOrderDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6591065006019885723L;
	
	private String orderId;
	
	private Long userId;

	private String userName;
	
	private String mobile;
	
	private Long shopId;
	
	private String shopName;
	
	private Long goodsId;
	
	private String goodsName;

	private Integer orderServiceType;
	
	private String orderTitle;
	
	private String bookTime;
	
	private Double orderTotalPrice;
	
	private Integer orderStatus;
	
	private String goodsLogo;
	
	private String resourceType;
	
	private Long bizId;
	
	private String bizName;
	
	/*-----------20150731-------------*/
	@JsonIgnore
	private Long addressId;
	@JsonIgnore
	private String userRemark;
	@JsonIgnore
	private Double logisticsPrice;
	@JsonIgnore
	private Integer payStatus;
	@JsonSerialize(using=MyDateTimeSerializer.class)
	private Date orderTime;
	@JsonIgnore
	private Double settlePrice;

	@JsonSerialize(using=MyDateTimeSerializer.class)
	private Date currentTime;
	
	

	public Date getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getOrderServiceType() {
		return orderServiceType;
	}

	public void setOrderServiceType(Integer orderServiceType) {
		this.orderServiceType = orderServiceType;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}

	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getGoodsLogo() {
		return goodsLogo;
	}

	public void setGoodsLogo(String goodsLogo) {
		this.goodsLogo = goodsLogo;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public Double getLogisticsPrice() {
		return logisticsPrice;
	}

	public void setLogisticsPrice(Double logisticsPrice) {
		this.logisticsPrice = logisticsPrice;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public Double getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}
	
}
