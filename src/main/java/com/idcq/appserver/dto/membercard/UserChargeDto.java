package com.idcq.appserver.dto.membercard;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;
/**
 * 用户账单类
 * @author Administrator
 *
 */
public class UserChargeDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8790647474439727446L;
	
	//用户账单id
	private Long billId;
	
	//用户ID
	private Long userId;
	
	//充值类型：1、充值到传奇宝 2、充值到会员卡
	private Integer chargeType;
	
	//会员卡ID
	private Long umcId;
	
	//充值金额
	private Double amount;
	
	//终端类型
	private String terminalType;
    //订单id
    private String orderId;
	
	//支付平台名称
	@JsonProperty(value="3rdOrgName")
	private String thirdOrgName;
	
	//账单类型:1（账户资金增加）,-1（账户资金减少）
	private Integer billDirection;
	
	//交易流水号
	private Long transactionId;
	
	//创建时间
	private Date createTime;
	
	//账单描述
	private String billDesc;
	
	//消费者的用户ID
	private Long consumerUserId;
	
	//消费者的用户手机
	private String consumerMobile;
	
	//用户类型
	private String userRole;
	
	//账单类型
	private String billType;
	
	//账单类型
	private String billTitle;
	//账单状态
	private Integer billStatus;
	//账单状态标记
	private Integer billStatusFlag;
	//余额
	private Double accountAmount;
	//用户图片id
	private Long billLogo;
	
	private Double accountAfterAmount; // 使用后资金余额
	private Integer accountType; // 账户类型：1=平台奖励，2=冻结资金，3=消费金
	private Integer userBillType; // 账单类型:1=消费,2=提现,3=提现退回,4=消费返利,5=推荐会员奖励,6=推荐店铺奖励,7=服务店铺奖励,8=市级代理奖励,9=区县代理奖励,10=乡镇代理奖励,11=冻结资金,12=解冻资金,30=支付宝充值,31=建行借记卡充值,32=建行信用卡充值
	
	
	
	
	public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public Double getAccountAfterAmount() {
		return accountAfterAmount;
	}

	public void setAccountAfterAmount(Double accountAfterAmount) {
		this.accountAfterAmount = accountAfterAmount;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getUserBillType() {
		return userBillType;
	}

	public void setUserBillType(Integer userBillType) {
		this.userBillType = userBillType;
	}

	public Long getBillLogo() {
		return billLogo;
	}

	public void setBillLogo(Long billLogo) {
		this.billLogo = billLogo;
	}

	public Integer getBillStatus() {
		return billStatus;
	}

	public Integer getBillStatusFlag() {
		return billStatusFlag;
	}

	public void setBillStatusFlag(Integer billStatusFlag) {
		this.billStatusFlag = billStatusFlag;
	}

	public Double getAccountAmount() {
		return accountAmount;
	}

	public void setAccountAmount(Double accountAmount) {
		this.accountAmount = accountAmount;
	}

	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}

	public Long getBillId() {
		return billId;
	}

	public void setBillId(Long billId) {
		this.billId = billId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public Long getUmcId() {
		return umcId;
	}

	public void setUmcId(Long umcId) {
		this.umcId = umcId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getThirdOrgName() {
		return thirdOrgName;
	}

	public void setThirdOrgName(String thirdOrgName) {
		this.thirdOrgName = thirdOrgName;
	}

	public Integer getBillDirection() {
		return billDirection;
	}

	public void setBillDirection(Integer billDirection) {
		this.billDirection = billDirection;
	}



	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
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

	public Long getConsumerUserId() {
		return consumerUserId;
	}

	public void setConsumerUserId(Long consumerUserId) {
		this.consumerUserId = consumerUserId;
	}

	public String getConsumerMobile() {
		return consumerMobile;
	}

	public void setConsumerMobile(String consumerMobile) {
		this.consumerMobile = consumerMobile;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillTitle() {
		return billTitle;
	}

	public void setBillTitle(String billTitle) {
		this.billTitle = billTitle;
	}

}
