package com.idcq.appserver.service.settle;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.bill.IPlatformBillDao;
import com.idcq.appserver.dao.bill.IShopBillDao;
import com.idcq.appserver.dao.order.IOrderDao;
import com.idcq.appserver.dao.orderSettle.IOrderSettleDao;
import com.idcq.appserver.dao.pay.IPayDao;
import com.idcq.appserver.dao.shop.IShopAccountDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.user.IAgentDao;
import com.idcq.appserver.dao.user.IUserAccountDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.order.OrderSettleDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.AgentDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.service.bill.IPlatformBillService;
import com.idcq.appserver.service.bill.IUserBillService;
import com.idcq.appserver.service.common.ICommonService;
import com.idcq.appserver.service.pay.IPayServcie;
import com.idcq.appserver.service.shop.IShopBillService;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.smsutil.SmsReplaceContent;
@Service
public class SettleOrderImpl implements ISettleService {
    
    private static final Logger logger = Logger.getLogger(SettleOrderImpl.class);
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IShopDao shopDao;
    @Autowired
    private IShopAccountDao shopAccountDao;
    @Autowired
    private IPlatformBillService platformBillService;
    @Autowired
    private IUserBillService userBillService;
    @Autowired
    private IUserDao userDao;
    @Autowired
    private IShopBillService shopBillService;
    @Autowired
    private IUserAccountDao userAccountDao;
    @Autowired
    private IAgentDao agentDao;
    @Autowired
    private IOrderDao orderDao;
    @Autowired
    private IPayDao payDao;
    @Autowired
    private IPlatformBillDao  platformBillDao;
    @Autowired
    private IShopBillDao shopBillDao;
    @Autowired
    private  IOrderSettleDao orderSettleDao;
    @Autowired
    private  IPayServcie payService;    
    
