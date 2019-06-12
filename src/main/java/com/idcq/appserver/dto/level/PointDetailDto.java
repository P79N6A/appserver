package com.idcq.appserver.dto.level;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class PointDetailDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2024995409232554279L;
	@JsonIgnore
	private String token;
	
	/**
	 * 店铺Id
	 */
	@JsonIgnore
	private Integer ShopId;
	
	/**
	 * 查询积分值下限（用于查询积分值区间）
	 */
	private Integer lowerPointValue;
	
	/**
	 * 查询积分值上限（用于查询积分值区间）
	 */
	private Integer upperPointValue;
	
	/**
	 * 查询起始时间
	 */
	private Date startTime;
	
	/**
	 * 查询截止时间
	 */
	private Date endTime;
	
	/**
	 * 页码,从第1页开始
	 */
	@JsonIgnore
	private Integer pNo;
	
	/**
	 * 每页记录数
	 */
	@JsonIgnore
	private Integer pSize;
	
	/**
	 * 积分明细id
	 */
	private Integer pointDtailId;
	
	/**
	 * 业务类型:店铺类型=1,会员类型=2
	 */
	private Integer bizType;
	
	/**
	 * 业务 id(店铺 Id ,会员 Id)
	 */
	private Integer bizId;
	private String bizIds;//(多个，用逗号隔开)
	/**
	 * 积分规则Id
	 */
	private Integer pointRuleId;
	
	/**
	 * 积分来源类型: 商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，
	 *              制单类=5，注册类=6，评价类=7，消费类=8
	 */
	private Integer pointSourceType;
	
	/**
	 * 积分来源id
	 */
	private String pointSourceId;
	
	/**
	 * 积分来源标题
	 */
	private String pointDetailTitle;
	
	/**
	 * 获取积分值
	 */
	private Integer pointValue;
	
	/**
	 * 获取积分时间
	 */
	private Date createTime;
	
	/**
	 * 积分后的积分值
	 */
	private Integer pointAferValue;
	
	/**
	 * 规则类型，商家入驻类=1，绑定类=2，发布商品类=3，
	 * 推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
	 */
	private Integer ruleType;
	
	/**
	 * 任务类型，常规性任务=1，非常规性任务=2
	 */
	private Integer taskType;
	
	/**
	 * 积分规则子类型，商家入驻类：1=店铺入驻
	 */
	private Integer subRuleType;
	
	/**
	 * 规则名称
	 */
	private String ruleName;
	
	/**
	 * 规则明细
	 */
	private String ruleDetail;

	/**
	 * 操作人id
	 */
	private Long operaterId;
	
	/**
	 * 操作人名称
	 */
	private String operaterName;
	
	/**
	 * 备注
	 */
	private String remark;

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

	public Integer getLowerPointValue() {
		return lowerPointValue;
	}

	public void setLowerPointValue(Integer lowerPointValue) {
		this.lowerPointValue = lowerPointValue;
	}

	public Integer getUpperPointValue() {
		return upperPointValue;
	}

	public void setUpperPointValue(Integer upperPointValue) {
		this.upperPointValue = upperPointValue;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
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

	public Integer getPointDtailId() {
		return pointDtailId;
	}

	public void setPointDtailId(Integer pointDtailId) {
		this.pointDtailId = pointDtailId;
	}

	public Integer getBizType() {
		return bizType;
	}

	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}

	public Integer getBizId() {
		return bizId;
	}

	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}

	public Integer getPointRuleId() {
		return pointRuleId;
	}

	public void setPointRuleId(Integer pointRuleId) {
		this.pointRuleId = pointRuleId;
	}

	public Integer getPointSourceType() {
		return pointSourceType;
	}

	public void setPointSourceType(Integer pointSourceType) {
		this.pointSourceType = pointSourceType;
	}

	public String getPointSourceId() {
		return pointSourceId;
	}

	public void setPointSourceId(String pointSourceId) {
		this.pointSourceId = pointSourceId;
	}

	public String getPointDetailTitle() {
		return pointDetailTitle;
	}

	public void setPointDetailTitle(String pointDetailTitle) {
		this.pointDetailTitle = pointDetailTitle;
	}

	public Integer getPointValue() {
		return pointValue;
	}

	public void setPointValue(Integer pointValue) {
		this.pointValue = pointValue;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPointAferValue() {
		return pointAferValue;
	}

	public void setPointAferValue(Integer pointAferValue) {
		this.pointAferValue = pointAferValue;
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

	public Integer getSubRuleType() {
		return subRuleType;
	}

	public void setSubRuleType(Integer subRuleType) {
		this.subRuleType = subRuleType;
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

	public Long getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(Long operaterId) {
		this.operaterId = operaterId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperaterName() {
		return operaterName;
	}

	public void setOperaterName(String operaterName) {
		this.operaterName = operaterName;
	}

	public String getBizIds() {
		return bizIds;
	}

	public void setBizIds(String bizIds) {
		this.bizIds = bizIds;
	}
	


}
