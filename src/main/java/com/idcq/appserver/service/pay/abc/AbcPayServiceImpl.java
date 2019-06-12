package com.idcq.appserver.service.pay.abc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.pay.abc.util.AbcPayUtil;
import com.idcq.appserver.service.pay.impl.ThirdPayServiceImpl;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;

@Service("payChannel:6")
public class AbcPayServiceImpl extends ThirdPayServiceImpl {

	@Override
	protected String getThirdPayChannelData(Map<String, Object> requestMap,
											String channelParameter, 
											Integer subPayModel) throws Exception {
		
		
		return AbcPayUtil.getPrePayDataFromAbc(buildPrePayRequestMap(requestMap, channelParameter, subPayModel));
	}

	
	private Map<String, String> buildPrePayRequestMap(
			Map<String, Object> requestMap, String channelParameter,
			Integer subPayModel) throws Exception {
		
		if (!(subPayModel.equals(9) || subPayModel.equals(10))) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"农行子支付方式无效，发起子支付方式："+subPayModel);
		}
		Map<String, String> prePayReqeustMap = new HashMap<String, String>();
		prePayReqeustMap.put("OrderNo", requestMap.get("out_trade_no").toString());
		prePayReqeustMap.put("OrderAmount", NumberUtil.formatDoubleStr2BigDecimalStr(
														requestMap.get("payAmount").toString(),2));
		Date now = new Date();
		prePayReqeustMap.put("OrderDate", DateUtils.format(now, "yyyy/MM/dd"));
		prePayReqeustMap.put("OrderTime", DateUtils.format(now, "HH:mm:ss"));
		prePayReqeustMap.put("OrderDesc", requestMap.get("subject") == null ? "充值" : requestMap.get("subject").toString());
		prePayReqeustMap.put("subPayModel", subPayModel.toString());
		prePayReqeustMap.put("clientSystem", requestMap.get("clientSystem").toString());
		StringBuilder bodyData = new StringBuilder();
		bodyData.append("payChannel=").append(requestMap.get("payChannel").toString()).
				append(",").append("payReason=").append(requestMap.get("payReason").toString());
		prePayReqeustMap.put("MerchantRemarks", bodyData.toString());
		return prePayReqeustMap;
	}
}
