package com.idcq.appserver.dto.message;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 消息设置dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:48:33
 */
public class MessageSettingDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1301803228847718667L;
	@JsonIgnore
	private Long settingId;
	private String settingKey;	//
	private String settingName;
	private Integer remandFlag;	//提醒标记：1（开启提醒），0（关闭提醒）
	private String msgTitle;
	private String msgContent; //消息模板
	
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public Long getSettingId() {
		return settingId;
	}
	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}
	public String getSettingKey() {
		return settingKey;
	}
	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}
	public String getSettingName() {
		return settingName;
	}
	public void setSettingName(String settingName) {
		this.settingName = settingName;
	}
	public Integer getRemandFlag() {
		return remandFlag;
	}
	public void setRemandFlag(Integer remandFlag) {
		this.remandFlag = remandFlag;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	
}
