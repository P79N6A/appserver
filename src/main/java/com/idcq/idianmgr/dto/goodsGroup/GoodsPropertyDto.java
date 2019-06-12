package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;

/**
 * 商品属性dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:44:33
 */
public class GoodsPropertyDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -4810359092700376972L;
	private Long goodsPropertyId;
    private Long groupPropertyId;
    private Long proValuesId;
    private Long goodsId;
	public GoodsPropertyDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getGoodsPropertyId() {
		return goodsPropertyId;
	}
	public void setGoodsPropertyId(Long goodsPropertyId) {
		this.goodsPropertyId = goodsPropertyId;
	}
	public Long getGroupPropertyId() {
		return groupPropertyId;
	}
	public void setGroupPropertyId(Long groupPropertyId) {
		this.groupPropertyId = groupPropertyId;
	}
	public Long getProValuesId() {
		return proValuesId;
	}
	public void setProValuesId(Long proValuesId) {
		this.proValuesId = proValuesId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

    
}