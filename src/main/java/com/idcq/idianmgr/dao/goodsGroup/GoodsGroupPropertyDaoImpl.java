package com.idcq.idianmgr.dao.goodsGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;

/**
 * GoodsGroupDaoImpl
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 下午8:30:12
 */
@Repository
public class GoodsGroupPropertyDaoImpl extends BaseDao<GoodsGroupPropertyDto> implements IGoodsGroupPropertyDao{
	
	@Override
	public List<GoodsGroupPropertyDto> getGoodsGroupProList(Map param) throws Exception {
		return super.findList(generateStatement("getGoodsGroupProList"),param);
	}
	
	@Override
	public GoodsGroupPropertyDto getGoodsGroupProperty(Map param) throws Exception {
		return (GoodsGroupPropertyDto) super.selectOne(generateStatement("getGoodsGroupProperty"),param);
	}
	
	@Override
	public int updateGoodsGroupPro(GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception {
		return super.update(generateStatement("updateGoodsGroupPro"),goodsGroupPropertyDto);
	}
	
	@Override
	public Long insertGoodsGroupPro(GoodsGroupPropertyDto goodsGroupPropertyDto) throws Exception {
		return (long) super.insert(generateStatement("insertGoodsGroupPro"),goodsGroupPropertyDto);
	}
	
	@Override
	public int delGoodsGroupProperty(int propertyId) throws Exception {
		return super.delete(generateStatement("delGoodsGroupProperty"),propertyId);
	}

	@Override
	public GoodsGroupPropertyDto getGoodsGroupPropertyByProValuesId(
			Long proValuesId) {
		return (GoodsGroupPropertyDto) this.selectOne(generateStatement("getGoodsGroupPropertyByProValuesId"), proValuesId);
	}

	public Long addGoodsGroupProBackId(GoodsGroupPropertyDto ggpDto)
			throws Exception {
		super.insert(generateStatement("addGoodsGroupProBackId"),ggpDto);
		Long groupPropertyId = ggpDto.getGroupPropertyId();
		return groupPropertyId;
	}

	public GoodsGroupPropertyDto getGoodsGroupPropertyByGroupId(
			Long goodsGroupId) throws Exception {
		GoodsGroupPropertyDto dto = (GoodsGroupPropertyDto) this.selectOne(generateStatement("getGoodsGroupPropertyByGroupId"), goodsGroupId);
		return dto;
	}
	

	@Override
	public List<GoodsGroupPropertyDto> getGoodsGroupProperyByGroupId(
			Long groupId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("goodsGroupId", groupId);
		return findList(generateStatement("getGoodsGroupProperyByGroupId"),params);
	}
}
