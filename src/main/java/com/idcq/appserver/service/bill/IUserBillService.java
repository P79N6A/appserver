package com.idcq.appserver.service.bill;

import com.idcq.appserver.dto.order.OrderDto;

import java.util.List;

public interface IUserBillService {

	void insertUserBill(Long userId,Long transactionId,Integer billDirection,
				Double payAmount,Double accountAfterAmount,
				Double userAcountAmount,OrderDto order,String billType,
				Integer orderPayType,String billDesc,
				Integer userBillType,Integer accountType, Long agentId) throws Exception;

	List<Integer> getMyRewardType(Long userId, String[] accountTypes);
}
