package com.idcq.appserver.index.quartz.job;

import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
/**
 * 定时发送短信验证码定时器
 * @author nie_jq
 * @time 2015-07-21
 *
 */
public class SendSmsCodeTimerJob implements Job {
	private static final Logger logger = Logger.getLogger(SendSmsCodeTimerJob.class);

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		// TODO Auto-generated method stub
		Properties properties = ContextInitListener.COMMON_PROPS;
		String smsTimerSwitch = properties.getProperty("sms_timer_switch");
		smsTimerSwitch = (null == smsTimerSwitch?CommonConst.SMS_TIMER_SWITCH_OFF:smsTimerSwitch);
		logger.info("==================>>当前定时发送短信的开关为："+smsTimerSwitch);
		if (CommonConst.SMS_TIMER_SWITCH_ON.equals(smsTimerSwitch)) {
			String datetime = DateUtils.format(new Date(), "yyyyMMddHHmmss");
			try {
				ISendSmsService smsService = BeanFactory.getBean(ISendSmsService.class);
				String channel = smsService.getSmsChannel(true);
				String mobile = "13692101942";
				String content = "当前时间："+datetime+"，通道为："+channel;
				smsService.sendSms(mobile, content);
			} catch (Exception e) {
				logger.error("=============>>>测试短信通道发送短信是否正常异常了",e);
			}
		}
	}
}
