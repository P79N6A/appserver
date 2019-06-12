/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.permission.RoleDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年6月3日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.permission;

import java.io.Serializable;

public class RoleDto implements Serializable {

	private static final long serialVersionUID = 1650227537823316016L;
	
/*	  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '管理员角色主键',
	  `rolename` varchar(50) NOT NULL COMMENT '管理员角色名称',
	  `role_type` tinyint(3) unsigned DEFAULT NULL COMMENT 'O2O后台管理=1，店铺后台管理=2，收银=3，收单=4',
	  `shop_id` int(10) unsigned DEFAULT NULL COMMENT '商铺ID',
	  `role_desc` varchar(200) DEFAULT NULL COMMENT '角色说明',
	  `industry_type` tinyint(2) DEFAULT '0' COMMENT '行业类型:通用=0，便利店=1，餐饮=2',
	  `sequence` int(10) DEFAULT NULL COMMENT '排序',
	  `userstate` int(2) NOT NULL DEFAULT '1' COMMENT '管理员角色是否禁用，''1''代表正常，''2''代表禁用',
*/
	
	private Long id;
	private String roleName;
	private Integer roleType;
	private Long shopId;
	private String roleDesc;
	private Integer industryType;
	private Integer sequence;
	private Integer userstate;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Integer getRoleType() {
		return roleType;
	}
	public void setRoleType(Integer roleType) {
		this.roleType = roleType;
	}
	public Long getShopId() {
		return shopId;
	}
	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
	public Integer getIndustryType() {
		return industryType;
	}
	public void setIndustryType(Integer industryType) {
		this.industryType = industryType;
	}
	public Integer getSequence() {
		return sequence;
	}
	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}
	public Integer getUserstate() {
		return userstate;
	}
	public void setUserstate(Integer userstate) {
		this.userstate = userstate;
	}
	
}
