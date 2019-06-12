package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.order.OrderShopRsrcDto;

public interface IOrderShopRsrcDao {

	
	void saveOrderShopRsrc(OrderShopRsrcDto osrc) throws Exception;
	
	int delOrderShopRsrc(String orderId) throws Exception;
	
	/**
	 * 检查订单资源是否存在
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int getOrderShopRsrcCount(String orderId) throws Exception;
	
	/**
	 * 获取指定预定商铺订单的一条资源信息
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderShopRsrcDto getOrderShopRsrcLimitOne(String orderId) throws Exception;
	
	/**
	 * 启用已经预定的商铺资源
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int enableOrderShopRsrc(String orderId) throws Exception;
	
	/**
	 * 获取商铺各分组各时间段已定数量
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<OrderShopRsrcDto> getOrderGroupByshopId(Map param) throws Exception;
	
	/**
	 * 获取订单预定资源类型
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	String getResourceTypeByOrderId(String orderId) throws Exception;
	
	/**
	 * 获取指定的预定商品资源
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getResourceByOrderId(String orderId) throws Exception;
	
	/**
	 * 获取指定订单的预定资源组列表
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<OrderShopRsrcDto> getOrderGroupsByOrderId(String orderId) throws Exception;

	/**
	 * 查询指定商铺指定时间段某个资源组已经被预定的数量
	 * 
	 * @param shopId
	 * @param groupId
	 * @param intevalId
	 * @return
	 * @throws Exception
	 */
	long queryRsrcGroupNumOfUsed(long shopId,long groupId,long intevalId) throws Exception;
	
	/**
	 * 查询指定商铺指定时间段某个资源组已经被预定的数量
	 * 
	 * @param shopId
	 * @param bookRuleId
	 * @param intevalId
	 * @return
	 * @throws Exception
	 */
	long queryRsrcGroupNumOfUsed2(long shopId,long bookRuleId,long intevalId,String reserveDate) throws Exception;
	
	/**
	 * 根据资源组名称、商铺id查询资源组
	 * @param groupName
	 * @param shopId
	 * @return
	 */
	Long getGroupIdByName(String groupName,Long shopId) throws Exception;
	
	
	/**
	 * 根据shopid查询时间段信息
	 * @param groupName
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> getIntevalByShopId(Long shopId) throws Exception;
	/**
	 * 根据orderId查询订单资源信息
	 * @return
	 */
	List<Map<String, Object>> getOrderShopResourceByOrderId(String orderId) throws Exception;
	/**
	 * 查询时间信息
	 * @param intevalId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getIntevalTimeById(Long intevalId) throws Exception;
	/**
	 * 查询商铺预定规则表
	 * @param bookRuleId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getBookTypeById(Long bookRuleId)throws Exception;
	
	/**
	 * 查询订单预定资源
	 * <br/>
	 * <b>
	 * 	条件：
	 *   订单状态为已预订-0
	 *   订单预定资源状态-1
	 * </b>
	 * @param param
	 * @return
	 */
	List<Map<String, Object>> queryOrderShopResource(Map<String,Object> param)throws Exception;
	
	/**
	 * 批量修改订单预定资源提醒状态为已经提醒
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int batchUpdateOrderShopResource(List<Map<String,Object>> list)throws Exception;
	/**
	 * 根据orderId获取resourceId
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	Long getResourceIdByOrderId(String orderId)throws Exception;
	
	
	List<OrderShopRsrcDto>getShopBookOrders(Long shopId,String bookTimeFrom,String bookTimeTo)throws Exception;
	/**
	 * 根据orderid获取订单资源信息
	 * @param orderId
	 * @return
	 */
	Map<String, Object> getOrderResourceByOrderId(String orderId);
	
	/**
	 * 修改订单资源状态
	 * @return
	 * @throws Exception
	 */
	int updateStatusByOrderId(String orderId,Integer status) throws Exception;
	
	
	int updateStatusByOrderIdList(List<String> orderIdList,Integer status)throws Exception;
	
	/**
	 * 
	 * @Title: getNeedAutoFinishOrder 
	 * @param @param queryTime
	 * @param @return
	 * @return List<OrderShopRsrcDto>    返回类型 
	 * @throws
	 */
	List<OrderShopRsrcDto> getNeedAutoFinishOrder(String queryTime);
	
	/**
	 * 根据订单编号查询技师ID
	 * @param orderId
	 * @param resourceType
	 * @return
	 * @throws Exception
	 */
	Long getTechIdByOrderId(String orderId,String resourceType)throws Exception;
	
	/**
	 * 根据技师编号查询是否还有服务中的订单
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Integer getTechServerOrderSrc(Map param) throws Exception;
}
