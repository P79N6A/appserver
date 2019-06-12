package com.idcq.appserver.dao.goods;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;
import com.idcq.appserver.dto.goods.GoodsDto;

public interface IGoodsCategoryDao {
	
	/**
	 * 获取商品分类
	 * 
	 * @param shopId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<GoodsCategoryDto> getShopGoodsCategory(Long shopId,Integer columnId,String parentCategoryId, Integer page, Integer pageSize) throws Exception ;
	
	Integer getShopGoodsCategoryTotal(Long shopId,Integer columnId,String parentCategoryId) throws Exception ;
	
	List<GoodsCategoryDto> queryGoodsCategory(GoodsCategoryDto goodsCategory) throws Exception;
	GoodsCategoryDto queryGoodsCategoryById(Long goodsCategoryId) throws Exception;
	Integer queryGoodsCategoryLastIndex(GoodsCategoryDto goodsCategory) throws Exception;
	/**
	 * 分页获取商品分类
	* @Title: getGoodsCategoryByPage 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param pageModel
	* @param @return
	* @param @throws Exception
	* @return PageModel    返回类型 
	* @throws
	 */
	PageModel getGoodsCategoryByPage(PageModel pageModel)throws Exception;
	
	List<GoodsCategoryDto>getGoodsCategoryByCategoryPid(Long parentCategoryId)throws Exception;
	
	/**
	 * 获取指定的商品分类
	 * @param id
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getGoodsCategoryById(Long id)throws Exception;
	
	/**
	 * 获取商品族分类
	 * @param shopId
	 * @param goodsGroupId
	 * @param parentCategoryId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<GoodsCategoryDto> getShopGoodsGroupCategory(Long shopId,Integer columnId,String goodsGroupId,String parentCategoryId, Integer page, Integer pageSize) throws Exception ;
	
	/**
	 * 获取商品族分类数量
	 * @param shopId
	 * @param parentCategoryId
	 * @return
	 * @throws Exception
	 */
	Integer getShopGoodsGroupCategoryTotal(Long shopId,String goodsGroupId,String parentCategoryId) throws Exception ;

	/**
	 * 获取商铺分类(树形机构)
	 * @param shopId
	 * @param parentCategoryId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<Map> getShopCategory(Long shopId, String parentCategoryId, Integer page, Integer pageSize) throws Exception;
    /**
     * 查询商铺商品销售统计接口
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getGoodsSalesStat(Map<String, Object> pMap) throws Exception;
    /**
     * 查询商铺商品销售统计列表总数
     * @return
     * @throws Exception
     */
    Integer getGoodsSalesStatCount(Map<String, Object> pMap) throws Exception;
    
    /**
     * 查询商铺分类商品销售统计
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getGoodsCategorySalesStat(Map<String, Object> pMap) throws Exception;
    /**
     * 查询商铺分类商品销售统计列表总数
     * @return
     * @throws Exception
     */
    Integer getGoodsCategorySalesStatCount(Map<String, Object> pMap) throws Exception;
    
    /**
     * 根据商品id列表获取
     * 商品列表
     * @Title: getCategoryListByIdList 
     * @param @param goodsCategoryIdList
     * @param @return
     * @param @throws Exception
     * @return List<GoodsCategoryDto>    返回类型 
     * @throws
     */
	List<GoodsCategoryDto> getCategoryListByIdList(
			List<String> goodsCategoryIdList)throws Exception;
	
	int addGoodsCategory(GoodsCategoryDto goodsCategory) throws Exception;
	List<Long> queryGoodsCategoryIdsByShopId(Long shopId) throws Exception;

	List<GoodsCategoryDto> getSubGoodsCategoryByPid(List<Long> categoryIdList);
}
