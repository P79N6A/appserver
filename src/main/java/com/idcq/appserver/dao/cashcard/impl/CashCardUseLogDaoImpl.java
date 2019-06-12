package com.idcq.appserver.dao.cashcard.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dao.cashcard.ICashCardUseLogDao;
import com.idcq.appserver.dto.cashcard.CashCardDto;
import com.idcq.appserver.dto.cashcard.CashCardUseLogDto;

@Repository
public class CashCardUseLogDaoImpl extends BaseDao<CashCardUseLogDto> implements
		ICashCardUseLogDao {

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardUseLogDao#insertCashCard(com.idcq.appserver.dto.cashcard.CashCardUseLogDto)
     */
    @Override
    public Integer insertCashCardUseLog(CashCardUseLogDto cardUseLogDto) throws Exception
    {
        return super.insert(generateStatement("insertCashcardUseLog"), cardUseLogDto);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardUseLogDao#getCashCardUsedByShop(java.util.Map)
     */
    @Override
    public Map<String, Object> getCashCardUsedByShop(Map<String, Object> pMap) throws Exception
    {
        return (Map<String, Object>) super.selectOne(generateStatement("getCashCardUsedByShop"), pMap);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.cashcard.ICashCardUseLogDao#getCashCardUsedByUser(java.util.Map)
     */
    @Override
    public Map<String, Object> getCashCardUsedByUser(Map<String, Object> pMap) throws Exception
    {
        return (Map<String, Object>) super.selectOne(generateStatement("getCashCardUsedByUser"), pMap);
    }

}
