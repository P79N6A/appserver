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
import com.idcq.appserver.dao.redpacket.IRedPacketDao;
import com.idcq.appserver.dto.message.MessageCenterDto;
import com.idcq.appserver.dto.redpacket.RedPacketDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.ProgramUtils;
/**
 * 红包过期提醒
 * @author nie_jq
 *
 */
@Service("pushRedPacketUserProcessor")
public class PushRedPacketUserProcessor implements IProcessor{
	private Log logger = LogFactory.getLog(PushRedPacketUserProcessor.class);
	public Object exective(Map<String, Object> params) throws Exception {
		logger.info("----------会员红包过期，通知会员，参数："+params);
		UserDto userDto = (UserDto) params.get("userData");
		Long redPacketId = (Long) params.get("redPacketId");
		IRedPacketDao redPacketDao = BeanFactory.getBean(IRedPacketDao.class);
		RedPacketDto redPacketDto = redPacketDao.getRedPacketDtoById(redPacketId);
		if (null == redPacketDto) {
			logger.warn("----------------红包信息不存在，编号："+redPacketId);
			return null;
		}
		MessageCenterDto messageCenterDto = new MessageCenterDto();
		messageCenterDto.setBizId(redPacketId);
		messageCenterDto.setMessageTitle("红包过期提醒");
		messageCenterDto.setShowContent(redPacketDto.getAmount()+"元红包快过期");
		messageCenterDto.setNotifyModel(MsgCenterNotifyModelEnum.END2END.getValue());//点对点
		messageCenterDto.setMessageStatus(MsgCenterMsgStatusEnum.SEND_OK.getValue());//发送OK
		messageCenterDto.setBizType(BizTypeEnum.BUSAREA_ACTIVITY.getValue());//商圈活动
		messageCenterDto.setNotifyType(MsgCenterNotifyTypeEnum.NOTITY_USER.getValue());//通知商铺会员
		messageCenterDto.setReceiverId(userDto.getUserId());
		Map<String, Object> sendParams = new HashMap<String, Object>();
		sendParams.put("sendDataKey", userDto);
		sendParams.put("msgDataKey", messageCenterDto);
		sendParams.put("redPacket", redPacketDto);
		sendParams.put("operateType", 4);//红包过期提醒
		sendParams.put("usage",CommonConst.RED_PACKET_EXPIRED);//短信方式必填
		logger.info("发送消息，请求参数："+sendParams);
		// TODO ...插入点 发送消息
		ProgramUtils.executeBeanByExecutePointCode("pushMessageSmsPoint", 1, sendParams);
		return null;
	}

}
