package com.idcq.appserver.dao.cashcard;

import java.util.Map;

import com.idcq.appserver.dto.cashcard.CashCardUseLogDto;

public interface ICashCardUseLogDao
{
    /**
     * 插入充值卡使用记录
     * 
     * @param cardUseLogDto
     * @return
     * @throws Exception
     */
    Integer insertCashCardUseLog(CashCardUseLogDto cardUseLogDto) throws Exception;
    /**
     * 查询商铺充值卡使用记录
     * 
     * @param cardUseLogDto
     * @return
     * @throws Exception
     */
    Map<String, Object> getCashCardUsedByShop(Map<String, Object> pMap) throws Exception;
    
    /**
     * 查询用户充值卡使用记录
     * 
     * @param cardUseLogDto
     * @return
     * @throws Exception
     */
    Map<String, Object> getCashCardUsedByUser(Map<String, Object> pMap) throws Exception;
}
