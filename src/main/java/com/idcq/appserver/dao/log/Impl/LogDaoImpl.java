package com.idcq.appserver.dao.log.Impl;

import com.idcq.appserver.dto.log.ShopOperationLog;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.log.ILogDao;
import com.idcq.appserver.dto.log.UserOperatorLogDto;
@Repository
public class LogDaoImpl extends BaseDao<UserOperatorLogDto> implements ILogDao {

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.log.ILogDao#insertLog(com.idcq.appserver.dto.log.LogDto)
	 */
	@Override
	public int insertLog(UserOperatorLogDto userOperatorLogDto) throws Exception {
		
		return super.insert("insertLog", userOperatorLogDto);
	}

	@Override
	public int addShopOperatorLog(ShopOperationLog shopOperationLog) {
		
		return super.insert("addShopOperatorLog", shopOperationLog);
	}
}
