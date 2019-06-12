package com.idcq.appserver.dto.cashcoupon;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateSerializer;

/**
 * 代金券dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月1日
 * @time 下午2:08:39
 */
public class CashCouponDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1181477036042389538L;
	
	private Long cashCouponId;
    private String cashCouponName;
    private String cashCouponDesc;			//代金券使用说明
    private Long issuerShopId;			//发行的商铺ID
    private Long shopId;
    private String shopName;
    @JsonIgnore
    private Long cashCouponImgId;		
    private Long heatDegree;				//热度
    private Long totalNumber;			//总数
    private Long availableNumber;		//可用数量
    private Long cityId;	
    private Integer value;
    private Double price;					//销售价格
    @JsonSerialize(using = MyDateSerializer.class) 
    private Date startTime;					//有效期开始时间
    @JsonSerialize(using = MyDateSerializer.class) 
    private Date stopTime;					//有效期结束时间
//    @JsonSerialize(using = CustomDateSerializer.class) 
//    private Date obtainTime;
    /*---added---*/
    private Integer conditionPrice;			//订单金额满多少元才可使用
    @JsonSerialize(using=MyDateSerializer.class)
    private Date issueFromDate;				//领取起始时间
    @JsonSerialize(using=MyDateSerializer.class)
    private Date issueToDate;				//领取结束时间
    private Long obtainNumberPerDayPerPerson;//每人每天最多可领取数量
    private Long useNumberPerOrder;		//每单1次最多可使用的张数
    private Integer useTogetherFlag;		//是否能和同类券一起使用：0（不能），1（可以）
    @JsonIgnore
    private Integer cashCouponStatus;		//状态
    /*-------------追加------------*/
    private String cashCouponImg;
    private Date obtainTime;
    /*---------20150714追加----------*/
    @JsonIgnore
    private Date endTime;				//代金券过期时间
    @JsonIgnore
    private Double cashCouponAmount;	//代金券余额
    @JsonIgnore
    private Double uccPrice;			//面额
    @JsonIgnore
    private Double usedPrice;			//已经使用的金额		
    /*---------20150722追加----------*/
    @JsonIgnore
    private Long uccId;					//用户代金券ID		
    
    
    
	public CashCouponDto() {
		super();
	}


	public Long getCashCouponId() {
		return cashCouponId;
	}


	public void setCashCouponId(Long cashCouponId) {
		this.cashCouponId = cashCouponId;
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


	public Long getIssuerShopId() {
		return issuerShopId;
	}


	public void setIssuerShopId(Long issuerShopId) {
		this.issuerShopId = issuerShopId;
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


	public Long getCashCouponImgId() {
		return cashCouponImgId;
	}


	public void setCashCouponImgId(Long cashCouponImgId) {
		this.cashCouponImgId = cashCouponImgId;
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


	public Long getCityId() {
		return cityId;
	}


	public void setCityId(Long cityId) {
		this.cityId = cityId;
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


	public Integer getCashCouponStatus() {
		return cashCouponStatus;
	}


	public void setCashCouponStatus(Integer cashCouponStatus) {
		this.cashCouponStatus = cashCouponStatus;
	}


	public String getCashCouponImg() {
		return cashCouponImg;
	}


	public void setCashCouponImg(String cashCouponImg) {
		this.cashCouponImg = cashCouponImg;
	}


	public Date getObtainTime() {
		return obtainTime;
	}


	public void setObtainTime(Date obtainTime) {
		this.obtainTime = obtainTime;
	}


	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}


	public Double getPrice() {
		return price;
	}


	public void setPrice(Double price) {
		this.price = price;
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
	public Double getUsedPrice() {
		return usedPrice;
	}
	public void setUsedPrice(Double usedPrice) {
		this.usedPrice = usedPrice;
	}
	public Long getUccId() {
		return uccId;
	}
	public void setUccId(Long uccId) {
		this.uccId = uccId;
	}
	
	
}