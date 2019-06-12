package com.idcq.appserver.dto.region;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 城市区域dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:51:53
 */
public class DistrictDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8728125290988172371L;
	private Long districtId;
	private String districtName;
	@JsonIgnore
	private Long cityId;
	@JsonIgnore
	private String dateCreated;
	@JsonIgnore
	private String dateUpdated;
	
	public Long getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
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
