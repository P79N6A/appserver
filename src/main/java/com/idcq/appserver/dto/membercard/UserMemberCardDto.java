package com.idcq.appserver.dto.membercard;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class UserMemberCardDto implements Serializable {
	private static final long serialVersionUID = 3350549550118283561L;

	@JsonIgnore
	private Long accountId;
	@JsonIgnore
	private Long userId;
	@JsonIgnore
	private Integer cardTypeId;
	@JsonIgnore
	private Long shopId;
	@JsonIgnore
	private String cardNo;
	private Double amount;
	@JsonIgnore
	private Date createTime;
	
	public UserMemberCardDto() {
		super();
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getCardTypeId() {
		return cardTypeId;
	}

	public void setCardTypeId(Integer cardTypeId) {
		this.cardTypeId = cardTypeId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	
}
