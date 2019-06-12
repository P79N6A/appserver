package com.idcq.appserver.common.AsynchronousTask.consumer;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.level.CalculatePointMessageModel;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.service.level.processor.IPointProcessor;
import com.idcq.appserver.utils.BeanFactory;

@Service
public class CalculatePointMessageConsumer {
	private static final Logger logger = Logger.getLogger(CalculatePointMessageConsumer.class);
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@PostConstruct  
	private void initConsumer() {
 
	    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CalculatePointMessageConsumer");
	    Properties props = ContextInitListener.MQ_PROPS;
        String host = props.getProperty(CommonConst.MQ_SERVER_HOST);
        String port = props.getProperty(CommonConst.MQ_SERVER_PORT);
        consumer.setNamesrvAddr(host + ":" + port);
	    consumer.setInstanceName("CalculatePointMessageConsumer");
	   
		try {
			   consumer.subscribe("calculatePointMessage", "*");
		       consumer.registerMessageListener(new MessageListenerConcurrently()
		       {
		           @Override
		           public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
		           {
		               logger.info("calculatePointTask"+Thread.currentThread().getName() + " Receive New Messages: " + msgs);
		               MessageExt msg = msgs.get(0);
		               final CalculatePointMessageModel messageModel = getMessageModel(msg.getBody(), CalculatePointMessageModel.class);
		               if (messageModel == null) {
		            	   return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		               }
		               
		               final IPointProcessor pointProcessor = (IPointProcessor)BeanFactory.getBean("pointRuleType:"+
		            		   																messageModel.getRuleType());
		               if (pointProcessor == null) {
		            	   logger.info("积分规则处理器不存在---pointRuleType"+messageModel.getRuleType());
		            	   return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		               }
		               
		               executor.execute(new Runnable() {
						
							@Override
							public void run() {
								try {
									pointProcessor.processPoint(messageModel);
								} catch (Exception e) {
									logger.info("积分规则处理器异常：", e);
								}
								
							}
		               });
		               
		               return  ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		           }
		       });
		       
		       consumer.start();
		} catch (Exception e) {
			logger.error("CalculatePointMessageConsumer初始化失败",e);
		}
       logger.info("CalculatePointMessageConsumerr  started!!");
	}
	
	private <T> T getMessageModel(byte[] body,Class<T> clazz) {
		T model = null;
		try {
			model = JSON.parseObject(new String(body, "UTF-8"), clazz);
		} catch (Exception e) {
			logger.error("请求参数类型或格式错误",e);
		}
		return model;
	}
}
