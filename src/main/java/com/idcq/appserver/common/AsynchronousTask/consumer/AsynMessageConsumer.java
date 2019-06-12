package com.idcq.appserver.common.AsynchronousTask.consumer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.AsynchronousTask.processor.IAsynMessageProcessor;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.BeanFactory;

@Service
public class AsynMessageConsumer {
	private final static Logger logger = LoggerFactory.getLogger(AsynMessageConsumer.class);
	private static final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	@PostConstruct  
	private void initConsumer() {
 
	    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("AsynMessageConsumer");
	    Properties props = ContextInitListener.MQ_PROPS;
        String host = props.getProperty(CommonConst.MQ_SERVER_HOST);
        String port = props.getProperty(CommonConst.MQ_SERVER_PORT);
        consumer.setNamesrvAddr(host + ":" + port);
	    consumer.setInstanceName("AsynMessageConsumer");
	   
		try {
			   consumer.subscribe("AsynMessage", "*");
		       consumer.registerMessageListener(new MessageListenerConcurrently()
		       {
		           @Override
		           public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
		           {
		               logger.info("AsynMessageTask"+Thread.currentThread().getName() + " Receive New Messages: " + msgs);
		               final MessageExt msg = msgs.get(0);
		               final String tag = msg.getTags();
		               
		               final IAsynMessageProcessor asynMessageProcessor = (IAsynMessageProcessor)BeanFactory.getBean(tag);
		               if (asynMessageProcessor == null) {
		            	   logger.info("AsynMessage:{}处理器不存在",tag);
		            	   return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		               }
		               
		               executor.execute(new Runnable() {
						
							@Override
							public void run() {
								try {
									asynMessageProcessor.consumerAsynMessage(msg.getBody());
								} catch (Exception e) {
									logger.info("AsynMessage:{}处理器异常：", tag);
									logger.info("异常信息：", e);
								}
								
							}
		               });
		               
		               return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		           }
		       });
		       
		       consumer.start();
		} catch (Exception e) {
			logger.error("AsynMessageConsumer初始化失败",e);
		}
       logger.info("AsynMessageConsumerr  started!!");
	}
}
