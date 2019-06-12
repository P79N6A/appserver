package com.idcq.appserver.dto.order;

public class OrderPayModelForApp
{
    private Double payAmount;
    private Integer payType;
    private String orderPayTime;
//    private Integer payStatus;
    public Double getPayAmount()
    {
        return payAmount;
    }
    public void setPayAmount(Double payAmount)
    {
        this.payAmount = payAmount;
    }
    public Integer getPayType()
    {
        return payType;
    }
    public void setPayType(Integer payType)
    {
        this.payType = payType;
    }
    public String getOrderPayTime()
    {
        return orderPayTime;
    }
    public void setOrderPayTime(String orderPayTime)
    {
        this.orderPayTime = orderPayTime;
    }
//    public Integer getPayStatus()
//    {
//        return payStatus;
//    }
//    public void setPayStatus(Integer payStatus)
//    {
//        this.payStatus = payStatus;
//    }
    
}
