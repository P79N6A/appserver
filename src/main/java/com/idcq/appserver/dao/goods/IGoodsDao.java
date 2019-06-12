package com.idcq.appserver.dao.goods;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsBarcodeDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.GoodsUnitDto;

public interface IGoodsDao {
	/**
	 * 调用存储过程定时统计商品销售数量
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	int statisGoodsSoldExecute(Date startTime,Date endTime) throws Exception;
	int batchAddGoodsDto(List<GoodsDto> goodList) throws Exception;
	int batchUpdateGoodsDto(List<GoodsDto> goodList) throws Exception;
	GoodsDto queryExistGood(GoodsDto good) throws Exception;
	/**
	 * 获取指定商品
	 * 
	 * @param product
	 * @return
	 * @throws Exception
	 */
	GoodsDto getGoodsByIdFromDb(Long id) throws Exception;
	
	/**
	 * 获取指定的商品
	 * 
	 * @param shopId	非必填，可以为null
	 * @param goodsId	必填
	 * @return
	 * @throws Exception
	 */
	GoodsDto getGoodsByIds(Long shopId, Long goodsId) throws Exception ;
	List<Long> queryGoodsIdList(GoodsDto good) throws Exception;
	
	/**
	 * 验证商品存在性
	 * 
	 * @param shopId 商铺ID，非必填
	 * @param goodsId 商品ID，非必填
	 * @return
	 * @throws Exception
	 */
	int validGoodsExists(Long shopId, Long goodsId) throws Exception ;
	
	/**
	 * 验证商品存在性
	 *
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	int queryGoodsExists(Long goodsId) throws Exception ;
	
	/**
	 * 获取商品计量单位支持小数位数
	 * 
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	Integer getDigitScaleOfGoodsUnit(Long goodsId) throws Exception ;
	
	/**
	 * 获取商品列表
	 * 
	 * @param goods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<GoodsDto> getGoodsList(GoodsDto goods, Integer page, Integer pageSize) throws Exception ;
	
	int queryGoodIsExist(GoodsDto good) throws Exception;
	/**
	 * 为商品全文索引获取商品列表
	 * 
	 * @param goods
	 * @param startId
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	List<GoodsDto> getGoodsListToIndex(GoodsDto goods, Integer startId, Integer pageSize) throws Exception ;
	
	int getLastId() throws Exception;
	/**
	 * 根据goodId查询商品数量
	 * @return
	 */
	int getCountByGoodId(Long goodId);
	
	/**
	 * 获得商品列表
	 * @param idList
	 * @return
	 */
	List<GoodsDto>getGoodsListByIds(List<Long>idList);
	
	/**
	 * 根据商品id修改商品点赞次数
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	int updateGoodsZan(Long goodsId) throws Exception;
	
	/**
	 * 获取商品点赞次数
	 * @param goodsId
	 * @return
	 * @throws Exception
	 */
	Integer getGoodsZanById(Long goodsId) throws Exception;
	
	/**
	 * solr爬取数据接口
	 * @param goods
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getGoodsListForSearch(GoodsDto goods,PageModel pageModel,int... queryTotalCount) throws Exception ;
	
	/**
	 * 增加商品销售数量
	 * 
	 * @param goodsId
	 * @param soldNum	增加的销售数量
	 * @return
	 * @throws Exception
	 */
	int incrGoodsSoldNum(Long goodsId, Long soldNum) throws Exception;
	
	/**
	 * 获取商铺商品列表
	* @Title: getGoodsTotalCount 
	* @Description: TODO
	* @param @return
	* @param @throws Exception
	* @return int    返回类型 
	* @throws
	 */
	int getGoodsTotalCount()throws Exception;
	
	
	int getGoodsReplicationCount();
	/**
	 * 查找出需要更新索引的商品，根据时间戳方式
	* @Title: getGoodsByPageAndLastUpdateTime 
	* @Description: TODO
	* @param @param lastUpdateTime
	* @param @param pageModel
	* @param @return
	* @param @throws Exception
	* @return List<GoodsDto>    返回类型 
	* @throws
	 */
	List<GoodsDto>getGoodsByPageAndLastUpdateTime(String lastUpdateTime,PageModel pageModel)throws Exception;
	
