package com.idcq.appserver.service.pay.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.BankInfo;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.common.billStatus.RechargeEnum;
import com.idcq.appserver.common.billStatus.WithdrawEnum;
import com.idcq.appserver.common.enums.AccountTypeEnum;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.dao.bank.IBankCardDao;
import com.idcq.appserver.dao.bank.IBankDao;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.bill.IUserXBillDao;
import com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.common.IConfigDao;
import com.idcq.appserver.dao.coupon.IUserCouponDao;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.membercard.IUserMemberBillDao;
import com.idcq.appserver.dao.message.IPushUserMsgDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderGoodsDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransaction3rdDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dao.pay.IWithdrawDao;
import com.idcq.appserver.dao.pay.OrderGoodsSettleDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shop.IShopWithDrawDao;
import com.idcq.appserver.dao.user.IPushUserTableDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bank.BankCardDto;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.bill.UserXBillDto;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.pay.OrderGoodsSettleLog;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.pay.WithdrawListDto;
import com.idcq.appserver.dto.pay.WithdrawRequestModel;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.goods.enums.ChainStoresTypeEnum;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.BillUtil;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 我的订单service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:14:50
 */
@Service
public class PayServiceImpl implements IPayServcie {
	
	private final Log logger = LogFactory.getLog(getClass());

	@Autowired
	public IPayDao payDao;
	@Autowired
	public ITransactionDao transactionDao;
	@Autowired
	public ITransaction3rdDao iTransaction3rdDao;
	@Autowired
	public IPacketDao packetDao;
	@Autowired
	public IOrderDao orderDao;
	@Autowired
	public IGoodsDao goodsDao;
	@Autowired
	IUserAccountDao userAccountDao;
	@Autowired
	IWithdrawDao withdrawDao;
	@Autowired
	IUserCouponDao userCouponDao;
	@Autowired
	public OrderGoodsSettleDao orderGoodsSettleDao;
	@Autowired
	public IOrderGoodsDao orderGoodsDao;
	@Autowired
	private IPushUserTableDao pushUserTableDao;
	@Autowired
	private IPushUserMsgDao pushUserMsgDao;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IUserMemberBillDao userMemberBillDao;
	@Autowired
	private IBankCardDao bankCardDao;
	@Autowired
	private IUserBillDao userBillDao;
	@Autowired
	private IUserDao userDao;
	@Autowired
	public IOrderLogDao orderLogDao;
	@Autowired
	public IShopDao shopDao;
	@Autowired
	private IBankDao bankDao;
	@Autowired
	private IShopWithDrawDao shopWithDrawDao;
	@Autowired
	private IShopBillDao shopBillDao;
	@Autowired
	private IShopAccountDao shopAccountDao;
	@Autowired
	private ICollectDao collectDao;
	@Autowired
	private IUserCashCouponDao userCashCouponDao;
	@Autowired
	private IUserXBillDao userXBillDao;
	@Autowired
	private IPushService pushService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	public IPlatformBillDao platformBillDao;
	@Autowired
	public IAttachmentRelationDao attachmentRelationDao;
    @Autowired
    private IStorageServcie storageService;
    @Autowired
    private IConfigDao configDao;
	
	public Integer addTransaction(TransactionDto transactionDto)
			throws Exception {

		return transactionDao.addTransaction(transactionDto);
	}

	public Long addOrderPay(PayDto payDto) throws Exception {
		
		if(payDto.getPayAmount()>0){
			payDao.addOrderPay(payDto);
		}
		
		// 获取订单实际需要支付的金额
		BigDecimal amount = packetDao.queryOrderAmount(String.valueOf(payDto
				.getOrderId()));
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = packetDao.queryOrderPayAmount(
				String.valueOf(payDto.getOrderId()), 0);
		if (payAmount == null) {
			payAmount = new BigDecimal(0);
		}
		OrderDto order = new OrderDto();
		order.setOrderId(payDto.getOrderId());
		if (payAmount.doubleValue() >= amount.doubleValue()) {
			order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
			orderDao.updateOrder(order);
		} else {
			order.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
			orderDao.updateOrder(order);
		}
		return payDto.getOrderPayId();
	}

	public void updateOrderGroupStatus(List<Map> list, int payStatus,Integer orderStatus,String remark) throws Exception {
		String orderId = null;
		OrderDto order = null;
		for (int i = 0, len = list.size(); i < len; i++) {
			orderId = (String) list.get(i).get("orderId");
			order = orderDao.getOrderById(orderId);
			order.setPayStatus(payStatus);
			if (orderStatus != null) {
				order.setOrderStatus(orderStatus);// 更改订单状态为已结账
			}
			if (2 == order.getOrderType()) {// 购买优惠券,需更新优惠券状态
				userCouponDao.updateUserCoupon(order.getUserId(),orderId, 1);
			}
			orderDao.updateOrder(order);
			
			//记录订单日志
			saveOrderLog(order,remark);
			
	        if(CommonConst.ORDER_STS_YJZ == order.getOrderStatus()){
				//修改库存
				storageService.insertShopStorageByOrderId(orderId,order.getShopId());
	        }
		}
	}

	private Map<String, Double> getDeductCouponAndRewardAmount(Double deductMoney,Double couponAmount) {
		double deductCouponAmount = 0;
		double deductRewardAmount = 0;
		
		if (couponAmount < deductMoney) {
			deductCouponAmount  = couponAmount;
			deductRewardAmount = NumberUtil.sub(deductMoney, couponAmount) ;
		}
		else {
			deductCouponAmount = deductMoney;
		}
		
		Map<String, Double> deductResult = new HashMap<String,Double>();
		deductResult.put("deductCoupon", deductCouponAmount);
		deductResult.put("deductReward", deductRewardAmount);
		return deductResult;
	}
	
	private PlatformBillDto buildPlatformBillFromMutiOrder(UserDto user,Double ductMoney,PayDto payDto,Double amountTotal,TransactionDto transactionDto,
			String billType,Integer billDirection) throws Exception {
		PlatformBillDto platformBill = new PlatformBillDto();
		platformBill.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
		platformBill.setBillType(billType);
		platformBill.setBillDirection(billDirection);
		platformBill.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
		if (billDirection > 0) { 
			platformBill.setMoney(ductMoney);
		}
		else if (billDirection < 0) {
			platformBill.setMoney(0 - ductMoney);
		}
		platformBill.setTransactionId(transactionDto.getTransactionId());
		platformBill.setOrderId(payDto.getOrderId());
		platformBill.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB);
		platformBill.setGoodsSettlePrice(amountTotal);
		platformBill.setCreateTime(new Date());
		platformBill.setSettleTime(new Date());
		platformBill.setBillDesc(billType);
		platformBill.setConsumerUserId(user.getUserId());
		platformBill.setConsumerMobile(user.getMobile());
		
		return platformBill;
	}
	
	public UserBillDto buildUserBillFromMutiOrder(UserDto user,PayDto payDto,UserAccountDto userAccount,String billType,Integer billDirection) throws Exception{
		UserBillDto userBillDto=new UserBillDto();
		userBillDto.setBillType(billType);
		userBillDto.setBillDirection(billDirection);
		userBillDto.setBillStatus(ConsumeEnum.ORDERED.getValue());//已预订
		userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
		//修改账单记录为负数
		userBillDto.setMoney(payDto.getPayAmount());
		//余额
		Double amount = 0D;
		if(null != userAccount){
			//消费记录账户余额
		    amount = userAccount.getAmount();
			userBillDto.setAccountAmount(amount);
		}
		userBillDto.setOrderPayType(payDto.getOrderPayType());
		userBillDto.setOrderId(payDto.getOrderId());
		
		userBillDto.setAccountAfterAmount(amount);
		userBillDto.setOrderId(payDto.getOrderId());
		userBillDto.setCreateTime(new Date());
		userBillDto.setBillDesc("消费");
		userBillDto.setUserId(payDto.getUserId());
		userBillDto.setConsumerUserId(payDto.getUserId());
		userBillDto.setOrderPayType(payDto.getOrderPayType());
		userBillDto.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_PROCESS);
		userBillDto.setConsumerMobile(user.getMobile());
	
		return userBillDto;
	}
	public Map<String, Object> addOrderTransAndPay(TransactionDto transactionDto, PayDto payDto) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取订单实际需要支付的金额
		BigDecimal amount = null;
		Double amountTotal = 0.0;
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = null;
		OrderDto order = null;
		int orderPayType = payDto.getOrderPayType();
		List<Map> list = null;
		// 需扣除的金额   区分消费金和平台奖励
		double deductMoney = 0;
		Map<String, Double> deductCouponAndRewardResult = null;
		Double couponAmount = Double.valueOf(0);
		Double rewardAmount = Double.valueOf(0);
		Double accountBlance = Double.valueOf(0);
		//扣除口的消费金和平台奖励余额
		double couponBlance = 0;
		double rewardBlance = 0;
		
		UserDto user=userDao.getUserById(transactionDto.getUserId());
		boolean flag = false; //结算标识
		if (1 == orderPayType) {// 订单组支付
			list = payDao.queryOrderGroupById(payDto.getOrderId());
			CommonValidUtil.validObjectNull(list,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			if (list.size() == 0) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			}
			OrderDto orderDto=orderDao.getOrderById((String)list.get(0).get("orderId"));
			payDto.setShopId(orderDto.getShopId());
			int orderStatus=orderDto.getOrderStatus();
			if((CommonConst.ORDER_STS_YJZ == orderStatus&&CommonConst.REVERSE_SETTLE_FLAG!=orderDto.getSettleFlag().intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus 
					|| CommonConst.ORDER_STS_YTD == orderStatus){
				//订单状态为不可支付状态
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
			}
			// 获取订单实际需要支付的金额
			amountTotal = payDao.getSumOrderGroupAmount(payDto.getOrderId());
			amount = new BigDecimal(amountTotal);
			payAmount = packetDao.queryOrderPayAmount(payDto.getOrderId(),payDto.getOrderPayType());
			// 查询账户余额 update by liuhuibin
			UserAccountDto account = userAccountDao.getAccountMoney(transactionDto.getUserId());
			couponAmount = account.getCouponAmount();
			rewardAmount = account.getRewardAmount();
			//账户可用余额
			accountBlance = NumberUtil.add(couponAmount, rewardAmount);
			
			if (payAmount.doubleValue() >= amount.doubleValue()) {// 已支付金额 >
				// 订单需支付的金额
				updateOrderGroupStatus(list,CommonConst.PAY_STATUS_PAY_SUCCESS,CommonConst.ORDER_STS_YJZ,"传奇宝支付");// 批量更新订单状态
				OrderGoodsSettleUtil.detailOrderGoodsSettle(transactionDto.getOrderId(), orderPayType);
			} else {
				// 需支付的金额
				double needPayAmount = NumberUtil.sub(amount.doubleValue(), payAmount.doubleValue());
				
				// 支付时的金额 > 账户余额
				if (payDto.getPayAmount() > accountBlance) {
					throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
				}
				transactionDao.addTransaction(transactionDto);
				payDto.setPayId(transactionDto.getTransactionId());
				if (payDto.getPayAmount() >= needPayAmount) {// 足额支付
					deductMoney = needPayAmount;
					//支付区分消费金和平台奖励 update by liuhuibin
					deductCouponAndRewardResult = getDeductCouponAndRewardAmount(deductMoney, couponAmount);
					// 增加支付流水
					payDto.setPayAmount(needPayAmount);
					// 增加消费金流水
					if (deductCouponAndRewardResult.get("deductCoupon").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_CONSUM);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductCoupon"));
						payDao.addOrderPay(payDto);
					}
					
					//增加平台奖励消费流水
					if (deductCouponAndRewardResult.get("deductReward").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_REWARD);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductReward"));
						payDto.setOrderPayId(null);
						payDao.addOrderPay(payDto);
					}
					// 修改订单状态
					updateOrderGroupStatus(list,CommonConst.PAY_STATUS_PAY_SUCCESS,CommonConst.ORDER_STS_YJZ,"传奇宝支付");// 批量更新订单状态
					
					OrderGoodsSettleUtil.detailOrderGoodsSettle(transactionDto.getOrderId(), orderPayType);
					// 修改交易流水的状态
					transactionDto.setStatus(1);// 支付完成
					transactionDao.updateTransaction(transactionDto);
				} else {
					deductMoney = payDto.getPayAmount();
					//支付区分消费金和平台奖励 update by liuhuibin
					deductCouponAndRewardResult = getDeductCouponAndRewardAmount(deductMoney, couponAmount);
					// 增加消费金流水
					if (deductCouponAndRewardResult.get("deductCoupon").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_CONSUM);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductCoupon"));
						payDao.addOrderPay(payDto);
					}
					
					//增加平台奖励消费流水
					if (deductCouponAndRewardResult.get("deductReward").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_REWARD);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductReward"));
						payDto.setOrderPayId(null);
						payDao.addOrderPay(payDto);
					}
					// 修改订单状态
					updateOrderGroupStatus(list,CommonConst.PAY_STATUS_NOT_PAY,null,"传奇宝支付");// 批量更新订单状态
				}
				// 更新账户余额  update by liuhuibin
				 couponBlance = NumberUtil.sub(couponAmount, deductCouponAndRewardResult.get("deductCoupon"));
				 rewardBlance = NumberUtil.sub(rewardAmount, deductCouponAndRewardResult.get("deductReward"));
			//	userAccountDao.updateAccountMoney(transactionDto.getUserId(),account.getAmount() - deductMoney);
				userAccountDao.updateCouponAndRewardAmount(transactionDto.getUserId(), 
						NumberUtil.sub(account.getAmount(), deductMoney), couponBlance , rewardBlance,null);
			}
			
			//记录用户账单
			UserBillDto userBillDto = buildUserBillFromMutiOrder(user, payDto, account, "消费", -1);
			
			//记录消费金账单
			if (deductCouponAndRewardResult.get("deductCoupon").compareTo(Double.valueOf(0)) > 0) {
				userBillDto.setMoney(0 - deductCouponAndRewardResult.get("deductCoupon"));
				userBillDto.setAccountAfterAmount(couponBlance);
				userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
				userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
				userBillDao.insertUserBill(userBillDto);
			}
			//记录平台奖励账单
			if (deductCouponAndRewardResult.get("deductReward").compareTo(Double.valueOf(0)) > 0) {
				userBillDto.setMoney(0 - deductCouponAndRewardResult.get("deductReward"));
				userBillDto.setAccountAfterAmount(rewardBlance);
				userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_REWARD);
				userBillDao.insertUserBill(userBillDto);
			}
			
			//记录平台账单platformBill  
			Double ductMoney = NumberUtil.add(deductCouponAndRewardResult.get("deductCoupon"), deductCouponAndRewardResult.get("deductReward"));
			PlatformBillDto platformBillDto = buildPlatformBillFromMutiOrder(user, ductMoney, payDto, amountTotal, transactionDto, "消费支付", 1);
			platformBillDao.insertPlatformBill(platformBillDto);
			
		} else {// 单个订单支付
			order = orderDao.getOrderById(transactionDto.getOrderId());
			if ( null == order){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_NOT_EXIST);
			}
