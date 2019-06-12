package com.idcq.appserver.utils.queue;

import java.util.concurrent.LinkedBlockingQueue;

import com.idcq.appserver.dto.pay.OrderGoodsSettle;

public class Consumer implements Runnable{
	
	private LinkedBlockingQueue<OrderGoodsSettle> queue;
	
	public Consumer(LinkedBlockingQueue<OrderGoodsSettle> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		OrderGoodsSettle orderGoodsSettle=null;
		try {
			orderGoodsSettle=(OrderGoodsSettle) queue.take();
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public LinkedBlockingQueue<OrderGoodsSettle> getQueue() {
		return queue;
	}

	public void setQueue(LinkedBlockingQueue<OrderGoodsSettle> queue) {
		this.queue = queue;
	}

	
}
