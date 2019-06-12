package com.idcq.appserver.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 异步线程执行工具
 * 
 * @author Administrator
 * 
 * @date 2015年5月20日
 * @time 上午9:14:27
 */
public class AsyncExecutorUtil {
	
	private static final Log logger = LogFactory.getLog(AsyncExecutorUtil.class);
	
	/**
	 * 执行单个任务
	 * 
	 * @param task 任意一个实现了Rannable的javabean都可以作为一个待执行任务，以入参传入
	 * @return
	 */
	public static int execute(Runnable task){
		logger.info("异步线程池执行任务-start");
		try {
			ExecutorService es = Executors.newCachedThreadPool();
			es.execute(task);
			//等待一次压入池内的任务执行完成，释放池
			es.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异步线程池执行任务-系统异常",e);
			return 0;
		}
		logger.info("异步线程池执行任务-end");
		return 1;
	}
	
	/**
	 * 执行批量任务
	 * 
	 * @param tasks 入参tasks集合的每个必须是实现了Rannable的javabean
	 * @return
	 */
	public static int executeList(List<Runnable> tasks){
		logger.info("异步线程池执行批量任务-start");
		try {
			ExecutorService es = Executors.newCachedThreadPool();
			if(tasks != null){
				for(Runnable e : tasks){
					es.execute(e);
				}
			}
			//等待一次压入池内的任务执行完成，释放池
			es.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("异步线程池执行批量任务-系统异常",e);
			return 0;
		}
		logger.info("异步线程池执行批量任务-end");
		return 1;
	}
	
}
