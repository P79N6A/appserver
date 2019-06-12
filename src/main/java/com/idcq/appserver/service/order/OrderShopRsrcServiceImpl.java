package com.idcq.appserver.service.order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.order.IOrderShopRsrcDao;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.service.collect.ICollectService;

/**
 * 订单商铺资源service
 * 
 * @author Administrator
 * 
 * @date 2015年5月20日
 * @time 上午11:08:20
 */
@Service
public class OrderShopRsrcServiceImpl implements IOrderShopRsrcService{
	
	@Autowired
	public IOrderShopRsrcDao orderShopRsrcDao;
	
	@Autowired
	private ICollectDao collectDao;
	
	public int enableOrderShopRsrc(String orderId) throws Exception {
		return this.orderShopRsrcDao.enableOrderShopRsrc(orderId);
	}

	public OrderShopRsrcDto getOrderShopRsrcLimitOne(String orderId)
			throws Exception {
		return this.orderShopRsrcDao.getOrderShopRsrcLimitOne(orderId);
	}

	@Override
	public List<OrderShopRsrcDto> getShopBookOrders(Long shopId,
			String bookTimeFrom, String bookTimeTo) throws Exception {
		return orderShopRsrcDao.getShopBookOrders(shopId, bookTimeFrom, bookTimeTo);
	}

	@Override
	public List<OrderShopRsrcDto> getNeedAutoFinishOrder(String queryTime)
			throws Exception {
		return orderShopRsrcDao.getNeedAutoFinishOrder(queryTime);
	}

	                                                                                                                                               
	public void releaseShopResource(List<String> orderIdList) throws Exception {	
		orderShopRsrcDao.updateStatusByOrderIdList(orderIdList, CommonConst.OSRESOURCE_STATUS_INVALID);
		collectDao.updateShopResourceStatusByOrderIdList(orderIdList, CommonConst.RESOURCE_STATUS_NOT_IN_USE);
	}

	
}
