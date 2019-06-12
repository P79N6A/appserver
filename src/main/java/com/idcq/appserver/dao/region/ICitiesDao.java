package com.idcq.appserver.dao.region;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.region.CitiesDto;

public interface ICitiesDao {
	
	/**
	 * 获取城市信息列表
	 * 
	 * @param provinceId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<CitiesDto> getCitis(Long provinceId,String cityName,int page,int pageSize) throws Exception ;
	
	public Integer getCitisTotal(Long provinceId,String cityName) throws Exception;
	
	/**
	 * 获取所有城市信息
	 * @return
	 * @throws Exception
	 */
	List<Map> getAllCitis() throws Exception ;
	
	/**
	 * 查询city信息
	 * @param cityId
	 * @return
	 */
	CitiesDto getCityById(Long cityId) throws Exception ;
	
	/**
	 * 根据城市名称查询城市和省的信息
	 * @param cityName
	 * @return
	 */
	Map<String, Object> getCityInfoByName(String cityName);
}
