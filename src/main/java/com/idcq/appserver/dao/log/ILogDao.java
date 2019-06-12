package com.idcq.appserver.dao.log;

import com.idcq.appserver.dto.log.ShopOperationLog;
import com.idcq.appserver.dto.log.UserOperatorLogDto;

public interface ILogDao {

	
	int insertLog(UserOperatorLogDto userOperatorLogDto) throws Exception;

	/**
	 * 插入商铺操作日志
	 * @param shopOperationLog
	 * @return
     */
	int addShopOperatorLog(ShopOperationLog shopOperationLog);

}
