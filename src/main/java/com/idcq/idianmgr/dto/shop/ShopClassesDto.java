package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 班次dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:06:39
 */
public class ShopClassesDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1006474847312719998L;
	private Long classesId;
    private Long shopId;
    private Integer classType;
    private String workTime;
    private Date createTime;
    
    private Integer classesType;
    
	public ShopClassesDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Long getClassesId() {
		return classesId;
	}

	public void setClassesId(Long classesId) {
		this.classesId = classesId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	

	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getClassType() {
		return classType;
	}

	public void setClassType(Integer classType) {
		this.classType = classType;
	}

	public Integer getClassesType() {
		return classesType;
	}

	public void setClassesType(Integer classesType) {
		this.classesType = classesType;
	}

    
}