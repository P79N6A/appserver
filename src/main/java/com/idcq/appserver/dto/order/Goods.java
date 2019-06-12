package com.idcq.appserver.dto.order;

import java.io.Serializable;

public class Goods implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String token;
	private String code;
	private String name;
	private String price;
	
	public String toString(){
		return "name="+name+";code="+code+"; price="+price;
	}
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
}
