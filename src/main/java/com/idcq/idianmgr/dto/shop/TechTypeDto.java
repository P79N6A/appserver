package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 技师级别类
 * @author Administrator
 *
 */
public class TechTypeDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String shopId;
	private String techTypeId;
	@JsonIgnore
	private Integer operateType;  // 操作类型：0-新增，1-修改，为1时techTypeId必填
	@JsonIgnore
	private String parentTechTypeId;
	private String techTypeName;
	@JsonIgnore
	private Date lastUpdateTime;
	@JsonIgnore
	private Date createTime;
	@JsonIgnore
	private Integer isValid; // 是否有效：有效=1，失效(删除)=0
	@JsonIgnore
	private Integer typeOrder;//排序
	@JsonIgnore
	private String techTypeIds;
	
	
	
	public String getTechTypeIds() {
		return techTypeIds;
	}
	public void setTechTypeIds(String techTypeIds) {
		this.techTypeIds = techTypeIds;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getTypeOrder() {
		return typeOrder;
	}
	public void setTypeOrder(Integer typeOrder) {
		this.typeOrder = typeOrder;
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
	
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Integer getIsValid() {
		return isValid;
	}
	public void setIsValid(Integer isValid) {
		this.isValid = isValid;
	}

	@JsonIgnore
	public boolean isAdd(){
		return (operateType == null || operateType == 0); 
	}
}
