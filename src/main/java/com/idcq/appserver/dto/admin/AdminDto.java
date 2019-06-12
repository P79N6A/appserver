package com.idcq.appserver.dto.admin;

import java.io.Serializable;

public class AdminDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7941802395588573433L;

	private Integer id;
	private Integer userState;
	private Integer roleState;
	private Integer roleId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserState() {
		return userState;
	}
	public void setUserState(Integer userState) {
		this.userState = userState;
	}
	public Integer getRoleState() {
		return roleState;
	}
	public void setRoleState(Integer roleState) {
		this.roleState = roleState;
	}
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	
}
