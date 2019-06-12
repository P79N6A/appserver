package com.idcq.appserver.dto.shop;

import java.util.Date;

/**
 * 商铺提现实体类
 */
public class ShopWithDrawDto {
	/**
	 *  提现id
	 */
	private Long withDrawId;
	
	/**
	 * 店铺id
	 */
	private Long shopId;
	
	/**
	 * 银行名称
	 */
	private String bankName;
	
	/**
	 * 开户支行
	 */
	private String bankSubbranchName;
	
	/**
	 * 银行卡号
	 */
	private String cardNumber;
	
	/**
	 * 金额
	 */
	private Double amount;
	
	/**
	 * 商铺申请提现时间
	 */
	private Date applyTime;
	
	/**
	 * 交易编号
	 */
	private Long transactionId;
	
	/**
	 * 提现状态
	 */
	private Integer withdrawStatus;
	
	/**
	 * 处理人
	 */
	private Integer handleUserId;
	
	/**
	 * 处理时间
	 */
	private Date handleTime;
	
	/**
	 * 审核备注
	 */
	private String handleRemark;
	
	/**
	 * 实际提现时间
	 */
	private Date withDrawTime;
	
	/**
	 * 发起交易的终端类型
	 */
	private String terminalType;
	
	/**
	 * 提现使用的真实姓名
	 */
	private String userName;
	/**
	 * 审核备注
	 */
	private String handleMark;
	
	private Double onlineIncomeFreeze; // 线上营业收入冻结金额  2015.12.1 add by huangrui
	private Double rewardFreeze; // 平台奖励冻结金额 2015.12.1 add by huangrui
	
	private Double nextWithdrawAmount; // 提现后账户可提现余额(店铺收入余额+奖励余额) 2015.12.9 add by huangrui
	
	private Double withdrawCommission;//手续费

	public Double getNextWithdrawAmount() {
		return nextWithdrawAmount;
	}

	public void setNextWithdrawAmount(Double nextWithdrawAmount) {
		this.nextWithdrawAmount = nextWithdrawAmount;
	}

	public Double getOnlineIncomeFreeze() {
		return onlineIncomeFreeze;
	}

	public void setOnlineIncomeFreeze(Double onlineIncomeFreeze) {
		this.onlineIncomeFreeze = onlineIncomeFreeze;
	}

	public Double getRewardFreeze() {
		return rewardFreeze;
	}

	public void setRewardFreeze(Double rewardFreeze) {
		this.rewardFreeze = rewardFreeze;
	}

	public String getHandleMark() {
		return handleMark;
	}

	public void setHandleMark(String handleMark) {
		this.handleMark = handleMark;
	}

	public Long getWithDrawId() {
		return withDrawId;
	}

	public void setWithDrawId(Long withDrawId) {
		this.withDrawId = withDrawId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankSubbranchName() {
		return bankSubbranchName;
	}

	public void setBankSubbranchName(String bankSubbranchName) {
		this.bankSubbranchName = bankSubbranchName;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getWithdrawStatus() {
		return withdrawStatus;
	}

	public void setWithdrawStatus(Integer withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}

	public Integer getHandleUserId() {
		return handleUserId;
	}

	public void setHandleUserId(Integer handleUserId) {
		this.handleUserId = handleUserId;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getHandleRemark() {
		return handleRemark;
	}

	public void setHandleRemark(String handleRemark) {
		this.handleRemark = handleRemark;
	}

	public Date getWithDrawTime() {
		return withDrawTime;
	}

	public void setWithDrawTime(Date withDrawTime) {
		this.withDrawTime = withDrawTime;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Double getWithdrawCommission() {
		return withdrawCommission;
	}

	public void setWithdrawCommission(Double withdrawCommission) {
		this.withdrawCommission = withdrawCommission;
	}
}
