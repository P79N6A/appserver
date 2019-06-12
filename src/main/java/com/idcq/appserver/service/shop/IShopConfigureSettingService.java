package com.idcq.appserver.service.shop;

import java.util.List;

import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
import com.idcq.appserver.dto.shop.ShopSettingsDto;

/**
 * 商铺设置接口类
 * @author shengzhipeng
 * @date:2015年9月21日 上午10:52:57
 */
public interface IShopConfigureSettingService {

	/**
	 * 查询商铺设置信息
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopSettingService.queryShopConfigureSetting
	 * @Description:
	 *
	 * @param shopId 商铺id
	 * @param settingType 设置类型：1-积分设置  2-打印设置
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 上午10:58:34
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	List<ShopConfigureSettingDto> queryShopConfigureSetting(Long shopId, Integer settingType) throws Exception;
	
	/**
	 * 保存商铺配置信息
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopConfigureSettingService.saveConfigureSetting
	 * @Description:
	 *
	 * @param shopSettingsDto
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 上午11:43:39
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	void saveConfigureSetting(ShopSettingsDto shopSettingsDto) throws Exception;

	int updateConfigureSetting(ShopSettingsDto shopSettingsDto) throws Exception;
}
