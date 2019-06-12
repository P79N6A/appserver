package com.idcq.appserver.dao.city;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.city.CityDto;

/**
 * 城市区域dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:53:58
 */
@Repository
public class CityDaoImpl extends BaseDao<CityDto>implements ICityDao{

	public List<CityDto> getCityList(CityDto city, int page, int pageSize)
			throws Exception {
		return null;
	}

	
}
