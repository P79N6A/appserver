package com.idcq.appserver.dao.shop;

import java.util.Map;

import com.idcq.appserver.dto.shop.TakeoutSetDto;

public interface ITakeoutSetDao {
	
	/**
	 * 获取指定店铺的外卖费用设置
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	TakeoutSetDto getTakeoutSetByShopId(Long shopId) throws Exception ;
	
	/**
	 * 收银机初始化接口
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map queryTakeoutSetInit(Long shopId) throws Exception;
}
