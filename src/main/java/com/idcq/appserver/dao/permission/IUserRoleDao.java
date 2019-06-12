package com.idcq.appserver.dao.permission;

import com.idcq.appserver.dto.permission.UserRoleDto;

public interface IUserRoleDao {
	
	/**
	 * 插入用户角色
	 * 
	 * @Function: com.idcq.appserver.dao.permission.IUserRoleDao.insertUserRole
	 * @Description:
	 *
	 * @param userRoleDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月6日 上午11:30:53
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月6日    ChenYongxin      v1.0.0         create
	 */
	Integer insertUserRole(UserRoleDto userRoleDto) throws Exception;
}
