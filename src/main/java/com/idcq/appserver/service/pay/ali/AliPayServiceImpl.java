package com.idcq.appserver.service.pay.ali;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.pay.ali.util.AliPayUtil;
import com.idcq.appserver.service.pay.impl.ThirdPayServiceImpl;
import com.idcq.appserver.utils.JacksonUtil;

@Service("payChannel:1")
public class AliPayServiceImpl extends ThirdPayServiceImpl {
	
	@Override
	protected String getThirdPayChannelData(Map<String, Object> requestMap, 
											String channelParameter,
											Integer subPayModel)
											throws Exception {
		
		return AliPayUtil.getPrePayDataFromAli(buildPrePayRequestMap(requestMap, channelParameter, subPayModel));
	}
	
	private Map<String, Object> buildPrePayRequestMap(
			Map<String, Object> requestMap, String channelParameter,
			Integer subPayModel) throws Exception {
		Map<String, Object> prePayReqeustMap = null;
		try {
			prePayReqeustMap = JacksonUtil.parseJson2Map(channelParameter);
		} catch (Exception e) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"支付宝支付请求数据channelData(json串格式错误)");
		}
		
		checkValidPrePayRequest(prePayReqeustMap,subPayModel);
		prePayReqeustMap.put("out_trade_no", requestMap.get("out_trade_no"));
		prePayReqeustMap.put("total_amount", requestMap.get("payAmount"));
		prePayReqeustMap.put("subject", requestMap.get("subject"));
		prePayReqeustMap.put("subPayModel", subPayModel);
		Map<String, String> bodyData = new HashMap<String, String>();
		bodyData.put("payChannel", requestMap.get("payChannel").toString());
		bodyData.put("payReason", requestMap.get("payReason").toString());
		prePayReqeustMap.put("body", JacksonUtil.map2Json(bodyData));
		return prePayReqeustMap;
	}
	
	private void checkValidPrePayRequest(Map<String, Object> prePayReqeustMap,Integer subPayModel) throws Exception {
		if (subPayModel == 18) {
			if (prePayReqeustMap.get("buyer_id") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"缺少buyer_id(买家的支付宝唯一用户号)");
		} 
	}
}
