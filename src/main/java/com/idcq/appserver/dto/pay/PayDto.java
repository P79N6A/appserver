package com.idcq.appserver.dto.pay;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 订单支付记录dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:17:34
 */
public class PayDto implements Serializable
{

    /**
	 * 
	 */
    private static final long serialVersionUID = 2205395592081909944L;

    @JsonIgnore
    private Long orderPayId;

    private String orderId;

    private String groupId;
    public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	private Integer payType; // 订单类型

    private Long payId; // 支付的唯一标记，支付类型为0和1时填写transation_id,代金券支付时填写代金券ID，红包支付时填写红包ID

    private Double payAmount; // 支付金额

    private Integer orderPayType;// 订单支付类型：0(单个订单支付），1（多个订单支付

    private String orderPayTime;

    private String lastUpdateTime;

    /**
     * 收款人类型：1点传奇平台收款-0, 商铺收款-1
     */
    private Integer payeeType;

    /**
     * 商铺id
     */
    private Long shopId;

    private Integer payIndex;// 支付序号

    private String userPayTime;// 客户端支付时间
    
    private Long userId;
    
    /**
     * 支付渠道:支付宝=0,消费者APP传奇宝=1,微信=2,收银机短信=3,收银机传奇宝=4
     */
    private Integer payChannel;
    
    private Integer payStatus;
    
    private Integer autoSettleFlag;
    
    private Integer clientPayId;
    @JsonIgnore
    private Double oddChange;//当次找零 add by ljp 20151228
    //@JsonIgnore
    //private Integer payChannel;//支付通道 add by ljp 20151228
    @JsonIgnore
    private Double realCharges;//当次实收 add by ljp 20151228
    
    /*-----------追加，和表不相干------------*/

    public Integer getClientPayId() {
		return clientPayId;
	}

	public void setClientPayId(Integer clientPayId) {
		this.clientPayId = clientPayId;
	}

	private Long uccId;

    private Integer price;
    private String notifyCashierMobile;
    public String getNotifyCashierMobile() {
		return notifyCashierMobile;
	}

	public void setNotifyCashierMobile(String notifyCashierMobile) {
		this.notifyCashierMobile = notifyCashierMobile;
	}

	private Double needPayAmount;
    
    private Double realMoney;//实收金额
    private Double changeMoney;//找零金额
    
    private Integer clientSystem;
    public Integer getClientSystem() {
		return clientSystem;
	}

	public void setClientSystem(Integer clientSystem) {
		this.clientSystem = clientSystem;
	}

	public Long getOrderPayId()
    {
        return orderPayId;
    }

    public void setOrderPayId(Long orderPayId)
    {
        this.orderPayId = orderPayId;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }

    public Integer getPayType()
    {
        return payType;
    }

    public void setPayType(Integer payType)
    {
        this.payType = payType;
    }

    public Double getPayAmount()
    {
        return payAmount;
    }

    public void setPayAmount(Double payAmount)
    {
        this.payAmount = payAmount;
    }

    public Long getPayId()
    {
        return payId;
    }

    public void setPayId(Long payId)
    {
        this.payId = payId;
    }

    public Integer getOrderPayType()
    {
        return orderPayType;
    }

    public void setOrderPayType(Integer orderPayType)
    {
        this.orderPayType = orderPayType;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUccId()
    {
        return uccId;
    }

    public void setUccId(Long uccId)
    {
        this.uccId = uccId;
    }

    public Integer getPrice()
    {
        return price;
    }

    public void setPrice(Integer price)
    {
        this.price = price;
    }

    public Double getNeedPayAmount()
    {
        return needPayAmount;
    }

    public void setNeedPayAmount(Double needPayAmount)
    {
        this.needPayAmount = needPayAmount;
    }

    public String getOrderPayTime()
    {
        return orderPayTime;
    }

    public void setOrderPayTime(String orderPayTime)
    {
        this.orderPayTime = orderPayTime;
    }

    public String getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getPayeeType()
    {
        return payeeType;
    }

    public void setPayeeType(Integer payeeType)
    {
        this.payeeType = payeeType;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
    }

    public Integer getPayIndex()
    {
        return payIndex;
    }

    public void setPayIndex(Integer payIndex)
    {
        this.payIndex = payIndex;
    }

    public String getUserPayTime()
    {
        return userPayTime;
    }

    public void setUserPayTime(String userPayTime)
    {
        this.userPayTime = userPayTime;
    }

    public Integer getPayChannel()
    {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel)
    {
        this.payChannel = payChannel;
    }

    public Integer getPayStatus()
    {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus)
    {
        this.payStatus = payStatus;
    }

    public Integer getAutoSettleFlag()
    {
        return autoSettleFlag;
    }

    public void setAutoSettleFlag(Integer autoSettleFlag)
    {
        this.autoSettleFlag = autoSettleFlag;
    }

	public Double getOddChange() {
		return oddChange;
	}

	public void setOddChange(Double oddChange) {
		this.oddChange = oddChange;
	}

	public Double getRealCharges() {
		return realCharges;
	}

	public void setRealCharges(Double realCharges) {
		this.realCharges = realCharges;
	}

	public Double getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(Double realMoney) {
		this.realMoney = realMoney;
	}

	public Double getChangeMoney() {
		return changeMoney;
	}

	public void setChangeMoney(Double changeMoney) {
		this.changeMoney = changeMoney;
	}

}