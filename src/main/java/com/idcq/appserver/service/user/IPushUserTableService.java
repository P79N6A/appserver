package com.idcq.appserver.service.user;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.PushUserTableDto;

public interface IPushUserTableService {

	/**
	 * 保存推送注册id
	 * @param pushUser
	 * @return
	 */
	int savePushUser(PushUserTableDto pushUser);
	
	/**
	 * 根据用户查询推送注册注册信息
	 * @param userId
	 * @return
	 */
	List<PushUserTableDto> getPushUserByUserId(Long userId, Integer userTpye);
	
	/**
	 * 根据regId查询推送注册注册信息
	 * @param regId
	 * @return
	 */
	PushUserTableDto getPushUserByRegId(String regId);
	
	/**
	 * 更新注册信息
	 * @param pushUser
	 * @return
	 */
	int updatePushUser(PushUserTableDto pushUser);
	
	/**
	 * 向指定的区域用户推送消息
	 */
	public List<PushUserTableDto> getPushUserByRegion(Map param);
}
