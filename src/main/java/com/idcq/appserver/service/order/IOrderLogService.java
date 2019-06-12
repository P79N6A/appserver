package com.idcq.appserver.service.order;

import com.idcq.appserver.dto.order.OrderLogDto;

public interface IOrderLogService {
	
	/**
	 * 保存订单日志
	 * 
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	int saveOrderLog(OrderLogDto orderLog) throws Exception;
}
