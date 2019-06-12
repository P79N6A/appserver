package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.utils.MyTimeSerializer;

/**
 * 商铺详情dto
 *
 * @author Administrator
 *
 * @date 2015年3月24日
 * @time 下午2:13:18
 */
public class ShopDetailDto implements Serializable
{

    /**
	 *
	 */
    private static final long serialVersionUID = 439727431981740738L;





    private String shopName;

    private String shopLogoUrl; // 商铺logo

    private Double starLevelGrade; // 星级评分

    private Double serviceGrade; // 服务评分

    private Double envGrade; // 环境评分
    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonSerialize(using=MyTimeSerializer.class)
    private Date startBTime; // 开始营业时间
    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonSerialize(using=MyTimeSerializer.class)
    private Date stopBTime; // 结束营业时间

    private String address;

    private String telephone;

    private String shopInfrastructure; // 商铺基础设施

    private Double longitude; // 经度

    private Double latitude; // 纬度

    /*--------------------*/
    private Integer soldNumber; // 销售次数/下订单数量

    private Integer timedDiscountFlag; // 是否支持限时折扣

    private Integer redPacketFlag;

    private Integer cashCouponFlag;

    private Integer couponFlag;

    private Double memberDiscount;

    /*-----------追加20150512------------*/
    private Integer takeoutFlag; // 是否启用外卖

    private Integer bookFlag; // 是否启用预定

    /*-----------追加20150518------------*/
    private String shopMode; // 商铺模式

    private Integer districtId;

    private String districtName;

    private String shopkeeper; // 店主名称

    private String shopDesc;

    private String shopSettingImgs; // 店铺环境图

    private String shopSettingIds; // 店铺环境图ID

    private String businessLicenceId; // 营业执照图片ID

    private String businessLicenceNo; // 营业执照号

    private String businessLicencePicUrl;// 营业执照图片URL地址

    private String shopHours;

    private Integer serverMode;

    private Integer columnId;

    private String subColumnId;

    private Double distance;// 距离

    /*-----------追加20160216------------*/
    private String shopIndustryName; // 店铺行业

    /* 追加20160315 */
    private Integer businessAreaActivityFlag;// 商圈活动标识

    // 商圈活动名称
    private String activityRuleName;

    /*------追加20160422--------*/
    private Integer provinceId;// 省份ID
    private String provinceName;

    private Long cityId;// 城市ID
    private String cityName;

    private Integer townId;// 乡镇Id

    private String TownName;

    private String identityCardNo;// 身份证号

    private Long identityCardPicId;// 身份证正面图片ID

    private String identityCardPicUrl;// 身份证正面图片url

    private Long identityCardNextPicId;// 身份证背面图片id

    private String identityCardNextPicUrl;// 身份证背面图片url

    /******************新增20160505**************************/
	private Integer shopClassify;//商户类型:1-个人商户(默认值)；2-个体商户；3-企业商户
	private String organizationCode;//组织机构代码
	private Integer organizationCodePicId;
	private String organizationCodePic;//组织机构代码证图片
	private String taxRegistrationCertificate;// 税务登记证号
	private Integer taxRegistrationCertificatePicId;
	private String taxRegistrationCertificatePic;//税务登记证图片
	private String businessCertificates;//经营许可证号,可能有多个
	private String businessCertificatePicIds;
	private String businessCertificatePics;//经营许可证图片，可能有多个
	private Integer authorizationPicId;
	private String authorizationPic;//授权书图片
	private String principalIdentityCardNo;//法人身份号',
	private Integer principalIdentityCardPicId1;//法人身份证正面图片Id
	private String principalIdentityCardPicUrl1;//法人身份证正面图片
	private Integer principalIdentityCardPicId2;//法人身份证反面图片Id
	private String principalIdentityCardPicUrl2;//法人身份证正面图片
	private String skillsCertificateNos;//个人技能编号，可能多个
	private String skillsCertificatePicIds;
	private String skillsCertificatePics;//个人技能证图片可能多个
	private Integer is3In1;
	private Double percentage;
	private Integer shopLogoId;//图片logo
	
	private Long principalId;

    /**
     * 推荐人手机号码或userID
     */
    private String referMobileOrUserId;
    /**
     * 店铺服务人员ID
     */
	private Long shopServerUserId;
	/**
	 * 店铺服务人员名
	 */
	private String shopServerUserName;

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
     *0=其他,1=绿店,2=红店
     */
    private Integer shopIdentity;

    /**
     *购买V产品金额
     */
    private Double buyvMoney;

    /**
     * 购买V产品类型
     0：一点结算
     1：一点管家
     2：单屏收银机
     3：双屏收银机
     */
    private Integer buyvType;

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