    public void settleOrder(OrderDto order, ShopDto shop) throws Exception {

        //3721结算方案
        String orderId = order.getOrderId();
        Long userId = order.getUserId();
        Long shopId = order.getShopId();
        Double settlePrice = order.getSettlePrice();
        
    	//线上账户金额转到保证金账户
//        payService.dealDepositAmount(shopId, order.getPlatformTotalIncome());
        
        Map<String, Object> configMap = commonService.getConfigByGroup("GROUP_3721");
        logger.info("返点比例配置：" + configMap.toString());
        //计算平台税率
        Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
        Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
        //记录平台收益比例
        Object platformRevenueRatioConfig = configMap.get("platform_revenue_ratio");
        Double platformRevenueRatio = platformRevenueRatioConfig == null ? 0 : Double.valueOf(platformRevenueRatioConfig.toString());
        //店铺最低的返点比例
        Object rebatesStartDiscountConfig = configMap.get("c_rebates_start_discount");
        Double rebatesStartDiscountRatio = rebatesStartDiscountConfig == null ? 0 : Double.valueOf(rebatesStartDiscountConfig.toString());
        
        Double percentage = shop.getPercentage();
        OrderSettleDto orderSettleDto = new OrderSettleDto();
        orderSettleDto.setOrderId(orderId);
        if (userId != null && percentage.compareTo(rebatesStartDiscountRatio) >= 0) {
            UserDto orderUser = userDao.getDBUserById(userId);
            orderSettleDto.setUser_id(userId);
            orderSettleDto.setUser_name(orderUser.getMobile());
            if (orderUser != null) {
                //计算会员收益
                String rebatesLevel = orderUser.getRebatesLevel();
                Object userRebateRatioConfig = configMap.get(rebatesLevel);
                Double userRebateRatio = userRebateRatioConfig == null ? 0 : Double.valueOf(userRebateRatioConfig.toString());
                Double consumeInB2RebatesRatio = 1D;
                Integer shopIdentity = shop.getShopIdentity();
                if(null != shopIdentity && shopIdentity == 2 ) {
                    Object consumeInB2RebatesConfig = configMap.get("c_consume_in_b2_rebates");
                    consumeInB2RebatesRatio = consumeInB2RebatesConfig == null ? 0 : Double.valueOf(consumeInB2RebatesConfig.toString());
                }
                Double userRebateAmount = NumberUtil.multiply(settlePrice, consumeInB2RebatesRatio, userRebateRatio);
               
                //平台扣税,因为全返里面包含一部分金额不扣税
//                userRebateAmount = NumberUtil.multiply(userRebateAmount, 1 - platformTaxDeductionRatio);
                
                logger.info("会员消费扣税前的总收益：" + userRebateAmount);
                userAccountSettle(order, configMap, userId, rebatesLevel, userRebateAmount,
                                CommonConst.USER_BILL_TYPE_CONSUMER_REBATE, false, true, shopIdentity);
                
                orderSettleDto.setUser_share_price(userRebateAmount);
//                orderSettleDto.setUser_share_ratio(userRebateRatio);
                
                //计算会员推荐人收益
                Long referUserId = orderUser.getReferUserId();
                if (referUserId != null) {
                    UserDto referUser = userDao.getDBUserById(referUserId);
                    if (referUser != null) {
                        String referUserRebatesLevel = referUser.getRebatesLevel();
                        if (!"normal_ratio".equals(referUserRebatesLevel)) {
                            Object referRatioConfig = configMap.get("a_refer_a_ratio");
                            if (referRatioConfig != null) {
//                                String[] ratios = referRatioConfig.toString().split(":");
//                                Double referIncome = NumberUtil.multiply(userRebateAmount, Double.valueOf(ratios[0]));
//                                logger.info("推荐会员消费扣税后的总收益：" + referIncome);
//                                referIncome = NumberUtil.divide(referIncome, Double.valueOf(ratios[1]), 4);
//                               
                                userAccountSettle(order, configMap, referUserId, referUserRebatesLevel, userRebateAmount,
                                        CommonConst.USER_BILL_TYPE_USER_REWARD, true, true, shopIdentity);
//                                
//                                orderSettleDto.setUser_ref_share_price(referIncome);
//                                orderSettleDto.setUser_ref_share_ratio(NumberUtil.divide(Double.valueOf(ratios[0]), Double.valueOf(ratios[1]), 4));
//                                orderSettleDto.setUser_ref_share_user_id(referUserId);
//                                orderSettleDto.setUser_ref_share_user_name(referUser.getMobile());
                            }
                        }
                    }
                }

                
            }
        }
        
        //计算平台收益
        Double platformRevenueAmount = NumberUtil.multiply(platformRevenueRatio, percentage, settlePrice);
        logger.info("提供给相关人分成的平台收益：" + platformRevenueAmount);
        //平台收益扣税
//        Double platRevAmountDeduction = NumberUtil.multiply(platformRevenueAmount, 1 - platformTaxDeductionRatio);
//        logger.info("提供给相关人分成的扣税后平台收益：" + platRevAmountDeduction);
        //计算平台入帐
        Double platformIncomeAmount = NumberUtil.multiply(settlePrice, percentage);
        logger.info("平台总收益：" + platformIncomeAmount);
//        orderSettleDto.setPlatform_net_income_price(eswq);
        orderSettleDto.setPlatform_total_income_ratio(percentage);
        orderSettleDto.setPlatform_total_income_price(platformIncomeAmount);
        
        //计算店铺收益4币
        Object orderMoney4RatioConfig = configMap.get("b1_order_ratio_money4");
        Double orderMoney4Ratio = orderMoney4RatioConfig == null ? 0 : Double.valueOf(orderMoney4RatioConfig.toString());
        Double monkey4Amount = NumberUtil.multiply(platformIncomeAmount, orderMoney4Ratio);
        logger.info("店铺生意金收入：" + monkey4Amount);
        //更新店铺账户
        shopAccountDao.updateShopAccount(shopId, monkey4Amount, null, null, null, null, null, null, monkey4Amount, monkey4Amount, null, platformIncomeAmount,monkey4Amount);
        //插入店铺账单
        ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(shopId);
        //分账前金额
        Double changeAmountBefore = NumberUtil.sub(shopAccountDto.getAmount(), monkey4Amount);
        if(order!=null){
       	 order.setOrderTitle("返利"+NumberUtil.formatDoubleStr(monkey4Amount,4)+"元");
       }
        shopBillService.insertShopBill(CommonConst.BILL_DIRECTION_ADD, monkey4Amount,
                null, shopId, changeAmountBefore, shopAccountDto.getMarketAmount(), CommonConst.SHOP_ACCOUNT_TYPE_MARKET,
                CommonConst.SHOP_BILL_TYPE_RETURN_MARKET,order.getOrderTitle(), "3721消费获得生意金", order);
      	 order.setOrderTitle(null);

       
        //平台出账
        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, monkey4Amount, 
                CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, "销售商品返还生意金", "3721销售商品返还生意金,返还比例："+orderMoney4Ratio,
                CommonConst.PLATFORM_BILL_TYPE_BACK_MARKET, CommonConst.PLATFORM_BILL_STATUS_OVER);
        
        changeAmountBefore = shopAccountDto.getAmount();
        
        //返回线上收入给店铺
        Double payAmount = payDao.getSumPayAmount(orderId, CommonConst.PAYEE_TYPE_SHOP);// 商铺已收款项
        
        Double voucherAmount = payDao.getSumPayAmount(orderId, null, CommonConst.PAY_TYPE_VOUCHER);
       
        payAmount = NumberUtil.add(payAmount, voucherAmount);
        Double onlineIncome = NumberUtil.sub(order.getSettlePrice(), payAmount);
        logger.info("线上营业收入：" + onlineIncome);
        
