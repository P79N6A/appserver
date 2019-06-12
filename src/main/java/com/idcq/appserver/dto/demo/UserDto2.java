package com.idcq.appserver.dto.demo;

import java.io.Serializable;

public class UserDto2 implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5835799659706030421L;
	private Long id;
	private Long uId;
	private String name;
	private String password;
	
	public UserDto2() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getuId() {
		return uId;
	}
	public void setuId(long uId) {
		this.uId = uId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	
}
