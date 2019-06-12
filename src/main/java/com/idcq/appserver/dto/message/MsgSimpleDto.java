package com.idcq.appserver.dto.message;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:48:33
 */
public class MsgSimpleDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1554437694584964428L;
	private long id;
	private int msgType;	//消息类型
	private String msgTitle;//消息标题
	private String msgDesc; //消息描述
	private String merchantId;//商家ID 
	private String merchantName;//商家名称
	private Date pubTime;	//发布时间
	
	public MsgSimpleDto() {
		super();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgDesc() {
		return msgDesc;
	}
	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public Date getPubTime() {
		return pubTime;
	}
	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}
	
	
}
