package com.idcq.appserver.controller.goods;

import java.io.Serializable;
/**
 * 标品商品json转换dto
 * @author shengzhipeng
 * @date:2015年9月21日 下午2:47:14
 */
public class StandardGoodsDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2092710038112625571L;
	
	private Long shopId;
	
	private String token;
	
	private String barcode;
	
	private String goodsName;
	
	private String unitName;
	
	private Double price;
	
	private String specifications;
	
	private Long goodsId;
	
	private Long goodsCategoryId;
	
	private Integer digitScale;
	
	private Integer validityTerm;// 有效期
	
	private Integer goodsType;// 商品类型
	
    private Double storageTotalNumber;
    
    private Double alarmNumberMax;
    
    private Double alarmNumberMin;
    
    private Integer isNeedCheck;//是否支持盘点 1.支持 0.不支持
    
    private String goodsNo;
    
    private String goodsPriceSpec;

	private Integer sellMode;

	private Integer sellModeValue;

	public Integer getSellMode()
	{
		return sellMode;
	}

	public void setSellMode(Integer sellMode)
	{
		this.sellMode = sellMode;
	}

	public Integer getSellModeValue()
	{
		return sellModeValue;
	}

	public void setSellModeValue(Integer sellModeValue)
	{
		this.sellModeValue = sellModeValue;
	}

	public Integer getIsNeedCheck() {
		return isNeedCheck;
	}

	public void setIsNeedCheck(Integer isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public Integer getValidityTerm() {
		return validityTerm;
	}

	public void setValidityTerm(Integer validityTerm) {
		this.validityTerm = validityTerm;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecifications() {
		return specifications;
	}

	public void setSpecifications(String specifications) {
		this.specifications = specifications;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}

    public Integer getDigitScale()
    {
        return digitScale;
    }

    public void setDigitScale(Integer digitScale)
    {
        if(digitScale == null)
        {
            digitScale = 0;
        }
        this.digitScale = digitScale;
    }

    public Double getAlarmNumberMax() {
        return alarmNumberMax;
    }

    public void setAlarmNumberMax(Double alarmNumberMax) {
        this.alarmNumberMax = alarmNumberMax;
    }

    public Double getAlarmNumberMin() {
        return alarmNumberMin;
    }

    public String getGoodsNo() {
        return goodsNo;
    }

    public void setGoodsNo(String goodsNo) {
        this.goodsNo = goodsNo;
    }

    public void setAlarmNumberMin(Double alarmNumberMin) {
        this.alarmNumberMin = alarmNumberMin;
    }

    public Double getStorageTotalNumber() {
        return storageTotalNumber;
    }

    public void setStorageTotalNumber(Double storageTotalNumber) {
        this.storageTotalNumber = storageTotalNumber;
    }

    public String getGoodsPriceSpec() {
        return goodsPriceSpec;
    }

    public void setGoodsPriceSpec(String goodsPriceSpec) {
        this.goodsPriceSpec = goodsPriceSpec;
    }
	
}
