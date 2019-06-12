package com.idcq.appserver.service.message;

import com.idcq.appserver.dto.message.PushShopMsgDto;

public interface IPushShopMsgService {
	/**
	 * 记录向商铺设备推送的消息
	 * @param dto
	 * @return
	 */
	public int insertSelective(PushShopMsgDto dto) throws Exception;
}
