package com.idcq.appserver.dto.order;

import java.util.Date;
/**
 * 商铺意见反馈实体类
 * @author 陈永鑫
 */
public class ShopFeedBack {
	/**
	 * id
	 */
	private Long feedbackId;
	/**
	 * 商铺id
	 */
	private Long shopId;
	/**
	 * 处理反馈的管理员ID
	 */
	private Long handleAdminId;
	/**
	 * 商铺反馈
	 */
	private String feedback;
	/**
	 * 反馈时间
	 */
	private Date createTime;
	/**
	 * 处理时间
	 */
	private Date handleTime;
	/**
	 * 反馈状态
	 */
	private Integer handStatus;
	/**
	 * 处理意见
	 */
	private String handleSuggestion;
	public Long getFeedbackId() {
		return feedbackId;
	}
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getHandleAdminId() {
		return handleAdminId;
	}
	public void setHandleAdminId(Long handleAdminId) {
		this.handleAdminId = handleAdminId;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}
	public Integer getHandStatus() {
		return handStatus;
	}
	public void setHandStatus(Integer handStatus) {
		this.handStatus = handStatus;
	}
	public String getHandleSuggestion() {
		return handleSuggestion;
	}
	public void setHandleSuggestion(String handleSuggestion) {
		this.handleSuggestion = handleSuggestion;
	}
	
}
