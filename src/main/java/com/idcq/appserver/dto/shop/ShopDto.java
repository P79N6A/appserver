package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.utils.MyTimeSerializer;


/**
 * 商铺dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月10日
 * @time 下午6:22:39
 */
public class ShopDto implements Serializable,Comparable<ShopDto>{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 6203992495678868086L;
	@JsonIgnore
	private Long shopId;
	@JsonIgnore
    private Integer shopStatus;
    private String shopName;
    @JsonIgnore
    private Integer shopType;
    @JsonIgnore
    private Long principalId;
    @JsonIgnore
    private String telephone;
    @JsonIgnore
    private String principalName;
    private String address;
    private Double longitude;
    private Double latitude;
    @JsonIgnore
    private String businessLicense;
    @JsonIgnore
    private String businessLicensePic;
    @JsonIgnore
    private Long referUserId;
    @JsonIgnore
    private Long referShopId;
    @JsonIgnore
    private Integer referUserType;
    
    /* 20160504连锁店相关信息 */
    @JsonIgnore
    private Long integrationPromotionUserId;
    @JsonIgnore
    private BigDecimal integrationPromotionRatio;
    @JsonIgnore
    private Long integrationFacilitateUserId;
    @JsonIgnore
    private BigDecimal integrationFacilitateRatio;
    @JsonIgnore
    private Long integrationPrincipalUserId;
    @JsonIgnore
    private BigDecimal integrationPrincipalRatio;
//    private Double buyVMoney;
//    private Integer buyVType;
//    public Double getBuyVMoney() {
//		return buyVMoney;
//	}
//
//
//	public void setBuyVMoney(Double buyVMoney) {
//		this.buyVMoney = buyVMoney;
//	}
//
//
//	public Integer getBuyVType() {
//		return buyVType;
//	}

    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonSerialize(using=MyTimeSerializer.class)
    private Date startBTime;
    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonSerialize(using=MyTimeSerializer.class)
    private Date stopBTime;
    private String bigLogo;
    private String smallLogo;
    @JsonIgnore
    private Double memberDiscount;
    @JsonIgnore
    private Double platformDiscount;
    @JsonIgnore
    private Integer auditStatus;
    @JsonIgnore
    private Date auditTime;
    @JsonIgnore
    private Long auditUserId;
    
    private Double starLevelGrade;
    private Double envGrade;
    private Double serviceGrade;
    /**
     * 配送速度评分
     */
    private Double logisticsGrade;
    @JsonIgnore
    private Double deposit;
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String withdrawPassword;
    @JsonIgnore
    private Integer isPrincipalPwd;
    @JsonIgnore
    private Date createTime;
    @JsonIgnore
    private Date lastUpdateTime;
    @JsonIgnore
    private String shopDesc;
    @JsonIgnore
    private Integer provinceId;
    @JsonIgnore
	private Long cityId;
    @JsonIgnore
	private Integer districtId;
    
    @JsonIgnore
    private String districtName;
    @JsonIgnore
    private Integer columnId;
    @JsonIgnore
    private Integer soldNumber;
    
    /*----------追加20150324-------------*/
    private String shopInfrastructure;
    private Integer townId;
    private Integer shopLogoId;
    private Integer zanNumber;
    private Long shopServerUserId;
    private Double percentage;
    private Integer isBook;
    private String shopLogoUrl;
    private Integer timedDiscountFlag;		//是否支持限时折扣
    
    /*-----------追加20150512------------*/
    private Integer takeoutFlag;			//是否启用外卖
    private Integer bookFlag;				//是否启用预定
    /**
     *  '是否启用预约上门：是=1(默认)，否=0',
     */
    private Integer isHomeService;
    /*-----------追加20150518------------*/
   
    @JsonIgnore
    private Integer malling; // 抹零设置: 0无抹零设置, 1抹元, 2抹角   add 2015-11-17
    
    @JsonIgnore
    private String contractValidFrom; // 合作合同生效日
    @JsonIgnore
    private String contractValidTo; // 合作合同截止日
    
    private String businessLicenceId; //营业执照图片ID
    
	private String businessLicenceNo; //营业执照号



	private String businessLicensePicUrl;//营业执照图URL地址
    
    /*-----------追加20160215------------*/
    
