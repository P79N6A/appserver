package com.idcq.appserver.service.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.order.OrderDetailDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderModelForApp;
import com.idcq.appserver.dto.order.OrderShopResourceGoodDto;
import com.idcq.appserver.dto.order.OrderShopRsrcDto;
import com.idcq.appserver.dto.order.POSOrderDto;
import com.idcq.appserver.dto.order.XorderDto;
import com.idcq.appserver.dto.order.XorderGoodsDto;
import com.idcq.appserver.dto.pay.OrderGoodsSettle;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.wxscan.WxPayResult;

public interface IOrderServcie {
	
	/**
	 * 测试生成订单号
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int saveTestOrderId(String orderId) throws Exception;
	public double countVoucherDeduction(Long userId,OrderDto orderDto) throws Exception;
	void updateShopCoupon(List<Integer> couponIds,String orderId,Long shopId,String mobile) throws Exception;
	void checkShopSeatValid(Long seatId, Long shopId) throws Exception;
	/**
	 * 获取会员的订单列表
	 * 
	 * @param shop
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getMyOrders(Long userId, Map param, Integer goodsNumberint,int page, int pageSize) throws Exception ;
	
	/**
	 * 红包所对应的订单信息
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getRedPackOrderDetail(String orderId) throws Exception;
	
	/**
	 * 获取的我的订单数量
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getMyOrderNumber(Long userId) throws Exception;
	
	/**
	 * 用户下单
	 * 
	 * @param order
	 * @param handle	1，app下单；2，pos请求一点传奇支付；3，pos预订单
	 * @return
	 * @throws Exception
	 */
	OrderDto placeOrder(OrderDto order,int handle) throws Exception;
	
	/**
	 * 获取座位订单
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getSeatOrder(Map<String, Object> requestMap) throws Exception;
	/**
	 * 预定订单
	 * @param order
	 * @param handle
	 * @return
	 * @throws Exception
	 */
	void syncBookList(OrderDto order,OrderShopResourceGoodDto orderShopResourceGoodDto,UserDto user,int handle) throws Exception;
	
	/**
	 * 修改订单
	 * @param order
	 * @param channel 1，app更新订单
	 * @return
	 * @throws Exception
	 */
	OrderDto updateOrder(OrderDto order) throws Exception;
	
	
	/**
	 * 批量修改订单userId
	 * @param updateParam
	 * @return
	 * @throws Exception
	 */
	int updateUserId(Map<String, String> updateParam) throws Exception;
	/**
	 * 获取订单详情
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	OrderDetailDto wrapOrderDetail(String orderId, Long userId) throws Exception;
	
	/**
	 * 获取指定的订单
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderDto getOrderById(String orderId) throws Exception;
	
	/**
	 * 获取指定订单主干信息
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderDto getOrderMainById(String orderId) throws Exception;
	
	OrderDto getOrderById(String orderId,int handle) throws Exception;
	
	/**
	 * 取消订单
	 * 
	 * @param order
	 * @return
	 * @throws Exception
	 */
	Integer cancelOrder(OrderDto order) throws Exception;
	
	Double settleOrder(OrderDto order) throws Exception;
	/**
	 * 批量结账
	 * 
	 * @Function: com.idcq.appserver.service.order.IOrderServcie.bitchSettleOrder
	 * @Description:
	 *
	 * @param order
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月28日 下午4:03:19
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月28日    ChenYongxin      v1.0.0         create
	 */
	String bitchSettleOrder(OrderDto order) ;
	/**
	 * 用户预定商铺资源
	 * <p>
	 * 	20150520之前的规则，groupId是资源组ID，之后是外键关联到bookRuleId，相应因为流程也做了修改
	 * </p>
	 * @param orderShopRsrc
	 * @throws Exception
	 */
	void reserveShopRsrc(OrderShopRsrcDto orderShopRsrc) throws Exception;
	
	public void reserveShopRsrcApp(OrderShopRsrcDto orderShopRsrc) throws Exception;
	
	/**
	 * 用户使用商铺资源
	 * 
	 * @param param
	 * @throws Exception
	 */
	int useShopResource(Long userId,Long resourceId,String orderId) throws Exception;
	
	/**
	 * 获取商铺分组资源
	 * @param shopId
	 * @return
	 */
	public List<Map> getListShopResGroup(Long shopId,String queryDate,int serverMode) throws Exception;
	
	/**
	 * 同步交易信息
	 * <p>
	 * 	注意：方法内部不对入参posOrder所包含的商品、商铺、用户、价格、订单号等信息的必填性及存在性做校验<br>
	 * 请在调用本方法前对入参做严谨的校验，校验信息参考“收银机同步交易记录”接口说明
	 * </p>
	 * @param posOrder
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> syncOrderList(POSOrderDto posOrder) throws Exception ;
	
	/**
	 * 验证订单的存在性
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	int queryOrderExists(String orderId) throws Exception;
	
	Integer getOrderStatusById(String orderId) throws Exception;
	
	public OrderGoodsSettle dealOrderGoodsSettle(Long userId, Long shopId,Long goodsId, String orderId,Map orderGoodsSettleSetting) throws Exception;
	/**
	 * 格局资源组名称、商铺id查询资源信息
	 * @param groupName
	 * @param shopId
	 * @return
	 */
	Long getGroupIdByName(String groupName,Long shopId) throws Exception;
	/**
	 * 根据shopid查询时间段信息
	 * @param groupName
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> getIntevalByShopId(Long shopId) throws Exception;	
	
	/**
	 * 删除订单
	 * @param userId
	 * @param orderId
	 * @throws Exception
	 */
	void deleteOrder(String userId, String orderId) throws Exception;
	

