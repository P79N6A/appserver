package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 店铺外卖费用配置dto
 * 
 * @author Administrator
 * 
 * @date 2015年5月11日
 * @time 下午7:03:35
 */
public class TakeoutSetDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1943742513942201469L;
	@JsonIgnore
	private Long id;
	@JsonIgnore
    private Integer shopId;
    private String paymentType;			//支付方式
    @JsonIgnore
    private Integer maxPreorder;		//最多提前预定天数
    @JsonIgnore
    private Integer minPreorder;		//至少提前预定天数
    @JsonIgnore
    private String deliveryTime;		//配送时间列表
    @JsonIgnore
    private Integer deliveryDistribution;//配送范围：公里
    @JsonIgnore
    private Integer status;				//状态
    private Integer deliveryPrice;		//配送费用
    @JsonIgnore
    private Integer isReduction;			//是否使用满x元免费配送
    private Double reduction;			//满X元免配送费 
    private BigDecimal bookMoney;		//预定金
    private Integer totalMoneyFlag;		//是否要求全款标记：0（不要求全款），1（全款）
    private String remark;				//备注
    
	public TakeoutSetDto() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public Integer getMaxPreorder() {
		return maxPreorder;
	}

	public void setMaxPreorder(Integer maxPreorder) {
		this.maxPreorder = maxPreorder;
	}

	public Integer getMinPreorder() {
		return minPreorder;
	}

	public void setMinPreorder(Integer minPreorder) {
		this.minPreorder = minPreorder;
	}

	public String getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	public Integer getDeliveryDistribution() {
		return deliveryDistribution;
	}

	public void setDeliveryDistribution(Integer deliveryDistribution) {
		this.deliveryDistribution = deliveryDistribution;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDeliveryPrice() {
		return deliveryPrice;
	}

	public void setDeliveryPrice(Integer deliveryPrice) {
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

	public BigDecimal getBookMoney() {
		return bookMoney;
	}

	public void setBookMoney(BigDecimal bookMoney) {
		this.bookMoney = bookMoney;
	}

	public Integer getTotalMoneyFlag() {
		return totalMoneyFlag;
	}

	public void setTotalMoneyFlag(Integer totalMoneyFlag) {
		this.totalMoneyFlag = totalMoneyFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	
    
}