package com.idcq.appserver.dto.activity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class BusinessAreaActivityDto implements Serializable{
	
	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -2916789842562530488L;

    private Long businessAreaActivityId;
    private Long modelId;
    private String businessAreaName;
    private Integer activityStatus;
    private Long shopId;
    private Integer actNum;
    private Date beginDate;
    private Date endDate;
    private Date signBeginDate;
    private Date signEndDate;
    private String actPosterIds;
    private Date createTime;
    private String actDesc;
    private Integer isRelease;
    private String activityProtocol;
    private String activityShareTitle;
    private Integer clientSystemType;
    private List<BusinessAreaConfigDto> activityRules;

    private String shopName;
    private Integer shopType;
    private String actPosterUrls;
    private Integer townId;
    private String townName;
    private Date joinTime;
    private Integer joinType;
    private String shopLogoUrl;
    private Integer columnId;
    private String columnName;
    
    private String activityRuleModle;
    
    private Integer shopDistrictId;
    private Long shopCity;
    
    /**
     * 发起店铺列表
     */
    private List<Long>shopIdList;
    public Long getShopCity() {
		return shopCity;
	}

	public void setShopCity(Long shopCity) {
		this.shopCity = shopCity;
	}

	public Integer getShopDistrictId() {
		return shopDistrictId;
	}

	public void setShopDistrictId(Integer districtId) {
		this.shopDistrictId = districtId;
	}

	private Integer isDelete;
    public String getActivityRuleModle() {
		return activityRuleModle;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public void setActivityRuleModle(String activityRuleModle) {
		this.activityRuleModle = activityRuleModle;
	}

	/**
     * 状态不相等
     */
    @JsonIgnore
    private Integer notStatus;
    
    private String activityDesc;
    
    private String activityRuleName;
    
    private Long cityId;
	public BusinessAreaActivityDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getBusinessAreaActivityId() {
		return businessAreaActivityId;
	}

	public void setBusinessAreaActivityId(Long businessAreaActivityId) {
		this.businessAreaActivityId = businessAreaActivityId;
	}

	public Long getModelId() {
		return modelId;
	}

	public void setModelId(Long modelId) {
		this.modelId = modelId;
	}

	public String getBusinessAreaName() {
		return businessAreaName;
	}

	public void setBusinessAreaName(String businessAreaName) {
		this.businessAreaName = businessAreaName;
	}

	public Integer getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getActNum() {
		return actNum;
	}

	public void setActNum(Integer actNum) {
		this.actNum = actNum;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getSignBeginDate() {
		return signBeginDate;
	}

	public void setSignBeginDate(Date signBeginDate) {
		this.signBeginDate = signBeginDate;
	}

	public Date getSignEndDate() {
		return signEndDate;
	}

	public void setSignEndDate(Date signEndDate) {
		this.signEndDate = signEndDate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getActivityProtocol() {
		return activityProtocol;
	}

	public void setActivityProtocol(String activityProtocol) {
		this.activityProtocol = activityProtocol;
	}

	public String getActivityShareTitle() {
		return activityShareTitle;
	}

	public void setActivityShareTitle(String activityShareTitle) {
		this.activityShareTitle = activityShareTitle;
	}

	public Integer getClientSystemType() {
		return clientSystemType;
	}

	public void setClientSystemType(Integer clientSystemType) {
		this.clientSystemType = clientSystemType;
	}

	public List<BusinessAreaConfigDto> getActivityRules() {
		return activityRules;
	}

	public void setActivityRules(List<BusinessAreaConfigDto> activityRules) {
		this.activityRules = activityRules;
	}

	public String getActPosterIds() {
		return actPosterIds;
	}

	public void setActPosterIds(String actPosterIds) {
		this.actPosterIds = actPosterIds;
	}

	public String getActDesc() {
		return actDesc;
	}

	public void setActDesc(String actDesc) {
		this.actDesc = actDesc;
	}

	public Integer getIsRelease() {
		return isRelease;
	}

	public void setIsRelease(Integer isRelease) {
		this.isRelease = isRelease;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}

	@Override
	public String toString() {
		return "BusinessAreaActivityDto [businessAreaActivityId="
				+ businessAreaActivityId + ", modelId=" + modelId
				+ ", businessAreaName=" + businessAreaName
				+ ", activityStatus=" + activityStatus + ", shopId=" + shopId
				+ ", actNum=" + actNum + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", signBeginDate=" + signBeginDate
				+ ", signEndDate=" + signEndDate + ", actPosterIds="
				+ actPosterIds + ", createTime=" + createTime + ", actDesc="
				+ actDesc + ", isRelease=" + isRelease + ", activityProtocol="
				+ activityProtocol + ", activityShareTitle="
				+ activityShareTitle + ", clientSystemType=" + clientSystemType
				+ ", activityRules=" + activityRules + "]";
	}

	public Integer getNotStatus() {
		return notStatus;
	}

	public void setNotStatus(Integer notStatus) {
		this.notStatus = notStatus;
	}

	public String getActivityDesc() {
		return activityDesc;
	}

	public void setActivityDesc(String activityDesc) {
		this.activityDesc = activityDesc;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Integer getJoinType() {
		return joinType;
	}

	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}

	public String getShopLogoUrl() {
		return shopLogoUrl;
	}

	public void setShopLogoUrl(String shopLogoUrl) {
		this.shopLogoUrl = shopLogoUrl;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

    public String getActivityRuleName() {
        return activityRuleName;
    }

    public void setActivityRuleName(String activityRuleName) {
        this.activityRuleName = activityRuleName;
    }

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getActPosterUrls() {
		return actPosterUrls;
	}

	public void setActPosterUrls(String actPosterUrls) {
		this.actPosterUrls = actPosterUrls;
	}

	public List<Long> getShopIdList() {
		return shopIdList;
	}

	public void setShopIdList(List<Long> shopIdList) {
		this.shopIdList = shopIdList;
	}
	
}