    private String shopIndustryName; // 店铺行业
    
    
    /**
     * 是否支持代金卷
     */
    @JsonIgnore
    private Integer cashCouponFlag;
    
    /**
     * 是否支持优惠券
     */
    @JsonIgnore
    private Integer couponFlag;
    
    /**
     * 是否支持红包
     */
    @JsonIgnore
    private Integer redPacketFlag;
    
    /**
     * 是否支持满就送
     */
    @JsonIgnore
    private Integer fullSentFlag;
    /**
     * 店铺距离
     */
    @JsonIgnore
    private Double distance;
    
    @JsonIgnore
    private String shopMode;
    
    @JsonIgnore
    private Integer shopColumnPid;
    @JsonIgnore
    private List<ColumnDto>shopMultiColumns;
    
    @JsonIgnore
    private String townName;
    
    @JsonIgnore
    private Float leastBookPrice;
    
    private List<Map<String,Object>>goods=new ArrayList<Map<String,Object>>();
    @JsonIgnore
    private Integer confirmMinute;
    
    @JsonIgnore
    private List<String>searchKeys;
    private String shopHours;
    
    private Integer serverMode;
    
    /**
     * 用来比较排序的
     */
    @JsonIgnore
    private Integer sortIndex;
    
    /**
     * 平台分成方式
     * 结算方式: 1-按抽成比例结算,2-按折扣结算
     */
    @JsonIgnore
    private Integer settleType;
    /**
     * 订单分成方式
     */
    @JsonIgnore
    private Integer orderSettleType;
    
    @JsonIgnore 
    private Integer sign; // 是否签约：0-线下签约  1-非签约  2-线上签约
    
    /**
     * 店铺关键字
     */
    @JsonIgnore
    private String shopKey;
    
    /**
     * 商圈活动标识
     */
    @JsonIgnore
    private Integer businessAreaActivityFlag=0;
    /**
     * 推荐会员的手机号或是userId
     */
    private String referMobileOrUserId;
    /**
     * 商户类型:1-个人商户；2-个体商户；3-企业商户
     */
    @JsonIgnore
	private Integer shopClassify;
    /**
     * 组织机构代码
     */
	private String organizationCode;
	
	private Integer organizationCodePicId;
	/**
     * 组织机构代码证图片
     */
	private String organizationCodePic;
	 /**
     * 税务登记证号
     */
	private String taxRegistrationCertificate;
	private Integer taxRegistrationCertificatePicId; 
	/**
     * 税务登记证图片
     */
	private String taxRegistrationCertificatePic;
	 /**
     * 经营许可证号,可能有多个
     */
	private String businessCertificates;
	private String businessCertificatePicIds; 
	/**
     * 经营许可证图片，可能有多个
     */
	private String businessCertificatePics;
	
	private Integer authorizationPicId;
	/**
     * 授权书图片
     */
	private String authorizationPic;
	 /**
     * 法人身份号
     */
	private String principalIdentityCardNo;
	 /**
     * 法人身份证正面图片Id
     */
	private Integer principalIdentityCardPicId1;
	private String principalIdentityCardPicUrl1;
	 /**
     * 法人身份证反面图片Id
     */
	private Integer principalIdentityCardPicId2;
	private String principalIdentityCardPicUrl2;
	 /**
     * 个人技能证标号，可能多个
     */
	private String skillsCertificateNos;
	
	private String skillsCertificatePicIds;
	 /**
     * 个人技能证图片可能多个
     */
	private String skillsCertificatePics;
	
	private Integer is3In1;//是否三证合一
	/**
	 * 等级积分
	 */
	private Integer shopPoint;
	/**
	 * 等级Id
	 */
	private Integer levelId;

    /**
	 * 等级名称
	 */
	private String levelName;
	
	   /**
     * 经营者名称
     */
    private String shopManagerName;
    /**
     * 经营者身份证
     */
    private String shopManagerIdentityCardNo;
    
    private String shopManagerIdentityCardPic1;
    
    private String shopManagerIdentityCardPic2;
    
    /**
     * 是否推荐
     */
    private Integer isRecommend;
	
    /*****************2016年6月2日 09:29:37*************************/
    /**
     * 服务人员手机号
     */
	private String shopServeUserMobile;
	/**
	 * 营业执照生效日
	 */
	private Date businessLicenseValidFrom;
	
