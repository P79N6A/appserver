package com.idcq.appserver.dao.region;

import java.util.Collection;
import java.util.List;

import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.region.TownDto;

public interface IRegionDao {
	
	/**
	 * 获取城市信息列表
	 * 
	 * @param pCode
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<DistrictDto> getDistricts(Long pCode,int page,int pageSize) throws Exception ;
	
	public Integer getDistrictsTotal(Long pCode) throws Exception ;
	
	DistrictDto getDistrictById(Long districtId) throws Exception ;
	
	List<DistrictDto>getAllDistricits()throws Exception;
	
	List<DistrictDto> getDistrictByIdList(Collection<Long> districtId) ;
	
	TownDto getTownById(Integer townId) throws Exception ;

	CitiesDto getCityById(Integer cityId) throws Exception;

	ProvinceDto getProvinceId(Integer provinceId) throws Exception;
}
