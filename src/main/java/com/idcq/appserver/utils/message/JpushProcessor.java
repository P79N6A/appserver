package com.idcq.appserver.utils.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.BizTypeEnum;
import com.idcq.appserver.common.enums.ClientSystemTypeEnum;
import com.idcq.appserver.common.enums.MsgCenterNotifyTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dto.activity.BusinessAreaActivityDto;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.redpacket.RedPacketDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.message.IMessageCenterService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.Jpush;
/**
 * 商圈活动商铺消息推送-jpush
 * @author nie_je
 *
 */
@Service("jpushProcessor")
public class JpushProcessor implements IProcessor{
	private Log logger = LogFactory.getLog(JpushProcessor.class);

	public Object exective(Map<String, Object> params) throws Exception {
		Map<String,Object> results = getDeviceRegId(params);
		List<PushDto> pushDtos = (List<PushDto>) results.get("pushDtos");
		List<MessageCenterDto> messages = (List<MessageCenterDto>) results.get("messages");
		if (null != pushDtos && pushDtos.size()> 0) {
			IPushService pushService = BeanFactory.getBean(IPushService.class);
			IMessageCenterService messageCenterService = BeanFactory.getBean(IMessageCenterService.class);
			for (int i = 0; i < pushDtos.size(); i++) {
				PushDto pushDto = pushDtos.get(i);
				MessageCenterDto msgDto = messages.get(i);
				String content = msgDto.getShowContent();
				msgDto.setNotifyTime(new Date());
				// TODO ...需要定义Jpush推送的内容模板，及每个action对应的值
				msgDto.setShowContent(content);
				//先保存消息，返回主键
				messageCenterService.saveMessageCenter(msgDto);
				logger.info("保存消息信息："+msgDto);
				//发送内容组装
				logger.info("推送内容："+content);
				Integer userType = null;
				if (msgDto.getNotifyType() == 1) {
					userType = CommonConst.USER_TYPE_ZREO;
				}else{
					userType = CommonConst.USER_TYPE_TEN;
				}
				//消息推送
				Jpush.sendPushToTarget(pushDto.getPlatForm(), userType, pushDto.getRegId(), msgDto.getMessageTitle(), content.toString());
			}
		}else{
			logger.warn("没有查询到可以接收消息的设备！");
		}
		return null;
	}
	
