package com.idcq.appserver.dao.order;

import java.util.List;

import com.idcq.appserver.dto.order.OrderGoodsServiceTech;
import com.idcq.appserver.dto.shop.ShopTechnicianDto;

public interface IOrderGoodsServiceTechDao {

	/**
	 * @desc 批量添加 商铺技师
	 * @param goodsTechList
	 * @throws Exception
	 */
	public void batchAddOrderGoodsServiceTech(List<OrderGoodsServiceTech> goodsTechList) throws Exception;
	
}
