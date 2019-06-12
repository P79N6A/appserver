package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.List;

public class BatchInsertCookingDetailModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 478386125647500525L;

	private String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	private Long shopId;
	private List<ShopCookingDetails> cookDetail;
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public List<ShopCookingDetails> getCookDetail() {
		return cookDetail;
	}
	public void setCookDetail(List<ShopCookingDetails> cookDetail) {
		this.cookDetail = cookDetail;
	}
}
