package com.idcq.appserver.dao.collect;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.shop.ShopAppVersionDto;
import com.idcq.appserver.dto.shop.ShopCookingDetails;
import com.idcq.appserver.dto.shop.ShopIncomeStatDto;

public interface ICollectDao {
	/**
	 * 初始化商品(菜单)信息
	 * @param shopId
	 * @return
	 */
	public List<Map> queryShopGoodsInfo(Long shopId)throws Exception;
	
	/**
	 * 根据shopId获取商品分类信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<Map> queryShopGoodsCategoryInfo(Long shopId)throws Exception;
	/**
	 * 初始化商铺员工信息
	 * @param shopId
	 * @return
	 */
	public List<Map> queryShopEmployeeInfo(Long shopId)throws Exception;
	
	/**
	 * 初始化座位(商铺资源)信息
	 * @param shopId
	 * @return
	 */
	public List<Map> queryShopResourceInfo(Long shopId)throws Exception;
	
	/**
	 * 初始化版本信息
	 * @param param
	 * @return
	 */
	public List<Map> queryAppVersionInfo(Map param)throws Exception;
	
	/**
	 * 查询lastUpdate
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public String queryLastUpdate(Map param) throws Exception;
	
	/**
	 * 查询商家服务费信息(附加费)
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryExtraFeeInfo(Long shopId) throws Exception;
	
	/**
	 * 查询商铺状态信息
	 * @param shopId
	 * @return
	 */
	public Map queryShopInfoStatus(Long shopId) throws Exception;;
	
	/**
	 * 查询订单详情
	 * @param orderId
	 * @return
	 */
	public Map getOrderDetail4CR(String orderId) throws Exception;;
	
	/**
	 * 查询订单商品详情
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public List<Map> getOrderGoodsDetail(String orderId) throws Exception;
	
	/**
	 * 查询订单支付详情数量
	 * @param param
	 * @return
	 */
	public int getOrderPayDetailCount(Map param);
	
	/**
	 * 查询订单支付详情
	 * @param param
	 * @return
	 */
	public List<Map> getOrderPayDetail(Map param);
	
	/**
	 * 初始化接口=商铺座位位置分类信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public List<Map> qaueryShopResourcePositionInfo(Long shopId)throws Exception;

	/**
	 * 初始化接口=商铺服务备注信息
	 * <b>
	 * 	先查询商铺服务备注信息，如果未查询到，则查询平台提供的通用备注信息
	 * </b>
	 * @param shopId
	 * @return
	 */
	public List<Map> qaueryShopCookingDetailInfo(Long shopId);
	
	/**
	 * 获取商铺外卖费用配置
	 * @param shopId
	 * @param settingType
	 * @return
	 */
	Map getDistriTakeoutSetInit(Long shopId, Integer settingType);
	
	/**
	 * 释放商铺资源
	 * @return
	 */
	int updateShopResourceStatus(String orderId, Integer resourceStatus);
	
	int updateShopResourceStatusByOrderIdList(List<String> orderId, Integer resourceStatus);
	
	String getShopAppVersion(String appName, Long shopId);
	
	int updateAppVersion(ShopAppVersionDto shopAppVersionDto) throws Exception;
	
	int addAppVersion(ShopAppVersionDto shopAppVersionDto) throws Exception;
	
	int addAppVersionLog(ShopAppVersionDto shopAppVersionDto) throws Exception;
	
	/**
	 * 根据条形码获取标品信息
	 * 
	 * @Function: com.idcq.appserver.dao.collect.ICollectDao.getStandardGoodsByBarCode
	 * @Description:
	 *
	 * @param barcode
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 下午2:29:11
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	Map getStandardGoodsByBarCode(String barcode);
	
	/**
	 * 获取商铺账务统计
	 * @param xorderId
	 * @return
	 * @throws Exception
	 */
	public ShopIncomeStatDto getShopIncomeStatDtoByOrderId(String xorderId) throws Exception;
	
	/**
	 * 更新信息
	 * @param shopInomeStatDto
	 * @return
	 * @throws Exception
	 */
	int updateShopIncomeStatDtoByOrderId(ShopIncomeStatDto shopInomeStatDto) throws Exception;

	public Integer getShopGoodsCount(Map<String, Object> map) ;

	/**
	 * CS22：获取店铺商品信息接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map> getShopGoods(Map<String, Object> map) throws Exception;

	public Map<String, String> queryEmployee(String orderId);

	public List<Map> queryGoodsByBarcode(Long shopId, String barcode,Integer goodsStatus, Integer searchFlag);
	public int batchInsertCookingDetail(List<ShopCookingDetails> cookDetailList) throws Exception;

	Map<String, BigDecimal> getShopIncomeStat(Map<String, Object> params);

	List<Map<String, Object>> getSalerPerformanceDtailByOrder(Map<String, Object> params);

	int countSalerPerformanceDtailByOrder(Map<String, Object> params);

	List<Map<String, Object>> getSalerPerformanceDtailByOrderGoods(Map<String, Object> params);

	int countSalerPerformanceDtailByOrderGoods(Map<String, Object> params);
	
	/**
	 * 获取店铺商品条数
	 * 
	 * @Function: com.idcq.appserver.dao.collect.ICollectDao.getShopSimpleCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年10月25日 上午10:08:11
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年10月25日    ChenYongxin      v1.0.0         create
	 */
	public Integer getShopSimpleCount(Map<String,Object>map)throws Exception;
}
