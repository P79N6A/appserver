package com.idcq.appserver.common.AsynchronousTask;

import com.idcq.appserver.dto.log.UserOperatorLogDto;

public interface IAsyncLogTask {

	Runnable createAsyncLogTask(final UserOperatorLogDto userOperatorLogDto);
}
