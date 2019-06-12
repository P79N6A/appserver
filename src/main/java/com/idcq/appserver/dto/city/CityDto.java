package com.idcq.appserver.dto.city;

import java.io.Serializable;

/**
 * 城市区域dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 上午11:51:53
 */
public class CityDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8728125290988172371L;
	private long id;
	private long pCode;	//父级区域编码
	
	public CityDto() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getpCode() {
		return pCode;
	}

	public void setpCode(long pCode) {
		this.pCode = pCode;
	}

	
}
