package com.idcq.appserver.utils.message.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.ClientSystemTypeEnum;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.message.IMessageCenterService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
/**
 * 使用Jpush渠道发送消息
 * @author nie_jq
 *
 */
public class JpushChannel extends BaseChannel {
    @Deprecated
	public void send(MessageCenterDto messageCenterDto,Object obj) throws Exception {
		logger.info("使用jpush方式发送消息...................");
		Map<String,Object> results = getDeviceRegId(messageCenterDto,obj);
		List<PushDto> pushDtos = (List<PushDto>) results.get("pushDtos");
		List<MessageCenterDto> messages = (List<MessageCenterDto>) results.get("messages");
		if (null != pushDtos && pushDtos.size()> 0) {
			IPushService pushService = BeanFactory.getBean(IPushService.class);
			IMessageCenterService messageCenterService = BeanFactory.getBean(IMessageCenterService.class);
			for (int i = 0; i < pushDtos.size(); i++) {
				PushDto pushDto = pushDtos.get(i);
				MessageCenterDto msgDto = messages.get(i);
				//先保存消息，返回主键
				messageCenterService.saveMessageCenter(msgDto);
				logger.info("保存消息信息："+msgDto);
				//发送内容组装
				JSONObject content = new JSONObject();
				content.put("messageId", msgDto.getMessageId());
				content.put("messageTitle", msgDto.getMessageTitle());
				content.put("showContent", msgDto.getShowContent());
				content.put("messageImage", msgDto.getMessageImage());
				content.put("sendTime", DateUtils.getCurDate());
				content.put("feedbackType", msgDto.getFeedbackType());
				logger.info("推送内容："+content);
				Integer userType = null;
				if (msgDto.getNotifyType() == 1) {
					userType = CommonConst.USER_TYPE_ZREO;
				}else{
					userType = CommonConst.USER_TYPE_TEN;
				}
				//消息推送
				pushService.pushInfo(pushDto.getPlatForm(), userType, pushDto.getRegId(), msgDto.getMessageTitle(), content.toString(), "message");
			}
		}else{
			logger.warn("没有查询到可以接收消息的设备！");
		}
	}
	
	private Map<String, Object> getDeviceRegId(MessageCenterDto messageCenterDto,Object obj) throws Exception{
		List<PushDto> pushDtos = new ArrayList<PushDto>();
		List<MessageCenterDto> messages = new ArrayList<MessageCenterDto>();
		long bizId = 0;
		Long shopId = null;
		int bizType = 0;
		String platForm = "";
		Integer clientSystemType = 0;//接收消息终端:1=收银机,2=收银PAD,3=消费者APP,4=一点管家,5=路由器,6=微信商城,7=公众号,8=商铺后台
		if (messageCenterDto.getNotifyType() == 1) {
			UserDto userDto = (UserDto) obj;
			bizId = userDto.getUserId();
			bizType = CommonConst.USER_TYPE_ZREO;
			clientSystemType = ClientSystemTypeEnum.CONSUMER_APP.getValue();
		}else{
			ShopDto shopDto = (ShopDto) obj;
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
					msgDto.setClientSystemType(getClientSystemType(devictType));
					messages.add(msgDto);
				}
			}
		}
		Map<String, Object> results = new HashMap<String, Object>();
		results.put("pushDtos", pushDtos);
		results.put("messages", messages);
		return results;
	}
	
	private Integer getClientSystemType(String devictType){
		if (null != devictType && "点餐PAD".equals(devictType)) {
			devictType = ClientSystemTypeEnum.CASHIER_PAD.getName();
		}
		return ClientSystemTypeEnum.getValueByName(devictType);
	}

}
