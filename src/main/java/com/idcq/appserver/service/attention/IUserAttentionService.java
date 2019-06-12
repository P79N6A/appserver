package com.idcq.appserver.service.attention;

import com.idcq.appserver.dto.attention.UserAttentionDto;
import com.idcq.appserver.dto.common.PageModel;

public interface IUserAttentionService {

	/**
	 * 根据userID获取用户关注列表
	 */	
	PageModel getMyAttention(int pageNo,int pageSize,long userId) throws Exception;
	
	
	int addUserAttention(UserAttentionDto userAttentionDto) throws Exception;
	
	//判断是否已经关注过
	int getCountByUserIdAndShopId(long userId,long shopId) throws Exception;
	
	/**
	 * 取消关注
	 * @param userId
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	int cancelUserAttention(long userId,long shopId) throws Exception;
}
