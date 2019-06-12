package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

public class ShopGoodsDto implements Serializable {

	private static final long serialVersionUID = -1097627460390530673L;
	
//	/*-- 新增的库存总量  20160719增加库存总量 --*/
//	private double storage_total_number;
//	/*-- 新增的最大库存  20160719增加最大库存 --*/
//	private double alarmNumberMax;
//	/*-- 新增的最小库存  20160719增加最小库存 --*/
//	private double alarmNumberMin;
	
	private Long goodsId;
	private String goodsName;
	private int goodsType;
	private int index;
	private String goodsNo;
	private String goodsLogo1;
	private String goodsLogo2;
	// private String goodsUrl;
	private Long soldNumber;
	private Long zanNumber;
	private double standardPrice;
	private double discountPrice;
	private double vipPrice;
	private double finalPrice;
	private String unit;
	/*---added--*/
	private Long goodsCategoryId;// 商品分类
	private String goodsCategoryName;//
	private String goodsDesc;
	private String goodsDetailDesc;
	private String taste;
	private String spiciness;
	private int isExpert;
	private String expertIds;
	private String pinyinCode;// 商品首字母简写
	
	/*-- 新增的成本价格    20160704增加成本价格 --*/
	private double costPrice;
    private String goodsProValuesIds;//商品族属性id列  
    private String goodsProValuesNames;//商品属性名称

	/**
	 * 是否参与打折
	 */
	private Integer goodsSettleFlag;
	@JsonIgnore
	private String goodsUrl;

	private Integer goodsServerMode;

	/**
	 * 是否支持外卖
	 */
	private Integer takeout;

	@JsonIgnore
	private String attachementIds;

	/**
	 * 商品族logo
	 */
	@JsonIgnore
	private String goodsGroupLogoUrls;

	/**
	 * 商品族分类
	 */
	@JsonIgnore
	private String goodsGroupCategoryIds;

	/**
	 * 商品族分类名称
	 */
	@JsonIgnore
	private String goodsGroupCategoryNames;

	/**
	 * 技师
	 */
	@JsonIgnore
	private String techIds;

	@JsonIgnore
	private String techNames;
	/**
	 * 商品族目录最低价
	 */
	private Double minPrice;

	/**
	 * 商品族目录最高价
	 */
	private Double maxPrice;

	// 使用时间
	private Double useTime;
	// 保持天数
	private Integer keepTime;

	@JsonIgnore
	private Long shopId;

	@JsonIgnore
	private List<Long> goodsCategoryList;

	@JsonIgnore
	private Integer orderBy;

	@JsonIgnore
	private String goodsServerModeParam;

//	@JsonIgnore
	private String goodsStatus;

	private Integer goodsGroupId;

	@JsonIgnore
	private Double memberDiscount;

	@JsonIgnore
	private Long goodsLogoId;

	@JsonIgnore
	private Integer isSupportMarketPrices;

	private String specsDesc;
	/**
	 * 告警类型：1 = 最大值告警，2=最小值告警,3-告警
	 */
	@JsonIgnore
	private String storageAlarmType;
	private Double storageTotalNumber;
	private Double alarmNumberMax;
	private Double alarmNumberMin;

	private String unitName;// 为了兼容一点管家
	private Integer digitScale;//单位精确度
	private Integer unitId;
	
	@JsonIgnore
	private Integer isNeedCheck;
	
	
	private String goodsPriceSpec;
	
	/**
	 * 提交条码的返回 
	 */
//	@JsonIgnore
	private String barcode;
	
	/**
	 * 低于库存数
	 */
	@JsonIgnore
	private Double storageNum;
	/**
	 * 排序方式（ 倒序-0； 顺序-1）
	 */
	@JsonIgnore
	private Integer orderByMode;
	/**
	 * 查询条件
	 */
	@JsonIgnore
	private String queryData;
	
	/**
	 * 搜索范围:1、商品;2、商品族;3、商品和商品族
	 */
	@JsonIgnore
	private Integer searchRange;
	
	private Integer isOrderDiscount;
	
	private List<GoodsSetDto> goodsSetList;
	
