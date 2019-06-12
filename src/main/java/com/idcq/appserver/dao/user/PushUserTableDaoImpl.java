package com.idcq.appserver.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.PushUserTableDto;

@Repository
public class PushUserTableDaoImpl extends BaseDao<PushUserTableDto> implements IPushUserTableDao{

	public int savePushUser(PushUserTableDto pushUser) {
		
		return super.insert(generateStatement("savePushUser"), pushUser);
	}

	public List<PushUserTableDto> getPushUserByUserId(Long userId, Integer userType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("userType", userType);
		return super.findList(generateStatement("getPushUserByUserId"), map);
	}

	public PushUserTableDto getPushUserByRegId(String regId) {
		return (PushUserTableDto) super.selectOne(generateStatement("getPushUserByRegId"), regId);
	}

	public int updatePushUser(PushUserTableDto pushUser) {
		
		return super.update(generateStatement("updatePushUser"), pushUser);
	}
	
	/**
	 * 向指定的区域用户推送消息
	 */
	public List<PushUserTableDto> getPushUserByRegion(Map param) {
		return super.findList(generateStatement("getPushUserByRegion"), param);
	}

	public int updatePushUserByUserId(PushUserTableDto pushUser) {
		return super.update(generateStatement("updatePushUserByUserId"), pushUser);
	}

	public void deletePushUserByRedId(String regId, Long userId, Integer userType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("regId", regId);
		map.put("userType", userType);
		super.delete(generateStatement("deletePushUserByRedId"), map);
	}
	
}
