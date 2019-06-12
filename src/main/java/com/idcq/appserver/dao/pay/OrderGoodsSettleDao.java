package com.idcq.appserver.dao.pay;

import java.util.List;

import com.idcq.appserver.dto.pay.OrderGoodsSettle;
import com.idcq.appserver.dto.pay.OrderGoodsSettleLog;


public interface OrderGoodsSettleDao {
	/**
	 * 处理订单结算，调用后台储存过程
	 * 
	 * @throws Exception
	 */
	public void detailOrderGoodsSettle(Long userid, Long shopid,
			Long goodsid, String orderid) throws Exception;
	/**
	 * 处理订单分账，调用后台储存过程
	 * 
	 * @throws Exception
	 */
	public void updateOrderGoodsSettle(Long userid, Long shopid,
			Long goodsid, String orderid,int type) throws Exception;
	/**
	 * 处理订单分账，调用后台储存过程 TODO shop
	 * 
	 * @throws Exception
	 */
	public void updateOrderGoodsSettleShop(Long userid, Long shopid,
			Long goodsid, String orderid,int type) throws Exception;
	
	/**
	 * 获取处理订单结算信息
	 * 
	 * @throws Exception
	 */
	public List<OrderGoodsSettle> getOrderGoodsSettle(Long userid, Long shopid,
			Long goodsid, String orderid,Integer skip) throws Exception;
	/**
	 * 获取处理订单结算信息Shop
	 * 
	 * @throws Exception
	 */
	public List<OrderGoodsSettle> getOrderGoodsSettleShop(Long userid, Long shopid,
			Long goodsid, String orderid,Integer skip) throws Exception;
	/**
	 * 
	 * @param goodsSettleLog
	 * @return
	 */
	public int addOrderGoodsSettleLog(OrderGoodsSettleLog goodsSettleLog);
	
	/**
	 * 商品结算记录
	 * 
	 * @param orderGoods
	 * @return
	 * @throws Exception
	 */
	int saveOrderGoodsSettle(OrderGoodsSettle orderGoodsSettle) throws Exception;
	
	public double getPlatformTotalPrice(String orderId) throws Exception;
	
	/**
	 *  根据订单ID查询各角色分成的总金额
	 * @param orderid
	 * @return
	 * @throws Exception
	 */
	public OrderGoodsSettle getOrderGoodsSettleMoney(String orderId) throws Exception;
	
	public void updateOrderGoodsSettle(OrderGoodsSettle orderGoodsSettle) throws Exception;
	
}
