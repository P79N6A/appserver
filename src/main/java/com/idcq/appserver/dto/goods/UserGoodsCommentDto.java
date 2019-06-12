package com.idcq.appserver.dto.goods;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

public class UserGoodsCommentDto {
    @JsonIgnore
	private Long commentId;
    private Long userId;
    private Integer starLevelGrade;
    private String commentContent;
    @JsonSerialize(using = CustomDateSerializer.class)  
    private Date commentTime;
    
    private String userName;
    private String imgBig;//会员头像（大图片
    private String imgSmall;//会员头像（小图片
    
    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStarLevelGrade() {
        return starLevelGrade;
    }

    public void setStarLevelGrade(Integer starLevelGrade) {
        this.starLevelGrade = starLevelGrade;
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
    
}