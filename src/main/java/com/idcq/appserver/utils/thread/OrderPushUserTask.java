package com.idcq.appserver.utils.thread;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderShopRsrcService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;

/**
 * 预约订单成功推送用户任务
 * 
 * @author Administrator
 * 
 * @date 2015年5月20日
 * @time 上午11:15:07
 */
public class OrderPushUserTask implements Runnable{
	
	private static final Log logger = LogFactory.getLog(OrderPushUserTask.class);
	
	private PushDto push;
	
	private OrderDto order;
	
	
	public OrderPushUserTask(PushDto push) {
		super();
		this.push = push;
	}
	
	public OrderPushUserTask(OrderDto order) {
		super();
		this.order = order;
	}


//	public void run() {
//		logger.info("异步线程给用户订单预约信息-run start");
//		try {
//			IPushService pushService = BeanFactory.getBean(IPushService.class);
//			pushService.pushInfoToUser2(this.push,CommonConst.USER_TYPE_ZREO);
//		} catch (Exception e) {
//			e.printStackTrace();
//			logger.error("异步线程给用户订单预约信息-系统异常",e);
//		}
//	}
	
	public void run() {
		logger.info("异步线程给用户订单预约信息-run start");
		try {
			Integer orderSenceType = order.getOrderSceneType();
			String orderId = this.order.getOrderId();
//			Integer orderType = this.order.getOrderType();
			// 订单来源
			Integer orderSource = this.order.getOrderSource();
			String reserveDateStr = null;
			Date reserveDate = null;
			if(orderSenceType == CommonConst.PLACE_ORDER_LIVE ||
					orderSenceType == CommonConst.PLACE_ORDER_BEAUTY ||
					orderSenceType == CommonConst.PLACE_ORDER_VENUE){
				//获取预定日期时间
				if(orderSource == null || orderSource == CommonConst.ORDER_SOURCE_FZZXD){//到店点菜，订单来源为非自助下单，则为餐饮预定到店
					IOrderShopRsrcService osrService = BeanFactory.getBean(IOrderShopRsrcService.class);
					OrderShopRsrcDto osr = osrService.getOrderShopRsrcLimitOne(orderId);
					if(osr != null){
						reserveDate = osr.getReserveResourceDate();
						String startTime = osr.getStartTime();
						startTime = !StringUtils.isBlank(startTime) ?  startTime.substring(0,startTime.lastIndexOf(":")) : null;
						if(reserveDate != null && !StringUtils.isBlank(startTime)){
							reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATE_FORMAT)+" "+startTime;
						}
					}
				}else{//到店点菜，自助下单
					reserveDate = this.order.getOrderTime();
					reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATETIME_FORMAT1);
				}
			}else if(orderSenceType == CommonConst.PLACE_ORDER_GOODS 
					|| orderSenceType == CommonConst.PLACE_ORDER_WM){//外卖、商品
				//获取配送时间
				reserveDate = this.order.getDistributionTime();
				if(reserveDate != null){
					reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATETIME_FORMAT1);
				}else{
					reserveDate = this.order.getServiceTimeFrom();
					reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATETIME_FORMAT1);
				}
			}else if(orderSenceType == CommonConst.PLACE_ORDER_SERVICE || orderSenceType ==  CommonConst.PLACE_ORDER_QIXIU){//服务、汽修
				//获取服务开始时间
				//获取配送时间
				reserveDate = this.order.getServiceTimeFrom();
				reserveDateStr = DateUtils.format(reserveDate, DateUtils.DATETIME_FORMAT1);
			}
			String shopName = this.order.getShopName();
//			Date reserveTime = 
			StringBuilder content = new StringBuilder();
			content.append("亲爱的用户，恭喜您预定成功，");
			content.append("预订单编号：" + orderId + "，");
			content.append("店铺：" + shopName + "，");
			content.append("预定时间：" + reserveDateStr + "。");
			content.append("祝您工作顺心，生活愉快！");
			PushDto push = new PushDto();
			push.setAction("reserveOrder");
			push.setContent(content.toString());
			push.setUserId(order.getUserId());
			IPushService pushService = BeanFactory.getBean(IPushService.class);
			pushService.pushInfoToUser2(push,CommonConst.USER_TYPE_ZREO);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异步线程给用户订单预约信息-系统异常",e);
		}
	}

	
}
