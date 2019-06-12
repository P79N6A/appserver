package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 商铺资源dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午6:49:55
 */
public class ShopRsrcDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 2688523905008458793L;
	private Integer resourceId;
    private Long shopId;
    private String resourceName;
    private String resourceDesc;
    private Integer groupId;
    private Integer positionId;
    private String resourceNo;
    private Date createTime;
    private Integer parentResourceId;
    private String resourceType;
    private Integer resourceStatus;
    private String orderId;
    private Date usedFromDateTime;
    private String groupName;
    
	public ShopRsrcDto() {
		super();
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
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
	public String getResourceDesc() {
		return resourceDesc;
	}
	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getPositionId() {
		return positionId;
	}
	public void setPositionId(Integer positionId) {
		this.positionId = positionId;
	}
	public String getResourceNo() {
		return resourceNo;
	}
	public void setResourceNo(String resourceNo) {
		this.resourceNo = resourceNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getParentResourceId() {
		return parentResourceId;
	}
	public void setParentResourceId(Integer parentResourceId) {
		this.parentResourceId = parentResourceId;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public Integer getResourceStatus() {
		return resourceStatus;
	}
	public void setResourceStatus(Integer resourceStatus) {
		this.resourceStatus = resourceStatus;
	}
	public Date getUsedFromDateTime() {
		return usedFromDateTime;
	}
	public void setUsedFromDateTime(Date usedFromDateTime) {
		this.usedFromDateTime = usedFromDateTime;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}