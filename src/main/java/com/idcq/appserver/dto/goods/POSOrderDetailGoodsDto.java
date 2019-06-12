package com.idcq.appserver.dto.goods;

import java.io.Serializable;

/**
 * 收银订单详情商品列表
 * @author Administrator
 * 
 * @date 2015年12月28日
 * @time 下午3:23:06
 */
public class POSOrderDetailGoodsDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1442890644220194380L;

	private long goodsId;	
	private String goodsName;	//商品名称
	private Double goodsNumber;	//商品数量
	private Double standardPrice;	//商品目录价
	private Double settleUnitPrice;	//改价后商品单价
	private Double goodsRequiredPrice;	//商品本应支付的金额
	private int goodsIndex;	//商品排序
	private Integer goodsSettleFlag;//商品结算吧标志
	private Double goodsSettlePrice;	//商品参与结算价格
	private String unitName;	//单位名称
	private Double orderGoodsDiscount;	//收银机对订单商品进行的折上折的折扣
    private String avsSpecs;    //订单商品增值服务
    private Integer isOrderDiscount = 1;
    private String setGoodsGroup;
    public String getAvsSpecs() {
		return avsSpecs;
	}

	public void setAvsSpecs(String avsSpecs) {
		this.avsSpecs = avsSpecs;
	}
	private String specsDesc;
	private Integer isCancle;
    private Integer goodsType;
    private Integer isConsumeOuter;

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
	public long getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Double getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(Double goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public Double getStandardPrice() {
		return standardPrice;
	}
	public Integer getIsOrderDiscount() {
		return isOrderDiscount;
	}

	public void setIsOrderDiscount(Integer isOrderDiscount) {
		this.isOrderDiscount = isOrderDiscount;
	}

	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}
	public Double getSettleUnitPrice() {
		return settleUnitPrice;
	}
	public void setSettleUnitPrice(Double settleUnitPrice) {
		this.settleUnitPrice = settleUnitPrice;
	}
	public Double getGoodsRequiredPrice() {
		return goodsRequiredPrice;
	}
	public void setGoodsRequiredPrice(Double goodsRequiredPrice) {
		this.goodsRequiredPrice = goodsRequiredPrice;
	}
	public int getGoodsIndex() {
		return goodsIndex;
	}
	public void setGoodsIndex(int goodsIndex) {
		this.goodsIndex = goodsIndex;
	}
	public Integer getGoodsSettleFlag() {
		return goodsSettleFlag;
	}
	public void setGoodsSettleFlag(Integer goodsSettleFlag) {
		this.goodsSettleFlag = goodsSettleFlag;
	}
	public Double getGoodsSettlePrice() {
		return goodsSettlePrice;
	}
	public void setGoodsSettlePrice(Double goodsSettlePrice) {
		this.goodsSettlePrice = goodsSettlePrice;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Double getOrderGoodsDiscount() {
		return orderGoodsDiscount;
	}
	public void setOrderGoodsDiscount(Double orderGoodsDiscount) {
		this.orderGoodsDiscount = orderGoodsDiscount;
	}
    public String getSpecsDesc() {
        return specsDesc;
    }
    public void setSpecsDesc(String specsDesc) {
        this.specsDesc = specsDesc;
    }
    public Integer getIsCancle() {
        return isCancle;
    }
    public void setIsCancle(Integer isCancle) {
        this.isCancle = isCancle;
    }

	public String getSetGoodsGroup() {
		return setGoodsGroup;
	}

	public void setSetGoodsGroup(String setGoodsGroup) {
		this.setGoodsGroup = setGoodsGroup;
	}
	
	
	
	
}