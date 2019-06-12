/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.order.ThirdPayUserRemarkDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年9月28日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.order;

import java.io.Serializable;

public class ThirdPayUserRemarkDto implements Serializable {

	private static final long serialVersionUID = -4076789243858287587L;
	
	private Long userId;
	private Long shopId;
	private String userRemark;
	/**
	 *购买V产品类型
	 0：一点结算
	 1：一点管家
	 2：单屏收银机
	 3：双屏收银机
	 */
	private Integer buyvType;
	
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
	public Integer getBuyvType() {
		return buyvType;
	}
	public void setBuyvType(Integer buyvType) {
		this.buyvType = buyvType;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	
}
