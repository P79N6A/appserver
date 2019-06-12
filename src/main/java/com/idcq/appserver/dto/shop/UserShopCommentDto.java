package com.idcq.appserver.dto.shop;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

public class UserShopCommentDto {
    @JsonIgnore
	private Long commentId;
    private Long userId;
    @JsonIgnore
    private Double starLevelGrade;
    private String commentContent;
    @JsonSerialize(using = CustomDateSerializer.class) 
    private Date commentTime;
    private String userName;
    private String imgBig;//会员头像（大图片
    private String imgSmall;//会员头像（小图片
    private Double envGrade;//环境评分
    private Double serviceGrade;//服务评分
    private Double logisticsGrade;//配送速度评分评分
    private Integer type;//店铺类型1：便利店，2位非便利店

    public Long getCommentId() {
		return commentId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getLogisticsGrade() {
		return logisticsGrade;
	}

	public void setLogisticsGrade(Double logisticsGrade) {
		this.logisticsGrade = logisticsGrade;
	}

	public Double getStarLevelGrade() {
		return starLevelGrade;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public void setStarLevelGrade(Double starLevelGrade) {
		this.starLevelGrade = starLevelGrade;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getImgBig() {
		return imgBig;
	}

	public void setImgBig(String imgBig) {
		this.imgBig = imgBig;
	}

	public String getImgSmall() {
		return imgSmall;
	}

	public void setImgSmall(String imgSmall) {
		this.imgSmall = imgSmall;
	}

	public Double getEnvGrade() {
		return envGrade;
	}

	public void setEnvGrade(Double envGrade) {
		this.envGrade = envGrade;
	}

	public Double getServiceGrade() {
		return serviceGrade;
	}

	public void setServiceGrade(Double serviceGrade) {
		this.serviceGrade = serviceGrade;
	}
	
}