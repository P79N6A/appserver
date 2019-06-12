package com.idcq.appserver.dto.coupon;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateSerializer;
import com.idcq.appserver.utils.MyDateTimeSerializer;

/**
 * 用户持有的优惠券dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月30日
 * @time 下午2:10:36
 */
public class UserCouponDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5539579845973910263L;
	@JsonIgnore
	private Long ucId;
    private Long userId;
    private Long couponId;
    private Integer price;
    private Integer couponStatus;
    @JsonSerialize(using=MyDateTimeSerializer.class)
    private Date obtainTime;
    @JsonIgnore
    private String orderId;
    
    /*---------追加优惠券字段-----------*/
    private String couponName;						//优惠券名称
    private String couponDesc;						//优惠券的使用须知
    private Long shopId;		
    private Long goodsId;						//优惠券适用的商品（套餐）id
    private Integer totalNumber;					//发行总数
    private Integer availableNumber;
    private Integer usedNumber;						//已经使用数量
    private Integer heatDegree;						//热度，数字越大，表示越热
    private Integer value;							//面值
    @JsonSerialize(using=MyDateSerializer.class)
    private Date issueFromDate;						//领取起始时间
    @JsonSerialize(using=MyDateSerializer.class)
    private Date issueToDate;						//领取截止时间
    private Integer obtainNumberPerDayPerPerson;	//每天每人最多可领取的张数
    @JsonSerialize(using=MyDateSerializer.class)
    private Date startTime;							//有效期开始时间
    @JsonSerialize(using=MyDateSerializer.class)
    private Date stopTime;							//有效期结束时间
    private String shopName;
    private String goodsName;
    private String couponImg;
    
    private Integer number;//领取数量
    
	public UserCouponDto() {
		super();
	}

	public Long getUcId() {
		return ucId;
	}

	public void setUcId(Long ucId) {
		this.ucId = ucId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	
	public Integer getCouponStatus() {
		return couponStatus;
	}

	public void setCouponStatus(Integer couponStatus) {
		this.couponStatus = couponStatus;
	}

	public Date getObtainTime() {
		return obtainTime;
	}

	public void setObtainTime(Date obtainTime) {
		this.obtainTime = obtainTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public String getCouponDesc() {
		return couponDesc;
	}

	public void setCouponDesc(String couponDesc) {
		this.couponDesc = couponDesc;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getAvailableNumber() {
		return availableNumber;
	}

	public void setAvailableNumber(Integer availableNumber) {
		this.availableNumber = availableNumber;
	}

	public Integer getUsedNumber() {
		return usedNumber;
	}

	public void setUsedNumber(Integer usedNumber) {
		this.usedNumber = usedNumber;
	}

	public Integer getHeatDegree() {
		return heatDegree;
	}

	public void setHeatDegree(Integer heatDegree) {
		this.heatDegree = heatDegree;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
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

	public Integer getObtainNumberPerDayPerPerson() {
		return obtainNumberPerDayPerPerson;
	}

	public void setObtainNumberPerDayPerPerson(Integer obtainNumberPerDayPerPerson) {
		this.obtainNumberPerDayPerPerson = obtainNumberPerDayPerPerson;
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

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getCouponImg() {
		return couponImg;
	}

	public void setCouponImg(String couponImg) {
		this.couponImg = couponImg;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
    
}