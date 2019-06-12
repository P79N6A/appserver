package com.idcq.appserver.service.cashcard;

import java.util.Map;

import com.idcq.appserver.common.AsynchronousTask.IAsynchronousTask;
import com.idcq.appserver.dto.cashcard.CashCardBatchDto;

public interface ICashCardService extends IAsynchronousTask{
	/**
	 * 根据批次ID获取充值卡批次信息
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	CashCardBatchDto getCashCardBatchBybatchIdFromDb(Long cashCardBatchId) throws Exception;
	
	void checkCashCardBatchvalidity(Long cashCardBatchId,Boolean asynchronous) throws Exception;
	
	void generateCashCardAsynchronously(Map<String, Object> requestMap) throws Exception;
	
	void generateCashCard(Long cashCardBatchId,Integer operaterId) throws Exception;
    /**
     * 充值卡充值接口
     * 
     * @Function: com.idcq.appserver.service.cashcard.ICashCardService.useCashCard
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月6日 下午7:05:32
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月6日    ChenYongxin      v1.0.0         create
     */
    Map<String, Object> useCashCard(Map<String, Object> pMap) throws Exception;
    
    /**
     * 下载充值卡
     * 
     * @Function: com.idcq.appserver.service.cashcard.ICashCardService.downloadCashCard
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月7日 下午4:10:12
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月7日    ChenYongxin      v1.0.0         create
     */
    Map<String, Object> downloadCashCard(Map<String, Object> pMap) throws Exception;
    
    /**
     * 充值卡充值记录查询接口
     * 
     * @Function: com.idcq.appserver.service.cashcard.ICashCardService.getCashCardUsed
     * @Description:
     *
     * @param pMap
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年1月11日 下午4:44:57
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年1月11日    ChenYongxin      v1.0.0         create
     */
    Map<String, Object> getCashCardUsed(Map<String, Object> pMap) throws Exception;
    
}
