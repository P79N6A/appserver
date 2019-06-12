package com.idcq.appserver.dto.goods;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;



/**
 * 商品分类（服务）dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 下午6:39:41
 */
public class GoodsCategoryDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7330812921665807812L;
	private Long goodsCategoryId;		//主键
	@JsonIgnore
	private Long shopId;		//商铺ID
	private Integer categoryIndex;		//排序
	private String categoryName;	//分类名称
	private Long parentCategoryId;		//上级分类id
	private Integer status;		//上架状态，禁用-0,启用-1
	private String keywords;//分类关键字，多个用英文逗号隔开
	private String remark;
	private String stopDate;
	
	private String carouselUrls;//5张轮播图的URL，中间用英文逗号分隔
	private String carouselAttachmentIds;//5张轮播图的ID，与url对应，中间用英文逗号分隔
	private String logoUrl;
	private String logoId;//缩略图ID
	
	private Integer goodsTotal;//分类下的商品总数
	
	private Integer columnId;
	
	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Long getSourceGoodsCategoryId() {
		return sourceGoodsCategoryId;
	}

	public void setSourceGoodsCategoryId(Long sourceGoodsCategoryId) {
		this.sourceGoodsCategoryId = sourceGoodsCategoryId;
	}

	private Long sourceGoodsCategoryId;

	public Integer getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(Integer goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	public GoodsCategoryDto() {
		super();
	}

	public Integer getCategoryIndex() {
		return categoryIndex;
	}

	public void setCategoryIndex(Integer categoryIndex) {
		this.categoryIndex = categoryIndex;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}


	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}


	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setCategoryKey(String keywords) {
		this.keywords = keywords;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public String getCarouselUrls() {
		return carouselUrls;
	}

	public void setCarouselUrls(String carouselUrls) {
		this.carouselUrls = carouselUrls;
	}

	public String getCarouselAttachmentIds() {
		return carouselAttachmentIds;
	}

	public void setCarouselAttachmentIds(String carouselAttachmentIds) {
		this.carouselAttachmentIds = carouselAttachmentIds;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getLogoId() {
		return logoId;
	}

	public void setLogoId(String logoId) {
		this.logoId = logoId;
	}
	
}
