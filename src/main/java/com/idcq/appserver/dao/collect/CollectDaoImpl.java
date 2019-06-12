package com.idcq.appserver.dao.collect;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dto.shop.ShopAppVersionDto;
import com.idcq.appserver.dto.shop.ShopCookingDetails;
import com.idcq.appserver.dto.shop.ShopIncomeStatDto;
@Repository
public class CollectDaoImpl extends SqlSessionDaoSupport implements ICollectDao {
	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	public List<Map> queryShopGoodsInfo(Long shopId) throws Exception{
		return this.getSqlSession().selectList("queryShopGoodsInfo", shopId);
	}

	public List<Map> queryShopEmployeeInfo(Long shopId) throws Exception{
		return this.getSqlSession().selectList("queryShopEmployeeInfo", shopId);
	}

	public List<Map> queryShopResourceInfo(Long shopId)throws Exception {
		return this.getSqlSession().selectList("queryShopResourceInfo", shopId);
	}

	public List<Map> queryAppVersionInfo(Map param)throws Exception {
		return this.getSqlSession().selectList("queryAppVersionInfo", param);
	}
	
	public List<Map> queryExtraFeeInfo(Long shopId) throws Exception {
		return this.getSqlSession().selectList("queryExtraFeeInfo", shopId);
	}

	public String queryLastUpdate(Map param) throws Exception {
		return this.getSqlSession().selectOne("queryLastUpdate", param);
	}
	
	public Map queryShopInfoStatus(Long shopId) throws Exception{
		return (Map)this.getSqlSession().selectOne("queryShopInfoStatus", shopId);
	}
	
	public Map getOrderDetail4CR(String orderId)  throws Exception{
		return (Map)this.getSqlSession().selectOne("getOrderDetail4CR", orderId);
	}

