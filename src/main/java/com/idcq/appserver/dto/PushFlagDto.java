package com.idcq.appserver.dto;

import java.io.Serializable;

/**
 * 启动消息推送标志
 * 
 * @author Administrator
 * 
 * @date 2015年4月10日
 * @time 下午5:23:33
 */
public class PushFlagDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4955649921893242294L;
	private Integer isPush;	//是否启动推送：1是；0否
	private String content;	//推送内容
	private	Integer userId;	//消息接收者
	private	Integer userType;	//消息接收者类型：1，会员；2，商铺
	
	public PushFlagDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getIsPush() {
		return isPush;
	}
	public void setIsPush(Integer isPush) {
		this.isPush = isPush;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	
	
}
