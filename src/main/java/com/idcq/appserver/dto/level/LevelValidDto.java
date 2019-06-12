package com.idcq.appserver.dto.level;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.idcq.appserver.common.annotation.Check;


public class LevelValidDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9195673049643467558L;
	
	private String token;
	/**
	 * 等级Id
	 */
    private Integer levelId;
    
    /**
     * 商铺Id
     */
    private Integer ShopId;
    
    /**
     * 等级类型:1=店铺， 2=会员，不填则查询所有类型等级
     */
    private Integer levelType;
    
    /**
     * 等级名称
     */
    @Check()
    private String levelName;
    
    /**
     * 等级达成条件（等级积分值下限）
     */
    @Check()
    private Integer levelCondition;
    
    /**
     * 等级值（等级排序字段）
     */
    @Check()
    private Integer sortBy;
    
    /**
     * 等级描述
     */
    private String levelDesc;
    
    /**
     * 等级附件图
     */
    private String levelImageUrl ;
    
    /**
     * 等级附件图Id
     */
    private Integer levelImageId ;
    
    private Date createTime;
    
    private Date lastUpdateTime;
    private Integer isDelete;//是否从删除：1-删除；0-不删除
    /**
     * 等级特权列表
     */
    private List<PrerogativeDto> prerogativeList ;

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Integer getLevelId() {
		return levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public Integer getLevelCondition() {
		return levelCondition;
	}

	public void setLevelCondition(Integer levelCondition) {
		this.levelCondition = levelCondition;
	}

	public Integer getSortBy() {
		return sortBy;
	}

	public void setSortBy(Integer sortBy) {
		this.sortBy = sortBy;
	}

	public String getLevelDesc() {
		return levelDesc;
	}

	public void setLevelDesc(String levelDesc) {
		this.levelDesc = levelDesc;
	}

	public String getLevelImageUrl() {
		return levelImageUrl;
	}

	public void setLevelImageUrl(String levelImageUrl) {
		this.levelImageUrl = levelImageUrl;
	}

	public Integer getLevelImageId() {
		return levelImageId;
	}

	public void setLevelImageId(Integer levelImageId) {
		this.levelImageId = levelImageId;
	}

	public List<PrerogativeDto> getPrerogativeList() {
		return prerogativeList;
	}

	public void setPrerogativeList(List<PrerogativeDto> prerogativeList) {
		this.prerogativeList = prerogativeList;
	}

	public Integer getShopId() {
		return ShopId;
	}

	public void setShopId(Integer shopId) {
		ShopId = shopId;
	}

	public Integer getLevelType() {
		return levelType;
	}

	public void setLevelType(Integer levelType) {
		this.levelType = levelType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	

}