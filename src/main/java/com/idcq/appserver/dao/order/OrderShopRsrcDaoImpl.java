package com.idcq.appserver.dao.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;

/**
 * 商铺资源预定dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午4:18:12
 */
@Repository
public class OrderShopRsrcDaoImpl extends BaseDao<OrderShopRsrcDto> implements IOrderShopRsrcDao{
	
	public int getOrderShopRsrcCount(String orderId) throws Exception {
		return (Integer)super.selectOne("getOrderShopRsrcCount", orderId);
	}

	public OrderShopRsrcDto getOrderShopRsrcLimitOne(String orderId)
			throws Exception {
		return (OrderShopRsrcDto)super.selectOne(generateStatement("getOrderShopRsrcLimitOne"), orderId);
	}

	public void saveOrderShopRsrc(OrderShopRsrcDto osrc) throws Exception {
		super.insert(generateStatement("saveOrderShopRsrc"),osrc);
	}

	public int delOrderShopRsrc(String orderId) throws Exception {
		return super.delete(generateStatement("delOrderShopRsrc"), orderId);
	}
	
	public List<OrderShopRsrcDto> getOrderGroupByshopId(Map param) throws Exception {
		return super.findList(generateStatement("getOrderGroupByshopId"),param);
	}

	public List<OrderShopRsrcDto> getOrderGroupsByOrderId(String orderId)
			throws Exception {
		return super.findList(generateStatement("getOrderGroupsByOrderId"),orderId);
	}

	public long queryRsrcGroupNumOfUsed(long shopId, long groupId,
			long intevalId) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("groupId", groupId);
		map.put("intevalId", intevalId);
		Long num = (Long)super.selectOne(generateStatement("queryRsrcGroupNumOfUsed"), map);
		return num == null ? 0L : num;
	}
	
	public long queryRsrcGroupNumOfUsed2(long shopId, long bookRuleId,
			long intevalId,String reserveDate) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("bookRuleId", bookRuleId);
		map.put("intevalId", intevalId);
		map.put("reserveDate", reserveDate);
		Long num = (Long)super.selectOne(generateStatement("queryRsrcGroupNumOfUsed2"), map);
		return num == null ? 0L : num;
	}

	@Override
	public Long getGroupIdByName(String groupName, Long shopId) {
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("groupName", groupName);
		map.put("shopId", shopId);
		return (Long) selectOne(generateStatement("getGroupIdByName"), map);
	}

	@Override
	public List<Map<String, Object>> getIntevalByShopId(Long shopId) {
		return (List) findList(generateStatement("getIntevalByShopId"), shopId);
	}

	public int enableOrderShopRsrc(String orderId) throws Exception {
		return super.update(generateStatement("enableOrderShopRsrc"), orderId);
	}

	@Override
	public List<Map<String, Object>> getOrderShopResourceByOrderId(
			String orderId) throws Exception {
		return (List) findList(generateStatement("getOrderShopResourceByOrderId"), orderId);
	}

	@Override
	public Map<String, Object> getIntevalTimeById(Long intevalId)
			throws Exception {
		return (Map) selectOne(generateStatement("getIntevalTimeById"), intevalId);
	}

	@Override
	public Map<String, Object> getBookTypeById(Long bookRuleId)
			throws Exception {
		return (Map) selectOne(generateStatement("getBookTypeById"), bookRuleId);
	}

	public List<Map<String, Object>> queryOrderShopResource(Map<String, Object> param)  throws Exception {
		return (List) findList(generateStatement("queryOrderShopResource"), param);
	}

	public int batchUpdateOrderShopResource(List<Map<String, Object>> list)
			throws Exception {
		return update("batchUpdateOrderShopResource", list);
	}

	@Override
	public Long getResourceIdByOrderId(String orderId) throws Exception {
		return (Long) selectOne(generateStatement("getResourceIdByOrderId"), orderId);
	}

	/**
	 * 获取商铺订单
	 * @Title: getShopBookOrders 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<OrderShopRsrcDto> getShopBookOrders(Long shopId,String bookTimeFrom,String bookTimeTo)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("bookTimeFrom",bookTimeFrom);
		params.put("bookTimeTo", bookTimeTo);
		params.put("shopId",shopId);
		return findList(generateStatement("getShopBookOrders"), params);
	}

	@Override
	public Map<String, Object> getOrderResourceByOrderId(String orderId) {
		return (Map<String, Object>)selectOne(generateStatement("getOrderResourceByOrderId"), orderId);
	}

	@Override
	public int updateStatusByOrderId(String orderId,Integer status) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("status", status);
		return super.update("updateStatusByOrderId",map);
	}

	@Override
	public List<OrderShopRsrcDto> getNeedAutoFinishOrder(String queryTime) {
		return getSqlSession().selectList(generateStatement("getNeedAutoFinishOrder"), queryTime);
	}

	@Override
	public int updateStatusByOrderIdList(List<String> orderIdList,
			Integer status) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("status", status);
		params.put("orderIdList",orderIdList);
		return super.update(generateStatement("updateStatusByOrderIdList"), params);
	}

	@Override
	public String getResourceTypeByOrderId(String orderId) throws Exception {
		return (String)super.selectOne("getResourceTypeByOrderId", orderId);
	}

	@Override
	public Map<String, Object> getResourceByOrderId(String orderId)
			throws Exception {
		return (Map<String, Object>)super.selectOne("getResourceByOrderId", orderId);
	}

	public Long getTechIdByOrderId(String orderId,String resourceType) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("resourceType", resourceType);
		param.put("orderId", orderId);
		return (Long) super.selectOne(generateStatement("getTechIdByOrderId"), param);
	}

	public Integer getTechServerOrderSrc(Map param) throws Exception {
		return (Integer) super.selectOne(generateStatement("getTechServerOrderSrc"), param);
	}
	
	
	
}
