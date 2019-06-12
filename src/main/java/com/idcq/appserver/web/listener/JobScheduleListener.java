//package com.idcq.appserver.web.listener;
//
//import javax.servlet.ServletContext;
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//import org.apache.log4j.Logger;
//
//import com.idcq.appserver.index.quartz.schedule.QuartzContext;
//
///**
// * 任务调度监听器
// * 
// * @author Administrator
// * 
// * @date 2015年3月18日
// * @time 下午4:03:56
// */
//public class JobScheduleListener implements ServletContextListener{
//	private static final Logger logger = Logger.getLogger(JobScheduleListener.class);
//	
//	public void contextDestroyed(ServletContextEvent arg0) {
//	}
//
//	public void contextInitialized(ServletContextEvent event) {
//		logger.info("context监听器被执行");
//		ServletContext cxt = event.getServletContext();
////		QuartzContext.getInstance().setContext(cxt);
//	}
//}
