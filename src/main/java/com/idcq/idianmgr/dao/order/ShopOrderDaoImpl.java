package com.idcq.idianmgr.dao.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.order.ShopOrderDto;

/**
 * 商铺订单dao实现类
 * @author shengzhipeng
 * @date:2015年7月30日 下午2:45:19
 */

@Repository
public class ShopOrderDaoImpl extends BaseDao<ShopOrderDto> implements IShopOrderDao{

	public List<ShopOrderDto> getShopOrders(Map map, int page, int pageSize)
			throws Exception {
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);
		return super.findList(generateStatement("getShopOrders"), map);
	}

	public int getShopOrdersCount(Map map) throws Exception {
		return this.getSqlSession().selectOne("getShopOrdersCount", map);
	}

	public List<Map<String, Object>> getShopOrdersNumber(Long shopId) throws Exception {
		return super.getSqlSession().selectList(generateStatement("getShopOrdersNumber"), shopId);
	}

	public int getNotPayOrderNum(Long shopId, Integer orderStatus)
			throws Exception {
		Map param =  new HashMap();
		param.put("shopId", shopId);
		param.put("orderStatus", orderStatus);
		return (int) super.selectOne(generateStatement("getNotPayOrderNum"), param);
	}

	public List<Map> getShopCategoryOrders(Long shopId, Long categoryId,
			String[] date) {
		Map param =  new HashMap();
		param.put("shopId", shopId);
		param.put("categoryId", categoryId);
		param.put("date", date);
		return super.getSqlSession().selectList(generateStatement("getShopCategoryOrders"), param);
	}

	public ShopOrderDto getShopOrderById(String orderId) throws Exception {
		return (ShopOrderDto)super.selectOne("getShopOrderById", orderId);
	}

	public String getGoodsLogoByOrderId(String orderId) throws Exception {
		return (String) super.selectOne(generateStatement("getGoodsLogoByOrderId"), orderId);
	}
}
