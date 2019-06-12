package com.idcq.appserver.dto.order;

import java.util.Date;
import java.util.List;

import com.idcq.appserver.dto.pay.PayDto;

public class OrderModelForApp
{   
    //订单标题
    private String orderTitle;
    //订单编号
    private String orderId;
    //店铺名称
    private String shopName;
    //支付状态  未支付-0,已支付-1,支付失败-2
    private Integer payStatus;
    // 订单结算价格
    private Double settlePrice;
    //订单结算时间
    private Date settleTime;
    //订单支付详情
    private List<OrderPayModelForApp> payList;
    
    
    public String getOrderTitle()
    {
        return orderTitle;
    }
    public void setOrderTitle(String orderTitle)
    {
        this.orderTitle = orderTitle;
    }
    public String getOrderId()
    {
        return orderId;
    }
    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
    }
    public String getShopName()
    {
        return shopName;
    }
    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }
    public Integer getPayStatus()
    {
        return payStatus;
    }
    public void setPayStatus(Integer payStatus)
    {
        this.payStatus = payStatus;
    }
    public Double getSettlePrice()
    {
        return settlePrice;
    }
    public void setSettlePrice(Double settlePrice)
    {
        this.settlePrice = settlePrice;
    }
    public Date getSettleTime()
    {
        return settleTime;
    }
    public void setSettleTime(Date settleTime)
    {
        this.settleTime = settleTime;
    }
    public List<OrderPayModelForApp> getPayList()
    {
        return payList;
    }
    public void setPayList(List<OrderPayModelForApp> payList)
    {
        this.payList = payList;
    }
    

}
