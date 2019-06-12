package com.idcq.appserver.dto.pay;

import java.io.Serializable;
import java.util.Date;

/**
 * 非会员支付信息
 * @author Administrator
 * 
 * @date 2015年9月11日
 * @time 上午11:05:02
 */
public class XorderPayDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6805499465897741318L;
	private Long xorderPayId;
    private String xorderId;
    private Integer payType;
    private Integer payId;
    private Double payAmount;
    private Integer orderPayType;
    private Date orderPayTime;
    private Date lastUpdateTime;
    private Integer payeeType;
    private Long shopId;
    private Integer payIndex;//支付序号
    private Date userPayTime;//客户端支付时间
    
	public XorderPayDto() {
		super();
	}

	public Long getXorderPayId() {
		return xorderPayId;
	}

	public void setXorderPayId(Long xorderPayId) {
		this.xorderPayId = xorderPayId;
	}

	public String getXorderId() {
		return xorderId;
	}

	public void setXorderId(String xorderId) {
		this.xorderId = xorderId;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getPayId() {
		return payId;
	}

	public void setPayId(Integer payId) {
		this.payId = payId;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getOrderPayType() {
		return orderPayType;
	}

	public void setOrderPayType(Integer orderPayType) {
		this.orderPayType = orderPayType;
	}

	public Date getOrderPayTime() {
		return orderPayTime;
	}

	public void setOrderPayTime(Date orderPayTime) {
		this.orderPayTime = orderPayTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Integer getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(Integer payeeType) {
		this.payeeType = payeeType;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getPayIndex() {
		return payIndex;
	}

	public void setPayIndex(Integer payIndex) {
		this.payIndex = payIndex;
	}

	public Date getUserPayTime() {
		return userPayTime;
	}

	public void setUserPayTime(Date userPayTime) {
		this.userPayTime = userPayTime;
	}
}