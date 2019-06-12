package com.idcq.appserver.dto.help;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class HelpDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8160329271004645565L;

	private Long infoId; //帮助信息ID
	
	@JsonIgnore
	private String audienceType; //投放的受众分类：APP端，店铺端
	private Long categoryId;   // 分类ID
	private String categoryName;  //分类名称
	private String infoTitle;  //帮助的标题
	private String infoContent;  //帮助的内容
	private Date createtime;
	/*20160705添加*/
	@JsonIgnore
	private int mode;//添加获取对象（mode:0-app段；1-商铺端，默认为0
	public int getMode() {
		return mode;
	}
	public void setMode(int mode) {
		this.mode = mode;
	}
	
	public Long getInfoId() {
		return infoId;
	}
	public void setInfoId(Long infoId) {
		this.infoId = infoId;
	}
	public String getAudienceType() {
		return audienceType;
	}
	public void setAudienceType(String audienceType) {
		this.audienceType = audienceType;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getInfoTitle() {
		return infoTitle;
	}
	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}
	public String getInfoContent() {
		return infoContent;
	}
	public void setInfoContent(String infoContent) {
		this.infoContent = infoContent;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
}
