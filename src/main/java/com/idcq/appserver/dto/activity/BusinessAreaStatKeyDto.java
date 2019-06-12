package com.idcq.appserver.dto.activity;

import java.io.Serializable;

public class BusinessAreaStatKeyDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6608223167508692645L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_business_area_stat.business_area_activity_id
     *
     * @mbggenerated Sat Mar 12 12:07:08 CST 2016
     */
    private Long businessAreaActivityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_business_area_stat.shop_id
     *
     * @mbggenerated Sat Mar 12 12:07:08 CST 2016
     */
    private Long shopId;

	public BusinessAreaStatKeyDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getBusinessAreaActivityId() {
		return businessAreaActivityId;
	}

	public void setBusinessAreaActivityId(Long businessAreaActivityId) {
		this.businessAreaActivityId = businessAreaActivityId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

    
    
}