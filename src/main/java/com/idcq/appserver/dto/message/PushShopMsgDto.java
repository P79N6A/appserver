package com.idcq.appserver.dto.message;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 商铺推送相关信息dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月10日
 * @time 下午4:15:52
 */
public class PushShopMsgDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7284449157322085179L;
	@JsonProperty("messageId")
	private Long pmId;
    private Long shopId;
    private String deviceType;
    private String snId;
    private String regId;
    private String action;
    private String messageContent;
    private Integer messageStatus;
    private Date sendTime;
	public PushShopMsgDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getPmId() {
		return pmId;
	}
	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSnId() {
		return snId;
	}
	public void setSnId(String snId) {
		this.snId = snId;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public Integer getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
    
    
}