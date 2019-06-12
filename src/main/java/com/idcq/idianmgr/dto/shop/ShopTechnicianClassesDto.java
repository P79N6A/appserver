package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 技师班次dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:09:47
 */
public class ShopTechnicianClassesDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 8912700024254206527L;
	private Long tcId;
    private Long shopId;
    private Date classesDate;
    private Long technicianId;
    private Long classesType;
    private Date createTime;
    private Date lastUpdateTime;
    
    @JsonIgnore
    private String startDate;
    
    @JsonIgnore
    private String endDate;
    
    @JsonIgnore
    private String techId;
	public ShopTechnicianClassesDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getTcId() {
		return tcId;
	}

	public void setTcId(Long tcId) {
		this.tcId = tcId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Date getClassesDate() {
		return classesDate;
	}

	public void setClassesDate(Date classesDate) {
		this.classesDate = classesDate;
	}

	public Long getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Long technicianId) {
		this.technicianId = technicianId;
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getTechId() {
		return techId;
	}

	public void setTechId(String techId) {
		this.techId = techId;
	}

	public Long getClassesType() {
		return classesType;
	}

	public void setClassesType(Long classesType) {
		this.classesType = classesType;
	}


}