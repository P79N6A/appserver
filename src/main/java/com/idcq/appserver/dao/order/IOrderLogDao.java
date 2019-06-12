package com.idcq.appserver.dao.order;

import java.util.List;

import com.idcq.appserver.dto.order.OrderLogDto;

public interface IOrderLogDao {
	/**
	 * 保存订单日志信息
	 * @param orderLogDto
	 * @return
	 * @throws Exception
	 */
	int saveOrderLog(OrderLogDto orderLogDto)throws Exception;
	
	/**
	 * 获取指定订单最近的状态日志
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderLogDto getOrderLastStatus(String orderId)throws Exception;
	
	/**
	 * 批量插入
	 * @param orderLogDtos
	 * @return
	 * @throws Exception
	 */
	int batchSaveOrderLogs(List<OrderLogDto> orderLogDtos) throws Exception;
	
	/**
	 * 根据订单状态查询最新的一条记录
	 * @param orderId
	 * @return
	 */
	OrderLogDto queryEntityByOrderIdDescLimit1(String orderId) throws Exception ;
	
	/**
	 * 查询订单所有变更记录
	 * @param orderId
	 * @return
	 */
	List<OrderLogDto> queryEntitysByOrderId(String orderId) throws Exception ;
}
