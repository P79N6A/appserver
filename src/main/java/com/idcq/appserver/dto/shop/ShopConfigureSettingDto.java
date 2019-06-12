package com.idcq.appserver.dto.shop;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
/**
 * 商铺设置Dto
 * @author shengzhipeng
 * @date:2015年9月21日 上午10:32:35
 */
public class ShopConfigureSettingDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1700890323783182142L;
	
	//主键
	@JsonIgnore
	private Long settingId;
	
	//店铺id
	@JsonIgnore
	private Long shopId;
	
	//设置项
	private String settingKey;
	
	//设置值
	private String settingValue;
	
	//设置说明
	@JsonIgnore
	private String settingDesc;
	
	//设置类型
	@JsonIgnore
	private Integer settingType;
	
	//创建时间
	@JsonIgnore
	private String createTime;
	
	//最后更新时间
	@JsonIgnore
	private String lastUpdateTime;

	public Long getSettingId() {
		return settingId;
	}

	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getSettingKey() {
		return settingKey;
	}

	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String getSettingDesc() {
		return settingDesc;
	}

	public void setSettingDesc(String settingDesc) {
		this.settingDesc = settingDesc;
	}
	
	public Integer getSettingType() {
		return settingType;
	}

	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(String lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	

}
