package com.idcq.appserver.service.pay.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.cashcoupon.IUserCashCouponDao;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dao.plugins.IShopPluginDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.packet.RedPacketPayInfo;
import com.idcq.appserver.dto.pay.GroupPayDetailModel;
import com.idcq.appserver.dto.pay.GroupPayModel;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.plugin.PluginDto;
import com.idcq.appserver.dto.plugin.ShopPluginDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.bill.IPlatformBillService;
import com.idcq.appserver.service.bill.IUserBillService;
import com.idcq.appserver.service.bill.IUserXBillService;
import com.idcq.appserver.service.cashcoupon.IUserCashCouponService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IChuanQiPayService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.plugins.IPluginsService;
import com.idcq.appserver.service.shop.IShopBillService;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;

@Service
public class ChuanQiPayServiceImpl implements IChuanQiPayService {

	private static final Logger logger = Logger.getLogger(ThirdPayServiceImpl.class);
	@Autowired
	private IShopAccountDao shopAccountDao;
	@Autowired
	private IShopBillDao shopBillDao;
	@Autowired
	private ITransactionDao transactionDao;
	@Autowired
	private IShopPluginDao shopPluginDao;
	@Autowired
	private IPluginsService pluginService;
	@Autowired
	private IUserAccountDao userAccountDao;
    @Autowired
    public IOrderDao orderDao;
    @Autowired
    private IOrderLogDao orderLogDao;
    @Autowired
	public IPayDao payDao;
	@Autowired
	private IUserBillDao userBillDao;
	@Autowired
	private IPayServcie payServcie;
	@Autowired
	private IUserBillService userBillService;
	@Autowired
	private IPlatformBillService platformBillService;
	@Autowired
	private ICollectDao collectDao;
	@Autowired
	private IUserCashCouponService userCashCouponService;
	@Autowired
	private IUserCashCouponDao userCashCouponDao;
	@Autowired
	private IUserXBillService userXBillService;
	@Autowired
	private IOrderServcie orderService;
	@Autowired
	private IPayServcie payService;
	@Autowired
	public IPacketDao packetDao;
	@Autowired
    public IPacketService packetService;
    @Autowired
    private IStorageServcie storageService;
	@Autowired
	private ILevelService levelService;
	@Autowired
	private IShopBillService shopBillService;
	@Autowired
	private IShopDao shopDao;
	
	@Override
	public void groupPay(GroupPayModel groupPayModel) throws Exception {
		Long userId = groupPayModel.getUserId();
		String orderId = groupPayModel.getOrderId();
		Integer autoSettleFlag  = groupPayModel.getAutoSettleFlag();
		if(null == autoSettleFlag) {
		    autoSettleFlag = 0;
		}
		double chuanQiPayNum = 0;
		double consumCardPayNum = 0;
		double redPacketPayNum = 0;
		double voucherPayNum = 0;
		for (GroupPayDetailModel payDetail : groupPayModel.getPayInfo()) {
			double payAmount = payDetail.getPayAmount().doubleValue();
			Integer clientSystem = payDetail.getClientSystem();
			if (payAmount <= 0) {
				continue;
			}
			int payType = payDetail.getPayType();
			if (payType == 1) {
				payOrderByChuanQiPay(userId, orderId, payAmount,clientSystem, autoSettleFlag);
				chuanQiPayNum += payAmount;
			} else if (payType == 2) {
				payOrderByConsumCard(userId, orderId, payAmount,clientSystem, autoSettleFlag);
				consumCardPayNum += payAmount;
			} else if (payType == 3) {
			    payOrderByRedPacket(userId, orderId, payAmount,payDetail.getAccountId(),clientSystem, autoSettleFlag);
			    redPacketPayNum += payAmount;
			}else if (payType == 4) {
				payOrderByVoucher(userId, orderId, payAmount, clientSystem, autoSettleFlag);
				voucherPayNum += payAmount;
			}else {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"组合支付目前不支持该种支付方式  payType:"+payType);
			}
		}
		
