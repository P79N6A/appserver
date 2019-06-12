package com.idcq.appserver.index.quartz.job;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.PropertyUtil;


/**
 * 定时统计商品销售数量
 * 
 * @author Administrator
 * 
 * @date 2015年5月16日
 * @time 下午4:32:28
 */
public class TimeStatisGoodsSoldJob implements Job{
	private static final Logger logger = Logger.getLogger(TimeStatisGoodsSoldJob.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("定时统计商品销售情况-start");
		try {
//			Properties prop = ContextInitListener.CACHE_PROPS;
//			String time = prop.getProperty("shop_sold_lastupdate_time") ;
			String appProp = "properties/common.properties";
			//商品索引文档路径
			String time = PropertyUtil.readProperty(appProp, "goods_sold_lastupdate_time");
			Date startTime = null;
			Date endTime = null;
			try {
				if(!StringUtils.isBlank(time)){
					startTime = DateUtils.parse(time, DateUtils.DATETIME_FORMAT);
				}else{
					startTime = DateUtils.getZeroTimeOfCurDate();
				}
			} catch (Exception e) {
				startTime = DateUtils.getZeroTimeOfCurDate();
			}
			endTime = DateUtils.getEndTimeOfCurDate();
			IGoodsDao goodsDao = BeanFactory.getBean(IGoodsDao.class);
			goodsDao.statisGoodsSoldExecute(startTime, endTime);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("goods_sold_lastupdate_time", DateUtils.format(endTime, DateUtils.DATETIME_FORMAT));
			PropertyUtil.writeProperty(appProp,map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时统计商品销售情况-系统异常",e);
		}
	}
	public static void main(String[] args) throws Exception {
		String startTime = DateUtils.getZeroTimeOfCurDate().toLocaleString();
		System.out.println(startTime);
		
	}
	
	
	
	
}
