package com.idcq.appserver.utils.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.service.order.IOrderLogService;
import com.idcq.appserver.utils.AsyncExecutorUtil;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 保存订单日志任务
 * 
 * @author Administrator
 * 
 * @date 2015年5月20日
 * @time 上午11:15:07
 */
public class OrderLogTask implements Runnable{
	
	private static final Log logger = LogFactory.getLog(AsyncExecutorUtil.class);
	
	private OrderLogDto orderLog;
	
	
	public OrderLogTask(OrderLogDto orderLogDto) {
		super();
		this.orderLog = orderLogDto;
	}


	public void run() {
		logger.info("异步线程保存订单日志-run start");
		try {
			IOrderLogService orderLogService = BeanFactory.getBean(IOrderLogService.class);
			orderLogService.saveOrderLog(this.orderLog);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异步线程保存订单日志-系统异常",e);
		}
	}

	
}
