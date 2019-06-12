package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;

/**
 * 技师和商品族中间表dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 下午6:03:29
 */
public class ShopTechnicianRefGoodsDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7007250917930797642L;
	private Long refGoodsId;
    private Long technicianId;
    private Long goodsGroupId;
    
	public ShopTechnicianRefGoodsDto() {
		super();
	}

	public Long getRefGoodsId() {
		return refGoodsId;
	}

	public void setRefGoodsId(Long refGoodsId) {
		this.refGoodsId = refGoodsId;
	}

	public Long getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Long technicianId) {
		this.technicianId = technicianId;
	}

	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

    
}