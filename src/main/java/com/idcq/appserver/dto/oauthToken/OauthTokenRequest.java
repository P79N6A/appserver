package com.idcq.appserver.dto.oauthToken;

import java.io.Serializable;

import com.idcq.appserver.common.annotation.Check;

public class OauthTokenRequest implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5374482886309524121L;
	
	@Check
	private Integer oauthChannel;
	@Check
	private String oauthCode;
	public Integer getOauthChannel() {
		return oauthChannel;
	}
	public void setOauthChannel(Integer oauthChannel) {
		this.oauthChannel = oauthChannel;
	}
	public String getOauthCode() {
		return oauthCode;
	}
	public void setOauthCode(String oauthCode) {
		this.oauthCode = oauthCode;
	}

}
