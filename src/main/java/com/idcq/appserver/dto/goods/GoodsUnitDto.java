package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.dto.column.ColumnDto;


/**
 * 商品单位
 */
public class GoodsUnitDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9195673049643467558L;

	/**
	 * 	商品计量单位ID
	 */
	private Integer unitId;
	/**
	 * 排序
	 */
	private Integer unitIndex; 
	/**
	 * 商品单位名称
	 */
	private String unitName; 
	/**
	 * 小数点位数，如=3表示支持小数后3位
	 */
	private Integer digitScale=0; 
	/**
	 * 上架状态，禁用-0,启用-1
	 */
	private Integer status=1; 
	/**
	 * 商品计量单位类型：1（餐饮），2（通用服务）
	 */
	private Integer unitType ;
	/**
	 * 商铺id,表示只适用于该店铺的特殊单位
	 */
	private Long shopId ; 
  
	/**
	 * 单位来源：=1商铺，=0/NULL平台
	 */
    private Integer sourceType=0;

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
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

	public Integer getDigitScale() {
		return digitScale;
	}

	public void setDigitScale(Integer digitScale) {
		this.digitScale = digitScale;
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	
}