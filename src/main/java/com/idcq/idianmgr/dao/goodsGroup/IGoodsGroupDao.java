/**
 * 
 */
package com.idcq.idianmgr.dao.goodsGroup;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.VerifyGoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupDto;
import com.idcq.idianmgr.dto.goodsGroup.GoodsGroupPropertyDto;

/**
 * 
 * @author zhq
 *
 */
public interface IGoodsGroupDao {
	/**
	 * 根据商品族编号及商铺编号查询商品族内商品列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map> getGoodsListByGroupId(Map<String, Object> param)throws Exception;
	public Integer queryGoodsGroupLastSequence() throws Exception;
	public Double getGoodsGroupMaxPrice(Long goodsGroupId) throws Exception;
	public Double getGoodsGroupMinPrice(Long goodsGroupId) throws Exception;
	/**
	 * 根据商品族内商品编号查询商品属性信息
	 * @param list 商品编号集合
	 * @return
	 * @throws Exception
	 */
	List<Map> getGoodsPropertyListByGoodsIds(List<Map> list)throws Exception;
	
	/**
	 * 添加商品族
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	Long addGoodsGroup(GoodsGroupDto dto) throws Exception;
	
	/**
	 * 修改商品族
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	int updateGoodsGroup(GoodsGroupDto dto) throws Exception;

	/**
	 * 根据商品族编号查询商品族内所有商品编号列表
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Long> getGoodsIdsByGroupIdAndShopId(Map<String,Object> param) throws Exception ;

	/**
	 * 根据编号查询对象信息
	 * @param goodsGroupId
	 * @return
	 * @throws Exception
	 */
	GoodsGroupDto getGoodsGroupById(Long goodsGroupId) throws Exception ;
	
	/**
	 * 批量修改商品族的状态
	 * @param ids
	 * @param status
	 * @return
	 * @throws Exception
	 */
	int updateGoodsGroupStatusBatch(List<Map<String,Object>> list) throws Exception ;
	
	/**
	 * 修改商品族状态
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateGoodsGroupStatus(Map<String,Object> map) throws Exception ;
	
	/**
	 * 分页获取商品族需要索引的记录
	 * @Title: getGoodsByPageAndLastUpdateTime 
	 * @param @param lastUpdateTime
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception
	 * @return List<GoodsGroupDto>    返回类型 
	 * @throws
	 */
	List<GoodsGroupDto>getGoodsByPageAndLastUpdateTime(String lastUpdateTime,PageModel  pageModel)throws Exception;
	
	List<GoodsGroupDto>findGoodsGroupByIdList(List idList)throws Exception;
	
	
	/**
	 * 针对查询搜索获得商品列表
	 * @param goods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsGroupListForSearch(GoodsDto goods,PageModel pageModel) throws Exception ;
	
	/**
	 * 检查是否有待处理的订单
	 * @param ggId
	 * @return
	 * @throws Exception
	 */
	int queryValidOrderByGgId(Long ggId) throws Exception ;
	
	/**
	 * 删除商品族
	 * @param ggId
	 * @return
	 * @throws Exception
	 */
	int delGoodsGroupById(Long ggId) throws Exception;
	
	/**
	 * 批量删除商品族
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int delGoodsGroupBatch(List<Map<String,Object>> ids) throws Exception;
	
	/**
	 * 分页获取商品族列表
	 * @param shopId
	 * @param goodsKey
	 * @param pNo
	 * @param pSize
	 * @return
	 * @throws Exception
	 */
	List<GoodsGroupDto> getGoodsGroupList(Long shopId,String goodsKey,Integer status,int pNo,int pSize) throws Exception;
	
	/**
	 * 统计符合条件的商品族数量
	 * @param shopId
	 * @param goodsKey
	 * @return
	 * @throws Exception
	 */
	int getGoodsGroupCount(Long shopId,String goodsKey,Integer status) throws Exception;

	/**
	 * 根据分类编号、商铺编号查询商品族信息
	 * @param param
	 * @return
	 */
	GoodsGroupDto getGoodsGroupBycategoryIdAndShopId(Map<String, Object> param);
	
	/**
	 * 检查技师存在性
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int queryTechniExists(Long id) throws Exception;
	
	/**
	 * 检查商品族存在性
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int queryGoodsGroupExists(Long id) throws Exception;
	
	
	/**
	 * 获取草稿状态的集合
	 * @return
	 * @throws Exception
	 */
	public List<GoodsGroupDto> getDriftGoodsGroupList() throws Exception ;
	
	/**
	 * 根据商品族ID批量删除草稿状态的商品
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	int delGoodsBatchByGoodsGroupId(List<Map<String,Object>> ids) throws Exception;
	
	/**
	 * 查询商品族下是否绑定商品
	 * @param ggId
	 * @return
	 * @throws Exception
	 */
	int queryGoodsExistsByGgId(Long ggId) throws Exception;
	
	/**
	 * 查询指定商品族下是否已经绑定了商品族属性
	 * @param ggId
	 * @return
	 * @throws Exception
	 */
	int queryGgPropertyByGgId(Long ggId) throws Exception;
	
	/**
	 * 获取商品族条数
	 * @Title: getGoodsGroupCountByShopId 
	 * @param @param shopId
	 * @param @return
	 * @return Integer    返回类型 
	 * @throws
	 */
	Integer getGoodsGroupCountByShopId(Long shopId);
	
	
	PageModel getGoodsGroupListByCondition(GoodsGroupDto goodsGroupDto,PageModel pageModel);
	
	/**
	 * 查询商铺销量最高的两个商品族
	 * @Title: getListByShopIdListAndNum 
	 * @param @param needQueryShopIdForGoods
	 * @param @param num
	 * @param @return
	 * @return List<GoodsGroupDto>    返回类型 
	 * @throws
	 */
	List<GoodsGroupDto> getListByShopIdListAndNum(
			List<Long> needQueryShopIdForGoods, int num);

	int updateGoodsGroupZan(Long goodsId) throws Exception;
	/**
	 * 判断商品族是否存在
	 * @return
	 * @throws Exception
	 */
	List<GoodsGroupDto> getGoodsGroup(VerifyGoodsGroupDto vrifyGoodsGroupDto)throws Exception;
	
	List<Long> getGoodsGroupIdByShopId(Long shopId) throws Exception;
}	
