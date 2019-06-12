package com.idcq.appserver.dto.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.utils.ToStringMethod;

/**
 * POS订单商品项dto
 * 
 * @author Administrator
 * 
 * @date 2015年4月13日
 * @time 上午11:25:15
 */
public class POSOrderGoodsDto implements Serializable
{
    /**
	 * 
	 */
    private static final long serialVersionUID = 4512646390852853460L;

    /**
     * 菜编号
     */
    private Long dishId;

    /**
     * 数量
     */
    private Double num;

    /**
     * 实际价格
     */
    private Double price;

    /**
     * 菜品备注
     */
    private String dishRemark;

    /**
     * 下单员工
     */
    private Long billerId;
    
    /**
     * 订单商品折上折
     */
    private Double orderGoodsDiscount;
    
    /**
     * 次卡id
     */
    private Integer cardId;
    
    /**
     * 商品名称
     */
    private String goodsName;
    
    private String specsDesc;
    
    private Double standardPrice;//目录价
    /**
     * 是否退订（退菜）：0=否，1=是
     */
    private Integer isCancle;
    
    /*
     * 订单的技师列表[新增订单商品关联技师列表]
     * */
    private List<Map<String, Object>> techs;
    
    
    public List<Map<String, Object>> getTechs() {
		return techs;
	}

	public void setTechs(List<Map<String, Object>> techs) {
		this.techs = techs;
	}

	public POSOrderGoodsDto()
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

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

    public String getSpecsDesc() {
        return specsDesc;
    }

    public void setSpecsDesc(String specsDesc) {
        this.specsDesc = specsDesc;
    }

    public Double getStandardPrice() {
        return standardPrice;
    }

    public void setStandardPrice(Double standardPrice) {
        this.standardPrice = standardPrice;
    }

    public Integer getIsCancle() {
        return isCancle;
    }

    public void setIsCancle(Integer isCancle) {
        this.isCancle = isCancle;
    }

}
