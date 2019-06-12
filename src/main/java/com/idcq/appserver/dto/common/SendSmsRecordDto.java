package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.Date;
/**
 * 短信发送记录
 * @author nie_jq
 * @time 2015.05.20
 *
 */
public class SendSmsRecordDto implements Serializable{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = 804931128625593151L;
    private Long sendId;
	private String sendUsage;//发送场景
	private String sendCode;//发送验证码
	private String sendMobile;//手机号码
	private String sendContent;//发送内容
	private String sendChannle;//发送通道
	private Date sendTime;//发送时间
	private Integer sendStatus;//发送状态
	/**
	 * 返回的消息id,创蓝会返回
	 */
	private String msgId;

	private Date createTime;

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public SendSmsRecordDto(){
		
	}
	public Long getSendId() {
		return sendId;
	}
	public void setSendId(Long sendId) {
		this.sendId = sendId;
	}
	public String getSendUsage() {
		return sendUsage;
	}
	public void setSendUsage(String sendUsage) {
		this.sendUsage = sendUsage;
	}
	public String getSendMobile() {
		return sendMobile;
	}
	public void setSendMobile(String sendMobile) {
		this.sendMobile = sendMobile;
	}
	public String getSendContent() {
		return sendContent;
	}
	public void setSendContent(String sendContent) {
		this.sendContent = sendContent;
	}
	public String getSendChannle() {
		return sendChannle;
	}
	public void setSendChannle(String sendChannle) {
		this.sendChannle = sendChannle;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}
	public String getSendCode() {
		return sendCode;
	}
	public void setSendCode(String sendCode) {
		this.sendCode = sendCode;
	}
	public String getMsgId()
    {
        return msgId;
    }
    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }
    @Override
	public String toString() {
		return "SendSmsRecordDto [sendId=" + sendId + ", sendUsage="
				+ sendUsage + ", sendCode=" + sendCode + ", sendMobile="
				+ sendMobile + ", sendContent=" + sendContent
				+ ", sendChannle=" + sendChannle + ", sendTime=" + sendTime
				+ ", sendStatus=" + sendStatus + "]";
	}
	
}
