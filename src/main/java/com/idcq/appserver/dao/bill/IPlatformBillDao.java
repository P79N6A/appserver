package com.idcq.appserver.dao.bill;

import java.util.List;

import com.idcq.appserver.dto.bill.PlatformBillDto;

public interface IPlatformBillDao {
	
	public int insertPlatformBill(PlatformBillDto platformBillDto)  throws Exception;
	
	int updatePlatformBill(PlatformBillDto platformBillDto)  throws Exception;
	
	int insertPlatformBillMiddle(PlatformBillDto platformBillDto)  throws Exception;
	
    List<PlatformBillDto> getPlatformBillMiddleByOrderId(String orderId) throws Exception;
    
    List<PlatformBillDto> getPlatformBillByOrderId(String orderId) throws Exception;
    
    void deletePlatformBillMiddle(String orderId) throws Exception;
}
