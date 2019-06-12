package com.idcq.appserver.common.AsynchronousTask.consumer;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.common.AsynchronousTask.IAsynchronousTask;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.JacksonUtil;

@Service
public class GenerateCashCardMessageConsumer {
	private static final Logger logger = Logger.getLogger(GenerateCashCardMessageConsumer.class);
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@PostConstruct  
	private void initConsumer() {
 
	    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("AsynchronousTaskConsumer");
	    Properties props = ContextInitListener.MQ_PROPS;
        String host = props.getProperty(CommonConst.MQ_SERVER_HOST);
        String port = props.getProperty(CommonConst.MQ_SERVER_PORT);
        consumer.setNamesrvAddr(host + ":" + port);
	    consumer.setInstanceName("asynchronousTaskConsumber");
	   
		try {
			   consumer.subscribe("AsynchronousTask", "*");
		       consumer.registerMessageListener(new MessageListenerConcurrently()
		       {
		           @Override
		           public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
		           {
		               logger.info("GenerateCashCardMessageConsumer"+Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());
		               MessageExt msg = msgs.get(0);
		               if (msg.getTopic().equals("AsynchronousTask"))
		               {
		                   if (msg.getTags() != null)
		                   {
		                	   String tag = msg.getTags();
		                	   IAsynchronousTask asynchronousTaskService = (IAsynchronousTask)BeanFactory.getBean(tag);
		                	   if (asynchronousTaskService == null) {
		                		   logger.error("异步任务："+tag+" 对应的service不存在");
		                		   return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		                	   }
		                	   Map<String, Object> asynchronousTaskRequestMap = parseMessageBody(msg.getBody());
		                	   logger.info("AsynchronousTask：" + tag+"开始执行 "+" 请求参数："+asynchronousTaskRequestMap);
		                	   Runnable asynchronousTask = asynchronousTaskService.createAsynchronousTask(asynchronousTaskRequestMap);
		                       executor.execute(asynchronousTask);
		                   }
		               }
		               return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		           }
		       });
		       
		       consumer.start();
		} catch (Exception e) {
			logger.error("GenerateCashCardMessageConsumer初始化失败",e);
		}
       logger.info("GenerateCashCardMessageConsumer  started!!");
	}
	
	private Map<String, Object> parseMessageBody(final byte[] messageBody) {
		Map<String, Object> cashCardRequestMap = null;
		try {
			String messageBodyStr = new String(messageBody, "UTF-8");
			cashCardRequestMap = JacksonUtil.parseJson2Map(messageBodyStr);
		} catch (Exception e) {
			logger.error("解析GenerateCashCardMessageConsumer消息体失败",e);
		}
		return cashCardRequestMap;
	}
}
