package com.idcq.appserver.dto.poster;

import java.io.Serializable;

public class Poster implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer posterID;
	private String posterTitle;
	private String posterDate;
	private String posterContent;
	private String bizType;
	public Integer getPosterID() {
		return posterID;
	}
	public void setPosterID(Integer posterID) {
		this.posterID = posterID;
	}
	public String getPosterTitle() {
		return posterTitle;
	}
	public void setPosterTitle(String posterTitle) {
		this.posterTitle = posterTitle;
	}
	public String getPosterDate() {
		return posterDate;
	}
	public void setPosterDate(String posterDate) {
		this.posterDate = posterDate;
	}
	public String getPosterContent() {
		return posterContent;
	}
	public void setPosterContent(String posterContent) {
		this.posterContent = posterContent;
	}
	public String getBizType() {
		return bizType;
	}
	public void setBizType(String bizType) {
		this.bizType = bizType;
	}
	
}
