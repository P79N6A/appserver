package com.idcq.appserver.dto.cashcard;

import java.io.Serializable;
import java.util.Date;

public class CashCardBatchLogDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 700674185289614806L;

    private Long logId;
    private Long cashCardBatchId;
    private Integer operateType;
    private Integer opertaterId;
    private Date operateTime;
    private String logDesc;
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Integer Long) {
		this.logId = logId;
	}
	public Long getCashCardBatchId() {
		return cashCardBatchId;
	}
	public void setCashCardBatchId(Long cashCardBatchId) {
		this.cashCardBatchId = cashCardBatchId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public Integer getOpertaterId() {
		return opertaterId;
	}
	public void setOpertaterId(Integer opertaterId) {
		this.opertaterId = opertaterId;
	}
	public Date getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}
	public String getLogDesc() {
		return logDesc;
	}
	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}
}
