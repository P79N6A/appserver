package com.idcq.appserver.dao.message;

import com.idcq.appserver.dto.message.MessageSettingDto;

public interface IMessageSettingDao {
	
	public MessageSettingDto getMsgSettingByKey(String key) throws Exception;
	
}
