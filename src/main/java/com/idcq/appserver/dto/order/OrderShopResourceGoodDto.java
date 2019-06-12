package com.idcq.appserver.dto.order;

public class OrderShopResourceGoodDto implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2165264479162494483L;
	/**
	 * 令牌
	 */
	private String token;
	/**
	 * 商铺id
	 */
	private Long shopId;
	/**
	 * json数据对象
	 */
	private DataJsonDto data;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
