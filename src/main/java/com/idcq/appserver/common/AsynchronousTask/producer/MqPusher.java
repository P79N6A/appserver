package com.idcq.appserver.common.AsynchronousTask.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.idcq.appserver.utils.mq.MqProduceApi;

public class MqPusher {

	private final static Logger logger = LoggerFactory.getLogger(MqPusher.class);
	
	private final static String topic = "AsynMessage";
	
	public static void pushMessage(String tag, Object messageBody){
		if (tag == null || messageBody == null) {
			logger.info("推送tag和messageBody不能为空");
			return;
		}
		logger.info("开始推送异步消息---- topic:{} tag:{} messageBody:{}",topic,tag,messageBody);
		MqProduceApi.setMessage(topic, tag, JSON.toJSONString(messageBody));
		logger.info("异步消息---- topic:{} tag:{} messageBody:{} 推送成功",topic,tag,messageBody);
	}
}
