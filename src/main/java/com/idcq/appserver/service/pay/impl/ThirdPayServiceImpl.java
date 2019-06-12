package com.idcq.appserver.service.pay.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.common.billStatus.RechargeEnum;
import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.common.msgPusher.busMsg.model.BusMsg;
import com.idcq.appserver.common.msgPusher.busMsg.msgSender.BusMsgSender;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.collect.ICollectDao;
import com.idcq.appserver.dao.common.IAttachmentRelationDao;
import com.idcq.appserver.dao.membercard.IUserMemberBillDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.order.IOrderLogDao;
import com.idcq.appserver.dao.order.IXorderDao;
import com.idcq.appserver.dao.packet.IPacketDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransactionDao;
import com.idcq.appserver.dao.plugins.IShopPluginDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserBillDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.membercard.UserChargeDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderLogDto;
import com.idcq.appserver.dto.order.ThirdPayUserRemarkDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.packet.RedPacketPayInfo;
import com.idcq.appserver.dto.pay.PayDto;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.level.ILevelService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.pay.IThirdPayService;
import com.idcq.appserver.service.pay.model.PayNotifyResult;
import com.idcq.appserver.service.plugins.IPluginsService;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.service.storage.IStorageServcie;
import com.idcq.appserver.utils.BillUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FieldGenerateUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.OrderGoodsSettleUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.idianmgr.dto.shop.ShopBean;
/**
 * 第三方支付service
 * @author Administrator
 *
 */
public abstract class ThirdPayServiceImpl implements IThirdPayService {

	private final static Logger logger = LoggerFactory.getLogger(ThirdPayServiceImpl.class);
	@Autowired
	private ITransactionDao transactionDao;
	@Autowired
	private IPayDao payDao;
    @Autowired
    private IPushService pushService;
    @Autowired
    private IShopServcie shopService;
    @Autowired
	public IUserDao userDao;
    @Autowired
    public IOrderDao orderDao;
    @Autowired
    private IXorderDao xorderDao;
	@Autowired
	public IPayServcie payServcie;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IOrderServcie orderServcie;
	@Autowired
	private IShopAccountDao shopAccountDao;
	@Autowired
	private IShopBillDao shopBillDao;
	@Autowired
	public IShopDao shopDao;
	@Autowired
	IUserAccountDao userAccountDao;
	@Autowired
	private IUserMemberBillDao userMemberBillDao;
    @Autowired
    private IMemberServcie memberService;
	@Autowired
	public IPacketDao packetDao;
	@Autowired
	public IAttachmentRelationDao attachmentRelationDao;
	@Autowired
	private IUserBillDao userBillDao;
	@Autowired
	public IPlatformBillDao platformBillDao;
	@Autowired
    private IOrderLogDao orderLogDao;
    @Autowired
    private IStorageServcie storageService;
	@Autowired
	private IShopPluginDao shopPluginDao;
	@Autowired
	private IPluginsService pluginService;
	@Autowired
    public IPacketService packetService;
	@Autowired
	private ISendSmsService sendSmsService;
	@Autowired
	private ILevelService levelService;
	@Autowired
	private ICollectDao collectDao;
	/* 消息推送添加 */
	@Autowired
	private BusMsgSender busMsgSender;
	@Autowired
	private IAgentDao agentDao;
	
	protected abstract String getThirdPayChannelData(Map<String, Object> requestMap,
			String channelParameter,Integer subPayModel) throws Exception;
	/**
	 * 获取第三方支付渠道预支付信息
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> prePayBy3rd(Map<String, Object> requestMap) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Integer subPayModel = Integer.valueOf(requestMap.get("subPayModel").toString());
		Integer payChannelCode = Integer.valueOf(requestMap.get("payChannel").toString());
		Integer clientSystem = Integer.valueOf(requestMap.get("clientSystem").toString());
		TransactionDto transaction = addTransactionFor3rd(requestMap,payChannelCode);
		requestMap.put("transactionId", transaction.getTransactionId());
		String outTradeNo = FieldGenerateUtil.generatebitOrderId(transaction.getTransactionId());
		
		TransactionDto updateTransaction = new TransactionDto();
		updateTransaction.setTransactionId(transaction.getTransactionId());
		updateTransaction.setOutTradeNo(outTradeNo);
		transactionDao.updateTransaction(updateTransaction);
		
		Long orderPayGroupId = System.currentTimeMillis();
		
		if (requestMap.get("redPackageMoney") != null) {
			@SuppressWarnings("unchecked")
			List<RedPacketPayInfo> redPacketPayList = (List<RedPacketPayInfo>)requestMap.get("redPackageIds");
			
			OrderDto orderDto = orderServcie.getOrderDtoById(transaction.getOrderId());
			Long userId = Long.valueOf(requestMap.get("userId").toString());
			for (RedPacketPayInfo redPacket : redPacketPayList) {
				
		        PayDto userRedPacketPay = getRedPacketPayDto(orderDto.getOrderId(), orderDto.getShopId(), userId,
		        									CommonConst.PAY_TYPE_SINGLE, CommonConst.PAY_TYPE_RED_PACKET, 
	        										redPacket.getRedPacketId(), redPacket.getPayAmount(),
	        										DateUtils.format(new Date(), null),orderPayGroupId.toString(),clientSystem);
		        
				if(userRedPacketPay.getPayAmount()>0){
					payDao.addOrderPay(userRedPacketPay);
				}
		        
			}
		}
		
		PayDto orderPay = addOrderPayFor3rd(requestMap,payChannelCode,orderPayGroupId.toString(),clientSystem);
		requestMap.put("orderPayId", orderPay.getOrderPayId());
		requestMap.put("out_trade_no", outTradeNo);
		String channelParameter = (String)requestMap.get("channelParameter");
		resultMap.put("transactionId", transaction.getTransactionId());
		
		if (subPayModel == 17)
			resultMap.put("dataType",1);
		else
			resultMap.put("dataType",2);
		
		resultMap.put("channelData", 
				getThirdPayChannelData(requestMap, channelParameter,subPayModel));
		return resultMap;
	}
	
	
	/**
	 * 处理第三方支付异步回调结果
	 * @param payResult
	 * @throws Exception
	 */
	@Override
	public void dealPayBy3rdNotify(PayNotifyResult payResult) throws Exception {
		buildNotifyAttchInfo(payResult);
		String payChannel = getPayChannel(payResult.getPayChannel());
		if (transactionIsFinish(payResult,payChannel)) {
			logger.info(payChannel+"支付回调以通知，不在处理回调，transactionId:"+
					payResult.getAttach().get("transactionId"));
            return;
		}
		TransactionDto transaction = updateTransactionAfterPaySuccess(payResult,payChannel);
		PayDto orderPay = updateOrderPayAfterPaySuccess(payResult,payChannel);
		if (orderPay.getGroupId() != null) {
			OrderDto orderDto = orderServcie.getOrderDtoById(orderPay.getOrderId());
			List<PayDto> orderPayGroup = payDao.getOrderPayByIdAndGroupId(orderPay.getOrderId(), orderPay.getGroupId());
			for (PayDto redPacketPay : orderPayGroup) {
				Long redPacketId = redPacketPay.getPayId();
				Double payAmount = redPacketPay.getPayAmount();
				RedPacket redPacket = packetDao.queryRedPacketById(redPacketId);
				useRedPacket(orderDto, redPacketPay,redPacket, payAmount, transaction.getUserId(),payResult.getTimeEnd());
			}
		}
		
		Integer payReason = payResult.getPayReason();
		//用户充值
		if (payReason == 1) {
			dealUserChargeFor3rd(transaction,payChannel,getUserBillType(payResult.getPayChannel()));
			pushRechargeInfo2Target(payResult,transaction,1,payChannel);
		}
		//商铺充值
		else if (payReason == 2) {
			dealShopChargeFor3rd(transaction,payChannel);
		}
		//购买插件
		else if (payReason == 5) {
			dealShopPluginFor3rd(transaction, payResult.getPayChannel());
		}
		//消费订单
		else {
			dealOrderPayFor3rd(transaction,orderPay,payReason,payResult);
		}
			
	}
	
