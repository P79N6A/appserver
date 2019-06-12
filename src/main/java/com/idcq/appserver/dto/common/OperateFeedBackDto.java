/**
 * 
 */
package com.idcq.appserver.dto.common;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 操作回馈dto
 * @author Administrator
 * 
 * @date 2016年3月15日
 * @time 下午5:19:16
 */
public class OperateFeedBackDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6126891171820623031L;
	private Long bizId;
	private Integer bizType;
	private Integer feedbackType;
	private Long userId;
	private Long shopId;
	private Integer clientSystem;
	private Date clientTime;
	private Integer notifyType;
	private Integer receiverId;
	public OperateFeedBackDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getBizId() {
		return bizId;
	}
	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}
	public Integer getBizType() {
		return bizType;
	}
	public void setBizType(Integer bizType) {
		this.bizType = bizType;
	}
	public Integer getFeedbackType() {
		return feedbackType;
	}
	public void setFeedbackType(Integer feedbackType) {
		this.feedbackType = feedbackType;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getClientSystem() {
		return clientSystem;
	}
	public void setClientSystem(Integer clientSystem) {
		this.clientSystem = clientSystem;
	}
	public Date getClientTime() {
		return clientTime;
	}
	public void setClientTime(Date clientTime) {
		this.clientTime = clientTime;
	}
	public Integer getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(Integer notifyType) {
		this.notifyType = notifyType;
	}
	public Integer getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(Integer receiverId) {
		this.receiverId = receiverId;
	}
	public Map getParamMap(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("bizId", bizId);
		map.put("bizType", bizType);
		map.put("feedbackType", feedbackType);
		map.put("userId", userId);
		map.put("shopId", shopId);
		map.put("clientSystem", clientSystem);
		map.put("clientTime", clientTime);
		map.put("notifyType", notifyType);
		map.put("receiverId", receiverId);
		return map;
	}
	
	
	
}