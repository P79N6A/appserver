package com.idcq.appserver.dto.order;

import java.io.Serializable;

/**
 * 组合支付参数接收
 * @author nei_jq
 * @date 2015-11-23
 *
 */
public class MultiPayDto  implements Serializable{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -4409140405783474908L;
    private Long shopId;
	private String token;
	private Integer payType;
	private Double payMoney;//需支付金额
	private String payClientTime;//客户端支付时间
	private Integer payIndex;//订单内支付次序。从1开始。
	private Integer veriCode;//短信支付验证码。payType=8时，必填
	private Double realCharges;//当次实收金额
	private Double oddChange;//当次找零金额
	private Integer clientPayId;//客户端ID
	private Integer clientSystem;
	public Integer getClientSystem() {
		return clientSystem;
	}
	public void setClientSystem(Integer clientSystem) {
		this.clientSystem = clientSystem;
	}
	/**
	 * 订单支付完成后是否自动结账。0:不结账 1:结账 
	 */
	private Integer autoSettleFlag;
	private MultiPayOrderDto data;
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
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public String getPayClientTime() {
		return payClientTime;
	}
	public void setPayClientTime(String payClientTime) {
		this.payClientTime = payClientTime;
	}
	public Integer getPayIndex() {
		return payIndex;
	}
	public void setPayIndex(Integer payIndex) {
		this.payIndex = payIndex;
	}
	public Integer getVeriCode() {
		return veriCode;
	}
	public void setVeriCode(Integer veriCode) {
		this.veriCode = veriCode;
	}
	
	public MultiPayOrderDto getData() {
		return data;
	}
	public void setData(MultiPayOrderDto data) {
		this.data = data;
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
	public Integer getClientPayId() {
		return clientPayId;
	}
	public void setClientPayId(Integer clientPayId) {
		this.clientPayId = clientPayId;
	}
	public Integer getAutoSettleFlag() {
        return autoSettleFlag;
    }
    public void setAutoSettleFlag(Integer autoSettleFlag) {
        this.autoSettleFlag = autoSettleFlag;
    }
    @Override
	public String toString() {
		return "MultiPayDto [shopId=" + shopId + ", token=" + token
				+ ", payType=" + payType + ", payMoney=" + payMoney
				+ ", payClientTime=" + payClientTime + ", payIndex=" + payIndex
				+ ", veriCode=" + veriCode + ", realCharges=" + realCharges
				+ ", oddChange=" + oddChange + ", clientPayId=" + clientPayId
				+ ", data=" + data + "]";
	}
}
