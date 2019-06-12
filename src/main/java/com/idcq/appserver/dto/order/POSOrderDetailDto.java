package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.dto.goods.POSOrderDetailGoodsDto;
import com.idcq.appserver.dto.pay.POSOrderDetailPayDto;
import com.idcq.appserver.utils.CustomDateSerializer;

/**
 * 收银机订单详情（含支付列表和商品列表）
 * @author Administrator
 * 
 * @date 2015年12月28日
 * @time 下午2:49:15
 */
public class POSOrderDetailDto implements Serializable {

	private static final long serialVersionUID = 8519229329593772208L;
	
	

	// 订单Id
	private String orderId;
	
	private long shopId;
	
	private int orderChannelType;
	
	// 用户ID
	private Long userId;

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
	
	// 支付时间类型
	private Integer payTimeType;

	// 配送地址ID
	private Long addressId;

	// 订单总价
	private Double orderTotalPrice;

	//订单标题
	private String orderTitle;
	
	//订单场景分类：0：预定单 1：到店点菜订单 2：外卖订单 3：服务订单 4：商品订单 
	private Integer orderSceneType;
	
	//订单备注 
	private String userRemark;
	
	//服务费 
	private Double logisticsPrice;

	//订单商品数量，此字段对应订单表中的order_goods_number
	private Integer goodsNumber;
	
	private Double settlePrice;
	
	private String mobile;	//会员手机号码
	
	private Long clientLastTime;
	
	private Long serverLastTime;//
	
	private int isMember;	//是否会员订单：是=1，否=0
	
	@JsonSerialize(using = CustomDateSerializer.class) 
	private Date lastUpdateTime;//最后修改时间
	
	private double memberDiscount;//会员折扣
	
	private double orderDiscount;//整单折扣
	
	/**
	 * 地址
	 */
	private String address;
	
	private Long cashierId;//收银员ID
	
	private List<POSOrderDetailGoodsDto> goods;
	
	private List<POSOrderDetailPayDto> paies;
	
	private String seatIds;//座位id
	private Integer consumerNum;//消费人数
	private Long billerId;//服务员id
	
	private Integer clientSystemType;
	private String cashierUsername;
	private Integer isWait;
	
	private String refuseReason;

	private String tokenId;

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

	public Integer getConsumerNum() {
		return consumerNum;
	}

	public void setConsumerNum(Integer consumerNum) {
		this.consumerNum = consumerNum;
	}

	public Long getBillerId() {
		return billerId;
	}

	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}

	public POSOrderDetailDto() {
		super();
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getOrderTitle() {
		return orderTitle;
	}

	public void setOrderTitle(String orderTitle) {
		this.orderTitle = orderTitle;
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

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Double getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public int getIsMember() {
		return isMember;
	}

	public void setIsMember(int isMember) {
		this.isMember = isMember;
	}

	public List<POSOrderDetailGoodsDto> getGoods() {
		return goods;
	}

	public void setGoods(List<POSOrderDetailGoodsDto> goods) {
		this.goods = goods;
	}

	public List<POSOrderDetailPayDto> getPaies() {
		return paies;
	}

	public void setPaies(List<POSOrderDetailPayDto> paies) {
		this.paies = paies;
	}

	public int getOrderChannelType() {
		return orderChannelType;
	}

	public void setOrderChannelType(int orderChannelType) {
		this.orderChannelType = orderChannelType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public double getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public double getOrderDiscount() {
		return orderDiscount;
	}

	public void setOrderDiscount(double orderDiscount) {
		this.orderDiscount = orderDiscount;
	}

	public Long getCashierId() {
		return cashierId;
	}

	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}

    public String getSeatIds() {
        return seatIds;
    }

    public void setSeatIds(String seatIds) {
        this.seatIds = seatIds;
    }

    public Integer getClientSystemType() {
        return clientSystemType;
    }

    public void setClientSystemType(Integer clientSystemType) {
        this.clientSystemType = clientSystemType;
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

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

}