//			if (order.getPayStatus() == CommonConst.PAY_STATUS_PAY_SUCCESS) {
//				throw new ValidateException(CodeConst.CODE_PAY_STATUS_SUCCESS,CodeConst.MSG_PAYED_ODDER);
//			}
			int orderStatus=order.getOrderStatus();
			if(CommonConst.ORDER_STS_YJZ == orderStatus || CommonConst.ORDER_STS_TDZ == orderStatus 
					|| CommonConst.ORDER_STS_YTD == orderStatus){
				//订单状态为不可支付状态
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
			}
			if (!transactionDto.getUserId().equals(order.getUserId())) {
				//订单不属于本人
				throw new ValidateException(CodeConst.CODE_ORDER_NOT_BELONG_TO_YOU,CodeConst.MSG_ORDER_NOT_BELONG_TO_YOU);
			}
			amount = packetDao.queryOrderAmount(payDto.getOrderId());
			payAmount = packetDao.queryOrderPayAmount(String.valueOf(payDto.getOrderId()),payDto.getOrderPayType());
			order.setOrderId(payDto.getOrderId());
			payDto.setShopId(order.getShopId());
			// 查询账户余额
			UserAccountDto account = userAccountDao.getAccountMoney(transactionDto.getUserId());
			couponAmount = account.getCouponAmount();
			rewardAmount = account.getRewardAmount();
			//账户可用余额
			accountBlance = NumberUtil.add(couponAmount, rewardAmount);
			
			if (payAmount.doubleValue() >= amount.doubleValue()) {// 已支付金额 >
				// 订单需支付的金额
				order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
//				if(orderStatus == CommonConst.ORDER_STS_YKD || orderStatus==CommonConst.ORDER_STS_PSZ){
//					order.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
//					order.setLastUpdateTime(new Date());
//					flag = true;
//				}
				order.setLastUpdateTime(new Date());
				order.setServerLastTime(System.currentTimeMillis());
				orderDao.updateOrder(order);
//				collectDao.updateShopResourceStatus(order.getOrderId(),CommonConst.RESOURCE_STATUS_NOT_IN_USE);
				if(flag){
					OrderGoodsSettleUtil.detailOrderGoodsSettle(transactionDto.getOrderId(), orderPayType);
				}
				saveOrderLog(order,"传奇宝支付");
				if (null != order && null != order.getOrderType() && 2 == order.getOrderType()) {// 购买优惠券,需更新优惠券状态
					userCouponDao.updateUserCoupon(transactionDto.getUserId(),transactionDto.getOrderId(),1);
				}
				resultMap.put("orderPayId", payDto.getOrderPayId());
				resultMap.put("amount", payDto.getPayAmount());
				
				return resultMap;
			} else {
				// 需支付的金额
				double needPayAmount =amount.subtract(payAmount).doubleValue();
				
				// 支付时的金额 > 账户余额
				if (payDto.getPayAmount().doubleValue() > accountBlance) {
					throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
				}
				transactionDao.addTransaction(transactionDto);
				payDto.setPayId(transactionDto.getTransactionId());
				if (payDto.getPayAmount() >= needPayAmount) {// 足额支付
					deductMoney = needPayAmount;
					//支付区分消费金和平台奖励 update by liuhuibin
					deductCouponAndRewardResult = getDeductCouponAndRewardAmount(deductMoney, couponAmount);
					// 增加支付流水
					payDto.setPayAmount(needPayAmount);
					// 增加消费金流水
					if (deductCouponAndRewardResult.get("deductCoupon").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_CONSUM);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductCoupon"));
						payDao.addOrderPay(payDto);
					}
					
					//增加平台奖励消费流水
					if (deductCouponAndRewardResult.get("deductReward").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_REWARD);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductReward"));
						payDto.setOrderPayId(null);
						payDao.addOrderPay(payDto);
					}
					
					
					// 修改订单状态
					order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