	/**
	 * 分页从临时商品表获取商品
	 * @Title: getGoodsDataFromTemporyTable 
	 * @Description: TODO
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception
	 * @return List<GoodsDto>    返回类型 
	 * @throws
	 */
	List<GoodsDto>getGoodsDataFromTemporyTable(PageModel pageModel)throws Exception;
	
	/**
	 * 根据最大商品id删除小于该值的所有商品
	* @Title: deleteTemporyGoodsByMaxGoodsId 
	* @Description: TODO
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	void deleteTemporyGoodsByMaxGoodsId()throws Exception;
	
	/**
	 * 根据商铺编号获取商品总条数
	* @Title: getTotalCountByShopId 
	* @param @param shopId
	* @param @return
	* @param @throws Exception
	* @return int    返回类型 
	* @throws
	 */
	int getTotalCountByShopId(Long shopId)throws Exception;
	
	
	List<GoodsDto>getGoodsListByShopId(Long shopId,PageModel pageModel);
	
	/**
	 * 根据商铺id列表查找每个商铺的销量最高的两个商品
	 * @Title: getGoodsGroupByShopIdList 
	 * @param @param shopIdList
	 * @param @return
	 * @param @throws Exception
	 * @return List<GoodsDto>    返回类型 
	 * @throws
	 */
	List<GoodsDto>getGoodsGroupByShopIdList(List<Long>shopIdList,int goodsNum)throws Exception;
	/**
	 * 根据goodsid查询商品logo
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	String getGoodsLogo1ById(Long goodsId) throws Exception;
	
	/**
	 * 添加商品信息
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 */
	Long addGoodsDto(GoodsDto goodsDto) throws Exception;
	
	int updateGoods(GoodsDto goodsDto) throws Exception;
	
	GoodsDto getGoodsById(Long goodsId) throws Exception;
	/**
	 * 获取商品族关联商品的ID集合
	 * @param ggId
	 * @return
	 * @throws Exception
	 */
	List<Object> getGoodsIdListOfGg(Long ggId) throws Exception;
	
	/**
	 * 修改商品的各个价格
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 */
	int updateGoodsPrice(GoodsDto goodsDto) throws Exception;
	
	/**
	 * 批量修改商品名称
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 */
	int updateGoodsNameBatch(List<Map<String,Object>> list) throws Exception;
	
	/**
	 * 修改商品族信息时同步部分信息到商品族关联商品列表
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 */
	int syncGoodsInfoOfGg(GoodsDto goodsDto) throws Exception;
	
	/**
	 * 修改商品价格
	 * @param goodsDto
	 * @return
	 * @throws Exception
	 */
	int updateGoodsStandardPrice(GoodsDto goodsDto) throws Exception;
	
	/**
	 * 修改商品状态
	 * @param map
	 * @return
	 * @throws Exception
	 */
	int updateGoodsStatus(Map<String,Object> map) throws Exception;
	
	/**
	 * 批量修改商品状态为删除
	 * @param list
	 * @return
	 * @throws Exception
	 */
	int batchUpdateGoodsById(List<Long> list) throws Exception;
	
	
	/**
	 * 获得需要索引的商品族列表
	 * @Title: getDistinctGoodsGroupIdForIndex 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception
	 * @return List<Integer>    返回类型 
	 * @throws
	 */
	List<Object>getDistinctGoodsGroupIdForIndex(String lastUpdateTime)throws Exception;
	
	/**
	 * 根据商品族编号查找所有商品的名称
	 * @Title: getGoodNameListByGroupId 
	 * @param @param goodsGroupId
	 * @param @return
	 * @param @throws Exception
	 * @return List<String>    返回类型 
	 * @throws
	 */
	List<GoodsDto>getGoodNameListByGroupId(List<Long> goodsGroupList)throws Exception;
	
	List<GoodsDto>getGoodsByGroupMap(Map<String, Object> parms)throws Exception;
	/**
	 * 获取商铺菜品信息
	 * @Function: com.idcq.appserver.dao.goods.IGoodsDao.getGoodsList
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月20日 上午10:45:30
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月20日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getGoodsList(Map<String, Object> map)throws Exception;
	
	/**
	 * 更新商品族下面所有商品的状态
	 * @param goodsGroupId 商品族编号
	 * @param status 状态
	 * @return
	 * @throws Exception
	 * @author:nie_jq
	 */
	int updateGoodsStatsIsDelByGoodsGroupId(Long goodsGroupId,int status) throws Exception;
	
