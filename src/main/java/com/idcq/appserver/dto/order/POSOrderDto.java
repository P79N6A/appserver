package com.idcq.appserver.dto.order;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.utils.ToStringMethod;

/**
 * POS订单详情dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月13日
 * @time 上午11:49:23
 */
public class POSOrderDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7866813309990771896L;
	private String token;
	private Long shopId;
	private POSOrderDataDto data;
	
	/**
	 * 扫码支付
	 */
	private Integer scanPay;
	//冗余用户信息
	@JsonIgnore
	private UserDto user;
	@JsonIgnore
	private int addOrEditFlag ;	//新增修改操作标志：1新增；0修改；
	@JsonIgnore
	private Integer settleFlag ;	//结算标志
	@JsonIgnore
    private int addOrderLogFlag ; //新增订单状态日志操作标志：1新增；0修改；
	
	
	@Override
	public String toString(){
		return ToStringMethod.toString(this);
	}
	public POSOrderDto() {
		super();
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public POSOrderDataDto getData() {
		return data;
	}
	public void setData(POSOrderDataDto data) {
		this.data = data;
	}
	public UserDto getUser() {
		return user;
	}
	public void setUser(UserDto user) {
		this.user = user;
	}

	public int getAddOrEditFlag() {
		return addOrEditFlag;
	}

	public void setAddOrEditFlag(int addOrEditFlag) {
		this.addOrEditFlag = addOrEditFlag;
	}

	public Integer getSettleFlag() {
		return settleFlag;
	}

	public void setSettleFlag(Integer settleFlag) {
		this.settleFlag = settleFlag;
	}
	public Integer getScanPay() {
		return scanPay;
	}
	public void setScanPay(Integer scanPay) {
		this.scanPay = scanPay;
	}
    public int getAddOrderLogFlag()
    {
        return addOrderLogFlag;
    }
    public void setAddOrderLogFlag(int addOrderLogFlag)
    {
        this.addOrderLogFlag = addOrderLogFlag;
    }

	
	
}
