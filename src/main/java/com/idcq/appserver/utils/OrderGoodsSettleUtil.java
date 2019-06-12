package com.idcq.appserver.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.utils.queue.OrderGoodsSettleThread;
import com.idcq.appserver.utils.thread.ThreadPoolManager;
/**
 * 订单结算工具
 * @author chenyongxin
 *
 */
public class OrderGoodsSettleUtil{
	
	 private static Logger logger=Logger.getLogger(OrderGoodsSettleUtil.class);
	/**
	 * 调用存储过程处理订单结算
	 * 
	 * @param t TODO 订单组id情况
	 * @throws Exception
	 */
	public static void detailOrderGoodsSettle(String orderId,Integer orderPayType)
			throws Exception {
		//订单组
		if(1==orderPayType){
			IPayDao payDao  = BeanFactory.getBean(IPayDao.class);
			List<Map> listOrder = payDao.queryOrderGroupById(orderId);;
			for (int i = 0, len = listOrder.size(); i < len; i++) {
				String orderIdS = (String) listOrder.get(i).get("orderId");
				//结算
				detailSingleOrder(orderIdS);
			}
			
		}//单个订单
		else{
			detailSingleOrder(orderId);
		}
	}
	/**
	 * 处理单个订单
	 * @param orderId
	 * @return 
	 * @throws Exception 
	 */
	public static void detailSingleOrder(String orderId) throws Exception{
		IOrderDao orderDao = BeanFactory.getBean(IOrderDao.class);
		OrderDto orderDto = orderDao.getOrderById(orderId);
		if(orderDto!=null){
			//反结账订单不做结算处理
			if(CommonConst.REVERSE_SETTLE_FLAG!=orderDto.getSettleFlag().intValue()){
				ThreadPoolManager pool=ThreadPoolManager.getInstance();
				pool.execute(new OrderGoodsSettleThread(orderId));
			}
		}

		
	}
	
	/**
	 * 处理非会员订单
	 * @Title: detailSingleXorder 
	 * @param @param orderId
	 * @return void    返回类型 
	 * @throws
	 */
	public static void detailSingleXorder(String xorderId){
		OrderGoodsSettleThread orderGoodsSettleThread=new OrderGoodsSettleThread(xorderId);
		orderGoodsSettleThread.setOrderType(CommonConst.ORDER_TYPE_XORDER);
		ThreadPoolManager pool=ThreadPoolManager.getInstance();
		pool.execute(orderGoodsSettleThread);
	}
	
}
