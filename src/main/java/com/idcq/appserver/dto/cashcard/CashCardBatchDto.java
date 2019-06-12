package com.idcq.appserver.dto.cashcard;

import java.io.Serializable;
import java.util.Date;

public class CashCardBatchDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5934299731682918668L;

	private Long cashCardBatchId;
    private String cardName;
    private Double money;
    private Integer cardNum;
    private Double totalMoney;
    private String cardChannel;
    private Date createTime;
    private Date endTime;
    private Date beginTime;
    private String batchNo;
    private Integer buildStatus;
    private Integer batchStatus;
    private String batchDesc;
	 
	public Long getCashCardBatchId() {
		return cashCardBatchId;
	}
	public void setCashCardBatchId(Long cashCardBatchId) {
		this.cashCardBatchId = cashCardBatchId;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getCardNum() {
		return cardNum;
	}
	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public String getCardChannel() {
		return cardChannel;
	}
	public void setCardChannel(String cardChannel) {
		this.cardChannel = cardChannel;
	}
	public Date getCreateTime()
    {
        return createTime;
    }
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }
    public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Date getBeginTime()
    {
        return beginTime;
    }
    public void setBeginTime(Date beginTime)
    {
        this.beginTime = beginTime;
    }
    public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public Integer getBuildStatus() {
		return buildStatus;
	}
	public void setBuildStatus(Integer buildStatus) {
		this.buildStatus = buildStatus;
	}
	public Integer getBatchStatus() {
		return batchStatus;
	}
	public void setBatchStatus(Integer batchStatus) {
		this.batchStatus = batchStatus;
	}
	public String getBatchDesc() {
		return batchDesc;
	}
	public void setBatchDesc(String batchDesc) {
		this.batchDesc = batchDesc;
	}
}