	private Map<String, Object> getDeviceRegId(Map<String, Object> params) throws Exception{
		MessageCenterDto messageCenterDto = (MessageCenterDto) params.get("msgDataKey");
		Object obj = params.get("sendDataKey");
		Integer operateType = (Integer) params.get("operateType");
//		StringBuilder notifyContent = new StringBuilder("一点传奇，");
		JSONObject pushStr = new JSONObject();
		
		List<PushDto> pushDtos = new ArrayList<PushDto>();
		List<MessageCenterDto> messages = new ArrayList<MessageCenterDto>();
		long bizId = 0;
		Long shopId = null;
		int bizType = 0;
		String platForm = "";
		Integer clientSystemType = 0;//接收消息终端:1=收银机,2=收银PAD,3=消费者APP,4=一点管家,5=路由器,6=微信商城,7=公众号,8=商铺后台
		int notifyType = messageCenterDto.getNotifyType();
		//通知商铺会员
		if (notifyType == MsgCenterNotifyTypeEnum.NOTITY_USER.getValue()) //通知商铺会员
		{
			/**
			 * 场景一：商圈活动报名截止，通知商铺会员operateType=5
			 */
			//模板：一点传奇，shopName店铺活动
			ShopMemberDto shopMemberDto = (ShopMemberDto) obj;//商铺会员
			ShopDto startShopDto = (ShopDto) params.get("startShopDto");//发起活动的商铺
			//BusinessAreaActivityDto activityDto = (BusinessAreaActivityDto) params.get("activity");//商圈活动
			bizId = shopMemberDto.getUserId();
			bizType = CommonConst.USER_TYPE_ZREO;
			clientSystemType = ClientSystemTypeEnum.CONSUMER_APP.getValue();
//			notifyContent.append(startShopDto.getShopName()).append("店铺活动");
			pushStr.put("action", "endActivity");
			pushStr.put("content",startShopDto.getShopName()+ "店铺活动");
		
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
			
			if (operateType == 3) {
				
				//旧模板：邀请参加shopName店铺发起的activityName活动
				/*新模板：【老边饺子馆】发起商家联盟活动，诚邀您参加活动！参与商家的会员消费可获得红包，快来给您的会员谋福利吧！*/
				//TODO 修改推送内容
				pushStr.put("action", "startActivity");
				pushStr.put("content", "【"+startShopDto.getShopName()+"】发起商家联盟活动，诚邀您参加活动！参与商家的会员消费可获得红包，快来给您的会员谋福利吧！");
				
			}else if(operateType == 1){
				
				//TODO 修改推送内容
				//旧模板：shopName店铺参与活动/shopName店铺取消参与
				/* 新模板：您已成功参加了【老边饺子馆】商圈联盟活动，您的会员到该店消费，可获得红包，快去通知您的会员吧*/
				pushStr.put("action", "joinActivity");
				pushStr.put("content","您已成功参加了【"+startShopDto.getShopName()+"】商圈联盟活动，您的会员到该店消费，可获得红包，快去通知您的会员吧");
			
			}else if (operateType == 2){
				//模板：shopName店铺参与活动/shopName店铺取消参与
//				notifyContent.append(startShopDto.getShopName()).append("店铺取消参与");
				pushStr.put("content", startShopDto.getShopName()+"店铺取消参与");
				pushStr.put("action", "cancelActivity");
			}
			bizId = shopDto.getPrincipalId();
			shopId = shopDto.getShopId();
			bizType = CommonConst.USER_TYPE_TEN;
			clientSystemType = ClientSystemTypeEnum.YD_MGR.getValue();
			String shopMode = shopDto.getShopMode();
			if(null == shopMode) {
				platForm = CommonConst.RESTAURANT;
			} else {
				platForm = shopMode;
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
//			notifyContent.append(redPacketDto.getAmount()+"").append("元红包快过期");
			pushStr.put("content", redPacketDto.getAmount()+"元红包快过期");
			pushStr.put("action", "redPacketExpire");
			UserDto userDto = (UserDto) obj;
			bizId = userDto.getUserId();
			bizType = CommonConst.USER_TYPE_ZREO;
			clientSystemType = ClientSystemTypeEnum.CONSUMER_APP.getValue();
		}
		IPushService pushService = BeanFactory.getBean(IPushService.class);
		//移动端（IOS、Android）：（消费者APP、一点管家APP）
		List<PushUserTableDto> tableDtos =  pushService.getRegIdsByUserId(bizId, bizType);
		logger.info("获取的移动端列表："+tableDtos);
		if (null != tableDtos && tableDtos.size() > 0) {
			for(PushUserTableDto tableDto : tableDtos){
				PushDto pushDto = new PushDto();
				pushDto.setRegId(tableDto.getRegId());
				pushDto.setPlatForm(platForm);
				pushDtos.add(pushDto);
				MessageCenterDto msgDto = new MessageCenterDto();
				PropertyUtils.copyProperties(msgDto, messageCenterDto);
				msgDto.setShowContent(pushStr.getString("content"));
				msgDto.setClientSystemType(clientSystemType);
				messages.add(msgDto);
			}
		}
		if (bizType == 10) {
			//商铺设备：（收银机、收银PAD，路由器）
			List<Map> regList = pushService.getRegIdsByShopId(shopId);
			logger.info("获取的商铺设备列表："+regList);
			if (null != regList && regList.size() > 0) {
				for(Map bean : regList){
					String regId = (String) bean.get("regId");
					//设备类型:路由器,收银机,点餐PAD,其他
					String devictType = (String) (CommonValidUtil.isEmpty(bean.get("deviceType"))?"":bean.get("deviceType"));
					PushDto pushDto = new PushDto();
					pushDto.setRegId(regId);
					pushDto.setPlatForm(platForm);
					pushDtos.add(pushDto);
					MessageCenterDto msgDto = new MessageCenterDto();
					PropertyUtils.copyProperties(msgDto, messageCenterDto);
					msgDto.setShowContent(pushStr.getString("content"));
					msgDto.setClientSystemType(getClientSystemType(devictType));
					messages.add(msgDto);
				}
			}
		}
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("action", pushStr.toString());
		results.put("pushDtos", pushDtos);
		results.put("messages", messages);
		//新增活动id返回
		BusinessAreaActivityDto activityDto = (BusinessAreaActivityDto) params.get("activity");//商圈活动
		if(activityDto!=null){
			results.put("businessAreaActivityId", activityDto.getBusinessAreaActivityId());
		}
		return results;
	}
	
	private Integer getClientSystemType(String devictType){
		if (null != devictType && devictType.contains("PAD")) {
			devictType = ClientSystemTypeEnum.CASHIER_PAD.getName();
		}
		return ClientSystemTypeEnum.getValueByName(devictType);
	}

}
