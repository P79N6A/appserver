package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 场地分类定价接口
 * 接收json数据帮助类
 * @author nie_jq
 *
 */
public class TempCateGoryDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5879914320214157668L;
	private String categoryId;
	private String shopId;
	private List<TempGoodsPropertyDto> resources = new ArrayList<TempGoodsPropertyDto>();
	
	public TempCateGoryDto() {
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public List<TempGoodsPropertyDto> getResources() {
		return resources;
	}
	public void setResources(List<TempGoodsPropertyDto> resources) {
		this.resources = resources;
	}
	
}
