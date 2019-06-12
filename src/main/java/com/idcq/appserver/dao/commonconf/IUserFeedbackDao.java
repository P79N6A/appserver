package com.idcq.appserver.dao.commonconf;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.commonconf.UserFeedbackDto;

public interface IUserFeedbackDao {
	
	int giveFeedback(UserFeedbackDto dto);
	
	/**
	 * 根据条件进行查询
	 * @param parameters
	 * @return
	 */
	List<UserFeedbackDto> queryByCondition(Map<String,Object>parameters);
}