    public String getBusinessLicenceId()
    {
        return businessLicenceId;
    }

    public Double getDistance()
    {
        return distance;
    }

    public void setDistance(Double distance)
    {
        this.distance = distance;
    }

    public void setBusinessLicenceId(String businessLicenceId)
    {
        this.businessLicenceId = businessLicenceId;
    }

    public String getBusinessLicenceNo()
    {
        return businessLicenceNo;
    }

    public void setBusinessLicenceNo(String businessLicenceNo)
    {
        this.businessLicenceNo = businessLicenceNo;
    }

    public ShopDetailDto()
    {
        super();
    }

    public String getShopSettingIds()
    {
        return shopSettingIds;
    }

    public void setShopSettingIds(String shopSettingIds)
    {
        this.shopSettingIds = shopSettingIds;
    }

    public String getShopkeeper()
    {
        return shopkeeper;
    }

    public void setShopkeeper(String shopkeeper)
    {
        this.shopkeeper = shopkeeper;
    }

    public String getShopDesc()
    {
        return shopDesc;
    }

    public void setShopDesc(String shopDesc)
    {
        this.shopDesc = shopDesc;
    }

    public String getShopSettingImgs()
    {
        return shopSettingImgs;
    }

    public void setShopSettingImgs(String shopSettingImgs)
    {
        this.shopSettingImgs = shopSettingImgs;
    }

    public String getShopName()
    {
        return shopName;
    }

    public void setShopName(String shopName)
    {
        this.shopName = shopName;
    }

    public String getShopLogoUrl()
    {
        return shopLogoUrl;
    }

    public void setShopLogoUrl(String shopLogoUrl)
    {
        this.shopLogoUrl = shopLogoUrl;
    }

    public Double getStarLevelGrade()
    {
        return starLevelGrade;
    }

    public void setStarLevelGrade(Double starLevelGrade)
    {
        this.starLevelGrade = starLevelGrade;
    }

    public Double getServiceGrade()
    {
        return serviceGrade;
    }

    public void setServiceGrade(Double serviceGrade)
    {
        this.serviceGrade = serviceGrade;
    }

    public Double getEnvGrade()
    {
        return envGrade;
    }

