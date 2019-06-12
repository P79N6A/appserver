package com.idcq.appserver.utils.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.MsgCenterNotifyTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.ProgramUtils;

/**
 * 参与方操作活动通知
 * 1.报名参加活动，通知发起方：Xx店铺参与活动
 * 2.取消参与活动，通知发起方：Xx店铺取消参与
 * @author nie_jq
 *
 */
@Service("joinShopOptActivityProcessor")
public class JoinShopOptActivityProcessor implements IProcessor{
	
	private Log logger = LogFactory.getLog(JoinShopOptActivityProcessor.class);
	
	public Object exective(Map<String, Object> params) throws Exception {
		logger.info("----------参与活动商铺，操作活动（取消、参与），参数："+params);
		//参与方商铺ID
		Long shopId = (Long) params.get("shopId");
		//操作活动
		Long businessAreaActivityId = (Long) params.get("businessAreaActivityId");
		//1-参与方参与活动  2-参与方取消活动
		Integer operateType = (Integer) params.get("operateType");

		IShopServcie shopService = BeanFactory.getBean(IShopServcie.class);
		IBusAreaActivityService activityService = BeanFactory.getBean(IBusAreaActivityService.class);
		BusinessAreaActivityDto activityDto = activityService.getBusinessAreaActivityById(businessAreaActivityId);
		ShopDto shopDto = shopService.getShopById(shopId);
		MessageCenterDto messageCenterDto = new MessageCenterDto();
		messageCenterDto.setBizId(businessAreaActivityId);
		messageCenterDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());
		messageCenterDto.setReceiverId(shopDto.getShopId());
		messageCenterDto.setNotifyType(MsgCenterNotifyTypeEnum.NOTITY_SHOP.getValue());
		Map<String, Object> sendParams = new HashMap<String, Object>();
		if (operateType == 1) {
			logger.info(shopDto.getShopName()+"参与了活动");
			messageCenterDto.setMessageTitle("参与商圈活动");
			messageCenterDto.setShowContent(shopDto.getShopName()+"参与了活动");
			sendParams.put("usage",CommonConst.JOIN_ACTIVITY);
		}else{
			logger.info(shopDto.getShopName()+"取消了活动");
			messageCenterDto.setMessageTitle("取消商圈活动");
			messageCenterDto.setShowContent(shopDto.getShopName()+"取消了活动");
			sendParams.put("usage",CommonConst.CANCLE_ACTIVITY);
		}
		ShopDto startShopDto = shopService.getShopById(activityDto.getShopId());
		sendParams.put("sendDataKey", shopDto);//参与方
		sendParams.put("msgDataKey", messageCenterDto);
		sendParams.put("startShopDto", startShopDto);//发起方
		sendParams.put("activity", activityDto);//活动
		sendParams.put("operateType", operateType);//参与方操作活动，通知发起商铺
		//短信方式必填
		logger.info("发送消息，请求参数："+sendParams);
		// TODO ...插入点 发送消息
		ProgramUtils.batchExecutePoint("pushMessagePoint", 308, sendParams);
		return null;
	}
}
