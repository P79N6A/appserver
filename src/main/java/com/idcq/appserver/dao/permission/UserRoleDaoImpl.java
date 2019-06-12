package com.idcq.appserver.dao.permission;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.permission.UserRoleDto;

@Repository
public class UserRoleDaoImpl extends BaseDao<UserRoleDto> implements IUserRoleDao {

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.permission.IUserRoleDao#insertUserRole(com.idcq.appserver.dto.permission.UserRoleDto)
	 */
	@Override
	public Integer insertUserRole(UserRoleDto userRoleDto) throws Exception {
		
		return super.insert("insertUserRole", userRoleDto);
		
	}


}
