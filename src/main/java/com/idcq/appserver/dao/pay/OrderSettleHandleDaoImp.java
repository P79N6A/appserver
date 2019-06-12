package com.idcq.appserver.dao.pay;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.utils.settle.OrderSettleUserAmountDto;
@Repository
public class OrderSettleHandleDaoImp extends BaseDao<OrderSettleUserAmountDto>implements IOrderSettleHandleDao{

	@Override
	public OrderSettleUserAmountDto queryUserSettleInfo(Long orderId)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("orderId", orderId);
		return (OrderSettleUserAmountDto)selectOne(generateStatement("queryUserSettleInfo"), params);
	}

}
