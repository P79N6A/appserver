package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 组合支付订单信息
 * @author nie_jq
 * @date 2015-11-23
 *
 */
public class MultiPayOrderDto  implements Serializable{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = -4355569480623026218L;
    private Date createTime;//下单时间
	private Integer orderStatus;//订单状态
	private Integer orderSceneType;//订单场景分类 1=到店点菜订单 2=外卖订单 3=服务订单 4=商品订单
	private Integer payStatus;//支付状态：未支付-0,已支付-1
	private Double discount;//会员折扣
	private Double settlePrice;//订单结算价
	private Double deductAmount;
	private List<Integer> userShopCouponIdList;
	private Integer settleFlag = 0;//用户结算标识 已结算-1，未结算-0,反结账-2
	public Double getDeductAmount() {
		return deductAmount;
	}
	public void setDeductAmount(Double deductAmount) {
		this.deductAmount = deductAmount;
	}
	public List<Integer> getUserShopCouponIdList() {
		return userShopCouponIdList;
	}
	public void setUserShopCouponIdList(List<Integer> userShopCouponIdList) {
		this.userShopCouponIdList = userShopCouponIdList;
	}
	private Double outfee;//服务费
	private Long billerId;//下单员ID
	private String mobile;//会员手机号码
	private Long cashierId;//收银员ID
	private String payTime;//交易时间
	private String id;//订单ID
	private Boolean isWm;//是否外卖
	private String userRemark;//订单备注
	private Integer consumerNum;//消费人数
	private String seatIds;//座位ID,多个逗号分隔，虚拟桌传"-1"
	private Integer isMaling;//是否有抹零操作：0无抹零，1抹元，2抹角
	private Double additionalDiscount;//订单折上折字段
	private List<MultiPayOrderInfoDto> orderInfo;
	/**
	 * 收银机当前时间(精确到毫秒) 
	 */
	private Long clientLastTime;
	private Long businessAreaActivityId;
	private Double orderPrice;
	private String cashierUsername;
	/**
	 * 是否叫起，默认为0
	 */
	private Integer isWait;
	
	private Double orderDiscountPrice;	
	private Double shopMemberDiscount;
	private Integer consumeType; //消费类型：0-开桌；1-快餐。

	private String tokenId; //点餐牌号

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

	//订单商品折前价
	public Double getOrderPrice() {
		return orderPrice;
	}
	public void setOrderPrice(Double orderPrice) {
		this.orderPrice = orderPrice;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public Integer getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getSettlePrice() {
		return settlePrice;
	}
	public void setSettlePrice(Double settlePrice) {
		this.settlePrice = settlePrice;
	}
	public Double getOutfee() {
		return outfee;
	}
	public void setOutfee(Double outfee) {
		this.outfee = outfee;
	}
	public Long getBillerId() {
		return billerId;
	}
	public void setBillerId(Long billerId) {
		this.billerId = billerId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getCashierId() {
		return cashierId;
	}
	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getIsWm() {
		return isWm;
	}
	public void setIsWm(Boolean isWm) {
		this.isWm = isWm;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
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
	public Integer getIsMaling() {
		return isMaling;
	}
	public void setIsMaling(Integer isMaling) {
		this.isMaling = isMaling;
	}
	public Double getAdditionalDiscount() {
		return additionalDiscount;
	}
	public void setAdditionalDiscount(Double additionalDiscount) {
		this.additionalDiscount = additionalDiscount;
	}
	
	public List<MultiPayOrderInfoDto> getOrderInfo() {
		return orderInfo;
	}
	public void setOrderInfo(List<MultiPayOrderInfoDto> orderInfo) {
		this.orderInfo = orderInfo;
	}
	public Long getClientLastTime()
    {
        return clientLastTime;
    }
    public void setClientLastTime(Long clientLastTime)
    {
        this.clientLastTime = clientLastTime;
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
    public Double getShopMemberDiscount() {
		return shopMemberDiscount;
	}
	public void setShopMemberDiscount(Double shopMemberDiscount) {
		this.shopMemberDiscount = shopMemberDiscount;
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
    public Double getOrderDiscountPrice() {
        return orderDiscountPrice;
    }
    public void setOrderDiscountPrice(Double orderDiscountPrice) {
        this.orderDiscountPrice = orderDiscountPrice;
    }
    @Override
	public String toString() {
		return "MultiPayOrderDto [createTime=" + createTime + ", orderStatus="
				+ orderStatus + ", orderSceneType=" + orderSceneType
				+ ", payStatus=" + payStatus + ", discount=" + discount
				+ ", settlePrice=" + settlePrice + ", outfee=" + outfee
				+ ", billerId=" + billerId + ", mobile=" + mobile
				+ ", cashierId=" + cashierId + ", payTime=" + payTime + ", id="
				+ id + ", isWm=" + isWm + ", userRemark=" + userRemark
				+ ", consumerNum=" + consumerNum + ", seatIds=" + seatIds
				+ ", isMaling=" + isMaling + ", additionalDiscount="
				+ additionalDiscount + ", orderInfos="
				+ orderInfo + "]";
	}
	
}
