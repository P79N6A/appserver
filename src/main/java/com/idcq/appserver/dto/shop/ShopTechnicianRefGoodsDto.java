/**
 * Copyright (C) 2015 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.shop.ShopTechnicianRefGoods
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2015年7月30日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.shop;

import java.io.Serializable;

public class ShopTechnicianRefGoodsDto  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4740455379633058307L;
	/**
	 * 关联ID
	 */
	private Long refGoodsId;
	/**
	 * 技师ID
	 */
	private Long technicianId;
	/**
	 * 商品族ID
	 */
	private Long goodsGroupId;
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
