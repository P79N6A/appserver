package com.idcq.appserver.service.shop;

import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;

public interface IDistribTakeoutSettingService {

	DistribTakeoutSettingDto getDistribTakeoutSetting(Long shopId,Integer settingType) throws Exception;	
	
	/**
	 * 保存店铺外卖费用设置
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	Long addDistribTakeoutSetting(DistribTakeoutSettingDto dto) throws Exception;	
}
