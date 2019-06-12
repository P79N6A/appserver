package com.idcq.appserver.service.user;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.user.IPushUserTableDao;
import com.idcq.appserver.dto.user.PushUserTableDto;

@Service
public class PushUserTableServiceImpl implements IPushUserTableService{

	@Autowired
	public IPushUserTableDao pushUserTableDao;
	
	
	public int savePushUser(PushUserTableDto pushUser) {
		return this.pushUserTableDao.savePushUser(pushUser);
	}

	public List<PushUserTableDto> getPushUserByUserId(Long userId, Integer userType) {
		return this.pushUserTableDao.getPushUserByUserId(userId, userType);
	}

	public PushUserTableDto getPushUserByRegId(String regId) {
		return this.pushUserTableDao.getPushUserByRegId(regId);
	}

	public int updatePushUser(PushUserTableDto pushUser) {
		return this.pushUserTableDao.updatePushUser(pushUser);
	}

	/**
	 * 向指定的区域用户推送消息
	 */
	public List<PushUserTableDto> getPushUserByRegion(Map param) {
		return this.pushUserTableDao.getPushUserByRegion(param);
	}
}
