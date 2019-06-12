package com.idcq.appserver.dao.order;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;

public interface IOrderGoodsDao {
	
	/**
	 * 新增订单商品
	 * 
	 * @param orderGoods
	 * @return
	 * @throws Exception
	 */
	int saveOrderGoods(OrderGoodsDto orderGoods) throws Exception;
	
	/**
	 * 批量保存订单商品列表
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int saveOrderGoodsBatch(List<OrderGoodsDto> list) throws Exception;
	
	int updateSettlePrice(String orderId,Double ratio) throws Exception;
	
	/**
	 * 获取指定单号的商品列表
	 * 
	 * @param orderGoods
	 * @return
	 * @throws Exception
	 */
	List<OrderGoodsDto> getOGoodsListByOrderId(OrderGoodsDto orderGoods) throws Exception;
	
	List<OrderGoodsDto> getOGoodsListByOrderId(String orderId) throws Exception;
	/**
	 * 删除指定订单的商品列表
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int delGoodsByOrderId(String orderId) throws Exception;
	
	OrderGoodsDto getOrderGoodsDto(OrderGoodsDto orderGoods) throws Exception;
	
	/**
	 * 获取商铺订单商品列表
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	List<Map> getOrderGoodsListById(String orderId) throws Exception;
	
	/**
	 * 查询订单商品信息，返回：打折标记、返点标记、商品名称、商品描述
	 * @param orderGoods
	 * @return
	 * @throws Exception
	 */
	OrderGoodsDto queryOrderGoodsDto(OrderGoodsDto orderGoods)throws Exception;
	
	void batchUpdateSettlePriceBy(List<Long> orderGoodsIds, Double memberDiscount);
	/**
	 * 查询订单商品少量基本信息
	 * 
	 * @Function: com.idcq.appserver.dao.order.IOrderGoodsDao.getOrderGoodsListByIdForLimit
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月4日 下午1:56:26
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月4日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>>getOrderGoodsListByIdForLimit(String orderId,Integer limit)throws Exception;;
}
