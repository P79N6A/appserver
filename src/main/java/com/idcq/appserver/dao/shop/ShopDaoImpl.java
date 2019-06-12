package com.idcq.appserver.dao.shop;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopEmployeeDto;
import com.idcq.appserver.dto.shop.ShopIncomeStatDto;
import com.idcq.appserver.utils.jedis.HandleCacheUtil;
import com.idcq.idianmgr.dto.shop.ShopBean;

/**
 * 商铺dao
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:19:18
 */
@Repository
public class ShopDaoImpl extends BaseDao<ShopDto>implements IShopDao{

	@Override
	public Integer getShopRefTotalMembers(Map<String, Object> map) {
		return (Integer)selectOne("getShopRefTotalMembers", map);
	}

	@Override
	public List<Map<String, Object>> getShopRefMembers(Map<String, Object> map) {
		return (List)findList("getShopRefMembers", map);
	}

	public int statisShopSoldExecute(Date startTime, Date endTime)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return super.update(generateStatement("statisShopSoldExecute"),map);
	}
	
	public List<ShopDto> getList(ShopDto shop, int page, int pageSize)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shop", shop);
		map.put("n", (page-1)*pageSize);                   
		map.put("m", pageSize);                                                                       
		return super.findList(generateStatement("getShopList"), map);
	}

	public List<ShopDto> searchShop(Map param) throws Exception {
		int page=Integer.parseInt((String) param.get("pageNO"));
		int pageSize=Integer.parseInt((String) param.get("pageSize"));
		param.put("n", (page-1)*pageSize);                   
		param.put("m", pageSize);           
		return super.findList(generateStatement("searchShop"), param);
	}

	public Integer searchShopTotal(Map param) throws Exception {
		return (Integer) super.selectOne(generateStatement("searchShopTotal"), param);
	}

	public Map getShopXyById(Long shopId) throws Exception {
		return (Map) super.selectOne(generateStatement("getShopXyById"), shopId);
	}

	public int getCountByShopId(Long shopId) {
		return (Integer)selectOne(generateStatement("getCountByShopId"),shopId);
	}
	
	@Override
	public int getShopPoint(Long shopId) {
		return (Integer)selectOne(generateStatement("getShopPoint"),shopId);
	}
	/**
	 * 根据id列表进行查询
	 */
	public List<ShopDto> getListByShopIds(List<Long> idList) {
		return findList(generateStatement("getShopsByShopIdList"),idList);
	}
	
	/**
	 * 分页查找
	 */
	public PageModel getShopPageModel(PageModel pageModel,ShopDto shopDto,int... queryTotalCount)
	{
		return super.findPagedList(generateStatement("getShopsByPage"), generateStatement("getTotalCount"), pageModel,queryTotalCount);
	}
	
  	public int updateShopZan(Long shopId) throws Exception{
		return super.update(generateStatement("updateShopZan"), shopId);
	}

	public Integer getShopZanById(Long shopId) throws Exception {
		return (Integer)selectOne(generateStatement("getShopZanById"),shopId);
	}
	
	/**
	 * 根据商铺编号找到商铺负责人
	 */
	public Long getUserIdByShopIed(Long shopId) {
		return (Long)selectOne(generateStatement("getUserIdByShopId"),shopId);
	}
	
	public ShopDto getShopExtendByIdAndStatus(Long shopId, Integer shopStatus)throws Exception
	{
	    Map<String,Object> map = new HashMap<String, Object>();
        map.put("shopId", shopId);
        map.put("shopStatus", shopStatus);
	    return (ShopDto)super.selectOne(generateStatement("getShopExtendByIdAndStatus"),map) ;
	}
	
	public int queryNormalShopExists(Long shopId) throws Exception {
		ShopDto pShop = (ShopDto)HandleCacheUtil.getEntityCacheByClass(ShopDto.class, shopId, CommonConst.CACHE_EX_TIME_SHOP);
		if(pShop != null)
		{
		    return 1;
		}
		return 0;
	}

	public int queryShopEmplExists(Long shopId, Long employeeId)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("employeeId", employeeId);
		return (Integer)super.selectOne(generateStatement("queryShopEmplExists"), map);
	}

	/** 
	* @Title: callTemporyIndexProcedure 
	* @Description: 
	* @param @param lastUpdateTime
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public void callTemporyIndexProcedure(String lastUpdateTime)
			throws Exception {
		Map<String,String>paramMap=new HashMap<String,String>();
		paramMap.put("prevUpdateTime", lastUpdateTime);
		this.selectOne("callTemporyIndexProcedure", paramMap);
	}

	public int incrShopSoldNum(Long shopId, Integer soldNum)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("soldNum", soldNum);
		return super.update(generateStatement("incrShopSoldNum"),map);
	}

	/** 
	* @Title: getShopTotalCount 
	* @Description: 
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public long getShopTotalCount() throws Exception {
		
		return (long)super.selectOne(generateStatement("getShopTotalCount"), new HashMap<String,String>());
	}

	
	/**
	 * 根据店铺的管理者ID 获取商铺
	 */
	public ShopDto getShopByPrincipalId(Long principalId) throws Exception {
		return (ShopDto)super.selectOne(generateStatement("getShopByPrincipalId"), principalId);
	}

	public List<Long> getIdListByPrincipalId(Long principalId) throws Exception {
		return (List)super.findList(generateStatement("getIdListByPrincipalId"),principalId);
	}
	/** 
	 * 获取需要重建索引的商铺
	* @Title: getShopByPageAndLastUpdateTime 
	* @param @param lastUpdateTime
	* @param @param pageModel
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	@Override
	public List<ShopDto> getShopByPageAndLastUpdateTime(String lastUpdateTime,
			PageModel pageModel) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("lastUpdateTime", lastUpdateTime);
		params.put("start", (pageModel.getToPage()-1)*pageModel.getPageSize());
		params.put("limit",pageModel.getPageSize());
		return super.findList(generateStatement("getShopByPageAndLastUpdateTime"),params);
	}

	/** 
	 * @Title: getShopMarketInfoByParam 
	 * @param @param lastUpdateTime
	 * @param @param pageModel
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public List<ShopDto> getShopMarketInfoByParam(String lastUpdateTime,
			PageModel pageModel) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("lastUpdateTime", lastUpdateTime);
		params.put("start", (pageModel.getToPage()-1)*pageModel.getPageSize());
		params.put("limit",pageModel.getPageSize());
		return super.findList(generateStatement("getShopMarketInfoByParam"),params);
	}

	/** 
	 * @Title: getShopEssentialInfo 
	 * @param @param shopId
	 * @param @return
	 * @param @throws Exception  
	 * @throws 
	 */
	@Override
	public ShopDto getShopEssentialInfo(Long shopId) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId", shopId);
		return (ShopDto)super.selectOne(generateStatement("getShopEssentialInfo"),params);
	}

	public Map queryShopStatus(Long shopId) throws Exception {
		return (Map)super.selectOne(generateStatement("queryShopStatus"), shopId);
	}

	@Override
	public Integer updateShopGrade(ShopDto shopDto) {
		return super.update(generateStatement("updateShopGrade"), shopDto);
	}

	public Double getMemberDiscount(Long shopId) throws Exception {
		Double discount = (Double) super.selectOne(
				generateStatement("getMemberDiscount"), shopId);
		return discount;
	}

	@Override
	public List<Long> getIdListByStatus(Integer limit, Integer pSize,Integer shopStatus)
			throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("limit", limit);
		params.put("pSize", pSize);
		params.put("shopStatus", shopStatus);
		return (List)super.findList(generateStatement("getIdList"),params);
	}

	public String getShopNameById(Long shopId) {
		return (String) super.selectOne(generateStatement("getShopNameById"), shopId);
	}

	@Override
	public Integer updateShopStatus(ShopDto shopDto) throws Exception {
		return super.update("updateShopStatus", shopDto);
	}
	public List<Map<String, Object>> getBaseShopList(Integer minPage, Integer maxPage)
			throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("minPage", minPage);
		params.put("maxPage", maxPage);
		return (List)super.findList(generateStatement("getBaseShopList"),params);
	}

	@Override
	public Double getDepositByPrincipalId(Long principalId) throws Exception {
		return (Double) super.selectOne(generateStatement("getDepositByPrincipalId"), principalId);
	}

	@Override
	public void giveShopDeposit(Long shopId, Double depositAmount) {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("shopId",shopId);
		params.put("amount",depositAmount);
		super.update(generateStatement("giveShopDeposit"),params);
	}
	
	@Override
	public Double getDepositByShopId(Long shopId) throws Exception {
		return (Double) super.selectOne(generateStatement("getDepositByShopId"), shopId);
	}

	@Override
	public String getShopPasswordById(Long shopId) throws Exception {
		return (String) super.selectOne(generateStatement("getShopPasswordById"), shopId);
	}

	@Override
	public Map<String, Object> shopOrderCount(Map<String, Object> map)
			throws Exception {
		return (Map) super.selectOne(generateStatement("shopOrderCount"), map);
	}

	@Override
	public Map<String, Object> getOrderAmountCount(Map<String, Object> map)
			throws Exception {
		return (Map) super.selectOne(generateStatement("getOrderAmountCount"), map);
	}

	@Override
	public List<Map<String, Object>> getBizLogo(Map<String, Object> map)
			throws Exception {
		return (List) super.findList(generateStatement("getBizLogo"), map);
	}
	
	@Override
	public Map<String, Object> getAttachmentInfo(Map<String, Object> map)
	        throws Exception {
	    return  (Map<String, Object>)super.selectOne(generateStatement("getAttachmentInfoById"), map);
	}

	@Override
	public Integer getBizLogoCount(Map<String, Object> map)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getBizLogoCount"), map);
	}	
	
	@Override
	public List<Map<String, Object>> getPlaceGoods(Map<String, Object> paramMap)
			throws Exception {
		return (List)findList(generateStatement("getPlaceGoods"), paramMap);
	}

	@Override
	public boolean isUsedResource(Map<String, Object> paramMap) {
		Integer count = (Integer)selectOne(generateStatement("isUsedResource"), paramMap);
		if(count != null && count>0 )
			return true;
		
		return false;
	}

	@Override
	public List<Map> queryNormalShopListBy(Long bizId, Integer bizType)
			throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("bizId",bizId);
		params.put("bizType",bizType);
		return super.getSqlSession().selectList(generateStatement("queryNormalShopListBy"), params);
	}

	@Override
	public Map queryShopEmployee(String mobile, Long shopId) throws Exception {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("mobile",mobile);
		params.put("shopId",shopId);
		return  super.getSqlSession().selectOne(generateStatement("queryShopEmployee"), params);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getOrderTotalCount(java.util.Map)
	 */
	@Override
	public Integer getOrderTotalCount(Map<String, Object> paramMap)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getOrderTotalCount"), paramMap);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getUserByShopId(java.lang.Long)
	 */
	@Override
	public Long getUserByShopId(Long shopId) {
		return (Long) super.selectOne(generateStatement("getUserByShopId"), shopId);

	}
	@Override
	public Integer getShopConfirmMinute(Long shopId) throws Exception {
		return (Integer)super.selectOne("getShopConfirmMinute", shopId);
	}

	@Override
	public Integer getIsHomeService(Long shopId) throws Exception {
		return (Integer)super.selectOne("getIsHomeService", shopId);
	}

	@Override
	public Integer getBookFlag(Long shopId) throws Exception {
		return (Integer)super.selectOne("getBookFlag", shopId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#bookSwitch(java.util.Map)
	 */
	@Override
	public Integer bookSwitch(Map<String, Object> mapParams) throws Exception {
		return super.update(generateStatement("bookSwitch"),mapParams);
	}

	@Override
	public Integer getTakeoutFlag(Long shopId) throws Exception {
		return (Integer)super.selectOne("getTakeoutFlag", shopId);
	}

	@Override
	public ShopDto getNormalShopById(Long shopId) throws Exception {
		 ShopDto pShop = (ShopDto)HandleCacheUtil.getEntityCacheByClass(ShopDto.class, shopId, CommonConst.CACHE_EX_TIME_SHOP);
		 return pShop;
	}

	@Override
	public ShopDto getShopById(Long shopId) throws Exception {
		if(shopId == null) {
			return null;
		}
		return (ShopDto)HandleCacheUtil.getEntityCacheByClass(ShopDto.class, shopId, CommonConst.CACHE_EX_TIME_SHOP);
	}

	@Override
	public ShopDto getShopFromDbById(Long primaryKeyId) throws Exception {
		return (ShopDto)super.selectOne(generateStatement("getById"), primaryKeyId);
	}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getAccountingStat(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getAccountingStat(Map<String, Object> map)
			throws Exception {
		return (List)super.findList("getAccountingStat", map);
	}
	@Override
	public Integer getAccountingStatCount(Map<String, Object> map)
			throws Exception {
		return (Integer)super.selectOne("getAccountingStatCount", map);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getOrderList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getOrderList(Map<String, Object> map) throws Exception {
		return (List)super.findList("getOrderList", map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getOrderListCount(java.util.Map)
	 */
	@Override
	public Integer getOrderListCount(Map<String, Object> map)
			throws Exception {
		return (Integer)super.selectOne("getOrderListCount", map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#queryShopServerList()
	 */
	@Override
	public List<Map<String, Object>> queryShopServerList(Map<String, Object> map) throws Exception {
		return (List)super.findList("queryShopServerList", map);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#insertEmployee(com.idcq.appserver.dto.shop.ShopEmployeeDto)
	 */
	@Override
	public Integer insertEmployee(ShopEmployeeDto shopEmployeeDto)
			throws Exception {
		return super.insert("insertEmployee", shopEmployeeDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#updateEmployee(com.idcq.appserver.dto.shop.ShopEmployeeDto)
	 */
	@Override
	public Integer updateEmployee(ShopEmployeeDto shopEmployeeDto)
			throws Exception {
		return super.update("updateEmployee", shopEmployeeDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#deleteEmployee(com.idcq.appserver.dto.shop.ShopEmployeeDto)
	 */
	@Override
	public Integer deleteEmployee(ShopEmployeeDto shopEmployeeDto)
			throws Exception {
		return super.delete("deleteEmployee", shopEmployeeDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#queryEmployeeByCodeAndShopId(com.idcq.appserver.dto.shop.ShopEmployeeDto)
	 */
	@Override
	public Long queryEmployeeByCodeAndShopId(ShopEmployeeDto shopEmployeeDto)
			throws Exception {
		return (Long)super.selectOne("queryEmployeeByCodeAndShopId", shopEmployeeDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#insertAccountingStat(com.idcq.appserver.dto.shop.ShopIncomeStatDto)
	 */
	@Override
	public Integer insertAccountingStat(ShopIncomeStatDto shopIncomeStat)
			throws Exception {
		return super.insert("insertAccountingStat", shopIncomeStat);
	}

	public Long getVantagesBy(Long shopId, Long userId) throws Exception {
		Map map = new HashMap();
		map.put("shopId", shopId);
		map.put("userId", userId);
		return (Long) super.selectOne("getVantagesBy", map);
	}

	@Override
	public void saveShop(ShopDto shopDto) throws Exception {
		insert("insertShop", shopDto);
	}

	@Override
	public void updateShop(ShopBean shopBean) {
		update("updateShop", shopBean);
	}
	@Override
	public void updateShopPoint(ShopBean shopBean) {
		super.update("updateShopPoint", shopBean);
	}
	@Override
	public void saveShopAccount(Map<String, Object> shopAccount)
			throws Exception {
		insert("insertShopAccout", shopAccount);
	}

	@Override
	public void updateShopPayPwd(Long shopId, String payPwd) throws Exception {
		Map map = new HashMap();
		map.put("shopId", shopId);
		map.put("payPwd", payPwd);
		update("updateShopPayPwd", map);
	}

	@Override
	public Map<String, Object> getPayPassWordById(Long shopId) throws Exception {
		return (Map<String, Object>)selectOne("getPayPassWordById", shopId);
	}

	public Integer queryUserIsShopServer(Long userId) throws Exception {
		return (Integer) super.selectOne(generateStatement("queryUserIsShopServer"), userId);
	}

    public List<Map> getPromotionShop(Long cityId, String queryDate, int pageNo, int pageSize) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cityId", cityId);
        map.put("queryDate", queryDate);
        map.put("n", (pageNo-1)*pageSize);                   
        map.put("m", pageSize);
        return this.getSqlSession().selectList(generateStatement("getPromotionShop"), map);
    }

    public int getPromotionShopCount(Long cityId, String queryDate) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("cityId", cityId);
        map.put("queryDate", queryDate);
        return this.getSqlSession().selectOne(generateStatement("getPromotionShopCount"), map);
    }

    public Map getPromotionShopDetail(Long shopId, String actCode) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("shopId", shopId);
        map.put("actCode", actCode);
        return this.getSqlSession().selectOne(generateStatement("getPromotionShopDetail"), map);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.shop.IShopDao#getOperateShopList(java.util.Map)
     */
    @Override
    public  List<Map<String, Object>> getOperateShopList(Map<String, Object> pMap) throws Exception
    {
        return (List)super.findList(generateStatement("getOperateShopList"),pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.shop.IShopDao#getOperateShopCount(java.util.Map)
     */
    @Override
    public Integer getOperateShopCount(Map<String, Object> pMap) throws Exception
    {
        return (Integer) super.selectOne(generateStatement("getOperateShopCount"), pMap);
    }

    @Override
    public Integer getOperateShopNumOfAgent(Map<String, Object> pMap) throws Exception {
    	return this.getSqlSession().selectOne(generateStatement("getOperateShopNumOfAgent"), pMap);
    }
    
    @Override
    public Integer getOperateDeviceShopNumOfAgent(Map<String, Object> pMap) throws Exception {
    	return this.getSqlSession().selectOne(generateStatement("getOerpateDeviceShopNumOfAgent"), pMap);
    }

	@Override
	public Map getTotalMember(Long shopId, String startTime, String endTime) {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return (Map) super.selectOne(generateStatement("getTotalMember"), map);
	}

    
    public Map<String, Object> getShopResource(Long shopId, Long resourceId)throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("shopId", shopId);
        map.put("resourceId", resourceId);
        return this.getSqlSession().selectOne(generateStatement("getShopResource"), map);
    }

	@Override
	public List<Map<String, Object>> getShopSalestatisticsByCashier(
			Map<String, Object> map) {
		return (List)findList("getShopSalestatisticsByCashier", map);
	}
	
	@Override
	public List<Map<String, Object>> getShopSalestatisticsByPayWay(Map<String, Object> map) {
		return (List)findList("getShopSalestatisticsByPayWay", map);
	}
	
	@Override
	public List<Map<String, Object>> getShopSalestatisticsByCashierId(
			Map<String, Object> map) {
		return (List)findList("getShopSalestatisticsByCashierId", map);
	}

	@Override
	public int verifyCardNo(Map<String, Object> map) {
		return this.getSqlSession().selectOne(generateStatement("verifyCardNo"), map);
	}

	@Override
	public Map getUserInfoByUserId(Long userId) {
		return (Map) super.selectOne(generateStatement("getUserInfoByUserId"), userId);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.shop.IShopDao#getOpenedCitis(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> getOpenedCitis(Map<String, Object> pMap) throws Exception
    {
        return (List)super.findList(generateStatement("getOpenedCitis"),pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.shop.IShopDao#getOpenedCitisCount(java.util.Map)
     */
    @Override
    public Integer getOpenedCitisCount(Map<String, Object> pMap) throws Exception
    {
        return this.getSqlSession().selectOne(generateStatement("getOpenedCitisCount"), pMap);
    }
    
    /**
     * 批量更新商铺相关设置
     * @Title: batchUpdateShopMarketing 
     * @param @param shopDtoList
     * @param @throws Exception  
     * @throws
     */
	public void batchUpdateShopMarketing(List<ShopDto> shopDtoList)
			throws Exception {
		super.update(generateStatement("batchUpdateShopMarketing"), shopDtoList);	
	}

	public List<ShopDto> getShopListByParams(Map<String, Object> params)
			throws Exception {
		return super.findList(generateStatement("getShopListByParams"),params);
	}

	public Integer getShopListByParamsCount(Map<String, Object> params)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("getShopListByParamsCount"), params);
	}

	@Override
	public int getShopByShopIdAndShopName(Map<String, Object> map) {
		return (Integer) super.selectOne(generateStatement("getShopByShopIdAndShopName"), map);
	}

	@Override
	public List<Map<String, Object>> getShopSalestatisticsByIsMember(
			Map<String, Object> map) {
		return (List)findList("getShopSalestatisticsByIsMember", map);
	}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#shopIncomeStatDetail(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> shopIncomeStatDetail(
			Map<String, Object> parms) throws Exception {
		return super.getSqlSession().selectList("shopIncomeStatDetail", parms);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#shopIncomeStatDetailCount(java.util.Map)
	 */
	@Override
	public Integer shopIncomeStatDetailCount(Map<String, Object> parms)
			throws Exception {
		return super.getSqlSession().selectOne("shopIncomeStatDetailCount", parms);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#shopDayIncomeStatDetailCount(java.util.Map)
	 */
	@Override
	public Integer shopDayIncomeStatDetailCount(Map<String, Object> pMap)
			throws Exception {
		return (Integer) super.selectOne(generateStatement("shopDayIncomeStatDetailCount"), pMap);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#shopDayIncomeStatDetail(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> shopDayIncomeStatDetail(
			Map<String, Object> map) throws Exception {
		
		return (List)findList("shopDayIncomeStatDetail", map);

	}
	
    @Override
    public ShopDto getShopByIdWithoutCache(Long shopId)
    {
        return (ShopDto)selectOne(generateStatement("getById"), shopId);
    }

	@Override
	public List<Map<String, String>> queryShopEmployeeByMap(
			Map<String, String> requestMap) {
//		return super.getSqlSession().selectOne(generateStatement("queryShopEmployeeByMap"), requestMap);
		return super.getSqlSession().selectList(generateStatement("queryShopEmployeeByMap"), requestMap);
	}

	@Override
	public int getShopCountByMap(Map<String, String> requestMap) {
		return (Integer) super.selectOne(generateStatement("getShopCountByMap"), requestMap);
	}

	@Override
	public int updateEmployeePwd(Map<String, Object> map) {
		return super.update("updateEmployeePwd", map);
	}

	@Override
	public List<Map<String, Object>> getAllShopEmployeeByMap(Map<String, Object> paramMap) {
		return super.getSqlSession().selectList(generateStatement("getAllShopEmployeeByMap"), paramMap);
	}

	@Override
	public List<Map<String, Object>> getAllShopBossesByMap(
			Map<String, Object> paramMap) {
		return super.getSqlSession().selectList(generateStatement("getAllShopBossesByMap"), paramMap);
	}
	
	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getSalerPerformanceList(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getSalerPerformanceList(
			Map<String, Object> param) throws Exception {
		
		return (List)findList("getSalerPerformanceList", param);
	}

	@Override
	public List<ShopEmployeeDto> queryEmployeeListByMap(
			Map<String, String> param) {
		return super.getSqlSession().selectList(generateStatement("queryEmployeeListByMap"), param);
	}

	@Override
	public Long updateEmployeeByDto(ShopEmployeeDto shopEmployeeDto) {
		return (Long)super.selectOne("updateEmployeeByDto", shopEmployeeDto);
		
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getShopListByHeadShopId(java.lang.Long)
	 */
	@Override
	public List<ShopDto> getShopListByHeadShopId(Long shopId) throws Exception {
		
		return (List)findList("getShopListByHeadShopId", shopId);
	}

    @Override
    public List<Map<String, Object>> getAttachmentInfoAnd(Map<String, Object> map) throws Exception
    {   
        List<Map<String, Object>> rs = new LinkedList<Map<String, Object>>();
        List<Object> tempRs = (List<Object>)super.getSqlSession().selectList(generateStatement("getAttachmentInfoByAnd"), map);
        if(null != tempRs && tempRs.size() > 0){
             for(Object obj : tempRs){
                 rs.add((Map<String, Object>)obj);
             }
        }
        return rs;
    }

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#deleteShopIncomeStat(java.util.Map)
	 */
	@Override
	public Integer deleteShopIncomeStat(String orderId)
			throws Exception {
		
		return super.delete("deleteShopIncomeStat", orderId);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.shop.IShopDao#getSalerPerformanceCount(java.util.Map)
	 */
	@Override
	public int getSalerPerformanceCount(Map<String, Object> param)
			throws Exception {
		return (int)super.selectOne("getSalerPerformanceCount", param);
	}
	
	
	
	
	
}
