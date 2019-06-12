package com.idcq.appserver.dao.cashcard.impl;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.cashcard.ICashCardBatchDao;
import com.idcq.appserver.dto.cashcard.CashCardBatchDto;

@Repository
public class CashCardBatchDaoImpl extends BaseDao<CashCardBatchDto> implements
		ICashCardBatchDao {

	@Override
	public CashCardBatchDto getCashCardBatchBybatchIdFromDb(Long cashCardBatchId) throws Exception {
		return (CashCardBatchDto) super.selectOne(generateStatement("getCashCardBatchByBatchId"), cashCardBatchId);
	}
	
	@Override
	public Boolean batchNOIsDuplicate(String batchNo) throws Exception {
		CashCardBatchDto duplicateBatch = (CashCardBatchDto) super.selectOne
				   (generateStatement("getCashCardBatchByBatchNo"), batchNo);
		if (duplicateBatch == null)
			return false;
		else
			return true;
	}
	
	@Override
	public void updateCashCardBatch(CashCardBatchDto cashCardBatch) throws Exception {
		super.update(generateStatement("updateCashCardBatch"), cashCardBatch);
	}
}
