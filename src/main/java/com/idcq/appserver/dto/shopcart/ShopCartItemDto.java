package com.idcq.appserver.dto.shopcart;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 用户购物车商品项dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月13日
 * @time 下午1:40:51
 */
public class ShopCartItemDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7163658861621226250L;
	@JsonIgnore
	private Integer itemId;
	@JsonIgnore
    private Long userId;
    private Long goodsId;
    private Integer goodsNumber;
    private Integer goodsIndex;
    private Long shopId;				//商铺ID
    private String shopName;
    
    /*------------商品信息-------------*/
    @JsonIgnore
    private Integer goodsServerMode;	//服务模式
    private Integer goodsType;			//类型
    @JsonIgnore
    private Integer sequence;			//排序
    private String goodsName;			//名称
    @JsonIgnore
    private String goodsDesc;			 
    private Float standardPrice;		//目录价格
    private Float discountPrice;		//折扣价格
    private Float vipPrice;				//会员价格
    private Float finalPrice;			//折上折价格
    private String unit;				//单位
    @JsonIgnore
    private Integer lowestUnit;			//最低起订数量
    private String goodsLogo1;				//大图
    private String goodsLogo2;			//小图
    @JsonIgnore
    private Integer goodsStatus;		//商品状态
    @JsonIgnore
    private String taste;				//口味
    @JsonIgnore
    private String spiciness;			//辣度
    @JsonIgnore
    private String label;				//标签
    @JsonIgnore
    private Integer takeout;			//是否外卖
    private Integer zanNumber;			//点赞次数
    private Integer soldNumber;			//销售次数
    private Integer shopColumnId;       //店铺一级分类ID
    
  
    
	public ShopCartItemDto() {
		super();
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(Integer goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public Integer getGoodsIndex() {
		return goodsIndex;
	}

	public void setGoodsIndex(Integer goodsIndex) {
		this.goodsIndex = goodsIndex;
	}

	public Integer getGoodsServerMode() {
		return goodsServerMode;
	}

	public void setGoodsServerMode(Integer goodsServerMode) {
		this.goodsServerMode = goodsServerMode;
	}

	public Integer getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public Float getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(Float standardPrice) {
		this.standardPrice = standardPrice;
	}

	public Float getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Float discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Float getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(Float vipPrice) {
		this.vipPrice = vipPrice;
	}

	public Float getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Float finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getLowestUnit() {
		return lowestUnit;
	}

	public void setLowestUnit(Integer lowestUnit) {
		this.lowestUnit = lowestUnit;
	}
	public String getGoodsLogo1() {
		return goodsLogo1;
	}

	public void setGoodsLogo1(String goodsLogo1) {
		this.goodsLogo1 = goodsLogo1;
	}

	public String getGoodsLogo2() {
		return goodsLogo2;
	}

	public void setGoodsLogo2(String goodsLogo2) {
		this.goodsLogo2 = goodsLogo2;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getTaste() {
		return taste;
	}

	public void setTaste(String taste) {
		this.taste = taste;
	}

	public String getSpiciness() {
		return spiciness;
	}

	public void setSpiciness(String spiciness) {
		this.spiciness = spiciness;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getTakeout() {
		return takeout;
	}

	public void setTakeout(Integer takeout) {
		this.takeout = takeout;
	}

	public Integer getZanNumber() {
		return zanNumber;
	}

	public void setZanNumber(Integer zanNumber) {
		this.zanNumber = zanNumber;
	}

	public Integer getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(Integer soldNumber) {
		this.soldNumber = soldNumber;
	}

	public Integer getShopColumnId() {
		return shopColumnId;
	}

	public void setShopColumnId(Integer shopColumnId) {
		this.shopColumnId = shopColumnId;
	}

	
    
    
}