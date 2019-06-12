package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 店铺预约dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午11:12:38
 */
public class ShopBookSettingDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6994828002910650262L;
	private Long shopId;
	private Integer operateType;	//操作类型：0-新增；1-修改
	private Long settingId;			//设置ID
	private Integer settingType;	//设置类型，配送=1，预约到店=2，外卖=3，预约上门=4
	private BigDecimal deliveryPrice;	//配送费
	private Integer isReduction;	//是否使用满X元免配送费
	private BigDecimal reduction;		//满X元免配送费
	private Integer isCashOnDelivery;//是否支持货到付款
	private Integer maxReserveBeforeDay;//最多提前预定的天数
	private BigDecimal minReserveBeforeHour;//最少提前几小时预定
	private Float deliveryDistribution;//配送范围
	private BigDecimal leastBookPrice;	//最低起订金额
	private Integer isCancelAnytime;//是否参与随时退
	private String deliveryTime;	//接单时间，多个日期以英文分号分隔，如7:00&12:00,14:00&18:00
	private String stopDate;		//暂停预定日期
	private String weekDay;			//可预定周期
	/*------------20150817-------------*/
	private String remark;
	
	public ShopBookSettingDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public Long getSettingId() {
		return settingId;
	}
	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}
	public Integer getSettingType() {
		return settingType;
	}
	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
	}
	
	public BigDecimal getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(BigDecimal deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	public Integer getIsReduction() {
		return isReduction;
	}
	public void setIsReduction(Integer isReduction) {
		this.isReduction = isReduction;
	}
	
	public BigDecimal getReduction() {
		return reduction;
	}
	public void setReduction(BigDecimal reduction) {
		this.reduction = reduction;
	}
	public Integer getIsCashOnDelivery() {
		return isCashOnDelivery;
	}
	public void setIsCashOnDelivery(Integer isCashOnDelivery) {
		this.isCashOnDelivery = isCashOnDelivery;
	}
	
	public Integer getMaxReserveBeforeDay() {
		return maxReserveBeforeDay;
	}
	public void setMaxReserveBeforeDay(Integer maxReserveBeforeDay) {
		this.maxReserveBeforeDay = maxReserveBeforeDay;
	}
	public BigDecimal getMinReserveBeforeHour() {
		return minReserveBeforeHour;
	}
	public void setMinReserveBeforeHour(BigDecimal minReserveBeforeHour) {
		this.minReserveBeforeHour = minReserveBeforeHour;
	}
	public Float getDeliveryDistribution() {
		return deliveryDistribution;
	}
	public void setDeliveryDistribution(Float deliveryDistribution) {
		this.deliveryDistribution = deliveryDistribution;
	}
	
	public BigDecimal getLeastBookPrice() {
		return leastBookPrice;
	}
	public void setLeastBookPrice(BigDecimal leastBookPrice) {
		this.leastBookPrice = leastBookPrice;
	}
	public Integer getIsCancelAnytime() {
		return isCancelAnytime;
	}
	public void setIsCancelAnytime(Integer isCancelAnytime) {
		this.isCancelAnytime = isCancelAnytime;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getStopDate() {
		return stopDate;
	}
	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}
	public String getWeekDay() {
		return weekDay;
	}
	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
