package com.idcq.appserver.dto.user;

import java.util.Date;

public class OrderCommentDto {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_id
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Long commentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.user_id
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Long userId;
    /**
     * 商铺id
     */
    private Long shopId;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.order_id
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.star_level_grade
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Double starLevelGrade;
	/**
	 * 环境评分
	 */
	private Double envGrade;
	/**
	 * 服务评分
	 */
	private Double serviceGrade;
	/**
	 * 配送速度
	 */
	private Double logisticsGrade;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_content
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String commentContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_time
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Date commentTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_status
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Byte commentStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_pic1
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String commentPic1;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_pic2
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String commentPic2;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_pic3
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String commentPic3;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_pic4
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String commentPic4;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.comment_pic5
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private String commentPic5;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.delete_admin_id
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Integer deleteAdminId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_user_order_comment.delete_comment_time
     *
     * @mbggenerated Tue Apr 14 15:59:01 CST 2015
     */
    private Date deleteCommentTime;

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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public Double getLogisticsGrade() {
		return logisticsGrade;
	}

	public void setLogisticsGrade(Double logisticsGrade) {
		this.logisticsGrade = logisticsGrade;
	}

	public String getCommentPic1() {
		return commentPic1;
	}

	public void setCommentPic1(String commentPic1) {
		this.commentPic1 = commentPic1;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
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
    
}
