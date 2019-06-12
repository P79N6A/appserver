package com.idcq.appserver.dao.region;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.region.TownDto;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;
@Repository
public class TownDaoImpl extends BaseDao<TownDto>implements ITownDao{

	public TownDto getTownById(Long townId) throws Exception {
		return (TownDto) HandleCacheUtil.getEntityCacheByClass(TownDto.class, townId, 0);
	}

	@Override
	public List<TownDto> getTowns(Long districtId, int page, int pageSize)
			throws Exception {
		Map param=new HashMap();
		param.put("districtId", districtId);
		param.put("n", (page-1)*pageSize);                   
		param.put("m", pageSize);   
		return super.findList("getTowns", param);
	}

	@Override
	public Integer getTownsTotal(Long districtId) throws Exception {
		Map param=new HashMap();
		param.put("districtId", districtId);
		return (Integer) super.selectOne(generateStatement("getTownsTotal"), param);
	}

}
