package com.idcq.appserver.index.quartz.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;
import com.idcq.appserver.utils.BeanFactory;


public class MemberBirthDaySendSmsStatusChangeJob extends QuartzJobBean{

	private Log logger = LogFactory.getLog(this.getClass());
	
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		try {
			logger.info("更新店内会员生日是否发送短信状态-start");
			ShopMemberDto shopMemberDto = new ShopMemberDto();
			shopMemberDto.setMemberStatus(CommonConst.MEMBER_STATUS_DELETE);
			shopMemberDto.setIsSendBirthdaySms(CommonConst.IS_SEND_BIRTHDAY_SMS_IS_FALSE);
			IShopMemberService shopMemberService = (IShopMemberService)BeanFactory.getBean(IShopMemberService.class);
			int n = shopMemberService.updateShopMemberExceptDelAndCurrentMonth(shopMemberDto);
			logger.info("更新店内会员生日是否发送短信状态-end");
		} catch (Exception e) {
			logger.info("更新店内会员生日是否发送短信状态-异常");
			e.printStackTrace();
		}
		
	}
	
	
}
