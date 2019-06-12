package com.idcq.appserver.dto.goods;

import java.io.Serializable;

public class ShopTimeIntevalDto implements Serializable {

	private static final long serialVersionUID = -1794207938618529745L;
	
	private Long intevalId;
	private int showIndex;
	private String intevalName;
	private String startTime;
	private String endTime;
	public ShopTimeIntevalDto() {
		super();
	}
	public Long getIntevalId() {
		return intevalId;
	}
	public void setIntevalId(Long intevalId) {
		this.intevalId = intevalId;
	}
	public int getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
	}
	public String getIntevalName() {
		return intevalName;
	}
	public void setIntevalName(String intevalName) {
		this.intevalName = intevalName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
}
