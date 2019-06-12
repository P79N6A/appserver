package com.idcq.appserver.service.bill;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.dao.bill.IUserXBillDao;
import com.idcq.appserver.dto.bill.UserXBillDto;

@Service
public class UserXBillServiceImpl implements IUserXBillService {
	
	@Autowired
	private IUserXBillDao userXBillDao;
	
	@Override
	public void insertUserXBill(Long userId, Long uccId,
								String orderId, Integer orderPayType,
								Integer billType, Double payMoney, 
								Double beforUsedAccountAmount, 
								String billTitle,String billDesc)
								throws Exception {
		
		UserXBillDto userXBill = getUserXBillDto(userId, uccId, 
												orderId, orderPayType, 
												billType, payMoney, 
												beforUsedAccountAmount, 
												billTitle, billDesc);
		
		
		userXBillDao.insertUserXBillDao(userXBill);
	}

	private UserXBillDto getUserXBillDto(Long userId, Long uccId,
										String orderId, Integer orderPayType,
										Integer billType, Double payMoney, 
										Double beforUsedAccountAmount, 
										String billTitle,String billDesc) {
		
		UserXBillDto userXBill = new UserXBillDto();
		userXBill.setUserId(userId);
		userXBill.setUccId(uccId);
		userXBill.setOrderPayType(orderPayType);
		userXBill.setOrderId(orderId);
		userXBill.setMoney(-payMoney);
		userXBill.setCreateTime(new Date());
		userXBill.setBillType(billType);
		
		if(!StringUtils.isEmpty(billTitle)){
			userXBill.setBillTitle(billTitle);
		}else{
			userXBill.setBillTitle("消费"+payMoney);
		}
		
		userXBill.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());
		userXBill.setBillDesc(billDesc);
		userXBill.setAccountAmount(beforUsedAccountAmount); 
		return userXBill;
	}
}
