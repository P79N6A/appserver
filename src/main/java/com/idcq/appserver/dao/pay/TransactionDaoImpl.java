package com.idcq.appserver.dao.pay;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.pay.TransactionDto;
import com.idcq.appserver.dto.user.UserDto;
/**
 * 我的订单daooo
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class TransactionDaoImpl extends BaseDao<TransactionDto>implements ITransactionDao{
	
	public Integer addTransaction(TransactionDto transactionDto)
			throws Exception {
		return super.insert(generateStatement("addTransaction"),transactionDto);
	}

	public Integer delTransaction(long transactionId) throws Exception {
		return super.delete(generateStatement("delTransaction"), transactionId);
	}

	public void updateTransaction(TransactionDto transactionDto) throws Exception {
		super.update("updateTransaction", transactionDto);
	}

	public void updateTransactionAfterRdPaySuccess(TransactionDto transactionDto) throws Exception {
		super.update("updateTransactionAfterPaySuccess", transactionDto);
	}
	
	public TransactionDto getDBTransactionById(Long transactionId) throws Exception {
		return (TransactionDto) super.selectOne(generateStatement("getTransactionById"), transactionId);
	}
	public Map<String, Object> getPayTargetById(Long transactionId) throws Exception {
		return (Map)super.selectOne(generateStatement("getPayTargetById"), transactionId);
	}

	@Override
	public List<TransactionDto> getPlatformIncomeStaticsByTimeAndMoneySource(Map<String, Object> searchParams) {
		return (List<TransactionDto>)super.findList("getPlatformIncomeStaticsByTimeAndMoneySource", searchParams);
	}


}
