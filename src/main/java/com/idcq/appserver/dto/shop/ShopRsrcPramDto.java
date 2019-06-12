package com.idcq.appserver.dto.shop;

import java.io.Serializable;

/**
 * 商铺资源dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午6:49:55
 */
public class ShopRsrcPramDto implements Serializable{
	private static final long serialVersionUID = 2688523905008458793L;
	/**
	 * 资源id
	 */
	private Long resourceId;
	/**
	 * 商铺资源
	 */
    private Long shopId;
    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 场地分类id
     */
    private Long categoryId;
    /**
     * 操作类型
     */
    private Integer operateType;
    
	public ShopRsrcPramDto() {
		super();
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	
}