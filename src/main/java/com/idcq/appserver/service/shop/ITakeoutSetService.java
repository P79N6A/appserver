package com.idcq.appserver.service.shop;

import java.util.Map;

public interface ITakeoutSetService {
	
	/**
	 * 获取指定店铺外卖费用设置
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getTakeoutSetByShopId(Long shopId) throws Exception;
}
