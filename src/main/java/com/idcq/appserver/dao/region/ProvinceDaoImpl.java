package com.idcq.appserver.dao.region;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;

/**
 * 省份dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:53:58
 */
@Repository
public class ProvinceDaoImpl extends BaseDao<ProvinceDto>implements IProvinceDao{

	public List<ProvinceDto> getAllProvinces() throws Exception {
		return super.findList(generateStatement("getAllProvinces"));
	}

	public ProvinceDto getProvinceById(Long provinceId) throws Exception {
		return (ProvinceDto) HandleCacheUtil.getEntityCacheByClass(ProvinceDto.class, provinceId, 0);
	}
}