	/**
	 * 直接物理删除订单
	 * @param orderId
	 */
	void delOrder(String orderId);
	
	/**
	 * 预定非会员订单
	 * @param xorder
	 * @param order
	 * @throws Exception
	 */
	public void syncXorder(OrderShopResourceGoodDto xorder,OrderDto order) throws Exception;
	
	public void dealOrder(OrderDto order, Map orderGoodsSettleSetting) throws Exception;
	
	public void dealSettle(OrderDto order,Long userId,Map orderGoodsSettleSetting) throws Exception;
	
	public void splitOrderByPrice(OrderDto order, Map orderGoodsSettleSetting) throws Exception;
	
	public void dealSettleByPrice(OrderDto order, Map orderGoodsSettleSetting) throws Exception;
	
	void dealOrderSettle(OrderDto order, Map orderGoodsSettleSetting) throws Exception;

	/**
	 * 更新抹零价
	 * @param order
	 * @return
	 * @throws Exception
	 */
	public void updateOrderMaling(OrderDto order) throws Exception;
	
	/**
	 * 获取自助下单的订单列表接口
	 * @param shopId
	 * @return
	 */
	List<Map<String, Object>> getOrder8List(Long shopId) throws Exception;;
	
	/**
	 * 获取订单商品列表
	 * 
	 * @return
	 * @throws Exception
	 */
	List<OrderGoodsDto> getOGoodsListByOrderId(String orderId) throws Exception;
	
		void autoFinishOrder(List<String>orderIds)throws Exception;
	
	/**
	 * 查询已开单的且时间已过了预定时间的订单
	 * @return
	 * @throws Exception
	 */
	List<String> getOrderIdsByCondition() throws Exception;
	
	/**
	 * 更新订单状态
	 * @param orderId
	 * @param status
	 * @throws Exception
	 */
	void updateOrderStatus(String orderId,int status) throws Exception;

	/**
	 * 写订单操作日志(通用接口)
	 * @param orderId
	 * @param remark
	 * @param userId 最后操作用户ID，=0表示系统操作
	 */
	void saveOrderLog(String orderId, String remark, Long userId) throws Exception;
	 /**
	  * 统计非会员订单支付信息
	  * 
	  * @Function: com.idcq.appserver.service.order.IOrderServcie.handleAccountingStatByuser
	  * @Description:
	  *
	  * @param order
	  * @throws Exception
	  *
	  * @version:v1.0
	  * @author:ChenYongxin
	  * @date:2015年9月24日 下午4:08:35
	  *
	  * Modification History:
	  * Date         Author      Version     Description
	  * -----------------------------------------------------------------
	  * 2015年9月24日    ChenYongxin      v1.0.0         create
	  */
	 void handleAccountingStatByUser(OrderDto order) throws Exception;
	/**
	 * 订单积分计算
	 * 
	 * @Function: com.idcq.appserver.service.order.OrderServiceImpl.orderVantages
	 * @Description:
	 *
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @throws Exception 
	 * @date:2015年9月28日 上午11:07:36
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月28日    ChenYongxin      v1.0.0         create
	 */
	 void orderVantages(OrderDto order) throws Exception;
	 
	 /**
	  * 支付宝扫码支付预订单
	  * @Title: aliscanPreOrder 
	  * @param @param order
	  * @param @throws Exception
	  * @return void    返回类型 
	  * @throws
	  */
	 void aliscanPreOrder(OrderDto order)throws Exception;
	 
	 void updateOrderOnly(OrderDto orderDto)throws Exception;
	 
	 /**
	  * 获得一点管家订单详情
	  * @Title: getIdgjOrderDetail 
	  * @param @param orderId
	  * @param @return
	  * @param @throws Exception
	  * @return OrderDto    返回类型 
	  * @throws
	  */
	 OrderDto getIdgjOrderDetail(String orderId)throws Exception;
	 
	/**
	 * 根据订单编号查询订单信息，只返回订单信息，不返回订单商品信息
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	OrderDto queryOrderDtoById(String orderId) throws Exception;
	
	/**
	 * 获取订单最大支付序号
	 * @param orderId 订单编号
	 * @param flag 0-查询非会员订单支付记录 1-查询会员订单支付记录
	 * @return
	 * @throws Exception
	 */
	Integer getMaxPayIndex(String orderId,int flag) throws Exception;
	
