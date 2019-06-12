package com.idcq.appserver.utils.message;

import java.util.HashMap;
import java.util.List;
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
import com.idcq.appserver.service.busArea.busAreaActivity.IBusAreaActivityService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.ProgramUtils;

/**
 * 商圈活动发布成功后，通知可参与商铺
 * @author nie_jq
 *
 */

@Service("pushBusinessAreaShopProcessor")
public class PushBusinessAreaShopProcessor implements IProcessor{
	
	private Log logger = LogFactory.getLog(PushBusinessAreaShopProcessor.class);
	
	public Object exective(Map<String, Object> params) {
		logger.info("----------商圈活动发布成功，通知可参与商铺，参数："+params);
		try {
			Long shopId = (Long) params.get("shopId");
			Long businessAreaActivityId = (Long) params.get("businessAreaActivityId");
			doSendShopMessage(shopId,businessAreaActivityId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询商圈活动可以参与的商家列表
	 * @param shopId
	 * @param businessAreaActivityId
	 * @return
	 * @throws Exception
	 */
	public void doSendShopMessage(Long shopId,Long businessAreaActivityId) throws Exception{
		IShopServcie shopService = BeanFactory.getBean(IShopServcie.class);
		ShopDto shopDto = shopService.getShopById(shopId);
		if (null != shopDto) {
			//查询该商铺同城商铺
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("shopId", shopDto.getShopId());
			params.put("cityId", shopDto.getCityId());
			Integer count = shopService.getShopListCount(params);
			List<ShopDto> shopDtoList = null;
			if (count > 0) {
				IBusAreaActivityService activityService = BeanFactory.getBean(IBusAreaActivityService.class);
				BusinessAreaActivityDto activityDto = activityService.getBusinessAreaActivityById(businessAreaActivityId);
				if (count > 50) {
					//大于50条，分页查询
					mulitShopData(activityDto,count,params,shopDto);
				}else{
					//50条内直接全部查询
					params.put("pNo", 1);
					params.put("pSize", 50);
					shopDtoList = shopService.getShopList(params);
					sendShopMessage(shopDtoList,activityDto,shopDto);
				}
			}
		}
	}
	
	private void mulitShopData(BusinessAreaActivityDto activityDto, int totalCount, Map<String, Object> params,ShopDto startShopDto) throws Exception{
		int pSize = 50;
		int totalPage = totalCount % pSize == 0? (totalCount / pSize) : ((totalCount / pSize)+1);
		params.put("pSize", pSize);
		List<ShopDto> shopDtoList = null;
		IShopServcie shopService = BeanFactory.getBean(IShopServcie.class);
		for (int i = 1; i <= totalPage; i++) {
			params.put("pNo", (i-1)*pSize);
			logger.info("获取第"+i+"商铺数据，请求参数："+params);
			shopDtoList = shopService.getShopList(params);
			sendShopMessage(shopDtoList,activityDto,startShopDto);
			Thread.sleep(1000);
		}
	}
	
	private void sendShopMessage(List<ShopDto> shopDtoList,BusinessAreaActivityDto activityDto,ShopDto startShopDto) throws Exception{
		if (null != shopDtoList && shopDtoList.size() > 0) {
			for(ShopDto shopDto : shopDtoList){
				MessageCenterDto messageCenterDto = new MessageCenterDto();
				String content = "一点传奇，邀请参加"+startShopDto.getShopName()+"店铺发起的"+activityDto.getBusinessAreaName()+"活动";
				messageCenterDto.setBizId(activityDto.getBusinessAreaActivityId());
				messageCenterDto.setMessageTitle(activityDto.getActivityShareTitle());
				messageCenterDto.setShowContent(content);
				messageCenterDto.setMessageImage(activityDto.getActPosterUrls());
				messageCenterDto.setNotifyModel(MsgCenterNotifyModelEnum.END2END.getValue());//点对点
				messageCenterDto.setMessageStatus(MsgCenterMsgStatusEnum.SEND_OK.getValue());//发送OK
				messageCenterDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());//商圈活动
				messageCenterDto.setNotifyType(MsgCenterNotifyTypeEnum.NOTITY_SHOP.getValue());//通知商铺
				messageCenterDto.setReceiverId(shopDto.getShopId());
				Map<String, Object> sendParams = new HashMap<String, Object>();
				sendParams.put("sendDataKey", shopDto);//参与商铺
				sendParams.put("msgDataKey", messageCenterDto);//
				sendParams.put("startShopDto", startShopDto);//发起方
				sendParams.put("activity", activityDto);//发起的活动
				sendParams.put("operateType", 3);//发布活动成功，通知商家参与
				sendParams.put("usage",CommonConst.TRADING_AREA_ACTIVITIES_SHOP + messageCenterDto.getBizId());//短信方式必填
				logger.info("发送消息，请求参数："+sendParams);
				// TODO ...插入点 发送消息
				ProgramUtils.batchExecutePoint("pushMessagePoint", 308, sendParams);
			}
		}
	}
}
