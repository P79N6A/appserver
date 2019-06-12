package com.idcq.appserver.dto.attention;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyTimeSerializer;

public class UserAttentionDto implements Serializable {

	private static final long serialVersionUID = 7310611984346243218L;

	@JsonIgnore
	private Long attentionId;
	@JsonIgnore
	private Long userId;

	private Long shopId;
	@JsonIgnore
	private Date createTime;

	/**---------------------------**/
	private String shopName;
	private String shopLogoUrl;
	private double starLevelGrade;
	private double serviceGrade;
	private double envGrade;
	@JsonSerialize(using = MyTimeSerializer.class) 
	private Date startBTime;
	@JsonSerialize(using = MyTimeSerializer.class) 
	private Date stopBTime;
	private String address;
	private String telephone;
	private String shopInfrastructure;
	private double longitude;
	private double latitude;
	private int redPacketFlag;
	@JsonIgnore
	private int fullSentFlag;
	private int couponFlag;
	private int cashCouponFlag;
	private int timedDiscountFlag;
	
	public UserAttentionDto() {
		super();
	}

	public Long getAttentionId() {
		return attentionId;
	}

	public void setAttentionId(Long attentionId) {
		this.attentionId = attentionId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getShopLogoUrl() {
		return shopLogoUrl;
	}

	public void setShopLogoUrl(String shopLogoUrl) {
		this.shopLogoUrl = shopLogoUrl;
	}

	public double getStarLevelGrade() {
		return starLevelGrade;
	}

	public void setStarLevelGrade(double starLevelGrade) {
		this.starLevelGrade = starLevelGrade;
	}

	public double getServiceGrade() {
		return serviceGrade;
	}

	public void setServiceGrade(double serviceGrade) {
		this.serviceGrade = serviceGrade;
	}

	public double getEnvGrade() {
		return envGrade;
	}

	public void setEnvGrade(double envGrade) {
		this.envGrade = envGrade;
	}

	public Date getStartBTime() {
		return startBTime;
	}

	public void setStartBTime(Date startBTime) {
		this.startBTime = startBTime;
	}

	public Date getStopBTime() {
		return stopBTime;
	}

	public void setStopBTime(Date stopBTime) {
		this.stopBTime = stopBTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getShopInfrastructure() {
		return shopInfrastructure;
	}

	public void setShopInfrastructure(String shopInfrastructure) {
		this.shopInfrastructure = shopInfrastructure;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public int getRedPacketFlag() {
		return redPacketFlag;
	}

	public void setRedPacketFlag(int redPacketFlag) {
		this.redPacketFlag = redPacketFlag;
	}

	public int getFullSentFlag() {
		return fullSentFlag;
	}

	public void setFullSentFlag(int fullSentFlag) {
		this.fullSentFlag = fullSentFlag;
	}

	public int getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(int couponFlag) {
		this.couponFlag = couponFlag;
	}

	public int getCashCouponFlag() {
		return cashCouponFlag;
	}

	public void setCashCouponFlag(int cashCouponFlag) {
		this.cashCouponFlag = cashCouponFlag;
	}

	public int getTimedDiscountFlag() {
		return timedDiscountFlag;
	}

	public void setTimedDiscountFlag(int timedDiscountFlag) {
		this.timedDiscountFlag = timedDiscountFlag;
	}
	
}