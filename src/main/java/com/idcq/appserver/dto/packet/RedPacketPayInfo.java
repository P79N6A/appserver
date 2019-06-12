package com.idcq.appserver.dto.packet;

import java.io.Serializable;

public class RedPacketPayInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3380063171990729017L;
	
	 private Long redPacketId;
	 private Double payAmount;
	public Long getRedPacketId() {
		return redPacketId;
	}
	public void setRedPacketId(Long redPacketId) {
		this.redPacketId = redPacketId;
	}
	public Double getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

}
