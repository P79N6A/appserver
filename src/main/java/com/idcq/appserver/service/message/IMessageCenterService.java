package com.idcq.appserver.service.message;

import com.idcq.appserver.dto.message.MessageCenterDto;

public interface IMessageCenterService {
	/**
	 * 保存消息信息
	 * @param messageCenterDto
	 * @return
	 * @throws Exception
	 */
	public int saveMessageCenter(MessageCenterDto messageCenterDto) throws Exception;
	
	
	/**
	 * 修改消息信息
	 * @param messageCenterDto
	 * @return
	 * @throws Exception
	 */
	public int updateMessageCenter(MessageCenterDto messageCenterDto) throws Exception;
}
