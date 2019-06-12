package com.idcq.appserver.utils.thread;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.idcq.appserver.listeners.ContextInitListener;

public class ThreadPoolManager {
	
	private static Logger logger=Logger.getLogger(ThreadPoolManager.class);
	public static BlockingQueue<Runnable> queue ;
	private static ThreadPoolExecutor executor;
	private static ThreadPoolManager threadPool;
	private Properties props;
	private Integer maxQueueSize;
	
	
	private ThreadPoolManager(){
		props=ContextInitListener.THREAD_POOL_PROPS;
		int corePoolSize= Integer.parseInt(props.getProperty("corePoolSize"));
		int maximumPoolSize= Integer.parseInt(props.getProperty("maximumPoolSize"));
		int keepAliveTime= Integer.parseInt(props.getProperty("keepAliveTime"));
		maxQueueSize= Integer.parseInt(props.getProperty("queueSize"));
		queue=new ArrayBlockingQueue<Runnable>(maxQueueSize);
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
	}
	
	public static synchronized ThreadPoolManager getInstance(){
		if(null==threadPool){
			threadPool = new ThreadPoolManager(); 
		}
		return threadPool;
	}
	
	public void execute(Runnable task){
		logger.info("添加线程至线程池");
		executor.execute(task);
		int queueSize =queue.size();  
		logger.info("线程队列大小为-->"+queueSize);
		if (queueSize == maxQueueSize){
			logger.info("队列已满...等待中!");
			try {
				queue.put(task);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("添加线程至线程池异常！"+e.getMessage());
			}
		} 
	}
	
}
