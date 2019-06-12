package com.idcq.appserver.dto.shop;

import java.io.Serializable;

/**
 * 店铺雇员，json接收实体==》商铺服务人员信息变更接口
 * @author ChenYongxin
 *
 */
public class ShopEmployeeDto implements Serializable{
    /**
	 * shopId	int	是	商铺编号
		token	string	是	设备令牌
		employeeCode	bigint	是	服务人员编码
		userName	string	否	服务人员名称
		operateType	int	是	0：新增  1：修改  2：删除
	 */
	private static final long serialVersionUID = 1943742513942201469L;
	/**
	 * 商铺id
	 */
	private Long shopId;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 令牌
	 */
	private String token;
	/**
	 * 商铺编号
	 */
    private String employeeCode;
    /**
     * 商铺类型
     */
    private Integer operateType;
    /**
     * 服务人员名称
     */
    private String userName;
    /**
     * 用户状态
     */
    private Integer status;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户性别
     */
    private Integer sex;
    /**
     * 技能说明
     */
    private String skill;
    /**
     * 雇员ID
     */
    private Long employeeId;
    
    private Integer isCheck;
    
    private Long userId;
    
    private String mobile;
    
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getSkill() {
		return skill;
	}
	public void setSkill(String skill) {
		this.skill = skill;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public Integer getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
    
    
}