        //商铺账户
        ShopAccountDto shopAccount = shopAccountDao.getShopAccount(order.getShopId());
        //返利比例
        Double platformTotalIncomeRatio = shop.getPercentage();
        //平台获得奖励，保证金不足从线上收入
        Double platformTotalIncome = NumberUtil.multiply(platformTotalIncomeRatio, order.getOrderRealSettlePrice());
        if (null != shop.getShopIdentity() && (shop.getShopIdentity() == 1 || shop.getShopIdentity() == 2)) {
            platformTotalIncome = NumberUtil.multiply(platformTotalIncomeRatio, order.getSettlePrice());
        }
       //保证金小于服务费
    	if(onlineIncome>0){
    		logger.info("线上收入转入保证金-start");
    		//处理后线上收入余额
            Double onlineIncomeAfterAmount = 0D;
            Double supportDeposit = 0D;
    		if(onlineIncome>=platformTotalIncome){
                //处理后线上收入余额
               onlineIncomeAfterAmount = 
                        NumberUtil.sub(shopAccount.getOnlineIncomeAmount(),platformTotalIncome);
                //处理后保证金余额
               supportDeposit = NumberUtil.add(shopAccount.getDepositAmount(), platformTotalIncome);
                
    		}
    		else{//扣完本次线上收入，剩余保证金扣除
                //处理后线上收入余额
               onlineIncomeAfterAmount = shopAccount.getOnlineIncomeAmount();
                //处理后保证金余额
               supportDeposit = NumberUtil.add(shopAccount.getDepositAmount(), onlineIncome);
               platformTotalIncome = onlineIncome;
    		}

            
            //增加一条线上收入扣减记录
            payService.insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER,
					CommonConst.BILL_DIRECTION_DOWN, "线上收入转入保证金"
							+ -platformTotalIncome + "元",
					CommonConst.SHOP_BILL_STATUS_OVER,
					CommonConst.SHOP_ACCOUNT_TYPE_INCOME, shopAccount.getAmount(),
					onlineIncomeAfterAmount, -platformTotalIncome, "转充",
					null,orderId);
			//增加一条保证金增加记录
            payService.insertShopBill(shopId, CommonConst.SHOP_BILL_TYPE_TRANSFER,
					CommonConst.BILL_DIRECTION_ADD, "线上收入转入保证金"
							+ platformTotalIncome + "元",
					CommonConst.SHOP_BILL_STATUS_OVER,
					CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT, shopAccount.getAmount(),
					supportDeposit, platformTotalIncome, "转充",
					null,orderId);
            // 更新账户余额
            shopAccountDao.updateShopAccount(shopId, null, -platformTotalIncome, 
            		null, null, platformTotalIncome, null, null, null, null, null, null,null);

