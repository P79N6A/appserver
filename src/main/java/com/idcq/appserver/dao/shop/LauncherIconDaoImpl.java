package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.LauncherIconDto;

/**
 * launcherdao
 * 
 * @author Administrator
 * 
 * @date 2016年2月19日
 * @time 上午11:44:23
 */
@Repository
public class LauncherIconDaoImpl extends BaseDao<LauncherIconDto> implements ILauncherIconDao{

	@Override
	public List<LauncherIconDto> getLauncherIconListByType(int launcherType,
			long shopId) {
	    List<LauncherIconDto> rs= new LinkedList<LauncherIconDto>();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("launcherType", launcherType);
		param.put("shopId", shopId);
		rs.addAll(super.findList("getLauncherIconListByType",param));
		rs.addAll(super.findList("getCommonLauncherIconListByType",param));
		return rs;
	}

	@Override
	public List<Map> getAppNames(long shopId) throws Exception {
		return (List) findList("getAppNames", shopId);
	}

	
}
