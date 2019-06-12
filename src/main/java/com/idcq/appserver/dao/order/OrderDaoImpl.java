package com.idcq.appserver.dao.order;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.enums.OrderStatusEnum;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.utils.DateUtils;
/**
 * 我的订单dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class OrderDaoImpl extends BaseDao<OrderDto>implements IOrderDao{

	public int saveTestOrderId(String orderId) throws Exception {
		return super.insert(generateStatement("saveTestOrderId"),orderId);
	}

	public List<OrderDto> getOrderList(OrderDto shop, int page, int pageSize)
			throws Exception {
		return null;
	}

	public OrderDto saveOrder(OrderDto order) throws Exception {
		super.insert(generateStatement("saveOrder"),order);
		return order;
	}

	public List<Map<String, Object>> getSeatOrder(Map<String, Object> requestMap) throws Exception {
		return (List)super.findList(generateStatement("getSeatOrder"),requestMap);
	}
	public OrderDto getOrderById(String orderId) throws Exception {
		return (OrderDto)super.selectOne(generateStatement("getOrderById"),orderId);
	}
	
	public int updateOrder(OrderDto order) throws Exception {
		int flag = super.update(generateStatement("updateOrder"), order);
		return flag ;
	}
	
	public int updateUserId(Map<String, String> updateParam) throws Exception {
		return super.update(generateStatement("updateUserId"), updateParam);
	}
	public int queryOrderExists(String orderId) throws Exception {
		return (Integer)super.selectOne(generateStatement("queryOrderExists"),orderId);
	}

	public int updateOrderPayed(String orderId) throws Exception {
		return super.update(generateStatement("updateOrderPayed"),orderId);
	}
	
	

	public Map getOrderGoodsSettleSetting(Map param) throws Exception {
		return (Map) this.selectOne("getOrderGoodsSettleSetting", param);
	}

	public OrderDto getOrderMainById(String orderId) throws Exception {
		return (OrderDto)super.selectOne(generateStatement("getOrderMainById"), orderId);
	}

	public int getNumberByOrder(String orderId) throws Exception {
		int num = 0; 
		Integer numInt= (Integer) super.selectOne(generateStatement("getNumberByOrder"),orderId);
	    return numInt == null ? num : numInt;
	}

	public Integer getOrderStatusById(String orderId) throws Exception {
//		OrderDto pOrder = null;
//		//查缓存
//		String orderRedis = DataCacheApi.get(CommonConst.KEY_ORDER+orderId);
//		if(!StringUtils.isBlank(orderRedis)){
//			pOrder = (OrderDto)JacksonUtil.jsonToObject(orderRedis, OrderDto.class, null);
//			return pOrder.getOrderStatus();
//		}else{
//			//查库
//			pOrder = (OrderDto)super.selectOne(generateStatement("getOrderMainById"), orderId);
//			if(pOrder != null){
//				String redisValue = JacksonUtil.objToString(pOrder);
//				//入缓存
//				DataCacheApi.set(CommonConst.KEY_ORDER+orderId, redisValue);
//				return pOrder.getOrderStatus();
//			}else{
//				return null;
//			}
//		}
		return (Integer)super.selectOne(generateStatement("getOrderStatusById"), orderId);
	}

	public List<Map<String, Object>> getOrderByStatus(Integer orderStatus) {
		return (List)super.findList(generateStatement("getOrderByStatus"), orderStatus);
	}

	public int updateOrderStatusById(String orderId) throws Exception {
		return super.update(generateStatement("updateOrderStatusById"),orderId);
	}

	public Integer getOrderStatus(String orderId) throws Exception {
		return (Integer)super.selectOne(generateStatement("getOrderStatus"), orderId);
	}

	public List<Map<String, Object>> getOrder8List(Long shopId) throws Exception {
		Map<String, Object> map  = new HashMap<String, Object>();
		DateUtils.getCurrentDate(DateUtils.DATETIME_FORMAT);
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date());
	    //1天内信息
	    c.add(Calendar.DATE, -1);
	    Date startTime = c.getTime();
		map.put("shopId", shopId);
		map.put("startTime",startTime);
		return (List)super.findList(generateStatement("getOrder8List"), map);
	}
	public static void main(String[] args) throws ParseException {
		DateUtils.getCurrentDate(DateUtils.DATETIME_FORMAT);
		Calendar c = Calendar.getInstance();
	    c.setTime(new Date());
	    //1天内信息
	    c.add(Calendar.DATE, -1);
	    Date startTime = c.getTime();
	    System.out.println(startTime);
	}

	@Override
	public void delOrder(String orderId) {
		delete(generateStatement("delOrder"), orderId);
	}
	
	public void delOrderGoods(String orderId){
		delete(generateStatement("delOrderGoods"), orderId);
	}

	@Override
	public Map<String, Object> getDistributionTakeoutSetting(Long shopId, Integer orderSceType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		// `1dcq_order`订单场景分类：1（到店点菜订单,非外卖订单），2（外卖订单），3（服务订单），4（商品订单）
		// 1dcq_distribution_takeout_setting 设置类型，配送=1/到店=2/外卖=3
		map.put("orderServiceType", orderSceType+1);
		return (Map<String, Object>)selectOne(generateStatement("getDistributionTakeoutSetting"), map);
	}

	@Override
	public Map<String, Object> getUserReserveTime(String orderId) {
		return (Map<String, Object>)selectOne(generateStatement("getUserReserveTime"), orderId);
	}

	public int updateOrderDeleteType(String orderId, Integer deleteType) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId", orderId);
		map.put("deleteType", deleteType);
		return super.update(generateStatement("updateOrderDeleteType"), map);
	}

	public List<OrderDto> getOrderDtoByStatus(Integer orderStatus) throws Exception {
		return super.findList(generateStatement("getOrderDtoByStatus"), orderStatus);
	}

	@Override
	public List<OrderDto> getNeedAutoFinishOrder(String lastUpdateTime)
			throws Exception {
		return findList(generateStatement("getNeedAutoFinishOrder"),lastUpdateTime);
	}
	
	/**
	 * 获得需要推送给用户者的订单
	 * 24小时从已预订/已开单自动变为已完成，
	 * 在22小时时，自动
	 * @Title: getNeedPushFinishOrderForPage 
	 * @param @param lastUpdateTime
	 * @param @param start
	 * @param @param limit
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<OrderDto>getNeedPushFinishOrderForPage(String startTime,String endTime,Integer start,Integer limit)throws Exception{
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("start", start);
		params.put("limit",limit);
		return findList(generateStatement("getNeedPushFinishOrderForPage"),params);
	}
	/**
	 * 批量修改订单状态
	 * @Title: batchUpdateOrderStatus 
	 * @param @param orderIdList
	 * @param @throws Exception  
	 * @throws
	 */
	public void batchUpdateOrderStatus(List<String> orderIdList, Integer status) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("status", status);
		params.put("orderIdList", orderIdList);
        params.put("serverLastTime", System.currentTimeMillis());
		update(generateStatement("batchUpdateOrderStatus"), params);
	}

	@Override
	public List<String> getOrderIdsByCondition() throws Exception {
		return (List)findList("getOrderIdsByCondition");
	}

	@Override
	public void updateOrderStatus(String orderId, int status) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("orderId", orderId);
		params.put("status", status);
		// 已完成，则需更新完成时间
		if(status == OrderStatusEnum.SETTLE.getValue()){
			params.put("orderFinishTime", new Date());
		}
		params.put("status", status);
		params.put("serverLastTime", System.currentTimeMillis());
		update(generateStatement("updateOrderStatus"), params);
	}

	@Override
	public void updateorderSettleMsg(Map<String, Object> params) {
		update(generateStatement("updateorderSettleMsg"), params);
	}
	
	/**
	 * 查询 一点管家订单详情
	 * @Title: queryIdgjOrderDetail 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public OrderDto queryIdgjOrderDetail(String orderId) throws Exception {
		return (OrderDto)super.selectOne(generateStatement("queryIdgjOrderDetail"), orderId);
	}

	public Map getAllOrderListAmountState(Map<String, Object> param) throws Exception {
		return (Map) super.selectOne(generateStatement("getAllOrderListAmountState"), param);
	}

	public Map getPayAmountStateByOrderIds(Map<String, Object> param)
			throws Exception {
		return (Map) super.selectOne(generateStatement("getPayAmountStateByOrderIds"), param);
	}

	public Integer getAllOrderListCount(Map<String, Object> param)throws Exception {
		return (Integer) super.selectOne(generateStatement("getAllOrderListCount"), param);
	}

	public List<Map<String, Object>> getAllOrderListDetail(Map<String, Object> param) throws Exception {
		return (List)super.findList(generateStatement("getAllOrderListDetail"), param);
	}

	public Integer getDayOrderStatCount(Map<String, Object> param)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getDayOrderStatCount"), param);
	}

	public List<Map<String, Object>> getDayOrderStatDetail(
			Map<String, Object> param) throws Exception {
		return (List)super.findList(generateStatement("getDayOrderStatDetail"), param);
	}

	public List<Map<String, Object>> getDayOrderPayStat(
			Map<String, Object> param) throws Exception {
		return (List)super.findList(generateStatement("getDayOrderPayStat"), param);
	}
	   
	public List<String> getNotSettleOrderIds(Long shopId,Integer limit,Integer pageSize) throws Exception {
		 Map<String,Object> params = new HashMap<String,Object>();
	        params.put("shopId", shopId);
	        params.put("limit", limit);
	        params.put("pageSize", pageSize);
	    return super.getSqlSession().selectList(generateStatement("getNotSettleOrderIds"), params);
	}
	
	public void updatePlatformServePrice(Double serveMoney, String orderId)throws Exception
	{
	    Map<String,Object> params = new HashMap<String,Object>();
        params.put("orderId", orderId);
        params.put("serveMoney", serveMoney);
	    super.update(generateStatement("updatePlatformServePrice"), params);
	}

    @Override
    public int updateOrderRealSettlePrice(String orderId, Double orderRealSettlePrice, Double sendRedPacketMoney) throws Exception {
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("orderId", orderId);
        params.put("orderRealSettlePrice", orderRealSettlePrice);
        params.put("sendRedPacketMoney", sendRedPacketMoney);
        return super.update(generateStatement("updateOrderRealSettlePrice"), params);
    }

	@Override
	public int getConsumeShopMembersCount(Map<String, Object> param) {
		return (Integer) super.selectOne(generateStatement("getConsumeShopMembersCount"), param);
	}

    @Override
    public List<OrderDto> queryOrderByShopIdAndOrderCode(Long shopId, String payCode,Integer payStatus, Integer payType, Integer page, Integer pageSize)
    {
        Map<String, Object> searchCondition = new HashMap<String, Object>(8,1);
        if(null != payStatus){
            searchCondition.put("payStatus", payStatus);
        }
        if(null != payType){
            searchCondition.put("payType", payType);
        }
        searchCondition.put("shopId", shopId);
        searchCondition.put("payCode", payCode);
        searchCondition.put("m", (page-1)*pageSize);
        searchCondition.put("n", pageSize);
        return super.getSqlSession().selectList(generateStatement("queryOrderByShopIdAndOrderCode"), searchCondition);
    }

    @Override
    public int countOrderByShopIdAndOrderCode(Long shopId, String payCode, Integer payStatus, Integer payType)
    {
        Map<String, Object> searchCondition = new HashMap<String, Object>(8,1);
        if(null != payStatus){
            searchCondition.put("payStatus", payStatus);
        }
        if(null != payType){
            searchCondition.put("payType", payType);
        }
        searchCondition.put("shopId", shopId);
        searchCondition.put("payCode", payCode);
//        Object obj = super.getSqlSession().selectOne(generateStatement("countOrderByShopIdAndOrderCode"), searchCondition);
//        int res = obj == null ? 0 : (int)obj;
        return (Integer)super.getSqlSession().selectOne(generateStatement("countOrderByShopIdAndOrderCode"), searchCondition);
    }
    
	@Override
	public Map<String, Object> getOrderCountAndSettleMoney(
			Map<String, Object> paramMap) {
		return (Map) this.selectOne("getOrderCountAndSettleMoney", paramMap);
	}

	public Map<String, Object> getRedPackOrderDetail(String orderId) throws Exception{
		return (Map) this.selectOne("getRedPacketOrderDetail", orderId);
	}

	@Override
	public Double getShopMemberSettlePriceSum(Map<String, Object> paramMap) {
		return (Double) super.selectOne(generateStatement("getConsumeShopMembersAmount"), paramMap);
	}

	@Override
	public List<Map<String, Object>> shopIncomeStatByTimePeriod(
			Map<String, Object> searchParams) {
		return (List)super.findList(generateStatement("shopIncomeStatByTimePeriod"),searchParams);
	}

	@Override
	public int shopIncomeStatByTimePeriodCount(Map<String, Object> searchParams) {
		return (int) super.selectOne(generateStatement("shopIncomeStatByTimePeriodCount"), searchParams);
	}
	
}
