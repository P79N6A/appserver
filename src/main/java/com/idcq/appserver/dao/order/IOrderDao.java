package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.order.OrderDto;

public interface IOrderDao {
	
	/**
	 * 测试生成订单号
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int saveTestOrderId(String orderId) throws Exception;
	
	/**
	 * 获取会员的订单列表
	 * 
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<OrderDto> getOrderList(OrderDto order,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取座位订单
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getSeatOrder(Map<String, Object> requestMap) throws Exception;
	/**
	 * 新增订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	OrderDto saveOrder(OrderDto order) throws Exception;
	
	/**
	 * 更新订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	int updateOrder(OrderDto order) throws Exception;
	
	/**
	 * 批量更新订单userId
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	int updateUserId(Map<String, String> updateParam) throws Exception;
	/**
	 * 直接删除订单
	 * @param orderId
	 */
	void delOrder(String orderId);
	
	/**
	 * 修改订单状态已经完成支付
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int updateOrderPayed(String orderId) throws Exception;
	
	/**
	 * 获取订单状态
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Integer getOrderStatus(String orderId) throws Exception;
	
	/**
	 * 获取指定的订单
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderDto getOrderById(String orderId) throws Exception;
	
	/**
	 * 获取订单主干信息
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderDto getOrderMainById(String orderId) throws Exception;
	
	/**
	 * 验证订单的存在性
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int queryOrderExists(String orderId) throws Exception;
	/**
	 * 获取订单分成比例
	 * @return
	 * @throws Exception
	 */
	public Map getOrderGoodsSettleSetting(Map param) throws Exception;
	/**
	 * 查询用户预订资源的数量
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int getNumberByOrder (String orderId) throws Exception;
	/**
	 * 根据订单状态获取订单信息
	 * @param orderStatus
	 * @return
	 */
	List<Map<String, Object>> getOrderByStatus(Integer orderStatus);
	/**
	 * 修改订单状态已经完成支付
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int updateOrderStatusById(String orderId) throws Exception;
	
	Integer getOrderStatusById(String orderId) throws Exception;
	
	/**
	 * 获取自助下单的订单列表接口
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> getOrder8List(Long shopId) throws Exception;

	/**
	 * 删除订单商品
	 * @param orderId
	 */
	void delOrderGoods(String orderId);

	/**
	 * 获取商铺设置
	 * @param shopId
	 * @return
	 */
	Map<String, Object> getDistributionTakeoutSetting(Long shopId, Integer orderServiceType);

	/**
	 * 获取订单资源中的预定时间
	 * @param orderId
	 * @return
	 */
	Map<String, Object> getUserReserveTime(String orderId);
	
	/**
	 * 修改订单删除标志
	 * @param orderId
	 * @return
	 */
	int updateOrderDeleteType(String orderId, Integer deleteType);
	
	/**
	 * 根据订单状态获取订单对象
	 * 
	 * @Function: com.idcq.appserver.dao.order.IOrderDao.getOrderDtoByStatus
	 * @Description: 订单表跟商铺表关联，根据订单状态查询confirm_minute不为0，且超过多少分钟的订单
	 *
	 * @param orderStatus
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月1日 下午1:59:06
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    shengzhipeng       v1.0.0         create
	 */
	List<OrderDto> getOrderDtoByStatus(Integer orderStatus) throws Exception;
	
	/**
	 * 根据时间查询最后修改时间小于参数时，
	 * 将已预订或已开单的订单变为已完成
	 * @Title: getNeedAutoFinishOrder 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return List<OrderDto>    返回类型 
	 * @throws
	 */
	List<OrderDto>getNeedAutoFinishOrder(String lastUpdateTime)throws Exception;
	
	
	void batchUpdateOrderStatus(List<String>orderIdList, Integer status)throws Exception;
	
	/**
	 * 获得需要推送的订单
	 * @Title: getNeedPushFinishOrder 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return List<OrderDto>    返回类型 
	 * @throws
	 */
	public List<OrderDto>getNeedPushFinishOrderForPage(String startTime,String endTime,Integer start,Integer limit)throws Exception;

	/**
	 * 查询已开单的且时间已过了预定时间的订单
	 * @return
	 * @throws Exception
	 */
	List<String> getOrderIdsByCondition() throws Exception;
	
	/**
	 * 更新订单状态
	 * @param orderId
	 * @param status
	 * @throws Exception
	 */
	void updateOrderStatus(String orderId, int status) throws Exception;

	/**
	 * 更新订单表的分账信息
	 * @param map
	 */
	void updateorderSettleMsg(Map<String, Object> map);
	
	/**
	 * 查询一点管家订单详情
	 * @Title: queryIdgjOrderDetail 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception
	 * @return OrderDto    返回类型 
	 * @throws
	 */
	OrderDto queryIdgjOrderDetail(String orderId)throws Exception;

	/**
	 * 获取订单列表，统计金额
	 * @param param
	 * @return
	 */
	Map getAllOrderListAmountState(Map<String, Object> param)throws Exception;

	/**
	 * 统计相关订单的支付金额
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Map getPayAmountStateByOrderIds(Map<String, Object> param)throws Exception;

	/**
	 * 获取订单列表总记录
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Integer getAllOrderListCount(Map<String, Object> param)throws Exception;

	/**
	 * 获取订单列表详情
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getAllOrderListDetail(Map<String, Object> param)throws Exception;

	/**
	 * 按日统计获取订单列表总记录数
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Integer getDayOrderStatCount(Map<String, Object> param)throws Exception;

	/**
	 * 按日统计获取订单列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getDayOrderStatDetail(Map<String, Object> param)throws Exception;

	/**
	 * 按日统计获取订单列表线上、现金、POS支付
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getDayOrderPayStat(Map<String, Object> param)throws Exception;
	
	List<String> getNotSettleOrderIds(Long shopId,Integer limit,Integer pageSize) throws Exception;
	
	void updatePlatformServePrice(Double serveMoney, String orderId)throws Exception;
	
	/**
     * 更新订单参与结算价格
     * 
     * @param order
     * @return
     * @throws Exception
     */
    int updateOrderRealSettlePrice(String orderId, Double orderRealSettlePrice, Double sendRedPacketMoney) throws Exception;

	int getConsumeShopMembersCount(Map<String, Object> resultMap);
	
	List<OrderDto> queryOrderByShopIdAndOrderCode(Long shopId, String payCode, Integer payStatus, Integer payType, Integer page, Integer pageSize);
	
	int countOrderByShopIdAndOrderCode(Long shopId, String payCode, Integer payStatus, Integer payType);

	Map<String, Object> getOrderCountAndSettleMoney(Map<String, Object> paramMap);

	Map<String, Object> getRedPackOrderDetail(String userId) throws Exception;

	Double getShopMemberSettlePriceSum(Map<String, Object> paramMap);

	List<Map<String, Object>> shopIncomeStatByTimePeriod(Map<String, Object> searchParams);

	int shopIncomeStatByTimePeriodCount(Map<String, Object> searchParams);

}
