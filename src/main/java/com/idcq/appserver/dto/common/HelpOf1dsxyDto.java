/**
 * 
 */
package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.Date;

/** 
 * 一点商学院帮助管理
 * @ClassName: Attachment 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年4月21日 上午11:23:30 
 *  
 */
public class HelpOf1dsxyDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 343852524600399933L;
	
	private Integer helpId;
	/**
	 * 帮助关键词，用于搜素，多个之间用英文逗号隔开
	 */
	private String helpKey;
	
	/**
	 * 帮助标题
	 */
	private String helpTitle;
	
	/**
	 * 帮助内容
	 */
	private String helpContent;
	
	/**
	 * 栏目Id
	 */
	private Integer columnId;
	
	/**
	 * 栏目名
	 */
	private String columnName;
	
	/**
	 * 点击(查看)次数
	 */
	private Integer clickNum;
	
	/**
	 * 帮助创建时间
	 */
	private String createTime;
	
	/**
	 * 栏目编号，唯一
	 */
	private String columnNo;

	public Integer getHelpId() {
		return helpId;
	}

	public void setHelpId(Integer helpId) {
		this.helpId = helpId;
	}

	public String getHelpKey() {
		return helpKey;
	}

	public void setHelpKey(String helpKey) {
		this.helpKey = helpKey;
	}

	public String getHelpTitle() {
		return helpTitle;
	}

	public void setHelpTitle(String helpTitle) {
		this.helpTitle = helpTitle;
	}

	public String getHelpContent() {
		return helpContent;
	}

	public void setHelpContent(String helpContent) {
		this.helpContent = helpContent;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getClickNum() {
		return clickNum;
	}

	public void setClickNum(Integer clickNum) {
		this.clickNum = clickNum;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getColumnNo() {
		return columnNo;
	}

	public void setColumnNo(String columnNo) {
		this.columnNo = columnNo;
	}
	
	
}
