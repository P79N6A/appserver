package com.idcq.appserver.dao.financeStatistic;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.financeStatistic.FinanceStatisticDto;

@Repository
public class FinanceStatisticDaoImpl extends BaseDao<FinanceStatisticDto> implements IfinanceStatisticDao {

	@Override
	public FinanceStatisticDto insertFinanceStatistic(
			FinanceStatisticDto financeStatisticDto) throws Exception {
		this.getSqlSession().insert("insertFinanceStatistic", financeStatisticDto);
		return financeStatisticDto;
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.financeStatistic.IfinanceStatisticDao#getFinanceStatisticDetail(java.util.Map)
	 */
	@Override
	public List<FinanceStatisticDto> getFinanceStatistic(
			FinanceStatisticDto params) throws Exception {
		return (List)super.findList("getFinanceStatistic", params);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.financeStatistic.IfinanceStatisticDao#getFinanceStatisticDetailCount(java.util.Map)
	 */
	@Override
	public int getFinanceStatisticCount(FinanceStatisticDto params)
			throws Exception {
		return (int) selectOne("getFinanceStatisticCount", params);

	}

	@Override
	public void insertfinanceStatisticDetails(List<Map<String, Object>> records) {
		this.getSqlSession().insert("insertfinanceStatisticDetails", records);
	}

	@Override
	public void insertFinanceStatisticStats(List<Map<String, Object>> params) {
		this.getSqlSession().insert("insertFinanceStatisticStats", params);
	}


	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.financeStatistic.IfinanceStatisticDao#getFinanceStatisticDetailBystatisticId(java.lang.Long)
	 */
	@Override
	public List<Map<String, Object>> getFinanceStatisticDetailBystatisticId(
			Long statisticId) throws Exception {
		
		return (List)findList("getFinanceStatisticDetailBystatisticId", statisticId);
	}


	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.financeStatistic.IfinanceStatisticDao#getFinanceStat(java.util.Map)
	 */
	@Override
	public List<Map<String, Object>> getFinanceStat(Map<String, Object> parms)
			throws Exception {
		
		return (List)findList("getFinanceStat", parms);
	}
	
}
