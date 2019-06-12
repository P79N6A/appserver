package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

public class ShopRsrcGroupDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7089894530255428392L;
	private Integer groupId;
    private Integer shopId;
    private String groupName;
    private String groupDesc;
    private Integer resourceNumber;
    private Integer maxPeople;
    private Integer showIndex;
    private Date createTime;
    private Integer minPeople;
    private Integer subscriptionMoney;
    private String bookType;
    
	public ShopRsrcGroupDto() {
		super();
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
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

	public Integer getResourceNumber() {
		return resourceNumber;
	}

	public void setResourceNumber(Integer resourceNumber) {
		this.resourceNumber = resourceNumber;
	}

	public Integer getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(Integer maxPeople) {
		this.maxPeople = maxPeople;
	}

	public Integer getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(Integer showIndex) {
		this.showIndex = showIndex;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getMinPeople() {
		return minPeople;
	}

	public void setMinPeople(Integer minPeople) {
		this.minPeople = minPeople;
	}

	public Integer getSubscriptionMoney() {
		return subscriptionMoney;
	}

	public void setSubscriptionMoney(Integer subscriptionMoney) {
		this.subscriptionMoney = subscriptionMoney;
	}

	public String getBookType() {
		return bookType;
	}

	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
    
}
