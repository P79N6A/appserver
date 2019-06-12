package com.idcq.appserver.dao.orderSettle;

import java.util.Map;

import com.idcq.appserver.dto.order.OrderSettleDto;


public interface IOrderSettleDao {
	
	/**
	 * 新增订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	void saveOrderSettle(OrderSettleDto orderSettle) throws Exception;
	
	/**
	 * 更新订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	int updateOrderSettle(Map map) throws Exception;

}
