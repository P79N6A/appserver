package com.idcq.appserver.dao.cashcard;

import com.idcq.appserver.dto.cashcard.CashCardBatchLogDto;

public interface ICashCardBatchLogDao {
    
    /**
     * 查询是否充值卡批次是否使用
     * 
     * @Function: com.idcq.appserver.dao.cashcard.ICashCardBatchLogDao.queryCashBardBatchIsExist
     * @Description:
     *
     * @param cashCardBatchId
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月7日 下午5:26:32
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月7日    ChenYongxin      v1.0.0         create
     */
    Integer queryCashBardBatchIsExist(Long cashCardBatchId) throws Exception;

	void insertCashCardBatchLog(CashCardBatchLogDto batchLog) throws Exception;
}
