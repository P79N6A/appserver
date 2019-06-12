package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

/**
 * 商铺可用时间段
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午8:44:04
 */
public class ShopTimeIntDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -325318839941002209L;
	private Long intevalId;
    private Long shopId;
    private String intevalName;
    private String startTime;
    private String endTime;
    private Integer showIndex;
    private Date createTime;
    /*------------追加--------------*/
    private Long bookRuleId;
    
	public ShopTimeIntDto() {
		super();
	}

	

	public Long getIntevalId() {
		return intevalId;
	}



	public void setIntevalId(Long intevalId) {
		this.intevalId = intevalId;
	}

	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getIntevalName() {
		return intevalName;
	}

	public void setIntevalName(String intevalName) {
		this.intevalName = intevalName;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getShowIndex() {
		return showIndex;
	}

	public void setShowIndex(Integer showIndex) {
		this.showIndex = showIndex;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getBookRuleId() {
		return bookRuleId;
	}

	public void setBookRuleId(Long bookRuleId) {
		this.bookRuleId = bookRuleId;
	}
    
    
    

}