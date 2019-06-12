package com.idcq.idianmgr.quartz;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 订单状态为待确认，商家超过10分钟未处理，订单状态变为已退订
 * 
 * @author shengzhipeng
 * @date:2015年8月1日 上午11:39:02
 */
public class HandleOrderStatusJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());
	
	private static final String REMARK = "定时处理商家未接订单";
	
	//表示系统操作
	private static final Long USER_ID = 0L;
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		logger.info("处理商家多少分钟后未确认的订单-start");
		try {
			ICollectService collectService = BeanFactory.getBean(ICollectService.class);
			ICommonService commonService = BeanFactory.getBean(ICommonService.class);
			IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
			List<OrderDto> orderList = collectService.timeProcessingOrder(USER_ID, REMARK);
			if (CollectionUtils.isNotEmpty(orderList)) {
				String action = CommonConst.ACTION_ORDER_DATA_UPDATE; 
				Integer userType = CommonConst.USER_TYPE_ZREO;
				for (OrderDto orderDto : orderList) {
					ShopDto shopDto = shopDao.getNormalShopById(orderDto.getShopId());
					if (null != shopDto) {
						StringBuilder content = new StringBuilder();
						content.append("预约失败！可能由于业务繁忙，");
						content.append(shopDto.getShopName());
						content.append("暂时无法为您提供服务。您支付的款项将退回到您的账户余额中。");
						commonService.pushUserMsg(action, content.toString(), orderDto.getUserId(), userType);
					}
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("处理商家多少分钟后未确认的订单-异常:" + e);
		}
	}


}
