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
public class PlatformBillDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    private Integer billId;

    /**
     * 账单类型:充值,体现,购物,红包,推荐奖励
     */
    private String billType; 

    private Double money;

    /**
     * 账单类型:1（账户资金增加）,-1（账户资金减少）
     */
    private Integer billDirection;

    /**
     * 账单状态:1（成功）,2（失败），3（进行中）
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

    private Long goodsId;

    private Double goodsNumber;

    private Double goodsSettlePrice;

    private String billDesc;

    private Long consumerUserId;

    private String consumerMobile;
    
    /**
     * 平台账单类型:1=消费支付，2=商铺线上营业收入，3=销售提成,4=支付会员奖励，5=支付推荐会员奖励,6=支付推荐商铺奖励,
     * 7=支付服务店铺费,8=支付一级代理费，9=支付二级代理费，10=支付三级代理费，11=购买红包
     */
    private Integer platformBillType;

    /**
     * 资金来源：支付宝支付-0,传奇宝支付-1,消费卡支付-2,微信支付-3,建行银行卡=4,建行信用卡=5
     */
    private Integer moneySource;
    
    private Long redPacketId;
    /**
     * 商铺id
     */
    private Long shopId;
    
    public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public PlatformBillDto()
    {
        super();
    }

    public Integer getBillId()
    {
        return billId;
    }

    public void setBillId(Integer billId)
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

    public String getBillDesc()
    {
        return billDesc;
    }

    public void setBillDesc(String billDesc)
    {
        this.billDesc = billDesc;
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

    public Integer getPlatformBillType()
    {
        return platformBillType;
    }

    public void setPlatformBillType(Integer platformBillType)
    {
        this.platformBillType = platformBillType;
    }

    public Integer getMoneySource()
    {
        return moneySource;
    }

    public void setMoneySource(Integer moneySource)
    {
        this.moneySource = moneySource;
    }

    public Long getRedPacketId() {
        return redPacketId;
    }

    public void setRedPacketId(Long redPacketId) {
        this.redPacketId = redPacketId;
    }

}