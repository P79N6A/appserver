package com.idcq.appserver.dto.order;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class OrderShopGoodsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5045151351764929905L;

	@JsonIgnore
	private String orderId;
	
	private Long shopId;

	private Long goodsId;

	private Integer goodsNumber;

	private Integer goodsIndex;

	// 商铺名称
	private String shopName;

	// 商品名称
	private String goodsName;

	// 单价
	private Double unitPrice;

	// 商品图片url
	private String goodsImg;
	
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Integer getGoodsIndex() {
		return goodsIndex;
	}

	public void setGoodsIndex(Integer goodsIndex) {
		this.goodsIndex = goodsIndex;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getGoodsImg() {
		return goodsImg;
	}

	public void setGoodsImg(String goodsImg) {
		this.goodsImg = goodsImg;
	}

}
