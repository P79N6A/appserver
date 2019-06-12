package com.idcq.idianmgr.dao.goodsGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupCategoryRelationDto;

/**
 * 商品族分类中间表dao
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 下午7:04:07
 */
@Repository
public class GoodsGroupCategoryRelationDaoImpl extends BaseDao<GoodsGroupCategoryRelationDto> 
	implements IGoodsGroupCategoryRelationDao{

	public int addGoodsGroupCategoryRelationBatch(List<GoodsGroupCategoryRelationDto> list) 
			throws Exception {
		return super.insert("addGoodsGroupCategoryRelationBatch", list);
	}

	public int delGoodsGroupCategoryRelationByGgId(Long id) throws Exception {
		return super.delete("delGoodsGroupCategoryRelationByGgId", id);
	}

	/**
	 * 根据商品族编号获取商品族种类列表
	 * @Title: getCategoryListByGroupId 
	 * @param @param goodsGroupId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<GoodsGroupCategoryRelationDto> getCategoryListByGroupId(
			Long goodsGroupId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("goodsGroupId", goodsGroupId);
		return findList(generateStatement("getCategoryListByGroupId"),params);
	}

	public Long getGoodsGroupIdBycategoryId(Map<String,Object> param) {
		return (Long) this.selectOne(generateStatement("getGoodsGroupIdBycategoryId"), param);
	}

	public int queryGoodsCategoryExists(Long id) throws Exception {
		return (int)super.selectOne("queryGoodsCategoryExists", id);
	}

	public Long getParentGoodsCategoryId(Long id) throws Exception {
		return (Long)super.selectOne("getParentGoodsCategoryId", id);
	}
	
	
}
