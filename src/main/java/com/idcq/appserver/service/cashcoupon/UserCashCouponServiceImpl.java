package com.idcq.appserver.service.cashcoupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IUserXBillDao;
import com.idcq.appserver.dao.cashcoupon.ICashCouponDao;
import com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.OrderGoodsSettleDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.UserXBillDto;
import com.idcq.appserver.dto.cashcoupon.CashCouponDto;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.common.NumberDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;

@Service
public class UserCashCouponServiceImpl implements IUserCashCouponService {
	private Log logger = LogFactory.getLog(UserCashCouponServiceImpl.class);
	@Autowired
	private IUserCashCouponDao userCouponDao;
	@Autowired
	public OrderGoodsSettleDao orderGoodsSettleDao;
	@Autowired
	public IOrderGoodsDao orderGoodsDao;
	@Autowired
	public IPayDao payDao;
	@Autowired
	public IOrderDao orderDao;
	@Autowired
	public ICashCouponDao cashCouponDao;
	@Autowired
	public IPacketDao packetDao;
	@Autowired
	public IOrderLogDao orderLogDao;
	@Autowired
	public IUserDao userDao;
	@Autowired
	private IUserXBillDao userXBillDao;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
    public IPlatformBillDao platformBillDao;
	@Autowired
	public IUserCashCouponDao userCashCouponDao;
	@Autowired
	private IStorageServcie storageServcie;
	
	public int obtainCoupon(UserCashCouponDto dto) throws Exception{
		return userCouponDao.obtainCoupon(dto);
	}

	public int obtainCoupons(List<UserCashCouponDto> lst) throws Exception{
		return userCouponDao.obtainCoupons(lst);
	}

	public int consumeCoupon(Long userId, String orderId, Long uccId, double usePrice) throws Exception {
		int flag = userCouponDao.consumeCoupon(userId,orderId,uccId,usePrice);
		//TODO 结算
		if(1==flag){
			OrderGoodsSettleUtil.detailSingleOrder(orderId);
		}
		return flag;
	}
	
	/**
	 * 消费代金券（支持多张同时支付）
	 * <p>
	 *	重构：支持多张代金券同时消费<br>
	 *	重构人：lujianping<br>
	 *	重构日期：20150506
	 * </p>
	 * @param userId
	 * @param uccIds
	 * @param orderId
	 * @param orderPayType
	 * @return
	 * @throws Exception
	 */
	public int consumeCoupon(Long userId, String uccIds, String orderId,Integer orderPayType) throws Exception {
		String[] uccIdArray = uccIds.split(";");
		int i = 0;
		NumberDto num = new NumberDto();
		int flag = 0;
		for(String e : uccIdArray){
			CommonValidUtil.validStrLongFormat(e, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_CASHCOUNPONID);
			flag = consumeCoupon2(userId, Long.valueOf(e), orderId, orderPayType, num);
			if(flag == 1){
				return 2;//代金券数量过剩
			}
		}
		return flag;//代金券为空
	}

