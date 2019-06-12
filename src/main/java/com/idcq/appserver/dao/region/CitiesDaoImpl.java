package com.idcq.appserver.dao.region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;

/**
 * 城市区域dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:53:58
 */
@Repository
public class CitiesDaoImpl extends BaseDao<CitiesDto>implements ICitiesDao{

	public List<CitiesDto> getCitis(Long provinceId,String cityName, int page, int pageSize) throws Exception {
		Map param=new HashMap();
		param.put("provinceId", provinceId);
		param.put("cityName", cityName);
		param.put("n", (page-1)*pageSize);                   
		param.put("m", pageSize);       
		return super.findList(generateStatement("getCitis"), param);
	}
	
	public Integer getCitisTotal(Long provinceId,String cityName) throws Exception {
		Map param=new HashMap();
		param.put("provinceId", provinceId);
		param.put("cityName", cityName);
		return (Integer) super.selectOne(generateStatement("getCitisTotal"), param);
	}

	public List<Map> getAllCitis() throws Exception {
		return (List)this.findList(generateStatement("getAllCitis"));
	}
	
	public CitiesDto getCityById(Long cityId) throws Exception {
		return (CitiesDto) HandleCacheUtil.getEntityCacheByClass(CitiesDto.class, cityId, 0);
	}

	public Map<String, Object> getCityInfoByName(String cityName) {
		return (Map<String, Object>) super.selectOne(generateStatement("getCityInfoByName"), cityName);
	}
}
