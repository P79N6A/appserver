package com.idcq.idianmgr.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.shop.ShopCashierParams;

public interface IShopCashierDao {
	/**
	 * 新增商铺收银员信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public void insertShopCashier(ShopCashierParams bean) throws Exception;
	
	/**
	 * 修改商铺收银员信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int updateShopCashier(ShopCashierParams bean) throws Exception;
	
	/**
	 * 删除商铺收银员信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int deleteShopCashier(Map param) throws Exception;
	
	/**
	 * 查询收银员是否存在
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int findShopCashierExists(Map param)throws Exception;
	List<Map> getShopCashiers(Long shopId) throws Exception;
	
	Map getShopCashierById(Long cashierId) throws Exception;
}
