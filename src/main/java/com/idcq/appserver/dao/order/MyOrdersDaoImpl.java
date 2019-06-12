package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.MyOrdersDto;
import com.idcq.appserver.dto.order.OrderShopGoodsDto;

@Repository
public class MyOrdersDaoImpl extends BaseDao<MyOrdersDto> implements IMyOrdersDao {

	public List<MyOrdersDto> getMyOrders(Long userId, Map map, int page,
			int pageSize) throws Exception {
		
		map.put("userId", userId);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);
		return super.findList(generateStatement("getMyOrders"), map);
	}

	public int getMyOrdersCount(Long userId, Map map) throws Exception {
		
		map.put("userId", userId);
		return (Integer) super.selectOne(generateStatement("getMyOrdersCount"), map);
	}

	public List<OrderShopGoodsDto> getShopGoodsByOrderId(String orderId)
			throws Exception {
		return this.getSqlSession().selectList("getShopGoodsByOrderId", orderId);
	}

	public Map getShopNameByShopId(Long shopId) throws Exception {
		return (Map) super.selectOne(generateStatement("getShopNameByShopId"), shopId);
	}
	
	public List<Map<String, Object>> getMyOrderNumber(Long userId) throws Exception {
		return this.getSqlSession().selectList("getMyOrderNumber", userId);
	}
	
    public Integer getOrderGoodsNum(String orderId)throws Exception {
		return (Integer) super.selectOne(generateStatement("getOrderGoodsNum"), orderId);
    }

	public int getFiterOrderNum(Long userId) throws Exception {
		return (int) super.selectOne(generateStatement("getFiterOrderNum"), userId);
	}

}
