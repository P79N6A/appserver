package com.idcq.appserver.dto.level;

public class CalculatePointMessageModel {

	private Integer ruleType;
	private Integer subRuleType;
	private Integer pointSourceType;
	private String pointSourceId;
	private Integer pointTargetType;
	private Integer pointTargetId;
	public Integer getPointTargetType() {
		return pointTargetType;
	}
	public void setPointTargetType(Integer pointTargetType) {
		this.pointTargetType = pointTargetType;
	}
	public Integer getPointTargetId() {
		return pointTargetId;
	}
	public void setPointTargetId(Integer pointTargetId) {
		this.pointTargetId = pointTargetId;
	}
	public Integer getRuleType() {
		return ruleType;
	}
	public void setRuleType(Integer ruleType) {
		this.ruleType = ruleType;
	}
	public Integer getSubRuleType() {
		return subRuleType;
	}
	public void setSubRuleType(Integer subRuleType) {
		this.subRuleType = subRuleType;
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
}
