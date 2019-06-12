package com.idcq.appserver.dao.permission;

import java.util.List;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.permission.RoleDto;

public interface IRoleDao {
	/**
	 * 获取角色信息
	 * 
	 * @Function: com.idcq.appserver.dao.permission.IRoleDao.getRole
	 * @Description:
	 *
	 * @param roleDto
	 * @param page
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年6月6日 上午11:30:28
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年6月6日    ChenYongxin      v1.0.0         create
	 */
	public List<RoleDto> getRole(RoleDto roleDto, PageModel page);
}
