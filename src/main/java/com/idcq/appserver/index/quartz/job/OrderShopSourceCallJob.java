package com.idcq.appserver.index.quartz.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.SysConfigureDto;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
/**
 * 订单预定资源过期提醒
 * @author nie_jq
 * @time 2015-05-21
 *
 */
public class OrderShopSourceCallJob implements Job {
	private static final Logger logger = Logger.getLogger(OrderShopSourceCallJob.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("订单预定资源过期提醒-start");
		IOrderShopRsrcDao orderShopRsrcDao = BeanFactory.getBean(IOrderShopRsrcDao.class);
		ICommonService commonService = BeanFactory.getBean(ICommonService.class);
		int start = 1;
		int pSize = 30;
		String action="reserveOrderDue";
		if (null != orderShopRsrcDao && commonService != null) {
			int callTime = CommonConst.ORDER_RSRC_CALL_TIME;
		/*	try {
				SysConfigureDto sysDto = commonService.getSysConfigureDtoByKey(CommonConst.ORDER_RSRC_CALL_TIME_KEY);
				if (null != sysDto && !StringUtils.isBlank(sysDto.getConfigureValue())) {
					callTime = Integer.parseInt(sysDto.getConfigureValue());
				}
			} catch (Exception e1) {
				callTime = CommonConst.ORDER_RSRC_CALL_TIME;
				logger.error("获取系统配置过期提前提醒时间系统异常啦，赋予默认提前时间："+callTime+"小时",e1);
			}*/
			try {
			    ConfigDto searchCondition = new ConfigDto();
			    searchCondition.setBizId(0l);
			    searchCondition.setBizType(0);
			    searchCondition.setConfigKey(CommonConst.ORDER_RSRC_CALL_TIME_KEY);
			    ConfigDto config = commonService.getConfigDto(searchCondition);
//			    SysConfigureDto sysDto = commonService.getSysConfigureDtoByKey(CommonConst.ORDER_RSRC_CALL_TIME_KEY);
			    if (null != config && !StringUtils.isBlank(config.getConfigValue())) {
			        callTime = Integer.parseInt(config.getConfigValue());
			    }
			} catch (Exception e1) {
			    callTime = CommonConst.ORDER_RSRC_CALL_TIME;
			    logger.error("获取系统配置过期提前提醒时间系统异常啦，赋予默认提前时间："+callTime+"小时",e1);
			}
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("pSize", pSize);
			while(true){
				try {
					String date = DateUtils.format(new Date(), DateUtils.DATE_FORMAT);
					String time = DateUtils.format(new Date(), DateUtils.TIME_FORMAT);
					date = calcDate(date, time);
					time = DateUtils.getDateAfterHour(time, callTime);
					param.put("startNo", (start-1)*pSize);
					param.put("date", date);
					param.put("time", time);
					param.put("orderStatus", CommonConst.ORDER_STS_YYD);
					List<Map<String,Object>> list = orderShopRsrcDao.queryOrderShopResource(param);
					boolean flag = (list == null || list.size() <= 0);
					if (flag) {
						break;
					}
					for(Map<String,Object> map : list){
						if(map == null || map.size() <= 0)continue;
						String orderId = null;
						String shopName = null;
						Long userId = null;
						String reserveTime = null;
						try {
							orderId = CommonValidUtil.isEmpty(map.get("order_id"))?"":(map.get("order_id")+"");
							shopName = CommonValidUtil.isEmpty(map.get("shop_name"))?"":(map.get("shop_name")+"");
							String startTime = CommonValidUtil.isEmpty(map.get("start_time"))?"00:00:00":(map.get("start_time")+"");
							String reserveDateTime = CommonValidUtil.isEmpty(map.get("reserve_resource_date"))?"0000-00-00":(map.get("reserve_resource_date")+"");
							reserveTime = DateUtils.convertFmtDateTimeStr(reserveDateTime+" "+startTime, DateUtils.DATETIME_FORMAT, DateUtils.DATETIME_FORMAT1);
							userId = CommonValidUtil.isEmpty(map.get("user_id"))?null:Long.parseLong((map.get("user_id")+""));
						} catch (Exception e) {
							logger.error("订单预定资源过期提醒，["+userId+"]，解析数据异常",e);
							continue;
						}
						try {
							/*
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("action", action);
							jsonObject.put("orderId", orderId);
							jsonObject.put("shopName", shopName);
							jsonObject.put("reserveTime", reserveTime);
							*/
							//亲爱的用户，您的预订单，编号：xxxxxxxxxxxx，店铺：四海一家，将于2015-5-20 16:17到期，请提前做好安排。祝您工作顺心，生活愉快！
							StringBuilder sb = new StringBuilder("亲爱的用户，您的预订单，");
							sb.append("编号："+orderId+"，");
							sb.append("店铺："+shopName+"，");
							sb.append("将于"+reserveTime+"到期，");
							sb.append("请提前做好安排。祝您工作顺心，生活愉快！");
							commonService.pushUserMsg(action,sb.toString(), userId, 0);
						} catch (Exception e) {
							logger.error("订单预定资源过期提醒，["+userId+"]，推行信息异常",e);
						}
					}
					//批量将数据修改为已经提醒
					if (!flag) {
						try {
							orderShopRsrcDao.batchUpdateOrderShopResource(list);
						} catch (Exception e) {
							logger.error("订单预定资源过期提醒，批量修改提醒状态异常",e);
						}
					}
					start++;
				} catch (Exception e) {
					break;
				}
			}
		}
	}
	
	private String calcDate(String date,String time){
		int num = Integer.valueOf(time.substring(0, time.indexOf(":")));
		int r = num + CommonConst.ORDER_RSRC_CALL_TIME;
		if (r >= 24) {
			date = DateUtils.getDateAfterDay(date, 1,"yyyy-MM-dd");
		}
		return date;
	}
	
	public static void main(String[] args) {
		OrderShopSourceCallJob job = new OrderShopSourceCallJob();
		String date = DateUtils.format(new Date(), DateUtils.DATE_FORMAT);
		String time = DateUtils.format(new Date(), DateUtils.TIME_FORMAT);
		time = "22:18:12";
		System.out.println(date);
		System.out.println(job.calcDate(date, time));
	}
}
