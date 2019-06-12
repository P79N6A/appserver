package com.idcq.appserver.dao.admin;

import com.idcq.appserver.dto.admin.AdminDto;

public interface IAdminDao {

	AdminDto getAdminById(Integer id) throws Exception;
	Integer getRoleIdByName(String roleName) throws Exception;
}
