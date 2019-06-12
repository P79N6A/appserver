package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * 商铺实体类
 * 用来接收请求参数
 * @author Administrator
 *
 */
public class ShopBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long shopId;
	private Long userId;
	private String shopName;
	private String shopkeeper;  // 店主
	private String telephone; // 固定电话，多个以逗号分隔
	private Integer provinceId;
	private Integer cityId;
	private Integer districtId;
	private Integer townId;
	private String address;
	private Integer columnId;
	private String subColumnId; // 所属二级行业ID，多个以逗号分隔
	private Double memberDiscount; // 会员折扣
	private String shopHours; // 营业时间,多个以逗号分隔
	private String shopDesc; 
	private Integer shopLogoId; // 商铺LogoId
	private String shopSettingImgIds; // 商铺环境图ID，多个以逗号分隔，最多支持5张
	private String idCardImgs; //身份证正反面ID，多个以逗号分隔
	private String businessLicenceId; //营业执照图片ID
	private String businessLicenceNo; //营业执照号
	private String identityCardNo;
	private String auditStatus;//审核状态：0-待审核,1-审核通过,2-审核被拒
	private Double longitude;//经度
	private Double latitude;//纬度
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
	private Integer is3In1;//是否三证合一
	private Double percentage;//商铺返点 
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
	 * 是否推荐
	 */
	private Integer isRecommend;	
	
	/**
	 * 推荐人手机号码或userID
	 */
	private String referMobileOrUserId;
	/**
	 * 推荐人ID
	 */
    private Long referUserId;
	
	/**
	 * 推荐人用户类型：会员-0，店铺管理者-10,经销商-20,代理商-30
	 */
    private Integer referUserType;
    
    private String shopManagerIdentityCardNo;
	
    private String shopManagerIdentityCardPic1;
    
    private String shopManagerIdentityCardPic2;
    
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
	
	private Long shopServerUserId;
	/**
	 *购买V产品金额
	 */
	@JsonIgnore
	private Double buyvMoney;

	/**
	 *购买V产品类型
	 0：一点结算
	 1：一点管家
	 2：单屏收银机
	 3：双屏收银机
	 */
