package com.idcq.appserver.dao.goods;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.GoodsBarcodeDto;
import com.idcq.appserver.dto.goods.GoodsDto;
import com.idcq.appserver.dto.goods.GoodsSetDto;
import com.idcq.appserver.dto.goods.GoodsUnitDto;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;
/**
 * 商品(服务)dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:32:10
 */
@Repository
public class GoodsDaoImpl extends BaseDao<GoodsDto>implements IGoodsDao{

	
	public Integer getDigitScaleOfGoodsUnit(Long goodsId) throws Exception {
		return (Integer)super.selectOne(generateStatement("getDigitScaleOfGoodsUnit"), goodsId);
	}

	public int statisGoodsSoldExecute(Date startTime, Date endTime)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return super.update(generateStatement("statisGoodsSoldExecute"),map);
	}
	
	public GoodsDto getGoodsByIdFromDb(Long id) throws Exception {
		return (GoodsDto)selectOne(generateStatement("getGoodsById"),id);
	}
	
	public GoodsDto getGoodsById(Long id) throws Exception {
		return (GoodsDto)HandleCacheUtil.getEntityCacheByClass(GoodsDto.class, id, CommonConst.CACHE_EX_TIME_GOODS);
	}

	public List<GoodsDto> getGoodsList(GoodsDto goods, Integer page, Integer pageSize)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("goods", goods);
		map.put("m", page);
		map.put("n", pageSize);
		return (List)super.findList(generateStatement("getGoodsList"),map);
	}

	public List<GoodsDto> getGoodsListToIndex(GoodsDto goods, Integer startId,
			Integer pageSize) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("goods", goods);
		map.put("n", startId);
		map.put("m", pageSize);
		return (List)super.findList(generateStatement("getGoodsListToIndex"),map);
	}

	public int getLastId() throws Exception {
		return (Integer)selectOne(generateStatement("getLastId"),1);
	}

	public int getCountByGoodId(Long goodId) {
		return (Integer)selectOne(generateStatement("getCountByGoodId"),goodId);
	}
	
	/**
	 * 获得商品列表根据id 集合
	 */
	public List<GoodsDto> getGoodsListByIds(List<Long> idList) {
		return findList(generateStatement("getGoodsByIdList"),idList);
	}
	
	public int updateGoodsZan(Long goodsId) throws Exception {
		return super.update(generateStatement("updateGoodsZan"), goodsId);
	}
		
	public Integer getGoodsZanById(Long goodsId) throws Exception {
		return (Integer)selectOne(generateStatement("getGoodsZanById"),goodsId);
	}
	
	public int queryGoodIsExist(GoodsDto good) throws Exception {
		return (Integer)selectOne(generateStatement("queryGoodIsExist"),good);
	}
	
	@Override
	public GoodsDto queryExistGood(GoodsDto good) throws Exception {
		return (GoodsDto)selectOne(generateStatement("queryExistGood"),good);
	}
	@Override
	public List<GoodsDto> queryGoods(GoodsDto good) throws Exception {
		return (List)super.findList(generateStatement("queryGoods"),good);
	}
	@Override
	public List<Long> queryGoodsIdList(GoodsDto good) throws Exception {
		return (List)super.findList(generateStatement("queryGoodsIdList"),good);
	}
	/**
	 * 获得solr爬取商品列表数据的接口
	 */
	public PageModel getGoodsListForSearch(GoodsDto goods,PageModel pageModel,int... queryTotalCount) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("goods", goods);
		return super.findPagedList(generateStatement("getGoodsListForSearch"), generateStatement("getTotalCount"), pageModel,queryTotalCount);
	}

	public int validGoodsExists(Long shopId, Long goodsId)
			throws Exception {
		GoodsDto goods = (GoodsDto)HandleCacheUtil.getEntityCacheByClass(GoodsDto.class, goodsId, CommonConst.CACHE_EX_TIME_GOODS);
		if(goods != null){
			return shopId.longValue() == goods.getShopId() ? 1 : 0;
		}else{
			return 0;
		}
	}

	public GoodsDto getGoodsByIds(Long shopId, Long goodsId)
			throws Exception {
		GoodsDto goods = (GoodsDto)HandleCacheUtil.getEntityCacheByClass(GoodsDto.class, goodsId, CommonConst.CACHE_EX_TIME_GOODS);
		if(goods != null){
			return shopId.longValue() == goods.getShopId() ? goods : null;
		}else{
			return null;
		}
	}
	

	
	public int incrGoodsSoldNum(Long goodsId, Long soldNum)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("goodsId", goodsId);
		map.put("soldNum", soldNum);
		return super.update(generateStatement("incrGoodsSoldNum"),map);
	}

	/** 
	 * 获取商品总条数
	* @Title: getGoodsTotalCount 
	* @Description:  
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public int getGoodsTotalCount() throws Exception {
		return (int)super.selectOne(generateStatement("getTotalCount"), new HashMap<String,String>());
	}

	/** 
	 * 根据时间戳方式找出需要建索引的商品
	 * @Title: getGoodsByPageAndLastUpdateTime 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param @param lastUpdateTime
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public List<GoodsDto> getGoodsByPageAndLastUpdateTime(
			String lastUpdateTime, PageModel pageModel) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("lastUpdateTime", lastUpdateTime);
		params.put("start", (pageModel.getToPage()-1)*pageModel.getPageSize());
		params.put("limit",pageModel.getPageSize());
		return super.findList(generateStatement("getGoodsByPageAndLastUpdateTime"),params);
	}

	/** 
	 * 分页从临时表获取商品数据
	 * @Title: getGoodsDataFromTemporyTable 
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public List<GoodsDto> getGoodsDataFromTemporyTable(PageModel pageModel)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("start", (pageModel.getToPage()-1)*pageModel.getPageSize());
		params.put("limit",pageModel.getPageSize());
		return super.findList(generateStatement("getGoodsDataFromTemporyTable"),params);
	}

	/** 
	 * 情况临时商品表
	 * @Title: deleteTemporyGoodsByMaxGoodsId 
	 * @param @param goodsId
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public void deleteTemporyGoodsByMaxGoodsId() throws Exception {
		super.update(generateStatement("clearTemporyGoodsByMaxId"),new HashMap());
	}

	/** 
	 * @Title: getGoodsReplicationCount 
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public int getGoodsReplicationCount(){
		return (int)super.selectOne(generateStatement("getGoodsReplicationCount"), new HashMap<String,String>());
	}
	
	/**
	 * 根据商品Id获取总条数
	 * @Title: getTotalCountByShopId 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public int getTotalCountByShopId(Long shopId) throws Exception {
		return (int)super.selectOne(generateStatement("getTotalCountByShopId"), new HashMap<String,String>());
	}

	@Override
	public List<GoodsDto> getGoodsListByShopId(Long shopId,PageModel pageModel){
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("start", (pageModel.getToPage()-1)*pageModel.getPageSize());
		params.put("limit",pageModel.getPageSize());
		params.put("shopId",shopId);
		return super.findList(generateStatement("getGoodsListByShopId"),params);
	}
	
	/**
	 * 根据商铺id列表查找每个商铺的销量最高的两个商品
	 * 根据销量将商品进行排序
	 * @Title: getGoodsGroupByShopIdList 
	 * @param @param shopIdList
	 * @param @return
	 * @param @throws Exception
	 * @return List<GoodsDto>    返回类型 
	 * @throws
	 */
	public List<GoodsDto> getGoodsGroupByShopIdList(List<Long> shopIdList,
			int goodsNum) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopIdList", shopIdList);
		params.put("goodsNum", goodsNum);
		return super.findList(generateStatement("getGoodsGroupByShopIdList"),params);
	}
		

	@Override
	public String getGoodsLogo1ById(Long goodsId) throws Exception {
		return (String)super.selectOne(generateStatement("getGoodsLogo1ById"), goodsId);
	}

	public Long addGoodsDto(GoodsDto goodsDto) {
		this.insert(generateStatement("addGoodsDto"), goodsDto);
		Long goodsId = goodsDto.getGoodsId();
		return goodsId;
	}

	@Override
	public int batchAddGoodsDto(List<GoodsDto> goodList) throws Exception{
		Map<String, List<GoodsDto>> mapper = new HashMap<String, List<GoodsDto>>();
		mapper.put("goodList", goodList);
		return this.insert(generateStatement("batchAddGoodsDto"), mapper);
	}
	
	@Override
	public int batchUpdateGoodsDto(List<GoodsDto> goodList) throws Exception{
		Map<String, List<GoodsDto>> mapper = new HashMap<String, List<GoodsDto>>();
		mapper.put("goodList", goodList);
		return this.insert(generateStatement("batchUpdateGoodsDto"), mapper);
	}
	public int updateGoods(GoodsDto goodsDto) throws Exception {
		return this.update(generateStatement("updateGoodsDto"), goodsDto);
	}
	public int updateGoodsStandardPrice(GoodsDto goodsDto) throws Exception {
		return this.update(generateStatement("updateGoodsStandardPrice"), goodsDto);
	}

	public int batchUpdateGoodsById(List<Long> list) throws Exception {
		return this.update(generateStatement("batchUpdateGoodsById"), list);
	}

	
	/***
	 * 查找唯一的商品族编号根据商品表的索引更新时间
	 * @Title: getDistinctGoodsGroupIdForIndex 
	 * @param @param lastUpdateTime
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<Object> getDistinctGoodsGroupIdForIndex(String lastUpdateTime)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("lastUpdateTime", lastUpdateTime);
		return (List<Object>)getSqlSession().selectList(generateStatement("getDistinctGoodsGroupIdForIndex"),params);
	}

	@Override
	public List<GoodsDto> getGoodNameListByGroupId(List<Long> goodsGroupList)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("groupIdList", goodsGroupList);
		return findList(generateStatement("getGoodNameListByGroupId"),params);
	}

	@Override
	public List<GoodsDto> getGoodsByGroupMap(Map<String,Object>params) throws Exception {
		return findList(generateStatement("getGoodsByGroupMap"),params);
	}

	@Override
	public int updateGoodsStatus(Map<String, Object> map) throws Exception {
		return super.update("updateGoodsStatus",map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsDao#getGoodsList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getGoodsList(Map<String, Object> map)
			throws Exception {
		return (List)findList(generateStatement("getGoodsListByCollect"),map);
	}

	
	@Override
	public int queryGoodsExists(Long goodsId) throws Exception {
		GoodsDto goods = (GoodsDto)HandleCacheUtil.getEntityCacheByClass(GoodsDto.class, goodsId, CommonConst.CACHE_EX_TIME_GOODS);
		return goods != null ? 1 : 0;
	}

	@Override
	public int updateGoodsPrice(GoodsDto goodsDto) throws Exception {
		return super.update("updateGoodsPrice",goodsDto);
	}

	@Override
	public int syncGoodsInfoOfGg(GoodsDto goodsDto) throws Exception {
		return super.update("syncGoodsInfoOfGg",goodsDto);
	}

	@Override
	public List<Object> getGoodsIdListOfGg(Long ggId) throws Exception {
		return super.getSqlSession().selectList("getGoodsIdListOfGg",ggId);
	}

	@Override
	public int updateGoodsNameBatch(List<Map<String, Object>> list)
			throws Exception {
		return super.update("updateGoodsNameBatch",list);
	}

	public int updateGoodsStatsIsDelByGoodsGroupId(Long goodsGroupId, int status)
			throws Exception {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("goodsGroupId", goodsGroupId);
		parameter.put("goodsStatus", status);
		return update(generateStatement("updateGoodsStatsIsDelByGoodsGroupId"), parameter);
	}

	public int addGoodsZanLog(Long userId, Long goodsId, Integer zanStatus) {
		Map param = new HashMap();
		param.put("userId", userId);
		param.put("goodsId", goodsId);
		param.put("zanStatus", zanStatus); //点赞
		return insert(generateStatement("addGoodsZanLog"), param);
	}

	public Long getShopGoodsIdByBarcode(Long shopId, String barcode) {
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("shopId", shopId);
		parameter.put("barcode", barcode);
		return super.getSqlSession().selectOne(generateStatement("getShopGoodsIdByBarcode"), parameter);
	}

	public int updateGoodsDto(GoodsDto goodsDto) {
		return super.update("updateGoodsDto", goodsDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.goods.IGoodsDao#queryGoodGroupExist()
	 */
	@Override
	public Long queryGoodGroupExist(Long goodsId) throws Exception {
		return super.getSqlSession().selectOne(generateStatement("queryGoodGroupExist"), goodsId);
	}

	public int updateGoodsInfoStatus(Long shopId, List<Long> goodsIds,
			Integer operateType) throws Exception {
		Map<String,Object> parameter = new HashMap<String, Object>();
		parameter.put("shopId", shopId);
		parameter.put("goodsIds", goodsIds);
		parameter.put("operateType", operateType);
		return super.update(generateStatement("updateGoodsInfoStatus"), parameter);
	}

	@Override
	public Double sumGoodsPirce(List<GoodsSetDto> list) throws Exception {
		return (Double) super.selectOne("sumGoodsPirce", list);
	}

	@Override
	public Integer addGoodsBarcode(GoodsBarcodeDto goodsBarcodeDto) {
		this.insert(generateStatement("addGoodsBarcode"), goodsBarcodeDto);
		Integer barcodeId = goodsBarcodeDto.getId();
		return barcodeId;
		
	}

	@Override
	public int updateGoodsBarcode(GoodsBarcodeDto goodsBarcodeDto) {
		return this.update(generateStatement("updateGoodsBarcode"), goodsBarcodeDto);
		
	}

	@Override
	public void updateGoodsUnit(GoodsUnitDto goodsUnitDto) {
		super.update(generateStatement("updateGoodsUnit"),goodsUnitDto);
	}

	@Override
	public void insertGoodsUnit(GoodsUnitDto goodsUnitDto) {
		super.insert(generateStatement("insertGoodsUnit"), goodsUnitDto);
	}

	@Override
	public GoodsUnitDto getShopGoodUnit(GoodsUnitDto goodsUnitDto) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("unitId", goodsUnitDto.getUnitId());
		paramMap.put("unitName", goodsUnitDto.getUnitName());
		paramMap.put("shopId", goodsUnitDto.getShopId());
		return (GoodsUnitDto)super.selectOne(generateStatement("getShopGoodUnit"),paramMap) ;
	}
	
	@Override
	public GoodsUnitDto getShopGoodUnitByUnitId(Integer unitId) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("unitId", unitId);
		return (GoodsUnitDto)super.selectOne(generateStatement("getShopGoodUnitByUnitId"),paramMap) ;
	}

	@Override
	public int delGoodsUnit(Map<String, Object> result) {
		return super.delete("delGoodsUnit", result);
	}

	@Override
	public List<GoodsUnitDto> getGoodsUnitList(Map<String, Object> result) {
		return (List)super.findList(generateStatement("getGoodsUnitList"),result);
	}

	@Override
	public int getGoodsUnit(Integer unitId) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("unitId", unitId);
		return (int)super.selectOne(generateStatement("getGoodsUnit"), paramMap);
	}

	@Override
	public List<GoodsUnitDto> getGoodsUnitByUnitNameAndShopId(
			Map<String, Object> result) {
		return (List)super.findList(generateStatement("getGoodsUnitByUnitNameAndShopId"),result);
	}

	@Override
	public int getGoodsByUnitId(Integer unitId) {
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("unitId", unitId);
		return (int)super.selectOne(generateStatement("getGoodsByUnitId"), paramMap);
	}

	@Override
	public int isExistSameName(Map<String, Object> map) {
		return (int)super.selectOne(generateStatement("getGoodsUnit"), map);
	}

	@Override
	public int getGoodsSalestatisticsCount(Map<String, Object> param) {
		return (int)super.selectOne(generateStatement("getGoodsSalestatisticsCount"), param);
	}

	@Override
	public List<Map<String, Object>> getGoodsSalestatistics(
			Map<String, Object> param) {
		return (List)getSqlSession().selectList(generateStatement("getGoodsSalestatistics"),param);
	}

	
	public int getGoodsCountByCategoryId(Long shopId,Long categoryId,Integer goodsType)throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("categoryId", categoryId);
		map.put("goodsType", goodsType);
		return (Integer) super.selectOne(generateStatement("getGoodsTotalByCategoryId"), map);
	}

	@Override
	public Double getGoodsSaleSalestatistics(Map<String, Object> param) {
		return (Double) super.selectOne("getGoodsSaleSalestatistics", param);
	}


	@Override
	public List<GoodsDto> queryGoodsByShopIdAndCategoryId(GoodsDto goodsDto, int pNo, int pSize) {
		pNo = pNo <= 0 ? 1 : pNo;
		RowBounds rowBounds = new RowBounds((pNo - 1) * pSize, pNo * pSize);
		return this.getSqlSession().selectList(generateStatement("queryGoodsByShopIdAndCategoryId"), goodsDto, rowBounds);
	}
}
