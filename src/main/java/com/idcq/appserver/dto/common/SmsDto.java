package com.idcq.appserver.dto.common;

import java.io.Serializable;

/**
 * 编码实体类
 * 
 *
 */
public class SmsDto implements Serializable{
	
	private static final long serialVersionUID = 8974791631088418681L;
	
	/**
	 * 店铺ID
	 */
	private Long shopId;
	
	private String token;
	
	/**
	 * 平台会员Id(多个与英文逗号分隔，与memberIds和mobiles互斥，且三者必填其一)
	 */
	private String userIds;
	
	/**
	 * 店内会员Id(多个与英文逗号分隔，与userIds和mobiles互斥，且三者必填其一)
	 */
	private String memberIds;
	
	/**
	 * 手机号(多个与英文逗号分隔，与userIds和memberIds互斥，且三者必填其一)
	 */
	private String mobiles;
	
	/**
	 * 发送内容
	 */
	private String smsContent;
	
	/**
	 * 短信账户类型：0:验证账户类型，1:营销短信类型
	 */
	private Integer smsType;
	
	/**
	 * 短信模版Id(与smsContent互斥)
	 */
	private Long smsModelId;
	
	/**
	 * 短信模版类型：1：生日提醒短信
	 */
	private Integer smsModelType;

	public String getUserIds() {
		return userIds;
	}

	public void setUserIds(String userIds) {
		this.userIds = userIds;
	}

	public String getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(String memberIds) {
		this.memberIds = memberIds;
	}

	public String getMobiles() {
		return mobiles;
	}

	public void setMobiles(String mobiles) {
		this.mobiles = mobiles;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public Integer getSmsType() {
		return smsType;
	}

	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getSmsModelId() {
		return smsModelId;
	}

	public void setSmsModelId(Long smsModelId) {
		this.smsModelId = smsModelId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getSmsModelType() {
		return smsModelType;
	}

	public void setSmsModelType(Integer smsModelType) {
		this.smsModelType = smsModelType;
	}
	

}
