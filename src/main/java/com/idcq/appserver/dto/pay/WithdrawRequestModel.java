package com.idcq.appserver.dto.pay;

import java.io.Serializable;
import java.util.List;

import com.idcq.appserver.common.annotation.Check;

/**
 * 总店提现接收model
 * @author ChenYongxin
 *
 */
public class WithdrawRequestModel implements Serializable{

	private static final long serialVersionUID = -2816503404237765365L;
	/**
	 * 总部商铺id
	 */
	@Check(recurse=true)
    private Long shopId;
	private String token;
	/**
	 * 银行卡号
	 */
	 @Check(recurse=true)
	private String cardNumber;
	/**
	 * 支付密码的MD5加密串
	 */
	 @Check(recurse=true)
	private String payPassword;
    @Check(recurse=true)
    List<WithdrawListDto> withdrawList;
    
    
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<WithdrawListDto> getWithdrawList() {
		return withdrawList;
	}
	public void setWithdrawList(List<WithdrawListDto> withdrawList) {
		this.withdrawList = withdrawList;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

    

}
