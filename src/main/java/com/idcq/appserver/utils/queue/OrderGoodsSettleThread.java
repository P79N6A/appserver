package com.idcq.appserver.utils.queue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.common.ISendSmsRecordDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.pay.ITransaction3rdDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.SendSmsRecordDto;
import com.idcq.appserver.dto.message.PushDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.pay.Transaction3rdDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.ShopMemberLevel.IShopMemberLevelService;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.common.ISendSmsService;
import com.idcq.appserver.service.message.IPushService;
import com.idcq.appserver.service.order.IOrderServcie;
import com.idcq.appserver.service.settle.ISettleService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.jedis.DataCacheApi;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;

public class OrderGoodsSettleThread implements Runnable{
	
	private Logger logger=Logger.getLogger(OrderGoodsSettleThread.class);
	private String orderId;
	
	private String orderType;//订单类型，默认为空，非会员结算需要使用
	
	public OrderGoodsSettleThread(String orderId) {
		super();
		this.orderId = orderId;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(10000);
			IOrderServcie orderServcie = BeanFactory.getBean(IOrderServcie.class);
			IPayDao payDao = BeanFactory.getBean(IPayDao.class);
			//订单已支付金额
			Double sumPayAmount = payDao.getSumPayAmount(orderId, null);
			 // 当状态为已结账状态、全额付款、已支付状态才执行分账
            IOrderDao orderDao = BeanFactory.getBean(IOrderDao.class);
            OrderDto order = orderDao.getOrderById(orderId);
            if(order.getOrderStatus() != CommonConst.ORDER_STATUS_FINISH) {
                logger.error("订单状态未完成不允许结算：" + order.getOrderStatus());
                return;
            }
			if(sumPayAmount < order.getSettlePrice())
			{
			    logger.error("订单未完成支付，不进行结算：" + orderId);
                return;
			}
		
			if(orderType != null && orderType == CommonConst.ORDER_TYPE_XORDER){ //非会员结算
			    logger.info("执行分账线程start----非会员订单...");
				orderServcie.dealXorderSettle(order, null);
			}else{
				logger.info("执行分账线程start...");
				
				long start=System.currentTimeMillis();
				 //统计支付信息 
                orderServcie.handleAccountingStatByUser(order);
				// 商铺信息
				Long shopId = order.getShopId(); 
				IShopDao shopDao = BeanFactory.getBean(IShopDao.class);
				ShopDto shop = shopDao.getShopEssentialInfo(shopId);
				Integer shopStatus = shop.getShopStatus(); 
                IShopAccountDao shopAccountDao = BeanFactory.getBean(IShopAccountDao.class);
                //临时新增代码
                Double platformTotalIncomeRatio = shop.getPercentage();

                if (platformTotalIncomeRatio < 0D)
                {
                    throw new ServiceException(CodeConst.CODE_PARAMETER_NOT_EXIST, "店铺(" + shop.getShopName()
                            + ")订单的分成比例异常");
                }
                
				// 获取分成比例
				HashMap<String, Date> param = new HashMap<String, Date>();
				param.put("orderTime", order.getOrderFinishTime());
			
                //平台获得奖励
                Double platformTotalIncome = NumberUtil.multiply(platformTotalIncomeRatio, order.getOrderRealSettlePrice());
                order.setPlatformTotalIncome(platformTotalIncome);
                if (null != shop.getShopIdentity() && (shop.getShopIdentity() == 1 || shop.getShopIdentity() == 2)) {
                    platformTotalIncome = NumberUtil.multiply(platformTotalIncomeRatio, order.getSettlePrice());
                }
                //商铺账户
                ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
                //不允许欠费
                Double balance = account.getDepositAmount();
//                IShopConfigureSettingDao shopSettingDao = BeanFactory.getBean(IShopConfigureSettingDao.class);
//                String bailFlag = shopSettingDao.getShopConfigureSettingValue(shopId, CommonConst.SHOP_SETTING_TYPE_CHARGE, CommonConst.SHOP_SETTING_BAIL_FLAG);
                ICommonService commonService = BeanFactory.getBean(ICommonService.class);
                ConfigDto searchCondition = new ConfigDto();
                searchCondition.setBizId(shopId);
                searchCondition.setBizType(1);
                searchCondition.setConfigGroup("DEPOSIT_CONFIG");
                searchCondition.setConfigKey(CommonConst.SHOP_SETTING_BAIL_FLAG);
                ConfigDto config =  commonService.getConfigDto(searchCondition);
                String bailFlag = null == config ? null : config.getConfigValue();
//                String bailFlag = shopSettingDao.getShopConfigureSettingValue(shopId, CommonConst.SHOP_SETTING_TYPE_CHARGE, CommonConst.SHOP_SETTING_BAIL_FLAG);
                Double payAmount = payDao.getSumPayAmount(orderId, CommonConst.PAYEE_TYPE_PLATFORM);
                balance = NumberUtil.add(balance,payAmount) ;
                if(CommonConst.SHOP_SETTING_BAIL_FLAG_TRUE.equals(bailFlag))
                {
                    //开启自动充值的话，需要加上线上收入和奖励
                    balance = NumberUtil.add(balance,account.getRewardAmount(),account.getOnlineIncomeAmount()) ;
                }
                if(shopStatus != CommonConst.SHOP_STATUS_NORMAL || balance < platformTotalIncome)
                {//TODO 保证金不足也结算，从线上收入扣除
                    shopAccountDao.updateShopAccount(shopId, null, null, null, null, null, null, CommonConst.ARREARS_FLAG_TURE,null,null, null);
                    logger.error("保证金不足，店铺状态：" + shopStatus+"订单不进行分账结算："+ order.getOrderId());
                    return;
                }
				//判断结算流程是走3721还是普通结算
                if (null != shop.getShopIdentity() && (shop.getShopIdentity() == 1 || shop.getShopIdentity() == 2)) {
                    ISettleService settleService = BeanFactory.getBean(ISettleService.class);
                    if(order.getShopSettleFlag() == 1) {
                        logger.error("该订单已结算请勿重复结算，订单ID:" + order.getOrderId());
                        return;
                    }
                    settleService.settleOrder(order, shop);
                }else {
                    Map<String, Object> orderGoodsSettleSetting = orderDao.getOrderGoodsSettleSetting(param);
                    CommonValidUtil.validObjectNull(orderGoodsSettleSetting,CodeConst.CODE_PARAMETER_NOT_EXIST,CodeConst.MSG_ORDER_GOODS_SETTLE_ERROR);

                	//普通结算
    	            orderServcie.dealOrderSettle(order, orderGoodsSettleSetting);
                }
                
	            orderServcie.updatePlatformServePrice(order);//更新服务费
                //计算积分
                orderServcie.orderVantages(order);
	            
				logger.info("分账计算处理完成,共耗时:"+(System.currentTimeMillis()-start)+"ms");
				
				//自动充值
				Integer changeShopStatus = autoRecharge(shopAccountDao, shopDao, shop, shopStatus);
				// 增加短信推送，商铺余额低于20元推送，余额为0推送
				changeShopStatus = sendSmsByShop(shopAccountDao, shopDao, shop, order, changeShopStatus);
				//推送收银机
				if(shopStatus != null && !shopStatus.equals(changeShopStatus)) 
				{
				    //店铺状态充值前后有变动就推送给收银机
					pushMsg(shop, shopId, changeShopStatus);
				}
			}
			IShopMemberService shopMemberService = BeanFactory.getBean(IShopMemberService.class);
			shopMemberService.isShopMemberValify(order.getShopId(), order.getMobile(), BigDecimal.valueOf(order.getSettlePrice()), true);
			//店铺会员等
			if(order != null && StringUtils.isNotBlank(order.getMobile()) && order.getShopId() != null){
				logger.info("订单手机号码为："+order.getMobile());
				IShopMemberLevelService shopMemberLevelService = BeanFactory.getBean(IShopMemberLevelService.class);
				shopMemberLevelService.upgrateShopMemberLevel(order.getMobile(), order.getShopId(), 2);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * 自动充值功能
	 * @param shopId 
	 * @param shopAccountDao
	 * @author  shengzhipeng
	 */
    private Integer autoRecharge(IShopAccountDao shopAccountDao, IShopDao shopDao, ShopDto shop, Integer shopStatus) throws Exception
    {
        Long shopId = shop.getShopId();
        IShopBillDao shopBillDao = BeanFactory.getBean(IShopBillDao.class);
        ITransaction3rdDao iTransaction3rdDao = BeanFactory.getBean(ITransaction3rdDao.class);
        //店铺转充
//        IShopConfigureSettingDao shopSettingDao = BeanFactory.getBean(IShopConfigureSettingDao.class);
//        List<ShopConfigureSettingDto> shopSettings = shopSettingDao.queryShopConfigureSetting(shopId, CommonConst.SHOP_SETTING_TYPE_CHARGE);
//        ..
        ICommonService commonService = BeanFactory.getBean(ICommonService.class);
        ConfigDto searchCondition = new ConfigDto();
        searchCondition.setBizId(shopId);
        searchCondition.setBizType(1);
        searchCondition.setConfigGroup("DEPOSIT_CONFIG");
        List<ConfigDto> configs = commonService.queryConfigDto(searchCondition);
        if (CollectionUtils.isNotEmpty(configs)) 
        {
            String bailFlag = CommonConst.SHOP_SETTING_BAIL_FLAG_FALSE; //默认关闭
            Double bailAlterAmount = CommonConst.SHOP_SETTING_BAIL_ALTER_AMOUNT_VALUE; //默认50
            Double bailAmount = CommonConst.SHOP_SETTING_BAIL_AMOUNT_VALUE; //默认300
            if(CommonConst.IS_SING_ONLINE == shop.getSign()) { 
                //线上签约初始值不一样
                bailAlterAmount = CommonConst.ONLINE_SETTING_BAIL_ALTER_AMOUNT_VALUE;
                bailAmount = CommonConst.ONLINE_SETTING_BAIL_AMOUNT_VALUE;
            } 
            for (ConfigDto tempConfig : configs)
            {
                //获取自动充值配置
                String key = tempConfig.getConfigKey();
                String value = tempConfig.getConfigValue();
                if(StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value) ) 
                {
                    if(CommonConst.SHOP_SETTING_BAIL_FLAG.equals(key))
                    {
                        bailFlag = value; 
                    } 
                    else if(CommonConst.SHOP_SETTING_BAIL_ALTER_AMOUNT.equals(key)) 
                    {
                        bailAlterAmount = Double.valueOf(value); 
                    } 
                    else if (CommonConst.SHOP_SETTING_BAIL_AMOUNT.equals(key))
                    {
                        bailAmount = Double.valueOf(value); 
                    }
                }
            }
            //商铺账户
            ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
            
            //账户总金额
            Double accountAmount = account.getAmount();
          
            //保证金余额
            Double depositAmount = account.getDepositAmount();
            
            //线上营业收入余额
            Double onlineIncomeAmount = account.getOnlineIncomeAmount();
            
            //平台奖励余额
            Double rewardAmount = account.getRewardAmount();
            if (CommonConst.SHOP_SETTING_BAIL_FLAG_TRUE.equals(bailFlag) && depositAmount < bailAlterAmount)
            {
                //开启了自动充值功能且保证金小于开始充值金额，走自动转充流程
                //需要充值的金额
                Double needRechargeAmount = NumberUtil.sub(bailAmount, depositAmount);
                Double deductionOnlineAmount = 0D; //扣减的营业收入金额
                Double deductionRewardAmount = 0D; //扣减的平台奖励金额
                Double addDepositAmount = 0D; //新增的保证金金额
                logger.info("店铺开启自动充值功能，需要充值的金额："+ needRechargeAmount + "线上营业收入余额" + onlineIncomeAmount + "奖励余额：" + rewardAmount);
               //需要充值的金额小于0 直接返回商铺状态
                if(needRechargeAmount<=0){
                   return shopStatus;
               }
                if(onlineIncomeAmount < needRechargeAmount) 
                {
                    //还需要充值的金额
                    needRechargeAmount = NumberUtil.sub(needRechargeAmount, onlineIncomeAmount); 
                    if(rewardAmount < needRechargeAmount)
                    {
                        //奖励的钱也不够充值就直接充完
                        deductionRewardAmount = rewardAmount;
                    } 
                    else 
                    {
                        deductionRewardAmount = needRechargeAmount;
                    }
                    deductionOnlineAmount = onlineIncomeAmount;
                    addDepositAmount = NumberUtil.add(deductionOnlineAmount, deductionRewardAmount); //保证金新增等于=奖励减少的+收入减少的
                } 
                else 
                {
                    deductionOnlineAmount = needRechargeAmount;
                    addDepositAmount = deductionOnlineAmount;
                }
                
                if(0 != deductionOnlineAmount || 0 != deductionRewardAmount || 0 != addDepositAmount) 
                {
                    //更新账户余额
                    shopAccountDao.updateShopAccount(shopId, null, -deductionOnlineAmount, -deductionRewardAmount, null, addDepositAmount, null,
                    		CommonConst.ARREARS_FLAG_FALSE,null,null, null);
                    Transaction3rdDto transaction3rdDto = new Transaction3rdDto();
                    transaction3rdDto.setUserId(shopId); //商铺id
                    
                    transaction3rdDto.setStatus(CommonConst.TRANSACTION_STATUS_FINISH);
                    transaction3rdDto.setTerminalType("系统自动");
                    transaction3rdDto.setPayAmount(needRechargeAmount);
                    transaction3rdDto.setTransactionTime(new Date());
                
                    iTransaction3rdDao.payBy3rd(transaction3rdDto);
                    Long transactionId = transaction3rdDto.getTransactionId();
                    Double useAmoutTotal = NumberUtil.add(deductionRewardAmount, deductionOnlineAmount);
                    if(0 != deductionOnlineAmount) 
                    {
                        //记账单 营业收入 
                        insertShopBill(shopId, shopBillDao, accountAmount, onlineIncomeAmount, -deductionOnlineAmount,
                                transactionId, CommonConst.SHOP_ACCOUNT_TYPE_INCOME, useAmoutTotal);
                    }
                    if(0 != deductionRewardAmount) 
                    {
                        //平台奖励账单
                        insertShopBill(shopId, shopBillDao, accountAmount, rewardAmount, -deductionRewardAmount, transactionId,
                                CommonConst.SHOP_ACCOUNT_TYPE_REWARD, useAmoutTotal);
                    }
                    // 保证金账单
                    insertShopBill(shopId, shopBillDao, accountAmount, depositAmount, addDepositAmount, transactionId, CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT,useAmoutTotal);
                }
                if(depositAmount < 0 && NumberUtil.add(depositAmount, addDepositAmount) > 0 && shopStatus == CommonConst.SHOP_LACK_MONEY_STATUS)
                {
                    //如果保证金开始金额小于0，充值后金额大于0,充值前状态又是欠费状态
                    shopStatus = modifyShopStatus(shopDao, shopId, CommonConst.SHOP_NORMAL_STATUS);
                }
            }
        }
        return shopStatus;
    }

	/**
	 * 记录商铺账单
	 * 
	 * @param shopId 商铺ID
	 * @param shopBillDao 底层DAO
	 * @param accountAmount 账户总余额
	 * @param beforeAmount 单个账户使用前余额
	 * @param useAmount 单个账户使用余额
	 * @param transactionId 交易流水号
	 * @param accountType 账户类型
	 * @throws Exception 
	 * @author  shengzhipeng
	 */
    private void insertShopBill(Long shopId, IShopBillDao shopBillDao, Double accountAmount,
            Double beforeAmount, Double useAmount, Long transactionId, int accountType, Double useAmoutTotal) throws Exception
    {
        ShopBillDto shopBillDto = new ShopBillDto();
        shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_TRANSFER);
        if(useAmount != null && useAmount < 0) 
        {
            shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
        }
        else 
        {
            shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
        }
        shopBillDto.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
        shopBillDto.setMoney(useAmount); //使用金额，新增为正，减少为负
        shopBillDto.setAccountAmount(accountAmount);//处理前账户总余额
        shopBillDto.setAccountAfterAmount(NumberUtil.add(beforeAmount, useAmount));//处理后对应账户的余额
        shopBillDto.setAccountType(accountType);
        shopBillDto.setCreateTime(new Date());
        shopBillDto.setSettleTime(new Date());
        shopBillDto.setShopId(shopId);
        shopBillDto.setBillDesc("店铺自动转充");
        shopBillDto.setTransactionId(transactionId);
        if(accountType == CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT) {
            shopBillDto.setBillTitle("余额自动转充"+  NumberUtil.formatDoubleStr(useAmoutTotal, 2));
        } else {
            shopBillDto.setBillTitle("自动转到保证金"+  NumberUtil.formatDoubleStr(useAmoutTotal, 2));
        }
        shopBillDao.insertShopBill(shopBillDto);
    }
	/**
	 * 
	 * @Description:推送消息给收银机
	 *
	 * @param shop
	 * @param shopId
	 * @param shopStatus
	 */
    private void pushMsg(ShopDto shop, Long shopId, Integer shopStatus)
    {
        try
        {
            DataCacheApi.del(CommonConst.KEY_SHOP + shopId);
            logger.info("店铺状态出现异常，向收银机推送消息");
            IPushService pushService = BeanFactory.getBean(IPushService.class);
            // 推送收银机
            JSONObject pushTarget = new JSONObject();
            pushTarget.put("action", CommonConst.ACTION_SHOP_DATA_UPDATE);
            pushTarget.put("shopId", shopId);
            pushTarget.put("lastUpdate", DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT));
            PushDto push = new PushDto();
            push.setShopId(shopId);
            push.setAction(CommonConst.ACTION_SHOP_DATA_UPDATE);
            pushTarget.put("shopStatus", shopStatus);
            if (shopStatus == CommonConst.SHOP_LACK_MONEY_STATUS)
            {
                pushTarget.put("shopDeadTime", DateUtils.format(DateUtils.addDays(shop.getLastUpdateTime(), 7), DateUtils.DATETIME_FORMAT));
            }
            push.setContent(pushTarget.toString());
            pushService.pushInfoToShop2(push);
        }
        catch (Exception e)
        {
            logger.info("店铺状态出现异常，向收银机推送消息出现异常：" + e.toString());
        }

    }
	/**
	 * 发送短信给商铺
	 * 
	 * @Function: com.idcq.appserver.utils.queue.OrderGoodsSettleThread.sendSmsByShop
	 * @Description:
	 *
	 * @param shopId
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年11月4日 上午11:03:02
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年11月4日    ChenYongxin      v1.0.0         create
	 */
	private Integer sendSmsByShop(IShopAccountDao shopAccountDao, IShopDao shopDao, ShopDto shop, OrderDto order, Integer changeShopStatus) throws Exception{
	    Long shopId = shop.getShopId();
	    try {
			logger.info("发送短信给商铺start");
			ISendSmsRecordDao sendSmsRecordDao = BeanFactory.getBean(ISendSmsRecordDao.class);
			ISendSmsService sendSmsService = BeanFactory.getBean(ISendSmsService.class);
			//发送短信给消费者,平台收款大于1元才发生短信通知
			SmsReplaceContent src = new SmsReplaceContent();
			if(NumberUtil.multiply(order.getOrderRealSettlePrice(), shop.getPercentage()) > 1D
			        && (shop.getShopIdentity() != null && shop.getShopIdentity() == 0)) {
			    src.setMobile(getMobileById(order.getUserId()));
	            src.setUsage(CommonConst.REBATE_CONTENT);
	            sendSmsService.sendSmsMobileCode(src); 
			}
			//商铺账户
			ShopAccountDto account = shopAccountDao.getShopAccount(shopId);
			if(account != null){
				//保证金余额
				Double depositAmount = account.getDepositAmount();
				//手机号码
				String mobile = getMobileById(shop.getPrincipalId());
				logger.info("商铺ID："+shopId+"手机号码："+mobile+"，保证金余额："+depositAmount);
				//当前时间
				String nowDateStr = DateUtils.format(new Date(), DateUtils.DATETIME_FORMAT1);
			    // 短信通知
                src.setConsumeDate(nowDateStr);
                src.setAcountAmount(depositAmount); // 保证金
                src.setMobile(mobile);
				//商铺余额小于等于20元大于0元的时候
				if(depositAmount >= 0 && depositAmount <= 20 && shop.getPercentage() > 0){
	                src.setUsage(CommonConst.NEARLY_NOT_ENOUGH);
					//上一次发送的短信
					SendSmsRecordDto sendSmsDB = sendSmsRecordDao.getSendContentByMobileAndUsage(mobile, CommonConst.NEARLY_NOT_ENOUGH);
					//余额即将不足短信发送内容
					if(sendSmsDB != null)
					{
						Date lastSendTime = sendSmsDB.getSendTime();
						int days = lastSendTime.compareTo(new Date());
						logger.info("短信发送间隔天数："+days);
						//2天发送一次
						if(days==2)
						{
			                 sendSmsService.sendSmsMobileCode(src);
							//发送短信
						}
					}
					else
					{//第一次发送
						logger.info("上一次短信在数据库不存在，发送短信...");
						 sendSmsService.sendSmsMobileCode(src);
					}
				}
				//店铺余额不足
				else if(depositAmount < 0)
				{
				    // 需要修改店铺状态为欠费
				    changeShopStatus = modifyShopStatus(shopDao, shopId, CommonConst.SHOP_LACK_MONEY_STATUS);
				    shopAccountDao.updateShopAccount(shopId, null, null, null, null, null, null, CommonConst.ARREARS_FLAG_TURE,null,null, null);
				    src.setUsage(CommonConst.NOT_ENOUGH);
			        src.setAcountAmount(Math.abs(depositAmount)); // 保证金
					//上一次发送的短信
					SendSmsRecordDto sendSmsDB = sendSmsRecordDao.getSendContentByMobileAndUsage(mobile, CommonConst.NOT_ENOUGH);
					//余额已经不足短信内容
					//余额即将不足短信发送内容
					if(sendSmsDB!=null)
					{
						Date lastSendTime = sendSmsDB.getSendTime();
						int days = lastSendTime.compareTo(new Date());
						//2天发送一次
						if(days==2)
						{
							//发送短信
						    sendSmsService.sendSmsMobileCode(src);
						}
					}
					else
					{//第一次发送
					    sendSmsService.sendSmsMobileCode(src);
					}
				}
			}
		} catch (Exception e) {
			logger.error("发送短信给店铺异常，shopId:"+shop);
		}
        return changeShopStatus;
	}

    private Integer modifyShopStatus(IShopDao shopDao, Long shopId, Integer shopStatus) throws Exception
    {
        ShopDto shopDto = new ShopDto();
        shopDto.setShopStatus(shopStatus);
        shopDto.setShopId(shopId);
        shopDao.updateShopStatus(shopDto);
        DataCacheApi.del(CommonConst.KEY_SHOP + shopId);
        return shopStatus;
    }
	/**
	 * 根据userid查询手机号码
	 */
	public String getMobileById(Long userId) throws Exception{
		IUserDao userDao = BeanFactory.getBean(IUserDao.class);
		UserDto user = userDao.getDBUserById(userId);
		String mobile = "";
		if(null!=user){
			mobile = user.getMobile();
		}
		return mobile;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	
}

