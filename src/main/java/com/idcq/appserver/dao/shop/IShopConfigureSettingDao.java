package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
/**
 * 该类做兼容性修改，不再查询shopSetting表，将转致新表1dcq_config，因此，该类中的任何方法不允许再加新调用 @Declare 20160526
 * 商铺设置接口类
 * @author shengzhipeng
 * @date:2015年9月21日 上午10:52:57
 */
@Deprecated
public interface IShopConfigureSettingDao {

	/**
	 * 查询商铺设置信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopSettingDao.queryShopConfigureSetting
	 * @Description:
	 *
	 * @param shopId 商铺id 
	 * @param settingType 设置类型: 1-积分设置  2-打印设置
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 上午10:56:34
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	List<ShopConfigureSettingDto> queryShopConfigureSetting(Long shopId, Integer settingType) throws Exception;
	
	/**
	 * 批量保存商铺配置设置信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopConfigureSettingDao.saveConfigureSetting
	 * @Description:
	 *
	 * @param list 配置信息list对象
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 上午11:47:05
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	void saveConfigureSettingBatch(List<ShopConfigureSettingDto> list) throws Exception;
	
	/**
	 * 根据商铺id和设置类型删除商铺配置信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopConfigureSettingDao.deleteConfigureSettingBy
	 * @Description:
	 *
	 * @param shopId 商铺id
	 * @param settingType 设置类型
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @param settingKEY 
	 * @date:2015年9月21日 上午11:58:32
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	void deleteConfigureSettingBy(Long shopId, Integer settingType, String settingKey)throws Exception;
	/**
	 * 获取key对应的值
	 * @param shopId 店铺ID
	 * @param settingType 配置类型
	 * @param settingKey 配置key
	 * @return 返回key对的value值
	 * @see [类、类#方法、类#成员]
	 * @author  shengzhipeng
	 */
	String getShopConfigureSettingValue(Long shopId, Integer settingType, String settingKey) throws Exception;

	int updateConfigureSetting(Map<String, Object> map);
}
