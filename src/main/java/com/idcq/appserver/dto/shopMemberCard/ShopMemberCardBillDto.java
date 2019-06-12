package com.idcq.appserver.dto.shopMemberCard;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 店铺会员卡账单
 * @ClassName: ShopMemberCardBillDto 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午1:43:20 
 *
 */
public class ShopMemberCardBillDto {
	
	/**
	 * 会员卡账单id
	 */
	private Integer cardBillId;
	
	/**
	 * 会员卡id
	 */
	private Integer cardId;
	
	/** 
	* @Fields cardType : 1.充值卡，2.次卡
	*/ 
	private String cardType;//卡类型
	
	/**
	 * 账单类型
	 */
	private Integer cardBillType;
	
	/**
	 * 充值金额
	 */
	private Double chargeMoney;
	
	/**
	 * 赠送金额
	 */
	private Double presentMoney;
	
	/**
	 * 账单后金额
	 */
	private Double afterAmount;
	
	/**
	 * 账单金额
	 */
	private Double billAmount;
	
	/**
	 * 账单操作人如消费人，
	 */
	private Integer opertaterId;
	
	/**
	 * 账单时间
	 */
	private Date billTime;
	
	/**
	 * 订单编号
	 */
	private String orderId;
	
	/***
	 * 支付id，关联支付表
	 */
	private Long payId;
	
	
	/**
	 * 账单描述
	 */
	private String cardBillDesc;
	
	/**
	 * 店铺编号
	 */
	private Integer shopId;

	/**
	 * 充值类型
	 */
	private Integer chargeType;
	
	
	private String mobile;
	
	/**
	 * 账单开始时间
	 */
	@JsonIgnore
	private String billStartTime;
	
	/**
	 * 账单结束时间
	 */
	@JsonIgnore
	private String billEndTime;
	
	private Double billMoney;
	
	private Integer billId;
	
	private String billerId;//提出人
	
	private Date clientRechargeTime;
	
	public String getBillerId() {
		return billerId;
	}


	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}


	public Integer getCardBillId() {
		return cardBillId;
	}


	public void setCardBillId(Integer cardBillId) {
		this.cardBillId = cardBillId;
	}


	public Integer getCardId() {
		return cardId;
	}


	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}


	public Integer getCardBillType() {
		return cardBillType;
	}


	public void setCardBillType(Integer cardBillType) {
		this.cardBillType = cardBillType;
	}


	public Double getChargeMoney() {
		return chargeMoney;
	}


	public void setChargeMoney(Double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}


	public Double getPresentMoney() {
		return presentMoney;
	}


	public void setPresentMoney(Double presentMoney) {
		this.presentMoney = presentMoney;
	}

	public Double getBillAmount() {
		return billAmount;
	}


	public void setBillAmount(Double billAmount) {
		this.billAmount = billAmount;
	}



	public Date getBillTime() {
		return billTime;
	}


	public void setBillTime(Date billTime) {
		this.billTime = billTime;
	}


	public String getOrderId() {
		return orderId;
	}


	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}


	public Long getPayId() {
		return payId;
	}


	public void setPayId(Long payId) {
		this.payId = payId;
	}


	public String getCardBillDesc() {
		return cardBillDesc;
	}


	public void setCardBillDesc(String cardBillDesc) {
		this.cardBillDesc = cardBillDesc;
	}


	public Integer getShopId() {
		return shopId;
	}


	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}


	public Integer getChargeType() {
		return chargeType;
	}


	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}


	public Double getAfterAmount() {
		return afterAmount;
	}


	public void setAfterAmount(Double afterAmount) {
		this.afterAmount = afterAmount;
	}


	public Integer getOpertaterId() {
		return opertaterId;
	}


	public void setOpertaterId(Integer opertaterId) {
		this.opertaterId = opertaterId;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public String getBillStartTime() {
		return billStartTime;
	}


	public void setBillStartTime(String billStartTime) {
		this.billStartTime = billStartTime;
	}


	public String getBillEndTime() {
		return billEndTime;
	}


	public void setBillEndTime(String billEndTime) {
		this.billEndTime = billEndTime;
	}


	public Double getBillMoney() {
		if(billMoney==null){
			billMoney=billAmount;
		}
		return billMoney;
	}


	public void setBillMoney(Double billMoney) {
		if(billMoney==null){
			billMoney=billAmount;
		}
		this.billMoney = billMoney;
	}


	public Integer getBillId() {
		if(billId==null){
			billId=cardBillId;
		}
		return billId;
	}


	public void setBillId(Integer billId) {
		if(billId==null){
			billId=cardBillId;
		}
		this.billId = billId;
	}


	public String getCardType() {
		return cardType;
	}


	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

    public Date getClientRechargeTime() {
        return clientRechargeTime;
    }

    public void setClientRechargeTime(Date clientRechargeTime) {
        this.clientRechargeTime = clientRechargeTime;
    }
}
