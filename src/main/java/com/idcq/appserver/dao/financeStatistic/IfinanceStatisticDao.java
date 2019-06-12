package com.idcq.appserver.dao.financeStatistic;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.financeStatistic.FinanceStatisticDto;

public interface IfinanceStatisticDao {
	/**
	 * PCF2：查询账务统计明细接口
	 * @param params
	 * @return
	 */
	List<FinanceStatisticDto> getFinanceStatistic(FinanceStatisticDto params) throws Exception;

	/**
	 * PCF2：查询账务统计明细总数
	 * @param params
	 * @return
	 */
	int getFinanceStatisticCount(FinanceStatisticDto params) throws Exception;
	
	List<Map<String, Object>> getFinanceStatisticDetailBystatisticId(Long statisticId)throws Exception;
	/**
	 * 查询账务统计状态
	 */
	List<Map<String, Object>> getFinanceStat(Map<String, Object> parms)throws Exception;


	FinanceStatisticDto insertFinanceStatistic(
			FinanceStatisticDto financeStatisticDto) throws Exception;

	void insertfinanceStatisticDetails(List<Map<String, Object>> records);


	void insertFinanceStatisticStats(List<Map<String, Object>> params);
}