	public List<Map> getOrderGoodsDetail(String orderId) throws Exception {
		return this.getSqlSession().selectList("getOrderGoodsDetail", orderId);
	}
	public int getOrderPayDetailCount(Map param) {
		return (Integer)this.getSqlSession().selectOne("getOrderPayDetailCount", param);
	}
	public List<Map> getOrderPayDetail(Map param) {
		return this.getSqlSession().selectList("getOrderPayDetail", param);
	}
	public List<Map> queryShopGoodsCategoryInfo(Long shopId) throws Exception {
		return this.getSqlSession().selectList("queryShopGoodsCategoryInfo", shopId);
	}
	public List<Map> qaueryShopResourcePositionInfo(Long shopId)
			throws Exception {
		return this.getSqlSession().selectList("qaueryShopResourcePositionInfo", shopId);
	}
	public List<Map> qaueryShopCookingDetailInfo(Long shopId) {
		return this.getSqlSession().selectList("qaueryShopCookingDetailInfo", shopId);
	}
	public Map getDistriTakeoutSetInit(Long shopId, Integer settingType) {
		Map param = new HashMap();
		param.put("shopId", shopId);
		param.put("settingType", settingType);
		return (Map)this.getSqlSession().selectOne("getDistriTakeoutSetInit", param);
	}
	public int updateShopResourceStatus(String orderId, Integer resourceStatus) {
		Map param = new HashMap();
		param.put("orderId", orderId);
		param.put("resourceStatus", resourceStatus);
		return this.getSqlSession().update("updateShopResourceStatus", param);
	}
	@Override
	public int updateShopResourceStatusByOrderIdList(List<String> orderIdList,
			Integer resourceStatus) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("orderIdList", orderIdList);
		param.put("resourceStatus", resourceStatus);
		return this.getSqlSession().update("updateShopResourceStatusByOrderIdList", param);
	}
	
	public String getShopAppVersion(String appName, Long shopId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("appName", appName);
		param.put("shopId", shopId);
		return this.getSqlSession().selectOne("getShopAppVersion", param);
	}
	
	public int updateAppVersion(ShopAppVersionDto shopAppVersionDto)
			throws Exception {
		return this.getSqlSession().update("updateAppVersion", shopAppVersionDto);
	}
	public int addAppVersion(ShopAppVersionDto shopAppVersionDto)
			throws Exception {
		return this.getSqlSession().insert("addAppVersion", shopAppVersionDto);
	}
	public int addAppVersionLog(ShopAppVersionDto shopAppVersionDto)
			throws Exception {
		return this.getSqlSession().insert("addAppVersionLog", shopAppVersionDto);
	}
	
	public Map getStandardGoodsByBarCode(String barcode) {
		return this.getSqlSession().selectOne("getStandardGoodsByBarCode", barcode);
	}
	public ShopIncomeStatDto getShopIncomeStatDtoByOrderId(String xorderId)
			throws Exception {
		return (ShopIncomeStatDto)this.getSqlSession().selectOne("getShopIncomeStatDtoByOrderId",xorderId);
	}
	public int updateShopIncomeStatDtoByOrderId(
			ShopIncomeStatDto shopInomeStatDto) throws Exception {
		return this.getSqlSession().update("updateShopIncomeStatDtoByOrderId", shopInomeStatDto);
	}
	@Override
	public Integer getShopGoodsCount(Map<String, Object> map)  {
		return (Integer)getSqlSession().selectOne("getShopGoodsByShopIdCount", map);
	}
	
	
	/**
	 * 仅仅查询店铺商品条数
	 * 
	 * @Function: com.idcq.appserver.dao.collect.CollectDaoImpl.getShopSimpleCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年10月25日 上午10:01:11
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年10月25日    ChenYongxin      v1.0.0         create
	 */
	public Integer getShopSimpleCount(Map<String,Object>map)throws Exception{
		return (Integer)getSqlSession().selectOne("getShopSimpleCount", map);
	}
	
	@Override
	public List<Map> getShopGoods(Map<String, Object> map)
			throws Exception {
		return (List)getSqlSession().selectList("getShopGoodsByShopId", map);
	}
	@Override
	public Map<String, String> queryEmployee(String orderId) {
		return this.getSqlSession().selectOne("queryEmployeeByCashierId", orderId);
	}
	@Override
	public List<Map> queryGoodsByBarcode(Long shopId,String barcode,Integer goodsStatus,Integer searchFlag) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("barcode", barcode);
		map.put("searchFlag", searchFlag);
		if(goodsStatus!=4){//商品状态参数传4的时候约定不与状态进行过滤
			map.put("goodsStatus", goodsStatus);
		}
		
		return (List)getSqlSession().selectList("queryGoodsByBarcode", map);
		
	}

	@Override
	public int batchInsertCookingDetail(List<ShopCookingDetails> cookDetailList) throws Exception {
		return this.getSqlSession().insert("batchInsertCookingDetail",cookDetailList);
	}

	@Override
	public Map<String, BigDecimal> getShopIncomeStat(Map<String, Object> params) {
		return this.getSqlSession().selectOne("getShopIncomeStat", params);
	}

	@Override public List<Map<String, Object>> getSalerPerformanceDtailByOrder(Map<String, Object> params)
	{
		int pNo = (Integer)params.get("pNo");
		int pSize = (Integer)params.get("pSize");
		RowBounds rowBounds = new RowBounds((pNo - 1) * pSize, pSize);
		return (List)this.getSqlSession().selectList("getSalerPerformanceDtailByOrder", params, rowBounds);
	}

	@Override public int countSalerPerformanceDtailByOrder(Map<String, Object> params)
	{
		return this.getSqlSession().selectOne("countSalerPerformanceDtailByOrder", params);
	}

	@Override public List<Map<String, Object>> getSalerPerformanceDtailByOrderGoods(Map<String, Object> params)
	{
		int pNo = (Integer)params.get("pNo");
		int pSize = (Integer)params.get("pSize");
		RowBounds rowBounds = new RowBounds((pNo - 1) * pSize, pSize);
		return (List)this.getSqlSession().selectList("getSalerPerformanceDtailByOrderGoods", params, rowBounds);
	}

	@Override public int countSalerPerformanceDtailByOrderGoods(Map<String, Object> params)
	{
		return this.getSqlSession().selectOne("countSalerPerformanceDtailByOrderGoods", params);
	}
}
