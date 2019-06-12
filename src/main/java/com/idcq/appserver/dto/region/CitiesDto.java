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
public class CitiesDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8728125290988172371L;
	private Long cityId;
	private String cityName;
	@JsonIgnore
	private String zipCode;
	@JsonIgnore
	private Long provinceId;
	@JsonIgnore
	private String dateCreated;
	@JsonIgnore
	private String dateUpdated;
	@JsonIgnore
	private String firstLetter;
	@JsonIgnore
	private String cityShort;
	
	public CitiesDto() {
		super();
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
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

	public String getFirstLetter() {
		return firstLetter;
	}

	public void setFirstLetter(String firstLetter) {
		this.firstLetter = firstLetter;
	}

    public String getCityShort()
    {
        return cityShort;
    }

    public void setCityShort(String cityShort)
    {
        this.cityShort = cityShort;
    }

}
