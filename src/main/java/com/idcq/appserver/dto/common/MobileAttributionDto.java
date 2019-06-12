/**
 * Copyright (C) 2015 Asiainfo-Linkage
 * 手机归属地
 * @className:com.idcq.appserver.utils.MobileAttributionDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2015年11月6日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.common;

public class MobileAttributionDto {
	/**
	 * 电话
	 */
	private String phone;
	/**
	 * 前七位
	 */
	private String prefix;
	/**
	 * 运营商
	 */
	private String supplier;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 备注
	 */
	private String suit;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		this.suit = suit;
	}

}
