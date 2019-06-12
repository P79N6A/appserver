package com.idcq.appserver.dto.packet;

import java.io.Serializable;
import java.util.Date;
/**
 * 红包dto
 * @author Administrator
 *
 */
public class RedPacket implements Serializable {
    
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -9216075607745623062L;
    
    private Long redPacketId;
    private Long userId;
    private Double amount;
    private Double price;
    private Date createTime;
    private String sourceOrderId;
    private Integer status;
    private Long shopId;
    private Long businessAreaActivityId;
    private Integer clientSystemType;
	private Long giveUserId;
	private Date beginDate;
	private Date endDate;
	private String obtainDesc;
    public Long getRedPacketId() {
        return redPacketId;
    }
    public void setRedPacketId(Long redPacketId) {
        this.redPacketId = redPacketId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Double getAmount() {
        return amount;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getSourceOrderId() {
        return sourceOrderId;
    }
    public void setSourceOrderId(String sourceOrderId) {
        this.sourceOrderId = sourceOrderId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Long getShopId() {
        return shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public Long getBusinessAreaActivityId() {
        return businessAreaActivityId;
    }
    public void setBusinessAreaActivityId(Long businessAreaActivityId) {
        this.businessAreaActivityId = businessAreaActivityId;
    }
    public Integer getClientSystemType() {
        return clientSystemType;
    }
    public void setClientSystemType(Integer clientSystemType) {
        this.clientSystemType = clientSystemType;
    }
    public Long getGiveUserId() {
        return giveUserId;
    }
    public void setGiveUserId(Long giveUserId) {
        this.giveUserId = giveUserId;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getObtainDesc() {
        return obtainDesc;
    }
    public void setObtainDesc(String obtainDesc) {
        this.obtainDesc = obtainDesc;
    }
	
	
}
