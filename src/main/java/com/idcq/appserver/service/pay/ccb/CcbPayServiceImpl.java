package com.idcq.appserver.service.pay.ccb;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.pay.ccb.util.CcbPayUtil;
import com.idcq.appserver.service.pay.impl.ThirdPayServiceImpl;
import com.idcq.appserver.utils.NumberUtil;

@Service("payChannel:3")
public class CcbPayServiceImpl extends ThirdPayServiceImpl {

	@Override
	protected String getThirdPayChannelData(Map<String, Object> requestMap,
			String channelParameter, Integer subPayModel) throws Exception {
		
		return CcbPayUtil.getPrePayDataFromCcb(buildPrePayRequestMap(requestMap, channelParameter, subPayModel));
	}

	
	private Map<String, String> buildPrePayRequestMap(
			Map<String, Object> requestMap, String channelParameter,
			Integer subPayModel) throws Exception {
		
		if (!(subPayModel.equals(9) || subPayModel.equals(10))) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"建行子支付方式无效，发起子支付方式："+subPayModel);
		}
		Map<String, String> prePayReqeustMap = new HashMap<String, String>();
		prePayReqeustMap.put("ORDERID", requestMap.get("out_trade_no").toString());
		prePayReqeustMap.put("PAYMENT", NumberUtil.formatDoubleStr2BigDecimalStr(
														requestMap.get("payAmount").toString(),2));
		
		prePayReqeustMap.put("REMARK1", requestMap.get("payChannel").toString());
		prePayReqeustMap.put("REMARK2", requestMap.get("payReason").toString());
		prePayReqeustMap.put("subPayModel", subPayModel.toString());
		return prePayReqeustMap;
	}
}
