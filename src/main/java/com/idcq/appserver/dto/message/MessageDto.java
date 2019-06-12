package com.idcq.appserver.dto.message;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 消息dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月4日
 * @time 下午3:48:33
 */
public class MessageDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1301803228847718667L;
	private Long messageId;
	private Integer msgType;	//消息类型
	private String msgTitle;	//消息标题
	private String msgDesc; 	//消息描述
	private String msgImg; 		//消息图片
	@JsonIgnore
	private String merchantId;	//商家ID 
	@JsonIgnore
	private String merchantName;//商家名称
	private Date pubTime;		//发布时间
	private Long shopId;		//商铺ID
	private String shopName;	//商铺名称
	@JsonIgnore
	private String targetUserType;	//目标类型
	@JsonIgnore
	private Long provinceId;
	@JsonIgnore
	private Long cityId;
	@JsonIgnore
	private Long districtId;
	@JsonIgnore
	private Long townId;
	
	public MessageDto() {
		super();
	}
	public Long getMessageId() {
		return messageId;
	}


	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}


	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
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

	public String getMsgImg() {
		return msgImg;
	}

	public void setMsgImg(String msgImg) {
		this.msgImg = msgImg;
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

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getTargetUserType() {
		return targetUserType;
	}

	public void setTargetUserType(String targetUserType) {
		this.targetUserType = targetUserType;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}
	
}
