package com.idcq.appserver.index.quartz.job;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopTechnicianDao;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;


/**
 * 定时统计商铺和商品销售数量
 * 
 * @author Administrator
 * 
 * @date 2015年5月16日
 * @time 下午4:32:28
 */
public class TimeStatisShopAndGoodsSoldJob implements Job{
	private static final Logger logger = Logger.getLogger(TimeStatisShopAndGoodsSoldJob.class);
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("定时统计商铺和商品销售情况-start");
		try {
			// 从app配置表读取上次统计结束时间
			ICommonDao commonDao = BeanFactory.getBean(ICommonDao.class);
			String lastUpdateTime = commonDao.getConfigValueByKey(CommonConst.APP_CFG_KEY_GOODS_SOLD_LAST_UPDATE_TIME);
			
			Date startTime = null;
			Date endTime = null;
			boolean isFirstTime = true;
			try {
				if(!StringUtils.isBlank(lastUpdateTime)){
					// 以后每次执行获取上次结束时间作为当次开始时间
					startTime = DateUtils.parse(lastUpdateTime, DateUtils.DATETIME_FORMAT);
					isFirstTime = false;
				}else{
					// 首次执行获取昨天的零点作为开始时间
					startTime = DateUtils.getZeroTimeOfYesterDay();
				}
			} catch (Exception e) {
				// 获取昨天的零点
				startTime = DateUtils.getZeroTimeOfYesterDay();
			}
			
//			endTime = DateUtils.getEndTimeOfCurDate();
			// 以当前系统时间作为结束时间
			endTime = new Date();
			
			// 统计商品销售数量
			IGoodsDao goodsDao = BeanFactory.getBean(IGoodsDao.class);
			goodsDao.statisGoodsSoldExecute(startTime, endTime);
			
			// 统计商铺订单数
			IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
			shopDao.statisShopSoldExecute(startTime, endTime);
			
			// 统计技师接单数
			IShopTechnicianDao shopTechnicianDao = BeanFactory.getBean(IShopTechnicianDao.class);
			shopTechnicianDao.statisTechnicianOrderNumExecute(startTime, endTime);
			
			// 记录结束时间到配置表
			if(isFirstTime){
				// 第一次执行需要新增app配置记录
				commonDao.addConfigValueByKey(DateUtils.format(endTime, DateUtils.DATETIME_FORMAT), 
					CommonConst.APP_CFG_KEY_GOODS_SOLD_LAST_UPDATE_TIME);
			}else{
				// 以后执行需要更新app配置记录
				commonDao.updateConfigValueByKey(DateUtils.format(endTime, DateUtils.DATETIME_FORMAT), 
						CommonConst.APP_CFG_KEY_GOODS_SOLD_LAST_UPDATE_TIME);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时统计商铺和商品销售情况-系统异常",e);
		}
	}
	
	/*
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("定时统计商铺和商品销售情况-start");
		try {
//			Properties prop = ContextInitListener.CACHE_PROPS;
//			String time = prop.getProperty("shop_sold_lastupdate_time") ;
			String appProp = "properties/common.properties";
			//商品索引文档路径
			String time = PropertyUtil.readProperty(appProp, "sold_lastupdate_time");
			Date startTime = null;
			Date endTime = null;
			try {
				if(!StringUtils.isBlank(time)){
					// 以后每次执行获取上次结束时间作为当次开始时间
					startTime = DateUtils.parse(time, DateUtils.DATETIME_FORMAT);
				}else{
					// 首次执行获取昨天的零点作为开始时间
					startTime = DateUtils.getZeroTimeOfYesterDay();
				}
			} catch (Exception e) {
				// 获取昨天的零点
				startTime = DateUtils.getZeroTimeOfYesterDay();
			}
			
//			endTime = DateUtils.getEndTimeOfCurDate();
			// 以当前系统时间作为结束时间
			endTime = new Date();
			
			// 统计商品销售数量
			IGoodsDao goodsDao = BeanFactory.getBean(IGoodsDao.class);
			goodsDao.statisGoodsSoldExecute(startTime, endTime);
			
			// 统计商铺订单数
			IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
			shopDao.statisShopSoldExecute(startTime, endTime);
			
			// 统计技师接单数
			IShopTechnicianDao shopTechnicianDao = BeanFactory.getBean(IShopTechnicianDao.class);
			shopTechnicianDao.statisTechnicianOrderNumExecute(startTime, endTime);
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("sold_lastupdate_time", DateUtils.format(endTime, DateUtils.DATETIME_FORMAT));
			PropertyUtil.writeProperty(appProp,map);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("定时统计商铺和商品销售情况-系统异常",e);
		}
	}
	*/
	
}
