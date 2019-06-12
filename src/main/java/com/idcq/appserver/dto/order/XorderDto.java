package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 非会员订单dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月30日
 * @time 下午5:51:17
 */
public class XorderDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3178790843055149826L;
	private String xorderId;
    private String userInfo;
    private Integer orderType;
    private Date orderTime;
    private Integer payStatus;
    private Double goodsPriceBeforeDiscount;
    private Double goodsPrice;
    private Double logisticsPrice;
    private Double orderTotalPrice;
    private Double settlePrice;
    private Double prepayMoney;
    private Integer settleFlag;
    private Integer distributionType;
    private Date distributionTime;
    private Long addressId;
    private Integer payTimeType;
    private Integer orderServiceType;
    private Date serviceTimeFrom;
    private Date serviceTimeTo;
    private Long shopId;
    private Integer orderStatus;
    private Integer orderSceneType;
    
    /*---------------------*/
    private Double maling ;
    /*---------20150703追加----------*/
    @JsonIgnore
	private Integer consumerNum;				//订单消费人数
    @JsonIgnore
	private String seatIds;						//座位ID，多个用英文逗号分隔
    /*---------20150704追加----------*/
    @JsonIgnore
    private Integer order_source;				//订单来源，主要用于区别是否自助下单
    @JsonIgnore
    private Integer goodsNumber;				//订单商品数量
    /*---------20150712追加----------*/
	private Integer isMaling;					//是否进行抹零：0无抹零，1抹元，2抹角
	/*---------20150801追加----------*/
	@JsonIgnore
    private String mobile;
	@JsonIgnore
	private String orderTitle;
    @JsonIgnore
    private Double orderDiscount;				//收银机折上折
    @JsonIgnore
    private Long cashierId;				    	//收银员 ID  
    @JsonIgnore
    private String cashierUsername;	//收银员   
    
    @JsonIgnore
    private Integer deleteType;
    /*---------------20151012追加by szp ---------------*/
    @JsonIgnore
    private Integer orderChannelType; //下单方式 1-APP下单,2-收银机下单,3-商品后台下单 4.一点管家下单
    @JsonIgnore 
    private Integer settleType; // 结算方式 0-按商品目录价结算,1-按订单总价结算
    
    @JsonIgnore
    private Integer payType;//支付类型
    
    @JsonIgnore
    private String remark;
    
	public XorderDto() {
		super();
	}
	public String getXorderId() {
		return xorderId;
	}
	public void setXorderId(String xorderId) {
		this.xorderId = xorderId;
	}
	public String getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Date getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Double getGoodsPriceBeforeDiscount() {
		return goodsPriceBeforeDiscount;
	}
	public void setGoodsPriceBeforeDiscount(Double goodsPriceBeforeDiscount) {
		this.goodsPriceBeforeDiscount = goodsPriceBeforeDiscount;
	}
	public Double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(Double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public Double getLogisticsPrice() {
		return logisticsPrice;
	}
	public void setLogisticsPrice(Double logisticsPrice) {
		this.logisticsPrice = logisticsPrice;
	}
	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}
	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}
	public Double getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}
	public Double getPrepayMoney() {
		return prepayMoney;
	}
	public void setPrepayMoney(Double prepayMoney) {
		this.prepayMoney = prepayMoney;
	}
	public Integer getSettleFlag() {
		return settleFlag;
	}
	public void setSettleFlag(Integer settleFlag) {
		this.settleFlag = settleFlag;
	}
	public Integer getDistributionType() {
		return distributionType;
	}
	public void setDistributionType(Integer distributionType) {
		this.distributionType = distributionType;
	}
	public Date getDistributionTime() {
		return distributionTime;
	}
	public void setDistributionTime(Date distributionTime) {
		this.distributionTime = distributionTime;
	}
	public Long getAddressId() {
		return addressId;
	}
	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}
	public Integer getPayTimeType() {
		return payTimeType;
	}
	public void setPayTimeType(Integer payTimeType) {
		this.payTimeType = payTimeType;
	}
	public Integer getOrderServiceType() {
		return orderServiceType;
	}
	public void setOrderServiceType(Integer orderServiceType) {
		this.orderServiceType = orderServiceType;
	}
	public Date getServiceTimeFrom() {
		return serviceTimeFrom;
	}
	public void setServiceTimeFrom(Date serviceTimeFrom) {
		this.serviceTimeFrom = serviceTimeFrom;
	}
	public Date getServiceTimeTo() {
		return serviceTimeTo;
	}
	public void setServiceTimeTo(Date serviceTimeTo) {
		this.serviceTimeTo = serviceTimeTo;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getOrderSceneType() {
		return orderSceneType;
	}
	public void setOrderSceneType(Integer orderSceneType) {
		this.orderSceneType = orderSceneType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Double getMaling() {
		return maling;
	}
	public void setMaling(Double maling) {
		this.maling = maling;
	}
	public String getSeatIds() {
		return seatIds;
	}
	public void setSeatIds(String seatIds) {
		this.seatIds = seatIds;
	}
	public Integer getConsumerNum() {
		return consumerNum;
	}
	public void setConsumerNum(Integer consumerNum) {
		this.consumerNum = consumerNum;
	}
	public Integer getOrder_source() {
		return order_source;
	}
	public void setOrder_source(Integer order_source) {
		this.order_source = order_source;
	}
	public Integer getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public Integer getIsMaling() {
		return isMaling;
	}
	public void setIsMaling(Integer isMaling) {
		this.isMaling = isMaling;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public Double getOrderDiscount() {
		return orderDiscount;
	}
	public void setOrderDiscount(Double orderDiscount) {
		this.orderDiscount = orderDiscount;
	}
	public Long getCashierId() {
		return cashierId;
	}
	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}
	public Integer getDeleteType() {
		return deleteType;
	}
	public void setDeleteType(Integer deleteType) {
		this.deleteType = deleteType;
	}
	public Integer getOrderChannelType() {
		return orderChannelType;
	}
	public void setOrderChannelType(Integer orderChannelType) {
		this.orderChannelType = orderChannelType;
	}
	public Integer getSettleType() {
		return settleType;
	}
	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}
	public String getCashierUsername() {
		return cashierUsername;
	}
	public void setCashierUsername(String cashierUsername) {
		this.cashierUsername = cashierUsername;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}