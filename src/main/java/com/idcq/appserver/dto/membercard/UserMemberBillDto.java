package com.idcq.appserver.dto.membercard;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class UserMemberBillDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9123906604256489114L;

    private Long billId;

    @JsonIgnore
    private Long userId;

    private String billType;

    private Byte billDirection;

    private Byte billStatus;

    private BigDecimal money;

    @JsonIgnore
    private Long transactionId;

    private Date createTime;

    @JsonIgnore
    private String billDesc;

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public Byte getBillDirection() {
        return billDirection;
    }

    public void setBillDirection(Byte billDirection) {
        this.billDirection = billDirection;
    }

    public Byte getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(Byte billStatus) {
        this.billStatus = billStatus;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBillDesc() {
        return billDesc;
    }

    public void setBillDesc(String billDesc) {
        this.billDesc = billDesc;
    }
}
