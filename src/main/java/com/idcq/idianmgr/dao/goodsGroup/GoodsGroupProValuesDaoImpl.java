package com.idcq.idianmgr.dao.goodsGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupProValuesDto;

/**
 * GoodsGroupDaoImpl
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 下午8:30:12
 */
@Repository
public class GoodsGroupProValuesDaoImpl extends BaseDao<GoodsGroupProValuesDto> implements IGoodsGroupProValuesDao{
	
	@Override
	public List<GoodsGroupProValuesDto> getGoodsGroupProValuesList(Map param) throws Exception {
		return super.findList(generateStatement("getGoodsGroupProValuesList"),param);
	}

	@Override
	public GoodsGroupProValuesDto getGoodsGroupProValues(Map param) throws Exception {
		return (GoodsGroupProValuesDto) super.selectOne(generateStatement("getGoodsGroupProValues"),param);
	}

	@Override
	public int delGoodsGroupProValue(int proValuesId) throws Exception {
		return super.delete(generateStatement("delGoodsGroupProValue"),proValuesId);
	}
	
	@Override
	public int updateGoodsGroupProValue(GoodsGroupProValuesDto goodsGroupProValuesDto) throws Exception {
		return super.update(generateStatement("updateGoodsGroupProValue"),goodsGroupProValuesDto);
	}
	
	@Override
	public Long insertGoodsGroupProValue(GoodsGroupProValuesDto goodsGroupProValuesDto) throws Exception {
		return (long) super.insert(generateStatement("insertGoodsGroupProValue"),goodsGroupProValuesDto);
	}

	public Long addGoodsGroupProValueBackId(GoodsGroupProValuesDto ggpvDto)
			throws Exception {
		super.insert(generateStatement("addGoodsGroupProValueBackId"),ggpvDto);
		Long proValuesId = ggpvDto.getProValuesId();
		return proValuesId;
	}

	public int batchDelGoodsGroupProValuesDtoByIds(List<Long> lsit) {
		return super.delete(generateStatement("batchDelGoodsGroupProValuesDtoByIds"), lsit);
	}

	@Override
	public List<GoodsGroupProValuesDto> getGoodsGroupProValuesList(
			List<Long> propertyIdList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("propertyIdList", propertyIdList);
		return findList(generateStatement("getGoodsGroupProValuesByIdList"),params);
	}

	public int updateIsSelectByIds(List<Long> list)
			throws Exception {
		return this.update(generateStatement("updateIsSelectByIds"), list);
	}

	@Override
	public int updateIsNotSelectByIds(List<Long> list) throws Exception {
		return this.update(generateStatement("updateIsNotSelectByIds"), list);
	}
	public int updateIsSelectEqZero(Long goodsGroupId) throws Exception {
		return this.update(generateStatement("updateIsSelectEqZero"), goodsGroupId);
	}

}
