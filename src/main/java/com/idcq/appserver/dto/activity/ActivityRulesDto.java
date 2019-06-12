package com.idcq.appserver.dto.activity;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

/**
 * 活动规则dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 */
public class ActivityRulesDto implements Serializable
{
    private static final long serialVersionUID = 4447550589523390537L;
    
    /**
     * 配置项代码
     */
    private String configCode;
    /**
     * 配置类型
     */
    private Integer configType;
    /**
     * 配置组号
     */
    private Integer groupId;
    /**
     * 配置值
     */
    private String configValue;
	public String getConfigCode() {
		return configCode;
	}
	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}
	public Integer getConfigType() {
		return configType;
	}
	public void setConfigType(Integer configType) {
		this.configType = configType;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public String getConfigValue() {
		return configValue;
	}
	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
   

   

}