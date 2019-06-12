package com.idcq.appserver.dto.order;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OrderGoodsServiceTech implements Serializable {

	private Integer orderTechId;//订单商品服务技师的唯一标识
	
	private Integer orderGoodsId;//订单商品id-对应1dcq_order_goods.order_goods_id
	
	private Integer techID;//技师id-对应1dcq_shop_technician.technician_id

	public Integer getOrderTechId() {
		return orderTechId;
	}

	public void setOrderTechId(Integer orderTechId) {
		this.orderTechId = orderTechId;
	}

	public Integer getOrderGoodsId() {
		return orderGoodsId;
	}

	public void setOrderGoodsId(Integer orderGoodsId) {
		this.orderGoodsId = orderGoodsId;
	}

	public Integer getTechID() {
		return techID;
	}

	public void setTechID(Integer techID) {
		this.techID = techID;
	}
	
	
}
