package com.idcq.appserver.dao.admin.impl;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.admin.IAdminDao;
import com.idcq.appserver.dto.admin.AdminDto;

@Repository
public class AdminDaoImpl extends BaseDao<AdminDto> 
					implements IAdminDao {
	@Override
	public AdminDto getAdminById(Integer id) throws Exception {
		return (AdminDto) super.selectOne(generateStatement("id"), id);
	}
	
	@Override
	public Integer getRoleIdByName(String roleName) throws Exception {
		return (Integer) super.selectOne(generateStatement("getRoleIdByName"), roleName);
	}
}
