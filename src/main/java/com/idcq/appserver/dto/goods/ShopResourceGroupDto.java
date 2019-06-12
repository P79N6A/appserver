package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ShopResourceGroupDto implements Serializable {

	private static final long serialVersionUID = 7753726614064184035L;

	@JsonIgnore
	private Long shopId;
	@JsonIgnore
	private Long groupId;
	@JsonIgnore
	private String groupName;
	@JsonIgnore
	private String groupDesc;
	@JsonIgnore
	private int resourceNumber;
	@JsonIgnore
	private int minPeople;
	@JsonIgnore
	private int maxPeople;
	private int showIndex;
	@JsonIgnore
	private Date createTime;
	@JsonIgnore
	private Integer subscriptionMoney;
	private String resourceType;
	
	public ShopResourceGroupDto() {
		super();
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public int getResourceNumber() {
		return resourceNumber;
	}
	public void setResourceNumber(int resourceNumber) {
		this.resourceNumber = resourceNumber;
	}
	public int getMaxPeople() {
		return maxPeople;
	}
	public void setMaxPeople(int maxPeople) {
		this.maxPeople = maxPeople;
	}
	public int getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getSubscriptionMoney() {
		return subscriptionMoney;
	}
	public void setSubscriptionMoney(Integer subscriptionMoney) {
		this.subscriptionMoney = subscriptionMoney;
	}
	public int getMinPeople() {
		return minPeople;
	}
	public void setMinPeople(int minPeople) {
		this.minPeople = minPeople;
	}
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	
}
