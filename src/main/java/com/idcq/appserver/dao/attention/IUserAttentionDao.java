package com.idcq.appserver.dao.attention;

import java.util.List;

import com.idcq.appserver.dto.attention.UserAttentionDto;

public interface IUserAttentionDao {
	
	/**
	 * 获取用户关注列表
	 */
	List<UserAttentionDto> getUserAttentionList(int pageNo,int pageSize,Long userId);
	
	//
	int getUserAttentionTotal(Long userId);
	
	//关注店铺
	int addUserAttention(UserAttentionDto dto);
	
	//判断是否已经关注过
	int getCountByUserIdAndShopId(Long userId,Long shopId);
	
	/**
	 * 取消关注
	 * @param userId
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	int cancelUserAttention(Long userId,Long shopId) throws Exception;
}
