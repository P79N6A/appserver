package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.utils.NumberUtil;

/**
 * 订单详情dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:17:34
 */
public class OrderDetailDto implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = -1117234140398934292L;

    @JsonIgnore
    private String orderId;

    private String orderTitle;

    private Long userId; // 用户ID

    private Integer orderType; // 订单类型

    private Double orderTotalPrice; // 订单总价

    private Integer payTimeType; // 支付时间类型

    private Long addressId; // 配送地址ID

    // 外卖地址
    private String inputAddress;

    /*------------------------*/
    private Integer orderServiceType;// 订单服务类型

    private Date distributionTime; // 服务起始时间
    // @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")

    private Date serviceTimeFrom; // 服务起始时间

    private Date serviceTimeTo; // 服务截止时间

    private Double prepayMoney; // 预支付金额

    private Integer distributionType;// 配送方式

    /*------------------------*/
    private Long shopId;

    private String shopName;

    private Double memberDiscount;// 商铺会员折扣

    private Integer orderSceneType;

    private String userRemark;

    private Double logisticsPrice;

    private Integer orderStatus;

    private Integer payStatus;

    private Date orderTime;

    private String payCode;
    //订单是否被修改， 默认服务端在做查询订单时不更改订单
    @JsonIgnore
    private Boolean isUpdate = false;
    
    public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	/*------------------------*/
    private Double payedAmount; // 已经支付金额

    private Double notPayedAmount; // 未支付金额=总价-已支付

    private List<OrderGoodsDto> goods = new ArrayList<OrderGoodsDto>();

    /*------------------------*/
    private String refuseReason; // 拒绝退单原因

    private Double settlePrice; // 参与结算价格

    /*---------20150704从Integer改为Double----------*/
    private Integer goodsNumber;

    /*---------20150727追加----------*/
    private Integer isComment;
    @JsonIgnore
    private String seatIds;

    private String seatName;
    
    private Double memberTotalPrice;
    
    private Integer isMember;
    
    private String shopTelephone;
    
    private String shopLogoUrl;
    
    private Long columnId;
    
    private String orderMobile;

    private List<Map> payList;

    public List<Map> getPayList()
    {
        return payList;
    }

    public void setPayList(List<Map> payList)
    {
        this.payList = payList;
    }

    public OrderDetailDto()
    {
        super();
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public String getOrderTitle()
    {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle)
    {
        this.orderTitle = orderTitle;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public Double getOrderTotalPrice()
    {
        return orderTotalPrice;
    }

    public void setOrderTotalPrice(Double orderTotalPrice)
    {
        this.orderTotalPrice = orderTotalPrice;
    }

    public Integer getPayTimeType()
    {
        return payTimeType;
    }

    public void setPayTimeType(Integer payTimeType)
    {
        this.payTimeType = payTimeType;
    }

    public Long getAddressId()
    {
        return addressId;
    }

    public void setAddressId(Long addressId)
    {
        this.addressId = addressId;
    }

    public String getInputAddress()
    {
        return inputAddress;
    }

    public void setInputAddress(String inputAddress)
    {
        this.inputAddress = inputAddress;
    }

    public Integer getOrderServiceType()
    {
        return orderServiceType;
    }

    public void setOrderServiceType(Integer orderServiceType)
    {
        this.orderServiceType = orderServiceType;
    }

    public Date getDistributionTime()
    {
        return distributionTime;
    }

    public void setDistributionTime(Date distributionTime)
    {
        this.distributionTime = distributionTime;
    }

    public Date getServiceTimeFrom()
    {
        return serviceTimeFrom;
    }

    public void setServiceTimeFrom(Date serviceTimeFrom)
    {
        this.serviceTimeFrom = serviceTimeFrom;
    }

    public Date getServiceTimeTo()
    {
        return serviceTimeTo;
    }

    public void setServiceTimeTo(Date serviceTimeTo)
    {
        this.serviceTimeTo = serviceTimeTo;
    }

    public String getPrepayMoney()
    {
        return NumberUtil.roundDoubleToStr(prepayMoney, 2);
    }

    public void setPrepayMoney(Double prepayMoney)
    {
        this.prepayMoney = prepayMoney;
    }

    public Integer getDistributionType()
    {
        return distributionType;
    }

    public void setDistributionType(Integer distributionType)
    {
        this.distributionType = distributionType;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public Double getMemberDiscount()
    {
        return memberDiscount;
    }

    public void setMemberDiscount(Double memberDiscount)
    {
        this.memberDiscount = memberDiscount;
    }

    public Integer getOrderSceneType()
    {
        return orderSceneType;
    }

    public void setOrderSceneType(Integer orderSceneType)
    {
        this.orderSceneType = orderSceneType;
    }

    public String getUserRemark()
    {
        return userRemark;
    }

    public void setUserRemark(String userRemark)
    {
        this.userRemark = userRemark;
    }

    public String getLogisticsPrice()
    {
        return NumberUtil.roundDoubleToStr(logisticsPrice, 2);
    }

    public void setLogisticsPrice(Double logisticsPrice)
    {
        this.logisticsPrice = logisticsPrice;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Integer getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus)
    {
        this.payStatus = payStatus;
    }

    public Date getOrderTime()
    {
        return orderTime;
    }

    public void setOrderTime(Date orderTime)
    {
        this.orderTime = orderTime;
    }

    public String getPayedAmount()
    {
        return NumberUtil.roundDoubleToStr(payedAmount, 2);
    }

    public void setPayedAmount(Double payedAmount)
    {
        this.payedAmount = payedAmount;
    }

    public String getNotPayedAmount()
    {
        return NumberUtil.roundDoubleToStr(notPayedAmount, 2);
    }

    public void setNotPayedAmount(Double notPayedAmount)
    {
        this.notPayedAmount = notPayedAmount;
    }

    public List<OrderGoodsDto> getGoods()
    {
        return goods;
    }

    public void setGoods(List<OrderGoodsDto> goods)
    {
        this.goods = goods;
    }

    public String getRefuseReason()
    {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason)
    {
        this.refuseReason = refuseReason;
    }

    public String getSettlePrice()
    {
        return NumberUtil.roundDoubleToStr(settlePrice, 2);
    }

    public void setSettlePrice(Double settlePrice)
    {
        this.settlePrice = settlePrice;
    }

    public Integer getGoodsNumber()
    {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber)
    {
        this.goodsNumber = goodsNumber;
    }

    public Integer getIsComment()
    {
        return isComment;
    }

    public void setIsComment(Integer isComment)
    {
        this.isComment = isComment;
    }

    public String getSeatIds()
    {
        return seatIds;
    }

    public void setSeatIds(String seatIds)
    {
        this.seatIds = seatIds;
    }

    public String getSeatName()
    {
        return seatName;
    }

    public void setSeatName(String seatName)
    {
        this.seatName = seatName;
    }

    public Double getMemberTotalPrice()
    {
        return memberTotalPrice;
    }

    public void setMemberTotalPrice(Double memberTotalPrice)
    {
        this.memberTotalPrice = memberTotalPrice;
    }

    public Integer getIsMember()
    {
        return isMember;
    }

    public void setIsMember(Integer isMember)
    {
        this.isMember = isMember;
    }

    public String getShopTelephone() {
        return shopTelephone;
    }

    public void setShopTelephone(String shopTelephone) {
        this.shopTelephone = shopTelephone;
    }
    
    
    public String getShopLogoUrl() {
		return shopLogoUrl;
	}

	public void setShopLogoUrl(String shopLogoUrl) {
		this.shopLogoUrl = shopLogoUrl;
	}

	public Long getColumnId() {
        return columnId;
    }

    public void setColumnId(Long columnId) {
        this.columnId = columnId;
    }
    public Boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(Boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getOrderMobile() {
        return orderMobile;
    }

    public void setOrderMobile(String orderMobile) {
        this.orderMobile = orderMobile;
    }

    @Override
    public String toString()
    {
        return "OrderDetailDto [orderId=" + orderId + ", orderTitle=" + orderTitle + ", userId=" + userId
                + ", orderType=" + orderType + ", orderTotalPrice=" + orderTotalPrice + ", payTimeType=" + payTimeType
                + ", addressId=" + addressId + ", inputAddress=" + inputAddress + ", orderServiceType="
                + orderServiceType + ", distributionTime=" + distributionTime + ", serviceTimeFrom=" + serviceTimeFrom
                + ", serviceTimeTo=" + serviceTimeTo + ", prepayMoney=" + prepayMoney + ", distributionType="
                + distributionType + ", shopId=" + shopId + ", shopName=" + shopName + ", memberDiscount="
                + memberDiscount + ", orderSceneType=" + orderSceneType + ", userRemark=" + userRemark
                + ", logisticsPrice=" + logisticsPrice + ", orderStatus=" + orderStatus + ", payStatus=" + payStatus
                + ", orderTime=" + orderTime + ", payedAmount=" + payedAmount + ", notPayedAmount=" + notPayedAmount
                + ", goods=" + goods + ", refuseReason=" + refuseReason + ", settlePrice=" + settlePrice
                + ", goodsNumber=" + goodsNumber + ", isComment=" + isComment + "]";
    }

}