package com.idcq.appserver.service.collect;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.idcq.appserver.controller.goods.StandardGoodsDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.essential.Unit;
import com.idcq.appserver.dto.order.MultiPayDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderGoodsServiceTech;
import com.idcq.appserver.dto.shop.ShopCookingDetails;
import com.idcq.appserver.dto.shop.ShopDeviceDto;

public interface ICollectService {

	/**
	 * 初始化数据接口
	 * @param shopId
	 * @param token
	 * @return
	 */
	Map getShopData(Long shopId, String needGoods, String token)throws Exception;

	/**
	 * 校验商铺及商铺设备token
	 * @param shopId
	 * @param token
	 * @return
	 */
	boolean queryShopAndTokenExists(Long shopId, String token) throws Exception ;
	
	/**
	 * 收银机登录接口
	 * @param request
	 * @return
	 * @throws Exception
	 */
	Map vertifyDevice(HttpServletRequest request)throws Exception;	
	
	/**
	 * 商铺推荐会员注册接口
	 * @param request
	 * @throws Exception
	 */
	 Map<String, String>  registerMember(HttpServletRequest request)throws Exception;	
	
	/**
	 * 查询会员接口
	 * @param mobile 手机号码
	 * @param shopId 店铺id
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> searchMember(String mobile, Long shopId, Double money, Long businessAreaActivityId)throws Exception;

	/**
	 * 获取APK最新版本信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map getLastestVesion(String appIds) throws Exception;

	/**
	 * 获取API地址接口
	 * @param snId
	 * @return
	 */
	String[] getCRAddress(String snId, String appName) throws Exception;	
	
	/**
	 * 商家确认取消订单
	 * @param shopId
	 * @param orderId
	 * @param auditFlag
	 * @param refundType
	 * @throws Exception
	 */
	void shopCancelOrder(Long shopId, String orderId, String auditFlag, String refundType, String refuseReason) throws Exception;
	
	/**
	 * 退款和释放资源操作
	 * @param order 订单详细信息
	 * @throws Exception
	 */
	void dealRefund(OrderDto order) throws Exception;

	/**
	 * 获取订单详情
	 * @param shopId
	 * @param orderId
	 * @return
	 */
	Map getOrderDetail4CR(Long shopId, String orderId) throws Exception;

	/**
	 * 获取订单支付详情
	 * @param shopId
	 * @param orderId
	 * @param token
	 * @return
	 */
	List<Map> getOrderPayDetail(Long shopId, String orderId, String token) throws Exception;
	
	/**
	 * 上报接口
	 * @param sn
	 * @param regId
	 * @param token
	 * @param shopIdStr
	 * @throws Exception
	 */
	void reportRegId(String sn, String regId, String token, String shopIdStr)throws Exception;

	/**
	 * 修订店铺内路由器的单个mac白名单接口
	 * @param shopDeviceDto 设备信息
	 * @param cmd 操作类型
	 * @throws Exception
	 */
	void modifyAWifiMacWhiteList(ShopDeviceDto shopDeviceDto,int cmd) throws Exception;
	
	/**
	 * 获取店铺内路由器的MAC白名单接口
	 * @param shopId
	 * @param token
	 * @return
	 * @throws Exception
	 */
	List<Map> getWifiMacWhitelist(Long shopId, String token) throws Exception;
	
	/**
	 * 获取店铺管理者所拥有的店铺列表
	 * @param mobile
	 * @param userPassword
	 * @param shopMode
	 * @return 
	 * @throws Exception
	 */
	List<Map> getOwnShopList(String mobile, String userPassword, String versionType, String authentication) throws Exception;

	/**
	 * 更新订单状态同时记录订单状态日志
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.dealOrderStatus
	 * @Description:
	 *
	 * @param orderInfo 需要更新的订单对象
	 * @param userId  操作的用户id
	 * @param remark 订单日志备注
	 * @return 更新数
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月1日 下午2:56:49
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月1日    shengzhipeng       v1.0.0         create
	 */
	int dealOrderStatus(OrderDto orderInfo, Long userId, String remark) throws Exception;
	
	/**
	 * 定时处理多少分钟未确认订单
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.timeProcessingOrder
	 * @Description:
	 *
	 * @param userId 操作员的userId
	 * @param remark 日志描述
	 * @return 返回处理过的订单对象
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月5日 下午1:50:34
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月5日    shengzhipeng       v1.0.0         create
	 */
	List<OrderDto> timeProcessingOrder(Long userId, String remark)  throws Exception;
	/**
	 * 收银机同步菜品时价
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.updateGoodsPrice
	 * @Description:
	 *
	 * @param goodsId 
	 * @param standardPrice 
	 * @return 
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:LuJianPing
	 * @date:2015年8月5日 下午1:50:34
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月20日    LuJianPing       v1.0.0         create
	 */
	int updateGoodsPrice(Long shopId, Long goodsId, Double standardPrice)  throws Exception;
	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.checkPassword
	 * @Description:
	 *
	 * @param shopId
	 * @param token
	 * @param password
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月20日 上午9:04:20
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月20日    shengzhipeng       v1.0.0         create
	 */
	boolean checkPassword(Long shopId, String token, String password) throws Exception;
	
