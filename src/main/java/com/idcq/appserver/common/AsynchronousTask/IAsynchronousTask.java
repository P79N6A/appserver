package com.idcq.appserver.common.AsynchronousTask;

import java.util.Map;

public interface IAsynchronousTask {

	Runnable createAsynchronousTask(final Map<String, Object> asynchronousTaskRequestMap);
}
