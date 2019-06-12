package com.idcq.appserver.dto.pay;

import java.io.Serializable;

import com.idcq.appserver.common.annotation.Check;

/**
 * 总店提现
 * @author ChenYongxin
 *
 */
public class WithdrawListDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3430272067180471612L;

	/*    
		money	double	是	提现金额
		shopId	int		是	分店商铺ID
	  */
	/**
	 * 商铺id
	 */
	@Check(recurse=true)
    private Long shopId;
	/**
	 * 提现金额
	 */
	@Check(recurse=true)
	private Double money;
	
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}

    

}
