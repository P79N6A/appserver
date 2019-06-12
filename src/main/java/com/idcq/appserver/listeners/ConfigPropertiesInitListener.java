package com.idcq.appserver.listeners;

import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.idcq.appserver.utils.PropertyUtil;

/**
 * 初始化properties文件
 * 
 * @author Administrator
 * 
 */
public class ConfigPropertiesInitListener implements ServletContextListener {

	public static Properties TIMECONFIG = null;

	public void contextDestroyed(ServletContextEvent sce) {

	}

	public void contextInitialized(ServletContextEvent sce) {
		String solrPropsFilePath = sce.getServletContext().getRealPath("/");
		solrPropsFilePath += "WEB-INF/classes/properties/time_config.properties";
		TIMECONFIG = PropertyUtil.getProperties(solrPropsFilePath);
	}

}
