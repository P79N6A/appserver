package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.DistribTakeoutSettingDto;

/**
 * 店铺外卖费用设置dao
 * 
 * @author Administrator
 * 
 * @date 2015年7月14日
 * @time 下午4:59:51
 */
@Repository
public class DistribTakeoutSettingDaoImpl extends BaseDao<DistribTakeoutSettingDto> implements IDistribTakeoutSettingDao{

	public DistribTakeoutSettingDto getDistribTakeoutSetting(Long shopId,
			Integer settingType) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("settingType", settingType);
		return (DistribTakeoutSettingDto)super.selectOne("getDistribTakeoutSetting", map);
	}

	public Long addDistribTakeoutSetting(DistribTakeoutSettingDto dto)
			throws Exception {
		super.insert("addDistribTakeoutSetting",dto);
		Long settingId = dto.getSettingId();
		return settingId;
	}

	public int updateDistribTakeoutSetting(DistribTakeoutSettingDto dto)
			throws Exception {
		return super.update("updateDistribTakeoutSetting",dto);
	}

	public int delDistribTakeoutSetting(Long settingId) throws Exception {
		return super.delete("delDistribTakeoutSetting", settingId);
	}
	
	public int delDistribTakeoutSettingByType(Long shopId,Integer settingType) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("settingType", settingType);
		return super.delete("delDistribTakeoutSettingByType", map);
	}

	public Long getDistribTakeoutSettingByType(Long shopId, Integer settingType)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("settingType", settingType);
		return (Long)super.selectOne("getDistribTakeoutSettingByType", map);
	}
	
	
	

	
	
	
}
