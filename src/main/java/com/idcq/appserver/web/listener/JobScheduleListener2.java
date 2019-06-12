//package com.idcq.appserver.web.listener;
//
//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
//
//import org.apache.log4j.Logger;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//
//import com.idcq.appserver.index.quartz.schedule.IndexScheduler;
//import com.idcq.appserver.index.quartz.schedule.SchFactory;
//
///**
// * 任务调度监听器
// * 
// * @author Administrator
// * 
// * @date 2015年3月18日
// * @time 下午4:03:56
// */
//public class JobScheduleListener2 implements ServletContextListener{
//	private static final Logger logger = Logger.getLogger(JobScheduleListener2.class);
//	
//	public void contextDestroyed(ServletContextEvent arg0) {
//	}
//
//	public void contextInitialized(ServletContextEvent event) {
////		ServletContext cxt = event.getServletContext();
////		ApplicationContext app = WebApplicationContextUtils.getWebApplicationContext(cxt);
////		IGoodsServcie imp = app.getBean(IGoodsServcie.class);
////		System.out.println("==================  "+imp);
//		logger.info("调度器监听器被执行");
//		//实例化调度器
////		Scheduler scheduler = SchFactory.getCurrentSchedule();
//		try {
//			//加载调度任务
////			IndexScheduler.addBuildGoodsIndexSch();
////			IndexScheduler.addBuildShopIndexSch();
//			//启动调度器
//			scheduler.start();
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//			logger.error("调度器启动发生异常，请检查",e);
//		} catch (Exception e){
//			e.printStackTrace();
//			logger.error("向调度器加入数据库全文索引调度任务发生异常，请检查",e);
//		}
//	}
//}
