package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

import com.idcq.appserver.common.annotation.Check;

public class SyncGoodsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2124939023871457607L;
	
	@Check()
	private Long shopId;
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public List<Long> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<Long> goodsList) {
		this.goodsList = goodsList;
	}
	@Check()
	private List<Long> goodsList;
}
