package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;

public class OrderLogDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2306342240915629691L;

	private Long orderLogId;

	private String orderId;
	
	private Integer payStatus;
	
	private Integer orderStatus;
	
	private String remark;
	
	private Date lastUpdateTime;
	
	private Long userId;

	public Long getOrderLogId() {
		return orderLogId;
	}

	public void setOrderLogId(Long orderLogId) {
		this.orderLogId = orderLogId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
