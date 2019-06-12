package com.idcq.appserver.utils.jedis;

import java.io.Serializable;
import java.util.Map;

import com.idcq.appserver.dao.cache.ICacheDao;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 基类service类
 * @ClassName: BaseService 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年8月26日 上午9:45:24 
 * 
 */
public class HandleCacheUtil {
	
    static ICacheDao cacheDao = BeanFactory.getBean(ICacheDao.class);
	/**
	 * 获取对象根据参数
	 * @Title: getEntityCacheByKey 
	 * @param @param key
	 * @param @param sqlId
	 * @param @param params
	 * @param @return
	 * @return Object    返回类型 
	 * @throws
	 */
	public static Object getEntityCacheByKey(String key, String sqlId, Map<String,Object> params, int seconds)throws Exception
	{
		return cacheDao.getEntityCacheByKey(key, sqlId, params, seconds);
	}
	
	/**
	 * 获取对象列表的缓存
	 * @Title: getEntityListCacheByKey 
	 * @param @param key
	 * @param @param sqlId
	 * @param @param params
	 * @param @param seconds
	 * @param @return
	 * @param @throws Exception
	 * @return Object    返回类型 
	 * @throws
	 */
	public static Object getEntityListCacheByKey(String key, String sqlId, Map<String,Object> params, int seconds)throws Exception
	{
		return cacheDao.getEntityListCacheByKey(key, sqlId, params, seconds);
	}
	
	/**
	 * 
	 * 
	 * @Function: com.idcq.appserver.utils.jedis.HandleCacheUtil.getEntityCacheByClass
	 * @Description:
	 *
	 * @param clazz  缓存实体对象的类
	 * @param primaryKeyId 对应表的主键id
	 * @param seconds 缓存失效时间 0为不设置失效时间
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月26日 下午3:54:29
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月26日    shengzhipeng       v1.0.0         create
	 */
	public static Object getEntityCacheByClass(Class<?> clazz, Serializable primaryKeyId, int seconds)throws Exception {
		return cacheDao.getEntityCacheByClass(clazz, primaryKeyId, seconds);
	}
	
}
