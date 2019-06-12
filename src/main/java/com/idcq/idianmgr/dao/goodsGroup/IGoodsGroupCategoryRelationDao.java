package com.idcq.idianmgr.dao.goodsGroup;

import java.util.List;
import java.util.Map;

import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;


public interface IGoodsGroupCategoryRelationDao {

	int addGoodsGroupCategoryRelationBatch(List<GoodsGroupCategoryRelationDto> list) throws Exception;
	int delGoodsGroupCategoryRelationByGgId(Long id) throws Exception;
	List<GoodsGroupCategoryRelationDto>getCategoryListByGroupId(Long goodsGroupId)throws Exception;
	
	/**
	 * 根据分类编号查询商品族编号信息
	 * @param param
	 * @return
	 * @throws Exception
	 */
	Long getGoodsGroupIdBycategoryId(Map<String,Object> param)throws Exception;
	
	int queryGoodsCategoryExists(Long id) throws Exception;
	Long getParentGoodsCategoryId(Long id) throws Exception;
	
}
