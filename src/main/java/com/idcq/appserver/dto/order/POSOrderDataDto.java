package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.utils.ToStringMethod;

/**
 * POS订单详情dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月13日
 * @time 上午11:38:49
 */
public class POSOrderDataDto implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 3570373792278133788L;

    private Date createTime; // 下单时间

    private Double discount; // 折扣

    private Double paidAmount; // 实收金额

    private Double total; // 菜单总价

    private Double outfee; // 服务费、杂费

    private Long billerId; // 下单员工

    private String mobile; // 会员电话

    private Long cashierId; // 收银员

    private Date payTime; // 交易时间

    private String payMode; // 支付方式 "现金","会员余额","刷卡"

    private String id; // 订单id shopID(10位)+yyMMddHHmmssSSS(到毫秒，15位)+随机数(5位)，

    private boolean isWm; // 是否为外卖
    
    private Integer settleFlag;//用户结算标识 已结算-1，未结算-0,反结账-2
    
    private List<POSOrderGoodsDto> orderInfo; // 商品信息
	private List<Integer> userShopCouponIdList;//优惠券id列表
	
	private String cashierUsername;
	
	private Integer isWait;
	
	public List<Integer> getUserShopCouponIdList() {
		return userShopCouponIdList;
	}

	public void setUserShopCouponIdList(List<Integer> userShopCouponIdList) {
		this.userShopCouponIdList = userShopCouponIdList;
	}

	public Double getCouponDiscountPrice() {
		return couponDiscountPrice;
	}

	public void setCouponDiscountPrice(Double couponDiscountPrice) {
		this.couponDiscountPrice = couponDiscountPrice;
	}

	private Double couponDiscountPrice;//优惠券抵扣金额
    /*----------------------*/
    private Integer orderStatus; // 订单状态

    private Integer orderSceneType;

    private Integer payStatus; // 支付状态

    /*---------20150624追加----------*/
    private String userRemark;

    /*---------20150701追加----------*/
    private Integer orderType; // 订单类型

    /*---------20150703追加----------*/
    private Integer consumerNum; // 订单消费人数

    private String seatIds; // 座位ID，多个用英文逗号分隔

    /*---------20150712追加----------*/
    private Integer isMaling; // 是否进行抹零：0无抹零，1抹元，2抹角

    private Double additionalDiscount; // 折上折

    private Double currenPayAmount;// 当前支付金额
    
    private Long clientLastTime;
    
    @JsonIgnore
    private Long orderPayId;
    
    private Long businessAreaActivityId;
    
    public Double getAdditionalDiscount()
    {
        return additionalDiscount;
    }

    public void setAdditionalDiscount(Double additionalDiscount)
    {
        this.additionalDiscount = additionalDiscount;
    }

    public String toString()
    {
        return ToStringMethod.toString(this);
    }

    public POSOrderDataDto()
    {
        super();
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public Double getDiscount()
    {
        return discount;
    }

    public void setDiscount(Double discount)
    {
        this.discount = discount;
    }

    public Double getPaidAmount()
    {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount)
    {
        this.paidAmount = paidAmount;
    }

    public Double getTotal()
    {
        return total;
    }

    public void setTotal(Double total)
    {
        this.total = total;
    }

    public Double getOutfee()
    {
        return outfee;
    }

    public void setOutfee(Double outfee)
    {
        this.outfee = outfee;
    }

    public Long getBillerId()
    {
        return billerId;
    }

    public void setBillerId(Long billerId)
    {
        this.billerId = billerId;
    }

    public String getMobile()
    {
        return mobile;
    }

    public void setMobile(String mobile)
    {
        if ("0".equals(mobile))
        {
            mobile = null;
        }
        this.mobile = mobile;
    }

    public Long getCashierId()
    {
        return cashierId;
    }

    public void setCashierId(Long cashierId)
    {
        this.cashierId = cashierId;
    }

    public Date getPayTime()
    {
        return payTime;
    }

    public void setPayTime(Date payTime)
    {
        this.payTime = payTime;
    }

    public String getPayMode()
    {
        return payMode;
    }

    public void setPayMode(String payMode)
    {
        this.payMode = payMode;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean getIsWm()
    {
        return isWm;
    }

    public void setIsWm(boolean isWm)
    {
        this.isWm = isWm;
    }

    public List<POSOrderGoodsDto> getOrderInfo()
    {
        return orderInfo;
    }

    public void setOrderInfo(List<POSOrderGoodsDto> orderInfo)
    {
        this.orderInfo = orderInfo;
    }

    public Integer getOrderStatus()
    {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus)
    {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderSceneType()
    {
        return orderSceneType;
    }

    public void setOrderSceneType(Integer orderSceneType)
    {
        this.orderSceneType = orderSceneType;
    }

    public Integer getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus)
    {
        this.payStatus = payStatus;
    }

    public String getUserRemark()
    {
        return userRemark;
    }

    public void setUserRemark(String userRemark)
    {
        this.userRemark = userRemark;
    }

    public Integer getOrderType()
    {
        return orderType;
    }

    public void setOrderType(Integer orderType)
    {
        this.orderType = orderType;
    }

    public Integer getConsumerNum()
    {
        return consumerNum;
    }

    public void setConsumerNum(Integer consumerNum)
    {
        this.consumerNum = consumerNum;
    }

    public String getSeatIds()
    {
        return seatIds;
    }

    public void setSeatIds(String seatIds)
    {
        this.seatIds = seatIds;
    }

    public Integer getIsMaling()
    {
        return isMaling;
    }

    public void setIsMaling(Integer isMaling)
    {
        this.isMaling = isMaling;
    }

    public Double getCurrenPayAmount()
    {
        return currenPayAmount;
    }

    public void setCurrenPayAmount(Double currenPayAmount)
    {
        this.currenPayAmount = currenPayAmount;
        if (currenPayAmount == null)
        {
            this.currenPayAmount = paidAmount;
        }
    }

    public Long getClientLastTime()
    {
        return clientLastTime;
    }

    public void setClientLastTime(Long clientLastTime)
    {
        this.clientLastTime = clientLastTime;
    }

	public Long getOrderPayId() {
		return orderPayId;
	}

	public void setOrderPayId(Long orderPayId) {
		this.orderPayId = orderPayId;
	}

    public Long getBusinessAreaActivityId() {
        return businessAreaActivityId;
    }

    public void setBusinessAreaActivityId(Long businessAreaActivityId) {
        this.businessAreaActivityId = businessAreaActivityId;
    }

    public String getCashierUsername() {
        return cashierUsername;
    }

    public void setCashierUsername(String cashierUsername) {
        this.cashierUsername = cashierUsername;
    }

    public Integer getIsWait() {
        return isWait;
    }

    public void setIsWait(Integer isWait) {
        this.isWait = isWait;
    }

	public Integer getSettleFlag() {
		return settleFlag;
	}

	public void setSettleFlag(Integer settleFlag) {
		this.settleFlag = settleFlag;
	}

	public void setWm(boolean isWm) {
		this.isWm = isWm;
	}

}
