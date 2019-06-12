package com.idcq.appserver.dao.pay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pay.OrderGoodsSettle;
import com.idcq.appserver.dto.pay.OrderGoodsSettleLog;
/**
 * 订单结算
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class OrderGoodsSettleDaoImpl extends BaseDao<OrderGoodsSettle>implements OrderGoodsSettleDao{
	


	public void detailOrderGoodsSettle(Long userId, Long shopId,
			Long goodsId, String orderId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId );
		map.put("shopId",shopId );
		map.put("goodsId",goodsId );
		map.put("orderId",orderId );
		super.update(generateStatement("detailOrderGoodsSettle"), map);
	}


	public List<OrderGoodsSettle> getOrderGoodsSettle(Long userId, Long shopId,
			Long goodsId, String orderId,Integer skip) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId );
		map.put("shopId",shopId );
		map.put("goodsId",goodsId );
		map.put("orderId",orderId );
		map.put("skip",skip );
		return (List)super.findList(generateStatement("getOrderGoodsSettle"), map);
	}


	public void updateOrderGoodsSettle(Long userId, Long shopId,
			Long goodsId, String orderId,int type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userId );
		map.put("shopId",shopId );
		map.put("goodsId",goodsId );
		map.put("orderId",orderId );
		map.put("oType",type );
		super.update(generateStatement("updateOrderGoodsSettle"), map);
	}


	public int addOrderGoodsSettleLog(OrderGoodsSettleLog goodsSettleLog) {
		return super.insert(generateStatement("addOrderGoodsSettleLog"),goodsSettleLog);
	}

	@Override
	public int saveOrderGoodsSettle(OrderGoodsSettle orderGoodsSettle) throws Exception {
		return this.insert("saveOrderGoodsSettle", orderGoodsSettle);
	}


	@Override
	public double getPlatformTotalPrice(String orderId) throws Exception {
		return (double) this.selectOne("getPlatformTotalPrice", orderId);
	}


	@Override
	public void updateOrderGoodsSettleShop(Long userid, Long shopid,
			Long goodsid, String orderid, int type) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userid );
		map.put("shopId",shopid );
		map.put("goodsId",goodsid );
		map.put("orderId",orderid );
		map.put("oType",type );
		super.update(generateStatement("updateOrderGoodsSettleShop"), map);		
	}


	@Override
	public List<OrderGoodsSettle> getOrderGoodsSettleShop(Long userid,
			Long shopid, Long goodsid, String orderid,Integer skip) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId",userid );
		map.put("shopId",shopid );
		map.put("goodsId",goodsid );
		map.put("orderId",orderid );
		map.put("skip",skip);
		return (List)super.findList(generateStatement("getOrderGoodsSettleShop"), map);
	}


	@Override
	public OrderGoodsSettle getOrderGoodsSettleMoney(String orderId)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("orderId",orderId);
		return (OrderGoodsSettle) super.selectOne("getOrderGoodsSettleMoney", map);
	}
	
	@Override
	public void updateOrderGoodsSettle(OrderGoodsSettle orderGoodsSettle) throws Exception {
		super.update(generateStatement("updateOrderGoodsSettleStatus"), orderGoodsSettle);		
	}
	
}
