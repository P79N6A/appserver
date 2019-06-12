package com.idcq.appserver.dto.cashregister;

import java.io.Serializable;

import com.idcq.appserver.utils.ToStringMethod;

/**
 * 订单商品dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:53:53
 */
public class OrderInfoDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = -6556701010515361170L;

    /**
     * 菜编号
     */
    private Long dishId;

    private Double num;

    /**
     * 实际价格
     */
    private Double price;

    /**
     * 折扣价格
     */
    private Double promoPrice;

    /**
     * //菜品备注，多个备注通过英文逗号(“,”)分隔
     */
    private String dishRemark;

    private Long billerId;

    /**
     * 订单商品折上折
     */
    private Double orderGoodsDiscount;

    private OrderInfoDto()
    {
        super();
    }

    public String toString()
    {
        return ToStringMethod.toString(this);
    }

    public Long getDishId()
    {
        return dishId;
    }

    public void setDishId(Long dishId)
    {
        this.dishId = dishId;
    }

    public Double getNum()
    {
        return num;
    }

    public void setNum(Double num)
    {
        this.num = num;
    }

    public Double getPrice()
    {
        return price;
    }

    public void setPrice(Double price)
    {
        this.price = price;
    }

    public Double getPromoPrice()
    {
        return promoPrice;
    }

    public void setPromoPrice(Double promoPrice)
    {
        this.promoPrice = promoPrice;
    }

    public String getDishRemark()
    {
        return dishRemark;
    }

    public void setDishRemark(String dishRemark)
    {
        this.dishRemark = dishRemark;
    }

    public Long getBillerId()
    {
        return billerId;
    }

    public void setBillerId(Long billerId)
    {
        if (null != billerId && billerId == 0)
        {
            billerId = null;
        }
        this.billerId = billerId;
    }

    public Double getOrderGoodsDiscount()
    {
        return orderGoodsDiscount;
    }

    public void setOrderGoodsDiscount(Double orderGoodsDiscount)
    {
        if (null != orderGoodsDiscount && orderGoodsDiscount == 0)
        {
            orderGoodsDiscount = 1D;
        }
        this.orderGoodsDiscount = orderGoodsDiscount;
    }
}