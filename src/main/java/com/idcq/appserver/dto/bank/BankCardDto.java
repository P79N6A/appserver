package com.idcq.appserver.dto.bank;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 银行卡dto
 * 
 * @author Administrator
 * 
 * @date 2015年3月11日
 * @time 下午8:21:48
 */
public class BankCardDto implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 4199542401559834896L;
	private Long bankCardId;
	@JsonIgnore
    private Long userId;		//关联会员ID
    private String name;		//户名
	@JsonIgnore
    private String idNum;		//身份证
    private String bankName;	//银行名称
    private String cardNumber;	//卡号
    @JsonIgnore
    private String phone;		//手机
    @JsonProperty(value="createTime")
//    @DateTimeFormat(pattern="yyyyMMddHHmmss")
    private Date time;		//添加时间`																		
	private Date lastUseTime;//最后使用时间
	private String bankLogoUrl;
	@JsonIgnore
	private String identityType;
	
	@JsonIgnore
	private Integer accountType;  // 账号类型:用户账号=1,商铺账号=2
	private String bankSubbranchName;
	
	
	
	public BankCardDto() {
		super();
	}
	
	
	public Integer getAccountType() {
		return accountType;
	}


	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}


	public Long getBankCardId() {
		return bankCardId;
	}
	public void setBankCardId(Long bankCardId) {
		this.bankCardId = bankCardId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIdNum() {
		return idNum;
	}
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public Date getLastUseTime() {
		return lastUseTime;
	}
	public void setLastUseTime(Date lastUseTime) {
		this.lastUseTime = lastUseTime;
	}
	public String getBankLogoUrl() {
		return bankLogoUrl;
	}
	public void setBankLogoUrl(String bankLogoUrl) {
		this.bankLogoUrl = bankLogoUrl;
	}
	public String getIdentityType() {
		return identityType;
	}
	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}


	public String getBankSubbranchName() {
		return bankSubbranchName;
	}


	public void setBankSubbranchName(String bankSubbranchName) {
		this.bankSubbranchName = bankSubbranchName;
	}
	
}