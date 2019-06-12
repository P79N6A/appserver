package com.idcq.appserver.utils.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.service.order.IOrderShopRsrcService;
import com.idcq.appserver.utils.AsyncExecutorUtil;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 激活订单商铺资源异步任务
 * 
 * @author Administrator
 * 
 * @date 2015年5月20日
 * @time 上午11:09:45
 */
public class EnableShopResourceTask implements Runnable{


	private static final Log logger = LogFactory.getLog(AsyncExecutorUtil.class);
	
	private String orderId;
	
	
	public EnableShopResourceTask(String orderId) {
		super();
		this.orderId = orderId;
	}


	public void run() {
		logger.info("异步线程激活订单商铺资源-run start");
		try {
			IOrderShopRsrcService orderShopRsrcService = BeanFactory.getBean(IOrderShopRsrcService.class);
			orderShopRsrcService.enableOrderShopRsrc(this.orderId);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异步线程激活订单商铺资源-系统异常",e);
		}
	}
	
}
