package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.LauncherIconDto;

public interface ILauncherService {
	
	/**
	 * CS23：获取Launcher主页功能图标列表接口
	 * 
	 * @param launcherType	launcher类型
	 * @param shopId	商铺ID
	 * @return
	 */
	public List<LauncherIconDto> getLauncherIcons(int launcherType,long	shopId) throws Exception;

	/**
	 * CS24：获取 apk 名称接口
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	 List<Map> getAppNames(long shopId) throws Exception;
	
}