	private PayDto getRedPacketPayDto(String orderId, Long shopId, Long userId,
									Integer orderPayType, 
									Integer payType,Long payId,
									Double payAmount, String nowTime,String groupId,Integer clientSystem) {
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
		payDto.setPayStatus(CommonConst.TRANSACTION_STATUS_WAIT);
		payDto.setGroupId(groupId);
		payDto.setClientSystem(clientSystem);
		return payDto;
	}
	
    private void useRedPacket(OrderDto order,PayDto redPacketPay, RedPacket redPacket, Double amount, Long userId,String payTime) throws Exception {
        Long redPacketId = redPacket.getRedPacketId();
    	updateRedPacketPay(redPacketPay, payTime);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("redPacketId", redPacketId);
        if (NumberUtil.sub(redPacket.getAmount(), amount) > 0) {
        	param.put("status", RedPacketStatusEnum.USEABLE.getValue());
        }else {
        	 param.put("status", RedPacketStatusEnum.USED.getValue());
        }
        param.put("payAmount", -amount);
        packetDao.updateRedPacketFlag(param);
        logger.error("使用红包 id:"+redPacket.getRedPacketId());
        UserBillDto userBillDto = BillUtil.buildUserBillForRedPacket(order, redPacket, amount, true);
        userBillDao.insertUserBill(userBillDto);
        PlatformBillDto billDto = BillUtil.buildPlatformBill(order, amount, CommonConst.PLT_BILL_MNY_SOURCE_HB, CommonConst.PLATFORM_BILL_TYPE_R_RED_PACKET, "红包支付", true);
        billDto.setBillDesc("用户使用红包支付");
        billDto.setRedPacketId(redPacketId);
        platformBillDao.insertPlatformBill(billDto);
    }
    
    private void updateRedPacketPay(PayDto redPacketPay,String payTime) throws Exception {
    	
    	redPacketPay.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
    	redPacketPay.setOrderPayTime(payTime);
    	redPacketPay.setPayStatus(CommonConst.TRANSACTION_STATUS_FINISH);
		payDao.updateOrderPayAfterRdPaySuccess(redPacketPay);
	
    }
    
	private void dealShopPluginFor3rd(TransactionDto transaction,Integer channelCode) throws Exception {
		Integer shopPluginId = Integer.valueOf(transaction.getOrderId());
		shopPluginDao.updateShopPluginAfterPaySuccess(shopPluginId);
		pluginService.insertPlatformBillForShopPlugin(transaction, getPlatformMoneySource(channelCode), "店铺购买插件");
	}
	
	private Boolean transactionIsFinish(PayNotifyResult payResult,String payChannel) throws Exception{
		Map<String, Object> attach = payResult.getAttach();
		Long transactionId = Long.valueOf(attach.get("transactionId").toString());
		logger.info(payChannel+"回调查询交易是否处理--start---transactionId:"+transactionId);
		Boolean isFinish = false;
		try {
			TransactionDto transaction = transactionDao.getDBTransactionById(transactionId);
			if (transaction== null || transaction.getStatus() == CommonConst.TRANSACTION_STATUS_FINISH)
				isFinish = true;
		} catch (Exception e) {
			logger.error(payChannel+"回调查询交易是否处理-transactionId:"+transactionId,e);
			throw new ValidateException(CodeConst.CODE_SQL_ERROR, payChannel+"回调查询交易是否处理错误");
		}
		return isFinish;
	}

