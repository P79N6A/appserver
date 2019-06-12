package com.idcq.appserver.dao.region;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.dto.region.TownDto;
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
public class RegionDaoImpl extends BaseDao<DistrictDto>implements IRegionDao{

	public List<DistrictDto> getDistricts(Long cityId, int page, int pageSize) throws Exception {
		Map param=new HashMap();
		param.put("cityId", cityId);
		param.put("limit", (page-1)*pageSize);                   
		param.put("pageSize", pageSize);           
		return super.findList(generateStatement("getDistricts"), param);
	}
	
	public Integer getDistrictsTotal(Long pCode) throws Exception {
		Map param=new HashMap();
		param.put("cityId", pCode);
		return (Integer) super.selectOne(generateStatement("getDistrictsTotal"), param);
	}

	public DistrictDto getDistrictById(Long districtId) throws Exception {
		return (DistrictDto) HandleCacheUtil.getEntityCacheByClass(DistrictDto.class, districtId, 0);
	}

	public List<DistrictDto> getAllDistricits() throws Exception {
		return super.findList(generateStatement("getAllDistricts"));
	}

	@Override
	public List<DistrictDto> getDistrictByIdList(Collection<Long> districtIdList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("districtIdList", districtIdList);
		return findList(generateStatement("getDistrictByIdList"),params);
	}

	@Override
	public TownDto getTownById(Integer townId) throws Exception {
		return (TownDto) HandleCacheUtil.getEntityCacheByClass(TownDto.class, townId, 0);
	}

	@Override
	public CitiesDto getCityById(Integer cityId) throws Exception {
		return (CitiesDto) HandleCacheUtil.getEntityCacheByClass(CitiesDto.class, cityId, 0);
	}

	@Override
	public ProvinceDto getProvinceId(Integer provinceId) throws Exception {
		return (ProvinceDto) HandleCacheUtil.getEntityCacheByClass(ProvinceDto.class, provinceId, 0);
	}
	
}
