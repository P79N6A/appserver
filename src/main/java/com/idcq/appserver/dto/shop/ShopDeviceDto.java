package com.idcq.appserver.dto.shop;

import java.util.Date;
/**
 * 商铺设备表
 * @author Administrator
 *
 */
public class ShopDeviceDto {
	private Integer deviceId;//设备ID
	private Long shopId;//商铺id
	private String deviceName;//设备名称
	private String deviceType;//设备类型:路由器,收银机,打单机
	private String snId;//设备的唯一识别码
	private String deviceDesc;//设备说明
	private Date createTime;//创建时间
	private String deviceModel;//设备的型号
	private String appVersion;//应用版本号
	private Date contactTime;//最后联系时间
	private Integer deviceStatus;//设备状态：0（待激活），1（正常状态）
	private Integer sysUptime;//系统升级次数
	private Integer sysMemfree;//系统空闲内存
	private Double sysLoad;//系统负载量
	private Integer wifiDogUptime;//wifidog升级次
	private Integer checkTime;//检查周期
	private String gwMac;//网关mac地址
	private String wanIp;//wan口IP
	private Integer clientCount;//连入路由用户数
	private String gwAddress;//路由网关地址
	private String ssid24;//2.4G制式的SSID
	private String ssid5;//5G制式的SSID
	private String pwd;//设备密码
	private Date pwdChangeTime;//设备密码修改时间
	private Integer deviceOwnerType;//设备归属类型0-平台 1-店铺
	private Date lastContactTime;//最后更新时间
	private String deviceToken;//设备token
	public ShopDeviceDto(){
		
	}
	public Integer getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getSnId() {
		return snId;
	}
	public void setSnId(String snId) {
		this.snId = snId;
	}
	public String getDeviceDesc() {
		return deviceDesc;
	}
	public void setDeviceDesc(String deviceDesc) {
		this.deviceDesc = deviceDesc;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getDeviceModel() {
		return deviceModel;
	}
	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	public Date getContactTime() {
		return contactTime;
	}
	public void setContactTime(Date contactTime) {
		this.contactTime = contactTime;
	}
	public Integer getDeviceStatus() {
		return deviceStatus;
	}
	public void setDeviceStatus(Integer deviceStatus) {
		this.deviceStatus = deviceStatus;
	}
	public Integer getSysUptime() {
		return sysUptime;
	}
	public void setSysUptime(Integer sysUptime) {
		this.sysUptime = sysUptime;
	}
	public Integer getSysMemfree() {
		return sysMemfree;
	}
	public void setSysMemfree(Integer sysMemfree) {
		this.sysMemfree = sysMemfree;
	}
	public Double getSysLoad() {
		return sysLoad;
	}
	public void setSysLoad(Double sysLoad) {
		this.sysLoad = sysLoad;
	}
	public Integer getWifiDogUptime() {
		return wifiDogUptime;
	}
	public void setWifiDogUptime(Integer wifiDogUptime) {
		this.wifiDogUptime = wifiDogUptime;
	}
	public Integer getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Integer checkTime) {
		this.checkTime = checkTime;
	}
	public String getGwMac() {
		return gwMac;
	}
	public void setGwMac(String gwMac) {
		this.gwMac = gwMac;
	}
	public String getWanIp() {
		return wanIp;
	}
	public void setWanIp(String wanIp) {
		this.wanIp = wanIp;
	}
	public Integer getClientCount() {
		return clientCount;
	}
	public void setClientCount(Integer clientCount) {
		this.clientCount = clientCount;
	}
	public String getGwAddress() {
		return gwAddress;
	}
	public void setGwAddress(String gwAddress) {
		this.gwAddress = gwAddress;
	}
	public String getSsid24() {
		return ssid24;
	}
	public void setSsid24(String ssid24) {
		this.ssid24 = ssid24;
	}
	public String getSsid5() {
		return ssid5;
	}
	public void setSsid5(String ssid5) {
		this.ssid5 = ssid5;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Date getPwdChangeTime() {
		return pwdChangeTime;
	}
	public void setPwdChangeTime(Date pwdChangeTime) {
		this.pwdChangeTime = pwdChangeTime;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getDeviceOwnerType() {
		return deviceOwnerType;
	}
	public void setDeviceOwnerType(Integer deviceOwnerType) {
		this.deviceOwnerType = deviceOwnerType;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Date getLastContactTime() {
		return lastContactTime;
	}
	public void setLastContactTime(Date lastContactTime) {
		this.lastContactTime = lastContactTime;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
