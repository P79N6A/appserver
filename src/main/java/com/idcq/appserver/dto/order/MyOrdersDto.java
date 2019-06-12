package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

/**
 * 我的订单dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月20日
 * @time 上午10:17:34
 */
public class MyOrdersDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3069780820434493985L;

	// 订单Id
	private String orderId;

	// 配送方式 即时配送-0，定时配送-1
	private Integer distributionType;

	// 未消费-0,待支付-1,待评价-2,已完成-3 
	private Integer orderStatus;
	
	//未支付-0,已支付-1,支付失败-2,用户取消-3
	private Integer payStatus;

	// 配送时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date distributionTime;

	// 订单服务类型 0:到店服务 1:上门服务
	private Integer orderServiceType;

	// 服务起始时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date serviceTimeFrom;

	// 服务截止时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date serviceTimeTo;

	// 订单类型
	private Integer orderType;

	// 下单时间
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date orderTime;

	// 预付金额
	private Double prepayMoney;
	
	// 用户ID
	@JsonIgnore
	private Long userId;

	// 支付时间类型
	private Integer payTimeType;

	// 配送地址ID
	private Long addressId;

	// 订单总价
	private Double orderTotalPrice;

	// 是否排序 0-时间排序，1-商铺名称排序 (接口变更暂时不用)
	@JsonIgnore
	private Integer orderBy;
	
	//订单标题
	private String orderTitle;
	
	//商铺id
	private Long shopId;
	
	//商铺名称
	private String shopName;
	
	//订单场景分类：0：预定单 1：到店点菜订单 2：外卖订单 3：服务订单 4：商品订单 
	private Integer orderSceneType;
	
	//订单备注 
	private String userRemark;
	
	//服务费 
	private Double logisticsPrice;

	private Double memberDiscount;
	
	private Double goodsPriceBeforeDiscount;
	
	private Double goodsPrice;
	// 商品信息
	@JsonIgnore
	private List<OrderShopGoodsDto> goods;
	
	//订单商品数量，此字段对应订单表中的order_goods_number
	private Integer goodsNumber;
	
	private Double payedAmount;
	
	private Double notPayedAmount;
	
	private Double settlePrice;
	
	private Integer isComment;
	
	private String refuseReason;
	
	private String columnId;
	
	private Double orderOriginalPrice;
	
	//商品信息
	private List<Map<String, Object>> orderGoodsList;
	//
	private String shopLogoUrl;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getDistributionType() {
		return distributionType;
	}

	public void setDistributionType(Integer distributionType) {
		this.distributionType = distributionType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public String getShopLogoUrl()
    {
        return shopLogoUrl;
    }

    public void setShopLogoUrl(String shopLogoUrl)
    {
        this.shopLogoUrl = shopLogoUrl;
    }

    public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public Date getDistributionTime() {
		return distributionTime;
	}

	public void setDistributionTime(Date distributionTime) {
		this.distributionTime = distributionTime;
	}

	public Integer getOrderServiceType() {
		return orderServiceType;
	}



	public List<Map<String, Object>> getOrderGoodsList()
    {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<Map<String, Object>> orderGoodsList)
    {
        this.orderGoodsList = orderGoodsList;
    }

    public void setOrderServiceType(Integer orderServiceType)
    {
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


	public Double getPrepayMoney() {
		return prepayMoney;
	}

	public void setPrepayMoney(Double prepayMoney) {
		this.prepayMoney = prepayMoney;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getPayTimeType() {
		return payTimeType;
	}

	public void setPayTimeType(Integer payTimeType) {
		this.payTimeType = payTimeType;
	}

	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Double getOrderTotalPrice() {
		return orderTotalPrice;
	}

	public void setOrderTotalPrice(Double orderTotalPrice) {
		this.orderTotalPrice = orderTotalPrice;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public List<OrderShopGoodsDto> getGoods() {
		return goods;
	}

	public void setGoods(List<OrderShopGoodsDto> goods) {
		this.goods = goods;
	}

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
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

	public Integer getOrderSceneType() {
		return orderSceneType;
	}

	public void setOrderSceneType(Integer orderSceneType) {
		this.orderSceneType = orderSceneType;
	}
	
	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	public Double getLogisticsPrice() {
		return logisticsPrice;
	}

	public void setLogisticsPrice(Double logisticsPrice) {
		this.logisticsPrice = logisticsPrice;
	}

	public Double getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
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

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Double getPayedAmount() {
		return payedAmount;
	}

	public void setPayedAmount(Double payedAmount) {
		this.payedAmount = payedAmount;
	}

	public Double getNotPayedAmount() {
		return notPayedAmount;
	}

	public void setNotPayedAmount(Double notPayedAmount) {
		this.notPayedAmount = notPayedAmount;
	}

	public Double getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}

	public Integer getIsComment() {
		return isComment;
	}

	public void setIsComment(Integer isComment) {
		this.isComment = isComment;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

    public String getColumnId()
    {
        return columnId;
    }

    public void setColumnId(String columnId)
    {
        this.columnId = columnId;
    }

    public Double getOrderOriginalPrice() {
        return orderOriginalPrice;
    }

    public void setOrderOriginalPrice(Double orderOriginalPrice) {
        this.orderOriginalPrice = orderOriginalPrice;
    }

}
