/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.permission.UserRoleDto
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

public class UserRoleDto implements Serializable {

	private static final long serialVersionUID = 3410312762611054502L;
	
/*	  `user_id` int(10) unsigned NOT NULL COMMENT '用户ID',
	  `user_type_id` tinyint(3) unsigned NOT NULL COMMENT '用户类型ID：1（user，user_id对应到1dcq_user表）,2(admin,user_id对应到1dcq_admin),3(employee,user_id对应到1dcq_shop_employee)',
	  `role_id` int(10) unsigned NOT NULL COMMENT '角色ID',*/
	private Long userId;
	private Integer userTypeId;
	private Long roleId;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getUserTypeId() {
		return userTypeId;
	}
	public void setUserTypeId(Integer userTypeId) {
		this.userTypeId = userTypeId;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
