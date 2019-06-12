package com.idcq.appserver.service.message;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.message.IMessageSettingDao;
import com.idcq.appserver.dto.message.MessageSettingDto;


/**
 * 消息service
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:57:03
 */
@Service
public class MessageSettingServiceImpl implements IMessageSettingService{

	private final Logger logger = Logger.getLogger(MessageSettingServiceImpl.class);
	
	@Autowired
	public IMessageSettingDao messageSettingDao;
	
	public MessageSettingDto isSendMsgSettingByKey(String key) throws Exception {
		MessageSettingDto messageSettingDto=messageSettingDao.getMsgSettingByKey(key);
		return messageSettingDto;
	}
	
}
