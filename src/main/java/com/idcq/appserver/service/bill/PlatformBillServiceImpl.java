package com.idcq.appserver.service.bill;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.order.OrderDto;

@Service
public class PlatformBillServiceImpl implements IPlatformBillService {

	@Autowired
	public IPlatformBillDao platformBillDao;
	
	@Override
	public void insertPlatformBill(Integer billDirection,Double money,
			   						Integer moneySource,OrderDto order,
		   							Long transactionId,String billType,
		   							String billDesc,Integer paltformBillType,
		   							Integer billStatus) throws Exception {
		
		PlatformBillDto platformBill = buildPlatformBillFor3rdPay(billDirection, money, 
																	moneySource, order, 
																	transactionId, billType, 
																	billDesc, paltformBillType, 
																	billStatus);
		
		platformBillDao.insertPlatformBill(platformBill);
	}
	
    private PlatformBillDto buildPlatformBillFor3rdPay(Integer billDirection,Double money,
    												   Integer moneySource,OrderDto order,
    												   Long transactionId,String billType,
    												   String billDesc,Integer paltformBillType,
    												   Integer billStatus) throws Exception {
    	
        PlatformBillDto platformBill = new PlatformBillDto();
        platformBill.setPlatformBillType(paltformBillType);
        platformBill.setBillType(billType);
        platformBill.setBillDirection(billDirection);
        platformBill.setBillStatus(billStatus);
        platformBill.setMoney(billDirection == 1 ? money : - money);
        platformBill.setTransactionId(transactionId);
        platformBill.setOrderId(order.getOrderId());
        platformBill.setGoodsSettlePrice(order.getSettlePrice());
        platformBill.setCreateTime(new Date());
        platformBill.setSettleTime(new Date());
        platformBill.setBillDesc(billDesc);
        if (order.getUserId() == null){
        	platformBill.setConsumerUserId(Long.valueOf(0));
        }else {
        	platformBill.setConsumerUserId(order.getUserId());
		}
        
        if (order.getMobile() == null){
        	platformBill.setConsumerMobile("0");
        }else {
        	platformBill.setConsumerMobile(order.getMobile());
		}
        if(null!=order.getGoodsNumber()){
            platformBill.setGoodsNumber(Double.valueOf(order.getGoodsNumber()));
        }
        platformBill.setMoneySource(moneySource);
        return platformBill;
    }
}
