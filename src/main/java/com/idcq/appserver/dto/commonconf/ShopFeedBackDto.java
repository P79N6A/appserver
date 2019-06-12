/**
 * 
 */
package com.idcq.appserver.dto.commonconf;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.utils.CustomDateSerializer;

/** 
 * @ClassName: ShopFeedBackDto 
 * @Description: TODO(商铺反馈信息) 
 * @author 张鹏程 
 * @date 2015年4月16日 下午2:55:56 
 *  
 */
public class ShopFeedBackDto {
	
	/**
	 * 反馈id
	 */
	private Long feedbackId;
	
	/**
	 * 商铺编号
	 */
	private Long shopId;
	
	/**
	 * 反馈
	 */
	private String feedback;
	
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date createTime;
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date handleTime;
	
	private Integer handleStatus;
	
	private Integer handleAdminId;
	
	private String handleSuggestion;
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date feedbackTime;
	
	private Integer feedbackType;
	
	private Integer clientSystemType;
	
	private List<Map> attachementUrls;
	
	public Date getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

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

	public Integer getHandleStatus() {
		return handleStatus;
	}

	public void setHandleStatus(Integer handleStatus) {
		this.handleStatus = handleStatus;
	}

	public Integer getHandleAdminId() {
		return handleAdminId;
	}

	public void setHandleAdminId(Integer handleAdminId) {
		this.handleAdminId = handleAdminId;
	}

	public String getHandleSuggestion() {
		return handleSuggestion;
	}

	public void setHandleSuggestion(String handleSuggestion) {
		this.handleSuggestion = handleSuggestion;
	}

    public Integer getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(Integer feedbackType) {
        this.feedbackType = feedbackType;
    }

    public Integer getClientSystemType() {
        return clientSystemType;
    }

    public void setClientSystemType(Integer clientSystemType) {
        this.clientSystemType = clientSystemType;
    }

    public List<Map> getAttachementUrls() {
        return attachementUrls;
    }

    public void setAttachementUrls(List<Map> attachementUrls) {
        this.attachementUrls = attachementUrls;
    }
}
