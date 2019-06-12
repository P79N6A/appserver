package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信验证码
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午8:51:30
 */
public class SmsVeriCodeDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3679253321593173590L;
	private int code;	//验证码
	private Date generateDate;	//验证码
	private int validTime;	//有效时长
	
	public SmsVeriCodeDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Date getGenerateDate() {
		return generateDate;
	}
	public void setGenerateDate(Date generateDate) {
		this.generateDate = generateDate;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getValidTime() {
		return validTime;
	}

	public void setValidTime(int validTime) {
		this.validTime = validTime;
	}
	
}