	/**
	 * 营业执照截止日
	 */
	private Date businessLicenseValidTo;
	/**
	 * 经营许可证截止时间
	 */
	private Date businessCertificateValidTo;
	
	/**
	 * 店铺经营者是否为法人：1=是(默认),0=否
	 */
	private Integer shopManagerIsCorporate;
	
    /**
     * 连锁店铺类型：0=非连锁,1=直营总部,2= 直营分店
     */
    private Integer chainStoresType;
    /**
     * 连锁店总部id
     */
    private Long headShopId;
    
    /**
     * 推荐理由
     */
    @JsonIgnore
    private String recommendReason;

	/**
	 *0=其他,1=绿店,2=红店
	 */
	@JsonIgnore
	private Integer shopIdentity;

	/**
	 *购买V产品金额
	 */
	@JsonIgnore
	private Double buyvMoney;
	/**
	 *本次购买V产品金额
	 */
	@JsonIgnore
	private Double totalFee;


	/**
	 *购买V产品类型
	 0：一点结算
	 1：一点管家
	 2：单屏收银机
	 3：双屏收银机
	 */
	@JsonIgnore
	private Integer buyvType;
    @JsonIgnore
    private String orderId;

	public Integer getBuyvType()
	{
		return buyvType;
	}

	public void setBuyvType(Integer buyvType)
	{
		this.buyvType = buyvType;
	}

