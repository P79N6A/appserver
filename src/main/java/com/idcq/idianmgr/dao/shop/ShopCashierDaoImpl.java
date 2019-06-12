package com.idcq.idianmgr.dao.shop;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.idcq.idianmgr.dto.shop.ShopCashierParams;
@Repository
public class ShopCashierDaoImpl extends SqlSessionDaoSupport implements IShopCashierDao {
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	@Override
	public void insertShopCashier(ShopCashierParams bean) throws Exception {
		this.getSqlSession().insert("insertShopCashier", bean);
	}

	@Override
	public int updateShopCashier(ShopCashierParams bean) throws Exception {
		return this.getSqlSession().insert("updateShopCashier", bean);
	}

	@Override
	public int deleteShopCashier(Map param) throws Exception {
		return this.getSqlSession().insert("deleteShopCashier", param);
	}
	@Override
	public int findShopCashierExists(Map param) throws Exception {
		return this.getSqlSession().selectOne("findShopCashierExists", param);
	}
	@Override
	public List<Map> getShopCashiers(Long shopId) throws Exception {
		return getSqlSession().selectList("getShopCashiers", shopId);
	}
    @Override
    public Map getShopCashierById(Long cashierId) throws Exception {
        return getSqlSession().selectOne("getShopCashierById", cashierId);
    }
}
