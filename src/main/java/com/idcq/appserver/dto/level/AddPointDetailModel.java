package com.idcq.appserver.dto.level;

import com.idcq.appserver.common.annotation.Check;

/**
 * 增加积分model
 * @author ChenYongxin
 *
 */
public class AddPointDetailModel {

	/*    
	    bizType	int		是	业务类型:店铺类型=1,会员类型=2
	    bizId	int		是	业务id(店铺Id ,会员Id)
	    ruleType	int		是	积分规则类型
	    subRuleType	int		是	积分规则子类型
        pointSourceType	int		否	积分来源类型:商家入驻类=1，绑定类=2，发布商品类=3，推荐类=4，制单类=5，注册类=6，评价类=7，消费类=8
        pointSourceId	string		否	积分来源Id
	  */
    @Check()
    private Integer bizId;
    @Check()
    private Integer bizType;
    @Check()
    private Integer ruleType;
    @Check()
    private Integer subRuleType;
    private Integer pointSourceType;
    private String pointSourceId;
    private String remark;

	public Integer getBizId() {
		return bizId;
	}
	public void setBizId(Integer bizId) {
		this.bizId = bizId;
	}
	public Integer getBizType() {
		return bizType;
	}
	public void setBizType(Integer bizType) {
		this.bizType = bizType;
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
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
    

}
