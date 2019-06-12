package com.idcq.appserver.dto.user;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

/**
 * 用户账单dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 上午10:49:20
 */
public class UserBillDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 4447550589523390537L;

    private Long billId;

    @JsonIgnore
    private Long userId;

    private String userRole;

    private String billType; // 账单类型:充值,体现,购物,红包,推荐奖励

    private Double money;

    private Double accountAmount;// 账户余额

    private Integer billDirection;// 账单类型:1（账户资金增加）,-1（账户资金减少）

    private Integer billStatus; // 账单状态:1（成功）,2（失败），3（进行中）

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime; // 账单时间

    @JsonIgnore
    private Date settleTime;// 分账时间

    private String billTitle;

    private String billLogoUrl;

    private String billMonth;

    private String billDesc;

    private Long consumerUserId;

    private String consumerMobile;

    private String orderId;

    private Long goodsId;

    private Double goodsNumber;

    private Double goodsSettlePrice;

    private Integer orderPayType;

    private Integer billStatusFlag;// 账单状态的进行中标记：1（进行中），0（已完成

    private String billStatusDesc;

    private Double settlePrice;// 订单结算价格

    private Double platformTotalIncomePrice;// 平台总收入

    private Double payAmount;// 已收款项

    // 支付宝支付订单的时候需要账单需要保存交易号 2015.9.30修改
    private Long transactionId;

    private Integer userBillType;
    
    private Integer accountType;
    
    private Double accountAfterAmount;
    
    private Integer isShow;//是否可见
    
    /**
     * 代理商ID
     */
    private Long agentId;
    
    private Long billLogo;
    
    private Long redPacketId;

    public Long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(Long transactionId)
    {
        this.transactionId = transactionId;
    }

    public Integer getIsShow()
    {
        return isShow;
    }

    public void setIsShow(Integer isShow)
    {
        this.isShow = isShow;
    }

    public UserBillDto()
    {
        super();
    }

    public Date getSettleTime()
    {
        return settleTime;
    }

    public void setSettleTime(Date settleTime)
    {
        this.settleTime = settleTime;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getBillId()
    {
        return billId;
    }

    public void setBillId(Long billId)
    {
        this.billId = billId;
    }

    public String getBillType()
    {
        return billType;
    }

    public void setBillType(String billType)
    {
        this.billType = billType;
    }

    public Double getMoney()
    {
        return money;
    }

    public void setMoney(Double money)
    {
        this.money = money;
    }

    public Integer getBillDirection()
    {
        return billDirection;
    }

    public void setBillDirection(Integer billDirection)
    {
        this.billDirection = billDirection;
    }

    public Integer getBillStatus()
    {
        return billStatus;
    }

    public void setBillStatus(Integer billStatus)
    {
        this.billStatus = billStatus;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

    public String getBillTitle()
    {
        return billTitle;
    }

    public void setBillTitle(String billTitle)
    {
        this.billTitle = billTitle;
    }

    public String getBillLogoUrl()
    {
        return billLogoUrl;
    }

    public void setBillLogoUrl(String billLogoUrl)
    {
        this.billLogoUrl = billLogoUrl;
    }

    public String getBillDesc()
    {
        return billDesc;
    }

    public void setBillDesc(String billDesc)
    {
        this.billDesc = billDesc;
    }

    public String getUserRole()
    {
        return userRole;
    }

    public void setUserRole(String userRole)
    {
        this.userRole = userRole;
    }

    public Long getConsumerUserId()
    {
        return consumerUserId;
    }

    public void setConsumerUserId(Long consumerUserId)
    {
        this.consumerUserId = consumerUserId;
    }

    public String getConsumerMobile()
    {
        return consumerMobile;
    }

    public void setConsumerMobile(String consumerMobile)
    {
        this.consumerMobile = consumerMobile;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public Long getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(Long goodsId)
    {
        this.goodsId = goodsId;
    }

    public Double getGoodsNumber()
    {
        return goodsNumber;
    }

    public void setGoodsNumber(Double goodsNumber)
    {
        this.goodsNumber = goodsNumber;
    }

    public Double getGoodsSettlePrice()
    {
        return goodsSettlePrice;
    }

    public void setGoodsSettlePrice(Double goodsSettlePrice)
    {
        this.goodsSettlePrice = goodsSettlePrice;
    }

    public Integer getOrderPayType()
    {
        return orderPayType;
    }

    public void setOrderPayType(Integer orderPayType)
    {
        this.orderPayType = orderPayType;
    }

    public String getBillMonth()
    {
        return billMonth;
    }

    public void setBillMonth(String billMonth)
    {
        this.billMonth = billMonth;
    }

    public Integer getBillStatusFlag()
    {
        return billStatusFlag;
    }

    public void setBillStatusFlag(Integer billStatusFlag)
    {
        this.billStatusFlag = billStatusFlag;
    }

    public String getBillStatusDesc()
    {
        return billStatusDesc;
    }

    public void setBillStatusDesc(String billStatusDesc)
    {
        this.billStatusDesc = billStatusDesc;
    }

    public Double getSettlePrice()
    {
        return settlePrice;
    }

    public void setSettlePrice(Double settlePrice)
    {
        this.settlePrice = settlePrice;
    }

    public Double getPlatformTotalIncomePrice()
    {
        return platformTotalIncomePrice;
    }

    public void setPlatformTotalIncomePrice(Double platformTotalIncomePrice)
    {
        this.platformTotalIncomePrice = platformTotalIncomePrice;
    }

    public Double getPayAmount()
    {
        return payAmount;
    }

    public void setPayAmount(Double payAmount)
    {
        this.payAmount = payAmount;
    }

    public Double getAccountAmount()
    {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount)
    {
        this.accountAmount = accountAmount;
    }

    public Integer getUserBillType()
    {
        return userBillType;
    }

    public void setUserBillType(Integer userBillType)
    {
        this.userBillType = userBillType;
    }

    public Integer getAccountType()
    {
        return accountType;
    }

    public void setAccountType(Integer accountType)
    {
        this.accountType = accountType;
    }

    public Double getAccountAfterAmount()
    {
        return accountAfterAmount;
    }

    public void setAccountAfterAmount(Double accountAfterAmount)
    {
        this.accountAfterAmount = accountAfterAmount;
    }

    public Long getAgentId()
    {
        return agentId;
    }

    public void setAgentId(Long agentId)
    {
        this.agentId = agentId;
    }

    public Long getBillLogo()
    {
        return billLogo;
    }

    public void setBillLogo(Long billLogo)
    {
        this.billLogo = billLogo;
    }

    public Long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(Long redPacketId) {
        this.redPacketId = redPacketId;
    }

}