package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.List;
/**
 * 商铺设置接口json转换dto
 * @author shengzhipeng
 * @date:2015年9月21日 下午2:51:50
 */
public class ShopSettingsDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2369743180632622059L;
	
	private Long shopId;
	
	private String token;
	
	private Integer settingType;
	
	private List<ShopConfigureSettingDto> lst;

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getSettingType() {
		return settingType;
	}

	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}

	public List<ShopConfigureSettingDto> getLst() {
		return lst;
	}

	public void setLst(List<ShopConfigureSettingDto> lst) {
		this.lst = lst;
	}

}
