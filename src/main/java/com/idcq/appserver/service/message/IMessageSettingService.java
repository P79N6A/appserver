package com.idcq.appserver.service.message;

import com.idcq.appserver.dto.message.MessageSettingDto;


public interface IMessageSettingService {
	
	/**
	 * 消息推送开关，true：推送，false：不推送
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public MessageSettingDto isSendMsgSettingByKey(String key) throws Exception;
	
}
