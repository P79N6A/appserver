package com.idcq.appserver.dto.activity;


public class BusinessAreaUserKeyDto {

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_business_area_user.business_area_activity_id
     *
     * @mbggenerated Sat Mar 12 13:31:30 CST 2016
     */
    private Long businessAreaActivityId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column 1dcq_business_area_user.member_id
     *
     * @mbggenerated Sat Mar 12 13:31:30 CST 2016
     */
    private Long memberId;

	public Long getBusinessAreaActivityId() {
		return businessAreaActivityId;
	}

	public void setBusinessAreaActivityId(Long businessAreaActivityId) {
		this.businessAreaActivityId = businessAreaActivityId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

   
    
}