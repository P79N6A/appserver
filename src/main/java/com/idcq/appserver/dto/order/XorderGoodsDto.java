package com.idcq.appserver.dto.order;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 非会员订单的商品dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月30日
 * @time 下午5:55:45
 */
public class XorderGoodsDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 4802771525441421565L;

    private Long xorderGoodsId;

    private String xorderId;

    private Long shopId;

    private Long goodsId;

    private Double goodsNumber;

    private Double goodsSettlePrice;

    private Integer goodsIndex;

    private String userRemark;

    private Double standardPrice;

    /**
     * 商品项应付金额
     */
    @JsonIgnore
    private Double goodsRequiredPrice;

    @JsonIgnore
    private Long billerId;

    @JsonIgnore
    private String specsDesc;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 订单商品折上折
     */
    private Double orderGoodsDiscount;

    public XorderGoodsDto()
    {
        super();
    }

    public Long getXorderGoodsId()
    {
        return xorderGoodsId;
    }

    public void setXorderGoodsId(Long xorderGoodsId)
    {
        this.xorderGoodsId = xorderGoodsId;
    }

    public String getXorderId()
    {
        return xorderId;
    }

    public void setXorderId(String xorderId)
    {
        this.xorderId = xorderId;
    }

    public Long getShopId()
    {
        return shopId;
    }

    public void setShopId(Long shopId)
    {
        this.shopId = shopId;
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

    public Integer getGoodsIndex()
    {
        return goodsIndex;
    }

    public void setGoodsIndex(Integer goodsIndex)
    {
        this.goodsIndex = goodsIndex;
    }

    public String getUserRemark()
    {
        return userRemark;
    }

    public void setUserRemark(String userRemark)
    {
        this.userRemark = userRemark;
    }

    public Double getStandardPrice()
    {
        return standardPrice;
    }

    public void setStandardPrice(Double standardPrice)
    {
        this.standardPrice = standardPrice;
    }

    public Double getGoodsRequiredPrice()
    {
        return goodsRequiredPrice;
    }

    public void setGoodsRequiredPrice(Double goodsRequiredPrice)
    {
        this.goodsRequiredPrice = goodsRequiredPrice;
    }

    public Long getBillerId()
    {
        return billerId;
    }

    public void setBillerId(Long billerId)
    {
        this.billerId = billerId;
    }

    public String getSpecsDesc()
    {
        return specsDesc;
    }

    public void setSpecsDesc(String specsDesc)
    {
        this.specsDesc = specsDesc;
    }

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
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

	@Override
	public String toString() {
		return "XorderGoodsDto [xorderGoodsId=" + xorderGoodsId + ", xorderId="
				+ xorderId + ", shopId=" + shopId + ", goodsId=" + goodsId
				+ ", goodsNumber=" + goodsNumber + ", goodsSettlePrice="
				+ goodsSettlePrice + ", goodsIndex=" + goodsIndex
				+ ", userRemark=" + userRemark + ", standardPrice="
				+ standardPrice + ", goodsRequiredPrice=" + goodsRequiredPrice
				+ ", billerId=" + billerId + ", specsDesc=" + specsDesc
				+ ", goodsName=" + goodsName + ", orderGoodsDiscount="
				+ orderGoodsDiscount + "]";
	}
    
    

}