package com.idcq.appserver.utils;

import java.util.Properties;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.listeners.ContextInitListener;

public class JedisPoolUtils {
	private static JedisPool jedisPool;
	private static ShardedJedisPool shardedJedisPool;
	
    /**
     * 建立连接池 真实环境，一般把配置参数缺抽取出来。
     * @throws Exception 
     * @throws NumberFormatException 
     * 
     */
    private static void createJedisPool() {
    	Properties props=ContextInitListener.REDIS_PROPS;
    	// 建立连接池配置参数
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
      //  config.setMaxTotal(Integer.parseInt(props.getProperty(CommonConst.REDIS_MAX_ACTIVE).trim()));
        config.setMaxActive(Integer.parseInt(props.getProperty(CommonConst.REDIS_MAX_ACTIVE).trim()));
        // 设置最大阻塞时间，记住是毫秒数milliseconds
       // config.setMaxIdle(Integer.parseInt(props.getProperty(CommonConst.REDIS_MAX_WAIT).trim()));
        // 设置空间连接
        config.setMaxIdle(Integer.parseInt(props.getProperty(CommonConst.REDIS_MAX_IDLE).trim()));
        config.setTestOnBorrow(true);
        config.setTestOnReturn(true);
        // 创建连接池
        try {
			jedisPool = new JedisPool(config, props.getProperty(CommonConst.REDIS_IP),Integer.parseInt(props.getProperty(CommonConst.REDIS_PORT)));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        //jedisPool = new JedisPool(config, "192.168.1.149",6379);
    }
    
    /**
     * 在多线程环境同步初始化
     */
    private static synchronized void jedisPoolInit() {
        if (jedisPool == null)
            createJedisPool();
    }

    /**
     * 获取一个jedis 对象
     * 
     * @return
     */
    public static Jedis getJedis() {
        if (jedisPool == null)
        	jedisPoolInit();
        Jedis jedis=null;
        boolean borrowOrOprSuccess = false;
        try {
        	jedis=jedisPool.getResource();
        } catch (Exception e) {
			borrowOrOprSuccess = false;
			if (jedis != null)
				jedisPool.returnBrokenResource(jedis);
			e.printStackTrace();
			return null;
		}finally {
			if (borrowOrOprSuccess)
				jedisPool.returnResource(jedis);
		}
        return jedis;
    }

    /**
     * 归还一个连接
     * 
     * @param jedis
     */
    public static void returnRes(Jedis jedis,boolean error) {
    	if(jedis!=null){
    		if (error){
    			jedisPool.returnBrokenResource(jedis);
    		} else {
    			jedisPool.returnResource(jedis);
    		}
    	}
    }
    
    /**
     * 归还一个连接
     * 
     * @param jedis
     */
    public static void returnRes(Jedis jedis) {
        jedisPool.returnResource(jedis);
    }
    
    /**
     * 归还一个连接
     * 
     * @param shardedJedis
     */
    public static void returnRes(ShardedJedis shardedJedis) {
    	shardedJedisPool.returnResource(shardedJedis);
    }
    
}
