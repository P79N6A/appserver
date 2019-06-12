package com.idcq.appserver.service.pay.abc.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.abc.trustpay.client.Constants;
import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.PaymentRequest;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;

public class AbcPayUtil {

	private static final Logger logger = Logger.getLogger(AbcPayUtil.class);
	private static final String SUCCESS = "0000";
	public static void main(String[] args) throws Exception{

	}
	private static Properties getCommonProperties() {
		return ContextInitListener.COMMON_PROPS;
	}
	public static String getPrePayDataFromAbc(Map<String, String> requestMap) throws Exception {
		PaymentRequest payRequest = buildAbcPayRequest(requestMap);
		JSON payResult = postPayRequest(payRequest);
		return getPayData(payResult);
	}
	
	@SuppressWarnings("unchecked")
	private static PaymentRequest buildAbcPayRequest(Map<String, String> requestMap) throws Exception {
		Integer subPayModel = Integer.valueOf(requestMap.get("subPayModel").toString());
		
		PaymentRequest payRequest = new PaymentRequest();
		payRequest.dicOrder.put("PayTypeID", Constants.PAY_TYPE_DIRECTPAY);
		payRequest.dicOrder.put("OrderDate", requestMap.get("OrderDate"));
		payRequest.dicOrder.put("OrderTime", requestMap.get("OrderTime"));
		payRequest.dicOrder.put("OrderDesc", requestMap.get("OrderDesc"));
		payRequest.dicOrder.put("OrderNo", requestMap.get("OrderNo"));
		payRequest.dicOrder.put("CurrencyCode", "156");
		payRequest.dicOrder.put("OrderAmount", requestMap.get("OrderAmount"));
		payRequest.dicOrder.put("InstallmentMark", Constants.INSTALLMENTMARK_NO);
		payRequest.dicOrder.put("CommodityType", "0202");
		
		LinkedHashMap<String, String> orderItem = new LinkedHashMap<String, String>();
		orderItem.put("ProductName", requestMap.get("OrderDesc"));
		payRequest.orderitems.put(1, orderItem);
		
		payRequest.dicRequest.put("PaymentType", subPayModel.equals(9) ? Constants.PAY_TYPE_ABC : Constants.PAY_TYPE_CREDIT);
		payRequest.dicRequest.put("PaymentLinkType", getAbcPayLinkType(Integer.valueOf(requestMap.get("clientSystem").toString())));
		payRequest.dicRequest.put("NotifyType", "1");
		payRequest.dicRequest.put("ResultNotifyURL", getCommonProperties().get("abcPayNotifyUrl"));
		payRequest.dicRequest.put("IsBreakAccount", Constants.IsBreakAccount_NO);
		payRequest.dicRequest.put("MerchantRemarks", requestMap.get("MerchantRemarks"));
		return payRequest;
				
	}
	
	private static String getAbcPayLinkType(Integer clientSystem) {
		if (clientSystem.equals(2) || clientSystem.equals(3)) {
			return Constants.PAY_LINK_TYPE_MOBILE;
		}else {
			return Constants.PAY_LINK_TYPE_NET;
		}
		
	}
	private static JSON postPayRequest(PaymentRequest payRequest) throws Exception {
		return payRequest.postRequest();
	}
	
	private static String getPayData(JSON payResult) throws Exception {
		String returnCode = payResult.GetKeyValue("ReturnCode");
		String errorMessage = payResult.GetKeyValue("ErrorMessage");
		String paymentURL = "";
		
		if (SUCCESS.equals(returnCode)) {
			paymentURL = payResult.GetKeyValue("PaymentURL");
		}else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,errorMessage);
		}
		return paymentURL;
	}
}
