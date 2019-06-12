package com.idcq.appserver.dao.user;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.user.OrderCommentDto;


public interface IUserOrderCommentDao {
	
	/**
	 * 增加订单评论
	 * 
	 * @param userGoodsCommentDao
	 * @return
	 * @throws Exception
	 */
	int makeOrderComment(OrderCommentDto orderCommentDto) throws Exception;
	/**
	 * 根据orderId获取订单列表
	 * @param goodsCommentDto
	 * @return
	 */
	List<Map<String, Object>> getOrderCommentById(String orderId, int pNo, int pSize,Long userId);
	/**
	 * 根据orderId获取订单列表总数
	 * @param goodsCommentDto
	 * @return
	 */
	int getOrderCommentCountById(String orderId);
}
