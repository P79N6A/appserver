package com.idcq.appserver.dao.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.GoodsCommentDto;
import com.idcq.appserver.dto.user.OrderCommentDto;

/**
 * 订单评论dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 下午5:10:44
 */
@Repository
public class UserOrderCommentImpl extends BaseDao<OrderCommentDto> implements IUserOrderCommentDao{
	
	public int makeGoodsComment(GoodsCommentDto goodsCommentDao)
			throws Exception {
		return (Integer)super.insert(generateStatement("makeGoodsComment"), goodsCommentDao);
	}

	@Override
	public int makeOrderComment(OrderCommentDto orderCommentDto)
			throws Exception {
		return (Integer)super.insert(generateStatement("makeOrderComment"), orderCommentDto);
	}

	public List<Map<String, Object>> getOrderCommentById(String orderId,
			int pNo, int pSize,Long userId) {
		Map<String,Object> map  = new HashMap<String,Object>();
		map.put("orderId", orderId);
		map.put("userId", userId);
		map.put("n", (pNo-1)*pSize);
		map.put("m",pSize);
		return (List)super.findList(generateStatement("getOrderCommentById"), map);
	}

	@Override
	public int getOrderCommentCountById(String orderId) {
		return (Integer)super.selectOne(generateStatement("getOrderCommentCountById"),orderId);
	}

}
