package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 加配送/到店/外卖规则设置
 * 
 * @author Administrator
 * 
 * @date 2015年7月14日
 * @time 下午4:37:09
 */
public class DistribTakeoutSettingDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8293877212394854294L;
	
	private Long settingId;
    private Long shopId;	
    private Integer settingType;			//设置类型，配送=1/到店=2/外卖=3
    private Integer maxReserveBeforeDay;	//最多提前预定的天数
    private BigDecimal minReserveBeforeHour;//至少提前几小时预定
    private Float deliveryDistribution;		//配送范围(公里)
    private BigDecimal leastBookPrice;		//最低起订金额
    private Integer isCashOnDelivery;		//是否支持货到付款
    private Integer isCancelAnytime;		//是否参与随时退
    private Integer cancelBeforeMinute;		//订单到期前多少分钟可自动退
    private BigDecimal deliveryPrice;		//配送费用
    private Integer isReduction;			//是否使用满X元免配送费
    private BigDecimal reduction;			//满X元免配送费
    private Integer orderTimeType;			//下单时间类型
    private Date createTime;				//创建时间
    private String remark;					//备注
    
    /*--------预定时间规则---------*/
    private String deliveryTime;			//接单时间
    private String stopDate;				//暂停预定日期
    private String weekDay;					//可预定周期
    /*--------20150818---------*/
    private Integer flag;					//规则启用标志
    
    
    
    
	public DistribTakeoutSettingDto() {
		super();
	}
	
	public Long getSettingId() {
		return settingId;
	}

	public void setSettingId(Long settingId) {
		this.settingId = settingId;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getSettingType() {
		return settingType;
	}
	public void setSettingType(Integer settingType) {
		this.settingType = settingType;
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
	public Integer getIsCashOnDelivery() {
		return isCashOnDelivery;
	}
	public void setIsCashOnDelivery(Integer isCashOnDelivery) {
		this.isCashOnDelivery = isCashOnDelivery;
	}
	public Integer getIsCancelAnytime() {
		return isCancelAnytime;
	}
	public void setIsCancelAnytime(Integer isCancelAnytime) {
		this.isCancelAnytime = isCancelAnytime;
	}
	public Integer getCancelBeforeMinute() {
		return cancelBeforeMinute;
	}
	public void setCancelBeforeMinute(Integer cancelBeforeMinute) {
		this.cancelBeforeMinute = cancelBeforeMinute;
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
	public Integer getOrderTimeType() {
		return orderTimeType;
	}
	public void setOrderTimeType(Integer orderTimeType) {
		this.orderTimeType = orderTimeType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}
    
    

}