	private void dealOrderPayFor3rd(TransactionDto transaction,PayDto orderPay,Integer payReason,PayNotifyResult payResult) throws Exception {
		String orderId = transaction.getOrderId();
		OrderDto order = orderDao.getOrderById(orderId);
		String payChannel = getPayChannel(payResult.getPayChannel());
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
		if((CommonConst.ORDER_STS_YJZ == orderStatus&&CommonConst.REVERSE_SETTLE_FLAG!=order.getSettleFlag().intValue()) || CommonConst.ORDER_STS_TDZ == orderStatus 
				 || CommonConst.ORDER_STS_YTD == orderStatus){
			//订单状态为不可支付状态
			throw new ValidateException(CodeConst.CODE_ORDER_STATUS_ERROR,CodeConst.MSG_ORDER_STATUS_ERROR);
		}
		// 获取订单实际需要支付的金额
		BigDecimal amount = packetDao.queryOrderAmount(orderId);
		// 获取订单实际已经支付的金额
		BigDecimal payAmount = packetDao.queryOrderPayAmount(orderId,transaction.getOrderPayType());
		int settleFlag = 0;
		if (payAmount.doubleValue() >= amount.doubleValue()) {
			logger.info("开始修改订单状态");
			order.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
			if (orderPay.getAutoSettleFlag() == CommonConst.AUTO_SETTLE_FLAG_TRUE) {
				order.setOrderStatus(CommonConst.ORDER_STATUS_FINISH);
			    Integer orderType = order.getOrderType()==null ? CommonConst.ORDER_TYPE_ALL_PRICE : order.getOrderType();
			    logger.info("订单类型orderType:"+ orderType);
				if (CommonConst.ORDER_TYPE_PAY_PAIMARY_AGENT == orderType ||
						CommonConst.ORDER_TYPE_PAY_MIDDLE_AGENT == orderType || 
						CommonConst.ORDER_TYPE_PAY__PAIMARY_UPGRADE_MIDDLE_AGENT == orderType) {
					
					settleFlag = 3;
					AgentDto agent = new AgentDto();
					agent.setUserId(transaction.getUserId());
					agent.setAgentShareRatio(Double.valueOf(0));
					String userRemark = order.getUserRemark();
					if (StringUtils.isNotBlank(userRemark)) {
						agent.setReferUserId(Long.valueOf(userRemark));
					}
					
					UserDto userDto = memberService.getUserByUserId(transaction.getUserId());

					if (StringUtils.isNotBlank(userRemark)) { 
					    Long referUserIdOld = userDto.getReferUserId();
	                    UserDto referUser = memberService.getUserByUserId(referUserIdOld);
	                    Long referUserId = Long.valueOf(userRemark);
	                    if(referUser == null || ("normal_ratio").equals(referUser.getRebatesLevel())) {
	                        userDto.setReferUserIdOld(referUserIdOld);
	                        userDto.setReferUserId(referUserId);
	                    } else if (("normal_ratio").equals(userDto.getRebatesLevel())) {
                            userDto.setReferUserIdOld(userDto.getReferUserId());
                            userDto.setReferUserId(referUserId);
	                    }
                    }
					
//					if (("normal_ratio").equals(userDto.getRebatesLevel())) {
//					    if (StringUtils.isNotBlank(userRemark)) {
//					        Long referUserId = Long.valueOf(userRemark);
//					        userDto.setReferUserIdOld(userDto.getReferUserId());
//					        userDto.setReferUserId(referUserId);
//					    }
//						if (StringUtils.isNotBlank(userRemark)) {
//							Calendar cal = Calendar.getInstance();
//							cal.set(Calendar.YEAR, 2016);
//							cal.set(Calendar.MONTH, Calendar.OCTOBER);
//							cal.set(Calendar.DAY_OF_MONTH, 1);
//							cal.set(Calendar.HOUR_OF_DAY, 0);
//							cal.set(Calendar.MINUTE, 0);
//							cal.set(Calendar.SECOND, 0);
//							Date registerChangeTime = cal.getTime();
//							
//							Long referUserId = Long.valueOf(userRemark);
//							if (userDto.getCreateTime().before(registerChangeTime)) {  //2016年10月1号之前 用户购买经销商身份直接替换用户原有推荐人
//								userDto.setReferUserId(referUserId);
//							} else { //2016年10月1号之后 用户购买经销商身份 如果用户原有推荐人是normal则替换成购买经销商时的推荐人
//								if (userDto.getReferUserId() != null) {
//									UserDto referUserDto = memberService.getUserByUserId(userDto.getReferUserId());
//									if (("normal_ratio").equals(referUserDto.getRebatesLevel())) {
//										userDto.setReferUserId(referUserId);
//									}
//								} else { 
//									userDto.setReferUserId(referUserId);
//								}
//						
//							}
//						}
	
//					}
					
					if (CommonConst.ORDER_TYPE_PAY_PAIMARY_AGENT == orderType) {
						agent.setAgentType(21);
						if(("normal_ratio").equals(userDto.getRebatesLevel())) {
						    userDto.setRebatesLevel("initial_ratio");
						}
					}else if (CommonConst.ORDER_TYPE_PAY_MIDDLE_AGENT == orderType) {
						agent.setAgentType(22);
						userDto.setRebatesLevel("middle_ratio");
					}else if (CommonConst.ORDER_TYPE_PAY__PAIMARY_UPGRADE_MIDDLE_AGENT == orderType) {
						agent.setAgentType(22);
						userDto.setRebatesLevel("middle_ratio");
					}else {
						agent.setAgentType(20);
					}
					agent.setSlottingFee(payAmount.doubleValue());
					agent.setCreateTime(new Date());
					agentDao.addAgent(agent);
					userDto.setIsDistributor(1);
					memberService.updateBaseInfo(userDto);
					DataCacheApi.del(CommonConst.KEY_USER + userDto.getUserId());
					commonService.agentInfoChange(agent.getAgentId(), agent.getUserId(), orderId);
				}
				else if(CommonConst.ORDER_TYPE_PAY_V_PRODUCT == orderType ){//TODO 购买V产品
					
					//1、先找店铺ID和推荐人ID
					String userRemark = order.getUserRemark();
					logger.info("购买v产品userRemark："+userRemark);
					if(StringUtils.isNotBlank(userRemark)){
						ThirdPayUserRemarkDto thirdPayUserRemarkDto = JacksonUtil.jsonToObject(userRemark, ThirdPayUserRemarkDto.class, null);
						Long  shopId = thirdPayUserRemarkDto.getShopId();
						//推荐人id
						Long  referUserId = thirdPayUserRemarkDto.getUserId();
						//产品类型
						Integer buyvTypeParam = thirdPayUserRemarkDto.getBuyvType();
						//支付总价
						Double totalFee = Double.valueOf(payResult.getTotalFee());
						
						ShopDto shopDB = shopDao.getShopById(shopId);
						if (shopDB != null) {
							order.setWxShopId(shopId);
							// 2、更新店铺购买V产品金额和推荐人
							Integer buyVType = getbuyVType(shopDB.getBuyvType(), buyvTypeParam);
							//购买总金额
							Double totalBuyVMoney = NumberUtil.add(shopDB.getBuyvMoney(), totalFee);
							ShopBean shopBean = new ShopBean();
							shopBean.setShopId(shopId);
							shopBean.setReferUserId(referUserId);
							shopBean.setBuyvMoney(totalBuyVMoney);
							shopBean.setBuyvType(buyVType); 
							shopDao.updateShop(shopBean);
							DataCacheApi.del(CommonConst.KEY_SHOP + shopId);


							// 3、返现操作
							// 调用commonService.checkForRebateForShopRegister(shopDto);
							shopDB.setReferUserId(referUserId);
							shopDB.setBuyvMoney(totalBuyVMoney);
							shopDB.setBuyvType(buyVType); 
							shopDB.setOrderId(orderId);
							shopDB.setTotalFee(totalFee);
							commonService.checkForRebateForShopRegister(shopDB);
					}
						
					}
					
				}
				else {
					storageService.insertShopStorageByOrderId(order.getOrderId(),order.getShopId());
					if (order.getIsMember() == 1) {
						Double sendMoney = packetService.sendRedPacketToUser(order);
					    if(sendMoney != 0) {
				                //更新订单结算价格
				             Double orderRealSettlePrice = NumberUtil.sub(order.getOrderRealSettlePrice(), sendMoney);
				              if (orderRealSettlePrice < 0) {
				                    orderRealSettlePrice = 0D;
				              }
				              order.setOrderRealSettlePrice(orderRealSettlePrice);
				              order.setSendRedPacketMoney(sendMoney);
				        }
					    settleFlag = 1;
					}
					else if (order.getIsMember() == 0) {
						orderServcie.generatePlatformBill(order,getPlatformMoneySource(payResult.getPayChannel()),orderPay.getPayAmount());
						orderServcie.generateShopMiddleBill(order,orderPay.getPayAmount());
						settleFlag = 2;
					}
					
					logger.debug("释放订单资源--orderId:{}",orderId);
					collectDao.updateShopResourceStatus(orderId,
							CommonConst.RESOURCE_STATUS_NOT_IN_USE);
				}
			}
			
			Date now = new Date();
			order.setServerLastTime(now.getTime());
			logger.debug("更新订单信息: {}",order);
			orderDao.updateOrder(order);
			
			orderServcie.updateGoodsAndShopSoldNum(orderId);
			
	        //插入反结账订单商品线上支付账单
			Integer orderType = order.getOrderType() == null ? CommonConst.ORDER_TYPE_ALL_PRICE : order.getOrderType();

			if(CommonConst.ORDER_TYPE_PAY_V_PRODUCT != orderType ){//TODO 购买V产品
		        payServcie.insertReverseShopBill(order);
			}
			
			OrderLogDto orderLogDto = new OrderLogDto();
			orderLogDto.setLastUpdateTime(now);
			orderLogDto.setOrderId(orderId);
			orderLogDto.setOrderStatus(order.getOrderStatus());
			orderLogDto.setPayStatus(CommonConst.PAY_STATUS_PAY_SUCCESS);
			orderLogDto.setUserId(0L);
			orderLogDto.setRemark(payChannel+"订单支付");
			logger.debug("更新订单日志: {}", orderLogDto);
			orderLogDao.saveOrderLog(orderLogDto);
			
		}
		
		if (payReason == 3) {
			dealMemberBill(transaction,orderPay,order,payResult.getPayChannel());
		}
		else if (payReason == 4) {
			dealNonMemberBill(transaction,orderPay,order,payResult.getPayChannel());
		}
		
		if (StringUtils.isBlank(order.getPayCode())) {
			pushMessage(transaction, orderPay, order, payReason,payChannel);
		}
		if(settleFlag == 1) {
            OrderGoodsSettleUtil.detailOrderGoodsSettle(transaction.getOrderId(), transaction.getOrderPayType());
            levelService.pushAddPointMessage(5, null, 1, order.getShopId().intValue(), 4, transaction.getOrderId(),true);
        } else if(settleFlag == 2) {
            orderServcie.handleAccountingStatByUser(order);
            OrderGoodsSettleUtil.detailSingleXorder(transaction.getOrderId());
        }
		
	}
	