//					if(orderStatus == CommonConst.ORDER_STS_YKD || orderStatus==CommonConst.ORDER_STS_PSZ){
//						order.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
//						order.setLastUpdateTime(new Date());
//						collectDao.updateShopResourceStatus(order.getOrderId(),CommonConst.RESOURCE_STATUS_NOT_IN_USE);
//						flag = true;
//					}
					order.setLastUpdateTime(new Date());
					order.setServerLastTime(System.currentTimeMillis());
					orderDao.updateOrder(order);
					if(flag){
						OrderGoodsSettleUtil.detailOrderGoodsSettle(transactionDto.getOrderId(), orderPayType);
					}
					//记录订单日志
					saveOrderLog(order,"传奇宝支付");
					//修改交易流水的状态
					transactionDto.setStatus(1);// 支付完成
					transactionDto.setPayAmount(needPayAmount);
					transactionDao.updateTransaction(transactionDto);
					if (null != order && 2 == order.getOrderType()) {// 购买优惠券,需更新优惠券状态
						userCouponDao.updateUserCoupon(transactionDto.getUserId(),transactionDto.getOrderId(), 1);
					}
				} else {
					//支付区分消费金和平台奖励 update by liuhuibin
					deductMoney = payDto.getPayAmount();
					deductCouponAndRewardResult = getDeductCouponAndRewardAmount(deductMoney, couponAmount);
					// 增加消费金流水
					if (deductCouponAndRewardResult.get("deductCoupon").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_CONSUM);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductCoupon"));
						payDao.addOrderPay(payDto);
					}
					
					//增加平台奖励消费流水
					if (deductCouponAndRewardResult.get("deductReward").compareTo(Double.valueOf(0)) > 0) {
						payDto.setPayType(CommonConst.PAY_TYPE_REWARD);
						payDto.setPayAmount(deductCouponAndRewardResult.get("deductReward"));
						payDto.setOrderPayId(null);
						payDao.addOrderPay(payDto);
					}
					// 修改订单状态
					order.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
					order.setServerLastTime(System.currentTimeMillis());
					orderDao.updateOrder(order);
					//记录订单日志
					saveOrderLog(order,"传奇宝支付");
				}
				// 更新账户余额
				//userAccountDao.updateAccountMoney(transactionDto.getUserId(),account.getAmount() - deductMoney);
				// 更新账户余额  update by liuhuibin
				couponBlance = NumberUtil.sub(couponAmount, deductCouponAndRewardResult.get("deductCoupon"));
				rewardBlance = NumberUtil.sub(rewardAmount, deductCouponAndRewardResult.get("deductReward"));
				userAccountDao.updateCouponAndRewardAmount(transactionDto.getUserId(), 
						NumberUtil.sub(account.getAmount(), deductMoney), couponBlance , rewardBlance,null);
			}
			//记录用户账单
			UserBillDto userBillDto=buildUserBill(payDto,order,account,"消费",-1);
			//记录消费金账单
			if (deductCouponAndRewardResult.get("deductCoupon").compareTo(Double.valueOf(0)) > 0) {
				userBillDto.setMoney(0 - deductCouponAndRewardResult.get("deductCoupon"));
				userBillDto.setAccountAfterAmount(couponBlance);
				userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
				userBillDto.setBillTitle(order.getOrderTitle());
				userBillDao.insertUserBill(userBillDto);
			}
			//记录平台奖励账单
			if (deductCouponAndRewardResult.get("deductReward").compareTo(Double.valueOf(0)) > 0) {
				userBillDto.setMoney(0 - deductCouponAndRewardResult.get("deductReward"));
				userBillDto.setBillTitle(order.getOrderTitle());
				userBillDto.setAccountAfterAmount(rewardBlance);
				userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_REWARD);
				userBillDao.insertUserBill(userBillDto);
			}
			
			//记录平台账单platformBill  
			Double ductMoney = NumberUtil.add(deductCouponAndRewardResult.get("deductCoupon"), deductCouponAndRewardResult.get("deductReward"));
			PlatformBillDto platformBillDto = buildPlatformBill(user,ductMoney,order,transactionDto,"消费支付",1);
			platformBillDao.insertPlatformBill(platformBillDto);
		}
		/*
		 * if(amount==null){ amount=new BigDecimal(0); } if(payAmount==null){
		 * payAmount=new BigDecimal(0); }
		 */
		resultMap.put("orderPayId", payDto.getOrderPayId());
		resultMap.put("amount", deductMoney);
		return resultMap;
	}
	
	public void saveOrderLog(OrderDto order,String remark) throws Exception{
		OrderLogDto orderLogDto=new OrderLogDto();
		orderLogDto.setOrderId(order.getOrderId());
		orderLogDto.setPayStatus(order.getPayStatus());
		orderLogDto.setOrderStatus(order.getOrderStatus());
		orderLogDto.setLastUpdateTime(new Date());
		orderLogDto.setRemark(remark);
		orderLogDao.saveOrderLog(orderLogDto);
	}
	
    public PlatformBillDto buildPlatformBillBy3Rd(Integer userBillType,Long userId,Double money,OrderDto order,Long transactionId,
            UserAccountDto userAccount,String billType,Integer billDirection) throws Exception {
        PlatformBillDto platformBill = new PlatformBillDto();
        platformBill.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
        platformBill.setBillType(billType);
        platformBill.setBillDirection(billDirection);
        platformBill.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBill.setMoney(money);
        platformBill.setTransactionId(transactionId);
        platformBill.setOrderId(order.getOrderId());
        platformBill.setGoodsNumber(Double.valueOf(order.getGoodsNumber()));
        platformBill.setGoodsSettlePrice(order.getSettlePrice());
        platformBill.setCreateTime(new Date());
        platformBill.setSettleTime(new Date());
        platformBill.setBillDesc(billType);
        platformBill.setConsumerUserId(userId);
        //查询用户手机号码
        UserDto user = userDao.getUserById(userId);
        if(user!=null){
            platformBill.setConsumerMobile(user.getMobile());
        }
        //获取资金来源
        Integer moneySource = formatMoneySource(userBillType);
        platformBill.setMoneySource(moneySource);
        return platformBill;
    }
    /**
     * 获取资金来源
     * TODO 以后有其他userBillType请小伙伴自己补充
     */
    private Integer formatMoneySource(Integer userBillType){
        Integer moneySource = 0;//默认支付宝
        /*'资金来源：支付宝支付-0,传奇宝支付-1,消费卡支付-2,微信支付-3,建行银行卡=4,建行信用卡=5',*/
        //支付宝
        if(userBillType==CommonConst.USER_BILL_TYPE_ALIPAY){
            moneySource = 0;
        }
        //建行借记卡充值
        if(userBillType==CommonConst.USER_BILL_TYPE_CCB_DEBIT){
            moneySource = 4;
        }
        // 建行信用卡充值
        if(userBillType==CommonConst.USER_BILL_TYPE_CCB_CREDIT){
            moneySource = 5;
        }
        if (userBillType==CommonConst.USER_BILL_TYPE_WEIXIN) {
        	 moneySource = 3;
        }
        return moneySource;
    }
    /**
     * 获取支付类型
     * TODO 以后有其他userBillType请小伙伴自己补充
     */
    private Integer getPayTypeByUserBillType(Integer userBillType){
        Integer payType = 0;//默认支付宝
        /*'资金来源：支付宝支付-0,传奇宝支付-1,消费卡支付-2,微信支付-3,建行银行卡=4,建行信用卡=5',*/
        //支付宝
        if(userBillType==CommonConst.USER_BILL_TYPE_ALIPAY){
            payType = 0;
        }
        //建行借记卡充值
        if(userBillType==CommonConst.USER_BILL_TYPE_CCB_DEBIT){
            payType = 9;
        }
        // 建行信用卡充值
        if(userBillType==CommonConst.USER_BILL_TYPE_CCB_CREDIT){
            payType = 10;
        }
        if (userBillType == CommonConst.USER_BILL_TYPE_WEIXIN) {
        	payType = 8;
        }
        return payType;
    }
	
	private PlatformBillDto buildPlatformBill(UserDto user,Double ductMoney,OrderDto order,TransactionDto transactionDto,
			String billType,Integer billDirection) throws Exception {
		PlatformBillDto platformBill = new PlatformBillDto();
		platformBill.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
		platformBill.setBillType(billType);
		platformBill.setBillDirection(billDirection);
		platformBill.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
		if (billDirection > 0) { 
			platformBill.setMoney(ductMoney);
		}
		else if (billDirection < 0) {
			platformBill.setMoney(0 - ductMoney);
		}
		platformBill.setTransactionId(transactionDto.getTransactionId());
		platformBill.setOrderId(order.getOrderId());
		platformBill.setMoneySource(CommonConst.PLT_BILL_MNY_SOURCE_CQB);
		platformBill.setGoodsNumber(Double.valueOf(order.getGoodsNumber()));
		platformBill.setGoodsSettlePrice(order.getSettlePrice());
		platformBill.setCreateTime(new Date());
		platformBill.setSettleTime(new Date());
		platformBill.setBillDesc(billType);
		platformBill.setConsumerUserId(user.getUserId());
		platformBill.setConsumerMobile(user.getMobile());
		
		return platformBill;
	}
	/**
	 * 构建userbill
	 * @param payDto
	 * @param order
	 * @param userAccount
	 * @param billType 账单类型（消费，充值等）
	 * @param billDirection  '账单类型:1（账户资金增加）,-1（账户资金减少）'
	 * @return
	 */
	public UserBillDto buildUserBill(PayDto payDto,OrderDto order,UserAccountDto userAccount,String billType,Integer billDirection) throws Exception{
	    int billStatus=order.getOrderStatus()+20;
		UserBillDto userBillDto=new UserBillDto();
		userBillDto.setBillType(billType);
		userBillDto.setBillDirection(billDirection);
		userBillDto.setBillStatus(billStatus);//已预订
		userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
		//修改账单记录为负数 
		userBillDto.setMoney(payDto.getPayAmount());
		//余额
		Double amount = 0D;
		if(null != userAccount){
			//消费记录账户余额
		    amount = userAccount.getAmount();
			userBillDto.setAccountAmount(amount);
		}
		userBillDto.setAccountAfterAmount(amount);
		userBillDto.setOrderId(payDto.getOrderId());
		userBillDto.setCreateTime(new Date());
		userBillDto.setBillDesc("消费");
		userBillDto.setUserId(payDto.getUserId());
		userBillDto.setConsumerUserId(payDto.getUserId());
		userBillDto.setOrderPayType(payDto.getOrderPayType());
		if(billStatus==ConsumeEnum.ORDERED.getValue() || billStatus==ConsumeEnum.HAVE_ORDER.getValue() || billStatus==ConsumeEnum.DISPATCH.getValue()
				|| billStatus==ConsumeEnum.CHARGEBACKING.getValue()){
			userBillDto.setBillStatusFlag(1);
		}else{
			userBillDto.setBillStatusFlag(0);
		}
		userBillDto.setConsumerMobile(order.getMobile());
		String orderTitle = order.getOrderTitle();
		
		if(!StringUtils.isEmpty(orderTitle)){
			userBillDto.setBillTitle(orderTitle);
		}else{
			userBillDto.setBillTitle("消费"+payDto.getPayAmount());
		}
		//billLogo设置为商铺logo
		List<Long> shopMicroLogo = attachmentRelationDao.getAttachRelatIdListByCondition(order.getShopId(), CommonConst.BIZ_TYPE_IS_SHOP, CommonConst.PIC_TYPE_IS_SUONUE);
		if(!CollectionUtils.isEmpty(shopMicroLogo)){
			userBillDto.setBillLogo(shopMicroLogo.get(0));
		}
		return userBillDto;
	}
	
	public double getSumPayAmount(String orderId) throws Exception {
		return payDao.getSumPayAmount(orderId,null);
	}

	public String payBy3rd(Transaction3rdDto transaction3rdDto) throws Exception {
		// 获取订单实际需要支付的金额
		BigDecimal amount = null;
		Double amountTotal = 0.0;
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = null;
		OrderDto order = null;
		List<Map> list = null;
		Long transactionId = null;
		Integer orderPayType = transaction3rdDto.getOrderPayType();
		// 终端类型为空
		if (StringUtils.isEmpty(transaction3rdDto.getTerminalType())) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_TERMINAL_TYPE_IS_NULL);
		}
		// 第三方支付平台名称
		if (StringUtils.isEmpty(transaction3rdDto.getRdOrgName())) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_RDORG_NAME_IS_NULL);
		}
		if (1 == orderPayType) {// 订单组支付
			list = payDao.queryOrderGroupById(transaction3rdDto.getOrderId());
			CommonValidUtil.validObjectNull(list,
					CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			OrderDto orderDto=orderDao.getOrderById((String)list.get(0).get("orderId"));
			int orderStatus=orderDto.getOrderStatus();
			if((CommonConst.ORDER_STS_YJZ == orderStatus&&CommonConst.REVERSE_SETTLE_FLAG!=orderDto.getSettleFlag().intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus 
					|| CommonConst.ORDER_STS_YTD == orderStatus){
				//订单状态为不可支付状态
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
			}
			// 获取订单实际需要支付的金额
			amountTotal = payDao.getSumOrderGroupAmount(transaction3rdDto
					.getOrderId());
			amount = new BigDecimal(amountTotal);
			// 获取订单实际已经支付的金额
			payAmount = packetDao.queryOrderPayAmount(
					transaction3rdDto.getOrderId(),
					transaction3rdDto.getOrderPayType());
		} else {// 单个订单支付
			order = orderDao.getOrderById(transaction3rdDto.getOrderId());
			if (order == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_ORDER_NOT_EXIST);
			}
			int orderStatus=order.getOrderStatus();
			if(CommonConst.ORDER_STS_YJZ == orderStatus || CommonConst.ORDER_STS_TDZ == orderStatus 
					|| CommonConst.ORDER_STS_YTD == orderStatus){
				//订单状态为不可支付状态
				throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
			}
			if (order.getPayStatus() == CommonConst.PAY_STATUS_PAY_SUCCESS) {
				throw new ValidateException(CodeConst.CODE_PAY_STATUS_SUCCESS,
						CodeConst.MSG_PAY_STATUS_SUCCESS);
			}
			if (!transaction3rdDto.getUserId().equals(order.getUserId())) {
				throw new ValidateException(
						CodeConst.CODE_ORDER_NOT_BELONG_TO_YOU,
						CodeConst.MSG_ORDER_NOT_BELONG_TO_YOU);
			}
			// 获取订单实际需要支付的金额
			amount = packetDao.queryOrderAmount(transaction3rdDto.getOrderId());
			// 获取订单实际已经支付的金额
			payAmount = packetDao.queryOrderPayAmount(
					String.valueOf(transaction3rdDto.getOrderId()),
					transaction3rdDto.getOrderPayType());
			order.setOrderId(transaction3rdDto.getOrderId());
		}
		// 需支付的金额
		double needPayAmount = NumberUtil.sub(amount.doubleValue(), payAmount.doubleValue());
		// 查询账户余额
		// 如果金额大于实际需要支付的金额，则将第三方支付金额等于需要支付金额
		if (transaction3rdDto.getPayAmount() > needPayAmount) {
			// 实际需要第三方支付多少钱
			transaction3rdDto.setPayAmount(needPayAmount);
		}
		// 使用第三方接口
		// 判断用户id和订单id是否存在, 存在的话直接返回交易号 2015.6.4
		Long transactionIdFromDb = iTransaction3rdDao.getPayByUserIdOrderId(transaction3rdDto);
		if ( transactionIdFromDb != null) {
			return FieldGenerateUtil.generatebitOrderId(transactionIdFromDb);
		}
		// 生成待支付信息
		iTransaction3rdDao.payBy3rd(transaction3rdDto);
		transactionId = transaction3rdDto.getTransactionId();
		String orderId = FieldGenerateUtil.generatebitOrderId(transactionId);
		return orderId;
	}

	public List<Map<String, Object>> getMy3rdPay(Long userId, int status,
			String rdOrgName, int pageNo, int pageSiz) throws Exception {
		return iTransaction3rdDao.getMy3rdPay(userId, status, rdOrgName,
				pageNo, pageSiz);
	}

	public int getMy3rdPayCount(Long userId, int status, String rdOrgName,
			int pageNo, int pageSiz) throws Exception {
		return iTransaction3rdDao.getMy3rdPayCount(userId, status, rdOrgName,
				pageNo, pageSiz);
	}

	public Map<String, Object> nofity3rdPayStatus(
			Transaction3rdDto transaction3rdDto,Integer userBillType) throws Exception {
		boolean flag = false; //结算标识
		String payRe = "success";
		// 获取订单实际需要支付的金额
		BigDecimal amount = null;
		Double amountTotal = 0.0;
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = null;
		// 银行返回交易状态
		Integer status = transaction3rdDto.getStatus();
		// 获取第三方支付信息
		Transaction3rdDto tDB = iTransaction3rdDao
				.ge3rdPayById(transaction3rdDto);
		//交易id
		Long transactionId = transaction3rdDto.getTransactionId();
		if(null==tDB){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"交易记录不存在");
		}
		// 交易成功了不再重复处理2015.5.13
		if(tDB.getStatus() != null && tDB.getStatus() == CodeConst.CODE_STATUS_SUCCSSS){
			return null;
		}
		
		// 0位订单支付，1为充值支付，区别：订单支付充值orderId,充值没有。
		Integer transactionType = tDB.getTransactionType();
		logger.info("transactionType为"+transactionType);
		if (null == transactionType) {
			transactionType = 0;
		}
		Integer orderPayType = tDB.getOrderPayType();
		if (null == orderPayType) {
			orderPayType = 0;
		}
		// 增加订单支付信息
		PayDto payDto = new PayDto();
		List<Map> list = null;
		OrderDto order = null;
		if (null != tDB) {
			payDto.setOrderId(tDB.getOrderId());
			payDto.setPayId(transactionId);
			payDto.setPayAmount(tDB.getPayAmount());
			payDto.setOrderPayType(orderPayType);
			payDto.setUserId(tDB.getUserId());
			// 平台收款
			payDto.setPayeeType(0);
			//支付类型
			Integer userPayType = getPayTypeByUserBillType(userBillType);
			payDto.setPayType(userPayType);
		}
		Long userId = tDB.getUserId();
		UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
		// 支付成功
		if (CodeConst.CODE_STATUS_SUCCSSS == status) {
			// 充值
			if (1 == transactionType) {
	             //用户id
                double paymount = tDB.getPayAmount();
			  //TODO 1、增加userbill  2、更新账号余额
			    UserChargeDto userCharge = new UserChargeDto();
			     //待反馈进度
		        userCharge.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
		        userCharge.setBillDesc(CommonConst.MEMBER_CHARGE);
		        userCharge.setTransactionId(transactionId);
		        userCharge.setAmount(paymount);
		        //充值代表资金增加
		        userCharge.setBillDirection(1);
		        userCharge.setUserRole(CommonConst.MEMBER);
		        userCharge.setCreateTime(new Date());
		        userCharge.setBillType(CommonConst.CHARGE);
		        userCharge.setUserId(userId);
		        userCharge.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
		        userCharge.setBillTitle("充值"+paymount+"元");
		        userCharge.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
		        userCharge.setUserBillType(userBillType);
		        
		        // 充值前余额
		        Double accountAmount =userAccount.getAmount();
		        //充值后余额
		        Double accountMoney = NumberUtil.add(userAccount.getCouponAmount(), paymount);
		        userCharge.setAccountAmount(accountAmount);
	            userCharge.setAccountAfterAmount(accountMoney);

		        userMemberBillDao.saveChargeBill(userCharge);
				
				// 更新传奇宝账号余额
				userAccountDao.updateUserAccount(userId, paymount, null, null, paymount, null
						,null,null,null,null,null,null,null,null,null);
			} else if (2 == transactionType) {
				// TODO 会员卡一期所有关于会员卡功能先不做
			} else if (0 == transactionType) {
				if (CommonConst.PAY_TYPE_GROUP == orderPayType) {// 订单组支付
				    logger.info("订单组支付");
					list = payDao.queryOrderGroupById(tDB.getOrderId());
					CommonValidUtil.validObjectNull(list,
							CodeConst.CODE_PARAMETER_NOT_EXIST,
							CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
					OrderDto orderDto=orderDao.getOrderById((String)list.get(0).get("orderId"));
					payDto.setShopId(orderDto.getShopId());
					int orderStatus=orderDto.getOrderStatus();
					if((CommonConst.ORDER_STS_YJZ == orderStatus&&CommonConst.REVERSE_SETTLE_FLAG!=orderDto.getSettleFlag().intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus 
						 || CommonConst.ORDER_STS_YTD == orderStatus){
						//订单状态为不可支付状态
						throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
					}
					// 获取订单实际需要支付的金额
					amountTotal = payDao.getSumOrderGroupAmount(tDB.getOrderId());
					amount = new BigDecimal(amountTotal);
					// 获取订单实际已经支付的金额
					payAmount = packetDao.queryOrderPayAmount(tDB.getOrderId(),
							tDB.getOrderPayType());
				} else {// 单个订单支付
				    logger.info("单订单支付");
					order = orderDao.getOrderById(tDB.getOrderId());
					if (order == null) {
						throw new ValidateException(
								CodeConst.CODE_PARAMETER_NOT_EXIST,
								CodeConst.MSG_ORDER_NOT_EXIST);
					}
					if (order.getPayStatus() == CommonConst.PAY_STATUS_PAY_SUCCESS) {
						throw new ValidateException(
								CodeConst.CODE_PAY_STATUS_SUCCESS,
								CodeConst.MSG_PAY_STATUS_SUCCESS);
					}
					int orderStatus=order.getOrderStatus();
					if(CommonConst.ORDER_STS_YJZ == orderStatus || CommonConst.ORDER_STS_TDZ == orderStatus 
							 || CommonConst.ORDER_STS_YTD == orderStatus){
						//订单状态为不可支付状态
						throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
					}
					if (!tDB.getUserId().equals(order.getUserId())) {
						throw new ValidateException(
								CodeConst.CODE_ORDER_NOT_BELONG_TO_YOU,
								CodeConst.MSG_ORDER_NOT_BELONG_TO_YOU);
					}
					// 获取订单实际需要支付的金额
					amount = packetDao.queryOrderAmount(tDB.getOrderId());
					// 获取订单实际已经支付的金额
					payAmount = packetDao
							.queryOrderPayAmount(
									String.valueOf(tDB.getOrderId()),
									tDB.getOrderPayType());
					order.setOrderId(tDB.getOrderId());
					payDto.setShopId(order.getShopId());
				}
				// 已支付金额 > 订单需支付的金额,更新订单状态
				if (payAmount.doubleValue() >= amount.doubleValue()) {
					// 修改交易流水的状态
					transaction3rdDto.setStatus(1);// 支付完成
					if (CommonConst.PAY_TYPE_GROUP == orderPayType) {
						updateOrderGroupStatus(list,CommonConst.PAY_STATUS_PAY_SUCCESS,CommonConst.ORDER_STATUS_WAIT_COMMENT,"第三方支付");// 批量更新订单状态
					} else {
						order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
						/*Integer orderStatus = order.getOrderStatus(); TODO app支付完成 不做结算，收银机手动触发结算
						//如果订单状态为1,2（已开单，派送中）且足额支付，则将订单置为已结账状态
						if(null!=orderStatus
								&&(CommonConst.ORDER_STS_PSZ==orderStatus||CommonConst.ORDER_STS_YKD==orderStatus)){
							//已结账状态
							order.setOrderStatus(CommonConst.ORDER_STS_YJZ);
							flag = true;
						}*/
						orderDao.updateOrder(order);
						
				        if(CommonConst.ORDER_STS_YJZ == order.getOrderStatus()){
							//修改库存
							storageService.insertShopStorageByOrderId(order.getOrderId(),order.getShopId());
				        }
						//全额支付才结算
					}
					if (null!=order && 2 == order.getOrderType()) {// 购买优惠券,需更新优惠券状态
						userCouponDao.updateUserCoupon(tDB.getUserId(),
								tDB.getOrderId(), 1);
					}
					if(flag){
						OrderGoodsSettleUtil.detailOrderGoodsSettle(tDB.getOrderId(), orderPayType);
					}
				} else {
					// 需支付的金额
					double needPayAmount = NumberUtil.sub(amount.doubleValue(), payAmount.doubleValue());
					// transactionDao.addTransaction(transactionDto);
					payDto.setPayId(tDB.getTransactionId());
					if (payDto.getPayAmount() >= needPayAmount) {// 足额支付
						if (CommonConst.PAY_TYPE_GROUP == orderPayType) {
							updateOrderGroupStatus(list,
									CommonConst.PAY_STATUS_PAY_SUCCESS,CommonConst.ORDER_STATUS_WAIT_COMMENT,"第三方支付");// 批量更新订单状态
						} else {
							Integer orderStatus = order.getOrderStatus();
							//如果订单状态为1,2（已开单，派送中）且足额支付，则将订单置为已结账状态
							if(null!=orderStatus
									&&(CommonConst.ORDER_STS_PSZ==orderStatus||CommonConst.ORDER_STS_YKD==orderStatus)){
								//已结账状态
								order.setOrderStatus(CommonConst.ORDER_STS_YJZ);
								flag = true;
							}
							order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
							orderDao.updateOrder(order);
						}
						// 增加支付流水
						payDto.setPayAmount(needPayAmount);
						payDao.addOrderPay(payDto);
						if(order!=null){
							payDto.setShopId(order.getShopId());
						}
						// 修改交易流水的状态
						transaction3rdDto.setStatus(1);// 支付完成
						if (null!=order&&2 == order.getOrderType()) {// 购买优惠券,需更新优惠券状态
							userCouponDao.updateUserCoupon(tDB.getUserId(),
									tDB.getOrderId(), 1);
						}
						if(flag){
							OrderGoodsSettleUtil.detailOrderGoodsSettle(tDB.getOrderId(), orderPayType);
						}
					} else {
						// 增加支付流水
						payDao.addOrderPay(payDto);
						// 修改订单状态
						if (CommonConst.PAY_TYPE_GROUP == orderPayType) {
							updateOrderGroupStatus(list,
									CommonConst.PAY_STATUS_NOT_PAY,CommonConst.ORDER_STATUS_WAIT_PAY,"第三方支付");// 批量更新订单状态
						} else {
							order.setPayStatus(CommonConst.PAY_STATUS_NOT_PAY);
							orderDao.updateOrder(order);
						}
					}
				}
				//第三方支付成功之后，记录用户账单，充值情况时候，已经增加账单，无需增加
				if(null!=payDto&&null!=order){
				    logger.info("订单支付，增加消费记录，增加充值记录");
		
					//增加充值记录（2015.8.27需求，消费的时候先记录一条充值记录，再增加一条消费记录）
					UserBillDto userChargeBill = buildUserBill(payDto,order,null,"充值",1);
					
					userChargeBill.setAccountAmount(userAccount.getAmount());
					userChargeBill.setMoney(tDB.getPayAmount());
					userChargeBill.setAccountAfterAmount(NumberUtil.add(tDB.getPayAmount(), userAccount.getCouponAmount()));
					userChargeBill.setBillDesc("第三方生成的充值账单记录");
					userChargeBill.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
					userChargeBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
					// 需要设置交易号2015.9.30
					userChargeBill.setTransactionId(tDB.getTransactionId());
					//用户账单logo类型
					userChargeBill.setUserBillType(userBillType);
					//消费金
					userChargeBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
					//设置账单不可现
					userChargeBill.setIsShow(CommonConst.USER_BILL_NOT_IS_SHOW);
					userBillDao.insertUserBill(userChargeBill);
				
			           //增加消费记录
                    UserBillDto userBillDto=buildUserBill(payDto,order,null,"消费",-1);
                    
                    //截取小数4位
                    userBillDto.setMoney(-tDB.getPayAmount());
                    //消费
                    userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
                    //消费金
                    userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
                    userBillDto.setAccountAfterAmount(userAccount.getCouponAmount());
                    userBillDto.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
                    userBillDto.setAccountAmount(userAccount.getAmount());
                    userBillDao.insertUserBill(userBillDto);
					//记录平台账单
					PlatformBillDto platformBillDto = buildPlatformBillBy3Rd(userBillType, userId, tDB.getPayAmount(), order, transactionId, userAccount, "消费支付", CommonConst.BILL_DIRECTION_ADD);
			        platformBillDao.insertPlatformBill(platformBillDto);
					
				}
			}
		} else {// 支付失败情况： 成功更新订单表状态1dcq_order为未支付，删除订单支付表记录
            logger.info("支付失败情况");
			payRe = "error";
				// 消费，存在订单情况
			if (0 == transactionType) {
				// 获取订单支付记录
				List<PayDto> payList = payDao.getOrderPayList(tDB.getOrderId(),CommonConst.PAY_STATUS_PAY_SUCCESS);
				if (CollectionUtils.isNotEmpty(payList)) {
					int payType = 0;
					for (PayDto pay : payList) {
						payType = pay.getPayType();
						// 现金支付
						if (payType == CommonConst.PAY_TYPE_CASH) {
							TransactionDto transactionDto = new TransactionDto();
							// 失败
							transactionDto.setStatus(2);
							transactionDto.setTransactionId(transaction3rdDto
									.getTransactionId());
							// 更新流水记录状态
							transactionDao.updateTransaction(transactionDto);
							// 删除现金支付记录
							payDao.deletePayByOrderPayId(pay.getOrderPayId());
						}
					}
				}
			}
		}
		int re = iTransaction3rdDao.nofity3rdPayStatus(transaction3rdDto);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("re", re);
		resultMap.put("payRe", payRe);
		resultMap.put("transactionType", transactionType);
		resultMap.put("userId", tDB.getUserId());
		resultMap.put("amount", tDB.getPayAmount());
		resultMap.put("orderId", tDB.getOrderId());
		return resultMap;
	}
	// 更新用户账单信息交易记录
	private void updateUserbillStatus(Integer billStatus,Integer billStatusFlag, Long transactionId)
			throws Exception {
		// 更新用户账单信息交易记录
		UserChargeDto userCharge = new UserChargeDto();
		userCharge.setTransactionId(transactionId);
		userCharge.setBillStatus(billStatus);
		userCharge.setBillStatusFlag(billStatusFlag);
		userMemberBillDao.updateUsesrBillByTransactionId(userCharge);
	}
	
	// 更新商铺账单信息交易记录
	private void updateShopbillStatus(Integer billStatus, Long transactionId)
				throws Exception {
			// 更新商铺账单信息交易记录
			ShopBillDto shopBillDto = new ShopBillDto();
			shopBillDto.setTransactionId(transactionId);
			shopBillDto.setBillStatus(billStatus);
			shopBillDao.updateShopBillByTransactionId(shopBillDto);
	}

	/**
	 * 调用存储过程处理订单结算
	 * 
	 * @param t
	 * @throws Exception
	 */
	public void detailOrderGoodsSettle(Long userId, String orderId)
			throws Exception {
		OrderGoodsDto orderGoods = new OrderGoodsDto();
		OrderGoodsSettleLog orderGoodsSettleLog = new OrderGoodsSettleLog();
		orderGoods.setOrderId(orderId);
		List<OrderGoodsDto> list = orderGoodsDao
				.getOGoodsListByOrderId(orderGoods);
		for (OrderGoodsDto orderGoodsDto : list) {
			orderGoodsSettleDao.detailOrderGoodsSettle(userId,
					orderGoodsDto.getShopId(), orderGoodsDto.getGoodsId(),
					orderId);
			orderGoodsSettleLog.setGoodsId(orderGoodsDto.getGoodsId());
			orderGoodsSettleLog.setUserId(userId);
			orderGoodsSettleLog.setOrderId(orderId);
			orderGoodsSettleLog.setShopId(orderGoodsDto.getShopId());
			orderGoodsSettleDao.addOrderGoodsSettleLog(orderGoodsSettleLog);
		}
	}

	/**
	 * 用户提现说明：
	 * 	约束：只能提现平台奖励(reward_amount)
	 * 	关键逻辑：
	 *		1.减少平台奖励(reward_amount)和账单
	 *		2.新增冻结资金(freeze_amount)和账单
	 *		3.新增提现记录
	 *	 modify by huangrui on 2015.11.30
	 *
	 */
	public Long withdrawByUser(WithdrawDto withdrawDto) throws Exception {
		Long userId = withdrawDto.getUserId();
		Double withdrawAmount = withdrawDto.getAmount(); //提现金额
		
		// 校验用户账户
		UserAccountDto oldAccount = validateUserAccount(userId, withdrawAmount);
		
		// 更新用户账户金额:减少平台奖励、新增冻结资金
		userAccountDao.updateUserAccount(userId, null, -withdrawAmount, null, null, withdrawAmount,
				null,null,null,null,null,null,null,null,null);
		
		// 设置提现后余额
		withdrawDto.setNextWithdrawAmount(NumberUtil.sub(oldAccount.getRewardAmount(), withdrawAmount));
				
		// 新增提现记录
		withdrawDao.withdraw(withdrawDto);
		
		// 新增账单
		saveWithdrawUserBill(withdrawDto, oldAccount);
		
		
		// 更改银行卡使用时间
		bankCardDao.updateBankCardUseTime(withdrawDto.getCardNumber(), userId);
		
		// 返回主键
		return withdrawDto.getWithdrawId();
	}

	/**
	 * 新增提现账单
	 * @param withdrawDto
	 * @param accountDto
	 * @throws Exception
	 */
	private void saveWithdrawUserBill(WithdrawDto withdrawDto, UserAccountDto accountDto)
			throws Exception {
		Long billLogo = bankDao.getBankLogoByName(withdrawDto.getBankName());
		
		// 平台奖励减少账单
		userBillDao.insertUserBill(BillUtil.buildUserRewardBillForWithdraw(withdrawDto, accountDto, billLogo, false));
		
		// 新增冻结资金账单
		userBillDao.insertUserBill(BillUtil.buildUserFreezeBillForWithdraw(withdrawDto, accountDto, billLogo,10, true));
		
		
	}
	
	/**
	 * 校验用户账户
	 * @param userId 用户ID
	 * @param withdrawAmount 提现金额
	 */
	private UserAccountDto validateUserAccount(Long userId,  Double withdrawAmount) throws Exception{
		
		// 查询账户余额
		UserAccountDto account = userAccountDao.getAccountMoney(userId);
		if(null==account){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"用户传奇宝账号不存在");
		}
		if(0==account.getAccountStatus()){
			throw new ValidateException(CodeConst.CODE_USER_ACCOUNT_FROZEN_58301, CodeConst.MSG_USER_ACCOUNT_FROZEN);
		}
		
		Double canUseAmount = account.getRewardAmount(); //可提现平台奖励
		//  账户余额不足
		if (NumberUtil.sub(canUseAmount, withdrawAmount) < 0)
			throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,
					CodeConst.MSG_ACCOUNT_NOT_BALANCE);
		
		return account;
	}
	
	/**
	 * 商铺提现说明：
	 * 	约束：优先从线上营业收入(online_income_amount)口出，不够在从减少平台奖励(reward_amount)扣除
	 *  关键逻辑：
	 *  	1.先扣除线上营业收入，不足再扣除平台奖励和新增相应账单
	 *  	2.新增冻结资金(freeze_amount)和相应账单
	 *  	3.新增提现记录
	 *  
	 */
	public Map<String, Object> withdrawByShop(ShopWithDrawDto shopWithDrawDto) throws Exception {
		
		//新增加处理分店提现处理银行卡2016-7-13
		shopWithDrawDto = deailBankCard(shopWithDrawDto);
		
		Map<String, Object> map = new HashMap<String, Object>();
		Long shopId = shopWithDrawDto.getShopId();
		Double withdrawAmount = shopWithDrawDto.getAmount(); //提现金额
		
		// 校验商铺账户
		ShopAccountDto account = validateShopAccount(shopId, withdrawAmount,null);
		//先扣手续费
//		account = deailWithdrawCommission(account,shopWithDrawDto);
		Double withdrawCommission = account.getWithdrawCommission();
		
		//先获取从线上营业收入扣除的金额
		double deductOnlineIncomeAmount = deductOnlineIncomeAmount(shopWithDrawDto, account);
		shopAccountDao.updateShopAccount(shopId, null, -deductOnlineIncomeAmount, null, null, null, deductOnlineIncomeAmount,null,null,null, null);
		shopWithDrawDto.setOnlineIncomeFreeze(deductOnlineIncomeAmount); 
		map.put("onlineIncomeAmount", deductOnlineIncomeAmount);
		
		boolean deduceFromReward = false; //是否需要从平台奖励扣除
		double deduceRewardAmount = 0d;
		if(withdrawAmount > deductOnlineIncomeAmount){
			// 再从平台奖励扣除
			deduceRewardAmount = deductRewardAmount(shopWithDrawDto, account, deductOnlineIncomeAmount);
			shopAccountDao.updateShopAccount(shopId, null, null, -deduceRewardAmount, null, null, deduceRewardAmount,null,null,null, null);
			deduceFromReward = true;
			
		}
		shopWithDrawDto.setRewardFreeze(deduceRewardAmount);
		map.put("rewardAmount", deduceRewardAmount);
		
		// 查询支行名称 8.12变更
		String cardNumber = shopWithDrawDto.getCardNumber();
		String subbranName = bankCardDao.getSubbranchNameByNum(cardNumber);
		if (StringUtils.isNotBlank(subbranName)) {
			shopWithDrawDto.setBankSubbranchName(subbranName);
		}
		shopWithDrawDto.setWithdrawCommission(withdrawCommission);
		
		shopWithDrawDto.setNextWithdrawAmount(NumberUtil.sub(NumberUtil.add(account.getOnlineIncomeAmount(), account.getRewardAmount()), NumberUtil.add(withdrawAmount, withdrawCommission)));
		shopWithDrawDao.shopWithdraw(shopWithDrawDto);
		//处理手续费
		deailWithdrawCommission(shopWithDrawDto);
		
		//新增减少线上营业收入账单
		if(deductOnlineIncomeAmount != 0D){
			shopBillDao.insertShopBill(BillUtil.buildShopOnlineIncomeAmountBillForWithdraw(shopWithDrawDto,account,deductOnlineIncomeAmount, false));
		}
		
		// 新增平台奖励扣除账单
		if(deduceFromReward && deduceRewardAmount != 0D){
			shopBillDao.insertShopBill(BillUtil.buildShopRewardAmountBillForWithdraw(shopWithDrawDto,account,deduceRewardAmount, false));
		}
		// 新增冻结资金账单
		shopBillDao.insertShopBill(BillUtil.buildShopFreezeBillForWithdraw(shopWithDrawDto, account, true));
		
		// 更改银行卡使用时间
		bankCardDao.updateBankCardUseTime(cardNumber, shopId);
		
		map.put("withdrawId", shopWithDrawDto.getWithDrawId());

		return map;
	}
	/**
	 * 处理手续费
	 * @throws Exception 
	 */
	private ShopAccountDto deailWithdrawCommission(ShopWithDrawDto shopWithDrawDto) throws Exception{
		Long shopId = shopWithDrawDto.getShopId();
		ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
		Double onlineIncomeAmount = account.getOnlineIncomeAmount();
		Double rewardAmount = account.getRewardAmount();
		Double withdrawCommission = shopWithDrawDto.getWithdrawCommission();//手续费
		Double onlineIncomeSubMoney = 0D;//线上收入减少金额
		Double rewardSubMongy = 0D;//奖励减少金额
		Double accountAmount = account.getAmount();
		Long transactionId = shopWithDrawDto.getWithDrawId();
		Double freezeAmount = NumberUtil.add(account.getFreezeAmount(), withdrawCommission);//处理后冻结金额
		//大于0再处理
		if(withdrawCommission>0){
			//线上收入>手续费
			if(onlineIncomeAmount>=withdrawCommission){
				logger.info("线上账号扣除扣除提现手续费"+withdrawCommission);
				onlineIncomeSubMoney = withdrawCommission;
				Double onlineIncomeAfterAmount = NumberUtil.sub(onlineIncomeAmount, withdrawCommission);
				insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION, 
						CommonConst.BILL_DIRECTION_DOWN, "提现手续费转出线上收入"+withdrawCommission+"元", CommonConst.SHOP_BILL_STATUS_OVER, 
						CommonConst.SHOP_ACCOUNT_TYPE_INCOME,accountAmount, onlineIncomeAfterAmount, -withdrawCommission, 
						"提现手续费"+withdrawCommission+"元",transactionId,null);
			}
			
			//奖励>手续费
			else if(rewardAmount>=withdrawCommission){
				logger.info("奖励账号扣除扣除提现手续费"+withdrawCommission);
				rewardSubMongy = withdrawCommission;
				Double rewardAmountAfterAmount = NumberUtil.sub(rewardAmount, withdrawCommission);
				insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION, CommonConst.BILL_DIRECTION_DOWN, "提现手续费转出奖励收入"+withdrawCommission+"元", CommonConst.SHOP_BILL_STATUS_OVER, 
						CommonConst.SHOP_ACCOUNT_TYPE_REWARD,accountAmount, rewardAmountAfterAmount,-withdrawCommission, "提现手续费"+withdrawCommission+"元",transactionId,null);
			}

			// 线上收入+ 奖励收入 足够
			else if (onlineIncomeAmount < withdrawCommission
					&& rewardAmount < withdrawCommission
					&& NumberUtil.add(onlineIncomeAmount, rewardAmount) >= withdrawCommission) {
				
				logger.info("奖励账号扣除扣除提现手续费"+withdrawCommission);
				onlineIncomeSubMoney = onlineIncomeAmount;
				insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION,
						CommonConst.BILL_DIRECTION_DOWN, "提现手续费转出线上收入"+onlineIncomeAmount+"元",
						CommonConst.SHOP_BILL_STATUS_OVER,
						CommonConst.SHOP_ACCOUNT_TYPE_INCOME, accountAmount, 0D,
						-onlineIncomeAmount, "提现手续费" + onlineIncomeAmount
								+ "元", null,null);

				// 奖励扣除金额
				Double changeRewardAmount = NumberUtil.sub(withdrawCommission,
						onlineIncomeAmount);
				rewardSubMongy = changeRewardAmount;
				Double rewardAmountAfterAmount = NumberUtil.sub(rewardAmount,
						changeRewardAmount);
				accountAmount = NumberUtil.sub(accountAmount, changeRewardAmount);

				insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION,
						CommonConst.BILL_DIRECTION_DOWN, "奖励账号扣除扣除提现手续费"+ changeRewardAmount + "元",
						CommonConst.SHOP_BILL_STATUS_OVER,
						CommonConst.SHOP_ACCOUNT_TYPE_REWARD, accountAmount,
						rewardAmountAfterAmount, -changeRewardAmount, "提现手续费"
								+ changeRewardAmount + "元", null,null);

			}	
			
			
			//冻结金额
			logger.info("记录手续费冻结金额流水..");
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION, 
					CommonConst.BILL_DIRECTION_ADD, "提现手续费转移冻结金额"+withdrawCommission+"元", CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.SHOP_ACCOUNT_TYPE_FREEZE,accountAmount, freezeAmount, withdrawCommission, 
					"提现手续费"+withdrawCommission+"元",transactionId,null);
			shopAccountDao.updateShopAccount(shopId, null, -onlineIncomeSubMoney, -rewardSubMongy, null, null, withdrawCommission,null,null,null, null);

			//返回处理后账号信息
			account.setWithdrawCommission(withdrawCommission);
			account.setOnlineIncomeAmount(NumberUtil.sub(account.getOnlineIncomeAmount(), onlineIncomeSubMoney));
			account.setRewardAmount(NumberUtil.sub(account.getRewardAmount(),rewardSubMongy));
			account.setFreezeAmount(NumberUtil.add(account.getFreezeAmount(), withdrawCommission));
		}
		
		return account;
	}
	private ShopWithDrawDto deailBankCard(ShopWithDrawDto shopWithDrawDto) throws Exception{
		
		Long shopId = shopWithDrawDto.getShopId();
		// 查询卡号记录,TODO 假如为分店，查询不到绑定银行卡信息，则取总店银行卡，并给分店绑定此卡号
		ShopDto shopDB = shopDao.getShopById(shopId);
        CommonValidUtil.validObjectNull(shopDB, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");
		shopWithDrawDto.setHandleRemark(shopDB.getShopName());
		BankCardDto bankCardDto = getBankCardByCard(shopWithDrawDto.getCardNumber(), shopId.toString(),AccountTypeEnum.SHOP.getValue());
		if(bankCardDto != null){
			shopWithDrawDto.setUserName(bankCardDto.getName());
			shopWithDrawDto.setBankName(bankCardDto.getBankName());
		}
		else{
			
			if(ChainStoresTypeEnum.IS_CHAIN_STORES.getValue()==shopDB.getChainStoresType().intValue()){
				//获取总店银行卡
				BankCardDto headShopCard = getBankCardByCard(null,shopDB.getHeadShopId().toString(),AccountTypeEnum.SHOP.getValue());
				shopWithDrawDto.setUserName(headShopCard.getName());
				shopWithDrawDto.setBankName(headShopCard.getBankName());
				shopWithDrawDto.setCardNumber(headShopCard.getCardNumber());
				logger.info("总店银行卡号："+headShopCard.getCardNumber());
				
				boolean isBindFlag = isBind(shopId, headShopCard.getCardNumber());
				if (!isBindFlag) {
					logger.info("分店开始绑定总店银行卡号...");
					//系统将分店银行卡默认绑定为总店银行卡
					headShopCard.setUserId(shopId);
					headShopCard.setBankCardId(null);
					headShopCard.setLastUseTime(new Date());
					bankCardDao.saveBankCard(headShopCard);
				}
				
			}
		}
	return shopWithDrawDto;
	}

	/**
	 * 判断银行卡是否与用户已经绑定
	 * 
	 * @param userId
	 * @param cardNumber
	 * @throws Exception
	 */
	public boolean isBind(Long userId, String cardNumber) throws Exception {
		Integer count = bankCardDao.getBankCardByMap(userId, cardNumber);
		if (null==count || 0==count) {
			return false;
		}
		return true;
	}
	/**
	 * 计算从线上营业收入扣除的金额
	 * @param shopWithDrawDto
	 * @param account
	 * @return
	 */
	private double deductOnlineIncomeAmount(ShopWithDrawDto shopWithDrawDto,
			ShopAccountDto account) {
		// 用户有设置扣除多少，就使用设置值
		if(shopWithDrawDto.getOnlineIncomeFreeze() != null){
			return shopWithDrawDto.getOnlineIncomeFreeze();
		}
		Double withdrawAmount = shopWithDrawDto.getAmount(); //提现金额
		Double onlineIncomeAmount = account.getOnlineIncomeAmount();

		if(onlineIncomeAmount == null)
			onlineIncomeAmount = 0d;
		
		// 线上营业收入可以扣完情况
		if( onlineIncomeAmount >= withdrawAmount){
			return withdrawAmount;
		}else {
			return onlineIncomeAmount;
		}
	}
	
	/**
	 * 计算从平台奖励扣除的金额
	 * @param shopWithDrawDto
	 * @param account
	 * @return
	 */
	private double deductRewardAmount(ShopWithDrawDto shopWithDrawDto,
			ShopAccountDto account, double onlineIncomeAmount) throws Exception{
		// 用户有设置扣除多少，就使用设置值
		if(shopWithDrawDto.getRewardFreeze() != null){
			return shopWithDrawDto.getRewardFreeze();
		}
		Double withdrawAmount = NumberUtil.sub(shopWithDrawDto.getAmount(), onlineIncomeAmount) ; //还需要提现金额
		Double rewardAmount = account.getRewardAmount();
		
		// 没有奖励不能从里面扣除
		if(rewardAmount == null || rewardAmount < withdrawAmount)
			throw new ValidateException(CodeConst.CODE_PLATFORM_REWARD_NOT_ENOUGH, "平台奖励账户余额不足");
		
		return withdrawAmount;
		
	}

	/**
	 * 校验商铺账户
	 * @param shopId
	 * @param withdrawAmount
	 * @param headquartersId 总店id
	 * @return
	 * @throws Exception
	 */
	private ShopAccountDto validateShopAccount(Long shopId, Double withdrawAmount,Long headquartersId) throws Exception {
		ShopDto shopDB = shopDao.getShopById(shopId);
		Double withdrawCommission = 0D;
		//判断手续费
		if(null != shopDB){
			Double percentage = shopDB.getPercentage();
			if(percentage.doubleValue()==0){
				ConfigQueryCondition parmConfig = new ConfigQueryCondition();
				
				parmConfig.setConfigKeys( new String[] {"withdrawCommission"});
				List<ConfigDto> sysConfigs = configDao.queryForConfig(parmConfig, null);
				Double coefficientNum = 0.006;
				if(CollectionUtils.isNotEmpty(sysConfigs)){
					coefficientNum =  Double.valueOf(sysConfigs.get(0).getConfigValue());
				}
				withdrawCommission = NumberUtil.multiply(withdrawAmount, coefficientNum);
			}
		}

		// 查询账户余额
		ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
		if(null==account){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"商铺传奇宝账号不存在");
		}
		if(null ==account.getAccountStatus() || 0==account.getAccountStatus()){
			throw new ValidateException(CodeConst.CODE_USER_ACCOUNT_FROZEN_58301, CodeConst.MSG_USER_ACCOUNT_FROZEN);
		}
		
		Double onlineIncomeAmount = account.getOnlineIncomeAmount(); // 在线营业收入余额
		Double rewardAmount = account.getRewardAmount(); // 平台奖励余额
		
		//余额不足
		if (NumberUtil.sub(NumberUtil.add(onlineIncomeAmount, rewardAmount), NumberUtil.add(withdrawCommission, withdrawAmount)) < 0)
			throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,
					CodeConst.MSG_ACCOUNT_NOT_BALANCE);
		
		//是否属于总店
		if(headquartersId != null){
			if(headquartersId.longValue() != shopDB.getHeadShopId().longValue()){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"该商铺不属于"+shopDB.getShopName()+"分店");
			}
		}
		account.setWithdrawCommission(Double.valueOf(NumberUtil.roundDoubleToStr(withdrawCommission, 2)));
		
		return account;
	}
	
	public List<Map<String, Object>> getWithdrawList(Map<String, Object> map,
			int pageNo, int pageSize) throws Exception {
		return withdrawDao.getWithdrawList(map, pageNo, pageSize);
	}

	public int getWithdrawListCount(Map<String, Object> map) throws Exception {
		return withdrawDao.getWithdrawListCount(map);
	}

	@Override
	public Map getOrdersToBePayedAmount(String orderId, int orderPayType)
			throws Exception {
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = null;
		BigDecimal amount = new BigDecimal(0);// 订单总价
		Double amountTotal = 0.0;
		OrderDto order = null;
		if (orderPayType == 1) {
			List<Map> list = payDao.queryOrderGroupById(orderId);
			CommonValidUtil.validObjectNull(list,
					CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			if (CollectionUtils.isEmpty(list)) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
						CodeConst.MSG_ORDER_GROUP_NOT_EXIST);
			}
			for (Map orderGroup : list) {
				order = orderDao.getOrderById((String) orderGroup
						.get("orderId"));
				amountTotal += order.getSettlePrice();
			}
			payAmount = packetDao.queryOrderPayAmount(orderId, 1);
		} else {
			order = orderDao.getOrderById(orderId);
			CommonValidUtil.validObjectNull(order,
					CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_ORDER_NOT_EXIST);
			// 获取订单实际需要支付的金额
			amount = packetDao.queryOrderAmount(orderId);
			amountTotal = amount.doubleValue();
			// 获取订单实际已经支付的金额
			payAmount = packetDao.queryOrderPayAmount(orderId, 0);
		}
		if (payAmount == null) {
			payAmount = new BigDecimal(0);
		}
		// 订单需支付的钱
		Double needPayAmount = NumberUtil.sub(amountTotal.doubleValue(), payAmount.doubleValue());
		if(needPayAmount<0){
			needPayAmount=0.0;
		}
		Map resultMap = new HashMap();
		resultMap.put("amount", new DecimalFormat("##0.00").format(needPayAmount));
		return resultMap;
	}

	@Override
	public String groupOrders(String orderIds) throws Exception {
		String[] orders = orderIds.split(",");
		String orderGroupId = null;// 订单支付组
		if (orders != null) {
			OrderDto order = null;
			String orderId=null;
			for (String oId : orders) {
				order = orderDao.getOrderById(oId);
				CommonValidUtil.validObjectNull(order,CodeConst.CODE_ORDER_GROUP_ORDER_NOT_EXIST,CodeConst.MSG_ORDER_GROUP_ORDER_NOT_EXIST);
			}
			for(int i=0,len=orders.length;i<len;i++){
				orderId=orders[i];
				if(i==0){
					//order = orderDao.getOrderById(orderId);
					orderGroupId= FieldGenerateUtil.generateOrderId(order.getShopId());
				}
				payDao.groupOrders(orderGroupId, orderId);
			}
			
		}
		return orderGroupId;
	}
	
	
	@Override
	public void handleAlipayBack(Transaction3rdDto transaction3rdDto)throws Exception
    {
        // 数据库查询流水
        Transaction3rdDto transactionFromDB = iTransaction3rdDao.ge3rdPayById(transaction3rdDto);
        // 避免重复通知操作
        if (transactionFromDB == null || CommonConst.TRANSACTION_STATUS_FINISH.equals(transactionFromDB.getStatus()))
        {
            logger.info("第三方已通知，不再处理");
            return;
        }

        // 成功
        // 更新交易记录

        transactionFromDB.setRdNotifyId(transaction3rdDto.getRdNotifyId());
        transactionFromDB.setStatus(CommonConst.TRANSACTION_STATUS_FINISH); // 交易成功
        transactionFromDB.setRdNotifyTime(new Date());
        iTransaction3rdDao.nofity3rdPayStatus(transaction3rdDto);



        // '用户id,当商铺提现时为商铺ID'
        Long userId = transactionFromDB.getUserId();
        // 更新商铺账户金额
        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(userId);

        if (shopAccountDto == null)
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺账号不存在，请检查1dcq_shop_account表是否存在shopId="
                    + transactionFromDB.getUserId());

        // 更新传奇宝账号余额, 防止账号余额为null
        Double amount = shopAccountDto.getAmount();
        if (amount == null){
            amount = 0d;
        }
        Double accountAfterAmount = shopAccountDto.getDepositAmount();
        double payAmount = transactionFromDB.getPayAmount();
        accountAfterAmount = NumberUtil.add(accountAfterAmount, payAmount);
        Integer arrearsFlag = null;
        if(accountAfterAmount >= 0)
        {
            arrearsFlag = CommonConst.ARREARS_FLAG_FALSE;
        }
        shopAccountDao.updateShopAccount(userId, payAmount, null, null, null, payAmount, null, arrearsFlag,null,null, null);
        
        // 插入商铺流水
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setTransactionId(transactionFromDB.getTransactionId());
        shopBillDto.setBillStatus(2); // 充值成功 更新商铺账单 成功 账单状态:处理中=1,成功=2,失败=3
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setShopId(userId);
        shopBillDto.setMoney(payAmount);
        shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_RECHARGE);//'账单类型:销售商品=1,支付保证金=2,购买红包=3,提现=4,充值=5,推荐奖励=6',
        shopBillDto.setBillDirection(1);//1代表增加
        shopBillDto.setAccountAmount(amount);
        shopBillDto.setBillDesc("第三方生成的充值账单记录");
        shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT);//保证金
        shopBillDto.setAccountAfterAmount(accountAfterAmount);
        shopBillDto.setBillTitle("充值"+NumberUtil.formatDoubleStr(payAmount, 2));
        shopBillDao.insertShopBill(shopBillDto);

        // 若充值后账户余额大于0，须更新商铺状态 商家状态:审核中-99,正常-0,下线-1,删除-2,欠费-3 
        //防止缓存有问题，直接查询数据库
        ShopDto shopDto = shopDao.getShopEssentialInfo(userId);
        if (null == shopDto)
        {
            logger.error("shopDao.getShopEssentialInfo()查询商铺不存在");
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        }
        int status = shopDto.getShopStatus();
        logger.info("更新前商铺状态："+status+",充值后保证金余额："+accountAfterAmount);
        if (shopDto != null && status == CommonConst.SHOP_LACK_MONEY_STATUS
                && accountAfterAmount >= 0)
        {
            shopDto.setShopStatus(CommonConst.SHOP_NORMAL_STATUS);
            shopDao.updateShopStatus(shopDto);
            // 店铺状态有修改，清空缓存
            DataCacheApi.del(CommonConst.KEY_SHOP + userId);
            try
            {
                //处理结算
                List<String> orderIds = orderDao.getNotSettleOrderIds(userId,0,100);
                if(CollectionUtils.isNotEmpty(orderIds))
                {
                    for (String orderId : orderIds)
                    {
                        logger.info("定时处理的未结算订单ID:" + orderId);
                        OrderGoodsSettleUtil.detailSingleOrder(orderId);
                    }
                }
                logger.info("推送消息到收银机start");
                // 推送消息到收银机
                JSONObject pushTarget = new JSONObject();
                pushTarget.put("action", CommonConst.ACTION_SHOP_DATA_UPDATE);
                pushTarget.put("shopId", userId);
                pushTarget.put("lastUpdate", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
                PushDto push = new PushDto();
                push.setShopId(userId);
                push.setAction(CommonConst.ACTION_SHOP_DATA_UPDATE);
                pushTarget.put("shopStatus", CommonConst.SHOP_NORMAL_STATUS);// 正常状态
                push.setContent(pushTarget.toString());
                logger.info("商铺id：" + userId);
                pushService.pushInfoToShop2(push);
            }
            catch (Exception e)
            {
                logger.error("推送消息到收银机失败" + "，商铺id：" + userId);
            }
        }

        // 成功时候发送短信 TODO 后面增加充值 发送短信
        // if(CodeConst.CODE_STATUS_SUCCSSS==transaction3rdDto.getStatus()){
        // //发送短信
        // //sendSmsService.sendSms(mobile, content);
        // }

    }

	public int checkOrderIsPayByCash(String orderId, Long uccId,Integer orderPayType)
			throws Exception {
		return this.payDao.checkOrderIsPayByCash(orderId,uccId,orderPayType);
	}

	public List<Map> queryOrderGroupById(String orderId) throws Exception {
		return this.payDao.queryOrderGroupById(orderId);
	}

	public Double getSumOrderGroupAmount(String orderId) throws Exception {
		return this.payDao.getSumOrderGroupAmount(orderId);
	}

	public Long getShopIdByOrderId(String orderId) throws Exception {
		return this.payDao.getShopIdByOrderId(orderId);
	}

	@Override
    public void updateTransactionByid(Long transactionId,String rdTransactionId) throws Exception{
		// 更新账单状态为“充值失败”bill_status=32，充值标示为“已完成”bill_status_flag=0
		cancelRecharge(transactionId);
		//更新支付流水状态
		TransactionDto transaction = new TransactionDto();
		transaction.setStatus(CommonConst.TRANSACTION_STATUS_FAIL);
		transaction.setTransactionId(transactionId);
		transactionDao.updateTransaction(transaction);
	}

	@Override
	public PageModel getServiceWithdrawList(Integer withdrawStatus,
			Long userId, String mobile, int pageNo, int pageSize)
			throws Exception {
		PageModel pageModel = new PageModel();
		int num = this.withdrawDao.getServiceWithdrawCount(withdrawStatus, userId, mobile);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (num > 0) {
			list = this.withdrawDao.getServiceWithdrawList(withdrawStatus, userId, mobile, pageNo, pageSize);
			pageModel.setToPage(pageNo);
			pageModel.setPageSize(pageSize);
			pageModel.setTotalItem(num);
			pageModel.setList(list);
		}
		return pageModel;
	}

	/**
	 * 管理审核商铺提现：
	 * 	同意：商铺账户表扣除冻结金额，原来的总额也需要扣除提现金额，并新增一条账单，最后更新提现状态
	 *  拒绝：
	 *  	1.商铺账户扣除冻结金额，并新增一条账单
	 *  	2.提现记录里面冻结线上收入多少就退还商铺账户线上收入多少，并记录账单
	 *  	3.提现记录里面如果有冻结平台奖励就退还商铺账户平台奖励多少，并记录账单
	 *  	4.更新提现状态
	 *  modify by huangrui 2015.12.1
	 *  	
	 */
	@Override
	public ShopWithDrawDto adminWithdraw(Map<String, Object> paramMap) throws Exception{
		
		// 查询提现记录
		ShopWithDrawDto withdrawDto = shopWithDrawDao.getShopWithDrawById(Long.valueOf((String)paramMap.get("withdrawId")));
		CommonValidUtil.validObjectNull(withdrawDto,  CodeConst.CODE_PARAMETER_NOT_EXIST, "提现记录不存在,withdrawId="+paramMap.get("withdrawId"));
		
		//防止重复调用，状态不为0且相等了不再做处理
		Integer statusFromPhp = Integer.parseInt(paramMap.get("withdrawStatus").toString());
		Integer statusFromDB = withdrawDto.getWithdrawStatus();
		if(statusFromDB == null)
			statusFromDB = 0; //初始化状态为为审核
		
		if(statusFromDB.intValue() != 0 && statusFromDB.intValue() == statusFromPhp.intValue()){
			logger.info("后台已重复提交操作，不再处理");
			throw new ServiceException(CodeConst.CODE_RESUBMIT_ERROR,"重复提交操作");
		}
		
		//3（支付失败），4（已成功提现）为终极状态，不可再改
		if(statusFromDB == 3 || statusFromDB == 4){
			logger.info("提现状态已为终极状态，不可更改，status="+statusFromDB);
			throw new ServiceException(CodeConst.CODE_FINAL_STATUS_ERROR,"数据不可再改");
		}
		
		// 查询商铺账户信息
		ShopAccountDto shopAccount = shopAccountDao.getShopAccount(withdrawDto.getShopId());
		
		// 1（审核不通过），2（审核通过，提现中），3（支付失败），4（已成功提现）
		if(statusFromPhp == 1){
			refuseWithdraw(withdrawDto, shopAccount);
		} else if(statusFromPhp == 3){
			//更新账户，减去冻结金 
			refuseWithdraw(withdrawDto, shopAccount);
			
		}else if (statusFromPhp == 4) {
			Double withdrawCommission  = withdrawDto.getWithdrawCommission();
			Double downMoney = NumberUtil.add(withdrawDto.getAmount(), withdrawCommission);
			// 商铺账户表扣除冻结金额，并新增一条账单 
			shopAccountDao.updateShopAccount(withdrawDto.getShopId(), -downMoney, null, null, 
					null, null, -downMoney,null,null,null, null);
			//新增账单
			shopBillDao.insertShopBill(BillUtil.buildShopFreezeBillForWithdraw(withdrawDto, shopAccount, false));
			//
			if(withdrawCommission>0){
				logger.info("记录手续费冻结金额流水..");
				insertShopBill(withdrawDto.getShopId(), CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION, 
						CommonConst.BILL_DIRECTION_DOWN, "提现手续费冻结金额解冻"+withdrawCommission+"元", CommonConst.SHOP_BILL_STATUS_OVER, 
						CommonConst.SHOP_ACCOUNT_TYPE_FREEZE,shopAccount.getAmount(), NumberUtil.sub(shopAccount.getFreezeAmount(), NumberUtil.add(withdrawCommission, withdrawDto.getAmount())), -withdrawCommission, 
						"退回提现手续费"+withdrawCommission+"元",withdrawDto.getWithDrawId(),null);
			}
			
			//设置提现成功时间如果没传的话
			if(paramMap.get("withdrawTime") == null)
				paramMap.put("withdrawTime", DateUtils.format(new Date(), null));
			
		}
		
		//更新提现记录
		withdrawDto.setWithdrawStatus(statusFromPhp);
		if(paramMap.get("handleUserId") != null)
			withdrawDto.setHandleUserId(Integer.valueOf(paramMap.get("handleUserId").toString()));
		if(paramMap.get("handleTime") != null)
			withdrawDto.setHandleTime(DateUtils.parse(paramMap.get("handleTime").toString()));
		if(paramMap.get("handleMark") != null)
			withdrawDto.setHandleMark(paramMap.get("handleMark").toString());
		if(paramMap.get("withdrawTime") != null)
			withdrawDto.setWithDrawTime(DateUtils.parse(paramMap.get("withdrawTime").toString()));
		
		shopWithDrawDao.updateShopWithdraw(withdrawDto);
		
		return withdrawDto;
		
	}

	/**
	 * 管理员拒绝商铺提现
	 * @param withdrawDto
	 * @param shopAccount
	 * @throws Exception
	 */
	private void refuseWithdraw(ShopWithDrawDto withdrawDto,ShopAccountDto shopAccount) throws Exception {
		//  1.商铺账户扣除冻结金额，并新增一条账单
		shopAccountDao.updateShopAccount(withdrawDto.getShopId(), null, null, null, null, null, -withdrawDto.getAmount(),null,null,null, null);
		shopBillDao.insertShopBill(BillUtil.buildShopFreezeBillForWithdraw(withdrawDto, shopAccount,false));
		
		// 2.提现记录里面冻结线上收入多少就退还商铺账户线上收入多少，并记录账单
		shopAccountDao.updateShopAccount(withdrawDto.getShopId(), null, withdrawDto.getOnlineIncomeFreeze(), null, null, null, null,null,null,null, null);
		if(withdrawDto.getOnlineIncomeFreeze()>0){
			shopBillDao.insertShopBill(BillUtil.buildShopOnlineIncomeAmountBillForWithdraw(withdrawDto, shopAccount, withdrawDto.getOnlineIncomeFreeze(), true));
		}
		
		// 3.提现记录里面如果有冻结平台奖励就退还商铺账户平台奖励多少，并记录账单
		if(withdrawDto.getRewardFreeze() > 0) {
			shopAccountDao.updateShopAccount(withdrawDto.getShopId(), null, null, withdrawDto.getRewardFreeze(), null, null, null,null,null,null, null);
			shopBillDao.insertShopBill(BillUtil.buildShopRewardAmountBillForWithdraw(withdrawDto, shopAccount, withdrawDto.getRewardFreeze(), true));
		}
		Double withdrawCommission = withdrawDto.getWithdrawCommission()==null ? 0D :withdrawDto.getWithdrawCommission();
		if(withdrawCommission>0){
			refuseWithdrawByWithdrawCommission(withdrawDto);
		}
	}
	private void refuseWithdrawByWithdrawCommission(ShopWithDrawDto withdrawDto) throws Exception {
		//查询账单手续费
		Map<String, Object> parms = new HashMap<String, Object>();
		Long shopId = withdrawDto.getShopId();
		Long withDrawId = withdrawDto.getWithDrawId();
		Double withdrawCommission = withdrawDto.getWithdrawCommission()==null ? 0D :withdrawDto.getWithdrawCommission() ;
		parms.put("shopId", shopId);
		parms.put("transactionId", withdrawDto.getWithDrawId());
		parms.put("billType", CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION);
		parms.put("billDirection", CommonConst.BILL_DIRECTION_DOWN);
		parms.put("n", 0);//分页参数
		parms.put("m", 10);
		
		List<Map<String, Object>> shopBillList = shopBillDao.getShopBill(parms);
		
		if(CollectionUtils.isNotEmpty(shopBillList)){
			
			Double onlineIncomeAddMoney = 0D;//线上收入账户增加金额
			Double rewardAddMongy = 0D;//奖励账户增加金额
			Double onlineIncomeAfterAmount = 0D;//处理后线上收入账户
			Double rewardAmountAfterAmount  = 0D;//处理后奖励账户
			ShopAccountDto shopAccount = shopAccountDao.getShopAccount(shopId);
			
			Double accountAmount = shopAccount.getAmount();
			for (Map<String, Object> shopBill : shopBillList) {
				Integer accountType = (Integer) shopBill.get("accountType");
				BigDecimal money = (BigDecimal) shopBill.get("money");
				Double addMoney = Math.abs(money.doubleValue());
				//线上收入账户
				if(accountType.intValue()==CommonConst.SHOP_ACCOUNT_TYPE_INCOME){
					Double onlineIncomeAmount = shopAccount.getOnlineIncomeAmount();
					onlineIncomeAfterAmount = NumberUtil.add(onlineIncomeAmount, addMoney);
					onlineIncomeAddMoney = addMoney;//线上收入增加
					
					insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_RETURN_WITHDRAW_COMMISSION, CommonConst.BILL_DIRECTION_ADD,
							"退回提现手续费转入线上收入"+addMoney+"元", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_INCOME,
							accountAmount, onlineIncomeAfterAmount, addMoney, "提现手续费"+addMoney+"元"
							,withDrawId,null);
				}
				//奖励账户
				if(accountType.intValue()==CommonConst.SHOP_ACCOUNT_TYPE_REWARD){
					Double rewardAmount = shopAccount.getRewardAmount();
					rewardAmountAfterAmount = NumberUtil.add(rewardAmount, addMoney);
					rewardAddMongy = addMoney;//奖励收入增加

					insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_RETURN_WITHDRAW_COMMISSION, CommonConst.BILL_DIRECTION_ADD,
							"退回提现手续费转入奖励收入"+addMoney+"元", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_REWARD,
							accountAmount, rewardAmountAfterAmount, addMoney, "提现手续费"+addMoney+"元",withDrawId,null);
				}

			}
			//扣出冻结资金
			insertShopBill(withdrawDto.getShopId(), CommonConst.SHOP_BILL_TYPE_WITHDRAW_COMMISSION, 
					CommonConst.BILL_DIRECTION_DOWN, "提现手续费冻结金额解冻"+withdrawCommission+"元", CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.SHOP_ACCOUNT_TYPE_FREEZE,shopAccount.getAmount(), 
					NumberUtil.sub(shopAccount.getFreezeAmount(), withdrawCommission), 
					-withdrawCommission, "提现手续费"+withdrawCommission+"元",withdrawDto.getWithDrawId(),null);
			
			//更新账户信息
			shopAccountDao.updateShopAccount(shopId, null, onlineIncomeAddMoney, rewardAddMongy, null, null, -withdrawCommission,null,null,null, null);
			
		}
	}
		
	
	/**
	 * 管理员审核用户提现：
	 * 	同意：用户账户减少冻结资金，原来的总额也需要扣除提现金额，并新增账单
	 *  拒绝：
	 *  	1.用户账户减少冻结资金，并新增账单
	 *  	2.用户账户退回平台奖励，并新增账单
	 *  最后更新提现状态
	 *  
	 *  modify by huangrui 2015.12.1
	 */
	@Override
	public void adminWithdrawForUser(Map<String, Object> paramMap) throws Exception{
		
		// 查询提现记录
		WithdrawDto withdrawDto = withdrawDao.getWithdrawById(Long.valueOf((String)paramMap.get("withdrawId")));
		CommonValidUtil.validObjectNull(withdrawDto,  CodeConst.CODE_PARAMETER_NOT_EXIST, "提现记录不存在,withdrawId="+paramMap.get("withdrawId"));
		
		//防止重复调用，状态不为0且相等了不再做处理
		Integer statusFromPhp = Integer.parseInt(paramMap.get("withdrawStatus").toString());
		Integer statusFromDB = withdrawDto.getWithdrawStatus();
		if(statusFromDB == null)
			statusFromDB = 0; //初始化状态为为审核
		
		if(statusFromDB.intValue() != 0 && statusFromDB.intValue() == statusFromPhp.intValue()){
			logger.info("后台已重复提交操作，不再处理");
			throw new ServiceException(CodeConst.CODE_RESUBMIT_ERROR,"重复提交操作");
		}
		
		//用户账单状态
		Integer billStatus = WithdrawEnum.WITHDRAW_SUCCESS.getValue();
		
		//3（支付失败），4（已成功提现）为终极状态，不可再改
		if(statusFromDB == 3 || statusFromDB == 4){
			logger.info("提现状态已为终极状态，不可更改，status="+statusFromDB);
			throw new ServiceException(CodeConst.CODE_FINAL_STATUS_ERROR,"数据不可再改");
		}
		
		// 查询商铺账户信息
		UserAccountDto userAccountDto = userAccountDao.getAccountMoney(withdrawDto.getUserId());
		
		// 1（审核不通过），2（审核通过，提现中），3（支付失败），4（已成功提现）
		if(statusFromPhp == 1){
			refuseUserWithdraw(withdrawDto, userAccountDto, billStatus);
		}
		else if(statusFromPhp == 3){
			//更新账户，减去冻结金 
			refuseUserWithdraw(withdrawDto, userAccountDto, billStatus);
			
		}else if (statusFromPhp == 4) {
			billStatus = WithdrawEnum.WITHDRAW_SUCCESS.getValue();
			// 用户账户减少冻结资金，并新增账单 
			userAccountDao.updateUserAccount(withdrawDto.getUserId(), -withdrawDto.getAmount(), null, null, null, -withdrawDto.getAmount(),
					null,null,null,null,null,null,null,null,null);
			userBillDao.insertUserBill(BillUtil.buildUserFreezeBillForWithdraw(withdrawDto, userAccountDto, null, billStatus, false));
			
			//设置提现成功时间如果没传的话
			if(paramMap.get("withdrawTime") == null)
				paramMap.put("withdrawTime", DateUtils.format(new Date(), null));
			
			
		}
		
		//更新提现记录
		withdrawDto.setWithdrawStatus(statusFromPhp);
		if(paramMap.get("handleUserId") != null)
			withdrawDto.setHandleUserId(Long.valueOf(paramMap.get("handleUserId").toString()));
		if(paramMap.get("handleTime") != null)
			withdrawDto.setHandleTime(DateUtils.parse(paramMap.get("handleTime").toString()));
		if(paramMap.get("handleMark") != null)
			withdrawDto.setHandleMark(paramMap.get("handleMark").toString());
		if(paramMap.get("withdrawTime") != null)
			withdrawDto.setWithdrawTime(DateUtils.parse(paramMap.get("withdrawTime").toString()));
		
		withdrawDao.updateWithdraw(withdrawDto);
	}

	private void refuseUserWithdraw(WithdrawDto withdrawDto, UserAccountDto userAccountDto, int billStatus) throws Exception {
		//1.用户账户减少冻结资金，并新增账单
		userAccountDao.updateUserAccount(withdrawDto.getUserId(), null, null, null, null, -withdrawDto.getAmount(),
				null,null,null,null,null,null,null,null,null);
		userBillDao.insertUserBill(BillUtil.buildUserFreezeBillForWithdraw(withdrawDto, userAccountDto, null,billStatus, false));
		//2.用户账户退回平台奖励，并新增账单
		userAccountDao.updateUserAccount(withdrawDto.getUserId(), null, withdrawDto.getAmount(), 
				null, null, null,null,null,null,null,null,null,null,null,null);
		userBillDao.insertUserBill(BillUtil.buildUserRewardBillForWithdraw(withdrawDto, userAccountDto, null, true));
	}

	@Override
	public UserAccountDto getAccountMoney(Long userId) throws Exception {
		return userAccountDao.getAccountMoney(userId);
	}

	@Override
	public PageModel getShopWithdrawList(Map<String, Object> map)
			throws Exception {
		PageModel pageModel = new PageModel();
		Integer count = shopWithDrawDao.getShopWithdrawCount(map);
		pageModel.setTotalItem(count);
		if(count!=0){
			 List<Map<String, Object>> list = shopWithDrawDao.getShopWithdrawList(map);
			 pageModel.setList(list);
		}
		return pageModel;
	}

	@Override
	public PageModel getShopRechargeList(Map<String, Object> map)
			throws Exception {
		PageModel pageModel = new PageModel();
		Integer count = iTransaction3rdDao.getShopRechargeCount(map);
		pageModel.setTotalItem(count);
		if(count!=0){
			 List<Map<String, Object>> list = iTransaction3rdDao.getShopRechargeList(map);
			 pageModel.setList(list);
		}
		return pageModel;
	}

	@Override
	public List<Map> getOrderPayDetail(String orderId) throws Exception{
		return payDao.getOrderPayDetail(orderId) ;
	}

	@Override
	public void cancelRecharge(Long transactionId) {
		
		// 更新账单状态为“充值失败”bill_status=32，充值标示为“已完成”bill_status_flag=0
		UserChargeDto userBillDto = new UserChargeDto();
		userBillDto.setBillStatus(RechargeEnum.RECHARGE_FAILURE.getValue());
		userBillDto.setBillStatusFlag(0);
		userBillDto.setTransactionId(transactionId);
		userBillDao.updateStatusByTransactionId(userBillDto);
		
		
	}

	public synchronized boolean payBySms(OrderDto order, Double needPayAmount, UserAccountDto accountDto, Double couponBalance, UserDto user) throws Exception{
		boolean isSettleFlag = false;
		Long userId = user.getUserId();
		if(userId.equals(order.getUserId()))
		{
		    logger.info("该订单属于代付情况，待付人手机号码：" + user.getMobile());
		}
		
		String orderId = order.getOrderId();
		Long shopId = order.getShopId();
		Integer orderPayType = 0; //单订单
		//账户总金额
        Double accountAmount = accountDto.getAmount();
        //消费金余额
        Double couponAmount = accountDto.getCouponAmount();
        //平台奖励余额
        Double rewardAmount = accountDto.getRewardAmount();
        //传奇宝账户可用于支付的余额
        Double accountUsableBalance = NumberUtil.add(couponAmount, rewardAmount);
        //在该店铺可用红包金额
        Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
        //可用总余额
        Double totleUsableBalance = accountUsableBalance + couponBalance + redPacketAmount;
		
		if (totleUsableBalance < needPayAmount) {
			//如果可用金额不够支付订单需要支付金额直接报错
			throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,CodeConst.MSG_ACCOUNT_NOT_BALANCE);// 账户余额不足
		}
		String nowTime = DateUtils.getCurDate();
		//使用红包支付金额
		Double useAmount = payByRedPacket(needPayAmount, order, userId, true,null);
		needPayAmount = NumberUtil.sub(needPayAmount, useAmount);
		if(needPayAmount == 0) {
		    //红包足额支付
		    dealOrderInfo(order);
            return true;
		}
		if(couponBalance >= needPayAmount) {
		    //消费卡足额支付
		    logger.info("消费卡可用余额足够,只使用消费卡余额");
		    payCashCoupon(needPayAmount, orderId, user, shopId, orderPayType, nowTime,order.getOrderTitle());
		    //2016.1.27修复 1、更新order状态 2、释放资源3、记录订单日志
            dealOrderInfo(order);
            isSettleFlag = true;
            
		} else {
			if(couponBalance > 0){//消费卡有钱先用完
			    logger.info("消费卡可用余额不够,需要使用传奇宝余额");
			    payCashCoupon(couponBalance, orderId, user, shopId, orderPayType, nowTime,order.getOrderTitle());
			}
		    Double residualAmount = NumberUtil.sub(needPayAmount, couponBalance); //还需要支付
		    if(residualAmount > 0)
		    {
		        //记录交易流水
	            TransactionDto accounTransaction = getTransactionDto(userId, orderId, residualAmount, orderPayType, nowTime); //传奇宝交易流水
	            transactionDao.addTransaction(accounTransaction);
	            Double changeRewardAmount = 0D; 
	            Double changeCouponAmount = 0D; 
	            if (couponAmount >= residualAmount){
	                //消费金足够支付
	                changeCouponAmount = residualAmount;
	            }else{
	                //使用的消费金
	                changeCouponAmount = couponAmount;
	                //使用消费金后还需支付
	                changeRewardAmount = NumberUtil.sub(residualAmount, changeCouponAmount);
	            }
	            //修改用户账户余额
	            userAccountDao.updateUserAccount(userId, -residualAmount, -changeRewardAmount, null, -changeCouponAmount,
	            		null,null,null,null,null,null,null,null,null,null);
	            //订单支付记录
	            PayDto accountPay = getPayDto(orderId, shopId, userId, orderPayType, CommonConst.PAY_TYPE_CSB, accounTransaction.getTransactionId(),residualAmount, nowTime,null);//传奇宝支付记录
	            //用户账单
	            UserBillDto userBillDto = buildUserBill(accountPay, order, accountDto, "消费", CommonConst.BILL_DIRECTION_DOWN);
	            if(changeCouponAmount > 0){
	                //记消费金账单
	                userBillDto.setAccountAmount(accountAmount);
	                userBillDto.setMoney(-changeCouponAmount);
	                userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
	                userBillDto.setAccountAfterAmount(NumberUtil.sub(accountAmount, changeCouponAmount)); //使用后余额
	                userBillDto.setTransactionId(accounTransaction.getTransactionId());
	                userBillDto.setIsShow(CommonConst.USER_BILL_IS_SHOW);
	                userBillDao.insertUserBill(userBillDto);
	                //消费金支付记录
	                accountPay.setPayType(CommonConst.PAY_TYPE_CONSUM);
	                accountPay.setPayAmount(changeCouponAmount);
	                payDao.addOrderPay(accountPay);
	            }
	            if(changeRewardAmount > 0){
	                //记平台奖励账单
	                if(changeCouponAmount > 0){
	                    userBillDto.setAccountAmount(userBillDto.getAccountAfterAmount());
	                     //使用后余额
	                    userBillDto.setAccountAfterAmount(NumberUtil.sub(userBillDto.getAccountAfterAmount(), changeRewardAmount)); 
	                }else{
	                    userBillDto.setAccountAmount(accountAmount);
	                    //使用后余额
	                    userBillDto.setAccountAfterAmount(NumberUtil.sub(accountAmount, changeRewardAmount)); 
	                }
	                userBillDto.setMoney(-changeRewardAmount);
	                userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_REWARD);
	                userBillDao.insertUserBill(userBillDto);
	                //平台奖励支付记录
	                accountPay.setPayType(CommonConst.PAY_TYPE_REWARD);
	                accountPay.setPayAmount(changeRewardAmount);
	                accountPay.setOrderPayId(null);
	                payDao.addOrderPay(accountPay);
	            }
	            //添加平台账单
	            insertPlatformBill(user, residualAmount, orderId,CommonConst.PLT_BILL_MNY_SOURCE_CQB,CommonConst.PLATFORM_BILL_TYPE_PAY);
	            isSettleFlag = true;
	            dealOrderInfo(order);
		    }
		}
		return isSettleFlag;
	}

	/**
	 * 更新订单状态
	 * 是否订单资源
	 * 记录订单操作业务日志
	 * @param order
	 * @author  shengzhipeng
	 * @date  2016年3月15日
	 */
    private void dealOrderInfo(OrderDto order) throws Exception {
        order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
        order.setOrderStatus(CommonConst.ORDER_STS_YJZ);//更改订单状态为已结账
        order.setLastUpdateTime(new Date());
        orderDao.updateOrder(order);
        collectDao.updateShopResourceStatus(order.getOrderId(),CommonConst.RESOURCE_STATUS_NOT_IN_USE); //释放资源
        
        if(CommonConst.ORDER_STS_YJZ == order.getOrderStatus()){
			//修改库存
			storageService.insertShopStorageByOrderId(order.getOrderId(),order.getShopId());
        }
        saveOrderLog(order,"短信支付");
    }
	
	public Double payByRedPacket(Double payAmount, OrderDto order, Long userId, boolean isAllow,Integer clientSystem) throws Exception {
	    Double useAmount = 0D;
	    if(payAmount <= 0) {
	        return useAmount;
	    }
	    Long shopId = order.getShopId();
	    List<RedPacket> redPackets = packetDao.getRedPacketBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
	    if (CollectionUtils.isNotEmpty(redPackets)) {
	        //可用红包金额
	        Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
	        if (redPacketAmount < payAmount) {
	            //可用金额如果小于需要支付的金额
	            if(!isAllow) {
	                // 账户余额不足
	                throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,CodeConst.MSG_ACCOUNT_NOT_BALANCE);
	            }
	            //直接扣减
	            for (RedPacket redPacket : redPackets) {
	                useRedPacket(order, redPacket, redPacket.getAmount(), userId,clientSystem);
                }
	            useAmount = redPacketAmount;  
	        } else {
	            //可用红包金额大于需要支付的金额
	            Double residualAmount = payAmount;
	            for (RedPacket redPacket : redPackets) {
	                Double amount = redPacket.getAmount(); // 单张红包余额
	                Long redPacketId = redPacket.getRedPacketId();
	                if (amount >= residualAmount) {
	                    //一张够用
	                    logger.info("一张红包足够支付，红包ID:" + redPacketId);
	                    useRedPacket(order, redPacket, residualAmount, userId,clientSystem);
	                    break;
	                } else {
	                    //一张不够用
	                    logger.info("一张红包不够支付，需要多次支付，每次支付红包ID:" + redPacketId);
	                    useRedPacket(order, redPacket, amount, userId,clientSystem);
	                    residualAmount = NumberUtil.sub(residualAmount, amount);
	                }
	            }
	            useAmount = payAmount;
	        }
	    }
        return useAmount;
	}

	/**
	 * 1、红包使用后需要更新红包状态和金额
	 * 2、保存用户使用红包账单
	 * 3、保存平台收到红包账单
	 * @param order 订单对象
	 * @param redPacket 红包
	 * @param amount 使用金额
	 * @author  shengzhipeng
	 * @date  2016年3月15日
	 */
	@Override
    public void useRedPacket(OrderDto order, RedPacket redPacket, Double amount, Long userId,Integer clientSystem) throws Exception {
        PayDto userRedPacketPay = getPayDto(order.getOrderId(), order.getShopId(), userId, CommonConst.PAY_TYPE_SINGLE, 
                CommonConst.PAY_TYPE_RED_PACKET, redPacket.getRedPacketId(), amount, DateUtils.format(new Date(), null),clientSystem);
		if(userRedPacketPay.getPayAmount()>0){
			payDao.addOrderPay(userRedPacketPay);
		}
        
        Map<String, Object> param = new HashMap<String, Object>();
        Long redPacketId = redPacket.getRedPacketId();
        param.put("redPacketId", redPacketId);
        if (NumberUtil.sub(redPacket.getAmount(), amount) > 0) {
        	param.put("status", RedPacketStatusEnum.USEABLE.getValue());
        }else {
        	 param.put("status", RedPacketStatusEnum.USED.getValue());
        }
        param.put("payAmount", -amount);
        packetDao.updateRedPacketFlag(param);
        UserBillDto userBillDto = BillUtil.buildUserBillForRedPacket(order, redPacket, amount, true);
        userBillDao.insertUserBill(userBillDto);
        PlatformBillDto billDto = BillUtil.buildPlatformBill(order, amount, CommonConst.PLT_BILL_MNY_SOURCE_HB, CommonConst.PLATFORM_BILL_TYPE_R_RED_PACKET, "红包支付", true);
        billDto.setBillDesc("用户使用红包支付");
        billDto.setRedPacketId(redPacketId);
        platformBillDao.insertPlatformBill(billDto);
    }
	
	
    private void insertPlatformBill(UserDto user, Double settlePrice, String orderId, Integer moneySource,Integer platformBillType) throws Exception
    {
        PlatformBillDto platformBillDto = new PlatformBillDto();
        platformBillDto.setBillType("会员消费");
        platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBillDto.setOrderId(orderId);
        platformBillDto.setCreateTime(new Date());
        platformBillDto.setConsumerUserId(user.getUserId());
        platformBillDto.setConsumerMobile(user.getMobile());
        platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_PAY);
        platformBillDto.setMoney(settlePrice);
        platformBillDto.setMoneySource(moneySource);
        platformBillDto.setBillDesc("会员消费使用线上支付金额");
        platformBillDto.setPlatformBillType(platformBillType);
        platformBillDao.insertPlatformBill(platformBillDto);
    }

    private void payCashCoupon(Double needPayAmount, String orderId, UserDto user, Long shopId, Integer orderPayType,
            String nowTime,String billTitle) throws Exception
    {
        Long userId = user.getUserId();
        Double residualAmount = needPayAmount;
        List<UserCashCouponDto> userCashCoupons = userCashCouponDao.getUserCashCouponByUserId(userId);
        for (UserCashCouponDto userCashCouponDto : userCashCoupons) {
            Double price = userCashCouponDto.getPrice(); // 消费卡面额
            Double usedPrice = userCashCouponDto.getUsedPrice(); //已使用金额
            Double usableBalance =NumberUtil.sub(price, usedPrice); //单张消费卡余额
            Long uccId = userCashCouponDto.getUccId();
            if (usableBalance >= residualAmount) {
                //一张够用
                logger.info("一张消费卡足够支付，代金券ID:" + uccId);
                useCashCoupon(orderId, shopId, userId, orderPayType, nowTime,
                        residualAmount, usedPrice, usableBalance, uccId, billTitle);
                break;
            } else {
                //一张不够用
                logger.info("一张消费卡不够支付，需要多次支付，每次支付消费卡ID:" + uccId);
                useCashCoupon(orderId, shopId, userId, orderPayType, nowTime,
                        usableBalance, usedPrice, usableBalance, uccId, billTitle);
                residualAmount = NumberUtil.sub(residualAmount, usableBalance);
            }
        }
        insertPlatformBill(user, needPayAmount, orderId,CommonConst.PLT_BILL_MNY_SOURCE_XFK,CommonConst.PLATFORM_BILL_TYPE_PAY);
    }


	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.service.pay.PayServiceImpl.useCashCoipon
	 * @Description:消费代金券：1，生成xbill记录；2，生成orderPay记录；3，修改代金券数值
	 *
	 * @param orderId 订单编号
	 * @param userId 用户id
	 * @param shopId 店铺id
	 * @param orderPayType 订单支付类型
	 * @param nowTime 操作时间
	 * @param residualAmount 剩余支付金额
	 * @param usedPrice 支付前已使用的代金券金额
	 * @param usableBalance 单张代金券余额
	 * @param uccId 代金券Id
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月24日 下午2:09:12
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月24日    shengzhipeng       v1.0.0         create
	 */
	private void useCashCoupon(String orderId, Long shopId, Long userId,
			Integer orderPayType, String nowTime, Double residualAmount,
			Double usedPrice, Double usableBalance, Long uccId,String billTitle)
			throws Exception {
		PayDto userCashPay = getPayDto(orderId, shopId, userId, orderPayType, CommonConst.PAY_TYPE_CASH_COUPON, uccId, residualAmount, nowTime,null);
		UserXBillDto userXBill = getUserXBillDto(userId, uccId, userCashPay, residualAmount, usableBalance,billTitle);
		if(userCashPay.getPayAmount()>0){
			payDao.addOrderPay(userCashPay);
		}		userXBillDao.insertUserXBillDao(userXBill);
		//支付后代金券使用金额=支付前使用金额+当前支付使用金额
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("usedPrice", (usedPrice+residualAmount));
		param.put("payId", uccId);
		userCashCouponDao.updateUserCashCoupon(param);
	}
	
	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.service.pay.PayServiceImpl.getUserXBillDto
	 * @Description:
	 *
	 * @param userId 用户id
	 * @param uccId 代金券主键id
	 * @param payDto 支付记录
	 * @param useMoney 使用的金额
	 * @param accountAmount 使用前金额
	 * @return
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年9月23日 下午3:39:08
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月23日    shengzhipeng       v1.0.0         create
	 */
	private UserXBillDto getUserXBillDto(Long userId, Long uccId, PayDto payDto, Double useMoney, Double accountAmount, String billTitle) {
	
		//生成代金券支付账单记录
		UserXBillDto userXBill = new UserXBillDto();
		userXBill.setUserId(userId);
		userXBill.setUccId(uccId);
		userXBill.setOrderPayType(payDto.getOrderPayType());
		userXBill.setOrderId(payDto.getOrderId());
		userXBill.setMoney(-useMoney); //使用金额
		userXBill.setCreateTime(new Date());
		userXBill.setBillType(CommonConst.USER_CASHCOUPON_USE);
		billTitle = StringUtils.isEmpty(billTitle) ? "消费卡消费" : billTitle;
		userXBill.setBillTitle(billTitle);
		userXBill.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());//账单状态为已完成
		userXBill.setBillDesc("订单支付");
		userXBill.setAccountAmount(accountAmount); //处理前金额
		return userXBill;
	}
	
	private PayDto getPayDto(String orderId, Long shopId, Long userId, Integer orderPayType, Integer payType,Long payId,
			Double payAmount, String nowTime,Integer clientSystem) {
		// 生成支付信息
		PayDto payDto=new PayDto();
		payDto.setPayId(payId);
		payDto.setOrderId(orderId);
		payDto.setPayType(payType);
		payDto.setPayAmount(payAmount);
		payDto.setOrderPayType(orderPayType);
		payDto.setUserId(userId);
		payDto.setOrderPayTime(nowTime);
		payDto.setLastUpdateTime(nowTime);
		payDto.setPayeeType(0);
		payDto.setShopId(shopId);
		payDto.setAutoSettleFlag(CommonConst.AUTO_SETTLE_FLAG_TRUE);
		payDto.setPayStatus(CommonConst.TRANSACTION_STATUS_FINISH);
		payDto.setClientSystem(clientSystem);
		//TODO
//		payDto.setPayChannel(payChannel);
		return payDto;
	}
	
	private TransactionDto getTransactionDto(Long userId, String orderId, Double payAmount, Integer orderPayType, String nowTime ) throws Exception {
		TransactionDto transactionDto=new TransactionDto();
		transactionDto.setUserId(userId);
		transactionDto.setOrderId(orderId);
		transactionDto.setPayAmount(payAmount);
		transactionDto.setTransactionTime(nowTime);
		transactionDto.setStatus(1); // 支付完成
		transactionDto.setUserPayChannelId(new Long(1));
		transactionDto.setOrderPayType(orderPayType);
		transactionDto.setLastUpdateTime(nowTime);
		transactionDto.setTransactionType(0); //消费
		return transactionDto;
	}

	@Override
	public BankCardDto getBankCardByCard(String cardNumber, String userId,Integer accountType) throws Exception {
		return bankCardDao.getBankCardByCard(cardNumber, userId,accountType);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.service.pay.IPayServcie#addBy3rd(com.idcq.appserver.dto.pay.Transaction3rdDto)
     */
    @Override
    public Long addPayBy3rd(Transaction3rdDto transaction3rdDto) throws Exception
    {
        // 生成待支付信息
        iTransaction3rdDao.payBy3rd(transaction3rdDto);
        Long transactionId = transaction3rdDto.getTransactionId();
        return transactionId;
    }
	
	@Override
	public int getShopWithdrawListCount(Map<String, Object> mapParameter)
			throws Exception {
		return withdrawDao.getShopWithdrawListCount(mapParameter);
	}

	@Override
	public List<Map<String, Object>> getShopWithdrawList(
			Map<String, Object> mapParameter, Integer pageNo, Integer pageSize)
			throws Exception {
		return withdrawDao.getShopWithdrawList(mapParameter, pageNo, pageSize);
	}

    @Override
    public Transaction3rdDto ge3rdPayById(Transaction3rdDto transaction3rdDto)
            throws Exception {
        return iTransaction3rdDao.ge3rdPayById(transaction3rdDto);
    }

    public Map<String, Object> getPayResult(Map<String, Object> map) throws Exception
    {
        return payDao.getPayResult(map);
    }

	@Override
	public Map<String, String> findBankNameByCardNo(String cardNo) {
		String cardForSixNo=cardNo.substring(0, 6);
		String name = BankInfo.getNameOfBank(cardForSixNo, 0);//获取银行卡的信息
		Map<String, String> map = new HashMap<String, String>();
		map.put("bankName", name);
		return map;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.pay.IPayServcie#updateTransactionAfterRdPaySuccess(com.idcq.appserver.dto.pay.TransactionDto)
	 */
	@Override
	public void updateTransactionAfterRdPaySuccess(TransactionDto transactionDto)
			throws Exception {
		
		transactionDao.updateTransactionAfterRdPaySuccess(transactionDto);
	}
	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.shop.IShopServcie#headquartersWithdraw(com.idcq.appserver.dto.pay.WithdrawRequestModel)
	 */
	@Override
	public Map<String, Object> headquartersWithdraw(WithdrawRequestModel requestModel)
			throws Exception {
		
		/**
    	 * 1、校验店铺信息
    	 * 2、扣出分店收入，转入总店（记录分店一条支付记录、总店一条入账记录）
    	 * 3、生成一条总店记录
    	 */
		
		ShopAccountDto headquartersShop = validateHeadquartersShop(requestModel.getShopId(),null);
		
		//转入总店金额
		Double totalMoney = 0D;
		List<WithdrawListDto> withdrawList  = requestModel.getWithdrawList();
		//线上收入
		Double onlineIncomeTotalAmount = 0D;
		//奖励收入
		Double rewardTotalAmount = 0D;
		for (WithdrawListDto withdraw : withdrawList) {
			
			//校验分店账号
			ShopAccountDto account = validateShopAccount(withdraw.getShopId(), withdraw.getMoney(),requestModel.getShopId());
			
			//更新分店账户，记录账户流水
			Map<String, Object> moneyMap = dealBranchShopAccount(account, withdraw);
			
			if(moneyMap.get("onlineIncomeAmount")!=null){
				onlineIncomeTotalAmount = NumberUtil.add(onlineIncomeTotalAmount, (Double) moneyMap.get("onlineIncomeAmount"));
			}
			if(moneyMap.get("rewardAmount")!=null){
				rewardTotalAmount = NumberUtil.add(rewardTotalAmount, (Double) moneyMap.get("rewardAmount"));
			}

		}
		
		
		totalMoney = NumberUtil.add(onlineIncomeTotalAmount, rewardTotalAmount);
		
		//TODO 更新总店账户，记录账户流水，增加提现信息
		dealTransactionShopAccount(headquartersShop, totalMoney, onlineIncomeTotalAmount, rewardTotalAmount);
		
		//处理提现
		Map<String, Object> resultMap = withdrawByShop(dealShopWithDraw(requestModel, totalMoney));
		
		return resultMap;
	}
	/**
	 * 更新总店账户，记录账户流水
	 */
	private void dealTransactionShopAccount(ShopAccountDto headquartersShop,Double totalMoney,Double onlineIncomeTotalAmount, Double rewardTotalAmount) throws Exception{
		
		Long shopId = headquartersShop.getShopId();
		Double accountAmount = headquartersShop.getAmount();
		
		//线上收入账户
		if(onlineIncomeTotalAmount!=null && onlineIncomeTotalAmount>0){
			
			Double onlineIncomeAmount = headquartersShop.getOnlineIncomeAmount();
			Double onlineIncomeAfterAmount = NumberUtil.add(onlineIncomeAmount, onlineIncomeTotalAmount);
			accountAmount = NumberUtil.add(accountAmount, onlineIncomeAmount);
			
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_ENTER, CommonConst.BILL_DIRECTION_ADD,
					"连锁提现转出线上收入"+onlineIncomeTotalAmount+"元", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_INCOME,
					accountAmount, onlineIncomeAfterAmount, onlineIncomeTotalAmount,"连锁提现转入" ,null,null);
		
		}
		
		//奖励账户
		if(rewardTotalAmount!=null && rewardTotalAmount>0){
			
			Double rewardAmount = headquartersShop.getRewardAmount();
			Double rewardAmountAfterAmount = NumberUtil.add(rewardAmount, rewardTotalAmount);

			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_ENTER, CommonConst.BILL_DIRECTION_ADD,
					"连锁提现转出奖励收入"+rewardTotalAmount+"元", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_REWARD,
					rewardAmount, rewardAmountAfterAmount, rewardTotalAmount,"连锁提现转入" ,null,null);
		
		}
		
		//更新账户信息
		shopAccountDao.updateShopAccount(shopId, totalMoney, onlineIncomeTotalAmount, rewardTotalAmount, rewardTotalAmount, null, null,null,null,null, null);
		
	}
	/**
	 * 构建商铺提现信息
	 */
	private ShopWithDrawDto dealShopWithDraw(WithdrawRequestModel requestModel,Double totalMoney) throws Exception{
		
		ShopWithDrawDto shopWithDrawDto = new ShopWithDrawDto();
		
		Long shopId = requestModel.getShopId();
		// 查询卡号记录
		BankCardDto bankCardDto = getBankCardByCard(requestModel.getCardNumber(), shopId.toString(),AccountTypeEnum.SHOP.getValue());
		if(bankCardDto != null){
			
			shopWithDrawDto.setUserName(bankCardDto.getName());
			shopWithDrawDto.setBankName(bankCardDto.getBankName());
		}
		shopWithDrawDto.setAmount(totalMoney);
		shopWithDrawDto.setShopId(shopId);
		shopWithDrawDto.setCardNumber(requestModel.getCardNumber());
		shopWithDrawDto.setWithdrawStatus(CommonConst.WITHDRAW_STATUS_TODO);
		shopWithDrawDto.setHandleTime(new Date());
		
		return shopWithDrawDto;
	}

	/**
	 * 更新分店账户，记录账户流水
	 */
	private Map<String, Object> dealBranchShopAccount(ShopAccountDto account,WithdrawListDto withdraw) throws Exception{
		
		Map<String, Object> moneyMap = new HashMap<String, Object>();
		//线上收入
		Double onlineIncomeAmount = account.getOnlineIncomeAmount();
		//奖励收入
		Double rewardAmount = account.getRewardAmount();
		//提现金额
		Double money = withdraw.getMoney();
		//分店id
		Long shopId = withdraw.getShopId();
		//账户余额
		Double accountAmount = account.getAmount();
		
		//线上收入足够
		if(onlineIncomeAmount>=money){
			
			Double onlineIncomeAfterAmount = NumberUtil.sub(onlineIncomeAmount, money);

			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_OUT, CommonConst.BILL_DIRECTION_DOWN,"连锁提现转出线上收入"+money+"元" , CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.SHOP_ACCOUNT_TYPE_INCOME,accountAmount, onlineIncomeAfterAmount, -money, "连锁提现转出",null,null);
		
			shopAccountDao.updateShopAccount(shopId, -money, -money, null, null, null, null,null,null,null, null);
			moneyMap.put("onlineIncomeAmount", money);
			
			
		}
		//奖励足够
		else if(rewardAmount>=money){
			
			Double rewardAmountAfterAmount = NumberUtil.sub(rewardAmount, money);

			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_OUT, CommonConst.BILL_DIRECTION_DOWN,"连锁提现转出奖励收入"+money+"元" , CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.SHOP_ACCOUNT_TYPE_REWARD,accountAmount, rewardAmountAfterAmount,-money, "连锁提现转出",null,null);
			
			shopAccountDao.updateShopAccount(shopId, -money, null, -money, null, null, null,null,null,null, null);
			
			moneyMap.put("rewardAmount", money);

		}
		//线上收入+ 奖励收入  足够
		else if(onlineIncomeAmount<money
				&&rewardAmount<money
				&&NumberUtil.add(onlineIncomeAmount, rewardAmount)>=money){
			
//			Double onlineIncomeAfterAmount = NumberUtil.sub(money,onlineIncomeAmount);
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_OUT, CommonConst.BILL_DIRECTION_DOWN,"连锁提现转出线上收入"+onlineIncomeAmount+"元" , CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.SHOP_ACCOUNT_TYPE_INCOME,accountAmount, 0D, -onlineIncomeAmount, "连锁提现转出",null,null);
			
			//奖励扣除金额
			Double changeRewardAmount = NumberUtil.sub(money,onlineIncomeAmount);
			Double rewardAmountAfterAmount = NumberUtil.sub(rewardAmount, changeRewardAmount);
			accountAmount = NumberUtil.sub(accountAmount, changeRewardAmount);

			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_OUT, CommonConst.BILL_DIRECTION_DOWN,"连锁提现转出奖励收入"+changeRewardAmount+"元" , CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.SHOP_ACCOUNT_TYPE_REWARD,accountAmount, rewardAmountAfterAmount, -changeRewardAmount,"连锁提现转出" ,null,null);
		
		   //更新账户
			shopAccountDao.updateShopAccount(shopId, -money, -onlineIncomeAmount,
					-changeRewardAmount, null, null, null,null,null,null, null);
			
			moneyMap.put("onlineIncomeAmount", onlineIncomeAmount);
			moneyMap.put("rewardAmount", changeRewardAmount);
			
		}
		
		return moneyMap;
		
	}
	/**
	 * 插入店铺流水
	 * 
	 * @Function: com.idcq.appserver.service.pay.impl.PayServiceImpl.insertShopBill
	 * @Description:
	 *
	 * @param shopId 商铺id
	 * @param billType 账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,红包=3(停用),提现=4,充值=5,推荐奖励=6,提现退回=7,
	 * 冻结资金=8,解冻资金=9,转充=10,充值卡充值=11,购买插件=12,锁提现流转转入=21,连锁提现转出=22,发红包=41',
	 * @param billDirection '账单类型:1（账户资金增加）,-1（账户资金减少）',
	 * @param BillDesc 描述
	 * @param billStatus 充值成功 更新商铺账单 成功 账单状态:处理中=1,成功=2,失败=3
	 * @param accountType 账户类型：0=线上营业收入，1=平台奖励，2=冻结资金，3=保证金
	 * @param accountAmount 处理前账号余额
	 * @param accountAfterAmount 使用后余额
	 * @param money 本次账单金额
	 * @param billTitle 账单标题
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月30日 下午4:44:08
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月30日    ChenYongxin      v1.0.0         create
	 */
	public ShopBillDto insertShopBill(Long shopId,String billType,Integer billDirection,String billDesc,Integer billStatus,
			Integer accountType,Double accountAmount,Double accountAfterAmount,Double money,String billTitle,Long transactionId,String orderId) throws Exception{
		 
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setBillStatus(billStatus); // 充值成功 更新商铺账单 成功 账单状态:处理中=1,成功=2,失败=3
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setShopId(shopId);
        shopBillDto.setMoney(money);
        shopBillDto.setBillType(billType);
        shopBillDto.setBillDirection(billDirection);//1代表增加
        shopBillDto.setAccountAmount(accountAmount);
        shopBillDto.setBillDesc(billDesc);
        shopBillDto.setAccountType(accountType);//保证金CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT
        shopBillDto.setAccountAfterAmount(accountAfterAmount);
        shopBillDto.setBillTitle(billTitle);
        shopBillDto.setTransactionId(transactionId);
        shopBillDto.setOrderId(orderId);
        shopBillDao.insertShopBill(shopBillDto);
        
        return shopBillDto;
	}
	/**
	 * 校验总店
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	private ShopAccountDto validateHeadquartersShop(Long shopId,Double depositMoney) throws Exception {
		
        ShopDto shop = shopDao.getShopById(shopId);
        CommonValidUtil.validObjectNull(shop, CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        
        Integer shopStatus = shop.getShopStatus();
        CommonValidUtil.validShopStatus(shopStatus, new Integer[]{ CommonConst.SHOP_LACK_MONEY_STATUS, CommonConst.SHOP_AUDIT_STATUS });
        
		// 查询账户余额
		ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
		
		if(null==account){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,"商铺传奇宝账号不存在");
		}
		if(null ==account.getAccountStatus() || 0==account.getAccountStatus()){
			throw new ValidateException(CodeConst.CODE_USER_ACCOUNT_FROZEN_58301, CodeConst.MSG_USER_ACCOUNT_FROZEN);
		}
		if(null!=depositMoney){
			if(account.getDepositAmount()<depositMoney){
				throw new ValidateException(CodeConst.CODE_ACCOUNT_NOT_BALANCE,CodeConst.MSG_ACCOUNT_NOT_BALANCE);
			}
		}
		
		return account;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.service.pay.IPayServcie#depositChange(java.lang.Long, java.lang.Integer, java.lang.Double)
	 */
	@Override
	public void depositChange(Long shopId, Integer accountType, Double money, Integer billType)
			throws Exception {
		
		//验证店铺
		ShopAccountDto shopAccount = validateHeadquartersShop(shopId,money);
		
		//账户类型  3-平台账户
		if(3==accountType){
		    String shopBillType = CommonConst.SHOP_BILL_TYPE_PACKAGE_PAY;
		    int platformBillType = CommonConst.PLATFORM_BILL_TYPE_PACKAGE_INCOME;
		    String billTitle = "划扣套餐费用" +money+"元";
		    String billDesc = "商铺保证金转入平台账户";
		    if(null != billType && billType == CommonConst.PLATFORM_BILL_TYPE_DEPOSIT_INCOME) {
		        shopBillType = CommonConst.SHOP_BILL_TYPE_DEPOSIT_PAY;
		        platformBillType = CommonConst.PLATFORM_BILL_TYPE_DEPOSIT_INCOME;
		        billTitle = "停用扣除保证金" +money+"元";
		    } else if (null != billType && billType == CommonConst.PLATFORM_BILL_TYPE_DEPOSIT_BACK) {
		        shopBillType = CommonConst.SHOP_BILL_TYPE_DEPOSIT_BACK;
                platformBillType = CommonConst.PLATFORM_BILL_TYPE_DEPOSIT_BACK;
                billTitle = "退还赠送保证金" +money+"元";
		    }
			//记录流水
			insertShopBill(shopId, shopBillType, CommonConst.BILL_DIRECTION_DOWN,
			        billDesc, CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT,
					shopAccount.getAmount(), NumberUtil.sub(shopAccount.getDepositAmount(), money), 
					-money, billTitle,null,null);
			//更新商铺账户
			shopAccountDao.updateShopAccount(shopId, -money, null, null, null, -money, null,null,null,null, null);
			
			//记录平台账单
			PlatformBillDto platformBillDto = buildPlatformBill(billDesc, CommonConst.BILL_DIRECTION_ADD, CommonConst.SHOP_BILL_STATUS_OVER, 
					CommonConst.BILL_TYPE_DEPOSIT_CHANGE, "", money, 
					CommonConst.MONEY_SOURCE_BZJ, shopId, shopId, platformBillType);

			platformBillDao.insertPlatformBill(platformBillDto);
			
		}
		//2-奖励账户
		if(2==accountType){
			
			//记录保证金流水
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER, CommonConst.BILL_DIRECTION_DOWN,
					"保证金转入奖励账户", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT,
					shopAccount.getAmount(), NumberUtil.sub(shopAccount.getDepositAmount(), money), 
					-money, "保证金转入奖励账户"+money+"元",null,null);
			//记录保证金流水
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER, CommonConst.BILL_DIRECTION_ADD,
					"保证金转入奖励账户", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_REWARD,
					shopAccount.getAmount(), NumberUtil.add(shopAccount.getRewardAmount(), money), 
					money, "保证金转入奖励账户"+money+"元",null,null);
			//更新商铺账户
			shopAccountDao.updateShopAccount(shopId, null, null, money, money, -money, null,null,null,null, null);
			
		}
		//1-线上收入账户 
		if(1==accountType){
			//记录保证金流水
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER, CommonConst.BILL_DIRECTION_DOWN,
					"保证金转入线上账户", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT,
					shopAccount.getAmount(), NumberUtil.sub(shopAccount.getDepositAmount(), money), 
					-money, "保证金转入线上账户"+money+"元",null,null);
			//记录保证金流水
			insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER, CommonConst.BILL_DIRECTION_ADD,
					"保证金转入线上账户", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_INCOME,
					shopAccount.getAmount(), NumberUtil.add(shopAccount.getOnlineIncomeAmount(), money), 
					money, "保证金转入线上账户"+money+"元",null,null);
			//更新商铺账户
			shopAccountDao.updateShopAccount(shopId, null, money, null, null, -money, null,null,null,null, null);
		}
		
	}
	private PlatformBillDto buildPlatformBill(String billDesc,Integer billDirection,Integer billStatus,String billType,
			String consumerMobile,Double money,Integer moneySource,Long shopId,Long consumerUserId,
			Integer platformBillType) throws Exception{
		
		PlatformBillDto platformBillDto = new PlatformBillDto();
		
		platformBillDto.setBillDesc(billDesc);
		platformBillDto.setBillDirection(billDirection);
		platformBillDto.setBillStatus(billStatus);
		platformBillDto.setBillType(billType);
		platformBillDto.setConsumerMobile(consumerMobile);
		platformBillDto.setCreateTime(new Date());
		platformBillDto.setMoney(money);
		platformBillDto.setMoneySource(moneySource);
		platformBillDto.setShopId(shopId);
		platformBillDto.setConsumerUserId(shopId);
		platformBillDto.setPlatformBillType(platformBillType);
		
		return platformBillDto;
		
	}
	
	/**
	 * 插入反结账订单商品线上支付账单
	 */
	public void insertReverseShopBill(OrderDto orderDto) throws Exception{
		if(orderDto != null){
			logger.info("插入商铺流水orderId:"+orderDto.getOrderId());
			//实际线上支付总额
			Double onLineMoney  =  payDao.getSumPayAmount(orderDto.getOrderId(), 0);
			//流水线上支付总额
			Double billMoney = shopBillDao.getShopBillSumMoney(orderDto.getOrderId(), 0);
			Long shopId = orderDto.getShopId();
			ShopAccountDto shopAccountDto =shopAccountDao.getShopAccount(shopId);
			//应该补齐线上支付金额 = 实际线上支付总额-流水线上支付总额
			Double changeMoney = NumberUtil.sub(billMoney,onLineMoney);
			
	        if(changeMoney>0d){
	        	insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_SALE, CommonConst.BILL_DIRECTION_ADD,
						"销售商品"+changeMoney+"元", CommonConst.SHOP_BILL_STATUS_OVER,CommonConst.SHOP_ACCOUNT_TYPE_INCOME,
						shopAccountDto.getAmount(), shopAccountDto.getOnlineIncomeAmount(), changeMoney,"销售商品" ,null,null);
	        	//更新账户
	        	shopAccountDao.updateShopAccount(shopId, changeMoney, changeMoney, null, null, null, null,null, null, null, null);
	      }
		}
	}

}
