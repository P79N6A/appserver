package com.idcq.appserver.dao.cashcard.impl;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.cashcard.ICashCardBatchLogDao;
import com.idcq.appserver.dto.cashcard.CashCardBatchLogDto;

@Repository
public class CashCardBatchLogDaoImpl extends BaseDao<CashCardBatchLogDto>
		implements ICashCardBatchLogDao {

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardBatchLogDao#queryCashBardBatchIsExist(java.lang.Long)
     */
    @Override
    public Integer queryCashBardBatchIsExist(Long cashCardBatchId) throws Exception
    {
        return (Integer) super.selectOne(generateStatement("queryCashBardBatchIsExist"), cashCardBatchId);
    }

	@Override
	public void insertCashCardBatchLog(CashCardBatchLogDto batchLog) throws Exception {
		super.insert(generateStatement("insertCashCardBatchLog"), batchLog);
	}
}
