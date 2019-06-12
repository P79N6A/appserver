package com.idcq.appserver.service.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.shop.IShopConfigureSettingDao;
import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
import com.idcq.appserver.dto.shop.ShopSettingsDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
/**
 * 该类已废置，暂转调用commonService相关方法
 * @deprecated
 * @author Administrator
 *
 */
@Service
public class ShopConfigureSettingServiceImpl implements IShopConfigureSettingService {

	@Autowired
	private IShopConfigureSettingDao shopSettingDao;
	
	@Deprecated
	public List<ShopConfigureSettingDto> queryShopConfigureSetting(Long shopId, Integer settingType) throws Exception {
		return shopSettingDao.queryShopConfigureSetting(shopId, settingType);
	}
	
	@Deprecated
	public void saveConfigureSetting(ShopSettingsDto shopSettingsDto)
			throws Exception {
		Long shopId = shopSettingsDto.getShopId();
		Integer settingType = shopSettingsDto.getSettingType();
		List<ShopConfigureSettingDto> list = shopSettingsDto.getLst();
		List<ShopConfigureSettingDto> shopConfigureSettingDtoList = shopSettingDao.queryShopConfigureSetting(shopId, settingType);
		if (CollectionUtils.isNotEmpty(list)) {
			if(list.size() == shopConfigureSettingDtoList.size()){
				shopSettingDao.deleteConfigureSettingBy(shopId, settingType,null);
			}
			for (ShopConfigureSettingDto shopConfigureSettingDto : list) {
				if(settingType==3){
					//是否为正数（正整数或是正浮点数）
					if(shopConfigureSettingDto.getSettingValue()==null){
						throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_REQUIRED_SETTING_VALUE);
					}
					if(!(NumberUtil.NegatineNumber(shopConfigureSettingDto.getSettingValue()) || NumberUtil.Posttive_float(shopConfigureSettingDto.getSettingValue()))){
						throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_POSITIVE_SETTING_VALUE);
					}
				}
				if(shopConfigureSettingDtoList.size() > 0 && list.size() != shopConfigureSettingDtoList.size()){
					shopSettingDao.deleteConfigureSettingBy(shopId, settingType,shopConfigureSettingDto.getSettingKey());
				}
				shopConfigureSettingDto.setShopId(shopId);
				shopConfigureSettingDto.setSettingType(settingType);
			}
			shopSettingDao.saveConfigureSettingBatch(list);
		}
	}
	/**
	 * 无效方法
	 */
	@Deprecated
	@Override
	public int updateConfigureSetting(ShopSettingsDto shopSettingsDto) throws Exception {
		/*List<ShopConfigureSettingDto> list = shopSettingsDto.getLst();
		if (CollectionUtils.isNotEmpty(list)) {
			Long ShopId
			shopSettingDao.deleteConfigureSettingBy(shopSettingsDto.getShopId(), shopSettingsDto.getSettingType());
			for (ShopConfigureSettingDto shopConfigureSettingDto : list) {
				shopConfigureSettingDto.setShopId(shopId);
				shopConfigureSettingDto.setSettingType(settingType);
			}
		}*/
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("shopId", shopSettingsDto.getShopId());
		map.put("settingType", shopSettingsDto.getSettingType());
		map.put("settingValue", CommonConst.SHOP_SETTING_BAIL_FLAG);
		return shopSettingDao.updateConfigureSetting(map);
	}

}
