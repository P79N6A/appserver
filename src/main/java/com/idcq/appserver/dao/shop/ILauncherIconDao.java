package com.idcq.appserver.dao.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.LauncherIconDto;

public interface ILauncherIconDao {
	
	
	public List<LauncherIconDto> getLauncherIconListByType(int launcherType,long shopId);

	public List<Map> getAppNames(long shopId) throws Exception;
}
