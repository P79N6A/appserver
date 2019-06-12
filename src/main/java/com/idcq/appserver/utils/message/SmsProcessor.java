package com.idcq.appserver.utils.message;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.MsgCenterNotifyTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.redpacket.RedPacketDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IMessageCenterService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
/**
 * 短信方式推送-sms
 * @author nie_jq
 *
 */
@Service("smsProcessor")
public class SmsProcessor implements IProcessor{
	private Log logger = LogFactory.getLog(SmsProcessor.class);
	public Object exective(Map<String, Object> params) throws Exception {
		MessageCenterDto messageCenterDto = (MessageCenterDto) params.get("msgDataKey");
		String usage = (String) params.get("usage");
		ISendSmsService sendSmsService = BeanFactory.getBean(ISendSmsService.class);
		SmsReplaceContent replaceContent = new SmsReplaceContent();
		replaceContent.setUsage(usage);
		replaceContent.setSmsType(CommonConst.SMS_TYPE_SEO);
		doSendContent(messageCenterDto, params, replaceContent);
		logger.info("消息信息："+messageCenterDto);
		logger.info("发送短信内容信息："+replaceContent);
		IMessageCenterService messageCenterService = BeanFactory.getBean(IMessageCenterService.class);
		messageCenterService.saveMessageCenter(messageCenterDto);
		sendSmsService.sendSmsMobileCode(replaceContent);
		return null;
	}
	
	private void doSendContent(MessageCenterDto messageCenterDto,Map<String, Object> params,SmsReplaceContent replaceContent) throws Exception{
		Object obj = params.get("sendDataKey");
		Integer operateType = (Integer) params.get("operateType");
		Integer notifyType = messageCenterDto.getNotifyType();
		Long receiverId = 0L;
		Integer clientSystemType = 0;//接收消息终端:1=收银机,2=收银PAD,3=消费者APP,4=一点管家,5=路由器,6=微信商城,7=公众号,8=商铺后台
		String mobile = "";
		
		if (notifyType == MsgCenterNotifyTypeEnum.NOTITY_USER.getValue()) //通知商铺会员
		{
			/**
			 * 场景一：商圈活动报名截止，通知商铺会员 operateType=5
			 */
			//模板：shopName店铺活动
			ShopDto startShopDto = (ShopDto) params.get("startShopDto");//发起活动的商铺
			BusinessAreaActivityDto activityDto = (BusinessAreaActivityDto) params.get("activity");//商圈活动
			replaceContent.setShopName(startShopDto.getShopName());
			replaceContent.setActivityName(activityDto.getBusinessAreaName());
			ShopMemberDto shopMemberDto = (ShopMemberDto) obj;
			mobile = shopMemberDto.getMobile() == null ? null : shopMemberDto.getMobile()+"";
		}
		else if(notifyType == MsgCenterNotifyTypeEnum.NOTITY_SHOP.getValue())//通知商铺
		{
			/**
			 * 1.场景一：活动发布成功，通知可参与商铺 operateType=3
			 * 2.场景二：参与方报名参与活动，通知发起商铺 operateType=1
			 * 3.场景三：参与方取消活动，通知发起商铺 operateType=2
			 */

			ShopDto shopDto = (ShopDto) obj;//参与方
			ShopDto startShopDto = (ShopDto) params.get("startShopDto");//发起活动的商铺
			BusinessAreaActivityDto activityDto = (BusinessAreaActivityDto) params.get("activity");//商圈活动
			if (operateType == 3) {
				//模板：邀请参加shopName店铺发起的activityName活动
				replaceContent.setShopName(startShopDto.getShopName());
				replaceContent.setActivityName(activityDto.getBusinessAreaName());
			}else{
				//模板：shopName店铺参与活动/shopName店铺取消参与
				replaceContent.setShopName(shopDto.getShopName());
			}
			receiverId = shopDto.getPrincipalId();
			if (receiverId != null) {
				IMemberServcie memberService = BeanFactory.getBean(IMemberServcie.class);
				UserDto userDto = memberService.getUserByUserId(receiverId);
				if (null != userDto) {
					mobile = userDto.getMobile();
				}
			}
		}
		else if(notifyType == MsgCenterNotifyTypeEnum.NOTITY_PLATFORM_USER.getValue())//通知平台会员
		{
			/**
			 * 1.场景一：会员红包过期，通知平台会员 operateType=4
			 */
			//模板：amount元红包快过期
			RedPacketDto redPacketDto = (RedPacketDto) params.get("redPacket");
			//红包金额
			replaceContent.setAmount(redPacketDto.getAmount());
			UserDto userDto = (UserDto) obj;
			mobile = userDto.getMobile();
		}
		if (StringUtils.isEmpty(mobile)) {
			logger.warn("--------------------手机号码不存在，不能发送短信");
			return;
		}
		messageCenterDto.setClientSystemType(clientSystemType);//短信
		replaceContent.setMobile(mobile);
	}
		

}
