package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;
import java.util.Date;

/**
 * 商品族分类关系表dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:13:43
 */
public class GoodsGroupCategoryRelationDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2761235740350509593L;
	private Long groupCrId;
    private Long goodsGroupId;
    private Integer crIndex;
    private Long groupCategoryId;
    private Long parentCategoryId;
    private Integer crStatus;
    private Date createTime;
    private Date lastUpdateTime;
    
    private String categoryName;
    
	public GoodsGroupCategoryRelationDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getGroupCrId() {
		return groupCrId;
	}

	public void setGroupCrId(Long groupCrId) {
		this.groupCrId = groupCrId;
	}

	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

	public Integer getCrIndex() {
		return crIndex;
	}

	public void setCrIndex(Integer crIndex) {
		this.crIndex = crIndex;
	}

	public Long getGroupCategoryId() {
		return groupCategoryId;
	}

	public void setGroupCategoryId(Long groupCategoryId) {
		this.groupCategoryId = groupCategoryId;
	}

	public Long getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public Integer getCrStatus() {
		return crStatus;
	}

	public void setCrStatus(Integer crStatus) {
		this.crStatus = crStatus;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}