            logger.info("线上收入转入保证金-end");
    	}
    	//扣减店铺账户保证金
        shopAccountDao.updateShopAccount(shopId, NumberUtil.sub(onlineIncome, platformIncomeAmount), onlineIncome, null, null, -platformIncomeAmount, null, null, null, null, null, null,null);
        
        shopAccountDto = shopAccountDao.getShopAccount(shopId);
        if(order!=null){
        	order.setOrderTitle("保证金");
        }
        shopBillService.insertShopBill(CommonConst.BILL_DIRECTION_DOWN, platformIncomeAmount,
                null, shopId, changeAmountBefore, shopAccountDto.getDepositAmount(), CommonConst.SHOP_ACCOUNT_TYPE_DEPOSIT,
                CommonConst.SHOP_BILL_TYPE_PAY,order.getOrderTitle(), "3721消费平台入帐扣除保证金", order);
    	order.setOrderTitle(null);

        //平台入帐
        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, platformIncomeAmount, 
                CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, "销售提成", "3721平台消费提成,提成比例："+percentage,
                CommonConst.PLATFORM_BILL_TYPE_SALE, CommonConst.PLATFORM_BILL_STATUS_OVER);
        
 
        if(onlineIncome > 0D) {
            changeAmountBefore = NumberUtil.add(platformIncomeAmount,changeAmountBefore);
            if(order!=null){
            	order.setOrderTitle("线上收入");
            }
            shopBillService.insertShopBill(CommonConst.BILL_DIRECTION_ADD, onlineIncome,
                    null, shopId, changeAmountBefore, shopAccountDto.getOnlineIncomeAmount(), CommonConst.SHOP_ACCOUNT_TYPE_INCOME,
                    CommonConst.SHOP_BILL_TYPE_SALE,order.getOrderTitle(), "3721销售商品线上收入", order);
        	order.setOrderTitle(null);
            // 平台出帐，
            platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, onlineIncome,
                    CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, "平台返还线上收款",
                    "3721店铺销售商品-平台返还线上收款", CommonConst.PLATFORM_BILL_TYPE_ONLINE,
                    CommonConst.PLATFORM_BILL_STATUS_OVER);
        }
        
        //计算店铺推荐人收益
        Long shopReferUserId = shop.getReferUserId();
        if (shopReferUserId != null) {
            UserDto shopReferUser = userDao.getDBUserById(shopReferUserId);
            if (shopReferUser != null && ("middle_ratio").equals(shopReferUser.getRebatesLevel())) {
                Object shopReferRewardRatioConfig = configMap.get("a_refer_b_order_ratio");
                Double shopReferRewardRatio = shopReferRewardRatioConfig == null ? 0 : Double.valueOf(shopReferRewardRatioConfig.toString());
                Double shopReferReward = NumberUtil.multiply(shopReferRewardRatio, platformRevenueAmount, (1 - platformTaxDeductionRatio));
                logger.info("店铺推荐人扣税后收益：" + shopReferReward);
                orderSettleDto.setShop_ref_share_price(shopReferReward);
                orderSettleDto.setShop_ref_share_ratio(shopReferRewardRatio);
                orderSettleDto.setShop_ref_share_user_id(shopReferUserId);
                orderSettleDto.setShop_ref_share_user_name(shopReferUser.getMobile());
                userAccountDao.updateUserAccount(shopReferUserId, shopReferReward, shopReferReward, shopReferReward, null, null,
                                                    null, null, null, null, null,
                                                    null, null, null, null);
                
                UserAccountDto shopReferUserAccount = userAccountDao.getAccountMoney(shopReferUserId);
                Double userAmountBefore = NumberUtil.sub(shopReferUserAccount.getAmount(), shopReferReward);
                if(order!=null){
               	 order.setOrderTitle("返利"+shopReferReward+"元");
               }
                userBillService.insertUserBill(shopReferUserId,
                        null,
                        CommonConst.BILL_DIRECTION_ADD,
                        shopReferReward, shopReferUserAccount.getRewardAmount(), 
                        userAmountBefore, order, "3721计划", 
                        CommonConst.PAY_TYPE_SINGLE, "3721推荐店铺消费返利-平台奖励", 
                        CommonConst.USER_BILL_TYPE_SHOP_REWARD,
                        CommonConst.USER_ACCOUNT_TYPE_REWARD,
                        null);
                order.setOrderTitle(null);
                // 平台出帐，
                platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, shopReferReward,
                        CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, "推荐店铺奖励",
                        "3721推荐店铺消费返利-平台奖励", CommonConst.PLATFORM_BILL_TYPE_SHOPRWARDS,
                        CommonConst.PLATFORM_BILL_STATUS_OVER);
                
                //平台税收入账 
                Double taxDeductionMoney = NumberUtil.multiply(shopReferRewardRatio, platformRevenueAmount, platformTaxDeductionRatio);
                if(taxDeductionMoney > 0) {
                    // 平台记账，税金只记一条账单
                    platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, taxDeductionMoney,
                            null, order, null, "收取返利税金",
                            "3721-推荐店铺返利平台扣税", CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION,
                            CommonConst.PLATFORM_BILL_STATUS_OVER);
                }
                
            }
        }
        
        //计算区域代理收益
        Long cityId = shop.getCityId();
        Long districtId = shop.getDistrictId().longValue();
        Long townId = shop.getTownId().longValue();
        
        AgentDto shopCityAgent = null;
        AgentDto shopDistrictAgent = null;
        AgentDto shopTownAgent = null;
        
        if (cityId != null) {
            shopCityAgent = agentDao.getAgent(null, shop.getCityId(), null, null, 31, null);
        }
        
        if (districtId != null) {
            shopDistrictAgent = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(), null, 32, null);
        }
        
        if (townId != null) {
            shopTownAgent = agentDao.getAgent(null, shop.getCityId(), shop.getDistrictId().longValue(), shop.getTownId().longValue(), 33, null);
        }
        
        if (shopCityAgent != null) {
            agentAccountSettle(shopCityAgent, configMap, order, platformRevenueAmount, CommonConst.USER_BILL_TYPE_CITY_AGENT_REWARD, orderSettleDto);
        }
        
        if (shopDistrictAgent != null) {
            agentAccountSettle(shopDistrictAgent, configMap, order, platformRevenueAmount, CommonConst.USER_BILL_TYPE_AREA_AGENT_REWARD, orderSettleDto);
        }
        
        if (shopTownAgent != null) {
            agentAccountSettle(shopTownAgent, configMap, order, platformRevenueAmount, CommonConst.USER_BILL_TYPE_TOWN_AGENT_REWARD, orderSettleDto);
        }
        //发红包相关账单
        insertBillByRedPacket(order);
        OrderDto newOrder = new OrderDto();
        Date date = new Date();
        newOrder.setOrderId(order.getOrderId());
        newOrder.setShopSettleFlag(CommonConst.SHOP_SETTLE_FLAG_TRUE); // 已结算
        newOrder.setShopSettleTime(date);
        newOrder.setLastUpdateTime(date);
        newOrder.setSettleFlag(CommonConst.USER_SETTLE_FLAG_TRUE);
        newOrder.setSettleTime(date);
        newOrder.setUserId(order.getUserId());
        newOrder.setPlatformTotalIncomePrice(platformIncomeAmount);
        orderDao.updateOrder(newOrder);