	/**
	 * 获取非会员订单信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	XorderGoodsDto getXorderGoodsByGoodsId(XorderGoodsDto dto) throws Exception;
	
	 /**
	  * 新增/修改会员订单及订单商品信息
	  * @param orderDto 会员订单信息
	  * @param orderFlag 新增/修改标识
	  * @param xorderFlag 是否存在非会员订单标识
	  * @return
	  * @throws Exception
	  */
	int insertOrUpdateOrderDto(OrderDto orderDto, boolean orderFlag, boolean xorderFlag) throws Exception;
	
	/**
	 * 新增/修改非会员订单及订单商品信息
	 * @param xorderDto 非会员订单信息
	 * @param orderFlag 新增/修改标识
	 * @param xorderGoodsDtos 非会员订单商品信息
	 * @return
	 * @throws Exception
	 */
	int insertOrUpdateXOrderDto(OrderDto orderDto, boolean xorderFlag,List<OrderGoodsDto> orderGoodsDtos) throws Exception;
	
	/**
	 * 查询订单商品信息
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	OrderGoodsDto getOrderGoodsByGoodsId(OrderGoodsDto dto) throws Exception;
	
	/**
	 * 根据订单编号，清除非会员订单记录（订单、订单商品）
	 * @param xorderId 非会员订单编号
	 * @return
	 * @throws Exception
	 */
	int clearXorderDtoRecord(String xorderId) throws Exception;



	/**
	 * 获取订单列表接口
	 * @param shopId 商铺编号
	 * @param dateType 查询日期类型
	 * @param startDate 开始日期
	 * @param endDate 截止日期
	 * @param orderStatuss 订单状态集合
	 * @param orderTransactionType 交易状态类型
	 * @param payType 支付类型
	 * @param billerId 导购员
	 * @param cashierId 收银员
	 * @param pNo
	 * @param pSize
	 * @param ringStr 
	 * @param yearOnYearStr 
	 * @param checkObject 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAllOrderList(List<Long> shopIds,Integer dateType, String startDate,
			String endDate, List<Integer> orderStatuss,
			Integer orderTransactionType, Integer payType,Integer billerId,Integer cashierId,int pNo, int pSize, Long userId, String yearOnYearStr, String ringStr) throws Exception;
	
	/**
	 * 支付完成的处理
	 * @Title: payFinishDeal 
	 * @param @param transactionDto
	 * @param @param payDto
	 * @param @param orderNo
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	void payFinishDeal(Transaction3rdDto transactionDto,PayDto payDto,String orderNo)throws Exception;
	
	/**
	 * 处理非会员订单的结算
	 * @Title: dealXorderSettle 
	 * @param @param order
	 * @param @param orderGoodsSettleSetting
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
    public void dealXorderSettle(OrderDto order, Map orderGoodsSettleSetting)throws Exception;

    
    /**
     * 处理通电
     * @Title: dealNotify 
     * @param @param wpr
     * @param @throws Exception
     * @return void    返回类型 
     * @throws
     */
	public void dealWxscanNotify(WxPayResult wpr)throws Exception;


	/**
	 * CO13：获取日订单统计接
	 * @param userId 会员编号
	 * @param shopId 商铺编号
	 * @param dateType 查询日期类型
	 * @param startDate 开始日期
	 * @param endDate 截止日期
	 * @param orderStatuss 订单状态集合
	 * @param orderTransactionType 交易状态类型
	 * @param payType 支付类型
	 * @param billerId 导购员
	 * @param cashierId 收银员
	 * @param pNo
	 * @param pSize
	 * @param ringStr 
	 * @param yearOnYearStr 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getDayOrderStat(Long userId, Long shopId, Integer dateType,
			String startDate, String endDate, List<Integer> orderStatuss,
			Integer orderTransactionType, Integer payType, Integer billerId,
			Integer cashierId, int pNo, int pSize, String yearOnYearStr, String ringStr)throws Exception;
	
	/**
	 * 获取收银订单详情列表
	 * @param params
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getOrderListDetail(Map<String,Object> params,int page, int pageSize) throws Exception;
	
	void updatePlatformServePrice(OrderDto order)throws Exception;
	
	OrderDto getOrderDtoById(String orderId) throws Exception;
	public BigDecimal queryOrderAmount(String orderId)throws Exception;
	void generatePlatformBill(OrderDto orderDto,Integer moneySource,Double billMoney) throws Exception;
	void generateShopMiddleBill(OrderDto orderDto,Double billMoney)throws Exception;
	public void pushOrder(String orderId, Long userId, Double payAmount);
	
	int updateOrderRealSettlePrice(String orderId, Double orderRealSettlePrice, Double sendRedPacketMoney) throws Exception;
	
	void updateGoodsAndShopSoldNum(String orderId)throws Exception;
	
	void dealSettleOrderByShopId(Long shopId)throws Exception;
	
	PageModel queryOrderByOrderCode(Long shopId, String orderCode,Integer payStatus, Integer payType, int pNo, int pSize);

	void reservResource(String seatIds, String orderId) throws Exception;
	
	void updateShopResourceStatus(String orderId);

	/**
	 * 兑换礼品时的结账
	 * @param payDto
	 * @throws Exception
     */
	void settleOrder(PayDto payDto, Integer moneyType) throws Exception;
}

