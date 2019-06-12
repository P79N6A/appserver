package com.idcq.appserver.dto.pay;

import java.io.Serializable;
import java.util.List;

public class GroupPayModel implements Serializable {

	private static final long serialVersionUID = 5370209273337356230L;
	
	private Long userId;
	private String orderId;
	private Double orderAmount;
	private String payPassword;
	private String veriCode;
	private String usage;
	private Integer autoSettleFlag;
	public Integer getAutoSettleFlag() {
		return autoSettleFlag;
	}
	public void setAutoSettleFlag(Integer autoSettleFlag) {
		this.autoSettleFlag = autoSettleFlag;
	}
	private List<GroupPayDetailModel> payInfo;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Double getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getVeriCode() {
        return veriCode;
    }
    public void setVeriCode(String veriCode) {
        this.veriCode = veriCode;
    }
    public List<GroupPayDetailModel> getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(List<GroupPayDetailModel> payInfo) {
		this.payInfo = payInfo;
	}
    public String getUsage() {
        return usage;
    }
    public void setUsage(String usage) {
        this.usage = usage;
    }

}