	public Integer getShopIdentity()
	{
		return shopIdentity;
	}
    public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}
	public void setShopIdentity(Integer shopIdentity)
	{
		this.shopIdentity = shopIdentity;
	}

	public Double getBuyvMoney()
	{
		return buyvMoney;
	}

	public void setBuyvMoney(Double buyvMoney)
	{
		this.buyvMoney = buyvMoney;
	}


	public ShopDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public Integer getSign() {
		return sign;
	}


	public void setSign(Integer sign) {
		this.sign = sign;
	}


	public String getBusinessLicenceId() {
		return businessLicenceId;
	}


	public void setBusinessLicenceId(String businessLicenceId) {
		this.businessLicenceId = businessLicenceId;
	}


	public Double getLogisticsGrade() {
		return logisticsGrade;
	}


	public void setLogisticsGrade(Double logisticsGrade) {
		this.logisticsGrade = logisticsGrade;
	}


	public Integer getIsRecommend() {
		return isRecommend;
	}


	public void setIsRecommend(Integer isRecommend) {
		this.isRecommend = isRecommend;
	}


	public String getBusinessLicenceNo() {
		return businessLicenceNo;
	}


	public void setBusinessLicenceNo(String businessLicenceNo) {
		this.businessLicenceNo = businessLicenceNo;
	}


	public Double getStarLevelGrade() {
		return starLevelGrade;
	}

	public void setStarLevelGrade(Double starLevelGrade) {
		this.starLevelGrade = starLevelGrade;
	}

	public 	Long getShopId() {
		return shopId;
	}

		public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	public String getContractValidFrom() {
		return contractValidFrom;
	}

	public void setContractValidFrom(String contractValidFrom) {
		this.contractValidFrom = contractValidFrom;
	}

	public String getContractValidTo() {
		return contractValidTo;
	}

	public void setContractValidTo(String contractValidTo) {
		this.contractValidTo = contractValidTo;
	}

	public Double getLogisticsGgrade() {
		return logisticsGrade;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getMalling() {
		return malling;
	}

	public void setMalling(Integer malling) {
		this.malling = malling;
	}

	public void setLogisticsGgrade(Double logisticsGrade) {
		this.logisticsGrade = logisticsGrade;
	}

	public Integer getShopStatus() {
		return shopStatus;
	}

	public void setShopStatus(Integer shopStatus) {
		this.shopStatus = shopStatus;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getShopType() {
		return shopType;
	}

	public void setShopType(Integer shopType) {
		this.shopType = shopType;
	}

	public Long getPrincipalId() {
		return principalId;
	}

	public void setPrincipalId(Long principalId) {
		this.principalId = principalId;
	}


	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getBusinessLicensePic() {
		return businessLicensePic;
	}

	public void setBusinessLicensePic(String businessLicensePic) {
		this.businessLicensePic = businessLicensePic;
	}

	public Long getReferUserId() {
		return referUserId;
	}

	public void setReferUserId(Long referUserId) {
		this.referUserId = referUserId;
	}

	public Long getReferShopId() {
		return referShopId;
	}

	public void setReferShopId(Long referShopId) {
		this.referShopId = referShopId;
	}

	public Integer getReferUserType() {
		return referUserType;
	}

	public void setReferUserType(Integer referUserType) {
		this.referUserType = referUserType;
	}


	public Date getStartBTime() {
		return startBTime;
	}

	public void setStartBTime(Date startBTime) {
		this.startBTime = startBTime;
	}

	public Date getStopBTime() {
		return stopBTime;
	}

	public void setStopBTime(Date stopBTime) {
		this.stopBTime = stopBTime;
	}

	public String getBigLogo() {
		return bigLogo;
	}

	public void setBigLogo(String bigLogo) {
		this.bigLogo = bigLogo;
	}

	public String getSmallLogo() {
		return smallLogo;
	}

	public void setSmallLogo(String smallLogo) {
		this.smallLogo = smallLogo;
	}

	public Double getMemberDiscount() {
		if(null == this.memberDiscount || 0 >= this.memberDiscount.doubleValue()){
			this.memberDiscount=1D;
		}
		return memberDiscount;
	}

	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}

	public Double getPlatformDiscount() {
		if(null == this.platformDiscount || 0 >= this.platformDiscount.doubleValue()){
			this.platformDiscount=1D;
		}
		return platformDiscount;
	}

	public void setPlatformDiscount(Double platformDiscount) {
		this.platformDiscount = platformDiscount;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Long getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}


	public Double getEnvGrade() {
		return envGrade;
	}

	public void setEnvGrade(Double envGrade) {
		this.envGrade = envGrade;
	}

	public Double getServiceGrade() {
		return serviceGrade;
	}

	public void setServiceGrade(Double serviceGrade) {
		this.serviceGrade = serviceGrade;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWithdrawPassword() {
		return withdrawPassword;
	}

	public void setWithdrawPassword(String withdrawPassword) {
		this.withdrawPassword = withdrawPassword;
	}


	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getIsPrincipalPwd() {
		return isPrincipalPwd;
	}

	public void setIsPrincipalPwd(Integer isPrincipalPwd) {
		this.isPrincipalPwd = isPrincipalPwd;
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

	public String getShopDesc() {
		return shopDesc;
	}

	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getSoldNumber() {
		return soldNumber;
	}

	public void setSoldNumber(Integer soldNumber) {
		this.soldNumber = soldNumber;
	}

	public String getShopInfrastructure() {
		return shopInfrastructure;
	}

	public void setShopInfrastructure(String shopInfrastructure) {
		this.shopInfrastructure = shopInfrastructure;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}

	public Integer getShopLogoId() {
		return shopLogoId;
	}

	public void setShopLogoId(Integer shopLogoId) {
		this.shopLogoId = shopLogoId;
	}

	public Integer getZanNumber() {
		return zanNumber;
	}

	public void setZanNumber(Integer zanNumber) {
		this.zanNumber = zanNumber;
	}

	public Long getShopServerUserId() {
		return shopServerUserId;
	}

	public void setShopServerUserId(Long shopServerUserId) {
		this.shopServerUserId = shopServerUserId;
	}

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Integer getIsBook() {
		return isBook;
	}

	public void setIsBook(Integer isBook) {
		this.isBook = isBook;
	}

	public String getShopLogoUrl() {
		return shopLogoUrl;
	}

	public void setShopLogoUrl(String shopLogoUrl) {
		this.shopLogoUrl = shopLogoUrl;
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

	public Integer getFullSentFlag() {
		return fullSentFlag;
	}

	public void setFullSentFlag(Integer fullSentFlag) {
		this.fullSentFlag = fullSentFlag;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public Integer getTakeoutFlag() {
		return takeoutFlag;
	}

	public void setTakeoutFlag(Integer takeoutFlag) {
		this.takeoutFlag = takeoutFlag;
	}

	public Integer getBookFlag() {
		return bookFlag;
	}

	public void setBookFlag(Integer bookFlag) {
		this.bookFlag = bookFlag;
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

	public String getTownName() {
		return townName;
	}

	public void setTownName(String townName) {
		this.townName = townName;
	}

	public Float getLeastBookPrice() {
		return leastBookPrice;
	}

	public void setLeastBookPrice(Float leastBookPrice) {
		this.leastBookPrice = leastBookPrice;
	}

	public List<Map<String,Object>> getGoods() {
		return goods;
	}

	public void setGoods(List<Map<String,Object>> goods) {
		this.goods = goods;
	}

	public Integer getConfirmMinute() {
		return confirmMinute;
	}

	public void setConfirmMinute(Integer confirmMinute) {
		this.confirmMinute = confirmMinute;
	}

	public List<String> getSearchKeys() {
		return searchKeys;
	}

	public void setSearchKeys(List<String> searchKeys) {
		this.searchKeys = searchKeys;
	}

	public String getShopHours() {
		return shopHours;
	}

	public void setShopHours(String shopHours) {
		this.shopHours = shopHours;
	}

	public Integer getServerMode() {
		return serverMode;
	}

	public void setServerMode(Integer serverMode) {
		this.serverMode = serverMode;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex(Integer sortIndex) {
		this.sortIndex = sortIndex;
	}

	public Integer getIsHomeService() {
		return isHomeService;
	}

	public void setIsHomeService(Integer isHomeService) {
		this.isHomeService = isHomeService;
	}

	@Override
	public int compareTo(ShopDto shopDto) {
		if(this.getSortIndex()!=null&&shopDto.getSortIndex()!=null)
		{
			int aVal=shopDto.getSortIndex().intValue();
			int currentVal=this.getSortIndex().intValue();
			if(aVal>currentVal)
			{ 
				return -1;
			}
			else if(aVal<currentVal)
			{
				return 1;
			}
		}
		return 0;
	}

	public Integer getSettleType() {
		return settleType;
	}

	public void setSettleType(Integer settleType) {
		this.settleType = settleType;
	}

	public Integer getOrderSettleType() {
		return orderSettleType;
	}

	public void setOrderSettleType(Integer orderSettleType) {
		this.orderSettleType = orderSettleType;
	}


	public String getShopIndustryName() {
		return shopIndustryName;
	}


	public void setShopIndustryName(String shopIndustryName) {
		this.shopIndustryName = shopIndustryName;
	}


	public String getShopKey() {
		return shopKey;
	}


	public void setShopKey(String shopKey) {
		this.shopKey = shopKey;
	}


	public Integer getBusinessAreaActivityFlag() {
		return businessAreaActivityFlag;
	}


	public void setBusinessAreaActivityFlag(Integer businessAreaActivityFlag) {
		this.businessAreaActivityFlag = businessAreaActivityFlag;
	}


	public String getBusinessLicensePicUrl() {
		return businessLicensePicUrl;
	}


	public void setBusinessLicensePicUrl(String businessLicensePicUrl) {
		this.businessLicensePicUrl = businessLicensePicUrl;
	}


	public String getReferMobileOrUserId() {
		return referMobileOrUserId;
	}


	public void setReferMobileOrUserId(String referMobileOrUserId) {
		this.referMobileOrUserId = referMobileOrUserId;
	}


	public Integer getShopClassify() {
		return shopClassify;
	}


	public void setShopClassify(Integer shopClassify) {
		this.shopClassify = shopClassify;
	}


	public String getOrganizationCode() {
		return organizationCode;
	}


	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}


	public String getOrganizationCodePic() {
		return organizationCodePic;
	}


	public void setOrganizationCodePic(String organizationCodePic) {
		this.organizationCodePic = organizationCodePic;
	}


	public String getTaxRegistrationCertificate() {
		return taxRegistrationCertificate;
	}


	public void setTaxRegistrationCertificate(String taxRegistrationCertificate) {
		this.taxRegistrationCertificate = taxRegistrationCertificate;
	}


	public String getTaxRegistrationCertificatePic() {
		return taxRegistrationCertificatePic;
	}


	public void setTaxRegistrationCertificatePic(
			String taxRegistrationCertificatePic) {
		this.taxRegistrationCertificatePic = taxRegistrationCertificatePic;
	}

	public String getAuthorizationPic() {
		return authorizationPic;
	}


	public void setAuthorizationPic(String authorizationPic) {
		this.authorizationPic = authorizationPic;
	}


	public String getPrincipalIdentityCardNo() {
		return principalIdentityCardNo;
	}


	public void setPrincipalIdentityCardNo(String principalIdentityCardNo) {
		this.principalIdentityCardNo = principalIdentityCardNo;
	}


	public Integer getPrincipalIdentityCardPicId1() {
		return principalIdentityCardPicId1;
	}


	public void setPrincipalIdentityCardPicId1(Integer principalIdentityCardPicId1) {
		this.principalIdentityCardPicId1 = principalIdentityCardPicId1;
	}


	public Integer getPrincipalIdentityCardPicId2() {
		return principalIdentityCardPicId2;
	}


	public void setPrincipalIdentityCardPicId2(Integer principalIdentityCardPicId2) {
		this.principalIdentityCardPicId2 = principalIdentityCardPicId2;
	}


	public Integer getOrganizationCodePicId() {
		return organizationCodePicId;
	}


	public void setOrganizationCodePicId(Integer organizationCodePicId) {
		this.organizationCodePicId = organizationCodePicId;
	}


	public Integer getTaxRegistrationCertificatePicId() {
		return taxRegistrationCertificatePicId;
	}


	public void setTaxRegistrationCertificatePicId(
			Integer taxRegistrationCertificatePicId) {
		this.taxRegistrationCertificatePicId = taxRegistrationCertificatePicId;
	}


	public String getBusinessCertificates() {
		return businessCertificates;
	}


	public void setBusinessCertificates(String businessCertificates) {
		this.businessCertificates = businessCertificates;
	}


	public String getBusinessCertificatePicIds() {
		return businessCertificatePicIds;
	}


	public void setBusinessCertificatePicIds(String businessCertificatePicIds) {
		this.businessCertificatePicIds = businessCertificatePicIds;
	}


	public String getBusinessCertificatePics() {
		return businessCertificatePics;
	}


	public void setBusinessCertificatePics(String businessCertificatePics) {
		this.businessCertificatePics = businessCertificatePics;
	}


	public Integer getAuthorizationPicId() {
		return authorizationPicId;
	}


	public void setAuthorizationPicId(Integer authorizationPicId) {
		this.authorizationPicId = authorizationPicId;
	}


	public String getSkillsCertificateNos() {
		return skillsCertificateNos;
	}


	public void setSkillsCertificateNos(String skillsCertificateNos) {
		this.skillsCertificateNos = skillsCertificateNos;
	}


	public String getSkillsCertificatePicIds() {
		return skillsCertificatePicIds;
	}


	public void setSkillsCertificatePicIds(String skillsCertificatePicIds) {
		this.skillsCertificatePicIds = skillsCertificatePicIds;
	}


	public String getSkillsCertificatePics() {
		return skillsCertificatePics;
	}


	public void setSkillsCertificatePics(String skillsCertificatePics) {
		this.skillsCertificatePics = skillsCertificatePics;
	}


	public String getPrincipalIdentityCardPicUrl1() {
		return principalIdentityCardPicUrl1;
	}


	public void setPrincipalIdentityCardPicUrl1(String principalIdentityCardPicUrl1) {
		this.principalIdentityCardPicUrl1 = principalIdentityCardPicUrl1;
	}


	public String getPrincipalIdentityCardPicUrl2() {
		return principalIdentityCardPicUrl2;
	}


	public void setPrincipalIdentityCardPicUrl2(String principalIdentityCardPicUrl2) {
		this.principalIdentityCardPicUrl2 = principalIdentityCardPicUrl2;
	}



    public Long getIntegrationPromotionUserId()
    {
        return integrationPromotionUserId;
    }


    public void setIntegrationPromotionUserId(Long integrationPromotionUserId)
    {
        this.integrationPromotionUserId = integrationPromotionUserId;
    }


    public BigDecimal getIntegrationPromotionRatio()
    {
        return integrationPromotionRatio;
    }


    public void setIntegrationPromotionRatio(BigDecimal integrationPromotionRatio)
    {
        this.integrationPromotionRatio = integrationPromotionRatio;
    }


    public Long getIntegrationFacilitateUserId()
    {
        return integrationFacilitateUserId;
    }


    public void setIntegrationFacilitateUserId(Long integrationFacilitateUserId)
    {
        this.integrationFacilitateUserId = integrationFacilitateUserId;
    }


    public BigDecimal getIntegrationFacilitateRatio()
    {
        return integrationFacilitateRatio;
    }


    public void setIntegrationFacilitateRatio(BigDecimal integrationFacilitateRatio)
    {
        this.integrationFacilitateRatio = integrationFacilitateRatio;
    }


    public Long getIntegrationPrincipalUserId()
    {
        return integrationPrincipalUserId;
    }


    public void setIntegrationPrincipalUserId(Long integrationPrincipalUserId)
    {
        this.integrationPrincipalUserId = integrationPrincipalUserId;
    }


    public BigDecimal getIntegrationPrincipalRatio()
    {
        return integrationPrincipalRatio;
    }


    public void setIntegrationPrincipalRatio(BigDecimal integrationPrincipalRatio)
    {
        this.integrationPrincipalRatio = integrationPrincipalRatio;
    }


	public Integer getIs3In1() {
		return is3In1;
	}


	public void setIs3In1(Integer is3In1) {
		this.is3In1 = is3In1;
	}


	public Integer getShopPoint() {
		return shopPoint;
	}


	public void setShopPoint(Integer shopPoint) {
		this.shopPoint = shopPoint;
	}


	public Integer getLevelId() {
		return levelId;
	}


	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}


	public String getLevelName() {
		return levelName;
	}


	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}


    public String getShopManagerName() {
        return shopManagerName;
    }


    public void setShopManagerName(String shopManagerName) {
        this.shopManagerName = shopManagerName;
    }


    public String getShopManagerIdentityCardNo() {
        return shopManagerIdentityCardNo;
    }


    public void setShopManagerIdentityCardNo(String shopManagerIdentityCardNo) {
        this.shopManagerIdentityCardNo = shopManagerIdentityCardNo;
    }


    public String getShopManagerIdentityCardPic1() {
        return shopManagerIdentityCardPic1;
    }


    public void setShopManagerIdentityCardPic1(String shopManagerIdentityCardPic1) {
        this.shopManagerIdentityCardPic1 = shopManagerIdentityCardPic1;
    }


    public String getShopManagerIdentityCardPic2() {
        return shopManagerIdentityCardPic2;
    }

    public void setShopManagerIdentityCardPic2(String shopManagerIdentityCardPic2) {
        this.shopManagerIdentityCardPic2 = shopManagerIdentityCardPic2;
    }


	public String getShopServeUserMobile() {
		return shopServeUserMobile;
	}


	public void setShopServeUserMobile(String shopServeUserMobile) {
		this.shopServeUserMobile = shopServeUserMobile;
	}


	public Date getBusinessLicenseValidFrom() {
		return businessLicenseValidFrom;
	}


	public void setBusinessLicenseValidFrom(Date businessLicenseValidFrom) {
		this.businessLicenseValidFrom = businessLicenseValidFrom;
	}


	public Date getBusinessLicenseValidTo() {
		return businessLicenseValidTo;
	}


	public void setBusinessLicenseValidTo(Date businessLicenseValidTo) {
		this.businessLicenseValidTo = businessLicenseValidTo;
	}


	public Date getBusinessCertificateValidTo() {
		return businessCertificateValidTo;
	}


	public void setBusinessCertificateValidTo(Date businessCertificateValidTo) {
		this.businessCertificateValidTo = businessCertificateValidTo;
	}


	public Integer getShopManagerIsCorporate() {
		return shopManagerIsCorporate;
	}


	public void setShopManagerIsCorporate(Integer shopManagerIsCorporate) {
		this.shopManagerIsCorporate = shopManagerIsCorporate;
	}


	public Integer getChainStoresType() {
		return chainStoresType;
	}


	public void setChainStoresType(Integer chainStoresType) {
		this.chainStoresType = chainStoresType;
	}


	public Long getHeadShopId() {
		return headShopId;
	}


	public void setHeadShopId(Long headShopId) {
		this.headShopId = headShopId;
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
   