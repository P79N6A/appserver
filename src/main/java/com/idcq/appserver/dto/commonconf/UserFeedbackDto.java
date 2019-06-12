package com.idcq.appserver.dto.commonconf;

import java.io.Serializable;
import java.util.Date;

public class UserFeedbackDto implements Serializable {
	
	private static final long serialVersionUID = -8183876504524182286L;
	
	private Long userId;
	private String feedback;
	private Date createTime;
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
}
