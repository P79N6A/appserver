package com.idcq.appserver.service.finance;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.financeStatistic.FinanceStatisticDto;

public interface IFinanceServcie {

	Map<String, Object> financeStatistic(String param)  throws Exception;
	/**
	 * 查询账务统计明细
	 */
	PageModel getFinanceStatisticDetail(FinanceStatisticDto parms)throws Exception;
	/**
	 * 查询账务统计状态
	 */
	List<Map<String, Object>> getFinanceStat(Map<String, Object> parms)throws Exception;
}
