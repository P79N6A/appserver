package com.idcq.appserver.utils.solr;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.goods.GoodsCategoryDto;

/**
 * 查询的内容
 * @author pchzhang
 *
 */
public class SearchContent {
	
	/**
	 * 索引id
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private String id;
	
	/**
	 * 商品名称或者店铺名称
	 */
	@SearchCondition(seachType=SearchType.KEYWORD)
	private String goodsName;
	
	/**
	 * 商品种类
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private String goodsCategoryId;
	
	
	/**
	 * 商品编号
	 */
	private String goodsNo;

	/**
	 * 索引对应数据库的主键id
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private String primaryId;

	/**
	 * 索引类型
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private String contentType;
	
	/**
	 * 商品状态
	 */
	/*private Integer goodsStatus;*/
	
	/**
	 * 商品类型或者店铺类型
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private String goodsType;
	
	
	/**
	 * 省id
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer provinceId;
	
	/**
	 * 所属区县
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer districtId;
	/**
	 * 城市id
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer cityId;
	
	/**
	 * 镇id
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer townId;
	
	/**
	 * 所属行业，所属分类
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer typeId;
	
	/**
	 * 地理位置
	 */
	private String shopLocation;
	
	/**
	 * 搜索半径
	 */
	@SearchCondition(seachType=SearchType.LOCATIONSEARCH)
	private String searchRadius;
	
	/**
	 * 赞的次数
	 */
	private Integer zanNumber;
	
	/**
	 * 销售次数
	 */
	private Integer soldNumber;
	
	/**
	 * 商品所属店铺id
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer goodsShopId;
	
	private String shopImage;
	
	@SearchCondition(seachType=SearchType.IN,searchInRelateField="primaryId")
	private String shopIds;
	
	/**
	 * 是否支持满就送
	 */
	private Integer fullSentFlag;
	
	/**
	 * 是否支持优惠券
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer couponFlag;
	
	/**
	 * 是否支持红包
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer redPacketFlag;
	
	/**
	 * 是否支持限时折扣
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer timedDiscountFlag;
	
	/**
	 * 是否支持代金卷
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer cashCouponFlag;
	
	/**
	 * 商铺基础设施
	 */
	private String shopInfrastructure;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 电话
	 */
	private String telephone;
	
	/**
	 * 总评分
	 */
	private Float starLevelGrade;
	
	/**
	 * 服务评分
	 */
	private Float serviceGrade;
	
	/**
	 * 环境评分
	 */
	private Float envGrade;
	
	/**
	 * 开始营业时间
	 */
	private String startBTime;
	
	/**
	 * 停止营业时间
	 */
	private String stopBTime;
	
	/**
	 * 根据销售数量排序
	 */
	@SearchCondition(seachType=SearchType.ORDERBY,orderByField="soldNumber",store=false)
	private int orderBySoldNumber;
	
	/**
	 * 根据评价排序
	 */
	@SearchCondition(seachType=SearchType.ORDERBY,orderByField="starLevelGrade",store=false)
	private int orderByStarLevelGrade;
	
	/**
	 * 根据折扣排序
	 */
	@SearchCondition(seachType=SearchType.ORDERBY,orderByField="memberDiscount",store=false)
	private int orderByMemberDiscount;
	
	/**
	 * 地理位置排序
	 */
	@SearchCondition(seachType=SearchType.LOCATIONORDERBY,store=false)
	private int orderByLocation;
	
	
	private Double distance;
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer shopStatus;
	
	private Double memberDiscount;
	
	
	private String goodsLogo1;
	
	private String goodsLogo2;
	
	private String goodsUrl;
	
	private String unit;
	
	/**
	 * 商铺编号
	 */
	private String shopId;
	
	private Integer goodsSettleFlag;
	
	/**
	 * 商铺所属行业类别
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer shopColumnId;
	
	/**
	 * 商铺所属父类行业
	 */
	private Integer shopColumnPid;
	
	
	/**
	 * 标准价
	 */
	private Float standardPrice;
	
	/**
	 * 折扣价 
	 */
	private Float discountPrice;
	
	/**
	 * vip价格
	 */
	private Float vipPrice;
	
	
	/**
	 * 是否开启了外卖
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer takeoutFlag;
	/**
	 * 最终价
	 */
	private Float finalPrice;	
	
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer goodsStatus;
	
	/**
	 * 商铺模式
	 */
	@SearchCondition(seachType=SearchType.EQUALS)
	private String shopMode;
	
	
	@JsonIgnore
	@SearchCondition(seachType=SearchType.EQUALS,store=false)
    private List<ColumnDto>shopMultiColumns;
	
	/**
	 * 商品类别
	 */
	@JsonIgnore
	@SearchCondition(seachType=SearchType.EQUALS,store=false)
	private List<GoodsCategoryDto>goodsCategorys;
	
	@SearchCondition(seachType=SearchType.KEYWORD,store=false)
	private String content;
	
	/**
	 * 最低起送价格
	 */
	private Float leastBookPrice;
	
