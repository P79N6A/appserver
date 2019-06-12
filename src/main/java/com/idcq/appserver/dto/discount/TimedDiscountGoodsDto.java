package com.idcq.appserver.dto.discount;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 商品的限时折扣
 * 
 * @author Administrator
 * 
 * @date 2015年4月8日
 * @time 下午3:48:00
 */
public class TimedDiscountGoodsDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7181460678914080763L;
	private Long discountGoodsId;
    private Long shopId;
    private Long discountId;
    private Long goodsId;
    private Date createTime;
    private Integer goodsIndex;
    
    /*------------商铺的限时折扣--------------*/
    private Integer discountType;
    private String discountName;
    private Integer discountStatus;
    private String discountPeriodType;
    private Date dayFromTime;
    private Date dayToTime;
    private String week;
    private Date weekFromTime;
    private Date weekToTime;
    private Date customFromDatetime;
    private Date customToDatetime;
    private Float discount;
    private Integer applyOnlineFlag;
    private Integer applyOfflineFlag;
    
    public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String ds = sdf.format(new Date());
		System.out.println(ds);
		Date d = sdf.parse(ds);
		System.out.println(d.toLocaleString());
	}
    
    public Long getDiscountGoodsId() {
        return discountGoodsId;
    }

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Long getDiscountId() {
		return discountId;
	}

	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getGoodsIndex() {
		return goodsIndex;
	}

	public void setGoodsIndex(Integer goodsIndex) {
		this.goodsIndex = goodsIndex;
	}

	public void setDiscountGoodsId(Long discountGoodsId) {
		this.discountGoodsId = discountGoodsId;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}

	public Integer getDiscountStatus() {
		return discountStatus;
	}

	public void setDiscountStatus(Integer discountStatus) {
		this.discountStatus = discountStatus;
	}

	public String getDiscountPeriodType() {
		return discountPeriodType;
	}

	public void setDiscountPeriodType(String discountPeriodType) {
		this.discountPeriodType = discountPeriodType;
	}

	public Date getDayFromTime() {
		return dayFromTime;
	}

	public void setDayFromTime(Date dayFromTime) {
		this.dayFromTime = dayFromTime;
	}

	public Date getDayToTime() {
		return dayToTime;
	}

	public void setDayToTime(Date dayToTime) {
		this.dayToTime = dayToTime;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Date getWeekFromTime() {
		return weekFromTime;
	}

	public void setWeekFromTime(Date weekFromTime) {
		this.weekFromTime = weekFromTime;
	}

	public Date getWeekToTime() {
		return weekToTime;
	}

	public void setWeekToTime(Date weekToTime) {
		this.weekToTime = weekToTime;
	}

	public Date getCustomFromDatetime() {
		return customFromDatetime;
	}

	public void setCustomFromDatetime(Date customFromDatetime) {
		this.customFromDatetime = customFromDatetime;
	}

	public Date getCustomToDatetime() {
		return customToDatetime;
	}

	public void setCustomToDatetime(Date customToDatetime) {
		this.customToDatetime = customToDatetime;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Integer getApplyOnlineFlag() {
		return applyOnlineFlag;
	}

	public void setApplyOnlineFlag(Integer applyOnlineFlag) {
		this.applyOnlineFlag = applyOnlineFlag;
	}

	public Integer getApplyOfflineFlag() {
		return applyOfflineFlag;
	}

	public void setApplyOfflineFlag(Integer applyOfflineFlag) {
		this.applyOfflineFlag = applyOfflineFlag;
	}

	
    
    
}