	private Integer getbuyVType(Integer buyvTypeDB,Integer buyvTypeParam) {
		/*店铺购买V产品类型:0=一点结算V0(默认),1=一点管家V1=3000,2=一点云盒单屏机V2=6000,3=双屏机V3=9000*/	
		Integer buyVType = 0;
		if(buyvTypeDB<buyvTypeParam){
			buyVType = buyvTypeParam;
		}
		else{
			buyVType = buyvTypeDB;
		}
		return buyVType;
	}

	private Integer getPlatformMoneySource(Integer channelCode) {
		Integer moneySource = 0;
		if (channelCode == 0) {
			moneySource = CommonConst.PLT_BILL_MNY_SOURCE_CQB;
		}else if (channelCode == 1) {
			moneySource = CommonConst.PLT_BILL_MNY_SOURCE_ZFB;
		}else if (channelCode == 2) {
			moneySource = CommonConst.PLT_BILL_MNY_SOURCE_WX;
		}else if (channelCode == 3) {
			moneySource = CommonConst.PLT_BILL_MNY_SOURCE_JHK;
		}
		return moneySource;
	}
	
	
	private Integer getUserBillType(Integer channelCode) {
		Integer userBillType = 0;
		if (channelCode == 0) {
			userBillType = CommonConst.USER_BILL_TYPE_CASH_CARD;
		}else if (channelCode == 1) {
			userBillType = CommonConst.USER_BILL_TYPE_ALIPAY;
		}else if (channelCode == 2) {
			userBillType = CommonConst.USER_BILL_TYPE_WEIXIN;
		}else if (channelCode == 6) {
			userBillType = CommonConst.USER_BILL_TYPE_ABC_DEBIT;
		}
		return userBillType;
	}
	private void dealNonMemberBill(TransactionDto transaction,PayDto orderPay,OrderDto order,Integer channelCode) throws Exception {
		String payChannel = getPayChannel(channelCode);
		UserBillDto userChargeBill = buildUserBill(orderPay,order,null,"充值",1,4);
		
		userChargeBill.setMoney(transaction.getPayAmount());
		userChargeBill.setBillDesc(payChannel+"支付生成的非会员充值账单记录");
		userChargeBill.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
		userChargeBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
		// 需要设置交易号2015.9.30
		userChargeBill.setTransactionId(transaction.getTransactionId());
		//用户账单logo类型
		userChargeBill.setUserBillType(CommonConst.USER_BILL_TYPE_WEIXIN);
		//消费金
		userChargeBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
		//设置账单不可现
		userChargeBill.setIsShow(CommonConst.USER_BILL_NOT_IS_SHOW);
		userBillDao.insertUserBill(userChargeBill);
		
        //增加消费记录
        UserBillDto userBillDto=buildUserBill(orderPay,order,null,"消费",-1,4);
        userBillDto.setBillDesc(payChannel+"支付非会员订单");
        //截取小数4位
        userBillDto.setMoney(-transaction.getPayAmount());
        //消费
        userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
        //消费金
        userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
        userBillDto.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
        userBillDao.insertUserBill(userBillDto);
        
/*		//记录平台账单
		PlatformBillDto platformBillDto = buildPlatformBillFor3rdPay(orderPay.getUserId(),
				transaction.getPayAmount(), order, transaction.getTransactionId(), null, 
				"消费支付", CommonConst.BILL_DIRECTION_ADD,false,channelCode);
        platformBillDao.insertPlatformBill(platformBillDto);*/
	}
	
