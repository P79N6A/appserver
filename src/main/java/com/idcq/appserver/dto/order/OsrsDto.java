package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 商铺资源预定dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午4:10:29
 */
public class OsrsDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -988434332338350748L;
	private Long groupId;
    private Long intevalId;
    private Integer resourceNumber;
    private Date reserveResourceDate;
    @JsonIgnore
    private Integer minPeople;
    @JsonIgnore
    private Integer maxPeople;
    
	public OsrsDto() {
		super();
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getIntevalId() {
		return intevalId;
	}

	public void setIntevalId(Long intevalId) {
		this.intevalId = intevalId;
	}

	public Integer getResourceNumber() {
		return resourceNumber;
	}

	public void setResourceNumber(Integer resourceNumber) {
		this.resourceNumber = resourceNumber;
	}

	public Date getReserveResourceDate() {
		return reserveResourceDate;
	}

	public void setReserveResourceDate(Date reserveResourceDate) {
		this.reserveResourceDate = reserveResourceDate;
	}

	public Integer getMinPeople() {
		return minPeople;
	}

	public void setMinPeople(Integer minPeople) {
		this.minPeople = minPeople;
	}

	public Integer getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(Integer maxPeople) {
		this.maxPeople = maxPeople;
	}

}

   