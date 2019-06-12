package com.idcq.appserver.dao.shop;

import java.util.List;

import com.idcq.appserver.dto.shop.UserShopCommentDto;

public interface IUserShopCommentDao {
	
	/**
	 * 获取商铺评论
	 * 
	 * @param shopId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<UserShopCommentDto> getShopComments(Long shopId,int page,int pageSize) throws Exception ;
	
	Integer getShopCommentsTotal(Long shopId) throws Exception ;
	
}
