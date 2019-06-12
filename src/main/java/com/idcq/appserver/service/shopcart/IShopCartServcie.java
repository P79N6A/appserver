package com.idcq.appserver.service.shopcart;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopcart.ShopCartDto;
import com.idcq.appserver.dto.shopcart.ShopCartItemDto;

public interface IShopCartServcie {

	
	/**
	 * 获取会员的购物车信息（含商品列表）
	 * 
	 * @param shop
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	ShopCartDto getShopCartByUser(ShopCartDto shop,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取购物车的商品列表
	 * 
	 * @param shop
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsListByCart(ShopCartItemDto item,int page,int pageSize) throws Exception ;
	
	/**
	 * 更新购物车商品
	 * 
	 * @param item
	 * @return
	 * @throws Exception
	 */
	int updateCart(ShopCartDto item) throws Exception ;
}
