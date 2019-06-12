package com.idcq.appserver.dto.level;

import java.io.Serializable;
import java.util.Date;

import com.idcq.appserver.common.annotation.Check;

public class PrerogativeValidDto  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  /**
	   * 特权 Id
	   */
	  
	private Integer prerogativeId; 

	/**
	 * 特权类型，多个以逗号分隔
	 */
	private Integer prerogativeType;
	  /**
	   * 特权名称
	   */
	 @Check()
	  private String prerogativeName;
	  
	  /**
	   * 特权值
	   */
	 @Check()
	  private String prerogativeValue;
	  
	  /**
	   * 特权图标附件Id
	   */
	  private Integer prerogativeImageId;
	  
	  /**
	   * 特权图标
	   */
	  private String prerogativeImageUrl;
	  
	  /**
	   * 特权排序字段值
	   */
	  @Check()
	  private Integer sortBy;
	  
	  /**
	   * 特权描述
	   */
	  private String prerogativeDesc;
	  
	  private Date createTime;
	  
	  private Date lastUpdateTime;
	  
	  private Integer isDelete;//是否删除：1=是，0=否

	public Integer getPrerogativeId() {
		return prerogativeId;
	}

	public void setPrerogativeId(Integer prerogativeId) {
		this.prerogativeId = prerogativeId;
	}

	public Integer getPrerogativeType() {
		return prerogativeType;
	}

	public void setPrerogativeType(Integer prerogativeType) {
		this.prerogativeType = prerogativeType;
	}

	public String getPrerogativeName() {
		return prerogativeName;
	}

	public void setPrerogativeName(String prerogativeName) {
		this.prerogativeName = prerogativeName;
	}

	public String getPrerogativeValue() {
		return prerogativeValue;
	}

	public void setPrerogativeValue(String prerogativeValue) {
		this.prerogativeValue = prerogativeValue;
	}
	
	public Integer getPrerogativeImageId() {
		return prerogativeImageId;
	}

	public void setPrerogativeImageId(Integer prerogativeImageId) {
		this.prerogativeImageId = prerogativeImageId;
	}

	public String getPrerogativeImageUrl() {
		return prerogativeImageUrl;
	}

	public void setPrerogativeImageUrl(String prerogativeImageUrl) {
		this.prerogativeImageUrl = prerogativeImageUrl;
	}

	public Integer getSortBy() {
		return sortBy;
	}

	public void setSortBy(Integer sortBy) {
		this.sortBy = sortBy;
	}

	public String getPrerogativeDesc() {
		return prerogativeDesc;
	}

	public void setPrerogativeDesc(String prerogativeDesc) {
		this.prerogativeDesc = prerogativeDesc;
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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	

}
