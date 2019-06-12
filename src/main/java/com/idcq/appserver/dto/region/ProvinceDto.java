package com.idcq.appserver.dto.region;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 省dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:51:53
 */
public class ProvinceDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8728125290988172371L;
	private Long provinceId;
	private String provinceName;
	@JsonIgnore
	private String dateCreated;
	@JsonIgnore
	private String dateUpdated;
	
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}
	public String getProvinceName() {
		return provinceName;
	}
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateUpdated() {
		return dateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		this.dateUpdated = dateUpdated;
	}
	
}
