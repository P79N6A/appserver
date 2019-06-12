package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.UserGoodsCommentDto;

public interface IUserGoodsCommentDao {
	
	/**
	 * 获取商品评论
	 * 
	 * @param goodsId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<UserGoodsCommentDto> getGoodsComments(Long goodsId, Integer page, Integer pageSize) throws Exception ;
	
	Integer getGoodsCommentsTotal(Long goodsId) throws Exception ;
	
}
