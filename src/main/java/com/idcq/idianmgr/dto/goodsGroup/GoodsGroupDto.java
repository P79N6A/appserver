package com.idcq.idianmgr.dto.goodsGroup;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.dto.column.ColumnDto;

/**
 * 商品族dto
 * 
 * @author Administrator
 * 
 * @date 2015年7月30日
 * @time 上午10:28:26
 */
public class GoodsGroupDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1811749659662097326L;
	private Long goodsGroupId;
    private Integer goodsServerMode;
    private Long shopId;
    private String goodsName;
    private String goodsDesc;
    private String goodsDetailDesc;
    private String goodsGroupKey;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    private Integer cityId;
    private Long unitId;
    private String goodsLogo;
    private Long goodsLogoId;
    private Integer goodsStatus;
    public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	private Integer isDraft;
    private Long zanNumber;
    private Long soldNumber;
    private BigDecimal starLevelGrade;
    private Integer recommendFlag;
    private Integer hotFlag;
    private String pinyincode;
    private Date createTime;
    private Date lastUpdateTime;
    private Integer sequence;
	/**
	 * 商品族属性名称
	 */
	private List<String> groupPropertyName;    
    
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
	
	@JsonIgnore
	private Long townId;
	
	
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
    
    @JsonIgnore
    private Long shopColumnId;
    
    /*------------20150731--------------*/
    private String goodsLogoUrl;
    private String goodsPrice;
    @JsonIgnore
    private List<Map<String,Object>>goods;
    @JsonIgnore
    private List<Map<String,Object>>groupProperties;
    
    //使用时间
    private Double useTime;
    //保持天数
    private Integer keepTime;
    
    private String unit;
    
    /**
     * 用来查询的参数
     * ,商品服务模式
     */
    @JsonIgnore
    private String goodsServerModeParam;
    
    private String goodsLogo1;
    
    private List<Long>categoryIdList;
    private Long parentCategoryId;
    
    @JsonIgnore
    private Integer orderBy;
	public GoodsGroupDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}
	public Integer getGoodsServerMode() {
		return goodsServerMode;
	}
	public void setGoodsServerMode(Integer goodsServerMode) {
		this.goodsServerMode = goodsServerMode;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
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
	public String getGoodsDetailDesc() {
		return goodsDetailDesc;
	}
	public void setGoodsDetailDesc(String goodsDetailDesc) {
		this.goodsDetailDesc = goodsDetailDesc;
	}
	public String getGoodsGroupKey() {
		return goodsGroupKey;
	}
	public void setGoodsGroupKey(String goodsGroupKey) {
		this.goodsGroupKey = goodsGroupKey;
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
	public Long getUnitId() {
		return unitId;
	}
	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}
	
	public String getGoodsLogo() {
		return goodsLogo;
	}
	public void setGoodsLogo(String goodsLogo) {
		this.goodsLogo = goodsLogo;
	}
	public Integer getGoodsStatus() {
		return goodsStatus;
	}
	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}
	public Integer getIsDraft() {
		return isDraft;
	}
	public void setIsDraft(Integer isDraft) {
		this.isDraft = isDraft;
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
	public String getPinyincode() {
		return pinyincode;
	}
	public void setPinyincode(String pinyincode) {
		this.pinyincode = pinyincode;
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
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Long getTownId() {
		return townId;
	}
	public void setTownId(Long townId) {
		this.townId = townId;
	}
	public Long getShopColumnId() {
		return shopColumnId;
	}
	public void setShopColumnId(Long shopColumnId) {
		this.shopColumnId = shopColumnId;
	}
	public String getGoodsLogoUrl() {
		return goodsLogoUrl;
	}
	public void setGoodsLogoUrl(String goodsLogoUrl) {
		this.goodsLogoUrl = goodsLogoUrl;
	}
	public String getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public List<Map<String,Object>> getGoods() {
		return goods;
	}
	public void setGoods(List<Map<String,Object>> goods) {
		this.goods = goods;
	}
	public List<Map<String,Object>> getGroupProperties() {
		return groupProperties;
	}
	public void setGroupProperties(List<Map<String,Object>> groupProperties) {
		this.groupProperties = groupProperties;
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
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getGoodsServerModeParam() {
		return goodsServerModeParam;
	}
	public void setGoodsServerModeParam(String goodsServerModeParam) {
		this.goodsServerModeParam = goodsServerModeParam;
	}
	public String getGoodsLogo1() {
		return goodsLogo1;
	}
	public void setGoodsLogo1(String goodsLogo1) {
		this.goodsLogo1 = goodsLogo1;
	}
	public List<Long> getCategoryIdList() {
		return categoryIdList;
	}
	public void setCategoryIdList(List<Long> categoryIdList) {
		this.categoryIdList = categoryIdList;
	}
	public Long getGoodsLogoId() {
		return goodsLogoId;
	}
	public List<String> getGroupPropertyName() {
		return groupPropertyName;
	}
	public void setGroupPropertyName(List<String> groupPropertyName) {
		this.groupPropertyName = groupPropertyName;
	}
	public void setGoodsLogoId(Long goodsLogoId) {
		this.goodsLogoId = goodsLogoId;
	}
	public Integer getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	public Long getParentCategoryId() {
		return parentCategoryId;
	}
	public void setParentCategoryId(Long parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}
	
    
    

}