	private int consumeCoupon2(Long userId, Long uccId, String orderId,Integer orderPayType,NumberDto number)
			throws Exception {
		//代金券存在性
		UserCashCouponDto userCouponDto = userCouponDao.getUserCouponDto(userId, uccId);
		CommonValidUtil.validObjectNull(userCouponDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_CASHCOUPON);
		//是否使用过
		CommonValidUtil.validIntNoEqual(userCouponDto.getCouponStatus(), 1, CodeConst.CODE_BE_USED_CC, CodeConst.MSG_BE_USED_CC);
		CashCouponDto couponDto = cashCouponDao.getCouponDto(userCouponDto.getCashCouponId());
		CommonValidUtil.validObjectNull(couponDto, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_CASHCOUPON);
		List<Map> orders = null;
		OrderDto validOrder = null;
		BigDecimal amount = null;			//订单总金额
		Double amountTotal = null;			//订单总金额
		BigDecimal  payAmount = null;		//已经支付的金额
		if(CommonConst.PAY_TYPE_GROUP == orderPayType){//订单组支付
			//订单组订单列表
			orders = this.payDao.queryOrderGroupById(orderId);
			if (orders==null || orders.size() == 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			}
			for(Map map : orders){
				validOrder=this.orderDao.getOrderById((String)map.get("orderId"));
				if(CommonConst.PAY_STATUS_PAYED == validOrder.getPayStatus()){
					throw new ValidateException(CodeConst.CODE_PAY_STATUS_SUCCESS,CodeConst.MSG_PAY_STATUS_SUCCESS);
				}
				//适用商铺
				Long shopId = couponDto.getShopId();//适用商铺
				if(couponDto.getIssuerShopId() != null){//不是平台所发行，仅适用指定店铺
					//查询订单的商铺
					if(!(shopId.toString()).equals(validOrder.getShopId().toString())){
						throw new ValidateException(CodeConst.CODE_CASHCOUPON_NO_OTHER_SHOP, CodeConst.MSG_CASHCOUPON_OTHER_SHOP);
					}
				}
			}
			amountTotal = this.payDao.getSumOrderGroupAmount(orderId);
			amount = new BigDecimal(amountTotal);							
			payAmount = this.packetDao.queryOrderPayAmount(orderId,orderPayType);
		}else{//单个订单支付
			//订单存在性
			validOrder = orderDao.getOrderById(orderId);
			CommonValidUtil.validObjectNull(validOrder, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_ORDER);
			//订单支付状态：未支付
			CommonValidUtil.validIntNoEqual(validOrder.getPayStatus(), CommonConst.PAY_STATUS_PAYED, 
					CodeConst.CODE_PAY_STATUS_SUCCESS, CodeConst.MSG_PAYED_ODDER);
			//适用商铺
			Long shopId = couponDto.getShopId();//适用商铺
			if(couponDto.getIssuerShopId() != null){//不是平台所发行，仅适用指定店铺
				//查询订单的商铺
				if(!(shopId.toString()).equals(validOrder.getShopId().toString())){
					throw new ValidateException(CodeConst.CODE_CASHCOUPON_NO_OTHER_SHOP, CodeConst.MSG_CASHCOUPON_OTHER_SHOP);
				}
			}
			amount = packetDao.queryOrderAmount(orderId);
			payAmount = packetDao.queryOrderPayAmount(orderId,orderPayType);
		}
		
		//消费有效时间范围
		CommonValidUtil.validCurDateOfDateRange(couponDto.getStartTime(), couponDto.getStopTime(), 
				CodeConst.CODE_NOT_IN_CONSUME_DATERANGE, CodeConst.MSG_NOT_IN_COUSUME_DATERANGE);
		//满多少可以使用
		Integer useablePrice = couponDto.getConditionPrice();
//		Double orderSettlePrice = pOrder.getSettlePrice();
		//订单金额未达到使用代金券额度
		CommonValidUtil.validDoubleGreaterThan(amount.doubleValue(), Double.valueOf(useablePrice), 
				CodeConst.CODE_USER_CASHCOUPON_CONDITION_PRICE, CodeConst.MSG_USER_CASHCOUPON_CONDITION_PRICE);
		//能否和同类券一起使用
		int useableTogether = couponDto.getUseTogetherFlag();
		if(useableTogether == 0){
			//查询是否使用代金券支付过
			int num = this.payDao.checkOrderIsPayByCash(orderId,uccId,orderPayType);
			CommonValidUtil.validIntNotZero(num, CodeConst.CODE_USER_CASHCOUPON_USE_TOGETHER_FLAG, CodeConst.MSG_USER_CC_NOTUSE_SAME);
		}
		//每单一次可用几张  
		long useNumberPerOrder = couponDto.getUseNumberPerOrder();
		//检查已经使用了多少张
		int usedNumber = this.userCouponDao.getCashUsedNumOfOrder(orderId, uccId,orderPayType);
		CommonValidUtil.validLongGreaterEqual(useNumberPerOrder, Long.valueOf(usedNumber), CodeConst.CODE_USER_CASHCOUPON_USE_NUMBER, 
				CodeConst.MSG_USER_CC_USEFUL_PER_ORDER);
		//面值
		Double price = couponDto.getPrice();
		//获取已支付总金额
//		Double sumPayedAmount = payService.getSumPayAmount(orderId);
		//剩余金额 = 应支付总额-已支付总额
		Double needPayAmount = amount.doubleValue() - payAmount.doubleValue() ; 
		if(needPayAmount<=0){
			if(number.getNum() <= 0){
				throw new ValidateException(CodeConst.CODE_CASHCOUPON_NOT, CodeConst.MSG_CASHCOUPON_NOT);//剩余金额<=0
			}else{
				return 1;
			}
		}
		//修改用户代金券信息
		PayDto pay = new PayDto();
		pay.setOrderId(orderId);
		pay.setUserId(userId);
		pay.setNeedPayAmount(needPayAmount);
//		int flag = userCouponService.consumeCoupon(list,validOrder,pay,orderPayType);
		//代金券本次被使用的金额
		Double usePrice = pay.getNeedPayAmount() >= price ? price : pay.getNeedPayAmount(); 
		//生成支付流水
		pay.setPayType(2);//代金券支付
		pay.setPayId(uccId);
		pay.setPayAmount(usePrice);
		pay.setOrderPayType(orderPayType);
		pay.setOrderPayTime(DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT));
		payDao.addOrderPay(pay);
		//修改代金券使用状态
		int flag = userCouponDao.consumeCoupon(userId,orderId,uccId,usePrice);
		String oId = null;
		Integer orderStatus = null;
		if(pay.getNeedPayAmount() <= price){//满额支付
			//更改订单状态支付完成
			if(orders != null && orders.size() > 0){//订单组
				for(Map e : orders){
					oId = (String)e.get("orderId");
					this.orderDao.updateOrderPayed(oId);
					//记录订单日志
					orderStatus = this.orderDao.getOrderStatusById(oId);
					this.saveOrderLog(oId, orderStatus, CommonConst.PAY_STATUS_PAYED);
				}
			}else{//单个
				this.orderDao.updateOrderPayed(orderId);
				//记录订单日志
				this.saveOrderLog(orderId, validOrder.getOrderStatus(), CommonConst.PAY_STATUS_PAYED);
			}
			//满额支付才进行结算
			OrderGoodsSettleUtil.detailOrderGoodsSettle(pay.getOrderId(),orderPayType);
		}
		number.incrNum(1);
		return flag;
	}
	
	private int saveOrderLog(String orderId, Integer orderStatus,Integer payStatus) throws Exception{
		OrderLogDto orderLog = new OrderLogDto();
		orderLog.setOrderId(orderId);
		orderLog.setPayStatus(payStatus);
		orderLog.setOrderStatus(orderStatus);
		orderLog.setLastUpdateTime(new Date());
		this.orderLogDao.saveOrderLog(orderLog);
		return 1;
	}
	
