package com.idcq.appserver.service.log;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.dao.log.ILogDao;
import com.idcq.appserver.dto.log.UserOperatorLogDto;

@Service("userLogTask")
public class LogServiceImpl implements IlogService {

    private static final Logger logger = Logger.getLogger(LogServiceImpl.class);
   
    @Autowired
    private ILogDao logDao;
	
	/* (non-Javadoc)
	 * @see com.idcq.appserver.common.AsynchronousTask.IAsyncLogTask#createAsyncLogTask(java.util.Map)
	 */
	@Override
	public Runnable createAsyncLogTask(
			final UserOperatorLogDto userOperatorLogDto) {
		return new Runnable() {
			@Override
			public void run() {
				if (userOperatorLogDto == null) {
					logger.error("请求参数为空");
					return;
				}
				try {
					//记录日志
					logDao.insertLog(userOperatorLogDto);
				} catch (Exception e) {
					logger.error("异步记录日志失败："+e.getMessage());
					e.printStackTrace();
				}
				
			}
		};
	}
}