	private void dealMemberBill(TransactionDto transaction,PayDto orderPay,OrderDto order,Integer channelCode) throws Exception {
		String payChannel = getPayChannel(channelCode);
		UserAccountDto userAccount = userAccountDao.getAccountMoney(orderPay.getUserId());
		UserBillDto userChargeBill = buildUserBill(orderPay,order,null,"充值",1,3);
		
		userChargeBill.setAccountAmount(userAccount.getAmount());
		userChargeBill.setMoney(transaction.getPayAmount());
		userChargeBill.setAccountAfterAmount(NumberUtil.add(transaction.getPayAmount(), userAccount.getCouponAmount()));
		userChargeBill.setBillDesc(payChannel+"支付生成的充值账单记录");
		userChargeBill.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
		userChargeBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
		// 需要设置交易号2015.9.30
		userChargeBill.setTransactionId(transaction.getTransactionId());
		//用户账单logo类型
		userChargeBill.setUserBillType(CommonConst.USER_BILL_TYPE_WEIXIN);
		//消费金
		userChargeBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
		//设置账单不可现
		userChargeBill.setIsShow(CommonConst.USER_BILL_NOT_IS_SHOW);
		userBillDao.insertUserBill(userChargeBill);
		
        //增加消费记录
        UserBillDto userBillDto=buildUserBill(orderPay,order,null,"消费",-1,3);
        userBillDto.setBillDesc(payChannel+"支付会员订单");
        //截取小数4位
        userBillDto.setMoney(-transaction.getPayAmount());
        //消费
        userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
        //消费金
        userBillDto.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
        userBillDto.setAccountAfterAmount(userAccount.getCouponAmount());
        userBillDto.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
        userBillDto.setAccountAmount(userAccount.getAmount());
        userBillDao.insertUserBill(userBillDto);
        
		//记录平台账单
		PlatformBillDto platformBillDto = buildPlatformBillFor3rdPay(orderPay.getUserId(),
				transaction.getPayAmount(), order, transaction.getTransactionId(), userAccount, 
				"消费支付", CommonConst.BILL_DIRECTION_ADD,true,channelCode);
        platformBillDao.insertPlatformBill(platformBillDto);
	}
	
