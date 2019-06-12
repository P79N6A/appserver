package com.idcq.appserver.service.goods;

import java.sql.Time;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;

public interface IShopTimeIntevalService {
	
	/**
	 * 获取分页商铺中的服务时段
	 */
	PageModel getPage(Long shopId,Integer serverMode,Integer pageNo,Integer pageSize) throws Exception;
	
	public List<Map> getTimeInterval(Time begin,Time end,long interval,int start);
	
}
