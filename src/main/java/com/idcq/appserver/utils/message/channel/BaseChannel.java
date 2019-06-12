package com.idcq.appserver.utils.message.channel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.dto.message.MessageCenterDto;

public abstract class BaseChannel {
	public static Log logger = LogFactory.getLog(BaseChannel.class.getSimpleName());
	public abstract void send(MessageCenterDto messageCenterDto,Object obj) throws Exception;
	
	public Integer notifyChannel;

	public Integer getNotifyChannel() {
		return notifyChannel;
	}

	public void setNotifyChannel(Integer notifyChannel) {
		this.notifyChannel = notifyChannel;
	}
}
