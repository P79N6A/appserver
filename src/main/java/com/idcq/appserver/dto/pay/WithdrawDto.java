package com.idcq.appserver.dto.pay;

import java.util.Date;

public class WithdrawDto {
	/**
	 * 主键
	 */
    private Long withdrawId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * userName
     */
    private String userName;
    /**
     * 用户类型
     */
    private Integer userType;
    /**
     * 发起终端的类型
     */
    private String terminalType;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 银行支行名称
     */
    private String bankSubbranchName;
    /**
     * 卡号
     */
    private String cardNumber;
    /**
     * 提现金额
     */
    private Double amount;
    /**
     * 提现时间
     */
    private Date applyTime;
    /**
     * 对应的交易编号
     */
    private Long transactionId;
    /**
     * 交易状态
     */
    private Integer withdrawStatus;
    /**
     * 处理人员
     */
    private Long handleUserId;
    /**
     * 处理时间
     */
    private Date handleTime;
    /**
     * 审核备注
     */
    private String handleMark;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 实际提现时间
     */
    private Date withdrawTime;
    
    private Double nextWithdrawAmount; //提现后账户可提现余额(奖励余额 ) 2015.12.9
    
    
	public Double getNextWithdrawAmount() {
		return nextWithdrawAmount;
	}
	public void setNextWithdrawAmount(Double nextWithdrawAmount) {
		this.nextWithdrawAmount = nextWithdrawAmount;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getWithdrawId() {
		return withdrawId;
	}
	public void setWithdrawId(Long withdrawId) {
		this.withdrawId = withdrawId;
	}
	public Long getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
	public Long getHandleUserId() {
		return handleUserId;
	}
	public void setHandleUserId(Long handleUserId) {
		this.handleUserId = handleUserId;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public String getHandleMark() {
		return handleMark;
	}
	public void setHandleMark(String handleMark) {
		this.handleMark = handleMark;
	}
	public Date getWithdrawTime() {
		return withdrawTime;
	}
	public void setWithdrawTime(Date withdrawTime) {
		this.withdrawTime = withdrawTime;
	}
    

}
