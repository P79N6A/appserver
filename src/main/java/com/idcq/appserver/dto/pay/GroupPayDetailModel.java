package com.idcq.appserver.dto.pay;

import java.io.Serializable;

import com.idcq.appserver.common.annotation.Check;

public class GroupPayDetailModel implements Serializable {

	private static final long serialVersionUID = -5392447093965018731L;
	
	private Integer payType;
	private Double payAmount;
	private Long accountId;
	private Integer clientSystem;
	public Integer getClientSystem() {
		return clientSystem;
	}
	public void setClientSystem(Integer clientSystem) {
		this.clientSystem = clientSystem;
	}
	public Long getAccountId() {
        return accountId;
    }
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
}
