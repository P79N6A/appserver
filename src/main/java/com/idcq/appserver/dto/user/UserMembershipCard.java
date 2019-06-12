/**
 * 
 */
package com.idcq.appserver.dto.user;

import java.util.Date;

/**
 * @author Administrator
 *
 */
public class UserMembershipCard {

	/**
	 * 会员卡ID
	 */
	Long accountId;
	/**
	 * 用户ID
	 */
	Long userId;
	/**
	 * 商铺会员卡类型ID
	 */
	Long cardTypeId;
	/**
	 * 商铺id
	 */
	Long shopId;
	/**
	 * 商铺会员卡号
	 */
	String cardNo;
	/**
	 * 卡余额
	 */
	Double amount;
	/**
	 * 创建时间
	 */
	Date createTime;
	
	
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
	public Long getCardTypeId() {
		return cardTypeId;
	}
	public void setCardTypeId(Long cardTypeId) {
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
