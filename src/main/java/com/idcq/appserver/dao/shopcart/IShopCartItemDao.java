package com.idcq.appserver.dao.shopcart;

import java.util.List;

import com.idcq.appserver.dto.shopcart.ShopCartItemDto;

public interface IShopCartItemDao {
	
	
	/**
	 * 获取购物车购物项
	 * 
	 * @param item
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<ShopCartItemDto> getItemListByUserId(ShopCartItemDto item,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取购物车商品项总记录数
	 * 
	 * @param item
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	int getItemListCountByUserId(ShopCartItemDto item) throws Exception ;
	
	/**
	 * 更新购物项
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	int updateCartItem(ShopCartItemDto item) throws Exception ;
	
	/**
	 * 新增购物项
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	int saveCartItem(ShopCartItemDto item) throws Exception ;
	
	/**
	 * 删除用户的购物项
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	int delCartItemsByUserId(Long userId) throws Exception ;
}
