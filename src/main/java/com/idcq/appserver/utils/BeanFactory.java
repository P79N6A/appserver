package com.idcq.appserver.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring bean工厂
 * @author Administrator
 *
 */
public class BeanFactory implements ApplicationContextAware{
	
	private static ApplicationContext appCtx;
	
	public void setApplicationContext(ApplicationContext ctx)
			throws BeansException {
		appCtx=ctx;
		
	}
	
	
	
	/**
	 * 获得spring管理的bean
	 * @param entityClass
	 * @return
	 */
	public static <T>T getBean(Class<T>entityClass)
	{
		return appCtx.getBean(entityClass);
	}
	
	public static Object getBean(String beanName) {
		return appCtx.getBean(beanName);
	}
}
