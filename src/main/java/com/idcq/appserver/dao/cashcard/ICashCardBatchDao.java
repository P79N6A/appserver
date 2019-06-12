package com.idcq.appserver.dao.cashcard;

import com.idcq.appserver.dto.cashcard.CashCardBatchDto;

public interface ICashCardBatchDao {

	/**
	 * 根据批次ID获取充值卡批次信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	CashCardBatchDto getCashCardBatchBybatchIdFromDb(Long cashCardBatchId) throws Exception;
	
	/**
	 * 查询生成的BatchNo是否重复
	 * @param batchNo
	 * @return
	 * @throws Exception
	 */
	Boolean batchNOIsDuplicate(String batchNo) throws Exception;
	
	/**
	 * 更新充值卡批次
	 * @param cashCardBatch
	 * @throws Exception
	 */
	void updateCashCardBatch(CashCardBatchDto cashCardBatch) throws Exception;
}
