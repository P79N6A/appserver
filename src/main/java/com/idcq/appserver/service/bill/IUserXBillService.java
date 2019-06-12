package com.idcq.appserver.service.bill;

public interface IUserXBillService {

	 void insertUserXBill(Long userId, Long uccId,
				String orderId, Integer orderPayType,
				Integer billType, Double payMoney, 
				Double beforUsedAccountAmount, 
				String billTitle,String billDesc)
				throws Exception;
}
