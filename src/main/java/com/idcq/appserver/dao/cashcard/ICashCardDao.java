package com.idcq.appserver.dao.cashcard;

import java.util.List;

import com.idcq.appserver.dto.cashcard.CashCardDto;

import java.util.Map;

import com.idcq.appserver.dto.cashcard.CashCardDto;

public interface ICashCardDao {
    /**
     * 通过卡号和卡密码
     * 
     * @Function: com.idcq.appserver.dao.cashcard.ICashCardDao.queryCashCard
     * @Description:
     *
     * @param cardNo
     * @param cardPassword
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月6日 下午6:36:14
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月6日    ChenYongxin      v1.0.0         create
     */
    CashCardDto queryCashCard (Map<String, Object> pMap) throws Exception;
    /**
     * 更新会员卡状态
     * 
     * @Function: com.idcq.appserver.dao.cashcard.ICashCardDao.updateCardStatus
     * @Description:
     *
     * @param cardNo 卡号
     * @param status 状态
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月6日 下午6:39:17
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月6日    ChenYongxin      v1.0.0         create
     */
    public Integer updateCardStatus(String cardNo,Integer status) throws Exception;

    /**
     * 批量插入充值卡
     * @param cashCardList
     * @throws Exception
     */
    void batchInsertCashCard(List<CashCardDto> cashCardList) throws Exception;
    /**
     * 根据批次id查询充值卡
     * @param cashCardList
     * @throws Exception
     */
    List<CashCardDto> getCashCardByBatchId (Long cashCardBatchId) throws Exception;
}
