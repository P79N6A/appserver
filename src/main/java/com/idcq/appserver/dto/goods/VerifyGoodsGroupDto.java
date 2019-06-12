package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

public class VerifyGoodsGroupDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -6253968014687687590L;
	/**
	 * 商品名称
	 */
	private String goodsName;
	/**
	 * 分类名称
	 */
	private String categoryName;
	
	private String goodsStatus;
	public String getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	/**
	 * 分类id
	 */
    private List<Long> categoryIdList;
	/**
	 * 单位名称
	 */
	private String unitName;
	/**
	 * 商品族属性名称
	 */
	private List<String> groupPropertyName;
	
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public List<String> getGroupPropertyName() {
		return groupPropertyName;
	}
	public void setGroupPropertyName(List<String> groupPropertyName) {
		this.groupPropertyName = groupPropertyName;
	}
	public List<Long> getCategoryIdList() {
		return categoryIdList;
	}
	public void setCategoryIdList(List<Long> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}
	
}
