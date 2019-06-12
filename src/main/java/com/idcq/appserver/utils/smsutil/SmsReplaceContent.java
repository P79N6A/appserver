package com.idcq.appserver.utils.smsutil;

import java.io.Serializable;
import java.util.Date;

public class SmsReplaceContent implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1445433574854420646L;

	private String mobile;
	
	@IngoreField
	private String usage;
	
	private String code;
	
	private String shopName;
	
	private Double acountAmount;
	
	private Integer status;
	
	private Double amount;
	private Double userRebateAmount;
	private String username;
	private Date createTime;
	
	private String pwd;
	
	@IngoreField
	private long sendTime = 0l;
	
	@IngoreField
	private Boolean cacheCodeFlag;//是否需要缓存Code

	@IngoreField
	private Boolean createCodeFlag;//是否需要创建Code

	@IngoreField
	private String usageFlag;//标记当前实际获取内容的usage(usage+usageFlag)
	
	private String consumeDate; 
	
	private Double onLinePayment;
	 
	private Double cashCouponPayment;
	 
	private Double cashPayment;
	
	private String activityName;//活动名称
	/**
	 *  短信账户类型：0:验证账户类型，1:营销短信类型
	 */
	@IngoreField
	private String smsType;
	
	private Double usedRedPacketMoney;
	
	/**
	 * 充值人姓名
	 */
	private String shopManagerName;
	//【shopName】尊敬的客户，couponName已经放入您的账户，有效期beginDate至endDate，到店消费couponUsedCondition即可使用。
	/**
	 * 优惠券名称
	 */
	private String couponName;
	/**
	 * 开始时间
	 */
	private String beginDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	/**
	 * 使用条件
	 */
	private Double couponUsedCondition;
	/**
	 * 面额
	 */
	private Double couponAmount;
	/**
	 * 金额
	 */
	private Double money;

	/**
	 * 充值金额
	 */
	private Double chargeMoney;
	
	/**
	 * 短信内容,如果想直接发送短信内容，只需设置这个值
	 */
	private String content; 
	
	public Double getUserRebateAmount() {
		return userRebateAmount;
	}
	public void setUserRebateAmount(Double userRebateAmount) {
		this.userRebateAmount = userRebateAmount;
	}
	private Double payAmount;
	
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Double getAcountAmount() {
		return acountAmount;
	}
	public void setAcountAmount(Double acountAmount) {
		this.acountAmount = acountAmount;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	public long getSendTime() {
		return sendTime;
	}
	public void setSendTime(long sendTime) {
		this.sendTime = sendTime;
	}
	public Boolean isCacheCodeFlag() {
		if (cacheCodeFlag == null) {
			return false;
		}
		return cacheCodeFlag;
	}
	public void setCacheCodeFlag(Boolean cacheCodeFlag) {
		this.cacheCodeFlag = cacheCodeFlag;
	}
	public Boolean isCreateCodeFlag() {
		if (createCodeFlag == null) {
			return false;
		}
		return createCodeFlag;
	}
	public void setCreateCodeFlag(Boolean createCodeFlag) {
		this.createCodeFlag = createCodeFlag;
	}
	public String getUsageFlag() {
		if (null == usageFlag) {
			return "";
		}
		return usageFlag.trim();
	}
	public void setUsageFlag(String usageFlag) {
		this.usageFlag = usageFlag;
	}
	public String getConsumeDate() {
		return consumeDate;
	}
	public void setConsumeDate(String consumeDate) {
		this.consumeDate = consumeDate;
	}
	public Double getOnLinePayment() {
		return onLinePayment;
	}
	public void setOnLinePayment(Double onLinePayment) {
		this.onLinePayment = onLinePayment;
	}
	public Double getCashCouponPayment() {
		return cashCouponPayment;
	}
	public void setCashCouponPayment(Double cashCouponPayment) {
		this.cashCouponPayment = cashCouponPayment;
	}
	public Double getCashPayment() {
		return cashPayment;
	}
	public void setCashPayment(Double cashPayment) {
		this.cashPayment = cashPayment;
	}
	
	
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	
    public Double getUsedRedPacketMoney() {
        return usedRedPacketMoney;
    }
    public void setUsedRedPacketMoney(Double usedRedPacketMoney) {
        this.usedRedPacketMoney = usedRedPacketMoney;
    }
    public String getSmsType() {
        return smsType;
    }
    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }
    
    public String getShopManagerName() {
		return shopManagerName;
	}
	public void setShopManagerName(String shopManagerName) {
		this.shopManagerName = shopManagerName;
	}
	public Double getChargeMoney() {
		return chargeMoney;
	}
	public void setChargeMoney(Double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCouponName() {
		return couponName;
	}
	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Double getCouponUsedCondition() {
		return couponUsedCondition;
	}
	public void setCouponUsedCondition(Double couponUsedCondition) {
		this.couponUsedCondition = couponUsedCondition;
	}
	public Boolean getCacheCodeFlag() {
		return cacheCodeFlag;
	}
	public Boolean getCreateCodeFlag() {
		return createCodeFlag;
	}
	public Double getCouponAmount() {
		return couponAmount;
	}
	public void setCouponAmount(Double couponAmount) {
		this.couponAmount = couponAmount;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "SmsReplaceContent [mobile=" + mobile + ", usage=" + usage
				+ ", code=" + code + ", shopName=" + shopName
				+ ", acountAmount=" + acountAmount + ", amount=" + amount
				+ ", username=" + username + ", pwd=" + pwd + ", sendTime="
				+ sendTime + ", cacheCodeFlag=" + cacheCodeFlag
				+ ", createCodeFlag=" + createCodeFlag + ", usageFlag="
				+ usageFlag + ", consumeDate=" + consumeDate
				+ ", onLinePayment=" + onLinePayment + ", cashCouponPayment="
				+ cashCouponPayment + ", cashPayment=" + cashPayment
				+ ", activityName=" + activityName + ", smsType=" + smsType
				+ ", usedRedPacketMoney=" + usedRedPacketMoney
				+ ", shopManagerName=" + shopManagerName + ", couponName="
				+ couponName + ", beginDate=" + beginDate + ", endDate="
				+ endDate + ", couponUsedCondition=" + couponUsedCondition
				+ ", couponAmount=" + couponAmount + ", chargeMoney="
				+ chargeMoney + "]";
	}

    
	
}
