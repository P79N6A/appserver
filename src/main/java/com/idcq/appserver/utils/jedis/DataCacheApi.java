package com.idcq.appserver.utils.jedis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import redis.clients.jedis.Jedis;

import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.SerializeUtil;

public class DataCacheApi {
	private static final Logger logger=Logger.getLogger(DataCacheApi.class);
	private static boolean flag=false;
	private static Jedis jedis;
	private static DataCacheApi dataCacheApi;
	@Autowired
//	private static RedisTemplate<Serializable, Serializable> redisTemplate;
	
	private DataCacheApi(){
	}
	
	public static synchronized DataCacheApi getInstance(){
		if(dataCacheApi==null){
			dataCacheApi=new DataCacheApi();
		}
		return dataCacheApi;
	}
	
	public static boolean switchOn(){
		boolean flag=true;
		Properties props=ContextInitListener.REDIS_PROPS;
		String switchStr=props.getProperty("redis.switch")==null ? "off" : props.getProperty("redis.switch");
		if("off".equals(switchStr.toLowerCase())){
			flag=false;
			logger.info("缓存开关为关闭状态，缓存不可用!");
		}
		return flag;
	}
	
	/**
	 * 从Redis获取数据，此方发适合于key、value为String类型
	 * @param key
	 * @return
	 */
	public static String get(final String key){
		String result=null;
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				result = (String) redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection){
						//ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
						//String result=(String) operations.get(key);
						byte[] bytes=connection.get(key.getBytes());
						String result=(String)SerializeUtil.unserialize(bytes);
						return result;
						/*byte[] keyBytes=redisTemplate.getStringSerializer().serialize(key);
					String result=null;
					if(connection.exists(keyBytes)){
						 byte[] value = connection.get(keyBytes);  
						 result = redisTemplate.getStringSerializer().deserialize(value);  
						 return result;
					}
					return null;*/
					}
				});
			} catch (Exception e) {
				logger.error("获取缓存失败："+e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return result;
	}
	
	/**
	 * 缓存Redis数据，此方发适合于key、value为String类型
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean set(final String key,final String value) {
		if(null==value){
			flag=false;
		}else{
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection){
						///ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
						//operations.getAndSet(key, value);
						/*byte[] keyBytes=redisTemplate.getStringSerializer().serialize(key);
						if(exist(key)){
							connection.del(keyBytes);
						}
						connection.set(keyBytes,redisTemplate.getStringSerializer().serialize(value));*/
						
						connection.set(key.getBytes(), SerializeUtil.serialize(value));
						flag=true;
						return flag;
					}
				});
			} catch (Exception e) {
				logger.error("设置缓存失败："+e.getMessage());
				e.printStackTrace();
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * 缓存Redis数据，此方发适合于key为String类型,value为Object
	 * @param key
	 * @param value 
	 * @param seconds 失效时长
	 * @return
	 */
	public static boolean setex(final String key,final String value,final int seconds){
		if(switchOn()){
			if(null==value){
				flag=false;
			}else{
				final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
				try {
					redisTemplate.execute(new RedisCallback<Object>() {
						@Override
						public Object doInRedis(RedisConnection connection){
							//ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
							//operations.getAndSet(key, value);
							//redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
							/*byte[] keyBytes=redisTemplate.getStringSerializer().serialize(key);
						if(exist(key)){
							connection.del(keyBytes);
						}
						connection.setEx(keyBytes,seconds,redisTemplate.getStringSerializer().serialize(value));*/
							connection.setEx(key.getBytes(),seconds, SerializeUtil.serialize(value));
							flag=true;
							return flag;
						}
					});
				} catch (Exception e) {
					logger.error("设置缓存失败："+e.getMessage());
					e.printStackTrace();
					return flag;
				}
			}
		}
		return flag;
    }
	
	/**
	 * 缓存Redis数据，此方发适合于key、value为String类型
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean setObject(final String key,final Object value){
		if(switchOn()){
			if(null==value){
				flag=false;
			}else{
				final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
				try {
					redisTemplate.execute(new RedisCallback<Object>() {
						@Override
						public Object doInRedis(RedisConnection connection){
							//ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
							//if(exists(key)){
							//	redisTemplate.delete(key);
							//}
							//operations.set(key,(Serializable) value);
							connection.set(key.getBytes(), SerializeUtil.serialize(value));
							flag=true;
							return flag;
						}
					});
				} catch (Exception e) {
					logger.error("设置缓存失败："+e.getMessage());
					e.printStackTrace();
					flag=false;
					return flag;
				}
			}
		}
		return flag;
	}
	
	/**
	 * 缓存Redis数据，此方发适合于key为String类型,value为Object
	 * @param key
	 * @param value 
	 * @param seconds 失效时长
	 * @return
	 */
	public static boolean setObjectEx(final String key,final Object value,final int seconds){
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				flag= (boolean) redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Boolean doInRedis(RedisConnection connection){
						//ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
						//operations.set(key, (Serializable) value);
						//operations.set(key, (Serializable) value, seconds,  TimeUnit.SECONDS);
						connection.setEx(key.getBytes(),seconds, SerializeUtil.serialize(value));
						flag=true;
						return flag;
					}
				});
			} catch (Exception e) {
				logger.error("获取缓存失败："+e.getMessage());
				e.printStackTrace();
				flag=false;
				return flag;
			}
		}
		return flag;
    }
	
	/**
	 * 获取Redis数据，此方发适合于key为String类型,value为对象类型
	 * @param key
	 * @param value
	 * @return
	 */
	public static Object getObject(final String key){
		Object obj=null;
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				obj=redisTemplate.execute(new RedisCallback<Object>(){
					@Override
					public Object doInRedis(RedisConnection connection){
						//ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
						//Object obj=operations.get(key);
						byte[] bytes=connection.get(key.getBytes());
						Object object=SerializeUtil.unserialize(bytes);
						return object;
					}
				});
			} catch (Exception e) {
				logger.error("获取缓存失败："+e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return obj;
	}
	
	public static synchronized Long incr(final String key){
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		return (Long) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long result=connection.incr(redisTemplate.getStringSerializer().serialize(key));
				return result;
			}
		});
	}
	
	/**
	 * 获取自增长的值
	 * @param key
	 * @return
	 */
	public static String getInc(final String key){
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		String result=null;
		try {
			result = (String) redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection){
					byte[] keyBytes=redisTemplate.getStringSerializer().serialize(key);
					String result=null;
					if(connection.exists(keyBytes)){
						 byte[] value = connection.get(keyBytes);  
						 result = redisTemplate.getStringSerializer().deserialize(value);  
						 return result;
					}
					return null;
				}
			});
		} catch (Exception e) {
			logger.error("获取缓存失败："+e.getMessage());
			e.printStackTrace();
			return null;
		}
		return result;
	}
	
	/**
	 * 设置自增长的值
	 * @param key
	 * @return
	 */
	public static boolean setInc(final String key,final String value){
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		try {
			flag =  (boolean) redisTemplate.execute(new RedisCallback<Object>() {
				@Override
				public Object doInRedis(RedisConnection connection){
					byte[] keyBytes=redisTemplate.getStringSerializer().serialize(key);
					if(exist(key)){
						connection.del(keyBytes);
					}
					connection.set(keyBytes,redisTemplate.getStringSerializer().serialize(value));
					flag=true;
					return flag;
				}
			});
		} catch (Exception e) {
			logger.error("获取缓存失败："+e.getMessage());
			e.printStackTrace();
			return flag;
		}
		return flag;
	}
	
	/**
	 * hash存取
	 * @param key
	 * @param map
	 * @return
	 */
	public static boolean hmset(final String key, final Map<String,String> map){
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				flag=(boolean) redisTemplate.execute(new RedisCallback<Object>(){
					@Override
					public Boolean doInRedis(RedisConnection connection){
						//ValueOperations<Serializable, Serializable> operations =redisTemplate.opsForValue();
						//Object obj=operations.get(key);
						Set set=map.keySet();
						Iterator it=set.iterator();
						String tempKey=null;
						Map<byte[],byte[]> hash=new HashMap();
						while (it.hasNext()) {
							tempKey=(String) it.next();
							hash.put(tempKey.getBytes(), map.get(tempKey).getBytes());
						}
						connection.hMSet(key.getBytes(), hash);
						return flag;
					}
				});
			} catch (Exception e) {
				logger.error("获取缓存失败："+e.getMessage());
				e.printStackTrace();
				return flag;
			}
		}
		return flag;
    }
	
	/**
	 * hash获取
	 * @param key
	 * @return
	 */
	public static Object hmget(final String key){
		Object obj=null;
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				obj=(Object) redisTemplate.execute(new RedisCallback<Object>(){
					@Override
					public Object doInRedis(RedisConnection connection){
						Map<byte[],byte[]> map=connection.hGetAll(key.getBytes());
						Set<byte[]> set=map.keySet();
						Iterator<byte[]> it=set.iterator();
						byte[] tempKey=null;
						Map<String,String> result=new HashMap<String, String>();
						while (it.hasNext()) {
							tempKey=it.next();
							result.put(new String(tempKey), new String(map.get(tempKey)));
						}
						return result;
					}
				});
			} catch (Exception e) {
				logger.error("获取缓存失败："+e.getMessage());
				e.printStackTrace();
				return null;
			}
		}
		return obj;
    }
	
	
	/**
	 * 根据正则表达式从redis中获取满足条件的key
	 * @param pattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<String> keys(final String pattern){
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		return (List<String>) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Object doInRedis(RedisConnection connection)
					throws DataAccessException {
				//Set set=connection.keys(redisTemplate.getStringSerializer().serialize(pattern));
				//Set set=redisTemplate.keys(pattern);
//				Iterator<Serializable> it=set.iterator();
//				Serializable key=null;
//				StringBuffer result=new StringBuffer();
//				while(it.hasNext()){
//					key=it.next();
//					result.append(redisTemplate.getStringSerializer().deserialize((byte[]) key)+"  ");
//				}
				Set<byte[]> set=connection.keys(pattern.getBytes());
				Iterator<byte[]> it=set.iterator();
				byte[] key=null;
				List<String> resultList=new ArrayList<String>();
				while(it.hasNext()){
					key=it.next();
					resultList.add(new String(key));
				}
				return resultList;
			}
		});
    }
	
	/**
	* 判断缓存中是否有对应的缓存
	* 
	* @param key
	* @return
	*/
	public static boolean exists(final String key) {
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		return redisTemplate.hasKey(key);
	}
	
	public static boolean exist(final String key) {
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		return (boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				boolean result=connection.exists(redisTemplate.getStringSerializer().serialize(key));
				return result;
			}
		});
	}
	
	/**
	 * 根据key删除满足条件的值
	 * @param key
	 * @return
	 */
	public static boolean del(final String key) {
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						//if (exists(key)){
						//	redisTemplate.delete(key);
						//}
						connection.del(key.getBytes());
						flag=true;
						return flag;
					}
				});
			} catch (Exception e) {
				logger.error("删除缓存失败："+e.getMessage());
				e.printStackTrace();
				flag=false;
				return flag;
			}
		}
		return flag;
	}
	
	public static boolean delObject(final String key) {
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection)
							throws DataAccessException {
						//redisTemplate.boundValueOps(key);
						//if (exists(key)){
						//	redisTemplate.delete(key);
						//}
						connection.del(key.getBytes());
						flag=true;
						return flag;
					}
				});
			} catch (Exception e) {
				logger.error("删除缓存失败："+e.getMessage());
				e.printStackTrace();
				flag=false;
				return flag;
			}
		}
		return flag;
	}
	
	/**
	 * 根据正则表达式从redis中删除所有满足条件的key
	 * @param pattern
	 * @return
	 */
	public static boolean delByPattern(final String pattern){
		final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
		return (boolean) redisTemplate.execute(new RedisCallback<Object>() {
			@Override
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				if(switchOn()){
					RedisSerializer<String> s=redisTemplate.getStringSerializer();
					
					Set<byte[]> keys=connection.keys(s.serialize(pattern));
					/*if (keys.size() > 0){
					redisTemplate.delete(keys);
				}*/
					Iterator<byte[]> it=keys.iterator();
					byte[] key=null;
					while(it.hasNext()){
						key=it.next();
						connection.del(key);
					}
				}
				return true;
			}
		});
    }
	
	/**
	 * 刷新缓存
	 * @param key
	 * @return
	 */
	public static boolean flushDb() {
		if(switchOn()){
			final RedisTemplate<Serializable, Serializable> redisTemplate=BeanFactory.getBean(RedisTemplate.class);
			try {
				redisTemplate.execute(new RedisCallback<Object>() {
					@Override
					public Object doInRedis(RedisConnection connection){
						connection.flushDb();
						flag=true;
						return flag;
					}
				});
			} catch (Exception e) {
				logger.error("刷新缓存失败："+e.getMessage());
				e.printStackTrace();
				flag=false;
				return flag;
			}
		}
		return flag;
	}
	
}
