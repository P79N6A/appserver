package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.common.enums.OrderStatusEnum;

/**
 * 订单dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:17:34
 */
public class OrderDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2205395592081909944L;
	private String orderId;
	private Long userId; // 用户ID
	private Integer orderType; // 订单类型
	private Date orderTime; // 下单时间
	@JsonIgnore
	private Integer payStatus; // 支付状态
	@JsonIgnore
	private Double goodsPriceBeforeDiscount; // 折前商品总价
	@JsonIgnore
	private Double goodsPrice; // 商品总价(折后)
	// @JsonIgnore
	private Double logisticsPrice; // 物流费用
	private Double orderTotalPrice; // 订单总价
	@JsonIgnore
	private Double settlePrice; // 参与结算的总价
	@JsonIgnore
	private Integer settleFlag; // 结算标示
	// @JsonIgnore
	private Integer distributionType;// 配送方式
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date distributionTime; // 配送时间
	private Integer payTimeType; // 支付时间类型
	private Long addressId; // 配送地址ID
	private String orderTitle; // 订单标题
	private String payCode;//订单支付码

	/*------------------------*/
	private Integer orderServiceType;// 订单服务类型
	private Date serviceTimeFrom; // 服务起始时间
	private Date serviceTimeTo; // 服务截止时间
	private Double prepayMoney; // 预支付金额
	// @JsonIgnore
	private String userRemark;
	@JsonIgnore
	private String userRemarkTime;
	@JsonIgnore
	private Long kefuId;
	@JsonIgnore
	private String kefuRemark;
	@JsonIgnore
	private String kefuRemarkTime;
	/**
	 * 需要扣出的保证金
	 */
	private Double platformTotalIncome;

	private List<OrderGoodsDto> goods = new ArrayList<OrderGoodsDto>();
	/*--------------------------*/
	@JsonIgnore
	private Double advancePrice; // 预付金额
	@JsonIgnore
	private Double discount; // 会员折扣
	@JsonIgnore
	private Double maling; // 抹零价
	/*--------------------------*/
	@JsonIgnore
	private Integer orderStatus; // 订单状态：未消费-0,待支付-1,待评价-2,已完成-3
	private Long shopId;
	// 订单场景分类：
	// 0：预定单
	// 1：到店点菜订单
	// 2：外卖订单
	// 3：服务订单
	// 4：商品订单
	private Integer orderSceneType; // 下订单场景
	@JsonIgnore
	private String shopName;
	@JsonIgnore
	private Double memberDiscount;// 商铺会员折扣
	@JsonIgnore
	private Long couponId;
	@JsonIgnore
	private Integer couponNum;
	@JsonIgnore
	private String inputAddress;
	@JsonIgnore
	private int addOrEditFlag; // 订单新增或修改：1为新增；2为修改
	@JsonIgnore
	private Date lastUpdateTime;// 最后跟新时间
	@JsonIgnore
	private String refuseReason;// 商家退订原因
	/*---------20150703追加----------*/
	@JsonIgnore
	private Integer consumerNum; // 订单消费人数
	private String seatIds; // 座位ID，多个用英文逗号分隔
	/*---------20150704追加----------*/
	@JsonIgnore
	private Integer goodsNumber; // 订单商品数量
	public Double getPlatformTotalIncome() {
		return platformTotalIncome;
	}

	public void setPlatformTotalIncome(Double platformTotalIncome) {
		this.platformTotalIncome = platformTotalIncome;
	}

	private Integer orderSource; // 订单来源，主要用于区别是否自助下单
	private Integer isActiveRefund;// 是否用户主动退单，仅=1为用户主动退单
	/*---------20150712追加----------*/
	@JsonIgnore
	private Integer isMaling; // 是否进行抹零：0无抹零，1抹元，2抹角
	@JsonIgnore
	private BigDecimal leastBookPrice; // 最低起订金额

	private Integer isComment;
	@JsonIgnore
	private Integer deleteType;
	@JsonIgnore
	private Integer confirmMinute;
	@JsonIgnore
	private Double orderDiscount; // 收银机折上折

	@JsonIgnore
	private Long cashierId; // 收银员

	/*---------------20150915追加by huangrui ---------------*/
	@JsonIgnore
	private Double orderRealSettlePrice; // 平台实收款内参与结算金额=参与折扣商品总价
	private Date orderFinishTime; // 订单完成时间
	private Date orderDebookTime; // 订单退订时间
	/*---------------20151012追加by szp ---------------*/
	@JsonIgnore
	private Integer orderChannelType; // 下单方式 1-APP下单,2-收银机下单,3-商品后台下单
	@JsonIgnore
	private Integer settleType; // 结算方式 0-按商品目录价结算,1-按订单总价结算
	@JsonIgnore
	private Integer shopSettleFlag; // 店铺结算标识
	@JsonIgnore
	private Date shopSettleTime; // 店铺结算时间
	@JsonIgnore
	private Date settleTime; // 用户结算时间
	@JsonIgnore
	private String cashierUsername; // 收银员

	// ----------一点管家扫码支付--11.20-start---------
	@JsonIgnore
	private Integer payType;// 支付类型

	/**
	 * 用户支付的手机
	 */
	@JsonIgnore
	private String mobile;

	/**
	 * 雇员用户名
	 */
	@JsonIgnore
	private String userName;
	/**
	 * 店铺备注
	 */
	@JsonIgnore
	private String remark;

	/**
	 * 是否会员订单：是=1，否=0
	 */
	private Integer isMember;

	@JsonIgnore
	private Long clientLastTime;
	@JsonIgnore
	private Long serverLastTime;
	@JsonIgnore
	private String shopTelephone;
	private Long businessAreaActivityId;
	@JsonIgnore
	private Double sendRedPacketMoney;

	private Long columnId;
	
	private Long wxShopId;

	
	private Integer peopleNumber;
	// ----------一点管家扫码支付--11.20-end---------
	@JsonIgnore
	private String orderCode;
	private double deductAmount;//抵扣金额
	private List<Integer> userShopCouponIdList;//优惠券id列表
	
	private Double couponDiscountPrice;
	
	private Integer clientSystemType;//客户端系统类型:1=收银机,2=一点管家,3=消费者APP,4=微信商城,5=公众号,6=商铺后台,7=收银PAD,8=路由器,
	
	private Integer isWait;
	@JsonIgnore 
	private Date clientFinishTime;
	@JsonIgnore
	private Double platformTotalIncomePrice;
	@JsonIgnore
	private Double orderDiscountPrice;  
	private Double shopMemberDiscount;
	@JsonIgnore
	private String tokenId;
	//消费类型：0-开桌；1-快餐。
	@JsonIgnore
	private Integer consumeType;

	public Integer getConsumeType()
	{
		return consumeType;
	}

	public void setConsumeType(Integer consumeType)
	{
		this.consumeType = consumeType;
	}

	public String getTokenId()
	{
		return tokenId;
	}

	public void setTokenId(String tokenId)
	{
		this.tokenId = tokenId;
	}

	public Double getCouponDiscountPrice() {
		return couponDiscountPrice;
	}

	public Long getWxShopId() {
		return wxShopId;
	}

	public void setWxShopId(Long wxShopId) {
		this.wxShopId = wxShopId;
	}

	public void setCouponDiscountPrice(Double couponDiscountPrice) {
		this.couponDiscountPrice = couponDiscountPrice;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}
	public Date getOrderFinishTime() {
		return orderFinishTime;
	}

	public void setOrderFinishTime(Date orderFinishTime) {
		this.orderFinishTime = orderFinishTime;
	}

	public Date getOrderDebookTime() {
		return orderDebookTime;
	}

	public void setOrderDebookTime(Date orderDebookTime) {
		this.orderDebookTime = orderDebookTime;
	}

	public Double getOrderRealSettlePrice() {
		return orderRealSettlePrice;
	}

	public void setOrderRealSettlePrice(Double orderRealSettlePrice) {
		this.orderRealSettlePrice = orderRealSettlePrice;
	}

	public OrderDto() {
		super();
	}

	public Double getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public String getInputAddress() {
		return inputAddress;
	}

	public void setInputAddress(String inputAddress) {
		this.inputAddress = inputAddress;
	}

	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Double getShopMemberDiscount() {
		return shopMemberDiscount;
	}

	public void setShopMemberDiscount(Double shopMemberDiscount) {
		this.shopMemberDiscount = shopMemberDiscount;
	}

	public List<OrderGoodsDto> getGoods() {
		return goods;
	}
	public void setGoods(List<OrderGoodsDto> goods) {
		this.goods = goods;
	}
	public Integer getOrderServiceType() {
		return orderServiceType;
	}

	public void setOrderServiceType(Integer orderServiceType) {
		this.orderServiceType = orderServiceType;
	}

	public Double getPrepayMoney() {
		return prepayMoney;
	}
	public void setPrepayMoney(Double prepayMoney) {
		this.prepayMoney = prepayMoney;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	public String getUserRemarkTime() {
		return userRemarkTime;
	}
	public void setUserRemarkTime(String userRemarkTime) {
		this.userRemarkTime = userRemarkTime;
	}
	public Long getKefuId() {
		return kefuId;
	}
	public void setKefuId(Long kefuId) {
		this.kefuId = kefuId;
	}
	public String getKefuRemark() {
		return kefuRemark;
	}
	public void setKefuRemark(String kefuRemark) {
		this.kefuRemark = kefuRemark;
	}
	public String getKefuRemarkTime() {
		return kefuRemarkTime;
	}
	public void setKefuRemarkTime(String kefuRemarkTime) {
		this.kefuRemarkTime = kefuRemarkTime;
	}

	public Date getDistributionTime() {
		return distributionTime;
	}
	public void setDistributionTime(Date distributionTime) {
		this.distributionTime = distributionTime;
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
	public String getOrderTitle() {
		return orderTitle;
	}
	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
	}
	public Double getAdvancePrice() {
		return advancePrice;
	}
	public void setAdvancePrice(Double advancePrice) {
		this.advancePrice = advancePrice;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getMaling() {
		return maling;
	}
	public void setMaling(Double maling) {
		this.maling = maling;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Integer getOrderSceneType() {
		return orderSceneType;
	}
	public void setOrderSceneType(Integer orderSceneType) {
		this.orderSceneType = orderSceneType;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;

		// 完成订单需设置完成时间
		if (orderStatus != null
				&& orderStatus.intValue() == OrderStatusEnum.SETTLE.getValue()
						.intValue()) {
			setOrderFinishTime(new Date());
		}

		// 已退单需设置退订时间
		if (orderStatus != null
				&& orderStatus.intValue() == OrderStatusEnum.STOPED.getValue()
						.intValue()) {
			setOrderDebookTime(new Date());
		}
	}

	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public Long getCouponId() {
		return couponId;
	}
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}
	public Integer getCouponNum() {
		return couponNum;
	}
	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}
	public int getAddOrEditFlag() {
		return addOrEditFlag;
	}
	public void setAddOrEditFlag(int addOrEditFlag) {
		this.addOrEditFlag = addOrEditFlag;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	public Integer getConsumerNum() {
		return consumerNum;
	}

	public void setConsumerNum(Integer consumerNum) {
		this.consumerNum = consumerNum;
	}

	public String getSeatIds() {
		return seatIds;
	}

	public void setSeatIds(String seatIds) {
		this.seatIds = seatIds;
	}
	public Integer getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public void setOrderSource(Integer orderSource) {
		this.orderSource = orderSource;
	}
	public Integer getIsActiveRefund() {
		return isActiveRefund;
	}
	public void setIsActiveRefund(Integer isActiveRefund) {
		this.isActiveRefund = isActiveRefund;
	}
	public Integer getOrderSource() {
		return orderSource;
	}
	public Integer getIsMaling() {
		return isMaling;
	}
	public void setIsMaling(Integer isMaling) {
		this.isMaling = isMaling;
	}
	public BigDecimal getLeastBookPrice() {
		return leastBookPrice;
	}
	public void setLeastBookPrice(BigDecimal leastBookPrice) {
		this.leastBookPrice = leastBookPrice;
	}

	public Integer getIsComment() {
		return isComment;
	}

	public void setIsComment(Integer isComment) {
		this.isComment = isComment;
	}

	public Integer getDeleteType() {
		return deleteType;
	}

	public void setDeleteType(Integer deleteType) {
		this.deleteType = deleteType;
	}

	public Integer getConfirmMinute() {
		return confirmMinute;
	}

	public void setConfirmMinute(Integer confirmMinute) {
		this.confirmMinute = confirmMinute;
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

	public Integer getShopSettleFlag() {
		return shopSettleFlag;
	}

	public void setShopSettleFlag(Integer shopSettleFlag) {
		this.shopSettleFlag = shopSettleFlag;
	}

	public Date getShopSettleTime() {
		return shopSettleTime;
	}

	public void setShopSettleTime(Date shopSettleTime) {
		this.shopSettleTime = shopSettleTime;
	}

	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}

	public String getCashierUsername() {
		return cashierUsername;
	}

	public void setCashierUsername(String cashierUsername) {
		this.cashierUsername = cashierUsername;
	}

	@Override
	public String toString() {
		return "OrderDto [orderId=" + orderId + ", userId=" + userId
				+ ", orderType=" + orderType + ", orderTime=" + orderTime
				+ ", payStatus=" + payStatus + ", goodsPriceBeforeDiscount="
				+ goodsPriceBeforeDiscount + ", goodsPrice=" + goodsPrice
				+ ", logisticsPrice=" + logisticsPrice + ", orderTotalPrice="
				+ orderTotalPrice + ", settlePrice=" + settlePrice
				+ ", settleFlag=" + settleFlag + ", distributionType="
				+ distributionType + ", distributionTime=" + distributionTime
				+ ", payTimeType=" + payTimeType + ", addressId=" + addressId
				+ ", orderTitle=" + orderTitle + ", orderServiceType="
				+ orderServiceType + ", serviceTimeFrom=" + serviceTimeFrom
				+ ", serviceTimeTo=" + serviceTimeTo + ", prepayMoney="
				+ prepayMoney + ", userRemark=" + userRemark
				+ ", userRemarkTime=" + userRemarkTime + ", kefuId=" + kefuId
				+ ", kefuRemark=" + kefuRemark + ", kefuRemarkTime="
				+ kefuRemarkTime + ", goods=" + goods + ", advancePrice="
				+ advancePrice + ", discount=" + discount + ", maling="
				+ maling + ", orderStatus=" + orderStatus + ", shopId="
				+ shopId + ", orderSceneType=" + orderSceneType + ", shopName="
				+ shopName + ", memberDiscount=" + memberDiscount
				+ ", couponId=" + couponId + ", couponNum=" + couponNum
				+ ", inputAddress=" + inputAddress + ", addOrEditFlag="
				+ addOrEditFlag + ", lastUpdateTime=" + lastUpdateTime
				+ ", refuseReason=" + refuseReason + ", consumerNum="
				+ consumerNum + ", seatIds=" + seatIds + ", goodsNumber="
				+ goodsNumber + ", orderSource=" + orderSource
				+ ", isActiveRefund=" + isActiveRefund + ", isMaling="
				+ isMaling + ", leastBookPrice=" + leastBookPrice
				+ ", isComment=" + isComment + ", deleteType=" + deleteType
				+ ", confirmMinute=" + confirmMinute + ", orderDiscount="
				+ orderDiscount + "]";
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsMember() {
		return isMember;
	}

	public void setIsMember(Integer isMember) {
		this.isMember = isMember;
	}

	public Long getClientLastTime() {
		return clientLastTime;
	}

	public void setClientLastTime(Long clientLastTime) {
		this.clientLastTime = clientLastTime;
	}

	public Long getServerLastTime() {
		return serverLastTime;
	}

	public void setServerLastTime(Long serverLastTime) {
		this.serverLastTime = serverLastTime;
	}

	public String getShopTelephone() {
		return shopTelephone;
	}

	public void setShopTelephone(String shopTelephone) {
		this.shopTelephone = shopTelephone;
	}

	public Long getBusinessAreaActivityId() {
		return businessAreaActivityId;
	}

	public void setBusinessAreaActivityId(Long businessAreaActivityId) {
		this.businessAreaActivityId = businessAreaActivityId;
	}

	public Double getSendRedPacketMoney() {
		return sendRedPacketMoney;
	}

	public void setSendRedPacketMoney(Double sendRedPacketMoney) {
		this.sendRedPacketMoney = sendRedPacketMoney;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public Integer getPeopleNumber() {
		return peopleNumber;
	}

	public void setPeopleNumber(Integer peopleNumber) {
		this.peopleNumber = peopleNumber;
	}

    public String getOrderCode()
    {
        return orderCode;
    }

    public void setOrderCode(String orderCode)
    {
        this.orderCode = orderCode;
    }

	public double getDeductAmount() {
		return deductAmount;
	}

	public void setDeductAmount(double deductAmount) {
		this.deductAmount = deductAmount;
	}

	public List<Integer> getUserShopCouponIdList() {
		return userShopCouponIdList;
	}

	public void setUserShopCouponIdList(List<Integer> userShopCouponIdList) {
		this.userShopCouponIdList = userShopCouponIdList;
	}

	public Integer getClientSystemType() {
		return clientSystemType;
	}

	public void setClientSystemType(Integer clientSystemType) {
		this.clientSystemType = clientSystemType;
	}

    public Date getClientFinishTime() {
        return clientFinishTime;
    }

    public void setClientFinishTime(Date clientFinishTime) {
        this.clientFinishTime = clientFinishTime;
    }

    public Integer getIsWait() {
        return isWait;
    }

    public void setIsWait(Integer isWait) {
        this.isWait = isWait;
    }

    public Double getPlatformTotalIncomePrice() {
        return platformTotalIncomePrice;
    }

    public void setPlatformTotalIncomePrice(Double platformTotalIncomePrice) {
        this.platformTotalIncomePrice = platformTotalIncomePrice;
    }

    public Double getOrderDiscountPrice() {
        return orderDiscountPrice;
    }

    public void setOrderDiscountPrice(Double orderDiscountPrice) {
        this.orderDiscountPrice = orderDiscountPrice;
    }
	
}