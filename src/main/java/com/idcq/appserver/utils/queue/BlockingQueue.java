package com.idcq.appserver.utils.queue;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;


public class BlockingQueue{
	
	private static Logger logger=Logger.getLogger(BlockingQueue.class);
	
	/**
	 * 订单结算队列
	 */
	private static LinkedBlockingQueue<Object> orderGoodsSettleQueue = new LinkedBlockingQueue<Object>(Integer.MAX_VALUE);  
	
	static class Producer extends Thread{
		
		private BlockingQueue blockingQueue;  
		private Object obj = new Object();
		
		public Producer(BlockingQueue blockingQueue) {
			this.blockingQueue = blockingQueue;
		}
		
		public void run() {
			blockingQueue.put(obj);
		}
	}
	
	static class Consumer extends Thread{
		
		private BlockingQueue blockingQueue;  
		private Object obj = new Object();
		
		public Consumer(BlockingQueue blockingQueue) {
			this.blockingQueue = blockingQueue;
		}
		
		public void run() {
			blockingQueue.get();
		}
	}
	
	
	/**
	 * 存入队列
	 * @param obj
	 */
	public static void put(Object obj) {  
        try {
        	orderGoodsSettleQueue.put(obj);
			logger.info("存入队列成功!");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} //向队列中添加数据，如果盘子满了，当前线程阻塞    
    }  
      
    /** 取数据 */  
    public static Object get() {  
        Object obj = null;  
        try {  
        	obj = orderGoodsSettleQueue.take();// 从队列中获取元素，如果盘子空了，当前线程阻塞  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
        return obj;  
    }  
    
}
