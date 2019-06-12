package com.idcq.appserver.common.AsynchronousTask.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public abstract class BaseProcessor implements IAsynMessageProcessor{

	private final static Logger logger = LoggerFactory.getLogger(BaseProcessor.class);
	
	protected <T> T getMessageModel(byte[] body,Class<T> clazz) {
		T model = null;
		try {
			model = JSON.parseObject(new String(body, "UTF-8"), clazz);
		} catch (Exception e) {
			logger.error("请求参数类型或格式错误",e);
		}
		return model;
	}
}
