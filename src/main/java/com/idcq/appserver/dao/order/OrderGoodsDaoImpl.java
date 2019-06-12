package com.idcq.appserver.dao.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.OrderGoodsDto;
/**
 * 我的订单daooo
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class OrderGoodsDaoImpl extends BaseDao<OrderGoodsDto>implements IOrderGoodsDao{

	public int saveOrderGoods(OrderGoodsDto orderGoods) throws Exception {
		return super.insert(generateStatement("saveOrderGoods"),orderGoods);
	}

	public List<OrderGoodsDto> getOGoodsListByOrderId(OrderGoodsDto orderGoods)
			throws Exception {
		return super.findList(generateStatement("getOGoodsListByOrderId"),orderGoods.getOrderId());
	}

	public int delGoodsByOrderId(String orderId) throws Exception {
		return super.delete(generateStatement("delGoodsByOrderId"), orderId);
	}

	public int updateSettlePrice(String orderId, Double ratio) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("orderId",orderId);
		map.put("ratio",ratio);
		return super.update(generateStatement("updateSettlePrice"),map);
	}

	@Override
	public OrderGoodsDto getOrderGoodsDto(OrderGoodsDto orderGoods)
			throws Exception {
		return (OrderGoodsDto) super.selectOne("getOrderGoodsDto", orderGoods);
	}

	public int saveOrderGoodsBatch(List<OrderGoodsDto> list) throws Exception {
		return super.insert(generateStatement("saveOrderGoodsBatch"), list);
	}

	public List<Map> getOrderGoodsListById(String orderId) throws Exception {
		return super.getSqlSession().selectList("getOrderGoodsListById",orderId);
	}

	public OrderGoodsDto queryOrderGoodsDto(OrderGoodsDto orderGoods)
			throws Exception {
		return (OrderGoodsDto) super.selectOne("queryOrderGoodsDto", orderGoods);
	}

    public void batchUpdateSettlePriceBy(List<Long> orderGoodsIds, Double memberDiscount)
    {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("orderGoodsIds",orderGoodsIds);
        map.put("memberDiscount",memberDiscount);
        super.update(generateStatement("batchUpdateSettlePriceBy"),map);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.order.IOrderGoodsDao#getOrderGoodsListByIdForLimit(java.lang.String)
     */
    @Override
    public List<Map<String, Object>> getOrderGoodsListByIdForLimit(String orderId,Integer limit) throws Exception
    {
        Map<String, Object> pMap = new HashMap<String, Object>();
        pMap.put("orderId",orderId);
        pMap.put("limit",limit);
        return super.getSqlSession().selectList("getOrderGoodsListByIdForLimit",pMap);
    }

    @Override
    public List<OrderGoodsDto> getOGoodsListByOrderId(String orderId) throws Exception {
        return super.findList(generateStatement("getOGoodsListByOrderId"), orderId);
    }
}