		OrderDto order = updateOrderStatus(orderId,autoSettleFlag);
		
		if (autoSettleFlag > 0) {
			OrderGoodsSettleUtil.detailOrderGoodsSettle(order.getOrderId(), CommonConst.PAY_TYPE_SINGLE);
		}
		
		String chuanQiPayOrderRemark = chuanQiPayNum != 0 ? " 传奇宝支付:"+chuanQiPayNum : "";
		String consumCardPayOrderRemark = consumCardPayNum !=0 ? " 消费卡支付" +consumCardPayNum : "";
		String redPacketPayOrderRemark = redPacketPayNum !=0 ? " 红包支付" +redPacketPayNum : "";
		String voucherPayOrderRemark = voucherPayNum !=0 ? " 代金券支付" + voucherPayNum : "";
		insertOrderLog(order, userId.longValue(),"组合支付"+
									chuanQiPayOrderRemark+
									consumCardPayOrderRemark+
									redPacketPayOrderRemark+
									voucherPayOrderRemark);
		
		orderService.pushOrder(orderId, userId, groupPayModel.getOrderAmount());
	}
	
	@Override
	public void payOrderByRedPacket(Long userId, String orderId, double payAmount,Long redPacketId,Integer clientSystem, Integer autoSettleFlag) throws Exception {
		OrderDto order = orderDao.getOrderById(orderId);
		Long shopId = order.getShopId();
		if (redPacketId == null) {
			Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
			if (redPacketAmount < payAmount) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包余额不足");
			}
			payService.payByRedPacket(payAmount, order, userId, true,clientSystem);
		}else {
			RedPacket redPacket = packetDao.queryRedPacketById(redPacketId);
			if (redPacket.getStatus() != RedPacketStatusEnum.USEABLE.getValue()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包不可用  红包Id:"+redPacketId);
			}
			if (redPacket.getAmount() < payAmount) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包余额不足  红包Id:"+redPacketId);
			}
			
			payService.useRedPacket(order, redPacket, payAmount, userId,clientSystem);
		}
	}
	
	@Override
	public List<RedPacketPayInfo> getRedPacketPayInfoByPayAmount(Long shopId, Long userId,double payAmount) throws Exception {
		  List<RedPacket> redPackets = packetDao.getRedPacketBy(shopId, userId, 
					RedPacketStatusEnum.USEABLE.getValue());
		  
		  if (CollectionUtils.isEmpty(redPackets)) {
		    	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"该用户没有可用红包  userId:"+userId);
		  }
		  
		  List<RedPacketPayInfo> redPacketPayInfoList = new LinkedList<RedPacketPayInfo>();
		  double needPayAmount = payAmount;
		  for (RedPacket redPacket : redPackets) {
			  if (needPayAmount <= 0) {
				  break;
			  }
			  
			  Long redPacketId = redPacket.getRedPacketId();
			  Double validAmount = redPacket.getAmount();
			  if (validAmount < 0) {
				  continue;
			  }
			  RedPacketPayInfo payInfo = new RedPacketPayInfo();
			  payInfo.setRedPacketId(redPacketId);
			  if (validAmount <= needPayAmount) {
				  payInfo.setPayAmount(validAmount);
				  needPayAmount = NumberUtil.sub(needPayAmount, validAmount);
			  }else {
				  payInfo.setPayAmount(needPayAmount);
				  needPayAmount = NumberUtil.sub(needPayAmount, needPayAmount);
			  }
			  
			  redPacketPayInfoList.add(payInfo);
		  }
		  
		  logger.info("获取红包支付信息列表："+redPacketPayInfoList.toString());
		  return redPacketPayInfoList;
	}
	
	@Override
	public List<RedPacketPayInfo> getRedPacketPayInfoByIds(List<Long> redPacketIds,double payAmount) throws Exception {
		List<RedPacketPayInfo> redPacketPayInfoList = new LinkedList<RedPacketPayInfo>();
		double needPayAmount = payAmount;
		for (Long redPacketId : redPacketIds) {
			
			RedPacket redPacket = packetDao.queryRedPacketById(redPacketId);
			
			if (redPacket == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包不存在  红包Id:"+redPacketId);
			}
			if (redPacket.getStatus() != RedPacketStatusEnum.USEABLE.getValue()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包不可用  红包Id:"+redPacketId);
			}
			
			if (needPayAmount <= 0) {
				  break;
			}
			  
			Double validAmount = redPacket.getAmount();
			if (validAmount < 0) {
				continue;
			}
			
			RedPacketPayInfo payInfo = new RedPacketPayInfo();
			payInfo.setRedPacketId(redPacketId);
		    if (validAmount <= needPayAmount) {
				  payInfo.setPayAmount(validAmount);
				  needPayAmount = NumberUtil.sub(needPayAmount, validAmount);
			}else {
				  payInfo.setPayAmount(needPayAmount);
				  needPayAmount = NumberUtil.sub(needPayAmount, needPayAmount);
			}
			  
			redPacketPayInfoList.add(payInfo);
		}
		
		if (needPayAmount > 0) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"红包余额不足  红包Id:"+redPacketIds.toString());
		}
		
		logger.info("获取红包支付信息列表："+redPacketPayInfoList.toString());
		return redPacketPayInfoList;
	}
	@Override
	public void checkRedPacketValid(Long shopId, Long userId,double payAmount,Long redPacketId) throws Exception {
	    List<RedPacket> redPackets = packetDao.getRedPacketBy(shopId, userId, 
	    							RedPacketStatusEnum.USEABLE.getValue());
	    
	    if (CollectionUtils.isEmpty(redPackets)) {
	    	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"该用户没有可用红包  userId:"+userId);
	    }
		if (redPacketId == null) {
			Double redPacketAmount = packetDao.getRedPacketAmountBy(shopId, userId, RedPacketStatusEnum.USEABLE.getValue());
			if (redPacketAmount < payAmount) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包余额不足");
			}
		}else {
			RedPacket redPacket = packetDao.queryRedPacketById(redPacketId);
			
			if (redPacket == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包不存在  红包Id:"+redPacketId);
			}
			
			if (redPacket.getStatus() != RedPacketStatusEnum.USEABLE.getValue()) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包不可用  红包Id:"+redPacketId);
			}
			if (redPacket.getAmount() < payAmount) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"红包余额不足  红包Id:"+redPacketId);
			}
		}
	}
	
	@Override
	public void payOrderByVoucher(Long userId, String orderId, double payAmount,Integer clientSystem, Integer autoSettleFlag) throws Exception {
		OrderDto order = orderDao.getOrderById(orderId);
		ShopDto shop = shopDao.getShopEssentialInfo(order.getShopId());
		if (shop.getShopIdentity() != 2) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"代金券只能在红店使用");
		}
		
		double avaliVoucherAmount = orderService.countVoucherDeduction(userId, order);
		if (payAmount > avaliVoucherAmount) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"代金券可用金额不足  支付金额："+payAmount+" 代金券可用金额："+avaliVoucherAmount);
		}
		
		UserAccountDto userAccount = userAccountDao.getAccountMoney(userId.longValue());
		if (userAccount == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, 
										"用户账户不存在  userId:"+userId);
		}
		
		double voucherAmount = userAccount.getVoucherAmount();
		double voucherBlance = NumberUtil.sub(voucherAmount, payAmount);
		double amount = userAccount.getAmount();
		
		userAccountDao.updateCouponAndRewardAmount(userId.longValue(), 
												   NumberUtil.sub(amount, payAmount),
												   null,
												   null,
												   voucherBlance);
		
		TransactionDto transaction = addTransactionForChuanQiPay(userId, orderId, 
									payAmount, CommonConst.TRANSACTION_TYPE_ORDER);
		
		if (payAmount > 0) {
			insertOrderPayForChuanQiPay(orderId, payAmount,
					  userId.longValue(), order.getShopId(), 
					  transaction.getTransactionId(), CommonConst.PAY_TYPE_VOUCHER,clientSystem, autoSettleFlag);
			
			userBillService.insertUserBill(userId.longValue(),
					transaction.getTransactionId().longValue(),
					CommonConst.BILL_DIRECTION_DOWN,
					payAmount, voucherBlance, 
					userAccount.getAmount(), order, "消费", 
					CommonConst.PAY_TYPE_SINGLE, "组合支付传奇Pay代金券消费", 
					CommonConst.USER_BILL_TYPE_CONSUME,
					CommonConst.USER_ACCOUNT_TYPE_VOUCHER,
					null);
			
			platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, payAmount,
					CommonConst.PLT_BILL_MNY_SOURCE_CQB, order, 
					transaction.getTransactionId(), 
					"消费支付", "组合支付传奇Pay代金券消费", CommonConst.PLATFORM_BILL_TYPE_PAY,
					CommonConst.PLATFORM_BILL_STATUS_OVER);
		}
	}
	
	@Override
	public void payOrderByChuanQiPay(Long userId, String orderId, double payAmount,Integer clientSystem, Integer autoSettleFlag) throws Exception {
		UserAccountDto userAccount = userAccountDao.getAccountMoney(userId.longValue());
		if (userAccount == null) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, 
										"用户账户不存在  userId:"+userId);
		}
		
		double couponAmount = userAccount.getCouponAmount();
		double rewardAmount = userAccount.getRewardAmount();
		double amountBlance = NumberUtil.add(couponAmount, rewardAmount);

		if (payAmount > amountBlance) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"用户账户余额不足，请及时充值");
		}
		
        double deductCouponAmount = 0;
        double deductRewardAmount = 0;
        
		if (couponAmount < payAmount) {
			deductCouponAmount  = couponAmount;
			deductRewardAmount = NumberUtil.sub(payAmount, couponAmount) ;
		}
		else {
			deductCouponAmount = payAmount;
		}
		
		double userAccountBlance = NumberUtil.sub(amountBlance, payAmount);
		double couponBlance = NumberUtil.sub(couponAmount, deductCouponAmount);
		double rewardBlance = NumberUtil.sub(rewardAmount, deductRewardAmount);
		
		
		userAccountDao.updateCouponAndRewardAmount(userId.longValue(), 
												   userAccountBlance,
												   couponBlance,
												   rewardBlance,
												   null);
		
		TransactionDto transaction = addTransactionForChuanQiPay(userId, orderId, 
									payAmount, CommonConst.TRANSACTION_TYPE_ORDER);
		
		OrderDto order = orderDao.getOrderById(orderId);
		
		if (deductCouponAmount > 0) {
			
			insertOrderPayForChuanQiPay(orderId, deductCouponAmount,
									  userId.longValue(), order.getShopId(), 
									  transaction.getTransactionId(), CommonConst.PAY_TYPE_CONSUM,clientSystem, autoSettleFlag);
			
			userBillService.insertUserBill(userId.longValue(),
										transaction.getTransactionId().longValue(),
										CommonConst.BILL_DIRECTION_DOWN,
										deductCouponAmount, couponBlance, 
										userAccountBlance, order, "消费", 
										CommonConst.PAY_TYPE_SINGLE, "组合支付传奇Pay消费金消费", 
										CommonConst.USER_BILL_TYPE_CONSUME,
										CommonConst.USER_ACCOUNT_TYPE_MONETARY,
										null);
			
			platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, deductCouponAmount,
											CommonConst.PLT_BILL_MNY_SOURCE_CQB, order, 
											transaction.getTransactionId(), 
											"消费支付", "组合支付传奇Pay消费金消费", CommonConst.PLATFORM_BILL_TYPE_PAY,
											CommonConst.PLATFORM_BILL_STATUS_OVER);
		}
		
		if (deductRewardAmount > 0) {
			
			insertOrderPayForChuanQiPay(orderId, deductRewardAmount,
					  				userId.longValue(), order.getShopId(), 
				  					transaction.getTransactionId(), CommonConst.PAY_TYPE_REWARD,clientSystem,autoSettleFlag);
			
			userBillService.insertUserBill(userId.longValue(),
									transaction.getTransactionId(),
									CommonConst.BILL_DIRECTION_DOWN,
									deductRewardAmount, rewardBlance, 
									userAccountBlance, order, "消费",
									CommonConst.PAY_TYPE_SINGLE, "组合支付传奇Pay平台奖励消费", 
									CommonConst.USER_BILL_TYPE_CONSUME,
									CommonConst.USER_ACCOUNT_TYPE_REWARD,
									null);
			
			platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, deductRewardAmount,
									CommonConst.PLT_BILL_MNY_SOURCE_CQB, order, 
									transaction.getTransactionId(), "消费支付", 
									"组合支付传奇Pay平台奖励消费", CommonConst.PLATFORM_BILL_TYPE_PAY, 
									CommonConst.PLATFORM_BILL_STATUS_OVER);
		}
		
	}
	
	@Override
	public void payOrderByConsumCard(Long userId, String orderId, double payAmount,Integer clientSystem, Integer autoSettleFlag) throws Exception {
		 double consumCardBalance = userCashCouponService.getUserCashCouponBalance(userId.longValue());
		 
		 if (payAmount > consumCardBalance) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
						"用户消费卡余额不足"); 
		 }
		 
		 OrderDto order = orderDao.getOrderById(orderId);
		 List<UserCashCouponDto> userCashCoupons = userCashCouponDao.getUserCashCouponByUserId(userId.longValue());
		 
		 double needPay = payAmount;
		 for (UserCashCouponDto consumCard : userCashCoupons) {
			 	
			 	if (needPay == 0) {
			 		break;
			 	}
			 	
			 	Long uccId = consumCard.getUccId();
		     	Double price = consumCard.getPrice();
		     	Double usedPrice = consumCard.getUsedPrice();
	            Double payBeforeBalance = NumberUtil.sub(price, usedPrice);
	            
	            if (payBeforeBalance <= 0) {
	            	continue;
	            }else if (payBeforeBalance >= needPay) {
	            	consumCardPay(orderId, order.getShopId().longValue(),
		            			userId.longValue(), uccId, usedPrice, needPay, 
		            			payBeforeBalance, order.getOrderTitle(),clientSystem,autoSettleFlag);
	            	
	            	needPay = 0;
	            }else {
	             	consumCardPay(orderId, order.getShopId().longValue(),
	            			userId.longValue(), uccId, usedPrice, payBeforeBalance, 
	            			payBeforeBalance, order.getOrderTitle(),clientSystem,autoSettleFlag);
	             	
	            	needPay = NumberUtil.sub(needPay, payBeforeBalance);
	            }
		 }
		 
