package com.idcq.appserver.dto.pay;

import java.io.Serializable;

public class OrderGroupDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3860704253521449705L;
	
	private String orderGroupId;
	private String orderId;
	public String getOrderGroupId() {
		return orderGroupId;
	}
	public void setOrderGroupId(String orderGroupId) {
		this.orderGroupId = orderGroupId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
	
}
