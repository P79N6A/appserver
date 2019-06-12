package com.idcq.appserver.dao.discount;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dto.shop.ShopDto;

@Repository
public class ShopTimeDiscountDaoImpl extends SqlSessionDaoSupport implements IShopTimeDiscountDao {
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}

	public List<Map> getShopTimedDiscount(Map<String, Object> params) throws Exception{
		return (List)this.getSqlSession().selectList("getShopTimedDiscount", params);
	}

	public int getShopTimeDiscountCount(Map<String, Object> params)throws Exception {
		return this.getSqlSession().selectOne("getShopTimeDiscountCount", params);
	}
	
	public List<Map> getShopTimedDiscountGoods(Map<String, Object> params) throws Exception{
		return (List)this.getSqlSession().selectList("getShopTimedDiscountGoods", params);
	}
	
	public int getShopTimedDiscountGoodsCount(Map<String, Object> params) {
		return this.getSqlSession().selectOne("getShopTimedDiscountGoodsCount", params);
	}

	public List<Map> getShopTimedDiscountGoodsAll(Long shopId)
			throws Exception {
		return (List)this.getSqlSession().selectList("getShopTimedDiscountGoodsAll", shopId);
	}
	
	public List<Map> getShopTimedDiscountGoodsAlls(Map params) throws Exception {
		return (List)this.getSqlSession().selectList("getShopTimedDiscountGoodsAlls", params);
	}

	public List<Map> searchShopTimeDiscount(List<ShopDto> shopList)  throws Exception{
		return (List)this.getSqlSession().selectList("searchShopTimeDiscount", shopList);
	}

	public List<Map> searchShopTimeDiscountGoodsAll(List<ShopDto> shopList)
			throws Exception {
		return (List)this.getSqlSession().selectList("searchShopTimeDiscountGoodsAll", shopList);
	}

	public List<Map> getShopTimedDiscountGoodsId(List<Long> list)
			throws Exception {
		return (List)this.getSqlSession().selectList("getShopTimedDiscountGoodsId", list);
	}

	public int insertTimedDiscountData(List<Map> timedLists) {
		return this.getSqlSession().insert("insertTimedDiscountData", timedLists);
	}
	
}
