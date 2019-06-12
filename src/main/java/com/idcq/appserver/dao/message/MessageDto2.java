package com.idcq.appserver.dao.message;

import java.util.Date;

public class MessageDto2 {
    private Long id;

    private Integer msgType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.msg_title
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private String msgTitle;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.msg_img
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private String msgImg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.msg_desc
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private String msgDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.shop_id
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Long shopId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.pub_time
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Date pubTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.target_user_type
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private String targetUserType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.province_id
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Integer provinceId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.city_id
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Integer cityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.district_id
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Integer districtId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.town_id
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Integer townId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.pub_user_id
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Integer pubUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_message.message_status
     *
     * @mbggenerated Mon Apr 27 09:25:46 CST 2015
     */
    private Integer messageStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public String getMsgTitle() {
		return msgTitle;
	}

	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}

	public String getMsgImg() {
		return msgImg;
	}

	public void setMsgImg(String msgImg) {
		this.msgImg = msgImg;
	}

	public String getMsgDesc() {
		return msgDesc;
	}

	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public String getTargetUserType() {
		return targetUserType;
	}

	public void setTargetUserType(String targetUserType) {
		this.targetUserType = targetUserType;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}

	public Integer getPubUserId() {
		return pubUserId;
	}

	public void setPubUserId(Integer pubUserId) {
		this.pubUserId = pubUserId;
	}

	public Integer getMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}

}
