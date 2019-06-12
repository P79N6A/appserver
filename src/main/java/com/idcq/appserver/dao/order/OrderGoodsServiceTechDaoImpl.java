package com.idcq.appserver.dao.order;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.order.OrderGoodsServiceTech;

/**
 * @desc 商品订单技师服务
 * @author djh
 *
 */
@Repository
public class OrderGoodsServiceTechDaoImpl  extends BaseDao<OrderGoodsServiceTech> implements IOrderGoodsServiceTechDao{

	@Override
	public void batchAddOrderGoodsServiceTech(
			List<OrderGoodsServiceTech> goodsTechList) throws Exception {
			this.insert(generateStatement("batchAddOrderGoodsServiceTech"), goodsTechList);
	}
}
