package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品族属性dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:34:55
 */
public class GoodsGroupPropertyDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 9160364669054618706L;
	private Long groupPropertyId;
    private String groupPropertyName;
    private Long goodsGroupId;
    private Integer propertyOrder;
    private Integer propertyType;
    private Long shopId;
    private Integer operateType;//操作类型： 0-新增，1-修改
    private Long shopPropertyId;
    
    public Long getShopPropertyId() {
		return shopPropertyId;
	}
	public void setShopPropertyId(Long shopPropertyId) {
		this.shopPropertyId = shopPropertyId;
	}
	private List<GoodsGroupProValuesDto>groupPropertyValues;
	public GoodsGroupPropertyDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getGroupPropertyId() {
		return groupPropertyId;
	}
	public void setGroupPropertyId(Long groupPropertyId) {
		this.groupPropertyId = groupPropertyId;
	}
	public String getGroupPropertyName() {
		return groupPropertyName;
	}
	public void setGroupPropertyName(String groupPropertyName) {
		this.groupPropertyName = groupPropertyName;
	}
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
	public Integer getPropertyOrder() {
		return propertyOrder;
	}
	public void setPropertyOrder(Integer propertyOrder) {
		this.propertyOrder = propertyOrder;
	}
	public Integer getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(Integer propertyType) {
		this.propertyType = propertyType;
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
	public List<GoodsGroupProValuesDto> getGroupPropertyValues() {
		if(groupPropertyValues==null)
		{
			groupPropertyValues=new ArrayList<GoodsGroupProValuesDto>();
		}
		return groupPropertyValues;
	}
	public void setGroupPropertyValues(
			List<GoodsGroupProValuesDto> groupPropertyValues) {
		this.groupPropertyValues = groupPropertyValues;
	}
	
}