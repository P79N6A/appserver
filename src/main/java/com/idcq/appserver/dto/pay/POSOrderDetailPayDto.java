package com.idcq.appserver.dto.pay;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

/**
 * 收银订单详情支付记录
 * @author Administrator
 * 
 * @date 2015年12月28日
 * @time 下午3:22:43
 */
public class POSOrderDetailPayDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2880340771264723933L;
	
	private int payType;
	
	private Long clientPayId;
	
	private double payAmount;//当次支付
	
	private Double realCharges;//当次实收
	
	private Double oddChange;//当次找零
	
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date orderPayTime;//服务器支付时间
	
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date userPayTime;//客户端支付时间
	
	private Integer payIndex;//支付次序
	
	private Integer payChannel;//支付通道
	
    private Integer payStatus;//支付状态
	
	

	public POSOrderDetailPayDto() {
		super();
	}


	public int getPayType() {
		return payType;
	}


	public void setPayType(int payType) {
		this.payType = payType;
	}


	public Long getClientPayId() {
		return clientPayId;
	}


	public void setClientPayId(Long clientPayId) {
		this.clientPayId = clientPayId;
	}


	public double getPayAmount() {
		return payAmount;
	}


	public void setPayAmount(double payAmount) {
		this.payAmount = payAmount;
	}


	public Double getRealCharges() {
		return realCharges;
	}


	public void setRealCharges(Double realCharges) {
		this.realCharges = realCharges;
	}


	public Double getOddChange() {
		return oddChange;
	}


	public void setOddChange(Double oddChange) {
		this.oddChange = oddChange;
	}


	public Date getOrderPayTime() {
		return orderPayTime;
	}


	public void setOrderPayTime(Date orderPayTime) {
		this.orderPayTime = orderPayTime;
	}


	public Date getUserPayTime() {
		return userPayTime;
	}


	public void setUserPayTime(Date userPayTime) {
		this.userPayTime = userPayTime;
	}


	public Integer getPayIndex() {
		return payIndex;
	}


	public void setPayIndex(Integer payIndex) {
		this.payIndex = payIndex;
	}


	public Integer getPayChannel() {
		return payChannel;
	}


	public void setPayChannel(Integer payChannel) {
		this.payChannel = payChannel;
	}


	public Integer getPayStatus() {
		return payStatus;
	}


	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

}