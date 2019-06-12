package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;

/**
 * 更新商品族内商品价格接口
 * json数据接收帮助类
 * @author nie_jq
 *
 */
public class TmpGoodsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1540826455640004269L;
	private Long goodsId;
	private Double goodsPrice;
	private String goodsProsValueIds;
	
	public TmpGoodsDto() {
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public String getGoodsProsValueIds() {
		return goodsProsValueIds;
	}
	public void setGoodsProsValueIds(String goodsProsValueIds) {
		this.goodsProsValueIds = goodsProsValueIds;
	}
	
}