    private PlatformBillDto buildPlatformBillFor3rdPay(Long userId,Double money,OrderDto order,Long transactionId,
            UserAccountDto userAccount,String billType,Integer billDirection,Boolean isMember,Integer channelCode) throws Exception {
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
        if (order.getUserId() == null){
        	platformBill.setConsumerUserId(Long.valueOf(0));
        }else {
        	platformBill.setConsumerUserId(order.getUserId());
		}
        
        if (order.getMobile() == null){
        	platformBill.setConsumerMobile("0");
        }else {
        	platformBill.setConsumerMobile(order.getMobile());
		}
        platformBill.setMoneySource(getPlatformMoneySource(channelCode));
        return platformBill;
    }
	private UserBillDto buildUserBill(PayDto payDto,OrderDto order,UserAccountDto userAccount,
			String billType,Integer billDirection,Integer payReason) throws Exception{
	    int billStatus=order.getOrderStatus()+20;
		UserBillDto userBillDto=new UserBillDto();
		userBillDto.setBillType(billType);
		userBillDto.setBillDirection(billDirection);
		userBillDto.setBillStatus(billStatus);//已预订
		userBillDto.setUserBillType(CommonConst.USER_BILL_TYPE_CONSUME);
		//修改账单记录为负数
		userBillDto.setMoney(payDto.getPayAmount());
		userBillDto.setOrderId(payDto.getOrderId());
		userBillDto.setCreateTime(new Date());
		userBillDto.setBillDesc("消费");
		Long userId = payDto.getUserId();
		Long comsumerUserId = order.getUserId();
		userBillDto.setUserId(userId != null ? userId : Long.valueOf(0));
		userBillDto.setConsumerUserId(comsumerUserId != null ? comsumerUserId : Long.valueOf(0));
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
	private void dealUserChargeFor3rd(TransactionDto transaction,String payChannel,Integer billType) throws Exception {
		
		Long userId = transaction.getPayeeUserId();
        double paymount = transaction.getPayAmount();
	    UserChargeDto userCharge = new UserChargeDto();
        userCharge.setBillStatus(RechargeEnum.RECHARGE_SUCCESS.getValue());
        userCharge.setBillDesc(CommonConst.MEMBER_CHARGE);
        userCharge.setTransactionId(transaction.getTransactionId());
        userCharge.setAmount(paymount);
        //充值代表资金增加
        userCharge.setBillDirection(1);
        userCharge.setUserRole(CommonConst.MEMBER);
        userCharge.setCreateTime(new Date());
        userCharge.setBillType(CommonConst.CHARGE);
        userCharge.setUserId(userId);
        userCharge.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
        userCharge.setBillTitle(payChannel+"用户充值"+paymount+"元");
        userCharge.setAccountType(CommonConst.USER_ACCOUNT_TYPE_MONETARY);
        userCharge.setUserBillType(billType);
        
        UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
        // 充值前余额
        Double accountAmount =userAccount.getAmount();
        //充值后余额
        Double accountMoney = NumberUtil.add(userAccount.getCouponAmount(), paymount);
        userCharge.setAccountAmount(accountAmount);
        userCharge.setAccountAfterAmount(accountMoney);

        userMemberBillDao.saveChargeBill(userCharge);
		
		// 更新传奇宝账号余额
		userAccountDao.updateUserAccount(userId, paymount, null, null, paymount, null,
				null,null,null,null,null,null,null,null,null);
	}
	
	private void dealShopChargeFor3rd(TransactionDto transaction,String payChannel)throws Exception
    {
        // '用户id,当商铺提现时为商铺ID'
        Long userId = transaction.getPayeeUserId();
        // 更新商铺账户金额
        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(userId);

        if (shopAccountDto == null)
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺账号不存在，请检查1dcq_shop_account表是否存在shopId="
                    + transaction.getUserId());

        // 更新传奇宝账号余额, 防止账号余额为null
        Double amount = shopAccountDto.getAmount();
        if (amount == null){
            amount = 0d;
        }
        Double accountAfterAmount = shopAccountDto.getDepositAmount();
        double payAmount = transaction.getPayAmount();
        accountAfterAmount = NumberUtil.add(accountAfterAmount, payAmount);
        Integer arrearsFlag = null;
        if(accountAfterAmount >= 0)
        {
            arrearsFlag = CommonConst.ARREARS_FLAG_FALSE;
        }
        shopAccountDao.updateShopAccount(userId, payAmount, null, null, null, payAmount, null, arrearsFlag,null,null, null);
        
        // 插入商铺流水
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setTransactionId(transaction.getTransactionId());
        shopBillDto.setBillStatus(2); // 充值成功 更新商铺账单 成功 账单状态:处理中=1,成功=2,失败=3
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setShopId(userId);
        shopBillDto.setMoney(payAmount);
        shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_RECHARGE);//'账单类型:销售商品=1,支付保证金=2,购买红包=3,提现=4,充值=5,推荐奖励=6',
        shopBillDto.setBillDirection(1);//1代表增加
        shopBillDto.setAccountAmount(amount);
        shopBillDto.setBillDesc(payChannel+"生成的充值账单记录");
        shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT);//保证金
        shopBillDto.setAccountAfterAmount(accountAfterAmount);
        shopBillDto.setBillTitle(payChannel+"充值");
        shopBillDao.insertShopBill(shopBillDto);

        // 若充值后账户余额大于0，须更新商铺状态 商家状态:审核中-99,正常-0,下线-1,删除-2,欠费-3
        ShopDto shopDto = shopDao.getShopByIdWithoutCache(userId);
        if (null == shopDto)
        {
            logger.error("shopDao.getShopById()查询商铺不存在");
            throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_SHOP);
        }
        int status = shopDto.getShopStatus();
        logger.info("更新前商铺状态："+status+","+payChannel+"充值后保证金余额："+accountAfterAmount);
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
                logger.info("商铺"+payChannel+"充值推送消息到收银机start");
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
                logger.error("商铺"+payChannel+"充值推送消息到收银机失败" + "，商铺id：" + userId);
            }
        }
    }


	// 推送消息
	private void pushMessage(TransactionDto transaction,PayDto orderPay,OrderDto order,Integer payReason,String payChannel) {
        try
        {
        	if (payReason == 3) {
                // 支付成功，向用户推送消息，并将推送的消息记录
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("action", "pay");
                jsonObject.put("orderId", order.getOrderId());
                jsonObject.put("amount", transaction.getPayAmount());
                commonService.pushUserMsg(jsonObject, transaction.getUserId(), CommonConst.USER_TYPE_ZREO);
        	}
            logger.info("订单信息：orderId=" + order.getOrderId() + ",orderStatus=" + order.getOrderStatus()
                    + ",orderPayStatus=" + order.getPayStatus() + "。");
            // 订单状态为待确认、支付状态为已支付，向一点管家推送订单确认消息
            if (order.getOrderStatus() == CommonConst.ORDER_STS_DQR
                    && order.getPayStatus() == CommonConst.PAY_STATUS_PAYED)
            {
                commonService.pushShopUserMsg("confirmOrder", "您有新订单，请在10分钟内处理", order.getShopId());
            }
            
            Integer orderStatus = order.getOrderStatus();
            Long shopId = order.getShopId();
            if (null != shopId)
            {  
                //向店铺收银机推送
                BusMsg busMsg = new BusMsg();
                busMsg.setBusCode("THIRD_PAY_PUSH_SHOP_CASHIER");
                busMsg.setCreateTime(new Date());
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("order", order);
                params.put("payReason", payReason);
                busMsg.setParams(params);
                try
                {
                    busMsgSender.sendBusMsg(busMsg);
                }
                catch (Exception e)
                {
                    logger.error(e.getMessage(), e);
                }
                //保持原有业务，临时性质
                StringBuilder content = new StringBuilder();
                content.append("{");
                content.append("\"shopId\":" + shopId + ",");
                content.append("\"action\":\"cashOrder\",");
                content.append("\"data\":{\"id\":\"" + order.getOrderId() + "\",\"orderStatus\":" + orderStatus
                        + ",\"payStatus\":" + order.getPayStatus() + "}");
                content.append("}");
                PushDto push = new PushDto();
                push.setAction("cashOrder");
                push.setContent(content.toString());
                if (payReason == 3)
                	push.setUserId(transaction.getUserId());
                push.setShopId(shopId);
                pushService.pushInfoToShop2(push);
            }
            
            if (orderPay.getNotifyCashierMobile() != null) {
            	StringBuilder messageContent = new StringBuilder();
            	messageContent.append("用户支付成功").append("\n").append("支付金额：").append(orderPay.getPayAmount()).append("元");
    			sendSmsService.sendSms(orderPay.getNotifyCashierMobile(), messageContent.toString());
            }
        }
        catch (Exception e)
        {
            logger.error(payChannel+"支付支付消息推送异常", e);
        }
	}
	private void buildNotifyAttchInfo(PayNotifyResult payResult) throws Exception {
		String outTradeNo = payResult.getOutTradeNo();
		String transactionId = outTradeNo.substring(15);
		payResult.getAttach().put("transactionId", transactionId);
		Long orderPayId = payDao.getOrderPayIdByPayId(Long.valueOf(transactionId));
		payResult.getAttach().put("orderPayId", orderPayId);
		
	}
	/**
	 * 推送支付信息
	 * @param payResult
	 * @throws Exception
	 */
