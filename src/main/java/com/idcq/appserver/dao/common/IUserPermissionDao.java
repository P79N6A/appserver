package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.UserPermission;

public interface IUserPermissionDao {

	List<Map<String, Object>> getUserAuthList(UserPermission userPermission) throws Exception;
}
