package com.idcq.appserver.utils;

import java.util.Date;

import com.ibm.icu.math.BigDecimal;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.billStatus.ConsumeEnum;
import com.idcq.appserver.common.billStatus.WithdrawEnum;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.dto.bill.ShopBillDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.packet.RedPacket;
import com.idcq.appserver.dto.pay.WithdrawDto;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.dto.shop.ShopWithDrawDto;
import com.idcq.appserver.dto.user.UserAccountDto;
import com.idcq.appserver.dto.user.UserBillDto;

/**
 * 账单操作相关工具类
 * @author huangrui
 *
 */
public class BillUtil {
	
	
	/**
	 * 用户提现：平台奖励账单
	 * @param withdrawDto 提现信息
	 * @param userAccount 用户账户
	 * @param billLogo 账单logo
	 * @param add 是否增加标示： true-增加金额   false-减少金额
	 * @return
	 */
	public static UserBillDto buildUserRewardBillForWithdraw(WithdrawDto withdrawDto, UserAccountDto userAccount, Long billLogo, boolean add){
		UserBillDto userBill = new UserBillDto();
		userBill.setUserId(withdrawDto.getUserId());
		if(add){
			userBill.setMoney(withdrawDto.getAmount());
			userBill.setAccountAfterAmount(NumberUtil.add(userAccount.getRewardAmount(), withdrawDto.getAmount())); 
			userBill.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
			userBill.setBillDesc("提现退回平台奖励" + withdrawDto.getAmount() + "元");
			userBill.setUserBillType(CommonConst.USER_BILL_TYPE_WITHDRAW_BACK); // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
			userBill.setBillTitle("提现退回" + withdrawDto.getAmount() + "元");
		} else {
			userBill.setMoney(0 - withdrawDto.getAmount());
			userBill.setAccountAfterAmount(NumberUtil.sub(userAccount.getRewardAmount(), withdrawDto.getAmount())); // 减少平台奖励
			userBill.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
			userBill.setBillDesc("提现扣除平台奖励" + withdrawDto.getAmount() + "元");
			userBill.setUserBillType(CommonConst.USER_BILL_TYPE_WITHDRAW); // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
			userBill.setBillTitle("提现" + withdrawDto.getAmount() + "元");
		}
		
		
		userBill.setBillType("提现");
		
		userBill.setCreateTime(new Date());
		// 手机号码
		userBill.setConsumerMobile(withdrawDto.getMobile());
		// 状态标示
		userBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_PROCESS);
		// 审核
		userBill.setBillStatus(WithdrawEnum.AUDITING.getValue());
		userBill.setAccountAmount(userAccount.getAmount());
		//提现的账单交易ID设置为提现号，方便app查询提现账单时能关联查询到此提现记录5.28
		userBill.setTransactionId(withdrawDto.getWithdrawId());
		//增加bill_logo字段 2015.7.1
		userBill.setBillLogo(billLogo);
		
		userBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_REWARD); // 账户类型：1=平台奖励，2=冻结资金，3=消费金
		//可见
		userBill.setIsShow(CommonConst.USER_BILL_IS_SHOW);
		
		return userBill;
	}
	

	/**
	 * 用户提现：冻结或解冻资金账单
	 * @param withdrawDto 提现信息
	 * @param userAccount 用户账户
	 * @param billLogo 账单logo
	 * @param billStatus 10-审核中 11-审核不通过 12-审核通过，提现中，13-支付失败 14-成功提现
	 * @param freeze 是否增加标示： true-冻结   false-解冻
	 * @return
	 */
	public static UserBillDto buildUserFreezeBillForWithdraw(WithdrawDto withdrawDto, UserAccountDto userAccount, Long billLogo, int billStatus, boolean freeze){
		UserBillDto userBill = new UserBillDto();
		userBill.setUserId(withdrawDto.getUserId());
		if(freeze){
			userBill.setMoney(withdrawDto.getAmount());
			userBill.setAccountAfterAmount(NumberUtil.add(userAccount.getFreezeAmount(), withdrawDto.getAmount())); // 增加冻结资金
			userBill.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
			userBill.setBillDesc("提现冻结资金"+withdrawDto.getAmount()+"元");
			userBill.setUserBillType(CommonConst.USER_BILL_TYPE_FREEZE); // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
		} else {
			userBill.setMoney(-withdrawDto.getAmount());
			userBill.setAccountAfterAmount(NumberUtil.sub(userAccount.getFreezeAmount(), withdrawDto.getAmount())); 
			userBill.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
			userBill.setBillDesc("提现解冻资金"+withdrawDto.getAmount()+"元");
			userBill.setUserBillType(CommonConst.USER_BILL_TYPE_UNFREEZE); // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
		}
		
		userBill.setBillTitle("提现" + withdrawDto.getAmount() + "元");
		userBill.setBillType("提现");
		
		userBill.setCreateTime(new Date());
		// 手机号码
		userBill.setConsumerMobile(withdrawDto.getMobile());
		// 状态标示
		userBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_PROCESS);
		// 审核
		userBill.setBillStatus(billStatus);
		userBill.setAccountAmount(userAccount.getAmount());
		//提现的账单交易ID设置为提现号，方便app查询提现账单时能关联查询到此提现记录5.28
		userBill.setTransactionId(withdrawDto.getWithdrawId());
		//增加bill_logo字段 2015.7.1
		userBill.setBillLogo(billLogo);
		
		userBill.setIsShow(CommonConst.USER_BILL_IS_SHOW);
		userBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_FREEZE); // 账户类型：1=平台奖励，2=冻结资金，3=消费金
		
		return userBill;
	}
	


	/**
	 * 商铺提现：线上营业收入商铺账单
	 * @param shopWithDrawDto 提现记录
	 * @param account 商铺账户
	 * @param deductOnlineIncomeAmount 扣除金额
	 * @param add 是否增加标示： true-增加金额   false-减少金额
	 * @return
	 */

	public static ShopBillDto buildShopOnlineIncomeAmountBillForWithdraw(
			ShopWithDrawDto shopWithDrawDto, ShopAccountDto account, double deductOnlineIncomeAmount, boolean add) {
		ShopBillDto shopBillDto = new ShopBillDto();
		shopBillDto.setShopId(shopWithDrawDto.getShopId());
		if(add){
			shopBillDto.setMoney(deductOnlineIncomeAmount);
			//账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
			shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_WITHDRAW_BACK);
			shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
			shopBillDto.setBillDesc("提现退回"+deductOnlineIncomeAmount+"元");
			// '账单状态:处理中=1,成功=2,失败=3',
			shopBillDto.setBillStatus(2);
			shopBillDto.setAccountAfterAmount(NumberUtil.add(account.getOnlineIncomeAmount(), deductOnlineIncomeAmount));
			shopBillDto.setBillTitle("提现退回"+new BigDecimal(shopWithDrawDto.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP)+"元");
		}else{
			shopBillDto.setMoney(0 - deductOnlineIncomeAmount);
			//账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
			shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_WITHDRAW);
			shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
			shopBillDto.setBillDesc("提现扣除线上营业收入"+deductOnlineIncomeAmount+"元");
			// '账单状态:处理中=1,成功=2,失败=3',
			shopBillDto.setBillStatus(2);
			shopBillDto.setAccountAfterAmount(NumberUtil.sub(account.getOnlineIncomeAmount(), deductOnlineIncomeAmount));
			shopBillDto.setBillTitle("提现"+new BigDecimal(shopWithDrawDto.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP)+"元");
			if(shopWithDrawDto.getAmount().compareTo(deductOnlineIncomeAmount) > 0D) {
                shopBillDto.setBillTitle("提现"+shopWithDrawDto.getAmount()+"元（来源于线上收入："+deductOnlineIncomeAmount+"元）");
            } 
		}
		
		shopBillDto.setAccountAmount(account.getAmount());
		shopBillDto.setCreateTime(new Date());
		
		//提现的账单交易ID设置为提现号，方便app查询提现账单时能关联查询到此提现记录5.28
		shopBillDto.setTransactionId(shopWithDrawDto.getWithDrawId());
		shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_INCOME); //账户类型：0=线上营业收入，1=平台奖励，2=冻结资金，3=保证金
		
		return shopBillDto;
	}


	/**
	 * 商铺提现：平台奖励商铺账单
	 * @param shopWithDrawDto 提现记录
	 * @param account 商铺账户
	 * @param deduceRewardAmount 扣除金额
	 * @param add 是否增加标示： true-增加金额   false-减少金额
	 * @return
	 */
	public static ShopBillDto buildShopRewardAmountBillForWithdraw(
			ShopWithDrawDto shopWithDrawDto, ShopAccountDto account,
			double deduceRewardAmount, boolean add) {
		ShopBillDto shopBillDto = new ShopBillDto();
		shopBillDto.setShopId(shopWithDrawDto.getShopId());
		if(add){
			shopBillDto.setMoney(deduceRewardAmount);
			//账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
			shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_WITHDRAW_BACK);
			shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
			// '账单状态:处理中=1,成功=2,失败=3',
			shopBillDto.setBillStatus(2);
			shopBillDto.setBillDesc("提现退回平台奖励"+deduceRewardAmount+"元");
			shopBillDto.setAccountAfterAmount(NumberUtil.add(account.getRewardAmount(), deduceRewardAmount));
			shopBillDto.setBillTitle("提现退回"+shopWithDrawDto.getAmount()+"元");
		}else{
			shopBillDto.setMoney(0 - deduceRewardAmount);
			//账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
			shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_WITHDRAW);
			shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
			// '账单状态:处理中=1,成功=2,失败=3',
			shopBillDto.setBillStatus(2);
			shopBillDto.setBillDesc("提现扣除平台奖励"+deduceRewardAmount+"元");
			shopBillDto.setAccountAfterAmount(NumberUtil.sub(account.getRewardAmount(), deduceRewardAmount));
			shopBillDto.setBillTitle("提现"+shopWithDrawDto.getAmount()+"元");
			if(shopWithDrawDto.getAmount().compareTo(deduceRewardAmount) > 0D) {
			    shopBillDto.setBillTitle("提现"+shopWithDrawDto.getAmount()+"元（来源于平台奖励："+deduceRewardAmount+"元）");
			} 
		}
		
		shopBillDto.setAccountAmount(account.getAmount());
		shopBillDto.setCreateTime(new Date());
		
		//提现的账单交易ID设置为提现号，方便app查询提现账单时能关联查询到此提现记录5.28
		shopBillDto.setTransactionId(shopWithDrawDto.getWithDrawId());
		shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_REWARD); //账户类型：0=线上营业收入，1=平台奖励，2=冻结资金，3=保证金
		
		
		return shopBillDto;
	}


	/**
	 * 商铺提现：冻结或解冻资金账单
	 * @param shopWithDrawDto 提现记录
	 * @param account 商铺账户
	 * @param billStatus 账单状态:处理中=1,成功=2,失败=3
	 * @param freeze 是否增加标示： true-冻结   false-解冻
	 * @return
	 */
	public static ShopBillDto buildShopFreezeBillForWithdraw(ShopWithDrawDto shopWithDrawDto, ShopAccountDto account, boolean freeze) {
		ShopBillDto shopBillDto = new ShopBillDto();
		shopBillDto.setShopId(shopWithDrawDto.getShopId());
		if(freeze){

			shopBillDto.setMoney(shopWithDrawDto.getAmount());
			//账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
			shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_FREEZE);
			shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
			shopBillDto.setBillDesc("提现冻结"+shopWithDrawDto.getAmount()+"元");
			shopBillDto.setAccountAfterAmount(NumberUtil.add(account.getFreezeAmount(), shopWithDrawDto.getAmount()));
		} else {
			shopBillDto.setMoney(-shopWithDrawDto.getAmount());
			//账单类型:销售商品=1,支付平台服务费(原支付保证金)=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
			shopBillDto.setBillType(CommonConst.SHOP_BILL_TYPE_UNFREEZE);
			shopBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
			shopBillDto.setBillDesc("提现解冻"+shopWithDrawDto.getAmount()+"元");
			shopBillDto.setAccountAfterAmount(NumberUtil.sub(account.getFreezeAmount(), shopWithDrawDto.getAmount()));
		}
		shopBillDto.setAccountAmount(account.getAmount());
		shopBillDto.setCreateTime(new Date());
		// '账单状态:处理中=1,成功=2,失败=3',
		shopBillDto.setBillStatus(2);
		
		//提现的账单交易ID设置为提现号，方便app查询提现账单时能关联查询到此提现记录5.28
		shopBillDto.setTransactionId(shopWithDrawDto.getWithDrawId());
		
		
	
		shopBillDto.setAccountType(CommonConst.SHOP_ACCOUNT_TYPE_FREEZE); //账户类型：0=线上营业收入，1=平台奖励，2=冻结资金，3=保证金
		shopBillDto.setBillTitle("提现"+shopWithDrawDto.getAmount()+"元");
		
		return shopBillDto;
	}

	/**
	 * 红包账单实体构建方法
	 * @param order 订单对象
	 * @param redPacket 红包对象
	 * @param useAmount 使用金额
	 * @param isUse 是否使用，true--使用红包，false --收到红包
	 * @return UserBillDto [返回类型说明]
	 * @author  shengzhipeng
	 * @date  2016年3月15日
	 */
    public static UserBillDto buildUserBillForRedPacket(OrderDto order, RedPacket redPacket, Double useAmount, boolean isUse){
        UserBillDto userBill = new UserBillDto();
        userBill.setUserId(redPacket.getUserId());
        //红包余额
        double amount = redPacket.getAmount();
        if(isUse){
            userBill.setMoney(-useAmount);
            userBill.setAccountAfterAmount(NumberUtil.sub(amount, useAmount)); 
            userBill.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
            userBill.setBillDesc("使用红包"+useAmount+"元");
            // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
            userBill.setUserBillType(CommonConst.USER_BILL_TYPE_S_RED_PACKET); 
            userBill.setAccountAmount(amount);
        } else {
            userBill.setMoney(useAmount);
            userBill.setAccountAfterAmount(amount); 
            userBill.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
            userBill.setBillDesc("收到红包"+useAmount+"元");
            // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,13=退款,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
            userBill.setUserBillType(CommonConst.USER_BILL_TYPE_R_RED_PACKET); 
            userBill.setAccountAmount(0D);
        }
//        
        userBill.setBillTitle(order.getOrderTitle());
        userBill.setBillType("消费");
        
        userBill.setCreateTime(new Date());
        // 手机号码
        userBill.setConsumerMobile(order.getMobile());
        userBill.setConsumerUserId(order.getUserId());
        // 状态标示
        userBill.setBillStatusFlag(CommonConst.BILL_STATUS_FLAG_FINISH);
        userBill.setBillStatus(ConsumeEnum.CLOSED_ACCOUNT.getValue());
        userBill.setOrderId(order.getOrderId());
        
        userBill.setIsShow(CommonConst.USER_BILL_IS_SHOW);
        userBill.setAccountType(CommonConst.USER_ACCOUNT_TYPE_RED_PACKET); // 账户类型：1=平台奖励，2=冻结资金，3=消费金 //4=红包
        userBill.setRedPacketId(redPacket.getRedPacketId());
        return userBill;
    }
    
    public static PlatformBillDto buildPlatformBill(OrderDto order, Double money, Integer moneySource, int platformBillType, String billType, boolean isAdd) {
        PlatformBillDto platformBillDto = new PlatformBillDto();
        platformBillDto.setBillType(billType);
        if(isAdd) {
            platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_ADD);
            platformBillDto.setMoney(money);
        } else {
            platformBillDto.setBillDirection(CommonConst.BILL_DIRECTION_DOWN);
            platformBillDto.setMoney(-money);
        }
        platformBillDto.setMoneySource(moneySource);
        platformBillDto.setBillStatus(CommonConst.PLATFORM_BILL_STATUS_OVER);
        platformBillDto.setOrderId(order.getOrderId());
        platformBillDto.setGoodsSettlePrice(order.getSettlePrice());
        platformBillDto.setCreateTime(new Date());
        platformBillDto.setConsumerUserId(order.getUserId());
        platformBillDto.setConsumerMobile(order.getMobile());
        platformBillDto.setPlatformBillType(platformBillType);
        return platformBillDto;
    }
}
