package com.idcq.appserver.dto.user;

import java.util.Date;

public class UserSearchHistory {
	/**
	 * 历史记录id
	 */
	private Long historyId;
	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 *  搜索内容
	 */
	private String searchContent;
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 搜索次数
	 */
	private Integer searchCount;
	
	/**
	 * 搜索满足的结果数目
	 */
	private Integer resultNum;
	
	/**
	 * 是否加入到suggestion索引
	 */
	private Integer flag;
	public Long getHistoryId() {
		return historyId;
	}
	public void setHistoryId(Long historyId) {
		this.historyId = historyId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getSearchContent() {
		return searchContent;
	}
	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getSearchCount() {
		return searchCount;
	}
	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}
	public Integer getResultNum() {
		return resultNum;
	}
	public void setResultNum(Integer resultNum) {
		this.resultNum = resultNum;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	
	

}