	/**
	 * 恢复或删除订单操作
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.delAndResumeOrder
	 * @Description:
	 *
	 * @param shopId 店铺id
	 * @param orderId 订单id
	 * @param operationType "0"：代表删除，"1"：代表恢复
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月11日 下午3:06:12
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月11日    shengzhipeng       v1.0.0         create
	 */
	void delAndResumeOrder(Long shopId, String orderId, String operationType) throws Exception;
	
	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.reportAppVersion
	 * @Description:
	 *
	 * @param shopId 店铺id
	 * @param appName 应用名称
	 * @param appDesc 应用描述
	 * @param appVersion 应用版本
	 * @param sn 序列号
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月16日 下午7:46:44
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月16日    shengzhipeng       v1.0.0         create
	 */
	void reportAppVersion(Long shopId, String appName, String appDesc, String appVersion, String sn) throws Exception;
	
	/**
	 * 根据条形码获取标品信息
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.queryStandardGoods
	 * @Description:
	 *
	 * @param barcode
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 下午2:26:10
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	Map queryStandardGoods(String barcode);
	
	/**
	 * 新增或修改商品信息
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.syncGoodsInfo
	 * @Description:
	 *
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月21日 下午2:55:33
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    shengzhipeng       v1.0.0         create
	 */
	Map syncGoodsInfo(StandardGoodsDto standardGoodsDto) throws Exception;
	
	/**
	 * 添加单位
	 * 根据单位名称和支持的小数点位数活动单位信息，如果单位不存在就将该单位
	 * 保存到单位表并且返回单位ID
	 * @param unitName 单位名称
	 * @param shopId 店铺名称
	 * @param digitScale 支持小数点位数
	 * @return 单位ID
	 * @see [类、类#方法、类#成员]
	 * @author  shengzhipeng
	 * @date  2016年3月26日
	 */
	Long saveUnit(String unitName, Long shopId, Integer digitScale);
	
	
	/**
	 * 非会员订单组合支付
	 * @param multiPayDto
	 * @param xorderDto
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> multiplePayFromXorder(MultiPayDto multiPayDto, OrderDto orderDto,List<OrderGoodsServiceTech> serviceTechs) throws Exception;
	
	/**
	 * 会员订单组合支付
	 * @param multiPayDto
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> multiplePayFromOrder(MultiPayDto multiPayDto,OrderDto orderDto,List<OrderGoodsServiceTech> serviceTechs) throws Exception;
	
	/**
	 * 查询订单支付记录
	 * @param orderId 订单编号
	 * @param flag 0-查询会员订单 1-查询非会员订单
	 * @return
	 */
	List<Map<String,Object>> getPayRecordByOrderId(String orderId,int flag) throws Exception;

	/**
	 * 商品状态变更
	 * @param shopId
	 * @param token
	 * @param goodsIds
	 * @param operateType
	 * @return
	 * @throws Exception
	 */
	int updateGoodsInfo(Long shopId, List<Long> goodsIds,
			Integer operateType) throws Exception;

	/**
	 * CS22：获取店铺商品信息接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	PageModel getShopGoods(Map<String, Object> map) throws Exception;

	List<Map> getXorderPayDetail(Long shopId, String orderId, String token)throws Exception;
	
	/**
	 * 获取订单状态
	 * @Title: getOrderStatus 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception
	 * @return int    返回类型 
	 * @throws
	 */
	int getOrderStatus(String orderId)throws Exception;
	
	/**
	 * 获取非会员订单状态
	 * @Title: getXorderStatus 
	 * @param @param orderId
	 * @param @return
	 * @param @throws Exception
	 * @return int    返回类型 
	 * @throws
	 */
	int getXorderStatus(String orderId)throws Exception;
	
	/**
	 * 获取订单已经支付金额
	 * @param orderId
	 * @param orderPayType
	 * @return
	 * @throws Exception
	 */
	BigDecimal queryOrderPayAmount(String orderId,int orderPayType) throws Exception;
	
	/**
	 * 获取非会员订单已经支付金额
	 * @param xorderId
	 * @return
	 * @throws Exception
	 */
	BigDecimal queryXorderPayAmount(String xorderId) throws Exception;

	Map<String,String> queryEmployee(String orderId);
	/**
	 * 检查店铺是否存在
	 * @param shopId
	 * @return
	 */
	boolean checkShopExists(Long shopId) throws Exception;

	List<Map> queryGoodsByBarcode(Long shopId, String barcode,Integer goodsStatus, Integer searchFlag) throws Exception;
	void batchInsertCookingDetail(List<ShopCookingDetails> cookDetailList) throws Exception;

	Long getGoodsUnitByUnitName(String unitName, Long shopId);
	
	List<Unit> queryUnitsForShop(Integer shopId);

	/**
	 * 获得商铺营收统计信息
	 * @return
	 */
	Map<String, Object> getShopIncomeStat(Map<String, Object> params) throws Exception;


	Map<String, Object> getSalerPerformanceByOrder(Map<String, Object> searchParams);

	Map<String, Object> getSalerPerformanceByOrderGoods(Map<String, Object> searchParams);

	/**
	 * 
	 * @Function: com.idcq.appserver.service.collect.ICollectService.getShopGoodsCount
	 * @Description:
	 *
	 * @param parseLong
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年10月25日 上午9:46:26
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年10月25日    ChenYongxin      v1.0.0         create
	 */
	int getShopGoodsCount(long parseLong)throws Exception ;
}
