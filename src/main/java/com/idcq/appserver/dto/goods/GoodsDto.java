package com.idcq.appserver.dto.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.dto.column.ColumnDto;


/**s
 * 商品dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月20日
 * @time 下午7:31:37
 */
public class GoodsDto implements Serializable,Cloneable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3212160319749809048L;
	private Long goodsId;
    private Integer goodsServerMode;	//商品服务模式
    private Integer goodsType;			//商品类型
    private String goodsNo;				//编号
    private Long goodsCategoryId;	//分类
    private String goodsFirstCategoryName;//商品一级分类名称
    private String goodsSecondCategoryName;//商品二级分类名称
	private Long shopId;				//商铺ID
    private Integer sequence;			//排序
    private String goodsName;			//商品名称
    private String goodsDesc;			//描述
    private String goodsDetailDesc;		//详细描述
    private Double standardPrice;		//目录价格
    private Double discountPrice;		//折扣价
    private Double vipPrice;				//会员价
    private Double finalPrice;			//折上折价
    private Double originalPrice;       //套餐内商品原价之和
    public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	private Long unitId;				//计量单位ID
    private Long lowestUnit;			//最低起订数量
    private Long goodsLogo1;			//商品图片
    private Long goodsLogo2;
    private Long goodsLogo3;
    private Long goodsLogo4;
    private Long goodsLogo5;
    private Long goodsLogo6;
    private Integer goodsStatus;		//上架状态
    private String taste;				//口味
    private String spiciness;			//辣度
    private Integer takeout;			//是否外卖
    private Long cookingStyleId;		//菜系ID
    private Integer isExpert;			//是否选择专家
    private Long zanNumber;			//点赞次数
    private Long soldNumber;			//销售次数
    private BigDecimal starLevelGrade;		//星级评价
    private Float percentage;			//平台分成比例
    private String expertIds;			//专家ID列表，多个以分号隔开
    private Double storageTotalNumber;//库存总量
    private Double storageAfterNumber;
    public Double getStorageAfterNumber() {
		return storageAfterNumber;
	}

	public void setStorageAfterNumber(Double storageAfterNumber) {
		this.storageAfterNumber = storageAfterNumber;
	}

	private Double alarmNumberMax;//最大预警库存量
    private Double alarmNumberMin;//最小预警库存量
    
    /*-----------满足接口文档需要追加字段---------------*/
	private String shopLabel;	//商铺名称
	private String area;		//所属区域
	private String industryType;//行业种类名称
	private float curDistance;	//动态距离
	private int amount;			//数量
	private float unitPrice;	//单价

	/*20160705请求参数新添加costPrice成本价字段*/
	private Double costPrice;
	private Long sourceGoodsId;//来源商品id
    private String goodsProValuesIds;//商品族属性id列  
    private String goodsProValuesNames;//商品属性名称
	
	
	@JsonIgnore
	private Float discount;		//商品限时折扣
	private Integer validityTerm;//有效期（月份为单位）
	private Integer isNeedCheck;//是否支持盘点 1.支持 0.不支持
	 
	private String pluCode;
	
	private Integer isOrderDiscount;
	
	private Map<String, String> goodsProValueMapper;//商品规格值映射关系

	public Map<String, String> getGoodsProValueMapper() {
		return goodsProValueMapper;
	}

	public void setGoodsProValueMapper(Map<String, String> goodsProValueMapper) {
		this.goodsProValueMapper = goodsProValueMapper;
	}

	/**
	 * 是否对用户显示
	 */
	@JsonIgnore
	private Integer isShow;
	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	//搜索需要关联的商铺的字段
	/**
	 * 商品所属商铺所在的城市
	 */
	@JsonIgnore
	private Long cityId;
	
	/**
	 * 商品所属商铺所在区县
	 */
	@JsonIgnore
	private Long districtId;
	
	/**
	 * 经度
	 */
	@JsonIgnore
	private Double longitude;
	
	/**
	 * 纬度
	 */
	@JsonIgnore
    private Double latitude;
	
	/**
	 * 是否支持打折
	 */
	@JsonIgnore
	private Integer goodsSettleFlag;
	
	/**
	 * 区县编号
	 */
	@JsonIgnore
	private Integer townId;
	
	/**
	 * 商铺所属行业
	 */
	@JsonIgnore
	private Integer shopColumnId;
	
	/**
	 * 商铺名称
	 */
	@JsonIgnore
	private String shopName;
	
	
	 /**
     * 店铺距离
     */
    @JsonIgnore
    private Double distance;
    
    /**
     * 时候支持红包
     */
    private Integer redPacketFlag;
    
    /**
     * 是否支持代金卷
     */
    private Integer cashCouponFlag;
    
    /**
     * 是否支持优惠券
     */
    private Integer couponFlag;
    
    /**
     * 商铺状态
     */
    private Integer shopStatus;
    
    @JsonIgnore
    private Integer shopColumnPid;
    
    
    @JsonIgnore
    private List<ColumnDto>shopMultiColumns;
    
    @JsonIgnore
    private String districtName;
    /**
     * 商品的搜索关键字
     */
    @JsonIgnore
    private List<String>searchKeys;
    //以下为2015-07-30新增
    /*------------------start-------------------*/
    //商品族编号
    private Long goodsGroupId;
    //使用时间
    private Double useTime;
    //保持天数
    private Integer keepTime;
    //推荐标记：1（推荐），0（取消推荐）
    private Integer recommendFlag;
    //热门标记:1热门,0非热门
    private Integer hotFlag;
    //商品的速记码
    private String pinyinCode;
    //创建时间
    private Date createTime;
    //最后跟新时间
    private Date lastUpdateTime;
    //附加费
    private Double extraCharge;
    /* --------------------end---------------------- */
    @JsonIgnore
    private List<String>goodsNameList;
    @JsonIgnore
    private BigDecimal minPrice;
    @JsonIgnore
    private BigDecimal maxPrice;
    
    @JsonIgnore
    private String goodsLogo;
    
    @JsonIgnore
    private String barcode;
    @JsonIgnore
    private String specsDesc;
    @JsonIgnore
    private String goodsKey;
    @JsonIgnore
    private Integer isSupportMarketPrices;
    @JsonIgnore
    private String goodsPriceSpec;
    @JsonIgnore
    private Integer goodsRebateFlag; //是否参与扣点 新增于11月6号
    @JsonIgnore
    private String unitName;
    
    @JsonIgnore
    private Integer businessAreaActivityFlag=0;

	@JsonIgnore
	private Integer sellMode;

	@JsonIgnore
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

	public GoodsDto() {
		super();
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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

	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	public Long getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(Long goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}

	public Long getShopId() {
		return shopId;
	}
	public String getPluCode() {
		return pluCode;
	}

	public void setPluCode(String pluCode) {
		this.pluCode = pluCode;
	}

	public Integer getIsNeedCheck() {
		return isNeedCheck;
	}

	public void setIsNeedCheck(Integer isNeedCheck) {
		this.isNeedCheck = isNeedCheck;
	}

	public Integer getValidityTerm() {
		return validityTerm;
	}

	public void setValidityTerm(Integer validityTerm) {
		this.validityTerm = validityTerm;
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

	public Double getStorageTotalNumber() {
		return storageTotalNumber;
	}

	public void setStorageTotalNumber(Double storageTotalNumber) {
		this.storageTotalNumber = storageTotalNumber;
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

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
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

	public void setGoodsDetailDesc(String goodsDetailDesc) {
		this.goodsDetailDesc = goodsDetailDesc;
	}

	public Double getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(Double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public Double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public Double getVipPrice() {
		return vipPrice;
	}

	public void setVipPrice(Double vipPrice) {
		this.vipPrice = vipPrice;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public Long getUnitId() {
		return unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	public Long getLowestUnit() {
		return lowestUnit;
	}

	public void setLowestUnit(Long lowestUnit) {
		this.lowestUnit = lowestUnit;
	}

	public Long getGoodsLogo1() {
		return goodsLogo1;
	}

	public void setGoodsLogo1(Long goodsLogo1) {
		this.goodsLogo1 = goodsLogo1;
	}

	public Long getGoodsLogo2() {
		return goodsLogo2;
	}

	public void setGoodsLogo2(Long goodsLogo2) {
		this.goodsLogo2 = goodsLogo2;
	}

	public Long getGoodsLogo3() {
		return goodsLogo3;
	}

	public void setGoodsLogo3(Long goodsLogo3) {
		this.goodsLogo3 = goodsLogo3;
	}

	public Long getGoodsLogo4() {
		return goodsLogo4;
	}

	public void setGoodsLogo4(Long goodsLogo4) {
		this.goodsLogo4 = goodsLogo4;
	}

	public Long getGoodsLogo5() {
		return goodsLogo5;
	}

	public void setGoodsLogo5(Long goodsLogo5) {
		this.goodsLogo5 = goodsLogo5;
	}

	public Long getGoodsLogo6() {
		return goodsLogo6;
	}

	public void setGoodsLogo6(Long goodsLogo6) {
		this.goodsLogo6 = goodsLogo6;
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

	public Integer getTakeout() {
		return takeout;
	}

	public void setTakeout(Integer takeout) {
		this.takeout = takeout;
	}

	public Long getCookingStyleId() {
		return cookingStyleId;
	}

	public void setCookingStyleId(Long cookingStyleId) {
		this.cookingStyleId = cookingStyleId;
	}

	public Integer isExpert() {
		return isExpert;
	}

	public void setExpert(Integer isExpert) {
		this.isExpert = isExpert;
	}

	public Long getZanNumber() {
		return zanNumber;
	}

	public void setZanNumber(Long zanNumber) {
		this.zanNumber = zanNumber;
	}

	public Long getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(Long soldNumber) {
		this.soldNumber = soldNumber;
	}

	public BigDecimal getStarLevelGrade() {
		return starLevelGrade;
	}

	public void setStarLevelGrade(BigDecimal starLevelGrade) {
		this.starLevelGrade = starLevelGrade;
	}

	public Float getPercentage() {
		return percentage;
	}

	public void setPercentage(Float percentage) {
		this.percentage = percentage;
	}

	public String getExpertIds() {
		return expertIds;
	}

	public void setExpertIds(String expertIds) {
		this.expertIds = expertIds;
	}

	public String getShopLabel() {
		return shopLabel;
	}

	public void setShopLabel(String shopLabel) {
		this.shopLabel = shopLabel;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getIndustryType() {
		return industryType;
	}

	public void setIndustryType(String industryType) {
		this.industryType = industryType;
	}

	public float getCurDistance() {
		return curDistance;
	}

	public void setCurDistance(float curDistance) {
		this.curDistance = curDistance;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Override
	public String toString() {
		return "GoodsDto [goodsId=" + goodsId + ", goodsServerMode="
				+ goodsServerMode + ", goodsType=" + goodsType + ", goodsNo="
				+ goodsNo + ", goodsCategoryId=" + goodsCategoryId
				+ ", shopId=" + shopId + ", sequence=" + sequence
				+ ", goodsName=" + goodsName + ", goodsDesc=" + goodsDesc
				+ ", goodsDetailDesc=" + goodsDetailDesc + ", standardPrice="
				+ standardPrice + ", discountPrice=" + discountPrice
				+ ", vipPrice=" + vipPrice + ", finalPrice=" + finalPrice
				+ ", unitId=" + unitId + ", lowestUnit=" + lowestUnit
				+ ", goodsLogo1=" + goodsLogo1 + ", goodsLogo2=" + goodsLogo2
				+ ", goodsLogo3=" + goodsLogo3 + ", goodsLogo4=" + goodsLogo4
				+ ", goodsLogo5=" + goodsLogo5 + ", goodsLogo6=" + goodsLogo6
				+ ", goodsStatus=" + goodsStatus + ", taste=" + taste
				+ ", spiciness=" + spiciness + ", takeout=" + takeout
				+ ", cookingStyleId=" + cookingStyleId + ", isExpert="
				+ isExpert + ", zanNumber=" + zanNumber + ", soldNumber="
				+ soldNumber + ", starLevelGrade=" + starLevelGrade
				+ ", percentage=" + percentage + ", expertIds=" + expertIds
				+ ", shopLabel=" + shopLabel + ", area=" + area
				+ ", industryType=" + industryType + ", curDistance="
				+ curDistance + ", amount=" + amount + ", unitPrice="
				+ unitPrice + "]";
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Long districtId) {
		this.districtId = districtId;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Float getDiscount() {
		return discount;
	}

	public void setDiscount(Float discount) {
		this.discount = discount;
	}

	public Integer getIsExpert() {
		return isExpert;
	}

	public void setIsExpert(Integer isExpert) {
		this.isExpert = isExpert;
	}

	public Integer getGoodsSettleFlag() {
		return goodsSettleFlag;
	}

	public void setGoodsSettleFlag(Integer goodsSettleFlag) {
		this.goodsSettleFlag = goodsSettleFlag;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}

	public Integer getShopColumnId() {
		return shopColumnId;
	}

	public void setShopColumnId(Integer shopColumnId) {
		this.shopColumnId = shopColumnId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getRedPacketFlag() {
		return redPacketFlag;
	}

	public void setRedPacketFlag(Integer redPacketFlag) {
		this.redPacketFlag = redPacketFlag;
	}

	public Integer getCashCouponFlag() {
		return cashCouponFlag;
	}

	public void setCashCouponFlag(Integer cashCouponFlag) {
		this.cashCouponFlag = cashCouponFlag;
	}

	public Integer getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(Integer couponFlag) {
		this.couponFlag = couponFlag;
	}

	public Integer getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}

	public Integer getShopColumnPid() {
		return shopColumnPid;
	}

	public void setShopColumnPid(Integer shopColumnPid) {
		this.shopColumnPid = shopColumnPid;
	}

	public List<ColumnDto> getShopMultiColumns() {
		return shopMultiColumns;
	}

	public void setShopMultiColumns(List<ColumnDto> shopMultiColumns) {
		this.shopMultiColumns = shopMultiColumns;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public List<String> getSearchKeys() {
		return searchKeys;
	}

	public void setSearchKeys(List<String> searchKeys) {
		this.searchKeys = searchKeys;
	}

	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
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

	public List<String> getGoodsNameList() {
		return goodsNameList;
	}

	public void setGoodsNameList(List<String> goodsNameList) {
		this.goodsNameList = goodsNameList;
	}

	public BigDecimal getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}

	public BigDecimal getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}

	public Integer getRecommendFlag() {
		return recommendFlag;
	}

	public void setRecommendFlag(Integer recommendFlag) {
		this.recommendFlag = recommendFlag;
	}

	public Integer getHotFlag() {
		return hotFlag;
	}

	public void setHotFlag(Integer hotFlag) {
		this.hotFlag = hotFlag;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Double getExtraCharge() {
		return extraCharge;
	}

	public void setExtraCharge(Double extraCharge) {
		this.extraCharge = extraCharge;
	}

	public String getGoodsLogo() {
		return goodsLogo;
	}

	public void setGoodsLogo(String goodsLogo) {
		this.goodsLogo = goodsLogo;
	}


	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getSpecsDesc() {
		return specsDesc;
	}

	public void setSpecsDesc(String specsDesc) {
		this.specsDesc = specsDesc;
	}

	public String getGoodsKey() {
		return goodsKey;
	}

	public void setGoodsKey(String goodsKey) {
		this.goodsKey = goodsKey;
	}

	public Integer getIsSupportMarketPrices() {
		return isSupportMarketPrices;
	}

	public void setIsSupportMarketPrices(Integer isSupportMarketPrices) {
		this.isSupportMarketPrices = isSupportMarketPrices;
	}

	public String getGoodsPriceSpec() {
		return goodsPriceSpec;
	}

	public void setGoodsPriceSpec(String goodsPriceSpec) {
		this.goodsPriceSpec = goodsPriceSpec;
	}

	public Integer getGoodsRebateFlag() {
		return goodsRebateFlag;
	}

	public void setGoodsRebateFlag(Integer goodsRebateFlag) {
		this.goodsRebateFlag = goodsRebateFlag;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public Integer getBusinessAreaActivityFlag() {
		return businessAreaActivityFlag;
	}

	public void setBusinessAreaActivityFlag(Integer businessAreaActivityFlag) {
		this.businessAreaActivityFlag = businessAreaActivityFlag;
	}

	public Long getSourceGoodsId() {
		return sourceGoodsId;
	}

	public void setSourceGoodsId(Long sourceGoodsId) {
		this.sourceGoodsId = sourceGoodsId;
	}


	public String getGoodsProValuesIds() {
		return goodsProValuesIds;
	}

	public void setGoodsProValuesIds(String goodsProValuesIds) {
		this.goodsProValuesIds = goodsProValuesIds;
	}
	public Double getCostPrice() {
		return costPrice;
	}

	public String getGoodsProValuesNames() {
		return goodsProValuesNames;
	}

	public void setGoodsProValuesNames(String goodsProValuesNames) {
		this.goodsProValuesNames = goodsProValuesNames;
	}

	public void setCostPrice(Double costPrice) {
		this.costPrice = costPrice;
	}
	
	@Override
	public GoodsDto clone() {
		GoodsDto clone = null;
		try {
			clone=(GoodsDto)super.clone();
		} catch (CloneNotSupportedException e) {
		}

		return clone; 
	}
    public String getGoodsFirstCategoryName() {
		return goodsFirstCategoryName;
	}

	public void setGoodsFirstCategoryName(String goodsFirstCategoryName) {
		this.goodsFirstCategoryName = goodsFirstCategoryName;
	}

	public String getGoodsSecondCategoryName() {
		return goodsSecondCategoryName;
	}

	public void setGoodsSecondCategoryName(String goodsSecondCategoryName) {
		this.goodsSecondCategoryName = goodsSecondCategoryName;
	}

    public Integer getIsOrderDiscount() {
        return isOrderDiscount;
    }

    public void setIsOrderDiscount(Integer isOrderDiscount) {
        this.isOrderDiscount = isOrderDiscount;
    }
}