package com.idcq.appserver.service.pay.ccb.util;

import java.util.Map;

import org.apache.log4j.Logger;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.wxscan.MD5Util;

public class CcbPayUtil {

	private static final Logger logger = Logger.getLogger(CcbPayUtil.class);
	public static void main(String[] args) throws Exception{

	}
	
	public static String getPrePayDataFromCcb(Map<String, String> requestMap) throws Exception {
		Integer subPayModel = Integer.valueOf(requestMap.get("subPayModel").toString());
		
		String pubKey = subPayModel.equals(9) ? CommonConst.DEBIT_CARD_PUBLIC_KEY : CommonConst.CREDIT_CARD_PUBLIC_KEY;
		String posId = subPayModel.equals(9) ? CommonConst.POSID_DEBIT_CARD : CommonConst.POSID_CREDIT_CARD;
		
		StringBuilder ccbRequestParams = new StringBuilder();
		ccbRequestParams.append("MERCHANTID=" + CommonConst.MERCHANTID);
		ccbRequestParams.append("&POSID=" + posId);
		ccbRequestParams.append("&BRANCHID=" + CommonConst.BRANCHID);
		ccbRequestParams.append("&ORDERID=" + requestMap.get("ORDERID").toString());
		ccbRequestParams.append("&PAYMENT=" + requestMap.get("PAYMENT").toString());
		ccbRequestParams.append("&CURCODE=" + CommonConst.CURCODE);
		ccbRequestParams.append("&TXCODE=" + CommonConst.TXCODE);
		ccbRequestParams.append("&REMARK1=" + requestMap.get("REMARK1").toString());
		ccbRequestParams.append("&REMARK2=" + requestMap.get("REMARK2").toString());
		ccbRequestParams.append("&TYPE=" + CommonConst.TYPE);
		ccbRequestParams.append("&PUB=" + pubKey);
		ccbRequestParams.append("&GATEWAY=" + CommonConst.GATEWAY);
		ccbRequestParams.append("&CLIENTIP=");
		ccbRequestParams.append("&REGINFO=" + CommonConst.REGINFO);
		ccbRequestParams.append("&PROINFO=" );
		ccbRequestParams.append("&REFERER=");
		ccbRequestParams.append("&MAC=" + MD5Util.getMD5Str(ccbRequestParams.toString()));
		
		StringBuilder ccbPayUrl = new StringBuilder();
		ccbPayUrl.append(CommonConst.CCB_REQUEST_URL).append(ccbRequestParams.toString().replace("&PUB=" + pubKey, ""));
		return ccbPayUrl.toString();
	}
}
