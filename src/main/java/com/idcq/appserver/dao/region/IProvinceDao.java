package com.idcq.appserver.dao.region;

import java.util.List;

import com.idcq.appserver.dto.region.ProvinceDto;

public interface IProvinceDao {
	
	/**
	 * 获取省份信息
	 * @return
	 * @throws Exception
	 */
	List<ProvinceDto> getAllProvinces() throws Exception ;
	
	ProvinceDto getProvinceById(Long provinceId) throws Exception ;
}
