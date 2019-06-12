package com.idcq.appserver.dto.level;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PointRuleDto  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4469149137922045523L;
	@JsonIgnore
	private String token;
	
	private Integer pointRuleId;
	/**
	 * 店铺Id
	 */
	@JsonIgnore
	private Integer ShopId;
	
	/**
	 * 
	 * 业务类型: 商家入驻类=1，绑定类=2，发布商品类=3，
	 *           推荐店铺=4， 推荐会员=5， 制单类=5，注册店铺=6，
	 *           注册会员=7， 评价类=,8，消费类=9（不填则查询所有积分规则）
	 * 
	 */
	private Integer ruleType;
	
	/**
	 * 任务类型，常规性任务=1，非常规性任务=2
	 */
	private Integer taskType;
	
	/**
	 * 
	 */
	private Integer subRuleType;
	                
	/**
	 * 
	 */
	private String ruleName; 
	
	/**
	 * 
	 */
	private String ruleDetail;
	
	/**
	 * 
	 */
	private Integer pointValue;
	
	/**
	 * 
	 */
	@JsonIgnore
	private Integer isDelete;
	
	/**
	 * 
	 */
	private String createTime;
	
	/**
	 * 
	 */
	private String lastUpdateTime;
	

	/**
	 * 页码,从第1页开始
	 */
	@JsonIgnore
	private Integer pNo=0;
	
	/**
	 * 每页记录数
	 */
	@JsonIgnore
	private Integer pSize=10;
	/**
	 * 页码,从第1页开始
	 */
	@JsonIgnore
	private Integer pageNo;
	
	/**
	 * 每页记录数
	 */
	@JsonIgnore
	private Integer pageSize;
	

	public Integer getPointRuleId() {
		return pointRuleId;
	}

	public void setPointRuleId(Integer pointRuleId) {
		this.pointRuleId = pointRuleId;
	}

	public Integer getSubRuleType() {
		return subRuleType;
	}

	public void setSubRuleType(Integer subRuleType) {
		this.subRuleType = subRuleType;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getShopId() {
		return ShopId;
	}

	public void setShopId(Integer shopId) {
		ShopId = shopId;
	}

	public Integer getRuleType() {
		return ruleType;
	}

	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getpNo() {
		return pNo;
	}

	public void setpNo(Integer pNo) {
		this.pNo = pNo;
	}

	public Integer getpSize() {
		return pSize;
	}

	public void setpSize(Integer pSize) {
		this.pSize = pSize;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleDetail() {
		return ruleDetail;
	}

	public void setRuleDetail(String ruleDetail) {
		this.ruleDetail = ruleDetail;
	}

	public Integer getPointValue() {
		return pointValue;
	}

	public void setPointValue(Integer pointValue) {
		this.pointValue = pointValue;
	}

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}
	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	

}
