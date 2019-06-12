package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.DistributionTakeoutSetting;

public interface IDistributionTakeoutService {
	
	/**
	 * 根据shopId获取配送/外卖设置
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	DistributionTakeoutSetting getDistributionTakeoutSettingByShopId(Long shopId) throws Exception;
	
	/**
	 * 获取配送时间/接单时间
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map> getDeliveryTime(Map param) throws Exception;
	
}
