package com.idcq.appserver.dto.bill;

import java.util.Date;

/**
 * 用户非现金账单
 * @author Administrator
 *
 */
public class UserXBillDto {

	//用户账单ID
	private Long xbillId;
	
	//用户代金券ID
	private Long uccId;
	
	//会员id
	private Long userId;
	
	//用户类型
	private String userRole;
	
	//账单类型
	private Integer billType;
	
	//账单状态
	private Integer billStatus;
	
	//账单金额
	private Double money;
	
	//处理前的账户余额
	private Double accountAmount; 
	
	//订单的唯一标识
	private String orderId; 
	
	//订单支付类型
	private Integer orderPayType;
	
	//创建时间
	private Date createTime;
	
	//账单说明
	private String billDesc;
	
	//账单标题
	private String billTitle;

	public Long getXbillId() {
		return xbillId;
	}

	public void setXbillId(Long xbillId) {
		this.xbillId = xbillId;
	}

	public Long getUccId() {
		return uccId;
	}

	public void setUccId(Long uccId) {
		this.uccId = uccId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public Integer getBillType() {
		return billType;
	}

	public void setBillType(Integer billType) {
		this.billType = billType;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(Double accountAmount) {
		this.accountAmount = accountAmount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderPayType() {
		return orderPayType;
	}

	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBillDesc() {
		return billDesc;
	}

	public void setBillDesc(String billDesc) {
		this.billDesc = billDesc;
	}

	public String getBillTitle() {
		return billTitle;
	}

	public void setBillTitle(String billTitle) {
		this.billTitle = billTitle;
	}
}
