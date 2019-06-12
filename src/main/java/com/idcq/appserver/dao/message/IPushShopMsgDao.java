package com.idcq.appserver.dao.message;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.message.PushShopMsgDto;

public interface IPushShopMsgDao {
	/**
	 * 记录向商铺设备推送的消息
	 * @param dto
	 * @return
	 */
	int insertSelective(PushShopMsgDto dto)throws Exception;
	
	/**
	 * 记录向商铺设备推送的消息
	 * @param dto
	 * @return
	 */
	int batchInsertSelective(List<PushShopMsgDto> dtos)throws Exception;



	int countMsgByConditions(Map<String, Object> searchParams);

	List<PushShopMsgDto> searchMsgByConditions(Map<String, Object> searchParams);


	void updateShopPushMsgStatus(Map<String, Object> updateParams);
}
