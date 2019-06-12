package com.idcq.appserver.dto.shop;

import java.io.Serializable;

public class DistributionTakeoutSetting implements Serializable {

	private static final long serialVersionUID = -1794207938618529745L;
	
	private Long shopId;
	private int showIndex;
	private Integer settingType;//设置类型，配送=1/到店=2/外卖=3
	private Integer maxReserveBeforeDay;//最多提前预定的天数
	private Float minReserveBeforeHour;//至少提前几小时预定, 比如提前0.5小时
	private Float deliveryDistribution;//配送范围(公里)
	private Double leastBookPrice;//最低起订金额
	private Integer isCashOnDelivery;//是否支持货到付款，支持货到付款=1，不支持=0
	private Integer isCancelAnytime;//是否参与随时退，是=1,否=0
	private Integer cancelBeforeMinute;//订单到期前多少分钟可自动退
	private Float deliveryPrice;//配送费用，免费配送=0/收费>0
	private Integer isReduction;//是否使用满X元免配送费，使用=1，不使用=0
	private Double reduction;//满X元免配送费
	private Integer orderTimeType;//下单时间类型，仅当天=1/其他时间=2
	private String createTime;
	private String remark;
	public DistributionTakeoutSetting() {
		super();
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public int getShowIndex() {
		return showIndex;
	}
	public void setShowIndex(int showIndex) {
		this.showIndex = showIndex;
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
	public Float getMinReserveBeforeHour() {
		return minReserveBeforeHour;
	}
	public void setMinReserveBeforeHour(Float minReserveBeforeHour) {
		this.minReserveBeforeHour = minReserveBeforeHour;
	}
	public Float getDeliveryDistribution() {
		return deliveryDistribution;
	}
	public void setDeliveryDistribution(Float deliveryDistribution) {
		this.deliveryDistribution = deliveryDistribution;
	}
	public Double getLeastBookPrice() {
		return leastBookPrice;
	}
	public void setLeastBookPrice(Double leastBookPrice) {
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
	public Float getDeliveryPrice() {
		return deliveryPrice;
	}
	public void setDeliveryPrice(Float deliveryPrice) {
		this.deliveryPrice = deliveryPrice;
	}
	public Integer getIsReduction() {
		return isReduction;
	}
	public void setIsReduction(Integer isReduction) {
		this.isReduction = isReduction;
	}
	public Double getReduction() {
		return reduction;
	}
	public void setReduction(Double reduction) {
		this.reduction = reduction;
	}
	public Integer getOrderTimeType() {
		return orderTimeType;
	}
	public void setOrderTimeType(Integer orderTimeType) {
		this.orderTimeType = orderTimeType;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
