package com.idcq.appserver.dto.cashcoupon;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateSerializer;
import com.idcq.appserver.utils.MyDateTimeSerializer;

public class UserCashCouponDto implements Serializable {

	private static final long serialVersionUID = -548999357317796750L;
    private Long uccId;
	@JsonIgnore
    private Long userId;
    private Long cashCouponId;
    private Double price;
    @JsonIgnore
    private Integer couponStatus;
    @JsonIgnore
    private Double usedPrice;
    @JsonSerialize(using=MyDateTimeSerializer.class)
    private Date obtainTime;
    /*---------追加代金券表相关字段-----------*/
    private String cashCouponName;
    private String cashCouponDesc;			//代金券使用说明
    private Long shopId;
    private String shopName;
    private Long heatDegree;				//热度
    private Long totalNumber;			//总数
    private Long availableNumber;		//可用数量
    private Integer value;
    private Integer conditionPrice;			//订单金额满多少元才可使用
    @JsonSerialize(using=MyDateSerializer.class)
    private Date issueFromDate;				//领取起始时间
    @JsonSerialize(using=MyDateSerializer.class)
    private Date issueToDate;				//领取结束时间
    private Long obtainNumberPerDayPerPerson;//每人每天最多可领取数量
    private Long useNumberPerOrder;		//每单1次最多可使用的张数
    private Integer useTogetherFlag;		//是否能和同类券一起使用：0（不能），1（可以）
    private String cashCouponImg;
    @JsonSerialize(using = MyDateSerializer.class) 
    private Date startTime;					//有效期开始时间
    @JsonSerialize(using = MyDateSerializer.class) 
    private Date stopTime;
    
    /*---------20150714追加----------*/
    @JsonSerialize(using = MyDateTimeSerializer.class) 
    private Date endTime;				//代金券过期时间
    private Double cashCouponAmount;	//代金券余额
    private Double uccPrice;			//面额

	public UserCashCouponDto() {
		super();
	}

	public Long getUccId() {
		return uccId;
	}

	public void setUccId(Long uccId) {
		this.uccId = uccId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCashCouponId() {
		return cashCouponId;
	}

	public void setCashCouponId(Long cashCouponId) {
		this.cashCouponId = cashCouponId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Double getUsedPrice() {
		return usedPrice;
	}

	public void setUsedPrice(Double usedPrice) {
		this.usedPrice = usedPrice;
	}

	public Date getObtainTime() {
		return obtainTime;
	}

	public void setObtainTime(Date obtainTime) {
		this.obtainTime = obtainTime;
	}

	public String getCashCouponName() {
		return cashCouponName;
	}

	public void setCashCouponName(String cashCouponName) {
		this.cashCouponName = cashCouponName;
	}

	public String getCashCouponDesc() {
		return cashCouponDesc;
	}

	public void setCashCouponDesc(String cashCouponDesc) {
		this.cashCouponDesc = cashCouponDesc;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getHeatDegree() {
		return heatDegree;
	}

	public void setHeatDegree(Long heatDegree) {
		this.heatDegree = heatDegree;
	}

	public Long getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Long totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Long getAvailableNumber() {
		return availableNumber;
	}

	public void setAvailableNumber(Long availableNumber) {
		this.availableNumber = availableNumber;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getConditionPrice() {
		return conditionPrice;
	}

	public void setConditionPrice(Integer conditionPrice) {
		this.conditionPrice = conditionPrice;
	}

	public Date getIssueFromDate() {
		return issueFromDate;
	}

	public void setIssueFromDate(Date issueFromDate) {
		this.issueFromDate = issueFromDate;
	}

	public Date getIssueToDate() {
		return issueToDate;
	}

	public void setIssueToDate(Date issueToDate) {
		this.issueToDate = issueToDate;
	}

	public Long getObtainNumberPerDayPerPerson() {
		return obtainNumberPerDayPerPerson;
	}

	public void setObtainNumberPerDayPerPerson(Long obtainNumberPerDayPerPerson) {
		this.obtainNumberPerDayPerPerson = obtainNumberPerDayPerPerson;
	}

	public Long getUseNumberPerOrder() {
		return useNumberPerOrder;
	}

	public void setUseNumberPerOrder(Long useNumberPerOrder) {
		this.useNumberPerOrder = useNumberPerOrder;
	}

	public Integer getUseTogetherFlag() {
		return useTogetherFlag;
	}

	public void setUseTogetherFlag(Integer useTogetherFlag) {
		this.useTogetherFlag = useTogetherFlag;
	}

	public String getCashCouponImg() {
		return cashCouponImg;
	}

	public void setCashCouponImg(String cashCouponImg) {
		this.cashCouponImg = cashCouponImg;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopTime() {
		return stopTime;
	}

	public void setStopTime(Date stopTime) {
		this.stopTime = stopTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getCashCouponAmount() {
		return cashCouponAmount;
	}

	public void setCashCouponAmount(Double cashCouponAmount) {
		this.cashCouponAmount = cashCouponAmount;
	}

	public Double getUccPrice() {
		return uccPrice;
	}

	public void setUccPrice(Double uccPrice) {
		this.uccPrice = uccPrice;
	}
	
	
}