//	commnet date : 20150506 by lujianping
//	public int consumeCoupon(List<Map> orders,OrderDto order,PayDto pay,Integer orderPayType) throws Exception {
//		//代金券本次被使用的金额
//		Double usePrice = pay.getNeedPayAmount() >= pay.getPrice() ? pay.getPrice() : pay.getNeedPayAmount(); 
//		//生成支付流水
//		pay.setPayType(2);//代金券支付
//		pay.setPayId(Long.valueOf(pay.getUccId()));
//		pay.setPayAmount(usePrice);
//		pay.setOrderPayType(orderPayType);
//		payDao.addOrderPay(pay);
//		//修改代金券使用状态
//		int flag = userCouponDao.consumeCoupon(pay.getUserId(),pay.getOrderId(),pay.getUccId(),usePrice);
//		if(pay.getNeedPayAmount() <= pay.getPrice()){//满额支付
//			//更改订单状态支付完成
//			if(orders != null && orders.size() > 0){//订单组
//				for(Map e : orders){
//					this.orderDao.updateOrderPayed((String)e.get("orderId"));
//				}
//			}else{//单个
//				this.orderDao.updateOrderPayed(order.getOrderId());
//			}
//			//满额支付才进行结算
//			OrderGoodsSettleUtil.detailOrderGoodsSettle(pay.getUserId(), pay.getOrderId(),orderPayType);
//		}
//		//TODO 结算
////		if(1==flag){
////			detailOrderGoodsSettle(userId, orderId);
////		}
//		return flag;
//	}

	public UserCashCouponDto getUserCouponDto(Long userId,Long uccId) {
		return userCouponDao.getUserCouponDto(userId,uccId);
	}

	public Integer selectPerDayPerPerson(Long userId) throws Exception {
		return userCouponDao.selectPerDayPerPerson(userId);
	}

	public Integer selectCountUseNumber(String orderId) throws Exception  {
		return userCouponDao.selectCountUseNumber(orderId);
	}
	/**
	 * 调用存储过程处理订单结算
	 * @param t
	 * @throws Exception
	 */
	public void  detailOrderGoodsSettle(Long userId, String orderId) throws Exception{
		OrderGoodsDto orderGoods = new OrderGoodsDto();
		orderGoods.setOrderId(orderId);
		List<OrderGoodsDto>  list = orderGoodsDao.getOGoodsListByOrderId(orderGoods);
		for (OrderGoodsDto orderGoodsDto : list) {
			orderGoodsSettleDao.detailOrderGoodsSettle(userId,
					orderGoodsDto.getShopId(), orderGoodsDto.getGoodsId(),
					orderId);
		}
	}

	public int getCashUsedNumOfOrder(String orderId, Long ccId,Integer orderPayType)
			throws Exception {
		return this.userCouponDao.getCashUsedNumOfOrder(orderId, ccId,orderPayType);
	}

 	public int consumeCashCoupon(Long userId, String orderId, int orderPayType,
			List<Long> uccIds, Integer pwdType, String password) throws Exception {
		// TODO 用户消费代金券
 		//1.用户校验
 		Map user = validUser(userId);
 		//2.是否需要校验密码
 		validPassword(pwdType, password,user);
 		//3.订单校验
 		List<Map> orderList = validOrder(userId, orderId, orderPayType);
 		//4.代金券校验
 		List<Map> userCashCoupons = validUserCashCoupon(userId,uccIds);
 		//5.订单支付
 		// 获取订单实际需要支付的金额
 		BigDecimal amount = new BigDecimal(0);
		if (orderPayType == 0) {
			amount = packetDao.queryOrderAmount(orderId);
		} else {
			amount = new BigDecimal(payDao.getSumOrderGroupAmount(orderId));
		}
		logger.info("该订单剩共需要支付金额："+amount);
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = packetDao.queryOrderPayAmount(orderId, orderPayType);
		if (null == payAmount) {
			payAmount = new BigDecimal(0);
		}
		logger.info("该订单已经支付金额："+payAmount);
		// 订单剩余需要支付的金额
		//实际需要支付金额-已经支付金额 = 剩余需要支付金额
		BigDecimal surplusAmount = amount.subtract(payAmount);
		logger.info("该订单剩余需要支付金额："+surplusAmount);
		//未支付完全
		if(surplusAmount.compareTo(new BigDecimal(0)) > 0){
			Map<String,Integer> order = orderList.get(0);
			Long shopId = CommonValidUtil.isEmpty(order.get("shop_id"))?null:Long.parseLong(order.get("shop_id")+"");
			//是否需要分账标记
			boolean flag = false;
			//单张优惠券支付单个订单
			Map userCashCoupon = userCashCoupons.get(0);
			Long uccId = (Long) userCashCoupon.get("ucc_id");
			// 代金券剩余可用金额
			BigDecimal uccPrice = new BigDecimal(userCashCoupon.get("uccPrice") + "");
			// 代金券实际金额跟剩余需要支付金额比较
			int compResult = surplusAmount.compareTo(uccPrice);
			//本次支付实际支付金额
			BigDecimal actualAmount = new BigDecimal(0);
			if (compResult > 0) {
				// 未支付完全
				actualAmount = uccPrice;
			}else{
				//将订单支付完全
				actualAmount = surplusAmount;
				logger.info("完全支付，支付金额为："+actualAmount);
				// TODO 需要生成各种记录
				//支付前的订单状态
				Integer beforeOrderStatus = order.get("order_status");
				logger.info("支付前的订单状态："+beforeOrderStatus);
				//修改订单状态及支付状态
				OrderDto orderDto = new OrderDto();
				orderDto.setOrderId(orderId);
				orderDto.setLastUpdateTime(new Date());
				Integer afterOrderStatus = beforeOrderStatus;
//				if (beforeOrderStatus != null && CommonConst.UP_ODR_STS_YJZ.contains(beforeOrderStatus)) {
//					afterOrderStatus = CommonConst.ORDER_STS_YJZ;
//					//支付完全，需要进行分账
//					flag = true;
//				}
				orderDto.setOrderStatus(afterOrderStatus);
				orderDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
				orderDao.updateOrder(orderDto);
				//生成订单操作日志
				OrderLogDto orderLogDto = new OrderLogDto();
				orderLogDto.setRemark("消费卡支付");
				orderLogDto.setLastUpdateTime(new Date());
				orderLogDto.setOrderId(orderId);
				orderLogDto.setOrderStatus(afterOrderStatus);
				orderLogDto.setPayStatus(CommonConst.PAY_STATUS_PAYED);
				orderLogDao.saveOrderLog(orderLogDto);
				orderDto.setOrderStatus(CommonConst.ORDER_STS_YJZ);
				
				//修改库存
				storageServcie.insertShopStorageByOrderId(orderId,shopId);
			}
			//TODO //生成各种记录
			//生成订单支付记录
			PayDto payDto = new PayDto();
			String nowTime = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT);
			payDto.setOrderId(orderId);
			payDto.setLastUpdateTime(nowTime);
			payDto.setOrderPayTime(nowTime);
			payDto.setPayeeType(0);
			payDto.setPayId(uccId);
			payDto.setPayType(CommonConst.PAY_TYPE_CASH_COUPON);// 代金券支付
			payDto.setOrderPayType(orderPayType);
			payDto.setPayAmount(actualAmount.doubleValue());
			payDto.setShopId(shopId);
			
			if(actualAmount.doubleValue()>0){
				payDao.addOrderPay(payDto);

				//生成代金券支付账单记录
				UserXBillDto userXBill = new UserXBillDto();
				userXBill.setUserId(userId);
				userXBill.setUccId(uccId);
				userXBill.setOrderPayType(payDto.getOrderPayType());
				userXBill.setOrderId(payDto.getOrderId());
				userXBill.setMoney(-actualAmount.doubleValue());
				userXBill.setCreateTime(new Date());
				userXBill.setBillType(CommonConst.USER_CASHCOUPON_USE);
				userXBill.setBillTitle(order.get("order_title")+"");
				userXBill.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());//账单状态为已完成
				userXBill.setBillDesc("订单支付");
				userXBill.setAccountAmount(uccPrice.doubleValue());
				userXBillDao.insertUserXBillDao(userXBill);
			}

			
			//记录平台账单
			OrderDto odto = orderDao.getOrderById(orderId);
			PlatformBillDto platformBillDto = buildPlatformBill(userId, actualAmount.doubleValue(), odto, "消费支付", 1, user.get("mobile")+"", 2);
			platformBillDao.insertPlatformBill(platformBillDto);
			//修改代金券使用金额
			//支付前代金券已使用金额
 			Double usedPrice = CommonValidUtil.isEmpty(userCashCoupon.get("used_price"))?0d:(Double.parseDouble(userCashCoupon.get("used_price")+""));
 			logger.info("支付前，消费券使用金额："+usedPrice.doubleValue());
 			logger.info("支付完成后，更新消费券使用金额："+(usedPrice.doubleValue()+actualAmount.doubleValue()));
			//支付后代金券使用金额=支付前使用金额+当前支付使用金额
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("usedPrice", (usedPrice+actualAmount.doubleValue()));
			param.put("payId", payDto.getPayId());
			userCouponDao.updateUserCashCoupon(param);
			
			//插入库存信息
			
