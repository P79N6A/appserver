package com.idcq.appserver.service.pay;

import java.util.Map;

import com.idcq.appserver.service.pay.model.PayNotifyResult;

public interface IThirdPayService {

	/**
	 * 获取第三方支付渠道预支付信息
	 * @param requestMap
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> prePayBy3rd(Map<String, Object> requestMap) throws Exception;
	
	/**
	 * 处理第三方异步回调结果
	 * @param payResult
	 * @throws Exception
	 */
	void dealPayBy3rdNotify(PayNotifyResult payResult) throws Exception;
}
