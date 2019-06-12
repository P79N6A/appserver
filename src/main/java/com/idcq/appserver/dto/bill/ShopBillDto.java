package com.idcq.appserver.dto.bill;

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
public class ShopBillDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 4447550589523390537L;

    private Long billId;

    @JsonIgnore
    private Long shopId;

    /**
     * 销售商品=1,支付平台服务费=2,购买红包=3,提现=4,充值=5,推荐奖励=6，提现退回=7，冻结资金=8，解冻资金=9，转充=10
     */
    private String billType;

    private Double money;

    /**
     * 账单类型:1（账户资金增加）,-1（账户资金减少）
     */
    private Integer billDirection;

    /**
     * //账单状态:1（成功）,2（失败），3（进行中） 新版本都是成功状态
     */
    private Integer billStatus;

    /**
     * 账单时间
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;

    /**
     * 分账时间
     */
    @JsonIgnore
    private Date settleTime;

    private Long transactionId;

    private String orderId;

    /**
     * 订单结算价格
     */
    private Double settlePrice;

    /**
     * 平台总收入
     */
    private Double platformTotalIncomePrice;

    /**
     * 已收款项
     */
    private Double payAmount;

    private String billDesc;

    /**
     * 备注
     */
    @JsonIgnore
    private String comment;

    /**
     * 账户余额
     */
    private Double accountAmount;

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * 商品数量
     */
    private Double goodsNumber;

    /**
     * 商品参与结算的价格
     */
    private Double goodsSettlePrice;
    
    /**
     * 对应账户类型账户使用户的余额
     */
    private Double accountAfterAmount;
    
    /**
     * 账户类型：0=线上营业收入，1=平台奖励，2=冻结资金，3=保证金
     */
    private Integer accountType;
    
    /**
     * 消费者id
     */
    private Long consumerUserId;
    
    /**
     * 消费者手机号码
     */
    private String consumerMobile;
    
    private String billTitle;
    
    private Long redPacketId;
    
    public String getBillTitle() {
		return billTitle;
	}

	public void setBillTitle(String billTitle) {
		this.billTitle = billTitle;
	}

	public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public ShopBillDto()
    {
        super();
    }

    public Double getAccountAmount()
    {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount)
    {
        this.accountAmount = accountAmount;
    }

    public Long getBillId()
    {
        return billId;
    }

    public void setBillId(Long billId)
    {
        this.billId = billId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
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

    public Date getSettleTime()
    {
        return settleTime;
    }

    public void setSettleTime(Date settleTime)
    {
        this.settleTime = settleTime;
    }

    public Long getTransactionId()
    {
        return transactionId;
    }

    public void setTransactionId(Long transactionId)
    {
        this.transactionId = transactionId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
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

    public String getBillDesc()
    {
        return billDesc;
    }

    public void setBillDesc(String billDesc)
    {
        this.billDesc = billDesc;
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

    public Double getAccountAfterAmount()
    {
        return accountAfterAmount;
    }

    public void setAccountAfterAmount(Double accountAfterAmount)
    {
        this.accountAfterAmount = accountAfterAmount;
    }

    public Integer getAccountType()
    {
        return accountType;
    }

    public void setAccountType(Integer accountType)
    {
        this.accountType = accountType;
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

    public Long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(Long redPacketId) {
        this.redPacketId = redPacketId;
    }

}