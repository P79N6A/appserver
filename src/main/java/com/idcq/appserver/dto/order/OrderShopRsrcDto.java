package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 商铺资源预定dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月25日
 * @time 下午4:10:29
 */
public class OrderShopRsrcDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5823908411620528746L;
	private Long orderResourceId;
    private String orderId;
    private Long shopId;
    private Long groupId;
    private Long intevalId;
    private Integer resourceNumber;
    private Date reserveResourceDate;
    private Date createTime;
    private Integer status;
    /*--------------------*/
    private Long userId;
    private String resourceType;	//资源类型
    /**
     * 用户名
     */
    private String userName;
    /**
     * 手机号码
     */
    private String mobile;
    
    List<OsrsDto> osrs;

    /*-----------------*/
    private Long bookRuleId;
    /*-----------------*/
    private String startTime;
    private String endTime;
    
    /**
     * 预定人数
     */
    @JsonIgnore
    private Integer pNum;
    
    /**
     * 最大人数
     */
    private Integer maxPeople;
    
    /**
     * 最小人数
     */
    private Integer minPeople;
    /*-------20150729追加----------*/
    private Long bizId;
    /*-------20150804追加----------*/
    private Integer serverMode;
    /*-------20150805追加----------*/
    private String bizName;
    
    
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public OrderShopRsrcDto() {
		super();
	}

	public Long getOrderResourceId() {
		return orderResourceId;
	}

	public void setOrderResourceId(Long orderResourceId) {
		this.orderResourceId = orderResourceId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getIntevalId() {
		return intevalId;
	}

	public void setIntevalId(Long intevalId) {
		this.intevalId = intevalId;
	}

	public List<OsrsDto> getOsrs() {
		return osrs;
	}

	public void setOsrs(List<OsrsDto> osrs) {
		this.osrs = osrs;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getBookRuleId() {
		return bookRuleId;
	}

	public void setBookRuleId(Long bookRuleId) {
		this.bookRuleId = bookRuleId;
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

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public Integer getpNum() {
		return pNum;
	}

	public void setpNum(Integer pNum) {
		this.pNum = pNum;
	}

	public Integer getMaxPeople() {
		return maxPeople;
	}

	public void setMaxPeople(Integer maxPeople) {
		this.maxPeople = maxPeople;
	}

	public Integer getMinPeople() {
		return minPeople;
	}

	public void setMinPeople(Integer minPeople) {
		this.minPeople = minPeople;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public Integer getServerMode() {
		return serverMode;
	}

	public void setServerMode(Integer serverMode) {
		this.serverMode = serverMode;
	}

	public String getBizName() {
		return bizName;
	}

	public void setBizName(String bizName) {
		this.bizName = bizName;
	}
	

}

   