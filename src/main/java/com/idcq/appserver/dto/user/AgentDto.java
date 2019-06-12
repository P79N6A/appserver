package com.idcq.appserver.dto.user;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class AgentDto implements Serializable {
	private static final long serialVersionUID = 4549146677985881424L;
	
	@JsonIgnore
    private Long agentId;
	@JsonIgnore
	private Long userId;
	private Integer agentType;
	@JsonIgnore
	private Long provinceId;
	@JsonIgnore
	private Long cityId;
	@JsonIgnore
	private Long districtId;
	@JsonIgnore
	private Long townId;
	@JsonIgnore
	private Date agentValidFrom;
	@JsonIgnore
	private Date agentValidTo;
	private Double slottingFee;
	
	private Long referUserId;
	
	public Long getReferUserId() {
		return referUserId;
	}
	public void setReferUserId(Long referUserId) {
		this.referUserId = referUserId;
	}
	/**
	 * 1-正常,2-停用
	 */
	@JsonIgnore
	private Integer agentStatus;
	/**
	 * 分成比例
	 */
	@JsonIgnore
	private Double agentShareRatio;
	
	private Date createTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getAgentType() {
		return agentType;
	}
	public void setAgentType(Integer agentType) {
		this.agentType = agentType;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public Long getTownId() {
		return townId;
	}
	public Double getSlottingFee() {
		return slottingFee;
	}
	public void setSlottingFee(Double slottingFee) {
		this.slottingFee = slottingFee;
	}
	public void setTownId(Long townId) {
		this.townId = townId;
	}
	public Date getAgentValidFrom() {
		return agentValidFrom;
	}
	public void setAgentValidFrom(Date agentValidFrom) {
		this.agentValidFrom = agentValidFrom;
	}
	public Date getAgentValidTo() {
		return agentValidTo;
	}
	public void setAgentValidTo(Date agentValidTo) {
		this.agentValidTo = agentValidTo;
	}
	public Integer getAgentStatus() {
		return agentStatus;
	}
	public void setAgentStatus(Integer agentStatus) {
		this.agentStatus = agentStatus;
	}
    public Double getAgentShareRatio()
    {
        return agentShareRatio;
    }
    public void setAgentShareRatio(Double agentShareRatio)
    {
        this.agentShareRatio = agentShareRatio;
    }
    public Long getAgentId()
    {
        return agentId;
    }
    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
	
}