//	@JsonIgnore
	private Integer buyvType;

	/**
	 * 店铺经营者是否为法人：1=是,0=否
	 */
	private Integer shopManagerIsCorporate;
	
	/**
	 * 店铺身份：0=其他,1=绿店(全返店),2=红店(折扣店)
	 */
	private Integer shopIdentity;
	
	/**
	 * 最后修改店铺身份日期
	 */
	private String lastChangeIdentityDate;
    
	public String getBusinessLicenceId() {
		return businessLicenceId;
	}
	public void setBusinessLicenceId(String businessLicenceId) {
		this.businessLicenceId = businessLicenceId;
	}
	public String getBusinessLicenceNo() {
		return businessLicenceNo;
	}
	public void setBusinessLicenceNo(String businessLicenceNo) {
		this.businessLicenceNo = businessLicenceNo;
	}
	public String getIdCardImgs() {
		return idCardImgs;
	}
	public void setIdCardImgs(String idCardImgs) {
		this.idCardImgs = idCardImgs;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getShopkeeper() {
		return shopkeeper;
	}
	public void setShopkeeper(String shopkeeper) {
		this.shopkeeper = shopkeeper;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
        if(provinceId == null || provinceId == 0)
	    {
	        provinceId = null;
	    }
		this.provinceId = provinceId;
	}
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
	    if(cityId == null || cityId == 0)
        {
	        cityId = null;
        }
		this.cityId = cityId;
	}
	public Integer getDistrictId() {
		return districtId;
	}
	public void setDistrictId(Integer districtId) {
	    if(districtId == null || districtId == 0)
        {
	        districtId = null;
        }
		this.districtId = districtId;
	}
	public Integer getTownId() {
		return townId;
	}
	public void setTownId(Integer townId) {
	    if(townId == null || townId == 0)
        {
	        townId = null;
        }
		this.townId = townId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getColumnId() {
		return columnId;
	}
	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}
	public String getSubColumnId() {
		return subColumnId;
	}
	public void setSubColumnId(String subColumnId) {
		this.subColumnId = subColumnId;
	}
	public Double getMemberDiscount() {
		return memberDiscount;
	}
	public void setMemberDiscount(Double memberDiscount) {
		this.memberDiscount = memberDiscount;
	}
	public String getShopHours() {
		return shopHours;
	}
	public void setShopHours(String shopHours) {
		this.shopHours = shopHours;
	}
	public String getShopDesc() {
		return shopDesc;
	}
	public void setShopDesc(String shopDesc) {
		this.shopDesc = shopDesc;
	}
	public Double getBuyvMoney() {
		return buyvMoney;
	}
	public void setBuyvMoney(Double buyvMoney) {
		this.buyvMoney = buyvMoney;
	}
	public Integer getBuyvType() {
		return buyvType;
	}
	public void setBuyvType(Integer buyvType) {
		this.buyvType = buyvType;
	}
	public Integer getShopLogoId() {
		return shopLogoId;
	}
	public void setShopLogoId(Integer shopLogoId) {
		this.shopLogoId = shopLogoId;
	}
	public String getShopSettingImgIds() {
		return shopSettingImgIds;
	}
	public void setShopSettingImgIds(String shopSettingImgIds) {
		this.shopSettingImgIds = shopSettingImgIds;
	}
	public String getIdentityCardNo() {
		return identityCardNo;
	}
	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
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
	public String getReferMobileOrUserId() {
		return referMobileOrUserId;
	}
	public void setReferMobileOrUserId(String referMobileOrUserId) {
		this.referMobileOrUserId = referMobileOrUserId;
	}
	public Long getReferUserId() {
		return referUserId;
	}
	public void setReferUserId(Long referUserId) {
		this.referUserId = referUserId;
	}
	public Integer getReferUserType() {
		return referUserType;
	}
	public void setReferUserType(Integer referUserType) {
		this.referUserType = referUserType;
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
	public Integer getIs3In1() {
		return is3In1;
	}
	public void setIs3In1(Integer is3In1) {
		this.is3In1 = is3In1;
	}
	public Double getPercentage() {
		return percentage;
	}
	public void setPercentage(Double percentage) {
		this.percentage = percentage;
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
    public Integer getIsRecommend() {
        return isRecommend;
    }
    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
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
	public String getShopManagerIdentityCardNo() {
		return shopManagerIdentityCardNo;
	}
	public void setShopManagerIdentityCardNo(String shopManagerIdentityCardNo) {
		this.shopManagerIdentityCardNo = shopManagerIdentityCardNo;
	}
	public Date getBusinessCertificateValidTo() {
		return businessCertificateValidTo;
	}
	public void setBusinessCertificateValidTo(Date businessCertificateValidTo) {
		this.businessCertificateValidTo = businessCertificateValidTo;
	}
	public Long getShopServerUserId() {
		return shopServerUserId;
	}
	public void setShopServerUserId(Long shopServerUserId) {
		this.shopServerUserId = shopServerUserId;
	}
	public Integer getShopManagerIsCorporate() {
		return shopManagerIsCorporate;
	}
	public void setShopManagerIsCorporate(Integer shopManagerIsCorporate) {
		this.shopManagerIsCorporate = shopManagerIsCorporate;
	}
	public Integer getShopIdentity() {
		return shopIdentity;
	}
	public void setShopIdentity(Integer shopIdentity) {
		this.shopIdentity = shopIdentity;
	}
	public String getLastChangeIdentityDate() {
		return lastChangeIdentityDate;
	}
	public void setLastChangeIdentityDate(String lastChangeIdentityDate) {
		this.lastChangeIdentityDate = lastChangeIdentityDate;
	}
	
	
	
}
