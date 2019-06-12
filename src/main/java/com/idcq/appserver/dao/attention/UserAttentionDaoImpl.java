package com.idcq.appserver.dao.attention;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.attention.UserAttentionDto;

@Repository
public class UserAttentionDaoImpl extends BaseDao<UserAttentionDto> implements
		IUserAttentionDao {

	public List<UserAttentionDto> getUserAttentionList(int pageNo, int pageSize,
			Long userId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("n", (pageNo-1)*pageSize);
		map.put("m", pageSize);
		return super.findList(generateStatement("getUserAttentionList"),map);
	}

	public int getUserAttentionTotal(Long userId) {
		return (Integer)super.selectOne(generateStatement("getUserAttentionTotal"), userId);
	}

	public int addUserAttention(UserAttentionDto dto) {
		return super.insert(generateStatement("addUserAttention"), dto);
	}

	public int getCountByUserIdAndShopId(Long userId, Long shopId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("shopId", shopId);
		return (Integer)super.selectOne(generateStatement("getCountByUserIdAndShopId"), map);
	}
	
	/**
	 * 取消关注
	 */
	public int cancelUserAttention(Long userId, Long shopId) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("userId", userId);
		map.put("shopId", shopId);
		return super.delete("cancelUserAttention", map);
	}

}
