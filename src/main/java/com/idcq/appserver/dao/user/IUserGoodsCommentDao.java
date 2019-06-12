package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.GoodsCommentDto;


public interface IUserGoodsCommentDao {
	
	/**
	 * 增加商品评论
	 * 
	 * @param userGoodsCommentDao
	 * @return
	 * @throws Exception
	 */
	int makeGoodsComment(GoodsCommentDto goodsCommentDao) throws Exception;
	/**
	 * 根据userId、goodid 获取商品列表
	 * @param goodsCommentDto
	 * @return
	 */
	List<GoodsCommentDto> getGoodsCommentTimeById(GoodsCommentDto goodsCommentDto);

	/**
	 * 根据商品和用户id查询最近的一条评论
	 * @param map 
	 * @return
	 */
	List<GoodsCommentDto> getGoodSCommentsByMap(Map map);
	/**
	 * 更新商品评论
	 * @param map
	 * @throws Exception
	 */
	void updateGoosComLogo(Map map) throws Exception ;
	
	/**
	 * 获取用户编号根据评论编号
	 * @param commentId
	 * @return
	 * @throws Exception
	 */
	Long getUserIdByCommentId(Long commentId)throws Exception;
	/**
	 * 获取评论总数
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	public Integer getGoodsCommentsTotal(Long goodsId) throws Exception;

}
