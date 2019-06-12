package com.idcq.appserver.service.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.message.IPushShopMsgDao;
import com.idcq.appserver.dao.message.IPushUserMsgDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IPushUserTableDao;
import com.idcq.appserver.dao.wifidog.IShopDeviceDao;
import com.idcq.appserver.dto.message.MessageSettingDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.message.PushShopMsgDto;
import com.idcq.appserver.dto.message.PushUserMsgDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.PushUserTableDto;
import com.idcq.appserver.utils.Jpush;


/**
 * 消息推送服务
 * 
 * @author Administrator
 * 
 * @date 2015年4月17日
 * @time 上午9:04:12
 */
@Service
public class PushServiceImpl implements IPushService{
	@Autowired
	public IPushUserMsgDao pushUserMsgDao;
	@Autowired
	public IPushShopMsgDao pushShopMsgDao;
	@Autowired
	public IPushUserTableDao pushUserTableDao;
	@Autowired
	public IShopDeviceDao shopDeviceDao;
	@Autowired
	private IMessageSettingService messageSettingService;
	@Autowired
	private IShopDao shopDao;
	
	public int pushInfoToUser(PushDto push,Integer userType) throws Exception {
			Jpush.sendPushToTarget(push.getPlatForm(), userType, push.getRegId(),push.getTitle(), push.getContent());
			PushUserMsgDto msg = new PushUserMsgDto();
			msg.setUserId(push.getUserId());
			msg.setRegId(push.getRegId());
			msg.setAction(push.getAction());
			msg.setMessageContent(push.getContent());	
			msg.setSendTime(new Date());
			this.pushUserMsgDao.insertSelective(msg);
		return 1;
	}

	public int pushInfoToShop(PushDto push) throws Exception {
			Jpush.sendPushToTarget(push.getPlatForm(), null, push.getRegId(),null,push.getContent());
			PushShopMsgDto msg = new PushShopMsgDto();
			msg.setShopId(push.getShopId());
			msg.setRegId(push.getRegId());
			msg.setAction(push.getAction());
			msg.setMessageContent(push.getContent());	
			msg.setSendTime(new Date());
			this.pushShopMsgDao.insertSelective(msg);
		return 1;
	}
	
	public int pushInfoToShop2(PushDto push) throws Exception {
//		List<PushUserTableDto> pushUserTables = pushUserTableDao.getPushUserByUserId(push.getShopId());
		Long shopId = push.getShopId();
		String shopRegId = shopDeviceDao.getShopRegIdByShopId(shopId);
		//找不到商家设备信息，不做推送
		if(!StringUtils.isBlank(shopRegId)){
			ShopDto shop = shopDao.getShopById(shopId);
			if(null == shop) {
				return 0;
			}
			String shopMode = shop.getShopMode();
			if(null == shopMode) {
				push.setPlatForm(CommonConst.RESTAURANT);
			} else {
				push.setPlatForm(shopMode);
			}
			push.setRegId(shopRegId);
			MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(push.getAction());
			if(null != messageSettingDto && 1 == messageSettingDto.getRemandFlag()) {
				this.pushInfoToShop(push);
				return 1;	
			}else{
				return 0;
			}
		}else{
			return 0;
		}
		
	}
	
	public int pushInfoToUser2(PushDto push ,Integer userType) throws Exception {
		List<PushUserTableDto> pushUserTables = pushUserTableDao.getPushUserByUserId(push.getUserId(),userType);
		String platForm = null;
		for(PushUserTableDto pushUserTable : pushUserTables){
			if(StringUtils.containsIgnoreCase(pushUserTable.getOsInfo(), "ios")){//ios平台
				platForm = "ios";
			}else{//android平台
				platForm = "and";
			}
			push.setPlatForm(platForm);
			push.setRegId(pushUserTable.getRegId());
			MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(push.getAction());
			if(null != messageSettingDto && 1 == messageSettingDto.getRemandFlag()) {
				push.setTitle(messageSettingDto.getMsgTitle());
				this.pushInfoToUser(push, userType);
				return 1;
			}else{
				return 0;
			}
		}
		return 1;
	}
	
	

	public int pushInfo(String osInfo, Integer userType, String registrationId,
			String title, String content, String action) throws Exception {
		MessageSettingDto messageSettingDto = messageSettingService.isSendMsgSettingByKey(action);
		if(null != messageSettingDto && 1 == messageSettingDto.getRemandFlag()) {
			Jpush.sendPushToTarget(osInfo, userType, registrationId, title, content);
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public List<Map> getRegIdsByShopId(Long shopId) throws Exception {
		return shopDeviceDao.getShopRegIdsByShopId(shopId);
	}

	public List<PushUserTableDto> getRegIdsByUserId(Long userId,int userType) throws Exception {
		List<PushUserTableDto> pushUserTables = pushUserTableDao.getPushUserByUserId(userId,userType);
		return pushUserTables;
	}
	
	
}
