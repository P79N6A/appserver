package com.idcq.appserver.service.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dto.order.OrderLogDto;

/**
 * 订单日志service
 * 
 * @author Administrator
 * 
 * @date 2015年5月20日
 * @time 上午9:30:03
 */
@Service
public class OrderLogServiceImpl implements IOrderLogService{

	@Autowired
	public IOrderLogDao orderLogDao;
	
	public int saveOrderLog(OrderLogDto orderLog) throws Exception {
		return this.orderLogDao.saveOrderLog(orderLog);
	}
	
	
}
