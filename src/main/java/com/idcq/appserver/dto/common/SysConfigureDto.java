package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.Date;
/**
 * 系统配置表
 * @author Administrator
 *
 */
public class SysConfigureDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4402295926560962901L;
	private Integer configureId;
	private String configureKey;
	private String configureValue;
	private Integer configureType;
	private String configureDesc;
	private Date createTime;
	private Date lastUpdateTime;
	
	public SysConfigureDto() {
	}
	public Integer getConfigureId() {
		return configureId;
	}
	public void setConfigureId(Integer configureId) {
		this.configureId = configureId;
	}
	public String getConfigureKey() {
		return configureKey;
	}
	public void setConfigureKey(String configureKey) {
		this.configureKey = configureKey;
	}
	public String getConfigureValue() {
		return configureValue;
	}
	public void setConfigureValue(String configureValue) {
		this.configureValue = configureValue;
	}
	public Integer getConfigureType() {
		return configureType;
	}
	public void setConfigureType(Integer configureType) {
		this.configureType = configureType;
	}
	public String getConfigureDesc() {
		return configureDesc;
	}
	public void setConfigureDesc(String configureDesc) {
		this.configureDesc = configureDesc;
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
}
