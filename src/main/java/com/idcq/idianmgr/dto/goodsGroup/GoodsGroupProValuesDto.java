package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 商品族属性值dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:39:28
 */
public class GoodsGroupProValuesDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5190140939529169469L;
	private Long proValuesId;
    private String proValue;
    @JsonIgnore
    private Long goodsGroupId;
    private Long groupPropertyId;
    private Integer valuesOrder;
    private Integer isSelect;//是否选择：是=1，否=0
    @JsonIgnore
    private Integer operateType;//操作类型： 0-新增，1-修改
    @JsonIgnore
    private Long shopId;
    
    
	public GoodsGroupProValuesDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getProValuesId() {
		return proValuesId;
	}
	public void setProValuesId(Long proValuesId) {
		this.proValuesId = proValuesId;
	}
	public String getProValue() {
		return proValue;
	}
	public void setProValue(String proValue) {
		this.proValue = proValue;
	}
	public Long getGroupPropertyId() {
		return groupPropertyId;
	}
	public void setGroupPropertyId(Long groupPropertyId) {
		this.groupPropertyId = groupPropertyId;
	}
	public Integer getValuesOrder() {
		return valuesOrder;
	}
	public void setValuesOrder(Integer valuesOrder) {
		this.valuesOrder = valuesOrder;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
	public Integer getIsSelect() {
		return isSelect;
	}
	public void setIsSelect(Integer isSelect) {
		this.isSelect = isSelect;
	}
	
}