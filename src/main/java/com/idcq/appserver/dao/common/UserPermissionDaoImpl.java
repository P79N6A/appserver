package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.common.UserPermission;

@Repository
public class UserPermissionDaoImpl extends BaseDao<UserPermission> implements IUserPermissionDao {

	@Override
	public List<Map<String, Object>> getUserAuthList(UserPermission userPermission) throws Exception {
		return (List)super.findList(generateStatement("getUserAuthList"),userPermission);
	}
}
