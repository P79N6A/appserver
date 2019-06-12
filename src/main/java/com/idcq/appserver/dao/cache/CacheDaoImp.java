package com.idcq.appserver.dao.cache;

import java.io.Serializable;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.utils.jedis.DataCacheApi;
/**
 * 缓存数据访问层
 * @ClassName: CacheDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年8月26日 上午10:30:34 
 *
 */
@Repository
public class CacheDaoImp extends BaseDao<Object> implements ICacheDao{
	
	/**
	 * 通用获取缓存的方法
	 * @Title: getEntityCacheByKey 
	 * @param @param key     缓存的key
	 * @param @param sqlId   执行的sqlId
	 * @param @param params  sql执行的参数
	 * @param @param seconds 失效时间
	 * @param @return
	 * @return Object    返回类型 
	 * @throws
	 */
	public Object getEntityCacheByKey(String key,String sqlId,Map<String,Object>params,int seconds) {
		return getObjectCacheByKey(key, sqlId, params, seconds, false);
	}

	@Override
	public Object getEntityListCacheByKey(String key, String sqlId,
			Map<String, Object> params, int seconds) {
		return getObjectCacheByKey(key, sqlId, params, seconds, true);
	}
	
	@Override
	public Object getEntityCacheByClass(Class<?> clazz, Serializable primaryKeyId,
			int seconds) {
		if (null == clazz || null == primaryKeyId ) {
			return null;
		}
		String key = clazz.getSimpleName() + CommonConst.KEY_SEPARATOR_COLON + primaryKeyId;
		String sqlId = clazz.getName() + CommonConst.CACHE_STATEMENT_ID;
		Object result=DataCacheApi.getObject(key);
		if(result != null)
		{
			return result;
		}
		
/*		String key = clazz.getSimpleName() + CommonConst.KEY_SEPARATOR_COLON + primaryKeyId;
		String sqlId = clazz.getName() + CommonConst.CACHE_STATEMENT_ID;
		Object result = null;*/
		
		logger.info("缓存中没有取到数据，改为数据库查询");
		result = (Object)this.selectOne(sqlId, primaryKeyId);
		if(result != null)
		{
			if(seconds != 0)
			{
				DataCacheApi.setObjectEx(key, result, seconds);
			}
			else
			{
				DataCacheApi.setObject(key,result);
			}
		}
		return result;
	}
	
	/**
	 * 获取对象的缓存根据key
	 * @Title: getObjectCacheByKey 
	 * @param @param key
	 * @param @param sqlId
	 * @param @param params
	 * @param @param seconds
	 * @param @param islist
	 * @param @return
	 * @return Object    返回类型 
	 * @throws
	 */
	private Object getObjectCacheByKey(String key,String sqlId,Map<String,Object>params,int seconds,boolean islist)
	{
		Object result=DataCacheApi.getObject(key);
		if(result!=null)
		{
			return result;
		}
		if(islist)
		{
			result=(Object)this.findList(sqlId,params);
		}
		else
		{
			result=(Object)this.selectOne(sqlId,params);
		}
		if(result!=null)
		{
			if(seconds!=0)
			{
				DataCacheApi.setObjectEx(key, result, seconds);
			}
			else
			{
				DataCacheApi.setObject(key,result);
			}
		}
		return result;
	}


}
