package com.idcq.appserver.dto.backup;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;



/**
 * 商铺dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午6:09:13
 */
public class ShopDto2 implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8159298331880528515L;
	private long shopId;	//商铺ID
	private String shopImg;	//图片
	private int shopStatus;	//状态
	private String shopName;//名称
	private String shopDesc;//描述
	private int shopType;	//类型
	private long principalId;//负责人ID
	private String telephone;//电话
	private String principalName;//法人姓名
	private String address;//地址
	private double longitude;	//经度
	private double latitude;	//纬度
	/*private String province;	//省级
	private String city;		//市级
	private String area;		//区
*/	private String businLicense;//营业执照号
	private String businLicensePic;		//营业执照图片
	private long referUserId;	//推荐人账号ID
	private int referUserType;	//推荐人用户类型
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date startBtime;	//开始营业时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date stopBtime;		//停止营业时间
	private String bigLogo;		//大图
	private String smallLogo;	//小图
	private Double memberDiscount;//会员优惠折扣
	private Double platformDiscount;//平台优惠折扣
	private int auditStatus;	//审核状态
	private Date auditTime;		//审核时间
	private long auditUserId;	//审核人ID
	private int starLevelGrade;//星级评分
	private int envGrade;		//环境评分
	private int serviceGrade;	//服务评分
	private double deposit;		//保证金
	private String email;		
	private String withdrawPwd;	//体现密码
	private int isPrincipalPwd;	//体现密码是否使用负责人密码
	private Date createTime;	//开户时间
	private Date lastUpdateTime;//最后更新时间
	private Integer provinceId;
	private Integer cityId;
	private Integer districtId;
	private Integer columnId;
	private Integer soldNumber;
	
	/*--------------满足借口文档追加---------------*/
	private int evalPersonNum;	//评价人数
	private float distance;		//距离
	private int redPacket;		//是否支持红包
	private int fullSent;		//是否支持满就送
	private int coupon;			//是否支持优惠券	
	public long getShopId() {
		return shopId;
	}
	public void setShopId(long shopId) {
		this.shopId = shopId;
	}
	public String getShopImg() {
		return shopImg;
	}
	public void setShopImg(String shopImg) {
		this.shopImg = shopImg;
	}
	public int getShopStatus() {
		return shopStatus;
	}
	public void setShopStatus(int shopStatus) {
		this.shopStatus = shopStatus;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopDesc() {
		return shopDesc;
	}
	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}
	public int getShopType() {
		return shopType;
	}
	public void setShopType(int shopType) {
		this.shopType = shopType;
	}
	public long getPrincipalId() {
		return principalId;
	}
	public void setPrincipalId(long principalId) {
		this.principalId = principalId;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPrincipalName() {
		return principalName;
	}
	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getBusinLicense() {
		return businLicense;
	}
	public void setBusinLicense(String businLicense) {
		this.businLicense = businLicense;
	}
	public String getBusinLicensePic() {
		return businLicensePic;
	}
	public void setBusinLicensePic(String businLicensePic) {
		this.businLicensePic = businLicensePic;
	}
	public long getReferUserId() {
		return referUserId;
	}
	public void setReferUserId(long referUserId) {
		this.referUserId = referUserId;
	}
	public int getReferUserType() {
		return referUserType;
	}
	public void setReferUserType(int referUserType) {
		this.referUserType = referUserType;
	}
	public Date getStartBtime() {
		return startBtime;
	}
	public void setStartBtime(Date startBtime) {
		this.startBtime = startBtime;
	}
	public Date getStopBtime() {
		return stopBtime;
	}
	public void setStopBtime(Date stopBtime) {
		this.stopBtime = stopBtime;
	}
	public String getBigLogo() {
		return bigLogo;
	}
	public void setBigLogo(String bigLogo) {
		this.bigLogo = bigLogo;
	}
	public String getSmallLogo() {
		return smallLogo;
	}
	public void setSmallLogo(String smallLogo) {
		this.smallLogo = smallLogo;
	}
	public Double getMemberDiscount() {
		if(null == this.memberDiscount || 0 >= this.memberDiscount.doubleValue()){
			this.memberDiscount=1D;
		}
		return memberDiscount;
	}
	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}
	public Double getPlatformDiscount() {
		if(null == this.platformDiscount || 0 >= this.platformDiscount.doubleValue()){
			this.platformDiscount=1D;
		}
		return platformDiscount;
	}
	public void setPlatformDiscount(Double platformDiscount) {
		this.platformDiscount = platformDiscount;
	}
	public int getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(int auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	
	public long getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(long auditUserId) {
		this.auditUserId = auditUserId;
	}
	public int getStarLevelGrade() {
		return starLevelGrade;
	}
	public void setStarLevelGrade(int starLevelGrade) {
		this.starLevelGrade = starLevelGrade;
	}
	public int getEnvGrade() {
		return envGrade;
	}
	public void setEnvGrade(int envGrade) {
		this.envGrade = envGrade;
	}
	public int getServiceGrade() {
		return serviceGrade;
	}
	public void setServiceGrade(int serviceGrade) {
		this.serviceGrade = serviceGrade;
	}
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWithdrawPwd() {
		return withdrawPwd;
	}
	public void setWithdrawPwd(String withdrawPwd) {
		this.withdrawPwd = withdrawPwd;
	}
	public int getIsPrincipalPwd() {
		return isPrincipalPwd;
	}
	public void setIsPrincipalPwd(int isPrincipalPwd) {
		this.isPrincipalPwd = isPrincipalPwd;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public int getEvalPersonNum() {
		return evalPersonNum;
	}
	public void setEvalPersonNum(int evalPersonNum) {
		this.evalPersonNum = evalPersonNum;
	}
	public float getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public int getRedPacket() {
		return redPacket;
	}
	public void setRedPacket(int redPacket) {
		this.redPacket = redPacket;
	}
	public int getFullSent() {
		return fullSent;
	}
	public void setFullSent(int fullSent) {
		this.fullSent = fullSent;
	}
	public int getCoupon() {
		return coupon;
	}
	public void setCoupon(int coupon) {
		this.coupon = coupon;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}
	public Integer getColumnId() {
		return columnId;
	}
	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}
	public Integer getSoldNumber() {
		return soldNumber;
	}
	public void setSoldNumber(Integer soldNumber) {
		this.soldNumber = soldNumber;
	}
	
	
}
