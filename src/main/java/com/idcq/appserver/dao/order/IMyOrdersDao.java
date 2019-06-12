package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.order.MyOrdersDto;
import com.idcq.appserver.dto.order.OrderShopGoodsDto;

public interface IMyOrdersDao {
	
	/**
	 * 获取会员的订单列表
	 * 
	 * @param myOrders
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<MyOrdersDto> getMyOrders(Long userId, Map map, int page, int pageSize) throws Exception;
	
	/**
	 * 
	 * @param myOrders
	 * @return
	 * @throws Exception
	 */
	int getMyOrdersCount(Long userId, Map map) throws Exception;
	
	/**
	 * 根据订单id查询商品和商铺的信息
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<OrderShopGoodsDto> getShopGoodsByOrderId(String orderId) throws Exception;
	
	/**
	 * 根据商铺id查询商铺名称
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map getShopNameByShopId(Long shopId)throws Exception;
	
	
	/**
	 * 根据用户id分组查询我的订单
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMyOrderNumber(Long userId) throws Exception;
	
	/**
	 * 根据订单id计算订单下商品数量
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Integer getOrderGoodsNum(String orderId)throws Exception;
	
	/**
	 * 这个用户不需要显示的订单个数
	 * 订单类型是预定订单0，且订单场景分类是1（到店点菜订单），且预付金额（prepay_money）大于0，且支付状态（pay_status）不等于已支付1，且订单状态为0
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	int getFiterOrderNum(Long userId)throws Exception;
}
