package com.idcq.appserver.service.order;

import java.util.List;

import com.idcq.appserver.dto.order.OrderShopRsrcDto;


public interface IOrderShopRsrcService {

	/**
	 * 启用已经预定的商铺资源
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int enableOrderShopRsrc(String orderId) throws Exception;
	
	/**
	 * 获取指定预定商铺订单的一条资源信息
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderShopRsrcDto getOrderShopRsrcLimitOne(String orderId) throws Exception;
	
	List<OrderShopRsrcDto> getShopBookOrders(Long shopId,String bookTimeFrom,String bookTimeTo)throws Exception;
	
	List<OrderShopRsrcDto>getNeedAutoFinishOrder(String queryTime)throws Exception;
	
	void releaseShopResource(List<String> orderIdList)throws Exception;
}
