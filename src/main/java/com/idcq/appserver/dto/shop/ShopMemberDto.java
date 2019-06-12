package com.idcq.appserver.dto.shop;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.CustomDateSerializer;

public class ShopMemberDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5904943922499793752L;

    private Long memberId;
    private Long shopId;
    private Long userId;
    private Long mobile;
    private String name;
    private Integer grade;
    private String memberCardNo;
    private Integer memberStatus;
    private Integer sourceType;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createTime;
    private Integer sex;
    private Integer birthdayYear;
    private String birthdayDate;
    private Long qq;
    private String weixinNo;
    private String weixinId;
    private String address;
    private String referMobile;
    private Double cardMoney;
    private Double chargeMoney;
    private Integer purchaseNum;
    private Double purchaseMoney;
    private Double points;
    private Double totalPoints;
    private String memberDesc;
    private Integer clientSystemType;
    private Integer isUser;//非平台会员-0；平台 会员-1;本店推荐的平台会员-2
    private String tags;
    private Integer shopMemberLevelId;
    private String shopMemberLevelName;
    private Double discount ;
    private Double consumeMinAmount;
    private Double consumeMaxAmout;
    @JsonIgnore
    private Integer isAutoUpgrate;
    
    
    
    
    
/**
	 * @return the shopMemberLevelName
	 */
	public String getShopMemberLevelName() {
		return shopMemberLevelName;
	}

	/**
	 * @param shopMemberLevelName the shopMemberLevelName to set
	 */
	public void setShopMemberLevelName(String shopMemberLevelName) {
		this.shopMemberLevelName = shopMemberLevelName;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

/**
	 * @return the shopMemberLevelId
	 */
	public Integer getShopMemberLevelId() {
		return shopMemberLevelId;
	}

	/**
	 * @param shopMemberLevelId the shopMemberLevelId to set
	 */
	public void setShopMemberLevelId(Integer shopMemberLevelId) {
		this.shopMemberLevelId = shopMemberLevelId;
	}

	//    @JsonIgnore
    private String veriCode;
//    @JsonIgnore
    private Integer refeType;
    
    /**
     * 优惠券数
     */
    private Integer userShopCouponNum;
    
    /**
     * 未使用优惠券数
     */
    private Integer unUsedShopCouponNum;
    
    /**
     * 已使用优惠券数
     */
    private Integer usedShopCouponNum;
    
    /**
     * 过期优惠券数
     */
    private Integer expireShopCouponNum;
    
    /**
     * 是否已发送生日短信：1-已发送；0-未发送
     */
    private Integer isSendBirthdaySms;
    
    
    public Integer getUserShopCouponNum() {
		return userShopCouponNum;
	}

	public void setUserShopCouponNum(Integer userShopCouponNum) {
		this.userShopCouponNum = userShopCouponNum;
	}

	public Integer getUnUsedShopCouponNum() {
		return unUsedShopCouponNum;
	}

	public void setUnUsedShopCouponNum(Integer unUsedShopCouponNum) {
		this.unUsedShopCouponNum = unUsedShopCouponNum;
	}

	public Integer getUsedShopCouponNum() {
		return usedShopCouponNum;
	}

	public void setUsedShopCouponNum(Integer usedShopCouponNum) {
		this.usedShopCouponNum = usedShopCouponNum;
	}

	public Integer getExpireShopCouponNum() {
		return expireShopCouponNum;
	}

	public void setExpireShopCouponNum(Integer expireShopCouponNum) {
		this.expireShopCouponNum = expireShopCouponNum;
	}

	public String getVeriCode() {
		return veriCode;
	}

	public void setVeriCode(String veriCode) {
		this.veriCode = veriCode;
	}

	public Integer getRefeType() {
		return refeType;
	}

	public void setRefeType(Integer refeType) {
		this.refeType = refeType;
	}

	/**
     * 某个店铺的店内会员数目
     */
    @JsonIgnore
    private Integer shopMemberNum;
    
    @JsonIgnore
    private String	token;

	public ShopMemberDto() {
		super();
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
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


	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}

	public String getMemberCardNo() {
		return memberCardNo;
	}

	public void setMemberCardNo(String memberCardNo) {
		this.memberCardNo = memberCardNo;
	}

	public Integer getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(Integer memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getWeixinId() {
		return weixinId;
	}

	public void setWeixinId(String weixinId) {
		this.weixinId = weixinId;
	}

	public Integer getSourceType() {
		return sourceType;
	}

	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Integer getBirthdayYear() {
		return birthdayYear;
	}

	public void setBirthdayYear(Integer birthdayYear) {
		this.birthdayYear = birthdayYear;
	}

	public String getBirthdayDate() {
		return birthdayDate;
	}

	public void setBirthdayDate(String birthdayDate) {
		this.birthdayDate = birthdayDate;
	}

	public Long getQq() {
		return qq;
	}

	public void setQq(Long qq) {
		this.qq = qq;
	}

	public String getWeixinNo() {
		return weixinNo;
	}

	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getReferMobile() {
		return referMobile;
	}

	public void setReferMobile(String referMobile) {
		this.referMobile = referMobile;
	}

	public Double getCardMoney() {
		return cardMoney;
	}

	public void setCardMoney(Double cardMoney) {
		this.cardMoney = cardMoney;
	}

	public Double getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(Double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Integer getPurchaseNum() {
		return purchaseNum;
	}

	public void setPurchaseNum(Integer purchaseNum) {
		this.purchaseNum = purchaseNum;
	}

	public Double getPurchaseMoney() {
		return purchaseMoney;
	}

	public void setPurchaseMoney(Double purchaseMoney) {
		this.purchaseMoney = purchaseMoney;
	}

	public Double getPoints() {
		return points;
	}

	public void setPoints(Double points) {
		this.points = points;
	}

	public Double getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Double totalPoints) {
		this.totalPoints = totalPoints;
	}

	public String getMemberDesc() {
		return memberDesc;
	}

	public void setMemberDesc(String memberDesc) {
		this.memberDesc = memberDesc;
	}

	public Integer getClientSystemType() {
		return clientSystemType;
	}

	public void setClientSystemType(Integer clientSystemType) {
		this.clientSystemType = clientSystemType;
	}

	public Integer getShopMemberNum() {
		return shopMemberNum;
	}

	public void setShopMemberNum(Integer shopMemberNum) {
		this.shopMemberNum = shopMemberNum;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getIsUser() {
		return isUser;
	}

	public void setIsUser(Integer isUser) {
		this.isUser = isUser;
	}

    public String getTags()
    {
        return tags;
    }

    public void setTags(String tags)
    {
        this.tags = tags;
    }

	public Integer getIsSendBirthdaySms() {
		return isSendBirthdaySms;
	}

	public void setIsSendBirthdaySms(Integer isSendBirthdaySms) {
		this.isSendBirthdaySms = isSendBirthdaySms;
	}

	public Double getConsumeMinAmount() {
		return consumeMinAmount;
	}

	public void setConsumeMinAmount(Double consumeMinAmount) {
		this.consumeMinAmount = consumeMinAmount;
	}

	public Double getConsumeMaxAmout() {
		return consumeMaxAmout;
	}

	public void setConsumeMaxAmout(Double consumeMaxAmout) {
		this.consumeMaxAmout = consumeMaxAmout;
	}

	public Integer getIsAutoUpgrate() {
		return isAutoUpgrate;
	}

	public void setIsAutoUpgrate(Integer isAutoUpgrate) {
		this.isAutoUpgrate = isAutoUpgrate;
	}
    
    

}