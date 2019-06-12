package com.idcq.appserver.dao.permission;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.permission.RoleDto;

@Repository
public class RoleDaoImpl extends BaseDao<RoleDto> implements IRoleDao {

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.permission.IRoleDao#getRole(com.idcq.appserver.dto.plugin.PluginModel, com.idcq.appserver.dto.common.Page)
	 */
	@Override
	public List<RoleDto> getRole(RoleDto roleDto, PageModel page) {
		
		Map<String,Object> parms  = new HashMap<String,Object>();
		parms.put("limit", (page.getToPage()-1)*page.getPageSize());
		parms.put("pSize",page.getPageSize());
		parms.put("role",roleDto);
		
		return findList("getRole", parms);
	}


}
