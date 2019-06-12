package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;

/**
 * 技师级别
 * @author Administrator
 *
 */
public class TechTypeBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String shopId;
	private String techTypeId;
	private Integer operateType;  // 操作类型：0-新增，1-修改，为1时techTypeId必填
	private String parentTechTypeId;
	private String techTypeName;
	private String workNumber;//工号
	private String token;//商铺token
	
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getWorkNumber() {
		return workNumber;
	}
	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getTechTypeId() {
		return techTypeId;
	}
	public void setTechTypeId(String techTypeId) {
		this.techTypeId = techTypeId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public String getParentTechTypeId() {
		return parentTechTypeId;
	}
	public void setParentTechTypeId(String parentTechTypeId) {
		this.parentTechTypeId = parentTechTypeId;
	}
	public String getTechTypeName() {
		return techTypeName;
	}
	public void setTechTypeName(String techTypeName) {
		this.techTypeName = techTypeName;
	}
}
