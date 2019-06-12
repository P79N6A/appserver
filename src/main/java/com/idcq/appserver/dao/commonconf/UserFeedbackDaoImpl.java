package com.idcq.appserver.dao.commonconf;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.commonconf.UserFeedbackDto;

@Repository
public class UserFeedbackDaoImpl extends BaseDao<UserFeedbackDto> implements
		IUserFeedbackDao {

	public int giveFeedback(UserFeedbackDto dto) {
		return super.insert(generateStatement("insertUserFeedback"),dto);
	}
	
	/**
	 * 根据条件进行查询
	 */
	public List<UserFeedbackDto> queryByCondition(Map<String, Object> parameters) {
		return super.findList(generateStatement("queryByCondition"), parameters);
	}

}
