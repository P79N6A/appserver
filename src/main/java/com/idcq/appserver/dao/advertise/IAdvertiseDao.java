package com.idcq.appserver.dao.advertise;

import java.util.List;

import com.idcq.appserver.dto.advertise.AdvertiseDto;

public interface IAdvertiseDao {
	
	/**
	 * 查询指定城市的广告列表
	 * 
	 * @param advertise
	 * @param page
	 * @param pageSize
	 * @return
	 */
	List<AdvertiseDto> getAdList(AdvertiseDto advertise,int page,int pageSize) ;
	
	/**
	 * 统计指定城市的广告记录数
	 * 
	 * @param advertise
	 * @return
	 */
	int getAdListCount(AdvertiseDto advertise) ;
}
