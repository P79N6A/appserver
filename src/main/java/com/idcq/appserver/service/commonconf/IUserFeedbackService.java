package com.idcq.appserver.service.commonconf;

import java.util.Map;

import com.idcq.appserver.dto.commonconf.UserFeedbackDto;

public interface IUserFeedbackService {

	int giveUserFeedback(UserFeedbackDto dto) throws Exception;

	/**
	 * APP版本检测
	 * @param userId
	 * @param appId
	 * @param curVersion
	 * @return
	 */
	Map queryVersion(Long userId, Long appId, String curVersion)throws Exception;
	
	
	UserFeedbackDto queryByFeedBackId(Long feedBackId)throws Exception;
}