	/**
	 * 分组查询的标识
	 */
	@SearchCondition(seachType=SearchType.GROUPSEARCH)
	private Integer groupFlag;
	
	
	@SearchCondition(seachType=SearchType.EQUALS)
	private Integer goodsServerMode;
	
	/**
	 * 搜索关键字
	 */
	private List<String> searchKeys;
	
	private Double minPrice;
	
	private Double maxPrice;
	
	/**
	 * goodsServerMode要采用多值方式存储
	 */
	private List<Integer>searchModes;
	
	private String shopHours;
	
	private List<String>columnNames;
	
	private Integer queryOnlyColumn;
	
	private Integer isShow;
	
	private List<String>shopKeys;

	/**
	 * 商品类别id和父类 id
	 */
	@SearchCondition(seachType=SearchType.EQUALS,store=false)
	private List<String>goodsCategoryIdList;
	
	/**
	 * 是否正在发起商圈活动
	 */
	@SearchCondition(seachType=SearchType.EQUALS,store=true)
	private Integer businessAreaActivityFlag;
	
	/**
	 * 审核时间毫秒数
	 */
	@SearchCondition(seachType=SearchType.GT,store=true)
	private Long auditTimeNum; 
	
	/**
	 * 分组的大小
	 */
	private Integer groupLimit;
	
	
	
	/**
     * 是否推荐商铺
     */
    @SearchCondition(seachType=SearchType.EQUALS)
    private Integer isRecommend;
    
    /**
     * 是否进行分词
     */
    private transient boolean doToken = false;
    
    /**
     * 推荐理由
     */
    private String recommendReason;

	/**
	 * 0=其他,1=绿店,2=红店
	 */
	private Integer shopIdentity;

	public Integer getShopIdentity()
	{
		return shopIdentity;
	}

	public void setShopIdentity(Integer shopIdentity)
	{
		this.shopIdentity = shopIdentity;
	}

	public Integer getGoodsShopId() {
		return goodsShopId;
	}

