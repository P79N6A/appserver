package com.idcq.appserver.dto.pageArea;

import java.util.Date;

public class PageAreaDto {
	
	/**
	 * 自增长id
	 */
	private Integer pageAreaId;
	
	/**
	 * 省份id
	 */
	private Integer provinceId;
	
	/**
	 * 城市id
	 */
	private Integer cityId;
	
	/**
	 * 区县id
	 */
	private Integer districeId;
	
	/**
	 * 位置类型
	 */
	private Integer positionType;
	
	/**
	 *  投放链接的路径
	 */
	private String pageAreaUrl;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	public Integer getPageAreaId() {
		return pageAreaId;
	}

	public void setPageAreaId(Integer pageAreaId) {
		this.pageAreaId = pageAreaId;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistriceId() {
		return districeId;
	}

	public void setDistriceId(Integer districeId) {
		this.districeId = districeId;
	}

	public Integer getPositionType() {
		return positionType;
	}

	public void setPositionType(Integer positionType) {
		this.positionType = positionType;
	}

	public String getPageAreaUrl() {
		return pageAreaUrl;
	}

	public void setPageAreaUrl(String pageAreaUrl) {
		this.pageAreaUrl = pageAreaUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
