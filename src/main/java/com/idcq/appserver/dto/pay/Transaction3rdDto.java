package com.idcq.appserver.dto.pay;

import java.util.Date;

public class Transaction3rdDto {
	/**
	 * 主键
	 */
	private Long transactionId;	
    /**
     * 发起交易的终端类型：IOS客户端,android客户端，Web客户端，PC客户端，WEB
     */
    private String terminalType;
    /**
     * userid
     */
    private Long userId;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 用户支付渠道id
     */
    private Long userPayChannelId;
    /**
     * 第三方支付平台的名称，比如支付宝，财富通，银联
     */
    private String rdOrgName;
    /**
     * 支付金额
     */
    private double payAmount;
    /**
     * 交易起始时间
     */
    private Date transactionTime;
    /**
     * 支付状态：待反馈支付进度-0,支付成功-1,支付失败-2
     */
    private Integer status;	
    /**
     * 交易相关商铺的描述信息
     */
    private String shopInfo;
    /**
     * 交易相关商品信息
     */
    private String goodsInfo;
    /**
     * 交易相关商品描述信息
     */
    private String goodsDesc;
    /**
     * 交易相关商品总数
     */
    private Integer goodsNumber;
    /**
     * 三方支付平台中的交易流水号
     */
    private String rdTransactionId;
    /**
     * 第三方支付平台异步通知的时间
     */
    private Date rdNotifyTime;
    /**
     * 第三方支付平台异步通知的唯一标识
     */
    private String rdNotifyId;
    
    /**
     * 支付类型
     */
    private Integer orderPayType;
    
    private Integer transactionType;
    
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getTerminalType() {
		return terminalType;
	}
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
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
	public Long getUserPayChannelId() {
		return userPayChannelId;
	}
	public void setUserPayChannelId(Long userPayChannelId) {
		this.userPayChannelId = userPayChannelId;
	}
	public String getRdOrgName() {
		return rdOrgName;
	}
	public void setRdOrgName(String rdOrgName) {
		this.rdOrgName = rdOrgName;
	}
	
	public double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}
	public Date getTransactionTime() {
		return transactionTime;
	}
	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getShopInfo() {
		return shopInfo;
	}
	public void setShopInfo(String shopInfo) {
		this.shopInfo = shopInfo;
	}
	public String getGoodsInfo() {
		return goodsInfo;
	}
	public void setGoodsInfo(String goodsInfo) {
		this.goodsInfo = goodsInfo;
	}
	public String getGoodsDesc() {
		return goodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}
	public Integer getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public String getRdTransactionId() {
		return rdTransactionId;
	}
	public void setRdTransactionId(String rdTransactionId) {
		this.rdTransactionId = rdTransactionId;
	}
	public Date getRdNotifyTime() {
		return rdNotifyTime;
	}
	public void setRdNotifyTime(Date rdNotifyTime) {
		this.rdNotifyTime = rdNotifyTime;
	}
	public String getRdNotifyId() {
		return rdNotifyId;
	}
	public void setRdNotifyId(String rdNotifyId) {
		this.rdNotifyId = rdNotifyId;
	}
	public Integer getOrderPayType() {
		return orderPayType;
	}
	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}
	
	public Integer getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}
}
