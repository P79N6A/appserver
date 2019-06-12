package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 店铺应用版本实体类
 * @author shengzhipeng
 * @date:2015年9月16日 下午7:50:39
 */
public class ShopAppVersionDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 716836741506099853L;
	
	private Long versionLogId;
	//店铺id
	private Long shopId;
	
	//mac地址
	private String snId;
	
	//应用名称
	private String appName;
	
	//应用版本
	private String appVersion;
	
	//应用描述
	private String appDesc;
	
	//创建时间
	private Date createTime;
	
	//最后更新时间
	private Date lastUpdateTime;

	public Long getVersionLogId() {
		return versionLogId;
	}

	public void setVersionLogId(Long versionLogId) {
		this.versionLogId = versionLogId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getSnId() {
		return snId;
	}

	public void setSnId(String snId) {
		this.snId = snId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

}
