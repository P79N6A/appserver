package com.idcq.idianmgr.dto.shop;

import java.io.Serializable;
import java.util.Date;
/**
 * 新增收银员参数接收实体类
 * @author nie_jq
 * @date 2015-11-17
 *
 */
public class ShopCashierParams implements Serializable{
	private static final long serialVersionUID = 7446928484969444464L;
	private Long shopId;
	private Long cashierId;
	private String loginName;
	private String password;
	private String clearPwd;
	private String name;
	private String mobile;
    private Long userId;
    private Date checkTime;
    private Integer isCheck;
    private Integer generatePwdMode;//生成密码方式：0-手动添加（password 必填）； 1-短信生成（veriCode 必填）；
	
	public String getClearPwd() {
		return clearPwd;
	}
	public void setClearPwd(String clearPwd) {
		this.clearPwd = clearPwd;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public Long getCashierId() {
		return cashierId;
	}
	public void setCashierId(Long cashierId) {
		this.cashierId = cashierId;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
	}
	public Integer getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	public Integer getGeneratePwdMode() {
		return generatePwdMode;
	}
	public void setGeneratePwdMode(Integer generatePwdMode) {
		this.generatePwdMode = generatePwdMode;
	}
	
}
