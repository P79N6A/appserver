package com.idcq.appserver.index.quartz.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.service.shop.IShopTechnicianService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
/**
 * 定时将技师的工作状态置为空闲
 * <p>
 * 	注：每天凌晨00点执行，将当天排班为休息的技师的工作状态置为休息
 * </p>
 * @author nie_jq
 * @datetime 2015-08-26 14:16
 *
 */
public class UpdateTechnicianWorkStatusJob  extends QuartzJobBean{
	private Log logger = LogFactory.getLog(UpdateTechnicianWorkStatusJob.class);
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		new UpdateWorkStatus1().start();
		new UpdateWorkStatus2().start();
	}
	
	/**
	 * 将当天排班为休息的技师的工作状态置为休息
	 * @author nie_jq
	 *
	 */
	class UpdateWorkStatus1 extends Thread{
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			logger.info("开始执行-[修改技师工作状态]-定时任务-start");
			logger.info("将当天排班为休息的技师的工作状态置为休息");
			int start = 1;
			int pSize = 30;
			int classesType = CommonConst.CLASSES_TYPE_XX;
			int operateType = 0;
			String classesDate = null;
			try {
				classesDate = DateUtils.getDate(new Date());
			} catch (Exception e) {
				logger.error("获取当前时间yyyy-MM-dd格式异常了",e);
			}
			IShopTechnicianService technicianService = BeanFactory.getBean(IShopTechnicianService.class);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("pSize", pSize);
			param.put("classesType", classesType);
			param.put("classesDate", classesDate);
			param.put("operateType", operateType);
			while(true){
				if (technicianService == null) {
					technicianService = BeanFactory.getBean(IShopTechnicianService.class);
				}
				param.put("startNo", (start-1)*pSize);
				try {
					List<Long> techIds = technicianService.getTechIdList(param);
					if (null == techIds || techIds.size() <= 0) {
						break;
					}
					technicianService.updateTechnicianWorkStatus(techIds, CommonConst.TECH_STATUS_XXZ);
				} catch (Exception e) {
					logger.error("定时将技师的工作状态置为空闲状态系统异常了",e);
					break;
				}
				start++;
			}
			logger.info("[修改技师工作状态]-定时任务-end，共耗时："+(System.currentTimeMillis() - startTime));
		}
		
	}
	
	/**
	 * 将昨天为休息，但今天正常上班的技师的工作状态置为空闲
	 * @author nie_jq
	 *
	 */
	class UpdateWorkStatus2 extends Thread{

		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			logger.info("开始执行-[修改技师工作状态]-定时任务-start");
			logger.info("将昨天为休息，但今天正常上班的技师的工作状态置为空闲");
			int start = 1;
			int pSize = 30;
			int classesType = CommonConst.CLASSES_TYPE_XX;
			int operateType = 1;
			String classesDate = null;
			try {
				String nowDate = DateUtils.getDate(new Date());
				classesDate = DateUtils.getDateAfterDay(nowDate,-1,"yyyy-MM-dd");
			} catch (Exception e) {
				logger.error("获取当前时间yyyy-MM-dd格式异常了",e);
			}
			IShopTechnicianService technicianService = BeanFactory.getBean(IShopTechnicianService.class);
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("pSize", pSize);
			param.put("classesType", classesType);
			param.put("classesDate", classesDate);
			param.put("operateType", operateType);
			while(true){
				if (technicianService == null) {
					technicianService = BeanFactory.getBean(IShopTechnicianService.class);
				}
				param.put("startNo", (start-1)*pSize);
				try {
					List<Long> techIds = technicianService.getTechIdList(param);
					if (null == techIds || techIds.size() <= 0) {
						break;
					}
					technicianService.updateTechnicianWorkStatus(techIds, CommonConst.TECH_STATUS_KXZ);
				} catch (Exception e) {
					logger.error("定时将技师的工作状态置为空闲状态系统异常了",e);
					break;
				}
				start++;
			}
			logger.info("[修改技师工作状态]-定时任务-end，共耗时："+(System.currentTimeMillis() - startTime));
		}
		
	}

}
