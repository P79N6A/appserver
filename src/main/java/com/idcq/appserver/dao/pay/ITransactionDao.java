package com.idcq.appserver.dao.pay;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.user.UserDto;

public interface ITransactionDao {
	
	/**
	 * 新增交易流水
	 * @return
	 * @throws Exception
	 */
	public Integer addTransaction(TransactionDto transactionDto) throws Exception; 
	
	
	/**
	 * 删除交易流水
	 * @return
	 * @throws Exception
	 */
	public Integer delTransaction(long transactionId) throws Exception; 
	
	/**
	 * 修改交易流水
	 * @param transactionId
	 * @throws Exception
	 */
	public void updateTransaction(TransactionDto transactionDto) throws Exception; 
	
	/**
	 * 第三方支付成功修改交易记录
	 * @param transactionId
	 * @throws Exception
	 */
	public void updateTransactionAfterRdPaySuccess(TransactionDto transactionDto) throws Exception; 
	
	public TransactionDto getDBTransactionById(Long transactionId) throws Exception;
	
	/**
	 * 获取交易目标Id
	 * @param transactionId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getPayTargetById(Long transactionId) throws Exception;


	List<TransactionDto> getPlatformIncomeStaticsByTimeAndMoneySource(Map<String, Object> searchParams);
}