//        orderSettleDao.saveOrderSettle(orderSettleDto);
        
    }
    
    private void agentAccountSettle(AgentDto agent, Map<String, Object> configMap, OrderDto order,
            Double platformRevenueAmount, Integer userBillType, OrderSettleDto orderSettleDto) throws Exception {
        //计算平台税率
        Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
        Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
        Double agentShareRatio = agent.getAgentShareRatio();
        Double agentIncome = NumberUtil.multiply(agentShareRatio, platformRevenueAmount, (1 - platformTaxDeductionRatio));
        logger.info("代理商扣税收益：" + agentIncome + "代理商分成比例：" + agentShareRatio);
        Long agentUserId = agent.getUserId();
        setOrderSettleDto(orderSettleDto, agentShareRatio, agentIncome, agent);
        userAccountDao.updateUserAccount(agentUserId, agentIncome, agentIncome, agentIncome, null, null, null, null, null,
                null, null, null, null, null, null);

        UserAccountDto agentUserAccount = userAccountDao.getAccountMoney(agentUserId);

        Double amount =NumberUtil.sub(agentUserAccount.getAmount(), agentIncome); 



        if(order!=null){
        	 order.setOrderTitle("返利"+agentIncome+"元");
        }
        userBillService.insertUserBill(agentUserId, null, CommonConst.BILL_DIRECTION_ADD, agentIncome,
                agentUserAccount.getRewardAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                "3721店铺消费代理返利-平台奖励,提成比例：" + agentShareRatio, userBillType, CommonConst.USER_ACCOUNT_TYPE_REWARD, agent.getAgentId());
        order.setOrderTitle(null);
      
        String billType =  "支付代理奖励";
        String platformBillDesc = "3721代理奖励-支付代理奖励";
        int platformBillType = CommonConst.PLATFORM_BILL_TYPE_1AGENT;
        String deductionDesc = "返利平台扣税";
        if(agent.getAgentType() == 31) {
            billType =  "支付一级代理奖励";
            platformBillDesc = "3721代理奖励-支付一级代理奖励";
            platformBillType = CommonConst.PLATFORM_BILL_TYPE_1AGENT;
            deductionDesc = "3721-一级代理奖励平台扣税";
        } else if(agent.getAgentType() == 32) {
            billType =  "支付二级代理奖励";
            platformBillDesc = "3721代理奖励-支付二级代理奖励";
            platformBillType = CommonConst.PLATFORM_BILL_TYPE_2AGENT;
            deductionDesc = "3721-二级代理奖励平台扣税";
        } else if(agent.getAgentType() == 33) {
            billType =  "支付三级代理奖励";
            platformBillDesc = "3721代理奖励-支付三级代理奖励";
            platformBillType = CommonConst.PLATFORM_BILL_TYPE_3AGENT;
            deductionDesc = "3721-三级代理奖励平台扣税";
        }
        // 平台出帐，
        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, agentIncome,
                CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, billType,
                platformBillDesc, platformBillType,
                CommonConst.PLATFORM_BILL_STATUS_OVER);
        
        //平台税收入账 
        Double taxDeductionMoney = NumberUtil.multiply(agentShareRatio, platformRevenueAmount, platformTaxDeductionRatio);
        if(taxDeductionMoney > 0) {
            // 平台记账，税金只记一条账单
            platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, taxDeductionMoney,
                    null, order, null, "收取返利税金",
                    deductionDesc, CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION,
                    CommonConst.PLATFORM_BILL_STATUS_OVER);
        }
        
        // 计算区域代理推荐人收益
        Long agentReferUserId = agent.getReferUserId();
        if (agentReferUserId != null) {
            UserDto agentReferUser = userDao.getDBUserById(agentReferUserId);
            if (agentReferUser != null && ("middle_ratio").equals(agentReferUser.getRebatesLevel())) {
                Object agentReferRatioConfig = configMap.get("a_refer_d_agent_ratio");
                Double agentReferRatio = agentReferRatioConfig == null ? 0 : Double.valueOf(agentReferRatioConfig
                        .toString());
//                //计算平台税率
//                Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
//                Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
                Double agentReferIncome = NumberUtil.multiply(agentIncome, agentReferRatio, (1-platformTaxDeductionRatio));

                orderSettleDto.setRefer_agent_price(agentReferIncome);
                orderSettleDto.setRefer_agent_ratio(platformTaxDeductionRatio);
                orderSettleDto.setRefer_agent_user_id(agentReferUserId);
                orderSettleDto.setRefer_agent_user_name(agentReferUser.getMobile());
                
                logger.info("代理商推荐人收益：" + agentReferIncome + "代理商推荐人分成比例：" + agentReferRatio);
                userAccountDao.updateUserAccount(agentReferUserId, agentReferIncome, agentReferIncome, agentReferIncome, null,
                        null, null, null, null, null, null, null, null, null, null);

                UserAccountDto referAgentUserAccount = userAccountDao.getAccountMoney(agentReferUserId);
                Double amountBefore = NumberUtil.sub(referAgentUserAccount.getAmount(), agentReferIncome);
                if(order!=null){
                	 order.setOrderTitle("返利"+NumberUtil.formatDoubleStr(agentReferIncome,4)+"元");
                }
                userBillService.insertUserBill(agentReferUserId, null, CommonConst.BILL_DIRECTION_ADD,
                        agentReferIncome, referAgentUserAccount.getRewardAmount(), amountBefore, order, "3721计划",
                        CommonConst.PAY_TYPE_SINGLE, "3721店铺消费推荐代理返利-平台奖励",
                        CommonConst.USER_BILL_TYPE_RECOMMEND_AGENT_AWARD, CommonConst.USER_ACCOUNT_TYPE_REWARD,null);
           	   order.setOrderTitle(null);

                // 平台出帐，
                platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, agentReferIncome,
                        CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, "支付推荐代理奖励",
                        "3721店铺消费推荐代理返利-平台奖励", CommonConst.PLATFORM_BILL_TYPE_REFER_AGENT,
                        CommonConst.PLATFORM_BILL_STATUS_OVER);
                
                //平台税收入账 
                Double taxReferDeductionMoney = NumberUtil.multiply(agentReferRatio, agentIncome, platformTaxDeductionRatio);
                if(taxReferDeductionMoney > 0) {
                    // 平台记账，税金只记一条账单
                    platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, taxReferDeductionMoney,
                            null, order, null, "收取返利税金",
                            "3721-推荐代理奖励平台扣税", CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION,
                            CommonConst.PLATFORM_BILL_STATUS_OVER);
                }
            }

        }
    }

    private void setOrderSettleDto(OrderSettleDto orderSettleDto, Double agentShareRatio, Double agentIncome, AgentDto agent) throws Exception {
        Long agentUserId = agent.getUserId();
        UserDto agentUser = userDao.getUserById(agentUserId);
        String mobile = "";
        Integer agentType = agent.getAgentType();
        if(null != agentUser) {
            mobile = agentUser.getMobile();
        }
        if(agentType == null) {
            agentType = 0;
        }
        if(agent.getAgentType() == 31) {
            orderSettleDto.setLevel1_agent_price(agentIncome);
            orderSettleDto.setLevel1_agent_share_ratio(agentShareRatio);
            orderSettleDto.setLevel1_agent_share_user_id(agentUserId);
            orderSettleDto.setLevel1_agent_share_user_name(mobile);
        } else if(agent.getAgentType() == 32) {
            orderSettleDto.setLevel2_agent_price(agentIncome);
            orderSettleDto.setLevel2_agent_share_ratio(agentShareRatio);
            orderSettleDto.setLevel2_agent_share_user_id(agentUserId);
            orderSettleDto.setLevel2_agent_share_user_name(mobile);
        }else if (agent.getAgentType() == 33) {
            orderSettleDto.setLevel3_agent_price(agentIncome);
            orderSettleDto.setLevel3_agent_share_ratio(agentShareRatio);
            orderSettleDto.setLevel3_agent_share_user_id(agentUserId);
            orderSettleDto.setLevel3_agent_share_user_name(mobile);
        }
    }
    public void userAccountSettle(OrderDto order, Map<String, Object> configMap, Long userId, String rebatesLevel,
            Double userRebateAmount, Integer userBillType, Boolean refer, boolean isPush, Integer shopIdentity) throws Exception {
       
  
            
        //计算平台税率， 1、3币收益扣税，2币在平台内部流通不扣税
        Object platformTaxDeductionRatioConfig = configMap.get("platform_tax_deduction_ratio");
        Double platformTaxDeductionRatio = platformTaxDeductionRatioConfig == null ? 0 : Double.valueOf(platformTaxDeductionRatioConfig.toString());
        Double settlePrice = order.getSettlePrice();
        // 1币收益
        Object legendRatioConfig = configMap.get("c_rebates_money1");
        Double legendRatio = legendRatioConfig == null ? 0 : Double.valueOf(legendRatioConfig.toString());
        Double legendAmount = 0D;

        // 2币收益
        Object consumeRatioConfig = configMap.get("c_rebates_money2");
        Double consumeRatio = consumeRatioConfig == null ? 0 : Double.valueOf(consumeRatioConfig.toString());
        Double consumeAmount = 0D;

        // 3币收益
        Object voucherRatioConfig = configMap.get("c_rebates_money3");
        Double voucherRatio = voucherRatioConfig == null ? 0 : Double.valueOf(voucherRatioConfig.toString());
        Double voucherAmount = 0D;
        // 总收入
        Double changeAmount = 0D;
        //平台税金收入 
        Double taxDeductionMoney = 0D;
        if(refer) {
            Object referRatioConfig = configMap.get("a_refer_a_ratio");
            String[] ratios = referRatioConfig.toString().split(":");
            Double referIncome = NumberUtil.multiply(userRebateAmount, Double.valueOf(ratios[0]));
            logger.info("推荐会员消费扣税后的总收益：" + referIncome);
          
            legendAmount = NumberUtil.multiply(NumberUtil.divide(NumberUtil.multiply(userRebateAmount, legendRatio, 
                    (1- platformTaxDeductionRatio), Double.valueOf(ratios[0])), Double.valueOf(ratios[1]), 4), 
                    (1- platformTaxDeductionRatio));
            consumeAmount = NumberUtil.divide(NumberUtil.multiply(userRebateAmount, consumeRatio, 
                     Double.valueOf(ratios[0])), Double.valueOf(ratios[1]), 4);
            voucherAmount = NumberUtil.multiply(NumberUtil.divide(NumberUtil.multiply(userRebateAmount, voucherRatio, 
                    (1- platformTaxDeductionRatio), Double.valueOf(ratios[0])), Double.valueOf(ratios[1]), 4), 
                    (1- platformTaxDeductionRatio));
            changeAmount = NumberUtil.add(legendAmount, consumeAmount, voucherAmount);
            taxDeductionMoney = NumberUtil.add(NumberUtil.multiply(NumberUtil.divide(NumberUtil.multiply(userRebateAmount, legendRatio, 
                    (1- platformTaxDeductionRatio), Double.valueOf(ratios[0])), Double.valueOf(ratios[1]), 4), 
                    platformTaxDeductionRatio),NumberUtil.multiply(NumberUtil.divide(NumberUtil.multiply(userRebateAmount, voucherRatio, 
                    (1- platformTaxDeductionRatio), Double.valueOf(ratios[0])), Double.valueOf(ratios[1]), 4), 
                    platformTaxDeductionRatio));
        } else {
            legendAmount = NumberUtil.multiply(userRebateAmount, legendRatio, (1- platformTaxDeductionRatio));
            consumeAmount = NumberUtil.multiply(userRebateAmount, consumeRatio);
            voucherAmount = NumberUtil.multiply(userRebateAmount, voucherRatio, (1- platformTaxDeductionRatio));
            changeAmount = NumberUtil.add(legendAmount, consumeAmount, voucherAmount);
            taxDeductionMoney = NumberUtil.sub(userRebateAmount, changeAmount);
        }
        
        if(taxDeductionMoney > 0) {
            // 平台记账，税金只记一条账单
            platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_ADD, taxDeductionMoney,
                    null, order, null, "收取返利税金",
                    refer == false ? "3721-消费返利平台扣税" : "3721-推荐会员返利平台扣税", CommonConst.PLATFORM_BILL_TYPE_TAX_DEDUCTION,
                    CommonConst.PLATFORM_BILL_STATUS_OVER);
        }
       
        Double rebateMoney = settlePrice;
        if(null != shopIdentity && shopIdentity == 2 ) {
            Object consumeInB2RebatesConfig = configMap.get("c_consume_in_b2_rebates");
            Double consumeInB2RebatesRatio = consumeInB2RebatesConfig == null ? 0 : Double.valueOf(consumeInB2RebatesConfig.toString());
            rebateMoney =  NumberUtil.multiply(rebateMoney, consumeInB2RebatesRatio);
        }
        // 消费返回总额
        Double consumeRebateTotal = rebateMoney;
