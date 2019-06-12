package com.idcq.appserver.dto.order;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 订单商品dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月12日
 * @time 上午10:53:53
 */
public class OrderGoodsDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 1461071130542978543L;

    @JsonIgnore
    private Long id;

    @JsonIgnore
    private String orderId;

    private String avsSpecs;
    public String getAvsSpecs() {
		return avsSpecs;
	}

	public void setAvsSpecs(String avsSpecs) {
		this.avsSpecs = avsSpecs;
	}

	/**
     * shopId
     */
    private Long shopId;

    private Long goodsId;

    private Double goodsNumber;

    private Integer goodsIndex;

    private Double goodsSettlePrice;

    private Double standardPrice;

    @JsonIgnore
    private String userRemark;

    private String shopName; // 商铺名称

    private String goodsName; // 商品名称

    private Double unitPrice; // 单价

    @JsonIgnore
    private Double promoPrice; // 折扣价

    private String goodsImg; // 商品图片url
    /**
     * 是否打折
     */
    private Integer goodsSettleFlag;

    private String unitName; // 商品项计量单位名称

    /**
     * 是否返点 0不返点，1返点
     */
    private Integer goodsRebateFlag;

    /*---------20150911----------*/
    @JsonIgnore
    private Double goodsRequiredPrice; // 商品项应付金额

    private Long billerId;

    private String specsDesc;
    
    /**
     * 订单商品折上折
     */
    private Double orderGoodsDiscount;
    /**
     * 会员单价
     */
    private Double memberPrice;
    
    /**
     * 改价后的商品单价
     */
    private Double settleUnitPrice;
    
    private String setGoodsGroup;
	public String getSetGoodsGroup() {
		return setGoodsGroup;
	}
	public void setSetGoodsGroup(String setGoodsGroup) {
		this.setGoodsGroup = setGoodsGroup;
	}
    /**
     * 是否退订（退菜）：0=否，1=是
     */
    private Integer isCancle;
    
    private Integer isOrderDiscount = 1;
    private Integer goodsType;

    private Integer isConsumeOuter;	//是否外带标志，0-否，1-是

    public Integer getIsConsumeOuter()
    {
        return isConsumeOuter;
    }

    public void setIsConsumeOuter(Integer isConsumeOuter)
    {
        this.isConsumeOuter = isConsumeOuter;
    }

    public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public Long getGoodsSetId() {
		return goodsSetId;
	}

	public void setGoodsSetId(Long goodsSetId) {
		this.goodsSetId = goodsSetId;
	}

	private Long goodsSetId;
    public OrderGoodsDto()
    {
        super();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getOrderId()
    {
        return orderId;
    }

    public void setOrderId(String orderId)
    {
        this.orderId = orderId;
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

    public Integer getGoodsIndex()
    {
        return goodsIndex;
    }

    public void setGoodsIndex(Integer goodsIndex)
    {
        this.goodsIndex = goodsIndex;
    }

    public Double getGoodsSettlePrice()
    {
        return goodsSettlePrice;
    }

    public void setGoodsSettlePrice(Double goodsSettlePrice)
    {
        this.goodsSettlePrice = goodsSettlePrice;
    }

    public String getUserRemark()
    {
        return userRemark;
    }

    public void setUserRemark(String userRemark)
    {
        this.userRemark = userRemark;
    }

    public Double getUnitPrice()
    {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice)
    {
        this.unitPrice = unitPrice;
    }

    public Double getPromoPrice()
    {
        return promoPrice;
    }

    public void setPromoPrice(Double promoPrice)
    {
        this.promoPrice = promoPrice;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public String getGoodsName()
    {
        return goodsName;
    }

    public void setGoodsName(String goodsName)
    {
        this.goodsName = goodsName;
    }

    public String getGoodsImg()
    {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg)
    {
        this.goodsImg = goodsImg;
    }

    public Integer getGoodsSettleFlag()
    {
        return goodsSettleFlag;
    }

    public void setGoodsSettleFlag(Integer goodsSettleFlag)
    {
        this.goodsSettleFlag = goodsSettleFlag;
    }

    public Double getStandardPrice()
    {
        return standardPrice;
    }

    public void setStandardPrice(Double standardPrice)
    {
        this.standardPrice = standardPrice;
    }

    public String getUnitName()
    {
        return unitName;
    }

    public void setUnitName(String unitName)
    {
        this.unitName = unitName;
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

    public Integer getGoodsRebateFlag()
    {
        return goodsRebateFlag;
    }

    public void setGoodsRebateFlag(Integer goodsRebateFlag)
    {
        this.goodsRebateFlag = goodsRebateFlag;
    }

    public String getSpecsDesc()
    {
        return specsDesc;
    }

    public void setSpecsDesc(String specsDesc)
    {
        this.specsDesc = specsDesc;
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

	public Double getMemberPrice()
    {
        return memberPrice;
    }

    public void setMemberPrice(Double memberPrice)
    {
        this.memberPrice = memberPrice;
    }

    public Double getSettleUnitPrice()
    {
        return settleUnitPrice;
    }

    public void setSettleUnitPrice(Double settleUnitPrice)
    {
        this.settleUnitPrice = settleUnitPrice;
    }

    public Integer getIsCancle() {
        return isCancle;
    }

    public void setIsCancle(Integer isCancle) {
        this.isCancle = isCancle;
    }

    public Integer getIsOrderDiscount() {
        return isOrderDiscount;
    }

    public void setIsOrderDiscount(Integer isOrderDiscount) {
        this.isOrderDiscount = isOrderDiscount;
    }

    @Override
	public String toString() {
		return "OrderGoodsDto [id=" + id + ", orderId=" + orderId + ", shopId="
				+ shopId + ", goodsId=" + goodsId + ", goodsNumber="
				+ goodsNumber + ", goodsIndex=" + goodsIndex
				+ ", goodsSettlePrice=" + goodsSettlePrice + ", standardPrice="
				+ standardPrice + ", userRemark=" + userRemark + ", shopName="
				+ shopName + ", goodsName=" + goodsName + ", unitPrice="
				+ unitPrice + ", promoPrice=" + promoPrice + ", goodsImg="
				+ goodsImg + ", goodsSettleFlag=" + goodsSettleFlag
				+ ", unitName=" + unitName + ", goodsRebateFlag="
				+ goodsRebateFlag + ", goodsRequiredPrice="
				+ goodsRequiredPrice + ", billerId=" + billerId
				+ ", specsDesc=" + specsDesc + ", orderGoodsDiscount="
				+ orderGoodsDiscount + "]";
	}
}