package com.idcq.appserver.dto.help;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

public class HelpCategoryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7292323389765440861L;

	private Long categoryId;  //分类ID
	private String categoryName; //分类名称
	private Integer categoryIndex; //分类排序
	
	@JsonIgnore
	private Long parentCategoryId; //上级分类id
	@JsonIgnore
	private Integer status; //上架状态，禁用-0,启用-1
	
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getCategoryIndex() {
		return categoryIndex;
	}
	public void setCategoryIndex(Integer categoryIndex) {
		this.categoryIndex = categoryIndex;
	}
	public Long getParentCategoryId() {
		return parentCategoryId;
	}
	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
