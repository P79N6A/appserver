package com.idcq.appserver.service.advertise;

import com.idcq.appserver.dto.advertise.AdvertiseDto;
import com.idcq.appserver.dto.common.PageModel;

public interface IAdvertiseService {
	
	/**
	 * 检索指定区域的广告信息列表
	 * 
	 * @param advertise 广告对象
	 * @param page 第几页
	 * @param pageSize 每页多少条
	 * @return
	 * @throws Exception
	 */
	PageModel getAdListByCity(AdvertiseDto advertise,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取广告信息列表
	 * 
     * @param advertise 广告对象
     * @param page 第几页
     * @param pageSize 每页多少条
	 * @return
	 * @throws Exception
	 */
	String getAdListInCacheByCity(AdvertiseDto advertise, int page, int pageSize) throws Exception;
}
