package com.idcq.appserver.dto.user;

import java.util.Date;

public class GoodsCommentDto {
	/**
	 * 主键
	 */
    private Long commentId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 商品id
     */
    private Long goodsId;
    /**
     * 星级评分
     */
    private Double starLevelGrade;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 评论时间
     */
    private Date commentTime;
    /**
     * 评论状态
     */
    private Byte commentStatus;
    /**
     * 评论图片1
     */
    private String commentPic1;
    /**
     * 评论图片2
     */
    private String commentPic2;
    /**
     * 评论图片3
     */
    private String commentPic3;
    /**
     * 评论图片4
     */
    private String commentPic4;
    /**
     * 评论图片5
     */
    private String commentPic5;
    /**
     * 删除评论的管理员ID
     */
    private Integer deleteAdminId;
    /**
     * 删除评论时间
     */
    private Date deleteCommentTime;
    /**
     * 环境评分
     */
    private Double envGrade;
    /**
     * 服务评分
     */
    private Double serviceGrade;
    /**
     * 预留评分1
     */
    private Double grade1;
    /**
     * 预留评分2
     */
    private Double grade2;
    /**
     * 预留评分3
     */
    private Double grade3;
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
	public Long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}
	public Double getStarLevelGrade() {
		return starLevelGrade;
	}
	public void setStarLevelGrade(Double starLevelGrade) {
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
	public Byte getCommentStatus() {
		return commentStatus;
	}
	public void setCommentStatus(Byte commentStatus) {
		this.commentStatus = commentStatus;
	}
	public String getCommentPic1() {
		return commentPic1;
	}
	public void setCommentPic1(String commentPic1) {
		this.commentPic1 = commentPic1;
	}
	public String getCommentPic2() {
		return commentPic2;
	}
	public void setCommentPic2(String commentPic2) {
		this.commentPic2 = commentPic2;
	}
	public String getCommentPic3() {
		return commentPic3;
	}
	public void setCommentPic3(String commentPic3) {
		this.commentPic3 = commentPic3;
	}
	public String getCommentPic4() {
		return commentPic4;
	}
	public void setCommentPic4(String commentPic4) {
		this.commentPic4 = commentPic4;
	}
	public String getCommentPic5() {
		return commentPic5;
	}
	public void setCommentPic5(String commentPic5) {
		this.commentPic5 = commentPic5;
	}
	public Integer getDeleteAdminId() {
		return deleteAdminId;
	}
	public void setDeleteAdminId(Integer deleteAdminId) {
		this.deleteAdminId = deleteAdminId;
	}
	public Date getDeleteCommentTime() {
		return deleteCommentTime;
	}
	public void setDeleteCommentTime(Date deleteCommentTime) {
		this.deleteCommentTime = deleteCommentTime;
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
	public Double getGrade1() {
		return grade1;
	}
	public void setGrade1(Double grade1) {
		this.grade1 = grade1;
	}
	public Double getGrade2() {
		return grade2;
	}
	public void setGrade2(Double grade2) {
		this.grade2 = grade2;
	}
	public Double getGrade3() {
		return grade3;
	}
	public void setGrade3(Double grade3) {
		this.grade3 = grade3;
	}

}
