package com.idcq.appserver.utils.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.MsgCenterMsgStatusEnum;
import com.idcq.appserver.common.enums.MsgCenterNotifyModelEnum;
import com.idcq.appserver.common.enums.MsgCenterNotifyTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.ProgramUtils;

/**
 * 向商铺会员发送商圈活动消息
 * @author nie_jq
 *
 */
@Service("pushBusinessAreaUserProcessor")
public class PushBusinessAreaUserProcessor implements IProcessor{

	private Log logger = LogFactory.getLog(PushBusinessAreaUserProcessor.class);
	
	public Object exective(Map<String, Object> params) throws Exception {
		logger.info("----------会员红包过期，通知会员，参数："+params);
		ShopMemberDto shopMemberDto = (ShopMemberDto) params.get("userData");
		Long businessAreaActivityId = (Long) params.get("businessAreaActivityId");
		IBusAreaActivityService activityService = BeanFactory.getBean(IBusAreaActivityService.class);
		IShopServcie shopService = BeanFactory.getBean(IShopServcie.class);
		
		BusinessAreaActivityDto activityDto = activityService.getBusinessAreaActivityById(businessAreaActivityId);
		ShopDto shopDto = shopService.getShopById(activityDto.getShopId());
		MessageCenterDto messageCenterDto = new MessageCenterDto();
		messageCenterDto.setBizId(activityDto.getBusinessAreaActivityId());
		messageCenterDto.setMessageTitle(activityDto.getActivityShareTitle());
		messageCenterDto.setShowContent(activityDto.getBusinessAreaName());
		messageCenterDto.setMessageImage(activityDto.getActPosterUrls());
		messageCenterDto.setNotifyModel(MsgCenterNotifyModelEnum.END2END.getValue());//点对点
		messageCenterDto.setMessageStatus(MsgCenterMsgStatusEnum.SEND_OK.getValue());//发送OK
		messageCenterDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());//商圈活动
		messageCenterDto.setNotifyType(MsgCenterNotifyTypeEnum.NOTITY_USER.getValue());//通知商铺会员
		messageCenterDto.setReceiverId(shopMemberDto.getUserId() == null?shopMemberDto.getMemberId():shopMemberDto.getUserId());
		Map<String, Object> sendParams = new HashMap<String, Object>();
		sendParams.put("sendDataKey", shopMemberDto);//商铺会员
		sendParams.put("msgDataKey", messageCenterDto);
		sendParams.put("startShopDto", shopDto);//发起活动的商铺
		sendParams.put("activity", activityDto);//商圈活动
		sendParams.put("operateType", 5);//发布活动成功，通知商家参与
		sendParams.put("usage",CommonConst.TRADING_AREA_ACTIVITIES_USER + messageCenterDto.getBizId());//短信方式必填
		
		logger.info("发送消息，请求参数："+sendParams);
		// TODO ...插入点 发送消息
		ProgramUtils.executeBeanByExecutePointCode("pushMessageSmsPoint", 1, sendParams);
		return null;
	}

}