//                NumberUtil.multiply(rebateMoney, (1- platformTaxDeductionRatio));
        Double consumeRebateMoney = changeAmount;
        // 扣减收益循环计数值,每达到上限值需减去上限值,普通C不计入
        Double deductionCountValue = changeAmount;
        // 更新用户账户
        if ("normal_ratio".equals(rebatesLevel)) {
            // 普通会员不计入这个值
            deductionCountValue = null;
        }
        
        if (refer) {
            // 推荐人奖励不计入这个两个值
            consumeRebateTotal = null;
            consumeRebateMoney = null;
        }
        userAccountDao.updateUserAccount(userId, changeAmount, legendAmount, legendAmount, null, null, legendAmount,
                consumeAmount, consumeAmount, voucherAmount, voucherAmount, deductionCountValue, null,
                consumeRebateTotal, consumeRebateMoney);

        UserAccountDto userAccount = userAccountDao.getAccountMoney(userId);
        // 账单前的账户总额
        Double amount = NumberUtil.sub(userAccount.getAmount(), changeAmount);
        // 记录用户账单
        if(null != order)
        {
            order.setOrderTitle("返利"+legendAmount+"元");
        }
        if(legendAmount>0){
        	userBillService.insertUserBill(userId.longValue(), null, CommonConst.BILL_DIRECTION_ADD, legendAmount,
                    userAccount.getRewardAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                    refer == false ? "3721消费返利-平台奖励" : "3721推荐会员奖励-平台奖励", userBillType,
                    CommonConst.USER_ACCOUNT_TYPE_REWARD, null);
        }
        
        if(null != order)
        {
            order.setOrderTitle("返利"+consumeAmount +"元");
        }
        if(consumeAmount>0){
        	userBillService.insertUserBill(userId.longValue(), null, CommonConst.BILL_DIRECTION_ADD, consumeAmount,
                    userAccount.getConsumeAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                    refer == false ? "3721消费返利-消费币" : "3721推荐会员奖励-消费币", userBillType, CommonConst.USER_ACCOUNT_TYPE_CONSUM, null);
        }
        
        if(null != order)
        {
            order.setOrderTitle("返利"+voucherAmount+"元");
        }
        if(voucherAmount>0){
        	userBillService.insertUserBill(userId.longValue(), null, CommonConst.BILL_DIRECTION_ADD, voucherAmount,
                    userAccount.getVoucherAmount(), amount, order, "3721计划", CommonConst.PAY_TYPE_SINGLE,
                    refer == false ? "3721消费返利-代金券" : "3721推荐会员奖励-代金券", userBillType, CommonConst.USER_ACCOUNT_TYPE_VOUCHER, null);
        }
        
       
        String billType = refer == false ? "支付会员奖励" : "支付推荐会员奖励";
        String platformBillDesc = refer == false ? "3721消费返利-支付会员奖励, 其中123币值为："+ legendAmount + CommonConst.COMMA_SEPARATOR + consumeAmount + CommonConst.COMMA_SEPARATOR + voucherAmount
                : "3721推荐会员奖励-支付推荐会员奖励,其中123币值为："+ legendAmount + CommonConst.COMMA_SEPARATOR + consumeAmount + CommonConst.COMMA_SEPARATOR + voucherAmount;
        int platformBillType = refer == false ? CommonConst.PLATFORM_BILL_TYPE_MEMBERAWARD : CommonConst.PLATFORM_BILL_TYPE_RECOMAWARDS;

        // 平台出帐，只记录一条，在描述中区分123币的值
        platformBillService.insertPlatformBill(CommonConst.BILL_DIRECTION_DOWN, changeAmount,
                CommonConst.PLT_BILL_MNY_SOURCE_DEPOSIT, order, null, billType,
                platformBillDesc, platformBillType,
                CommonConst.PLATFORM_BILL_STATUS_OVER);
        
        if (isPush) {
            SmsReplaceContent pushReplaceContent = new SmsReplaceContent();
            pushReplaceContent.setPayAmount(settlePrice);
            pushReplaceContent.setUserRebateAmount(changeAmount);
            String action = "userRebate";
            if (refer) {
                action = "recommendUserRebate";
                //消费返利推送给用户推荐人
            }else {
                if(null != shopIdentity && shopIdentity == 2) {
                   //折扣店模板需要修改
                    pushReplaceContent.setMoney(rebateMoney);
                    action = "user_rebates_consume_in_b2";
                } 
            }
            if(userRebateAmount > 0D ) {
            
                commonService.pushUserMsg(action, userId, 0, pushReplaceContent);
            }
        } else {
            String action = "user_rebates";
            if (refer) {
                action = "recommend_user_rebates";
                //消费返利推送给用户推荐人
            }
            SmsReplaceContent pushReplaceContent = new SmsReplaceContent();
            pushReplaceContent.setMoney(changeAmount);
            pushReplaceContent.setUsage(action);
            commonService.insertUserMsg(action, userId, 0, pushReplaceContent);
        }
    }
    
    
    /**
     * 结算是红包相关的订单
     * 
     * @param order
     * @author shengzhipeng
     * @date 2016年3月15日
     */
    public void insertBillByRedPacket(OrderDto order) throws Exception
    {
        // 根据订单编号获取账单
        String orderId = order.getOrderId();
        Long shopId = order.getShopId();
        List<PlatformBillDto> platformBills = platformBillDao.getPlatformBillByOrderId(orderId);
        if (CollectionUtils.isNotEmpty(platformBills))
        {
            for (PlatformBillDto platformBillDto : platformBills)
            {
                if (CommonConst.PLATFORM_BILL_TYPE_S_RED_PACKET == platformBillDto.getPlatformBillType())
                {
                    Double packetMoney = Math.abs(platformBillDto.getMoney());
                    // 如果存在平台发出去的红包，结算时需要从店铺线上收入扣回
                    platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
                    platformBillDto.setPlatformBillType(CommonConst.PLATFORM_BILL_TYPE_R_RED_PACKET);
                    platformBillDto.setMoney(packetMoney);
                    platformBillDto.setBillType("收红包");
                    platformBillDao.insertPlatformBillMiddle(platformBillDto);
                    
                    //扣减店铺账户保证金
                    shopAccountDao.updateShopAccount(shopId, -packetMoney, -packetMoney, null, null, null, null, null, null, null,null, null,null);
                    ShopAccountDto shopAccountDto = shopAccountDao.getShopAccount(shopId);
                    
                    ShopBillDto shopBillDto = new ShopBillDto();
                    shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_SEND_REDPACKET);
                    shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
                    shopBillDto.setBillStatus(CommonConst.SHOP_BILL_STATUS_OVER);
                    shopBillDto.setMoney(-packetMoney);
                    shopBillDto.setShopId(shopId);
                    shopBillDto.setOrderId(orderId);
                    shopBillDto.setSettlePrice(order.getSettlePrice());
                    shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_INCOME);
                    shopBillDto.setCreateTime(new Date());
                    shopBillDto.setBillTitle(order.getOrderTitle());
                    shopBillDto.setComment("发送用户红包" + packetMoney);
                    shopBillDto.setRedPacketId(platformBillDto.getRedPacketId());
                    shopBillDto.setAccountAfterAmount(shopAccountDto.getOnlineIncomeAmount());
                    shopBillDto.setSettleTime(new Date());
                    shopBillDto.setAccountAmount(NumberUtil.add(shopAccountDto.getAmount(), packetMoney));
                    shopBillDao.insertShopBill(shopBillDto);
                    
                    
                }
            }
        }
    }

}
