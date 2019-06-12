package com.idcq.appserver.index.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 商圈活动消息推送
 * @ClassName: BusAreActivityMessageInformJob 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年3月14日 上午11:43:22 
 *
 */
public class BusAreActivityMessageInformJob extends QuartzJobBean{

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		/**
		 * 步骤一:找出需要发送消息的商圈活动
		 * 
		 * 复杂处理:根据商圈活动报名截止时间界定
		 */
		
		/**
		 * 步骤二:根据商圈活动找出参与方店铺
		 * 
		 */
		
		/**
		 * 步骤三:找出商圈活动的消息模板
		 */
		
		/**
		 * 步骤四:找出步骤三中的店内会员
		 * 
		 */
		
		
	}

	
	
}
