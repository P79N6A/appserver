package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 服务分类数据
 * @author huangrui
 *
 */
public class CategoryDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long categoryId;
	private String shopId;
	@JsonIgnore
	private String token;
	private Integer operateType; // 0-新增，1-修改
	private Long parentCategoryId;
	private String categoryName;
	private String keywords;
	private String carouselAttachmentIds; // 5张轮播图的ID，中间用英文逗号分隔
	private String logoId; // 缩略图ID
	private String remark;
	
	private Integer status = 1; //上架状态，禁用-0,启用-1
	
	private Integer soldNumber;  //销售次数
	private Integer zanNumber; //点赞次数
	
	private String stopDate; //停用日期，多个以英文逗号分隔
	
	private String categoryIds; //分类ID，多个分类以英文逗号(,)分隔
	
	private Integer categoryIndex;//排序
	
	public Integer getSoldNumber() {
		return soldNumber;
	}
	public void setSoldNumber(Integer soldNumber) {
		this.soldNumber = soldNumber;
	}
	public Integer getZanNumber() {
		return zanNumber;
	}
	public void setZanNumber(Integer zanNumber) {
		this.zanNumber = zanNumber;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	
	public Long getParentCategoryId() {
		return parentCategoryId;
	}
	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getCarouselAttachmentIds() {
		return carouselAttachmentIds;
	}
	public void setCarouselAttachmentIds(String carouselAttachmentIds) {
		this.carouselAttachmentIds = carouselAttachmentIds;
	}
	
	public String getLogoId() {
		return logoId;
	}
	public void setLogoId(String logoId) {
		this.logoId = logoId;
	}
	public String getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	// 判断是否是新增操作
	public boolean isAdd() {
		return (operateType == null || operateType == 0);
	}

	// 判断是否是删除操作
	public boolean isDel() {
		return (operateType == null || operateType == 0);
	}

	// 判断是否是停用操作
	public boolean isStop() {
		return (operateType != null && operateType == 1);
	}

	// 判断是否是启用操作
	public boolean isRestart() {
		return (operateType != null && operateType == 2);
	}
    public Integer getCategoryIndex()
    {
        return categoryIndex;
    }
    public void setCategoryIndex(Integer categoryIndex)
    {
        this.categoryIndex = categoryIndex;
    }
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    

}
