package com.idcq.appserver.dto.message;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户推送相关信息dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月10日
 * @time 下午4:19:39
 */
public class PushUserMsgDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1024320856349929910L;
	private Long pmId;
    private Long userId;
    private String regId;
    private String action;
    private String messageContent;
    private Integer messageStatus;
    private Date sendTime;
	private Date createTime;
	//会员-0,店铺管理者-10
	private Integer userType;
	//ios,restaurant,goods,service,auto,others
	private String osInfo;
	private String title;

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
	}

	public String getOsInfo()
	{
		return osInfo;
	}

	public void setOsInfo(String osInfo)
	{
		this.osInfo = osInfo;
	}

	public Date getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}

	public PushUserMsgDto() {
		super();
	}

	public Long getPmId() {
		return pmId;
	}

	public void setPmId(Long pmId) {
		this.pmId = pmId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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