package com.idcq.appserver.index.quartz.job;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.OrderStatusEnum;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;

/**
 * 场地类已开单订单自动完成定时器
 * 逻辑：
 * 1.查询已开单的且时间已过了预定时间的订单
 * 2.更新订单状态为已完成(3)
 * 
 * @author 黄睿
 *
 */
public class OrderAutoFinishJob extends QuartzJobBean{
	
	private static final Logger logger = Logger.getLogger(OrderAutoFinishJob.class);
	
	private IOrderServcie orderServcie;

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			if(orderServcie == null)
			{
				orderServcie = BeanFactory.getBean(IOrderServcie.class);
			}
			logger.info("=================================场地类已开单订单自动完成定时器处理开始...");
			// 查询已开单的且时间已过了预定时间的订单
			// 每次处理10000条
			List<String> orderIds = null;
			while (true) {
				orderIds = orderServcie.getOrderIdsByCondition();
				if(CollectionUtils.isEmpty(orderIds)){
					break;
				}
				for(String id: orderIds){
					//更新订单状态为已完成(3)
					orderServcie.updateOrderStatus(id, OrderStatusEnum.SETTLE.getValue());
					
					//结账
					OrderGoodsSettleUtil.detailOrderGoodsSettle(id, CommonConst.PAY_TYPE_SINGLE);
					//写日志
					orderServcie.saveOrderLog(id,"定时器自动结账操作",0L);
				}
				
			}
			
			
			logger.info("=================================场地类已开单定时器自动完成定时器处理完毕");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		
	}

}
