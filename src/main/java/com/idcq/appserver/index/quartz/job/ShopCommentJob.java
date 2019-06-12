package com.idcq.appserver.index.quartz.job;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.user.IUserShopCommentDao;
import com.idcq.appserver.dto.user.ShopCommentDto;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;

/**
 * 
 * @ClassName: ShopCommentJob
 * @Description: 定时处理已经结算订单的商铺评论分数
 * @author 陈永鑫
 * @date 2015年5月5日 下午4:12:06
 * 
 */
public class ShopCommentJob extends QuartzJobBean {

	private final Log logger = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg)
			throws JobExecutionException {
		logger.info("处理已经结算订单的商铺评论状态-start");
		try {
			IOrderDao orderDao = BeanFactory
					.getBean(IOrderDao.class);
			IUserShopCommentDao userShopCommentDao = BeanFactory
					.getBean(IUserShopCommentDao.class);
			ICommonDao commonDao = BeanFactory
					.getBean(ICommonDao.class);
			//Properties props = ConfigPropertiesInitListener.TIMECONFIG;
			//String autoCommentTimeStr = props.getProperty("autoCommentTime");
			Map<String, String> mapSetting= new HashMap<String, String>();
			mapSetting.put("settingCode", CommonConst.SETTING_CODE_MOBILE_CODE);
			mapSetting.put("settingKey", CommonConst.SETTING_KEY_PRAISE_CODE);
			//获取配置表时间配置
			String autoCommentTimeStr = commonDao.getSettingValue(mapSetting);
			if(StringUtils.isNotBlank(autoCommentTimeStr)){
				autoCommentTimeStr = (String) CommonValidUtil.convertJsonStr(autoCommentTimeStr, CommonConst.SETTING_CODE_PRAISE_KEY);
			}
			else{
				autoCommentTimeStr = "5";
			}
			Integer autoDays = Integer.parseInt(autoCommentTimeStr);
			//查询已经结账的订单
			List<Map<String, Object>> listOrder = orderDao.getOrderByStatus(3);
			if(null!=listOrder&&0!=listOrder.size()){
				for (Map<String, Object> map : listOrder) {
					Date orderTime = (Date) map.get("lastUpdateTime");
					Integer days = daysBetween(orderTime);
					//如果订单结账未评论的时间超过配置的时间天数，自动给该商铺好评
					if(days>=autoDays){
						//TODO 时间比较
						Long shopId = (Long) map.get("shopId");
						Long userId = (Long) map.get("userId");
						String orderId = (String) map.get("orderId");
						ShopCommentDto userShopCommentDto = new ShopCommentDto();
						userShopCommentDto.setUserId(userId);
						userShopCommentDto.setShopId(shopId);
						userShopCommentDto.setOrderId(orderId);
						//默认五星评分
						userShopCommentDto.setServiceGrade(5.0);
						userShopCommentDto.setEnvGrade(5.0);
						userShopCommentDto.setCommentContent("");
						userShopCommentDao.makeCommentShop(userShopCommentDto);
						//更改订单状态
						orderDao.updateOrderStatusById(orderId);
					}
				}
			}
		} catch (Exception e) {
			logger.info("处理已经结算订单的商铺评论状态-异常");
			e.printStackTrace();
		}

	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param date
	 * @return 返回订单结束时间到现在距离的天数
	 */
	public int daysBetween(Date date) {
		if(null==date){
			return 0;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long time1 = cal.getTimeInMillis();
		cal.setTime(new Date());
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}
	
}
