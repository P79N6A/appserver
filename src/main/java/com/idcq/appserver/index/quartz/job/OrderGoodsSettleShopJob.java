package com.idcq.appserver.index.quartz.job;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;

/**
 * 
 * @ClassName: OrderGoodsSettleJob
 * @Description: 定时调用处理分账存储过程
 * @author 陈永鑫
 * @date 2015年4月7日 下午4:12:06
 * 
 */
public class OrderGoodsSettleShopJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("处理当天未结算订单-start");
		try {
			IOrderDao orderDao = BeanFactory
					.getBean(IOrderDao.class);
			Integer limit = 0;
			Integer pageSize = 20;
			while (true) {
				List<String> orderIds = orderDao.getNotSettleOrderIds(null,limit,pageSize);
				if (CollectionUtils.isEmpty(orderIds)) {
					break;
				} else {
					for (String orderId : orderIds) {
						logger.info("定时处理的未结算订单ID:" + orderId);
						OrderGoodsSettleUtil.detailSingleOrder(orderId);
						Thread.sleep(5000);//处理一个订单休眠5秒
					}
				}
				// 每页20条
				limit = limit + 20;
			}
		} catch (Exception e) {
			logger.error("处理当天未结算订单-异常");
			e.printStackTrace();
		}

	}
}