	public void setGoodsShopId(Integer goodsShopId) {
		this.goodsShopId = goodsShopId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsCategoryId() {
		return goodsCategoryId;
	}

	public void setGoodsCategoryId(String goodsCategoryId) {
		this.goodsCategoryId = goodsCategoryId;
	}


	public String getGoodsNo() {
		return goodsNo;
	}

	public void setGoodsNo(String goodsNo) {
		this.goodsNo = goodsNo;
	}

	

	/*public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}*/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGoodsType() {
		return goodsType;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public String getPrimaryId() {
		return primaryId;
	}

	public void setPrimaryId(String primaryId) {
		this.primaryId = primaryId;
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

	public String getShopLocation() {
		return shopLocation;
	}

	public void setShopLocation(String shopLocation) {
		this.shopLocation = shopLocation;
	}

	public String getSearchRadius() {
		return searchRadius;
	}

	public void setSearchRadius(String searchRadius) {
		this.searchRadius = searchRadius;
	}

	public String getShopImage() {
		return shopImage;
	}

	public void setShopImage(String shopImage) {
		this.shopImage = shopImage;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getShopIds() {
		return shopIds;
	}

	public void setShopIds(String shopIds) {
		this.shopIds = shopIds;
	}

	public Integer getFullSentFlag() {
		return fullSentFlag;
	}

	public void setFullSentFlag(Integer fullSentFlag) {
		this.fullSentFlag = fullSentFlag;
	}

	public Integer getCouponFlag() {
		return couponFlag;
	}

	public void setCouponFlag(Integer couponFlag) {
		this.couponFlag = couponFlag;
	}

	public Integer getRedPacketFlag() {
		return redPacketFlag;
	}

	public void setRedPacketFlag(Integer redPacketFlag) {
		this.redPacketFlag = redPacketFlag;
	}

	public String getShopInfrastructure() {
		return shopInfrastructure;
	}

	public void setShopInfrastructure(String shopInfrastructure) {
		this.shopInfrastructure = shopInfrastructure;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public Float getStarLevelGrade() {
		return starLevelGrade;
	}

	public void setStarLevelGrade(Float starLevelGrade) {
		this.starLevelGrade = starLevelGrade;
	}

	public Float getServiceGrade() {
		return serviceGrade;
	}

	public void setServiceGrade(Float serviceGrade) {
		this.serviceGrade = serviceGrade;
	}

	public Float getEnvGrade() {
		return envGrade;
	}

	public void setEnvGrade(Float envGrade) {
		this.envGrade = envGrade;
	}

	public String getStartBTime() {
		return startBTime;
	}

	public void setStartBTime(String startBTime) {
		this.startBTime = startBTime;
	}

	public String getStopBTime() {
		return stopBTime;
	}

	public void setStopBTime(String stopBTime) {
		this.stopBTime = stopBTime;
	}

	public int getOrderBySoldNumber() {
		return orderBySoldNumber;
	}

	public void setOrderBySoldNumber(int orderBySoldNumber) {
		this.orderBySoldNumber = orderBySoldNumber;
	}

	public int getOrderByStarLevelGrade() {
		return orderByStarLevelGrade;
	}

	public void setOrderByStarLevelGrade(int orderByStarLevelGrade) {
		this.orderByStarLevelGrade = orderByStarLevelGrade;
	}

	public int getOrderByLocation() {
		return orderByLocation;
	}

	public void setOrderByLocation(int orderByLocation) {
		this.orderByLocation = orderByLocation;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Integer getTimedDiscountFlag() {
		return timedDiscountFlag;
	}

	public void setTimedDiscountFlag(Integer timedDiscountFlag) {
		this.timedDiscountFlag = timedDiscountFlag;
	}

	public Integer getCashCouponFlag() {
		return cashCouponFlag;
	}

	public void setCashCouponFlag(Integer cashCouponFlag) {
		this.cashCouponFlag = cashCouponFlag;
	}

	public Integer getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}

	public Double getMemberDiscount() {
		return memberDiscount;
	}

	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
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

	public String getGoodsUrl() {
		return goodsUrl;
	}

	public void setGoodsUrl(String goodsUrl) {
		this.goodsUrl = goodsUrl;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getGoodsSettleFlag() {
		return goodsSettleFlag;
	}

	public void setGoodsSettleFlag(Integer goodsSettleFlag) {
		this.goodsSettleFlag = goodsSettleFlag;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Integer getShopColumnId() {
		return shopColumnId;
	}

	public void setShopColumnId(Integer shopColumnId) {
		this.shopColumnId = shopColumnId;
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

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getShopMode() {
		return shopMode;
	}

	public void setShopMode(String shopMode) {
		this.shopMode = shopMode;
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

	public Integer getTakeoutFlag() {
		return takeoutFlag;
	}

	public void setTakeoutFlag(Integer takeoutFlag) {
		this.takeoutFlag = takeoutFlag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Float getLeastBookPrice() {
		return leastBookPrice;
	}

	public void setLeastBookPrice(Float leastBookPrice) {
		this.leastBookPrice = leastBookPrice;
	}

	public Integer getGroupFlag() {
		return groupFlag;
	}

	public void setGroupFlag(Integer groupFlag) {
		this.groupFlag = groupFlag;
	}

	public Integer getGoodsServerMode() {
		return goodsServerMode;
	}

	public void setGoodsServerMode(Integer goodsServerMode) {
		this.goodsServerMode = goodsServerMode;
	}

	public List<String> getSearchKeys() {
		return searchKeys;
	}

	public void setSearchKeys(List<String> searchKeys) {
		this.searchKeys = searchKeys;
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

	public List<Integer> getSearchModes() {
		return searchModes;
	}

	public void setSearchModes(List<Integer> searchModes) {
		this.searchModes = searchModes;
	}

	public String getShopHours() {
		return shopHours;
	}

	public void setShopHours(String shopHours) {
		this.shopHours = shopHours;
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public Integer getQueryOnlyColumn() {
		return queryOnlyColumn;
	}

	public void setQueryOnlyColumn(Integer queryOnlyColumn) {
		this.queryOnlyColumn = queryOnlyColumn;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public List<String> getGoodsCategoryIdList() {
		return goodsCategoryIdList;
	}

	public void setGoodsCategoryIdList(List<String> goodsCategoryIdList) {
		this.goodsCategoryIdList = goodsCategoryIdList;
	}

	public List<String> getShopKeys() {
		return shopKeys;
	}

	public void setShopKeys(List<String> shopKeys) {
		this.shopKeys = shopKeys;
	}

	public Integer getBusinessAreaActivityFlag() {
		return businessAreaActivityFlag;
	}

	public void setBusinessAreaActivityFlag(Integer businessAreaActivityFlag) {
		this.businessAreaActivityFlag = businessAreaActivityFlag;
	}

	public Long getAuditTimeNum() {
		return auditTimeNum;
	}

	public void setAuditTimeNum(Long auditTimeNum) {
		this.auditTimeNum = auditTimeNum;
	}

	public Integer getGroupLimit() {
		return groupLimit;
	}

	public void setGroupLimit(Integer groupLimit) {
		this.groupLimit = groupLimit;
	}

    public int getOrderByMemberDiscount()
    {
        return orderByMemberDiscount;
    }

    public void setOrderByMemberDiscount(int orderByMemberDiscount)
    {
        this.orderByMemberDiscount = orderByMemberDiscount;
    }

    public List<GoodsCategoryDto> getGoodsCategorys()
    {
        return goodsCategorys;
    }

    public void setGoodsCategorys(List<GoodsCategoryDto> goodsCategorys)
    {
        this.goodsCategorys = goodsCategorys;
    }

    public Integer getIsRecommend()
    {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend)
    {
        this.isRecommend = isRecommend;
    }

    public boolean getDoToken()
    {
        return doToken;
    }

    public void setDoToken(boolean doToken)
    {
        this.doToken = doToken;
    }

    public String getRecommendReason()
    {
        return recommendReason;
    }

    public void setRecommendReason(String recommendReason)
    {
        this.recommendReason = recommendReason;
    }

	

}
