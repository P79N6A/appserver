package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.TopGoodsDto;

public interface ITopGoodsDao {
	
	/**
	 * 获取热卖商品列表
	 * 
	 * @param topGoods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<TopGoodsDto> getTopGoodsList(TopGoodsDto topGoods, Integer page, Integer pageSize) throws Exception ;
	
	/**
	 * 获取热卖品总记录
	 * 
	 * @param topGoods
	 * @return
	 * @throws Exception
	 */
	int getTopGoodsListCount(TopGoodsDto topGoods) throws Exception ;
}
