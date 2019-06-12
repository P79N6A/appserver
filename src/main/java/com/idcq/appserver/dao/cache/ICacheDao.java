package com.idcq.appserver.dao.cache;

import java.io.Serializable;
import java.util.Map;


public interface ICacheDao {

	Object getEntityCacheByKey(String key,String sqlId, Map<String,Object>params,int seconds)throws Exception;

	Object getEntityListCacheByKey(String key, String sqlId, Map<String, Object> params, int seconds);
	
	Object getEntityCacheByClass(Class<?> clazz, Serializable primaryKeyId, int seconds);
}
