package com.idcq.appserver.service.pay.weixin;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.pay.impl.ThirdPayServiceImpl;
import com.idcq.appserver.service.pay.weixin.util.WeiXinPayUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 微信支付service
 * @author Administrator
 *
 */
@Service("payChannel:2")
public class WeiXinPayServiceImpl extends ThirdPayServiceImpl {

	@Override
	protected String getThirdPayChannelData(Map<String, Object> requestMap, 
											String channelParameter,
											Integer subPayModel)
											throws Exception {
		if (channelParameter == null)
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
										"微信支付请求数据不能为空");
		
		return WeiXinPayUtil.getPrePayDataFromWeixin(
							buildPrePayRequestMap(requestMap, channelParameter,subPayModel));
	}
	
	private Map<String, Object> buildPrePayRequestMap(
			Map<String, Object> requestMap, String channelParameter,
			Integer subPayModel) throws Exception {
		Map<String, Object> prePayReqeustMap = null;
		try {
			prePayReqeustMap = JacksonUtil.parseJson2Map(channelParameter);
		} catch (Exception e) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"微信支付请求数据channelData(json串格式错误)");
		}
		checkValidPrePayRequest(prePayReqeustMap,subPayModel);
		String totalAmount = NumberUtil.multiply((Double)requestMap.get("payAmount"), Double.valueOf(100)).toString();
		String totalFee = NumberUtil.formatDoubleStr2BigDecimalStr(totalAmount,1);
		int indexOfDecimalPoint = totalFee.indexOf(".");
		prePayReqeustMap.put("total_fee", totalFee.substring(0, indexOfDecimalPoint));
		prePayReqeustMap.put("out_trade_no", requestMap.get("out_trade_no"));
		
		Map<String, String> attchData = new HashMap<String, String>();
		attchData.put("payChannel", requestMap.get("payChannel").toString());
		attchData.put("payReason", requestMap.get("payReason").toString());
		prePayReqeustMap.put("attach", JacksonUtil.map2Json(attchData));
		prePayReqeustMap.put("subPayModel", subPayModel);
		return prePayReqeustMap;
	}
	
	private void checkValidPrePayRequest(Map<String, Object> prePayReqeustMap,Integer subPayModel) throws Exception {
		if (subPayModel == 17) {
			if (prePayReqeustMap.get("body") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"微信支付请求数据body不能为空");
		} else if (subPayModel == 2) {
			if (prePayReqeustMap.get("appcode") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"发起微信支付appcode不能为空");
			if (prePayReqeustMap.get("body") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"微信支付请求数据body不能为空");
			if (prePayReqeustMap.get("spbill_create_ip") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"微信支付请求数据spbill_create_ip不能为空");
		}else {
			if (prePayReqeustMap.get("openid") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"微信支付请求数据openid不能为空");
			if (prePayReqeustMap.get("body") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"微信支付请求数据body不能为空");
			if (prePayReqeustMap.get("spbill_create_ip") == null) 
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,"微信支付请求数据spbill_create_ip不能为空");
		}
	}
}
