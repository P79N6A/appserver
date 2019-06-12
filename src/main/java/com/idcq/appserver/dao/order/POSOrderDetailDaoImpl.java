package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.POSOrderDetailDto;

@Repository
public class POSOrderDetailDaoImpl extends BaseDao<POSOrderDetailDto> implements IPOSOrderDetailDao {

	
	@Override
	public List<POSOrderDetailDto> getOrderListDetail(
			Map<String, Object> params, int page, int pageSize) {
		params.put("n", (page-1)*pageSize);                   
		params.put("m", pageSize);
		return super.findList(generateStatement("getOrderListDetail"), params);
	}

	@Override
	public int getOrderListDetailCount(Map<String, Object> params)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getOrderListDetailCount"), params);
	}
	
	

}