	public ShopGoodsDto() {
		super();
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(int goodsType) {
		this.goodsType = goodsType;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/*
	 * public String getGoodsUrl() { return goodsUrl; } public void
	 * setGoodsUrl(String goodsUrl) { this.goodsUrl = goodsUrl; }
	 */
	public Long getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(Long soldNumber) {
		this.soldNumber = soldNumber;
	}

	public Long getZanNumber() {
		return zanNumber;
	}

	public void setZanNumber(Long zanNumber) {
		this.zanNumber = zanNumber;
	}

	public double getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public double getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(double vipPrice) {
		this.vipPrice = vipPrice;
	}

	public double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
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

	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}

	public String getGoodsCategoryName() {
		return goodsCategoryName;
	}

	public void setGoodsCategoryName(String goodsCategoryName) {
		this.goodsCategoryName = goodsCategoryName;
	}

	public String getGoodsDesc() {
		return goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsDetailDesc() {
		return goodsDetailDesc;
	}

	public List<GoodsSetDto> getGoodsSetList() {
		return goodsSetList;
	}

	public void setGoodsSetList(List<GoodsSetDto> goodsSetList) {
		this.goodsSetList = goodsSetList;
	}

	public void setGoodsDetailDesc(String goodsDetailDesc) {
		this.goodsDetailDesc = goodsDetailDesc;
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

	public int getIsExpert() {
		return isExpert;
	}

	public void setIsExpert(int isExpert) {
		this.isExpert = isExpert;
	}

	public String getExpertIds() {
		return expertIds;
	}

	public void setExpertIds(String expertIds) {
		this.expertIds = expertIds;
	}

	public Integer getGoodsSettleFlag() {
		return goodsSettleFlag;
	}

	public void setGoodsSettleFlag(Integer goodsSettleFlag) {
		this.goodsSettleFlag = goodsSettleFlag;
	}

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public Integer getGoodsServerMode() {
		return goodsServerMode;
	}

	public void setGoodsServerMode(Integer goodsServerMode) {
		this.goodsServerMode = goodsServerMode;
	}

	public Integer getTakeout() {
		return takeout;
	}

	public void setTakeout(Integer takeout) {
		this.takeout = takeout;
	}

	public String getAttachementIds() {
		return attachementIds;
	}

	public void setAttachementIds(String attachementIds) {
		this.attachementIds = attachementIds;
	}

	public String getGoodsGroupLogoUrls() {
		return goodsGroupLogoUrls;
	}

	public void setGoodsGroupLogoUrls(String goodsGroupLogoUrls) {
		this.goodsGroupLogoUrls = goodsGroupLogoUrls;
	}

	public String getGoodsGroupCategoryIds() {
		return goodsGroupCategoryIds;
	}

	public void setGoodsGroupCategoryIds(String goodsGroupCategoryIds) {
		this.goodsGroupCategoryIds = goodsGroupCategoryIds;
	}

	public String getGoodsGroupCategoryNames() {
		return goodsGroupCategoryNames;
	}

	public void setGoodsGroupCategoryNames(String goodsGroupCategoryNames) {
		this.goodsGroupCategoryNames = goodsGroupCategoryNames;
	}

	public String getTechIds() {
		return techIds;
	}

	public void setTechIds(String techIds) {
		this.techIds = techIds;
	}

	public String getTechNames() {
		return techNames;
	}

	public void setTechNames(String techNames) {
		this.techNames = techNames;
	}

	public Double getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(Double minPrice) {
		this.minPrice = minPrice;
	}

	public Double getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(Double maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Double getUseTime() {
		return useTime;
	}

	public void setUseTime(Double useTime) {
		this.useTime = useTime;
	}

	public Integer getKeepTime() {
		return keepTime;
	}

	public void setKeepTime(Integer keepTime) {
		this.keepTime = keepTime;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public List<Long> getGoodsCategoryList() {
		return goodsCategoryList;
	}

	public void setGoodsCategoryList(List<Long> goodsCategoryList) {
		this.goodsCategoryList = goodsCategoryList;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getGoodsServerModeParam() {
		return goodsServerModeParam;
	}

	public void setGoodsServerModeParam(String goodsServerModeParam) {
		this.goodsServerModeParam = goodsServerModeParam;
	}

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Integer getGoodsGroupId() {
		return goodsGroupId;
	}

	public void setGoodsGroupId(Integer goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

	public Double getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public Long getGoodsLogoId() {
		return goodsLogoId;
	}

	public void setGoodsLogoId(Long goodsLogoId) {
		this.goodsLogoId = goodsLogoId;
	}

	public Integer getIsSupportMarketPrices() {
		return isSupportMarketPrices;
	}

	public void setIsSupportMarketPrices(Integer isSupportMarketPrices) {
		this.isSupportMarketPrices = isSupportMarketPrices;
	}

	public String getSpecsDesc() {
		return specsDesc;
	}

	public void setSpecsDesc(String specsDesc) {
		this.specsDesc = specsDesc;
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

	public void setAlarmNumberMin(Double alarmNumberMin) {
		this.alarmNumberMin = alarmNumberMin;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getStorageAlarmType() {
		return storageAlarmType;
	}

	public void setStorageAlarmType(String storageAlarmType) {
		this.storageAlarmType = storageAlarmType;
	}

	public Double getStorageTotalNumber() {
		return storageTotalNumber;
	}

	public void setStorageTotalNumber(Double storageTotalNumber) {
		this.storageTotalNumber = storageTotalNumber;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public Integer getIsNeedCheck() {
		return isNeedCheck;
	}

	public void setIsNeedCheck(Integer isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Integer getDigitScale() {
		return digitScale;
	}

	public void setDigitScale(Integer digitScale) {
		this.digitScale = digitScale;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getQueryData() {
		return queryData;
	}

	public void setQueryData(String queryData) {
		this.queryData = queryData;
	}

	public Double getStorageNum() {
		return storageNum;
	}

	public void setStorageNum(Double storageNum) {
		this.storageNum = storageNum;
	}

	public Integer getOrderByMode() {
		return orderByMode;
	}

	public void setOrderByMode(Integer orderByMode) {
		this.orderByMode = orderByMode;
	}

	public double getCostPrice() {
		return costPrice;
	}

	public void setCostPrice(double costPrice) {
		this.costPrice = costPrice;
	}

	public String getGoodsProValuesIds() {
		return goodsProValuesIds;
	}

	public void setGoodsProValuesIds(String goodsProValuesIds) {
		this.goodsProValuesIds = goodsProValuesIds;
	}

	public String getGoodsProValuesNames() {
		return goodsProValuesNames;
	}

	public void setGoodsProValuesNames(String goodsProValuesNames) {
		this.goodsProValuesNames = goodsProValuesNames;
	}

	public Integer getSearchRange() {
		return searchRange;
	}

	public void setSearchRange(Integer searchRange) {
		this.searchRange = searchRange;
	}

    public String getGoodsPriceSpec() {
        return goodsPriceSpec;
    }

    public void setGoodsPriceSpec(String goodsPriceSpec) {
        this.goodsPriceSpec = goodsPriceSpec;
    }

    public Integer getIsOrderDiscount() {
        return isOrderDiscount;
    }

    public void setIsOrderDiscount(Integer isOrderDiscount) {
        this.isOrderDiscount = isOrderDiscount;
    }

	
	

}