    public void setEnvGrade(Double envGrade)
    {
        this.envGrade = envGrade;
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

	public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getShopInfrastructure()
    {
        return shopInfrastructure;
    }

    public void setShopInfrastructure(String shopInfrastructure)
    {
        this.shopInfrastructure = shopInfrastructure;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Integer getSoldNumber()
    {
        return soldNumber;
    }

    public void setSoldNumber(Integer soldNumber)
    {
        this.soldNumber = soldNumber;
    }

    public Integer getTimedDiscountFlag()
    {
        return timedDiscountFlag;
    }

    public void setTimedDiscountFlag(Integer timedDiscountFlag)
    {
        this.timedDiscountFlag = timedDiscountFlag;
    }

    public Integer getRedPacketFlag()
    {
        return redPacketFlag;
    }

    public void setRedPacketFlag(Integer redPacketFlag)
    {
        this.redPacketFlag = redPacketFlag;
    }

    public Integer getCashCouponFlag()
    {
        return cashCouponFlag;
    }

    public void setCashCouponFlag(Integer cashCouponFlag)
    {
        this.cashCouponFlag = cashCouponFlag;
    }

    public Integer getCouponFlag()
    {
        return couponFlag;
    }

    public void setCouponFlag(Integer couponFlag)
    {
        this.couponFlag = couponFlag;
    }

    public Double getMemberDiscount()
    {
        return memberDiscount;
    }

    public void setMemberDiscount(Double memberDiscount)
    {
        this.memberDiscount = memberDiscount;
    }

    public Integer getTakeoutFlag()
    {
        return takeoutFlag;
    }

    public void setTakeoutFlag(Integer takeoutFlag)
    {
        this.takeoutFlag = takeoutFlag;
    }

    public Integer getBookFlag()
    {
        return bookFlag;
    }

    public void setBookFlag(Integer bookFlag)
    {
        this.bookFlag = bookFlag;
    }

    public String getShopMode()
    {
        return shopMode;
    }

    public void setShopMode(String shopMode)
    {
        this.shopMode = shopMode;
    }

    public Integer getDistrictId()
    {
        return districtId;
    }

    public void setDistrictId(Integer districtId)
    {
        this.districtId = districtId;
    }

    public String getDistrictName()
    {
        return districtName;
    }

    public void setDistrictName(String districtName)
    {
        this.districtName = districtName;
    }

    public String getShopHours()
    {
        return shopHours;
    }

    public void setShopHours(String shopHours)
    {
        this.shopHours = shopHours;
    }

    public Integer getServerMode()
    {
        return serverMode;
    }

    public void setServerMode(Integer serverMode)
    {
        this.serverMode = serverMode;
    }

    public Integer getColumnId()
    {
        return columnId;
    }

    public void setColumnId(Integer columnId)
    {
        this.columnId = columnId;
    }

    public String getShopIndustryName()
    {
        return shopIndustryName;
    }

    public void setShopIndustryName(String shopIndustryName)
    {
        this.shopIndustryName = shopIndustryName;
    }

    public Integer getBusinessAreaActivityFlag()
    {
        return businessAreaActivityFlag;
    }

    public void setBusinessAreaActivityFlag(Integer businessAreaActivityFlag)
    {
        this.businessAreaActivityFlag = businessAreaActivityFlag;
    }


    public String getActivityRuleName()
    {
        return activityRuleName;
    }

    public void setActivityRuleName(String activityRuleName)
    {
        this.activityRuleName = activityRuleName;
    }

    public Integer getProvinceId()
    {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId)
    {
        this.provinceId = provinceId;
    }


	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Integer getTownId()
    {
        return townId;
    }

    public void setTownId(Integer townId)
    {
        this.townId = townId;
    }

    public String getIdentityCardNo()
    {
        return identityCardNo;
    }

    public Long getIdentityCardPicId()
    {
        return identityCardPicId;
    }

    public void setIdentityCardPicId(Long identityCardPicId)
    {
        this.identityCardPicId = identityCardPicId;
    }

    public Long getIdentityCardNextPicId()
    {
        return identityCardNextPicId;
    }






	public void setIdentityCardNextPicId(Long identityCardNextPicId) {
		this.identityCardNextPicId = identityCardNextPicId;
	}




	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}


	public String getIdentityCardPicUrl() {
		return identityCardPicUrl;
	}




	public void setIdentityCardPicUrl(String identityCardPicUrl) {
		this.identityCardPicUrl = identityCardPicUrl;
	}





	public String getIdentityCardNextPicUrl() {
		return identityCardNextPicUrl;
	}




	public void setIdentityCardNextPicUrl(String identityCardNextPicUrl) {
		this.identityCardNextPicUrl = identityCardNextPicUrl;
	}




	public String getBusinessLicencePicUrl() {
		return businessLicencePicUrl;
	}




	public void setBusinessLicencePicUrl(String businessLicencePicUrl) {
		this.businessLicencePicUrl = businessLicencePicUrl;
	}




	public String getReferMobileOrUserId() {
		return referMobileOrUserId;
	}




	public void setReferMobileOrUserId(String referMobileOrUserId) {
		this.referMobileOrUserId = referMobileOrUserId;
	}




	public Long getShopServerUserId() {
		return shopServerUserId;
	}




	public void setShopServerUserId(Long shopServerUserId) {
		this.shopServerUserId = shopServerUserId;
	}




	public String getShopServerUserName() {
		return shopServerUserName;
	}




	public void setShopServerUserName(String shopServerUserName) {
		this.shopServerUserName = shopServerUserName;
	}

	public String getSubColumnId() {
		return subColumnId;
	}

	public void setSubColumnId(String subColumnId) {
		this.subColumnId = subColumnId;
	}

	public String getTownName() {
		return TownName;
	}

	public void setTownName(String townName) {
		TownName = townName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
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

	public Integer getOrganizationCodePicId() {
		return organizationCodePicId;
	}

	public void setOrganizationCodePicId(Integer organizationCodePicId) {
		this.organizationCodePicId = organizationCodePicId;
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

	public Integer getTaxRegistrationCertificatePicId() {
		return taxRegistrationCertificatePicId;
	}

	public void setTaxRegistrationCertificatePicId(
			Integer taxRegistrationCertificatePicId) {
		this.taxRegistrationCertificatePicId = taxRegistrationCertificatePicId;
	}

	public String getTaxRegistrationCertificatePic() {
		return taxRegistrationCertificatePic;
	}

	public void setTaxRegistrationCertificatePic(
			String taxRegistrationCertificatePic) {
		this.taxRegistrationCertificatePic = taxRegistrationCertificatePic;
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

	public Double getPercentage() {
		return percentage;
	}

	public void setPercentage(Double percentage) {
		this.percentage = percentage;
	}

	public Integer getShopLogoId() {
		return shopLogoId;
	}

	public void setShopLogoId(Integer shopLogoId) {
		this.shopLogoId = shopLogoId;
	}

	public Integer getIs3In1() {
		return is3In1;
	}

	public void setIs3In1(Integer is3In1) {
		this.is3In1 = is3In1;
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

    public Long getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Long principalId) {
        this.principalId = principalId;
    }


}
