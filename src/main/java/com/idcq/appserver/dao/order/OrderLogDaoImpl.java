package com.idcq.appserver.dao.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.OrderLogDto;
@Repository
public class OrderLogDaoImpl extends BaseDao<OrderLogDto> implements
		IOrderLogDao {

	public int saveOrderLog(OrderLogDto orderLogDto) throws Exception {
		return super.insert(generateStatement("saveOrderLog"), orderLogDto);
	}

	public int batchSaveOrderLogs(List<OrderLogDto> OrderLogDtos)
			throws Exception {
		return super.insert(generateStatement("batchSaveOrderLogs"), OrderLogDtos);
	}

	public OrderLogDto queryEntityByOrderIdDescLimit1(String orderId) throws Exception  {
		return (OrderLogDto) super.selectOne(generateStatement("queryEntityByOrderIdDescLimit1"), orderId);
	}

	public List<OrderLogDto> queryEntitysByOrderId(String orderId) throws Exception  {
		return (List<OrderLogDto>)super.findList(generateStatement("queryEntitysByOrderId"), orderId);
	}

	public OrderLogDto getOrderLastStatus(String orderId) throws Exception {
		return (OrderLogDto)super.selectOne(generateStatement("getOrderLastStatus"), orderId);
	}	
	
}
