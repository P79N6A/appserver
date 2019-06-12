/**
 * 
 */
package com.idcq.appserver.dto.essential;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/** 
 * 单元类
 * @ClassName: Unit 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 下午7:05:34 
 *  
 */
public class Unit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1002920105034452708L;

	private Long unitId;
	
	@JsonIgnore
	private Integer unitIndex;
	
	private String unitName;	
	@JsonIgnore
	private Integer status;
	@JsonIgnore
	private Integer unitType;
	
	private Integer digitScale;
	@JsonIgnore
	private Long shopId;
	
	private Integer sourceType;
	
	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Integer getUnitIndex() {
		return unitIndex;
	}

	public void setUnitIndex(Integer unitIndex) {
		this.unitIndex = unitIndex;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUnitType() {
		return unitType;
	}

	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}
	
	public Integer getDigitScale() {
		return digitScale;
	}

	public void setDigitScale(Integer digitScale) {
		this.digitScale = digitScale;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

    public Integer getSourceType()
    {
        return sourceType;
    }

    public void setSourceType(Integer sourceType)
    {
        this.sourceType = sourceType;
    }
	
}
