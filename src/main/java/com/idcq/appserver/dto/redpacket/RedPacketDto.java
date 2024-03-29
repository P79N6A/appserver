package com.idcq.appserver.dto.redpacket;

import java.io.Serializable;
import java.util.Date;

public class RedPacketDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1667900621487284356L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.red_packet_id
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Long redPacketId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.user_id
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.amount
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Double amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.price
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Double price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.give_user_id
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Long giveUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.create_time
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.source_order_id
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private String sourceOrderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.status
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Integer status;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.obtain_desc
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private String obtainDesc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.begin_date
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Date beginDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.end_date
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Date endDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.shop_id
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Integer shopId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.business_area_activity_id
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Integer businessAreaActivityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_red_packet.client_system_type
     *
     * @mbggenerated Tue Mar 15 11:26:25 CST 2016
     */
    private Integer clientSystemType;

	public RedPacketDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getRedPacketId() {
		return redPacketId;
	}

	public void setRedPacketId(Long redPacketId) {
		this.redPacketId = redPacketId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Long getGiveUserId() {
		return giveUserId;
	}

	public void setGiveUserId(Long giveUserId) {
		this.giveUserId = giveUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSourceOrderId() {
		return sourceOrderId;
	}

	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getObtainDesc() {
		return obtainDesc;
	}

	public void setObtainDesc(String obtainDesc) {
		this.obtainDesc = obtainDesc;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getBusinessAreaActivityId() {
		return businessAreaActivityId;
	}

	public void setBusinessAreaActivityId(Integer businessAreaActivityId) {
		this.businessAreaActivityId = businessAreaActivityId;
	}

	public Integer getClientSystemType() {
		return clientSystemType;
	}

	public void setClientSystemType(Integer clientSystemType) {
		this.clientSystemType = clientSystemType;
	}
    
    

}