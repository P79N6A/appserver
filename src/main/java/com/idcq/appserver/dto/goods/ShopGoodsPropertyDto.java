package com.idcq.appserver.dto.goods;

import java.io.Serializable;

public class ShopGoodsPropertyDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8568398767071572633L;
	
	private Long shopPropertyId;
	private String shopPropertyName;
	private String subPropertyName;
	private Long shopId;
	private Long parentShopPropertyId;
	private Integer propertyOrder;
	public Long getShopPropertyId() {
		return shopPropertyId;
	}
	public void setShopPropertyId(Long shopPropertyId) {
		this.shopPropertyId = shopPropertyId;
	}
	public String getShopPropertyName() {
		return shopPropertyName;
	}
	public void setShopPropertyName(String shopPropertyName) {
		this.shopPropertyName = shopPropertyName;
	}
	public String getSubPropertyName() {
		return subPropertyName;
	}
	public void setSubPropertyName(String subPropertyName) {
		this.subPropertyName = subPropertyName;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getParentShopPropertyId() {
		return parentShopPropertyId;
	}
	public void setParentShopPropertyId(Long parentShopPropertyId) {
		this.parentShopPropertyId = parentShopPropertyId;
	}
	public Integer getPropertyOrder() {
		return propertyOrder;
	}
	public void setPropertyOrder(Integer propertyOrder) {
		this.propertyOrder = propertyOrder;
	}

}
