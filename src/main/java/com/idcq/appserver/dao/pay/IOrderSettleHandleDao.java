package com.idcq.appserver.dao.pay;

import com.idcq.appserver.utils.settle.OrderSettleUserAmountDto;

public interface IOrderSettleHandleDao {
	
	OrderSettleUserAmountDto queryUserSettleInfo(Long orderId)throws Exception;
}
