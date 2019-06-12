package com.idcq.appserver.utils;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.bank.BankCardDto;
import com.idcq.appserver.service.common.ICommonService;

/**
 * 绑定银行卡推送信息
 * 
 * @author chenyongxin
 * 
 */
public class BindCardPushMessageTask implements Runnable {

	private static Logger logger = Logger
			.getLogger(BindCardPushMessageTask.class);
	private BankCardDto bankCard;
	
	public BindCardPushMessageTask(BankCardDto bankCard) {
		super();
		this.bankCard = bankCard;
	}
	/**
	 * 下订单推送消息给商铺
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public void pushMessage(BankCardDto bankCard) throws Exception {
		logger.info("推送绑定银行卡消息-start");
		try {
			ICommonService commonService = BeanFactory.getBean(ICommonService.class);
			Long userId = bankCard.getUserId();
			String cardNumber = bankCard.getCardNumber();
			String cardNumberStr = NumberUtil.deailCarNumber(cardNumber);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("action", "bindBankCard");
			jsonObject.put("cardNumber", cardNumberStr);
			commonService.pushUserMsg("bindBankCard", jsonObject.toString(), userId, CommonConst.USER_TYPE_ZREO);
			
		} catch (Exception e) {
			logger.error("推送绑定银行卡消息",e);
			e.printStackTrace();
		}
		logger.info("推送绑定银行卡消息-end");
	}
	@Override
	public void run() {
		try {
			pushMessage(this.bankCard);
		} catch (Exception e) {
			logger.error("推送订单消息异常",e);
			e.printStackTrace();
		}
	}
	

}