//			if (flag) {
//				// TODO 分账
//				//满额支付才进行结算
//				OrderGoodsSettleUtil.detailOrderGoodsSettle(orderId,orderPayType);
//			}
			/*
			 * //支持多张优惠券支付多个订单
			for(Map ucc : userCashCoupons){
				// 代金券剩余可用金额
				BigDecimal uccPrice = new BigDecimal(ucc.get("uccPrice") + "");
				// 红包实际金额跟剩余需要支付金额比较
				int compResult = surplusAmount.compareTo(uccPrice);
				//本次支付实际支付金额
				BigDecimal actualAmount = new BigDecimal(0);
				if (compResult > 0) {
					// 未支付完全
					actualAmount = uccPrice;
				}else{
					//将订单支付完全
					actualAmount = surplusAmount;
					// TODO 需要生成各种记录
					
					break;
				}
			}*/
		}else{
			throw new ValidateException(CodeConst.CODE_CASHCOUPON_NOT, CodeConst.MSG_CASHCOUPON_NOT);
		}
		return 0;
	}
 	
 	/**
	 * 封装平台账单
	 * @param userId 用户编号
	 * @param payAmount 支付金额
	 * @param order 订单
	 * @param billType 账单类型
	 * @param billDirection 账单描述
	 * @param mobile 手机号码
	 * @param moneySource 资金来源
	 * @return
	 * @throws Exception
	 */
	private PlatformBillDto buildPlatformBill(Long userId,Double payAmount,OrderDto order,String billType,Integer billDirection,String mobile,Integer moneySource) throws Exception {
		PlatformBillDto platformBill = new PlatformBillDto();
		platformBill.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
		platformBill.setBillType(billType);
		platformBill.setBillDirection(billDirection);
		platformBill.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
		platformBill.setMoney(payAmount);
		platformBill.setOrderId(order.getOrderId());
		platformBill.setGoodsNumber(Double.valueOf(order.getGoodsNumber()));
		platformBill.setGoodsSettlePrice(order.getSettlePrice());
		platformBill.setCreateTime(new Date());
		platformBill.setSettleTime(new Date());
		platformBill.setBillDesc(billType);
		platformBill.setConsumerUserId(userId);
		platformBill.setConsumerMobile(mobile);
		platformBill.setMoneySource(moneySource);
		return platformBill;
	}
 	
 	/**
 	 * 代金券校验
 	 * @param userId
 	 * @param uccIds
 	 * @return
 	 * @throws Exception 
 	 */
 	private List<Map> validUserCashCoupon(Long userId,List<Long> uccIds) throws Exception{
 		List<Map> uccLists = userCouponDao.queryUserCashCouponsByUccIds(uccIds);
 		if(uccLists == null || uccLists.size() <= 0){
 			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_CASHCOUPON);
 		}
 		logger.info("=============>>uccLists="+uccLists);
 		for(Map ucc : uccLists){
 			Long uccId = (Long) ucc.get("ucc_id");
 			//代金券使用商铺，为null表示使用所有
 			Long shopId = (Long) ucc.get("shop_id");
 			// TODO 本期可不实现 后期需要判断该代金券适用商铺
 			
 			//代金券实际所属会员
 			Long dbUserId = CommonValidUtil.isEmpty(ucc.get("user_id"))?null:Long.parseLong(ucc.get("user_id")+"");
 			if(!StringUtils.equals(dbUserId+"", userId+"")){
 				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "编号["+uccId+"]"+"该代金券不属于您");
 			}
 			//持有代金券有效最后时间
 			String endTime = CommonValidUtil.isEmpty(ucc.get("end_time"))?null:(ucc.get("end_time")+"");
 			//发行代金券有效最后时间，如果endTime=null，则取stopTime进行校验
 			String stopTime = CommonValidUtil.isEmpty(ucc.get("stop_time"))?null:(ucc.get("stop_time")+"");
 			String uccUserStopDateTime = null;
 			if(StringUtils.isBlank(endTime)){
 				if(!StringUtils.isBlank(stopTime)){
 					uccUserStopDateTime = stopTime;
 				}
 			}else{
 				uccUserStopDateTime = endTime;
 			}
 			if(!DateUtils.validDateTime(uccUserStopDateTime)){
 				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "编号["+uccId+"]"+"该代金券已经过期");
 			}
 			//代金券发行面额
 			//Double price = CommonValidUtil.isEmpty(ucc.get("price"))?null:(Double.parseDouble(ucc.get("price")+""));
 			BigDecimal price = CommonValidUtil.isEmpty(ucc.get("price"))?null:(new BigDecimal(ucc.get("price")+""));
 	 		logger.info("=============>>代金券面额price="+price);
 			//该代金券已经使用面额，剩余使用金额=代金券发行面额-已经使用面额
 			//Double usedPrice = CommonValidUtil.isEmpty(ucc.get("used_price"))?null:(Double.parseDouble(ucc.get("used_price")+""));
 			BigDecimal usedPrice = CommonValidUtil.isEmpty(ucc.get("used_price"))?null:(new BigDecimal(ucc.get("used_price")+""));
 	 		logger.info("=============>>代金券使用金额usedPrice="+usedPrice);
 			//代金券实际可用金额
 			BigDecimal uccPrice = new BigDecimal(0.0);
 			if(null != price && price.doubleValue() > 0){
 				if(null != usedPrice){
 					//uccPrice = (price.doubleValue() - usedPrice.doubleValue());
 					uccPrice = price.subtract(usedPrice);
 					if(uccPrice.doubleValue() <= 0){
 		 				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "编号["+uccId+"]"+"该代金券可用金额为0");
 					}
 				}else{
 					uccPrice = price;
 				}
 	 			//将代金券实际可用金额存入对象中
 				logger.info("=============>>代金券剩余金额uccPrice="+uccPrice);
 	 			ucc.put("uccPrice", uccPrice);
 			}else{
	 			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "编号["+uccId+"]"+"该代金券实际面额为0");
 			}
 		}
 		return uccLists;
 	}
 	
 	/**
	 * 会员信息检测
	 * @param userId
	 * @return
	 */
	public Map validUser(Long userId) throws Exception{
		Map user = userDao.queryUserStatus(userId);
		if (user == null || user.size() <= 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_MEMBER);
		}
		int status = CommonValidUtil.isEmpty(user.get("status"))?0:Integer.valueOf(user.get("status")+"");
		if ( status != 1) {
			throw new ValidateException(CodeConst.CODE_USER_STATUS_ERROR, CodeConst.MSG_USER_STATUS_UNUSUAL);
		}
		return user;
	}
	
	/**
	 * 支付密码校验
	 * @param pwdType 校验类型
	 * @param password 密码值
	 * @param user 用户
	 * @return
	 * @throws Exception
	 */
	private boolean validPassword(Integer pwdType, String password,Map user) throws Exception{
		if (null != pwdType) {
			if (pwdType.intValue() == 0) {
				/*
				 * 客户端已经单独校验，不能校验两次。date:2015-09-18 --聂久乾
				//验证码校验
				String mobile = (String) user.get("mobile");
				boolean flag = sendSmsService.getSmsCodeIsOkByCashCoupon(mobile, null, password);
				if (!flag) {
					throw new ValidateException(CodeConst.CODE_VERICODE_53101,CodeConst.MSG_VC_ERROR);
				}*/
				return true;
			}else{
				//支付密码校验
				String payPassword = (String) user.get("pay_password");
				if(!StringUtils.equals(password, payPassword)){
					throw new ValidateException(CodeConst.CODE_PAY_PWD_ERROR,CodeConst.MSG_PAYPWD_AUTHEN_ERROR);
				}
			}
		}
		return true;
	}
	
	/**
	 * 订单检测
	 * @param userId
	 * @param orderId
	 * @return
	 * @throws Exception 
	 */
	private List<Map> validOrder(Long userId,String orderId,int orderPayType) throws Exception{
		List<Map> orders = new ArrayList<Map>();
		Map<String,Object> order = null;
		if (0 == orderPayType) {
			Integer payStatus = null;
			Integer orderStatus = null;
			order = packetDao.queryOrderIsExists(userId, orderId);
			if (null == order || order.size() <=0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_MISS_ORDER);
			}else{
				payStatus = Integer.parseInt(order.get("pay_status")+"");
				orderStatus = Integer.parseInt(order.get("order_status")+"");
				order.put("order_status", orderStatus);
			}
			orders.add(order);
			// TODO 当只有订单支付状态=0 未支付，才可以进行支付
			if (payStatus.intValue() == 1) {
				//已经支付，不需要再支付
				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58505,CodeConst.MSG_REDPACKET_USE_58505);
			}
			// TODO 订单状态为（已预定-0,已开单-1,派送中-2,自助下单-8）时，才可以进行支付
			if (!CommonConst.PAY_OK_FLAG.contains(orderStatus)) {
				//该状态已经不可支付
				throw new ValidateException(CodeConst.CODE_REDPACKET_USE_58507,CodeConst.MSG_ORDER_STATUS_ERROR);
			}
			
			//订单所属商铺状态校验
			Integer shopStatus = CommonValidUtil.isEmpty(order.get("shop_status"))?-1:Integer.parseInt(order.get("shop_status")+"");
			CommonValidUtil.validShopStatus(shopStatus, null);
			
		} else if (1 == orderPayType) {// 订单组支付
			//orders = payDao.queryOrderGroupById(orderId);
			orders = payDao.queryOrderGroupByOrderId(orderId);
			//CommonValidUtil.validObjectNull(list,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			if (null == orders || orders.size() == 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			}
		}
		return orders;
	}
	
	public Double getUserCashCouponBalance(Long userId) throws Exception {
		return userCouponDao.getUserCashCouponBalance(userId);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.cashcoupon.IUserCashCouponService#queryUserCashCouponsByUserId(java.lang.Long)
     */
    @Override
    public Map queryUserCashCouponsByUserId(Long userId) throws Exception
    {
        return userCashCouponDao.queryUserCashCouponsByUserId(userId);
    }
	
}
