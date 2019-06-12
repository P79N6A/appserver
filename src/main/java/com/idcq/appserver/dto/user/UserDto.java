package com.idcq.appserver.dto.user;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.idcq.appserver.utils.MyDateSerializer;

/**
 * 用户(会员)dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 上午10:49:20
 */
public class UserDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4447550589523390537L;
	private Long userId;
	@JsonIgnore
    private Integer userType;	//用户类型
    @JsonIgnore
    private Integer userType2;
    private Integer isMember;
    public Integer getIsMember() {
		return isMember;
	}
	public void setIsMember(Integer isMember) {
		this.isMember = isMember;
	}
	@JsonIgnore
    private Integer status;		//用户状态
    @JsonIgnore
    private String statusDesc;	//状态描述
    @JsonIgnore
    private String userName;	//用户名
    private String nikeName;   //昵称
    @JsonProperty(value="idCard")
    private String identityCardNo;//身份证号
    private String mobile;
    @JsonIgnore
    private String password;	
    @JsonProperty(value="name")
    private String trueName;	//真实姓名
    @JsonIgnore
    private Long referUserId;//推荐人ID
    @JsonIgnore
    private String referMobile;//推荐人手机号
    @JsonIgnore
    private Long referShopId;//推荐人商铺ID
    @JsonIgnore
    private Integer referType;	//
    private Integer sex;		
    @JsonIgnore
    private Date createTime;	//开户时间
    @JsonIgnore
    private Date firstLoginTime;//第一次登录时间
    @JsonIgnore
    private Date lastUpdateTime;//最后更新时间
    @JsonProperty(value="imgBig")
    private String bigLogo;		
    @JsonProperty(value="imgSmall")
    private String smallLogo;	
    @JsonIgnore
    private String payPassword;	//支付密码
    @JsonIgnore
    private String withdrawPassword;//体现密码
    @JsonIgnore
    private String email;
    @JsonIgnore
    private String weixinNo;	//微信号
    @JsonIgnore
    private String qqNo;		//QQ
    @JsonIgnore
    private String type;		//会员注册方式
    
    /*--------20150321表更新追加字段-------------*/
    @JsonIgnore
    private Integer identityCardPic1;	//身份证正面照片
    @JsonIgnore
    private Integer identityCardPic2;	//身份证方面照片
    @JsonIgnore
    private String registerType;		//注册方式
    private Long provinceId;			//省ID
    private Long cityId;				//市ID
    private Long districtId;			//区县ID
    private Long townId;				//镇，街道ID
    @JsonIgnore
    private String residentTown;   	//用户常住地址	
    /*---------------------*/
    @JsonIgnore
    private String confPassword;
    @JsonIgnore
    private String newMobile;
    @JsonIgnore
    private String address;  
    @JsonIgnore
    private String bankCard;
    @JsonIgnore
    private String newPayPwd;
    @JsonIgnore
    private String confNewPayPwd;
    @JsonSerialize(using = MyDateSerializer.class)
    private Date birthday;
    private String provinceName; 
    private String cityName;
    private String districtName;
    private String townName;
    @JsonIgnore
    private String model;
    @JsonIgnore
    private String osInfo;
    @JsonIgnore
    private String sn;
    @JsonIgnore
    private String regId;
    @JsonIgnore
    private String oldPassWord;
    @JsonIgnore
    private Date lastLoginTime;
    @JsonIgnore
    private String veriCode;
    @JsonIgnore
    private String usage;

	/**
	 * 返点等级:normal_ratio=普通消费者返点,initial_ratio=初级经销商返点,middle_ratio=中级经销商返点(包括代理)
	 */
	@JsonIgnore
	private String rebatesLevel;
	
	@JsonIgnore
	private Integer isDistributor;
	@JsonIgnore
	private Integer isShopManager;
	
	@JsonIgnore
	private Long referUserIdOld;


	@JsonIgnore
	private Boolean isGroupLeader;
	@JsonIgnore
	private Integer referAgentNum;
	@JsonIgnore
	private Integer referAgentNum2;

	public Boolean getIsGroupLeader()
	{
		return isGroupLeader;
	}

	public void setIsGroupLeader(Boolean groupLeader)
	{
		isGroupLeader = groupLeader;
	}

	public Integer getReferAgentNum()
	{
		return referAgentNum;
	}

	public void setReferAgentNum(Integer referAgentNum)
	{
		this.referAgentNum = referAgentNum;
	}

	public Integer getReferAgentNum2()
	{
		return referAgentNum2;
	}

	public void setReferAgentNum2(Integer referAgentNum2)
	{
		this.referAgentNum2 = referAgentNum2;
	}

	public Integer getIsShopManager()
	{
		return isShopManager;
	}

	public void setIsShopManager(Integer isShopManager)
	{
		this.isShopManager = isShopManager;
	}

	public String getRebatesLevel()
	{
		return rebatesLevel;
	}

	public void setRebatesLevel(String rebatesLevel)
	{
		this.rebatesLevel = rebatesLevel;
	}

	public UserDto() {
		super();
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getStatusDesc() {
		return statusDesc;
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNikeName() {
		return nikeName;
	}
	public void setNikeName(String nikeName) {
		this.nikeName = nikeName;
	}
	public String getIdentityCardNo() {
		return identityCardNo;
	}
	public void setIdentityCardNo(String identityCardNo) {
		this.identityCardNo = identityCardNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public Long getReferUserId() {
		return referUserId;
	}
	public void setReferUserId(Long referUserId) {
		this.referUserId = referUserId;
	}
	public String getReferMobile() {
		return referMobile;
	}
	public void setReferMobile(String referMobile) {
		this.referMobile = referMobile;
	}
	public Long getReferShopId() {
		return referShopId;
	}
	public void setReferShopId(Long referShopId) {
		this.referShopId = referShopId;
	}
	public Integer getReferType() {
		return referType;
	}
	public void setReferType(Integer referType) {
		this.referType = referType;
	}
	
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
	public Integer getUserType2() {
		return userType2;
	}
	public void setUserType2(Integer userType2) {
		this.userType2 = userType2;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getFirstLoginTime() {
		return firstLoginTime;
	}
	public void setFirstLoginTime(Date firstLoginTime) {
		this.firstLoginTime = firstLoginTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
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
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}
	public String getWithdrawPassword() {
		return withdrawPassword;
	}
	public void setWithdrawPassword(String withdrawPassword) {
		this.withdrawPassword = withdrawPassword;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWeixinNo() {
		return weixinNo;
	}
	public void setWeixinNo(String weixinNo) {
		this.weixinNo = weixinNo;
	}
	public String getQqNo() {
		return qqNo;
	}
	public void setQqNo(String qqNo) {
		this.qqNo = qqNo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public String getNewMobile() {
		return newMobile;
	}
	public void setNewMobile(String newMobile) {
		this.newMobile = newMobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getNewPayPwd() {
		return newPayPwd;
	}
	public void setNewPayPwd(String newPayPwd) {
		this.newPayPwd = newPayPwd;
	}
	
	public String getConfPassword() {
		return confPassword;
	}
	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}
	public String getConfNewPayPwd() {
		return confNewPayPwd;
	}
	public void setConfNewPayPwd(String confNewPayPwd) {
		this.confNewPayPwd = confNewPayPwd;
	}
	public Integer getIdentityCardPic1() {
		return identityCardPic1;
	}
	public void setIdentityCardPic1(Integer identityCardPic1) {
		this.identityCardPic1 = identityCardPic1;
	}
	public Integer getIdentityCardPic2() {
		return identityCardPic2;
	}
	public void setIdentityCardPic2(Integer identityCardPic2) {
		this.identityCardPic2 = identityCardPic2;
	}
	public String getRegisterType() {
		return registerType;
	}
	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}
	public Long getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
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
	public Long getTownId() {
		return townId;
	}
	public void setTownId(Long townId) {
		this.townId = townId;
	}
	public String getResidentTown() {
		return residentTown;
	}
	public void setResidentTown(String residentTown) {
		this.residentTown = residentTown;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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
	public String getDistrictName() {
		return districtName;
	}
	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
	public String getTownName() {
		return townName;
	}
	public void setTownName(String townName) {
		this.townName = townName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOsInfo() {
		return osInfo;
	}
	public void setOsInfo(String osInfo) {
		this.osInfo = osInfo;
	}
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getOldPassWord() {
		return oldPassWord;
	}
	public void setOldPassWord(String oldPassWord) {
		this.oldPassWord = oldPassWord;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
    public String getVeriCode() {
        return veriCode;
    }
    public void setVeriCode(String veriCode) {
        this.veriCode = veriCode;
    }
    public String getUsage() {
        return usage;
    }
    public void setUsage(String usage) {
        this.usage = usage;
    }
    public Integer getIsDistributor() {
        return isDistributor;
    }
    public void setIsDistributor(Integer isDistributor) {
        this.isDistributor = isDistributor;
    }
    public Long getReferUserIdOld() {
        return referUserIdOld;
    }
    public void setReferUserIdOld(Long referUserIdOld) {
        this.referUserIdOld = referUserIdOld;
    }
}