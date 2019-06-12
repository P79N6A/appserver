package com.idcq.appserver.dto.pay;

import java.io.Serializable;

/**
 * 订单dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:17:34
 */
public class TransactionDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 2205395592081909944L;
	private Long transactionId;	
    private Long userId;			//用户ID
    private String orderId;			//订单ID
    private String transactionTime;
    private Integer status;		//支付状态：支付成功-1,支付失败-2
    private String statusDesc;		//支付状态说明
    private Long userPayChannelId; //用户支付渠道id
    private Double payAmount; //支付金额
    private Integer orderPayType;//订单支付类型：0(单个订单支付），1（多个订单支付
    private Integer orderType;//'1.会员订单 2.非会员订单'
	private String lastUpdateTime;
    private String rdOrgName;  //第三方支付平台的名称，比如支付宝，财富通，银联
    private Integer transactionType; //交易类型：0（消费），1（充值），2（会员卡充值）
    private String terminalType; // 发起交易的终端类型：IOS客户端,android客户端，Web客户端，PC客户端，WEB
    private String rdTransactionId;//第三方流水
    private String rdNotifyId; //第三方异步通知Id
    private String outTradeNo; //平台生成用于第三方支付的交易号
    public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getRdNotifyId() {
		return rdNotifyId;
	}
	public void setRdNotifyId(String rdNotifyId) {
		this.rdNotifyId = rdNotifyId;
	}
	private Long payeeUserId;//收款方Id;
    private String rdNotifyTime;//第三方支付平台异步通知的时间
    
    public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public String getRdNotifyTime() {
		return rdNotifyTime;
	}
	public void setRdNotifyTime(String rdNotifyTime) {
		this.rdNotifyTime = rdNotifyTime;
	}
	public Long getPayeeUserId() {
		return payeeUserId;
	}
	public void setPayeeUserId(Long payeeUserId) {
		this.payeeUserId = payeeUserId;
	}
	public String getRdTransactionId()
    {
        return rdTransactionId;
    }
    public void setRdTransactionId(String rdTransactionId)
    {
        this.rdTransactionId = rdTransactionId;
    }
    public String getRdOrgName() {
		return rdOrgName;
	}
	public void setRdOrgName(String rdOrgName) {
		this.rdOrgName = rdOrgName;
	}
	public Integer getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	public Long getUserPayChannelId() {
		return userPayChannelId;
	}
	public void setUserPayChannelId(Long userPayChannelId) {
		this.userPayChannelId = userPayChannelId;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public Integer getOrderPayType() {
		return orderPayType;
	}
	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}
	public String getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	
    
}