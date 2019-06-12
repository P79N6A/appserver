package com.idcq.appserver.dto.operationclassify;

import java.io.Serializable;

/**
 * 运营分类
 * @ClassName: OperationClassifyDto 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月27日 下午2:07:27 
 *
 */
public class OperationClassifyDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 分类Id
	 */
	private Integer classifyId;
	
	/**
	 * 分类名称
	 */
	private String classifyName;
	
	/**
	 * 分类描述
	 */
	private String classifyDesc;
	
	/**
	 * 分类搜索关键字
	 */
	private String classifyKey;
	
	/**
	 * 索引位置
	 */
	private Integer classifyIndex;
	
	/**
	 * 分类状态
	 */
	private Integer classifyStatus;
	
	/**
	 * 父类Id
	 */
	private Integer parentClassifyId;
	
	/**
	 * 分类图片ID
	 */
	private Integer classifyLogoId;
	
	/**
	 * 分类展示的图片地址
	 */
	private String classifyImgUrl;
	
	/**
	 * 城市Id
	 */
	private Integer cityId;
	
	private Integer childCount;
	
	
	private Integer hasChildren;

	public Integer getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Integer classifyId) {
		this.classifyId = classifyId;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getClassifyDesc() {
		return classifyDesc;
	}

	public void setClassifyDesc(String classifyDesc) {
		this.classifyDesc = classifyDesc;
	}

	public String getClassifyKey() {
		return classifyKey;
	}

	public void setClassifyKey(String classifyKey) {
		this.classifyKey = classifyKey;
	}

	public Integer getClassifyIndex() {
		return classifyIndex;
	}

	public void setClassifyIndex(Integer classifyIndex) {
		this.classifyIndex = classifyIndex;
	}

	public Integer getClassifyStatus() {
		return classifyStatus;
	}

	public void setClassifyStatus(Integer classifyStatus) {
		this.classifyStatus = classifyStatus;
	}

	public Integer getParentClassifyId() {
		return parentClassifyId;
	}

	public void setParentClassifyId(Integer parentClassifyId) {
		this.parentClassifyId = parentClassifyId;
	}

	public Integer getClassifyLogoId() {
		return classifyLogoId;
	}

	public void setClassifyLogoId(Integer classifyLogoId) {
		this.classifyLogoId = classifyLogoId;
	}

	public String getClassifyImgUrl() {
		return classifyImgUrl;
	}

	public void setClassifyImgUrl(String classifyImgUrl) {
		this.classifyImgUrl = classifyImgUrl;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	public Integer getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(Integer hasChildren) {
		this.hasChildren = hasChildren;
	}
}
