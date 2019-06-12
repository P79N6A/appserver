package com.idcq.idianmgr.dto.order;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 商铺线下订单dto(场地类)
 * 
 * @author Administrator
 * 
 * @date 2015年8月1日
 * @time 上午10:56:31
 */
public class OfflineOrderDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8622186604066199299L;
	private Long shopId;
	private String userName;
	private String mobile;
	private Long resourceId;
	private Date serviceTimeFrom;
	private Date serviceTimeTo;
	private Double orderTotalPrice;
	@JsonIgnore
	private String resourceName;
	
	public OfflineOrderDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
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
	public Long getResourceId() {
		return resourceId;
	}
	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}
	public Date getServiceTimeFrom() {
		return serviceTimeFrom;
	}
	public void setServiceTimeFrom(Date serviceTimeFrom) {
		this.serviceTimeFrom = serviceTimeFrom;
	}
	public Date getServiceTimeTo() {
		return serviceTimeTo;
	}
	public void setServiceTimeTo(Date serviceTimeTo) {
		this.serviceTimeTo = serviceTimeTo;
	}
	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	
}
