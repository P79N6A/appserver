package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;

/**
 * 商品评论dao
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:19:18
 */
@Repository
public class GoodsCategoryDaoImpl extends BaseDao<GoodsCategoryDto>implements IGoodsCategoryDao{
	
	public List<GoodsCategoryDto> getShopGoodsCategory(Long shopId,Integer columnId,String parentCategoryId, Integer page, Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("columnId", columnId);
		map.put("parentCategoryId", parentCategoryId);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return  super.findList(generateStatement("getShopGoodsCategory"), map);
	}

	@Override
	public List<GoodsCategoryDto> queryGoodsCategory(GoodsCategoryDto goodsCategory) throws Exception {
		return  super.findList(generateStatement("queryGoodsCategory"), goodsCategory);
	}
	
	@Override
	public Integer queryGoodsCategoryLastIndex(GoodsCategoryDto goodsCategory) throws Exception {
		return (Integer)super.selectOne(generateStatement("queryGoodsCategoryLastIndex"), goodsCategory);
	}
	
	@Override
	public GoodsCategoryDto queryGoodsCategoryById(Long goodsCategoryId) throws Exception {
		return (GoodsCategoryDto)super.selectOne(generateStatement("queryGoodsCategoryById"), goodsCategoryId);
	}
	
	@Override
	public int addGoodsCategory(GoodsCategoryDto goodsCategory) throws Exception {
		return super.insert(generateStatement("addGoodsCategory"), goodsCategory);
	}
	
	@Override
	public List<Long> queryGoodsCategoryIdsByShopId(Long shopId) throws Exception {
		return (List)super.findList(generateStatement("queryGoodsCategoryIdsByShopId"),shopId);
	}
	public Integer getShopGoodsCategoryTotal(Long shopId,Integer columnId,String parentCategoryId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("columnId", columnId);
		map.put("parentCategoryId", parentCategoryId);
		return (Integer) super.selectOne(generateStatement("getShopGoodsCategoryTotal"), map);
	}

	/** 
	* @Title: getGoodsCategoryByPage 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param pageModel
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public PageModel getGoodsCategoryByPage(PageModel pageModel)
			throws Exception {
		return super.findPagedList(generateStatement("getCategoryList"), generateStatement("getCategoryCount"), pageModel);
	}
	
	/**
	 * 根据商品类别父id获取子类别
	 * @Title: getGoodsCategoryByCategoryPid 
	 * @param @param parentCategoryId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<GoodsCategoryDto> getGoodsCategoryByCategoryPid(
			Long parentCategoryId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("parentCategoryId", parentCategoryId);
		return super.findList(generateStatement("getGoodsCategoryByCategoryPid"), params);
	}
	
	@Override
	public List<GoodsCategoryDto> getSubGoodsCategoryByPid(
			List<Long> categoryIdList) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("parentCategoryId", convertListToStr(categoryIdList));
		return super.findList(generateStatement("getGoodsCategoryByCategoryPid"), params);
	}
	
	/**
	 * 将list转化为
	* @Title: convertListToStr 
	* @param @param categoryIdList
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	private String convertListToStr(List<Long>categoryIdList)
	{
		if(categoryIdList==null||categoryIdList.size()==0)
		{
			return null;
		}
		StringBuffer buffer=new StringBuffer("-1");
		for(Long categoryId:categoryIdList)
		{
			buffer.append(","+categoryId);
		}
		return buffer.toString();
	}
	
	public List<GoodsCategoryDto> getShopGoodsGroupCategory(Long shopId,Integer columnId,String goodsGroupId,String parentCategoryId, Integer page, Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("goodsGroupId", goodsGroupId);
		map.put("parentCategoryId", parentCategoryId);
		map.put("columnId", columnId);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return  super.findList(generateStatement("getShopGoodsGroupCategory"), map);
	}
	
	public Integer getShopGoodsGroupCategoryTotal(Long shopId,String goodsGroupId,String parentCategoryId) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("goodsGroupId", goodsGroupId);
		map.put("parentCategoryId", parentCategoryId);
		return (Integer) super.selectOne(generateStatement("getShopGoodsGroupCategoryTotal"), map);
	}

	public Map<String,Object> getGoodsCategoryById(Long id) throws Exception {
		return (Map<String,Object>)super.selectOne("getGoodsCategoryById", id);
	}


	@Override
	public List<Map> getShopCategory(Long shopId, String parentCategoryId, Integer page,
			Integer pageSize) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("parentCategoryId", parentCategoryId);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return  (List)findList("getShopFullCategory", map);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.goods.IGoodsCategoryDao#getGoodsSalesStat(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> getGoodsSalesStat(Map<String, Object> pMap) throws Exception
    {
        return  (List)findList("getGoodsSalesStat", pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.goods.IGoodsCategoryDao#getGoodsSalesStatCount(java.util.Map)
     */
    @Override
    public Integer getGoodsSalesStatCount(Map<String, Object> pMap) throws Exception
    {
        
        return (Integer) super.selectOne(generateStatement("getGoodsSalesStatCount"), pMap);
    }
    
    /**
     * 获取商品类别列表
     * @Title: getCategoryListByIdList 
     * @param @param goodsCategoryIdList
     * @param @return
     * @param @throws Exception  
     * @throws
     */
	public List<GoodsCategoryDto> getCategoryListByIdList(
			List<String> goodsCategoryIdList) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("goodsCategoryIdList", goodsCategoryIdList);
		return super.findList(generateStatement("getCategoryListByIdList"),params);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsCategoryDao#getGoodsCategorySalesStat(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getGoodsCategorySalesStat(
			Map<String, Object> params) throws Exception {
		
		return (List)super.findList(generateStatement("getGoodsCategorySalesStat"),params);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsCategoryDao#getGoodsCategorySalesStatCount(java.util.Map)
	 */
	@Override
	public Integer getGoodsCategorySalesStatCount(Map<String, Object> params)
			throws Exception {
		
        return (Integer) super.selectOne(generateStatement("getGoodsCategorySalesStatCount"), params);
        
	}

	
}