/*		 TransactionDto transaction = addTransactionForChuanQiPay(userId, orderId, 
					payAmount, CommonConst.TRANSACTION_TYPE_ORDER);*/

		 platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, payAmount,
										CommonConst.PLT_BILL_MNY_SOURCE_XFK, order, 
										null, "消费支付", 
										"组合支付消费卡消费", CommonConst.PLATFORM_BILL_TYPE_PAY, 
										CommonConst.PLATFORM_BILL_STATUS_OVER);
	}
	
	private void consumCardPay(String orderId, Long shopId, 
								Long userId,Long uccId, 
								Double hasUsedPrice,
								Double payAmount, 
								Double beforUsedAccountAmount,
								String orderTile,
								Integer clientSystem, Integer autoSettleFlag) throws Exception {
		
		updateUserCashCoupon(uccId, hasUsedPrice, payAmount);
		
		insertOrderPayForChuanQiPay(orderId, payAmount, 
									userId, shopId, uccId,
									CommonConst.PAY_TYPE_CASH_COUPON,clientSystem,autoSettleFlag);
		
		userXBillService.insertUserXBill(userId, uccId,
										orderId, CommonConst.PAY_TYPE_SINGLE, 
										CommonConst.USER_CASHCOUPON_USE, payAmount,
										beforUsedAccountAmount, orderTile, "消费卡消费");
		
	}
	
	private void updateUserCashCoupon(Long uccId, Double hasUsedPrice,Double payAmount) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("usedPrice", (NumberUtil.add(hasUsedPrice, payAmount)));
		param.put("payId", uccId);
		userCashCouponDao.updateUserCashCoupon(param);
	}
	
	private void insertOrderPayForChuanQiPay(String orderId,Double payAmount,
										   Long userId,Long shopId,Long payId,
										   Integer PayType,Integer clientSystem, Integer autoSettleFlag) throws Exception {
		
		if(payAmount>0){
			PayDto orderPay =new PayDto();
			String nowTime = DateUtils.format(new Date(), 
							 DateUtils.DATETIME_FORMAT);
			
			orderPay.setOrderId(orderId);
			orderPay.setPayAmount(payAmount);
			orderPay.setOrderPayType(CommonConst.PAY_TYPE_SINGLE);
			orderPay.setUserId(userId);
			orderPay.setOrderPayTime(nowTime);
			orderPay.setLastUpdateTime(nowTime);
			orderPay.setPayeeType(CommonConst.PAYEE_TYPE_PLATFORM);
			orderPay.setShopId(shopId);
			orderPay.setPayId(payId);
			orderPay.setPayType(PayType);
			orderPay.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
			orderPay.setClientSystem(clientSystem);
			orderPay.setAutoSettleFlag(autoSettleFlag);
			payDao.addOrderPay(orderPay);
		}
	}
	private OrderDto updateOrderStatus(String orderId,Integer autoSettleFlag) throws Exception {
		OrderDto order = orderDao.getOrderById(orderId);
		order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
		if (autoSettleFlag > 0) {
			order.setOrderStatus(CommonConst.ORDER_STATUS_FINISH);
			if (order.getIsMember() == 1) {
				levelService.pushAddPointMessage(5, null, 1, order.getShopId().intValue(), 4, orderId,true);
			}
			
			Double sendMoney = packetService.sendRedPacketToUser(order);
		    if(sendMoney != 0) {
	                //更新订单结算价格
	             Double orderRealSettlePrice = NumberUtil.sub(order.getOrderRealSettlePrice(), sendMoney);
	              if (orderRealSettlePrice < 0) {
	                    orderRealSettlePrice = 0D;
	              }
	              order.setOrderRealSettlePrice(orderRealSettlePrice);
	              order.setSendRedPacketMoney(sendMoney);
	              orderDao.updateOrder(order);
	        }
		    orderService.updateGoodsAndShopSoldNum(orderId);
	        //插入反结账订单商品线上支付账单
	        payServcie.insertReverseShopBill(order);
		}
		Date now = new Date();
		order.setLastUpdateTime(now);
		order.setServerLastTime(now.getTime());
		orderDao.updateOrder(order);
		
        if(CommonConst.ORDER_STS_YJZ == order.getOrderStatus()){
			//修改库存
			storageService.insertShopStorageByOrderId(orderId,order.getShopId());
			collectDao.updateShopResourceStatus(orderId,CommonConst.RESOURCE_STATUS_NOT_IN_USE);
        }
		
		return order;
	}
	
	private void insertOrderLog(OrderDto order,Long userId,String orderRemark) throws Exception {
		OrderLogDto orderLogDto = new OrderLogDto();
		orderLogDto.setLastUpdateTime(new Date());
		orderLogDto.setOrderId(order.getOrderId());
		orderLogDto.setOrderStatus(order.getOrderStatus());
		orderLogDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
		orderLogDto.setUserId(userId);
		orderLogDto.setRemark(orderRemark);
		orderLogDao.saveOrderLog(orderLogDto);
	}
	
	@Override
	public void payShopPlugin(PluginDto pluginDto,ShopPluginDto shopPluginDto) throws Exception {
        Long shopId = shopPluginDto.getShopId().longValue();
        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(shopId);

        if (shopAccountDto == null) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺账户不存在  shopId:"+shopId);
        }
            
        double onlineIncomeAmount = shopAccountDto.getOnlineIncomeAmount();
        double rewardAmount = shopAccountDto.getRewardAmount();
        double amountBlance = NumberUtil.add(onlineIncomeAmount,rewardAmount);
        double payAmount = shopPluginDto.getMoney();
        
        if (payAmount > amountBlance) {
        	throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, 
					"商铺账户余额不足，请及时充值");
        }
        
        double deductOnlineIncome = 0;
        double deductRewardAmount = 0;
		if (onlineIncomeAmount < payAmount) {
			deductOnlineIncome  = onlineIncomeAmount;
			deductRewardAmount = NumberUtil.sub(payAmount, onlineIncomeAmount) ;
		}
		else {
			deductOnlineIncome = payAmount;
		}
		
        shopAccountDao.updateShopAccount(shopId, null, -deductOnlineIncome, 
        						-deductRewardAmount, null, null, null, null,null,null, null);
        
        TransactionDto transaction = addTransactionForChuanQiPay(shopPluginDto.getShopId().longValue(),
        														 shopPluginDto.getShopPluginId().toString(),
        														 shopPluginDto.getMoney(),
        														 CommonConst.TRANSACTION_TYPE_SHOP_PLUGIN);
        if (deductOnlineIncome > 0) {
        	shopBillService.insertShopBill(CommonConst.BILL_DIRECTION_DOWN,deductOnlineIncome, 
        			transaction.getTransactionId(), shopId, onlineIncomeAmount, 
        			NumberUtil.sub(onlineIncomeAmount, deductOnlineIncome),CommonConst.SHOP_ACCOUNT_TYPE_INCOME,CommonConst.SHOP_BILL_TYPE_PLUGIN,
        			"商铺购买"+pluginDto.getPluginName()+deductOnlineIncome,"店铺购买插件", null);
        }
        if (deductRewardAmount > 0) {
        	shopBillService.insertShopBill(CommonConst.BILL_DIRECTION_DOWN,deductRewardAmount,
        			transaction.getTransactionId(), shopId, rewardAmount, 
        			NumberUtil.sub(rewardAmount, deductRewardAmount),CommonConst.SHOP_ACCOUNT_TYPE_REWARD,CommonConst.SHOP_BILL_TYPE_PLUGIN,
        			"商铺购买"+pluginDto.getPluginName()+deductOnlineIncome,"店铺购买插件", null);
        }
        shopPluginDao.updateShopPluginAfterPaySuccess(shopPluginDto.getShopPluginId());
        pluginService.insertPlatformBillForShopPlugin(transaction, CommonConst.PLT_BILL_MNY_SOURCE_CQB, "店铺购买插件");
	}
	
	private TransactionDto addTransactionForChuanQiPay(Long userId,String orderId,
														Double payAmount,Integer transactionType) 
														throws Exception {
		
		TransactionDto transactionDto = buildTransactionForChuanQiPay(userId,orderId,payAmount,transactionType);
		transactionDao.addTransaction(transactionDto);
		return transactionDto;
	}
	
	
	private TransactionDto buildTransactionForChuanQiPay(Long userId,String orderId,
														Double payAmount,Integer transactionType) {
		TransactionDto transaction = new TransactionDto();
		transaction.setTerminalType(CommonConst.TERMINAL_TYPE_WEB);
		transaction.setUserId(userId);
		transaction.setOrderId(orderId);
		transaction.setTransactionType(transactionType);
		transaction.setPayAmount(payAmount);
		transaction.setTransactionTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		transaction.setLastUpdateTime(transaction.getTransactionTime());
		transaction.setStatus(CommonConst.TRANSACTION_STATUS_FINISH);
		transaction.setOrderPayType(CommonConst.ORDER_PAY_TYPE_SINGLE);
		return transaction;
	}
}
