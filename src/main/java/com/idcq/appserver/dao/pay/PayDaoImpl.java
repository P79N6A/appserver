package com.idcq.appserver.dao.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pay.PayDto;
/**
 * 我的订单daooo
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class PayDaoImpl extends BaseDao<PayDto>implements IPayDao{
	
	public Integer addOrderPay(PayDto payDto) throws Exception {
		return super.insert(generateStatement("addOrderPay"),payDto);
	}

	public List<PayDto> getOrderPayByIdAndGroupId(String orderId,String groupId) throws Exception {
		Map<String, String> param=new HashMap<String, String>();
		param.put("orderId", orderId);
		param.put("groupId", groupId);
		return (List<PayDto>)super.findList("getOrderPayByIdAndGroupId", param);
	}
	public void updateOrderPayAfterRdPaySuccess(PayDto payDto) throws Exception {
		super.update("updateOrderPayAfterPaySuccess", payDto);
	}
	public double getSumPayAmount(String orderId,Integer payeeType) throws Exception {
		Map param=new HashMap();
		param.put("orderId", orderId);
		param.put("payeeType", payeeType);
		return (Double)super.selectOne(generateStatement("getSumPayAmount"), param);
	}
	
    public double getSumPayAmount(String orderId,Integer payeeType, Integer payType) throws Exception {
        Map param=new HashMap();
        param.put("orderId", orderId);
        param.put("payeeType", payeeType);
        param.put("payType", payType);
        return (Double)super.selectOne(generateStatement("getSumPayAmount"), param);
    }

	public PayDto getDBOrderPayById(Long orderPayId) throws Exception {
		return (PayDto) super.selectOne(generateStatement("getOrderPayById"), orderPayId);
	}
	public List<PayDto> getOrderPayList(String orderId,Integer payStatus) throws Exception {
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("orderId",orderId);
		parms.put("payStatus",payStatus);
		return super.findList("getOrderPayList", parms);
	}

	public void deletePayByOrderPayId(Long orderPayId) throws Exception {
		super.delete("deletePayByOrderPayId", orderPayId);
	}

	@Override
	public void groupOrders(String orderGroupId,String orderId) throws Exception {
		Map param=new HashMap();
		param.put("orderGroupId", orderGroupId);
		param.put("orderId", orderId);
		super.insert("groupOrders", param);
	}

	@Override
	public List<Map> queryOrderGroupById(String orderGroupId) throws Exception {
		return (List)this.findList("queryOrderGroupById", orderGroupId);
	}
	
	public List<Map> queryOrderGroupByOrderId(String orderGroupId)
			throws Exception {
		return (List)this.findList("queryOrderGroupByOrderId", orderGroupId);
	}

	@Override
	public double getSumOrderGroupAmount(String orderGroupId) throws Exception {
		return (Double)super.selectOne(generateStatement("getSumOrderGroupAmount"), orderGroupId);
	}

	public int checkOrderIsPayByCash(String orderId,Long uccId,Integer orderPayType) throws Exception {
		Map param = new HashMap();
		param.put("orderId", orderId);
		param.put("uccId", uccId);
		param.put("orderPayType", orderPayType);
		Integer num = (Integer)super.selectOne(generateStatement("checkOrderIsPayByCash"), param);
		return num == null ? 0 : num;
	}

	public Long getShopIdByOrderId(String orderId) throws Exception {
		return (Long) super.selectOne(generateStatement("getShopIdByOrderId"), orderId);
	}

	@Override
	public List<Map> getOrderPayDetail(String orderId)
			throws Exception {
		
		return (List)findList("getOrderPayByOrderId", orderId);
	}
	@Override
	public Long getOrderPayIdByPayId(Long transactionId) throws Exception {
		return (Long) super.selectOne(generateStatement("getOrderPayIdByPayId"), transactionId);
	}
	@Override
	public Map<String, Object> getAmountByOrderIdAndPayeeType(
			Map<String, Object> map) throws Exception {
		return (Map)super.selectOne("getAmountByOrderIdAndPayeeType", map);
	}
	@Override
	public Map<String, Object> getAmountByOrderIdAndPayType(
			Map<String, Object> map) throws Exception {
		return (Map)super.selectOne("getAmountByOrderIdAndPayType", map);
	}

	public List<Map<String, Object>> getPayLogByOrderId(String orderId)
			throws Exception {
		return (List) findList(generateStatement("getPayLogByOrderId"),orderId);
	}

	public Integer getMaxPayIndex(String orderId) throws Exception {
		return (Integer) super.selectOne(generateStatement("getMaxPayIndex"), orderId);
	}

	public List<Map> getAllOrderPayDetail(List<String> orderIds) throws Exception {
		return (List)super.findList(generateStatement("getAllOrderPayDetail"), orderIds);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.pay.IPayDao#deletePayByOrderId()
     */
    @Override
    public void deletePayByOrderId(String orderId) throws Exception
    {
        super.delete("deletePayByOrderPayId", orderId);
        
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.pay.IPayDao#getPayResult()
     */
    @Override
    public Map<String, Object> getPayResult(Map<String, Object> map) throws Exception
    {
        return (Map<String, Object>) super.selectOne(generateStatement("getPayResult"), map);

    }

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.pay.IPayDao#getIncomeTotalMoney(java.lang.Long, java.lang.Integer, java.lang.Integer, java.util.Date, java.util.Date)
	 */
	@Override
	public Map<String, Object> getIncomeTotalMoney(Long shopId,
			Integer payStatus, Integer payeeType, Date startTime, Date endTime)
			throws Exception {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("shopId", shopId);
		parms.put("payStatus", payStatus);
		parms.put("payeeType", payeeType);
		parms.put("startTime", startTime);
		parms.put("endTime", endTime);
		
		return (Map<String, Object>)selectOne(generateStatement("getIncomeTotalMoney"), parms);
	}	
}
