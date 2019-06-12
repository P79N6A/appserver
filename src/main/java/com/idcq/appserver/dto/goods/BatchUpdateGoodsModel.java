package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class BatchUpdateGoodsModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1822885540369408564L;

	private List<Map<String, String>> lst;

	private String token;
	private Long shopId;
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public List<Map<String, String>> getLst() {
		return lst;
	}

	public void setLst(List<Map<String, String>> lst) {
		this.lst = lst;
	}

}
