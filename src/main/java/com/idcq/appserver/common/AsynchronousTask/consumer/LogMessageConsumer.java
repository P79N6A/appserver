package com.idcq.appserver.common.AsynchronousTask.consumer;

import java.util.List;
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
import com.idcq.appserver.common.AsynchronousTask.IAsyncLogTask;
import com.idcq.appserver.dto.log.UserOperatorLogDto;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;

@Service
public class LogMessageConsumer {
	private static final Logger logger = Logger.getLogger(LogMessageConsumer.class);
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();
	
	@PostConstruct  
	private void initConsumer() {
 
	    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("AsyncLogTaskConsumer");
	    Properties props = ContextInitListener.MQ_PROPS;
        String host = props.getProperty(CommonConst.MQ_SERVER_HOST);
        String port = props.getProperty(CommonConst.MQ_SERVER_PORT);
        consumer.setNamesrvAddr(host + ":" + port);
	    consumer.setInstanceName("logTaskConsumber");
	   
		try {
			//TODO 确认topic名称,获取用户日志下面所有记录
			   consumer.subscribe("userLogTask", "*");
		       consumer.registerMessageListener(new MessageListenerConcurrently()
		       {
		           @Override
		           public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
		           {
		               logger.info("AsynchronousTask"+Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());
		               MessageExt msg = msgs.get(0);
		               if ("userLogTask".equals(msg.getTopic()))
		               {
		                   if (msg.getTags() != null)
		                   {
		                	   String tag = msg.getTags();
		                	   IAsyncLogTask asyncLogTask = (IAsyncLogTask)BeanFactory.getBean(tag);
		                	   if (asyncLogTask == null) {
		                		   logger.error("异步任务："+tag+" 对应的service不存在");
		                		   return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		                	   }
		                	   UserOperatorLogDto userOperatorLogDto = parseMessageBody(msg.getBody());
		                	   logger.info("userOperatorLogDto：" + tag+"开始执行 "+" 请求参数："+userOperatorLogDto);
		                	   Runnable asynchronousTask = asyncLogTask.createAsyncLogTask(userOperatorLogDto);
		                       executor.execute(asynchronousTask);
		                   }
		               }
		               return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		           }
		       });
		       
		       consumer.start();
		} catch (Exception e) {
			logger.error("logTaskConsumber初始化失败",e);
		}
       logger.info("logTaskConsumber  started!!");
	}
	
	private UserOperatorLogDto parseMessageBody(final byte[] messageBody) {
		
		UserOperatorLogDto userOperatorLogDto = null;
		try {
			String messageBodyStr = new String(messageBody, "UTF-8");
			userOperatorLogDto = JacksonUtil.jsonToObject(messageBodyStr, UserOperatorLogDto.class, DateUtils.DATETIME_FORMAT);
		} catch (Exception e) {
			logger.error("解析logTaskConsumber消息体失败",e);
		}
		return userOperatorLogDto;
	}
}
