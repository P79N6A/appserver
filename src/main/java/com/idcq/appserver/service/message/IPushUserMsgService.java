package com.idcq.appserver.service.message;

import com.idcq.appserver.dto.message.PushUserMsgDto;

public interface IPushUserMsgService {
	/**
	 * 记录向用户推送的消息
	 * @param dto
	 * @return
	 */
	public int insertSelective(PushUserMsgDto dto) throws Exception;

	void pushAllRemainedMsg();
}
