package com.idcq.appserver.listeners;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.csource.fastdfs.ClientGlobal;

import com.idcq.appserver.aliscanpay.f2fpay.ToAlipayQrTradePay;
import com.idcq.appserver.utils.PropertyUtil;
import com.idcq.appserver.utils.mq.MqContext;
import com.idcq.appserver.utils.solr.SolrContext;
import com.idcq.appserver.wxscan.WXScanUtil;
/**
 * 系统上下文初始化
 * @author Administrator
 *
 */
public class ContextInitListener implements ServletContextListener{

	
	
	public static Properties SOLR_PROPS=null;
	public static Properties REDIS_PROPS=null;
	public static Properties JPUSH_PROPS=null;
	public static String fdfsFilePath = null;
	public static Properties COMMON_PROPS = null;
	public static Properties THREAD_POOL_PROPS = null;
    public static Properties MQ_PROPS = null;
	public static Properties SENSITIV_WORDS = null;
	
	public void contextDestroyed(ServletContextEvent sce) {
		
	}

	public void contextInitialized(ServletContextEvent sce) {
		String basePath = sce.getServletContext().getRealPath("/");
		String solrPropsFilePath = basePath + "WEB-INF/classes/properties/solr.properties";
		SOLR_PROPS=PropertyUtil.getProperties(solrPropsFilePath);
		String mqPropsFilePath = basePath + "WEB-INF/classes/properties/rocketMQ.properties";
		MQ_PROPS = PropertyUtil.getProperties(mqPropsFilePath);
		String redisPropsFilePath=basePath+"WEB-INF/classes/properties/redis.properties";
		REDIS_PROPS=PropertyUtil.getProperties(redisPropsFilePath);
		String jpushPropsFilePath=basePath+"WEB-INF/classes/properties/jpush.properties";
		JPUSH_PROPS=PropertyUtil.getProperties(jpushPropsFilePath);
		String cacheFilePath = basePath+"WEB-INF/classes/properties/common.properties";
		COMMON_PROPS=PropertyUtil.getProperties(cacheFilePath);
		ToAlipayQrTradePay.NOTIFY_URL=COMMON_PROPS.getProperty("scanNotifyUrl");
		WXScanUtil.notifyurl=COMMON_PROPS.getProperty("wxScanNotify");
		THREAD_POOL_PROPS=PropertyUtil.getProperties(basePath+"WEB-INF/classes/properties/threadPool.properties");
		
		String sensitivWordFilePath = basePath+"WEB-INF/classes/sensitivWords.properties";
		SENSITIV_WORDS = PropertyUtil.getProperties(sensitivWordFilePath);
		//文件服务器
		fdfsFilePath = basePath+ "WEB-INF/classes/properties/fdfs_client.properties";
		System.setProperty("fdfsPath", fdfsFilePath);
//		init(fdfsFilePath);
		
		startSolrContext();
		
//		startMgProduceContext();
	}
	
	/**
	 * 开启solr 上下文
	 */
	private void startSolrContext()
	{
		SolrContext.getInstance();
	}
    /**
     * 初始化MQ生产者
     */
    private void startMgProduceContext()
    {
       MqContext.getInstance();
    }
    	
	
	/**
	 * 初始化文件服务器参数
	 * @param fdfsFilePath
	 */
	private void init(String fdfsFilePath) {
		try {
			ClientGlobal.init(fdfsFilePath);
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("读取文件失败");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("读取文件失败");
			e.printStackTrace();
		}
	}

}
