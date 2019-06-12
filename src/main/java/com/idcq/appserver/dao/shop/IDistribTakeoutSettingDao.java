package com.idcq.appserver.dao.shop;

import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;

public interface IDistribTakeoutSettingDao {

	/**
	 * 获取店铺外卖费用设置
	 * 
	 * @param shopId
	 * @param settingType
	 * @return
	 * @throws Exception
	 */
	DistribTakeoutSettingDto getDistribTakeoutSetting(Long shopId,Integer settingType) throws Exception;
	
	/**
	 * 保存店铺外卖费用设置
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	Long addDistribTakeoutSetting(DistribTakeoutSettingDto dto) throws Exception;	
	
	/**
	 * 删除店铺外卖费用设置
	 * 
	 * @param settingId
	 * @return
	 * @throws Exception
	 */
	int delDistribTakeoutSetting(Long settingId) throws Exception;	
	
	/**
	 * 删除店铺外卖费用设置
	 * 
	 * @param settingType
	 * @return
	 * @throws Exception
	 */
	int delDistribTakeoutSettingByType(Long shopId,Integer settingType) throws Exception;	
	
	/**
	 * 获取店铺外卖费用设置ID
	 * 
	 * @param shopId
	 * @param settingType
	 * @return
	 * @throws Exception
	 */
	Long getDistribTakeoutSettingByType(Long shopId,Integer settingType) throws Exception;	
	
	/**
	 * 修改店铺外卖费用设置
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int updateDistribTakeoutSetting(DistribTakeoutSettingDto dto) throws Exception;	
}
