package com.idcq.appserver.dao.shop;

import com.idcq.appserver.dto.shop.ShopTimeIntDto;

public interface IShopTimeIntDao {
	
	/**
	 * 获取商铺可用的时间段ID
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Long getShopTimeIdById(Long shopId,Long intevalId) throws Exception;
	
	/**
	 * 根据ID获取商铺提供资源预定的时间段
	 * 
	 * @param intevalId
	 * @return
	 * @throws Exception
	 */
	ShopTimeIntDto getShopTimeById(Long intevalId) throws Exception;
	
	/**
	 * 获取指定资源预定规则
	 * 
	 * @param intevalId
	 * @return
	 * @throws Exception
	 */
	Long getBookRuleIdByTimeId(Long intevalId) throws Exception;
	
	/**
	 * 保存时间段
	 * 
	 * @param time
	 * @return
	 * @throws Exception
	 */
	Long saveTimeInteval(ShopTimeIntDto time) throws Exception;
	
	
}