/*	private void pushPaymentInfo2Target(WeixinPayResult payResult) throws Exception {
		Map<String, Object> attach = payResult.getAttach();
		Integer payReason = Integer.valueOf(attach.get("payReason").toString());
		if (payReason == 1 || payReason == 2)
			//pushRechargeInfo2Target(payResult,payReason);
		else if (payReason == 3 || payReason == 4)
			pushOrderPayInfo2Target(payResult,payReason);
	}*/
	
	/**
	 * 推送充值信息
	 * @param payResult
	 * @param payReason
	 * @throws Exception
	 */
	private void pushRechargeInfo2Target(PayNotifyResult payResult,
			TransactionDto transaction,Integer payReason,String payChannel) throws Exception {
		Map<String, Object> attach = payResult.getAttach();
		JSONObject pushContent = new JSONObject();
		try {
			pushContent.put("transactionId", transaction.getTransactionId());
			pushContent.put("payStatus", transaction.getStatus());
			pushContent.put("paidTime", payResult.getTimeEnd());
			PushDto pushInfo = new PushDto();
			pushInfo.setAction("pushRechargeInfoOfWeixinPay");
			pushInfo.setTitle(payChannel+"充值支付通知");
			pushInfo.setContent(pushContent.toString());
			if (payReason == 1) {
				Long userId = Long.valueOf(transaction.getPayeeUserId());
				UserDto user = userDao.getUserById(userId);
				pushInfo.setUserId(userId);
				pushService.pushInfoToUser2(pushInfo, user.getUserType());
			}
			else if (payReason == 2) {
				pushInfo.setShopId(Long.valueOf(transaction.getPayeeUserId()));
				pushService.pushInfoToShop2(pushInfo);
			}
		} catch (Exception e) {
			logger.error(payChannel+"支付成功后推送充值信息失败-transactionId:"+attach.get("transactionId")+
					"-payReason:"+payReason+"-targetId:"+transaction.getPayeeUserId(),e);
			throw new ValidateException(CodeConst.CODE_PUSH_FAIL, payChannel+"支付成功后推送充值信息失败");
		}

	}
	
	private PayDto updateOrderPayAfterPaySuccess(PayNotifyResult payResult,String payChannel) throws Exception {
		PayDto orderPay = buildOrderPayOfPaySuccess(payResult);
		try {
			payDao.updateOrderPayAfterRdPaySuccess(orderPay);
		} catch (Exception e) {
			logger.error(payChannel+"支付成功更新orderPay记录错误-orderPayId:"+orderPay.getOrderPayId(),e);
			throw new ValidateException(CodeConst.CODE_SQL_ERROR, payChannel+"支付成功更新orderPay记录错误");
		}
		return orderPay;
	}
	private TransactionDto updateTransactionAfterPaySuccess(PayNotifyResult payResult,String payChannel) throws Exception {
		TransactionDto transaction = buildTransactionOfPaySuccess(payResult);
		try {
			transactionDao.updateTransactionAfterRdPaySuccess(transaction);
		} catch (Exception e) {
			logger.error(payChannel+"支付成功更新Transaction记录错误-transactionId:"+transaction.getTransactionId(),e);
			throw new ValidateException(CodeConst.CODE_SQL_ERROR, "微信支付成功更新Transaction记录错误");
		}
		return transaction;
	}
	
	private TransactionDto addTransactionFor3rd(Map<String, Object> requestMap,Integer payChannelCode) throws Exception {
		TransactionDto transactionDto = buildTransactionDtoOfWeixin(requestMap,payChannelCode);
		String payChannel = getPayChannel(payChannelCode);
		try {
			transactionDao.addTransaction(transactionDto);
		} catch (Exception e) {
			logger.error("申请"+payChannel+"预支付时增加Transaction记录错误", e);
			throw new ValidateException(CodeConst.CODE_SQL_ERROR, "申请"+payChannel+"预支付时增加Transaction记录错误");
		}
		return transactionDto;
	}
	
	private String getPayChannel(Integer channelCode) {
		if (channelCode ==1)
			return "支付宝";
		else if (channelCode == 2)
			return "微信";
		else if (channelCode == 3)
			return "建设银行";
		else if (channelCode == 6)
			return "农业银行";
		else
			return "其他第三方";
	}
	private PayDto addOrderPayFor3rd(Map<String, Object> requestMap,Integer payChannelCode,String groudId,Integer clientSystem) throws Exception {
		PayDto orderPay = buildOrderPayDtoOfWeixin(requestMap,payChannelCode,groudId,clientSystem);
		String payChannel = getPayChannel(payChannelCode);
		try {
			if(orderPay.getPayAmount()>0){
				payDao.addOrderPay(orderPay);
			}
		} catch (Exception e) {
			logger.error("申请"+payChannel+"预支付时增加orderPay记录错误", e);
			throw new ValidateException(CodeConst.CODE_SQL_ERROR, "申请"+payChannel+"预支付时增加orderPay记录错误");
		}
		return orderPay;
	}
	
	private PayDto buildOrderPayOfPaySuccess(PayNotifyResult payResult) throws Exception {
		Map<String, Object> attach = payResult.getAttach();
		String orderPayId = attach.get("orderPayId").toString();
		PayDto orderPay = payDao.getDBOrderPayById(Long.valueOf(orderPayId));
		orderPay.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		orderPay.setOrderPayTime(payResult.getTimeEnd()==null?DateUtils.format(new Date(),DateUtils.DATETIME_FORMAT):payResult.getTimeEnd());
		orderPay.setPayStatus(CommonConst.TRANSACTION_STATUS_FINISH);
		return orderPay;
	}
	
	private TransactionDto buildTransactionDtoOfWeixin(Map<String, Object> requestMap,Integer payChannel) throws Exception{
		TransactionDto transaction = new TransactionDto();
		transaction.setTerminalType(CommonConst.TERMINAL_TYPE_WEB);
		Object userId = requestMap.get("userId");
		if (userId != null)
			transaction.setUserId(Long.valueOf(userId.toString()));
		else
			transaction.setUserId(Long.valueOf(requestMap.get("shopId").toString()));
		
		Integer payReason = Integer.valueOf(requestMap.get("payReason").toString());
		String targetId = requestMap.get("targetId").toString();
		
		if (payReason == 1 || payReason == 2) {
			transaction.setPayeeUserId(Long.valueOf(targetId));
			transaction.setUserId(Long.valueOf(targetId));
			int transactionsType = payReason == 1 ? CommonConst.TRANSACTION_TYPE_USER :
				CommonConst.TRANSACTION_TYPE_SHOP;
			transaction.setTransactionType(transactionsType);
		}
		else if (payReason == 3 || payReason == 4) {
			if (payReason == 4) {
				transaction.setOrderType(2);//非会员订单
				transaction.setUserId(null);
			}
			transaction.setOrderId(targetId);
			transaction.setTransactionType(CommonConst.TRANSACTION_TYPE_ORDER);
		}
		else if (payReason == 5) {
			transaction.setUserId(Long.valueOf(requestMap.get("shopId").toString()));
			transaction.setOrderId(targetId);
			transaction.setTransactionType(CommonConst.TRANSACTION_TYPE_SHOP_PLUGIN);
		}
		transaction.setUserPayChannelId(getPayChannelId(payChannel));
		transaction.setRdOrgName(getPayChannel(payChannel));
		transaction.setPayAmount(Double.valueOf(requestMap.get("payAmount").toString()));
		transaction.setTransactionTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		transaction.setLastUpdateTime(transaction.getTransactionTime());
		transaction.setStatus(CommonConst.TRANSACTION_STATUS_WAIT);
		transaction.setOrderPayType(CommonConst.ORDER_PAY_TYPE_SINGLE);
		return transaction;
	}
	
	private Long getPayChannelId(Integer channelCode) {
		Integer channelId = 0;
		if (channelCode == 1) {
			channelId = CommonConst.PAY_CHANNEL_ALI;
		} else if (channelCode == 2) {
			channelId = CommonConst.PAY_CHANNEL_WEIXIN;
		} else {
			channelId = CommonConst.PAY_CHANNEL_OTHER;
		}
		return Long.valueOf(channelId);
	}
	private PayDto buildOrderPayDtoOfWeixin(Map<String, Object> requestMap,Integer payChannel,String groudId,Integer clientSystem) throws Exception {
		PayDto orderPay = new PayDto();
		Integer payReason = Integer.valueOf(requestMap.get("payReason").toString());
		String targetId = requestMap.get("targetId").toString();
		if (payReason == 3 || payReason ==4 || payReason ==5)
			orderPay.setOrderId(targetId);
		orderPay.setPayType(getPayType(payChannel));
		Long transactionId = Long.valueOf(requestMap.get("transactionId").toString());
		orderPay.setPayId(transactionId);
		orderPay.setPayAmount(Double.valueOf(requestMap.get("payAmount").toString()));
		orderPay.setOrderPayType(CommonConst.ORDER_PAY_TYPE_SINGLE);
		orderPay.setLastUpdateTime(DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
		orderPay.setPayeeType(CommonConst.PAYEE_TYPE_PLATFORM);
		if (requestMap.get("shopId") != null)
			orderPay.setShopId(Long.valueOf(requestMap.get("shopId").toString()));
		if (requestMap.get("userId") != null)
			orderPay.setUserId(Long.valueOf(requestMap.get("userId").toString()));
		orderPay.setPayChannel(getPayChannelId(payChannel).intValue());
		orderPay.setPayStatus(CommonConst.TRANSACTION_STATUS_WAIT);
		if (requestMap.get("autoSettleFlag") == null)
			orderPay.setAutoSettleFlag(CommonConst.AUTO_SETTLE_FLAG_FLASE);
		else
			orderPay.setAutoSettleFlag(Integer.valueOf(requestMap.get("autoSettleFlag").toString()));
		orderPay.setClientPayId(Integer.valueOf(requestMap.get("clientSystem").toString()));
		orderPay.setGroupId(groudId);
		orderPay.setClientSystem(clientSystem);
		if (requestMap.get("notifyCashierMobile") != null) {
			orderPay.setNotifyCashierMobile(requestMap.get("notifyCashierMobile").toString());
		}
		return orderPay;
	}
	
	private Integer getPayType(Integer channelCode) {
		Integer payType = 0;
		if (channelCode == 1) {
			payType = CommonConst.PAY_TYPE_ALI;
		}else if (channelCode == 2) {
			payType = CommonConst.PAY_TYPE_WXSCAN;
		}else {
			payType = CommonConst.PAY_TYPE_OTHER;
		}
		return payType;
	}
	private TransactionDto buildTransactionOfPaySuccess(PayNotifyResult payResult) throws Exception {
		
		Map<String, Object> attach = payResult.getAttach();
		String transactionId = attach.get("transactionId").toString();
		TransactionDto transaction = transactionDao.getDBTransactionById(Long.valueOf(transactionId));
		transaction.setStatus(CommonConst.TRANSACTION_STATUS_FINISH);
		transaction.setRdTransactionId(payResult.getTransactionId());
		if (payResult.getNotifyId() != null)
			transaction.setRdNotifyId(payResult.getNotifyId());
		transaction.setPayAmount(Double.parseDouble(payResult.getTotalFee()));
		Date now = new Date();
		transaction.setRdNotifyTime(DateUtils.format(now, DateUtils.DATETIME_FORMAT));
		transaction.setLastUpdateTime(DateUtils.format(now, DateUtils.DATETIME_FORMAT));
		return transaction;
	}

}
