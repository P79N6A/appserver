package com.idcq.appserver.dto.common;

import java.io.Serializable;

import com.idcq.appserver.common.annotation.Check;

public class UserPermission implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2609352949678154662L;
	
	@Check
	private Long userId;
	private Integer roleId;
	private Long shopId;
	private Integer permissionId;
	private String module;
	private String controller;
	private String action;
	private String shopUrl;
	private Integer permissionType;
	
	@Check
	private Integer userTypeId;
	public Integer getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getPermissionId() {
		return permissionId;
	}
	public void setPermissionId(Integer permissionId) {
		this.permissionId = permissionId;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getController() {
		return controller;
	}
	public void setController(String controller) {
		this.controller = controller;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getShopUrl() {
		return shopUrl;
	}
	public void setShopUrl(String shopUrl) {
		this.shopUrl = shopUrl;
	}
	public Integer getPermissionType() {
		return permissionType;
	}
	public void setPermissionType(Integer permissionType) {
		this.permissionType = permissionType;
	}

}