	/**
	 * 记录点赞日志
	 * 
	 * @Function: com.idcq.appserver.dao.goods.IGoodsDao.addGoodsZanLog
	 * @Description:
	 *
	 * @param userId 用户id
	 * @param goodsId 商品id
	 * @param zanStatus 点赞状态
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月16日 上午10:05:48
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月16日    shengzhipeng       v1.0.0         create
	 */
	int addGoodsZanLog(Long userId, Long goodsId, Integer zanStatus);
	
	/**
	 * 获取店铺对应条形码商品数量
	 * 
	 * @Function: com.idcq.appserver.dao.goods.IGoodsDao.getShopGoodsByBarcode
	 * @Description:
	 *
	 * @param shopId 店铺id
	 * @param barcode 条形码
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 下午3:02:05
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	Long getShopGoodsIdByBarcode(Long shopId, String barcode);
	
	int updateGoodsDto(GoodsDto goodsDto);
	/**
	 * 查询商品是否属于商品族
	 * 
	 * @Function: com.idcq.appserver.dao.goods.IGoodsDao.queryGoodGroupExist
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月23日 下午3:11:11
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    ChenYongxin      v1.0.0         create
	 */
	Long queryGoodGroupExist(Long goodsId) throws Exception;
	
	/**
	 * 变更商品状态
	 * @param shopId 商铺编号
	 * @param goodsIds 商品编号集合
	 * @param operateType 下架-0，上架-1，删除-2
	 * @return
	 * @throws Exception
	 */
	int updateGoodsInfoStatus(Long shopId, List<Long> goodsIds, Integer operateType) throws Exception;
	
	/** 
	* @Title: IGoodsDao.java
	* @Description: 查询出指定商品的总价
	* @param @param list
	* @param @return
	* @param @throws Exception    
	* @throws 
	*/
	Double sumGoodsPirce(List<GoodsSetDto> list) throws Exception;
	Integer addGoodsBarcode(GoodsBarcodeDto goodsBarcodeDto);
	int updateGoodsBarcode(GoodsBarcodeDto goodsBarcodeDto);
	/**
	 * 修改商品单位接口
	 * @param goodsUnitDto
	 */
	public void updateGoodsUnit(GoodsUnitDto goodsUnitDto);
	/**
	 *  新增商品单位接口
	 * @param goodsUnitDto
	 */
	public void insertGoodsUnit(GoodsUnitDto goodsUnitDto);
	
	/**
	 * 查询单位信息
	 * @param unitId
	 * @return
	 */
	public GoodsUnitDto getShopGoodUnit(GoodsUnitDto goodsUnitDto);
	
	/**
	 * 查询单位信息
	 * @param unitId
	 * @return
	 */
	public GoodsUnitDto getShopGoodUnitByUnitId(Integer unitId);
	
	/**
	 * 删除商铺单位
	 * @param result
	 */
	int delGoodsUnit(Map<String, Object> result);
	/**
	 * 查询商铺单位
	 * @param result
	 * @return
	 */
	List<GoodsUnitDto> getGoodsUnitList(Map<String, Object> result);
	int getGoodsUnit(Integer unitId);
	List<GoodsUnitDto> getGoodsUnitByUnitNameAndShopId(Map<String, Object> result);
	int getGoodsByUnitId(Integer unitId);
	int isExistSameName(Map<String, Object> map);
	int getGoodsSalestatisticsCount(Map<String, Object> param);
	List<Map<String, Object>> getGoodsSalestatistics(Map<String, Object> param);
	/**
	 * 查询商品分类下的商品数量
	 * @param categoryId
	 * @param shopId
	 * @param goodsType 
	 * @return
	 */
	int getGoodsCountByCategoryId(Long shopId,Long categoryId, Integer goodsType)throws Exception;
	
	List<GoodsDto> queryGoods(GoodsDto good) throws Exception;
	Double getGoodsSaleSalestatistics(Map<String, Object> param);

	List<GoodsDto> queryGoodsByShopIdAndCategoryId(GoodsDto goodsDto, int pNo, int pSize);
}
