package com.idcq.appserver.dao.cashcard.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.cashcard.ICashCardDao;
import com.idcq.appserver.dto.cashcard.CashCardDto;

@Repository
public class ICashCardDaoImpl extends BaseDao<CashCardDto> implements ICashCardDao {

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardDao#queryCashCard(java.lang.String, java.lang.String)
     */
    @Override
    public CashCardDto queryCashCard(Map<String, Object> pMap) throws Exception
    {
        return (CashCardDto) super.selectOne(generateStatement("queryCashCard"), pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardDao#updateCardStatus(java.lang.String)
     */
    @Override
    public Integer updateCardStatus(String cardNo,Integer status) throws Exception
    {
        Map<String, Object> pMap = new HashMap<String, Object>();
        pMap.put("cashCardNo", cardNo);
        pMap.put("cardStatus", status);
        return super.update(generateStatement("updateCardStatusByNo"),pMap);
    }

	@Override
	public void batchInsertCashCard(List<CashCardDto> cashCardList) throws Exception {
		super.insertBatch(generateStatement("insertCashCard"), cashCardList);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardDao#getCashCardByBatchId(java.lang.Long)
     */
    @Override
    public List<CashCardDto> getCashCardByBatchId(Long cashCardBatchId) throws Exception
    {
        return (List<CashCardDto>) super.findList(generateStatement("getCashCardByBatchId"), cashCardBatchId);

    }
}
