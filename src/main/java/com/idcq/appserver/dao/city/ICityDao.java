package com.idcq.appserver.dao.city;

import java.util.List;

import com.idcq.appserver.dto.city.CityDto;

public interface ICityDao {
	
	/**
	 * 获取城市信息列表
	 * 
	 * @param city
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<CityDto> getCityList(CityDto city,int page,int pageSize) throws Exception ;
	
}
