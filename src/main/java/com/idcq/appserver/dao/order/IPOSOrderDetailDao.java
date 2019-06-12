package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.order.POSOrderDetailDto;


public interface IPOSOrderDetailDao {
	
	List<POSOrderDetailDto> getOrderListDetail(Map<String,Object> params, int page,int pageSize);
	
	int getOrderListDetailCount(Map<String, Object> params) throws Exception;
}
