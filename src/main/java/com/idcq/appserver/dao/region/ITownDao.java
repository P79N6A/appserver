package com.idcq.appserver.dao.region;

import java.util.List;
import com.idcq.appserver.dto.region.TownDto;

public interface ITownDao {

	TownDto getTownById(Long townId) throws Exception;
	
	/**
	 * 获取街道列表
	 * @param districtId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<TownDto> getTowns(Long districtId,int page,int pageSize) throws Exception ;
	
	public Integer getTownsTotal(Long districtId) throws Exception;
}
