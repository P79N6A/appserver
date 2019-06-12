package com.idcq.appserver.service.bill;

import com.idcq.appserver.dto.order.OrderDto;

public interface IPlatformBillService {

	void insertPlatformBill(Integer billDirection,Double money,
				Integer moneySource,OrderDto order,
				Long transactionId,String billType,
				String billDesc,Integer paltformBillType,
				Integer billStatus) throws Exception;
}
