package com.idcq.appserver.dao.shop;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.common.IConfigDao;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.shop.ShopConfigureSettingDto;
/**
 * 该类为兼容旧版本，操作的数据是1dcq_config，不再提供更新
 * @Deprecated
 * @Date 2016-05-26
 * @author zc
 *
 */
@SuppressWarnings("deprecation")
@Repository
public class ShopConfigureSettingDaoImpl extends BaseDao<ShopConfigureSettingDto> implements IShopConfigureSettingDao {

    @Autowired
    private IConfigDao configDao;
/*	@Override
	public List<ShopConfigureSettingDto> queryShopConfigureSetting(Long shopId,
			Integer settingType) throws Exception {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("shopId", shopId);
		parameter.put("settingType", settingType);
		return super.findList(generateStatement("queryShopConfigureSetting"), parameter);
	}*/
	@Override
	public List<ShopConfigureSettingDto> queryShopConfigureSetting(Long shopId,
	        Integer settingType) throws Exception {
	    ConfigDto searchCondition = new ConfigDto();
	    searchCondition.setConfigGroup(ConfigTypeConvertor.convertShopSettingForConfigGroup(settingType));
	    searchCondition.setBizId(shopId);
	    searchCondition.setBizType(1);
	    List<ConfigDto> tempResults = configDao.queryForAllConfig(searchCondition);
	    List<ShopConfigureSettingDto> results = new LinkedList<ShopConfigureSettingDto>();
	    if(null != tempResults && tempResults.size() > 0){
	        ShopConfigureSettingDto dto = null;
	        for(ConfigDto tempConfigDto : tempResults){
	            dto = new ShopConfigureSettingDto();
	            dto.setSettingKey(tempConfigDto.getConfigKey());
	            dto.setSettingValue(tempConfigDto.getConfigValue());
	            results.add(dto);
	        }
	    }
	    return results;
	}

/*	@Override
	public void saveConfigureSettingBatch(List<ShopConfigureSettingDto> list) throws Exception {
		super.insert(generateStatement("saveConfigureSettingBatch"), list);
	}*/
	public void saveConfigureSettingBatch(List<ShopConfigureSettingDto> list) throws Exception {
	    List<ConfigDto> inserts = new LinkedList<ConfigDto>();
	    ConfigDto dto = null;
	    for(ShopConfigureSettingDto temp : list){
	        dto = ConfigTypeConvertor.convertShopConfigureSettingDtoToConfigDto(temp);
	        inserts.add(dto);
	    }
	    configDao.batchInsert(inserts);
	}

/*	@Override
	public void deleteConfigureSettingBy(Long shopId, Integer settingType,String settingKey)
			throws Exception {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("shopId", shopId);
		parameter.put("settingType", settingType);
		parameter.put("settingKey", settingKey);
		super.delete(generateStatement("deleteConfigureSettingBy"), parameter);
	}*/
	@Override
    public void deleteConfigureSettingBy(Long shopId, Integer settingType,String settingKey)
	        throws Exception {
	    ConfigDto dto = new ConfigDto();
	    dto.setBizId(shopId);
	    dto.setBizType(1);
	    dto.setConfigKey(settingKey);
	    dto.setConfigGroup(ConfigTypeConvertor.convertShopSettingForConfigGroup(settingType));
	    configDao.deleteConfig(dto);
	}
	/**
	 * 无效方法
	 * @Date 2016-05-26
	 */
	@Deprecated
    @Override
	public int updateConfigureSetting(Map<String,Object> map) {
    	return super.update(generateStatement("updateConfigureSetting"),map);
	}

/*	public String getShopConfigureSettingValue(Long shopId, Integer settingType, String settingKey) throws Exception
    {
        Map<String, Object> parameter = new HashMap<String, Object>();
        parameter.put("shopId", shopId);
        parameter.put("settingType", settingType);
        parameter.put("settingKey", settingKey);
        return this.getSqlSession().selectOne(generateStatement("getShopConfigureSettingValue"), parameter);
    }*/
	
	public String getShopConfigureSettingValue(Long shopId, Integer settingType, String settingKey) throws Exception
	{
	   String result = null;
	   ConfigDto searchCondition = new ConfigDto();
	   searchCondition.setBizType(1);
	   searchCondition.setBizId(shopId);
	   searchCondition.setConfigKey(settingKey);
	   searchCondition.setConfigGroup(ConfigTypeConvertor.convertShopSettingForConfigGroup(settingType));
	   List<ConfigDto> tempResults = configDao.queryForAllConfig(searchCondition);
	   if(CollectionUtils.isNotEmpty(tempResults)){
	       result = tempResults.get(0).getConfigValue();
	   }
	   return result;
	}
}
