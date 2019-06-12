package com.idcq.appserver.dto.order;

public class PushDataInfoDto implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2165264479162494483L;
	/**
	 * 令牌
	 */
	private String action;
	/**
	 * 商铺id
	 */
	private Long shopId;
	/**
	 * json数据对象
	 */
	private DataJsonDto data;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public DataJsonDto getData() {
		return data;
	}
	public void setData(DataJsonDto data) {
		this.data = data;
	}

}
