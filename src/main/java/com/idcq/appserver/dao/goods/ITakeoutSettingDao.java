package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.TakeoutSettingDto;

public interface ITakeoutSettingDao {
	
	
	public List<TakeoutSettingDto>findTakeoutSettingByShopList(List<Long>shopIdList,Integer settingType)throws Exception;
}
