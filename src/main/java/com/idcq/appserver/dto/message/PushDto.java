package com.idcq.appserver.dto.message;

import java.io.Serializable;

/**
 * 推送dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月14日
 * @time 上午11:47:05
 */
public class PushDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7334890044546075221L;
	private String regId;		//极光平台的注册ID
	private String content;		//推送内容
	private String action;		
	private String platForm;	//目标平台，如"ios"苹果,"and"安卓
	private Long userId;		//用户ID
	private Long shopId;		//商铺ID
	private String title;
	
	public PushDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getPlatForm() {
		return platForm;
	}
	public void setPlatForm(String platForm) {
		this.platForm = platForm;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
