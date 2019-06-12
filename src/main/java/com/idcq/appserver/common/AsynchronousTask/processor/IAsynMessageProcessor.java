package com.idcq.appserver.common.AsynchronousTask.processor;

public interface IAsynMessageProcessor {

	public void consumerAsynMessage(byte[] body) throws Exception;
}
