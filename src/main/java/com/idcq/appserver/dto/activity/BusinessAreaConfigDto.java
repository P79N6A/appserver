package com.idcq.appserver.dto.activity;

import java.io.Serializable;
import java.util.Date;

public class BusinessAreaConfigDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8693163155633100498L;
    private Long businessAreaConfigId;
    private Long businessAreaActivityId;
    private Integer configType;
    private String configValue;
    private String configCode;
    private String configName;
    private String configDesc;
    private Integer groupId;
    private Date createTime;

	public BusinessAreaConfigDto() {
	}

	public Long getBusinessAreaConfigId() {
		return businessAreaConfigId;
	}

	public void setBusinessAreaConfigId(Long businessAreaConfigId) {
		this.businessAreaConfigId = businessAreaConfigId;
	}

	public Long getBusinessAreaActivityId() {
		return businessAreaActivityId;
	}

	public void setBusinessAreaActivityId(Long businessAreaActivityId) {
		this.businessAreaActivityId = businessAreaActivityId;
	}

	public Integer getConfigType() {
		return configType;
	}

	public void setConfigType(Integer configType) {
		this.configType = configType;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getConfigDesc() {
		return configDesc;
	}

	public void setConfigDesc(String configDesc) {
		this.configDesc = configDesc;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "BusinessAreaConfigDto [businessAreaConfigId="
				+ businessAreaConfigId + ", businessAreaActivityId="
				+ businessAreaActivityId + ", configType=" + configType
				+ ", configValue=" + configValue + ", configCode=" + configCode
				+ ", configName=" + configName + ", configDesc=" + configDesc
				+ ", groupId=" + groupId + ", createTime=" + createTime + "]";
	}
    
	
	
    

}