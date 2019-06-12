package com.idcq.appserver.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.message.IMessageCenterDao;
import com.idcq.appserver.dto.message.MessageCenterDto;
@Service
public class MessageCenterServiceImpl implements IMessageCenterService {
	@Autowired
	private IMessageCenterDao messageCenterDao;
	@Override
	public int saveMessageCenter(MessageCenterDto messageCenterDto)
			throws Exception {
		return messageCenterDao.addMessageCenterDto(messageCenterDto);
	}

	@Override
	public int updateMessageCenter(MessageCenterDto messageCenterDto)
			throws Exception {
		return messageCenterDao.updateMessageCenterDtoById(messageCenterDto);
	}

}
