package com.idcq.idianmgr.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.shop.TechTypeDto;
@Repository
public class TechTypeDaoImpl extends BaseDao<TechTypeDto> implements ITechTypeDao{


	@Override
	public void addTechType(TechTypeDto techTypeDto) {
		insert("insertTechType", techTypeDto);
	}

	@Override
	public void updateTechType(TechTypeDto techTypeDto) {
		update("updateTechType", techTypeDto);
	}

	@Override
	public List<TechTypeDto> getTechTypeDtos(TechTypeDto techTypeDto, int page,
			int pageSize) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", techTypeDto.getShopId());
		map.put("parentTechTypeId", techTypeDto.getParentTechTypeId());
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);
		return (List<TechTypeDto>)findList("selectTechTypes", map);
	}

	@Override
	public int getTechTypeCount(TechTypeDto techTypeDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", techTypeDto.getShopId());
		map.put("parentTechTypeId", techTypeDto.getParentTechTypeId());
		return (Integer)selectOne("selectTechTypeCount", map);
	}

	@Override
	public boolean isTechTypeUsed(String techTypeId) {
		Integer count = (Integer)selectOne("isTechTypeUsed", techTypeId);
		if(count != null && count > 0)
			return true;
		return false;
	}

	@Override
	public boolean isNameExist(TechTypeDto techTypeDto) {
		
		Integer count = (Integer)selectOne("isTechTypeNameUsed", techTypeDto);
		if(count != null && count > 0)
			return true;
		return false;
	}
	

}
