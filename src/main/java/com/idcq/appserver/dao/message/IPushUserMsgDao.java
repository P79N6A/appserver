package com.idcq.appserver.dao.message;

import java.util.List;

import com.idcq.appserver.dto.message.PushUserMsgDto;
import org.apache.ibatis.session.RowBounds;

public interface IPushUserMsgDao {
	/**
	 * 记录向用户推送的消息
	 * @param dto
	 * @return
	 */
	public int insertSelective(PushUserMsgDto dto) throws Exception ;
	
	/**
	 * 记录向用户推送的消息
	 * @param dtos
	 * @return
	 */
	public int batchInsertSelective(List<PushUserMsgDto> dtos)throws Exception;

	List<PushUserMsgDto> getPushUserMsg(PushUserMsgDto pushUserMsgDto, RowBounds rowBounds);

	int countPushUserMsg(PushUserMsgDto pushUserMsgDto);

	/**
	 * 注意该方法名有误导性，该方法实际为更新1dcq_push_user_message，条件为根据pm_id更新
	 * @param pushUserMsgDtos
     */
	void batchUpdateOrInsert(List<PushUserMsgDto> pushUserMsgDtos);
}
