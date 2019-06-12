package com.idcq.appserver.dao.discount;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopDto;

public interface IShopTimeDiscountDao {
	/**
	 * 获取商铺限时折扣
	 * @param params
	 * @return
	 */
	List<Map> getShopTimedDiscount(Map<String, Object> params)throws Exception;

	/**
	 * 获取商铺限时折扣信息总数
	 * @param params
	 * @return
	 */
	int getShopTimeDiscountCount(Map<String, Object> params)throws Exception;
	
	/**
	 * 获取商铺限时折扣商品信息
	 * @param discountId
	 * @return
	 */
	List<Map> getShopTimedDiscountGoods(Map<String,Object> params)throws Exception;
	
	/**
	 * 获取商铺限时折扣商品信息总数
	 * @param params
	 * @return
	 */
	int getShopTimedDiscountGoodsCount(Map<String, Object> params);
	
	/**
	 * 获取当前商铺所有限时折扣对应的商品信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopTimedDiscountGoodsAll(Long shopId) throws Exception;
	
	/**
	 * 获取当前商铺所有限时折扣对应的商品信息
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopTimedDiscountGoodsAlls(Map params) throws Exception;

	/**
	 * 搜索商铺限时折扣
	 * @param shopList
	 * @return
	 */
	List<Map> searchShopTimeDiscount(List<ShopDto> shopList) throws Exception;
	
	/**
	 * 获取商铺限时折扣对应商品的商品编号
	 * @param list
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopTimedDiscountGoodsId(List<Long> list) throws Exception;

	/**
	 * 初始化限时折扣数据
	 * @param timedLists
	 * @return
	 */
	int insertTimedDiscountData(List<Map> timedLists);

}
