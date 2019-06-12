package com.idcq.appserver.service.pay.weixin.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.wxscan.MD5Util;

public class WeiXinPayUtil {
	
	private static final Logger logger = Logger.getLogger(WeiXinPayUtil.class);
	private static CloseableHttpClient httpClient = HttpClientBuilder.create().build();
	private static Set<Integer> excludeASCIISet = new HashSet<Integer>(Arrays.asList(91,92,93,94,95,96));
	private static String lineSeparator = System.getProperty("line.separator");
	
	private static String placeOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	private static String deviceInfo = "WEB";
	private static String trade_type = "JSAPI";
	private static String fee_type = "CNY";

	public static void main(String[] args) throws Exception{
	}

	private static Properties getCommonProperties() {
		return ContextInitListener.COMMON_PROPS;
	}
	
	public static String getPrePayDataFromWeixin(Map<String, Object> requestMap) throws Exception {
		Integer subPayModel = Integer.valueOf(requestMap.get("subPayModel").toString());
		Map<String, Object> prePayMap = getPrePayInfoFromWeixin(requestMap,subPayModel);
		logger.error("weixinPrePayData:  "+prePayMap);
		if (prePayMap.get("return_code").toString().equals("FAIL")) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,prePayMap.get("return_msg").toString());
		}
		if (prePayMap.get("result_code").toString().equals("FAIL")) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,prePayMap.get("err_code_des").toString());
		}
		return getPrePayReturnData(prePayMap,subPayModel,requestMap);
	}
	private static String getPrePayReturnData(Map<String, Object> prePayMap,Integer subPayModel,Map<String, Object> requestMap) throws Exception{
		Map<String, String> channelData = new HashMap<String, String>();
		String signKey = getCommonProperties().get("key").toString();
		if (subPayModel == 17) {
			 return prePayMap.get("code_url").toString();
		} else if (subPayModel == 2) {
			Integer appCode = Integer.valueOf(requestMap.get("appcode").toString());
			signKey = getAppSignKeyByAppCode(appCode);
			channelData.put("appid", getAppIdByAppCode(appCode));
			channelData.put("partnerid", getMchIdByAppCode(appCode));
			channelData.put("prepayid", prePayMap.get("prepay_id").toString());
			channelData.put("package", "Sign=WXPay");
			channelData.put("noncestr", getNonceStr(32));
			channelData.put("timestamp", getTimeStamp(new Date()));
			channelData.put("sign", getSign(channelData,signKey));
			return JacksonUtil.map2Json(channelData);
		}else {
			StringBuilder packageBuilder = new StringBuilder();
			packageBuilder.append("prepay_id=").append(prePayMap.get("prepay_id").toString());
			channelData.put("appId", getCommonProperties().get("appId").toString());
			channelData.put("timeStamp", getTimeStamp(new Date()));
			channelData.put("nonceStr", getNonceStr(32));
			channelData.put("package", packageBuilder.toString());
			channelData.put("signType", "MD5");
			channelData.put("paySign", getSign(channelData,signKey));
			return JacksonUtil.map2Json(channelData);
		}
		
	}
	private static String getAppIdByAppCode(Integer appCode) {
		String appId = "";
		if (appCode == 1) {
			appId = getCommonProperties().get("1dcqAppId").toString();
		}else if (appCode == 2) {
			appId = getCommonProperties().get("1dgjAppId").toString();
		} else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该app没有微信支付权限，appCode:"+appCode);
		}
		return appId;
	}
	private static String getMchIdByAppCode(Integer appCode) {
		String mchId = "";
		if (appCode == 1) {
			mchId = getCommonProperties().get("1dcqMchId").toString();
		}else if (appCode == 2) {
			mchId = getCommonProperties().get("1dgjMchId").toString();
		} else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该app没有微信支付权限，appCode:"+appCode);
		}
		return mchId;
	}
	public static Map<String, Object> getPrePayInfoFromWeixin(Map<String, Object> requestMap,Integer subPayModel) throws Exception{
		Map<String, String> prePayRequestMap = buildPrePayRequestMap(requestMap,subPayModel);
		return getReturnInfoMap(placeOrderURL, map2Xml(prePayRequestMap));
	}
	private static Map<String, String> buildPrePayRequestMap(Map<String, Object> requestMap,Integer subPayModel) throws Exception{
		Map<String, String> prePayRequestMap = new HashMap<String, String>();
		String signKey = getCommonProperties().get("key").toString();
		prePayRequestMap.put("appid", getCommonProperties().get("appId").toString());
		prePayRequestMap.put("mch_id", getCommonProperties().get("mchId").toString());
		prePayRequestMap.put("nonce_str", getNonceStr(32));
		prePayRequestMap.put("body", requestMap.get("body").toString());
		if (requestMap.get("detail") != null)
			prePayRequestMap.put("detail", requestMap.get("detail").toString());
		if (requestMap.get("attach") != null)
			prePayRequestMap.put("attach", requestMap.get("attach").toString());
		prePayRequestMap.put("out_trade_no", requestMap.get("out_trade_no").toString());
		prePayRequestMap.put("fee_type",fee_type);
		prePayRequestMap.put("total_fee",requestMap.get("total_fee").toString());
		prePayRequestMap.put("notify_url",getCommonProperties().get("weixinPayNotifyUrl").toString());
		if (subPayModel == 17) {
			prePayRequestMap.put("device_info",deviceInfo);
			prePayRequestMap.put("spbill_create_ip",InetAddress.getLocalHost().getHostAddress());
			prePayRequestMap.put("trade_type","NATIVE");
		} else if (subPayModel == 2) {
			Integer appCode = Integer.valueOf(requestMap.get("appcode").toString());
			signKey = getAppSignKeyByAppCode(appCode);
			prePayRequestMap.put("appid", getAppIdByAppCode(appCode));
			prePayRequestMap.put("mch_id", getMchIdByAppCode(appCode));
			prePayRequestMap.put("spbill_create_ip",requestMap.get("spbill_create_ip").toString());
			prePayRequestMap.put("trade_type","APP");
		}else {
			prePayRequestMap.put("device_info",deviceInfo);
			prePayRequestMap.put("spbill_create_ip",requestMap.get("spbill_create_ip").toString());
			prePayRequestMap.put("trade_type",trade_type);
			prePayRequestMap.put("openid",requestMap.get("openid").toString());
		}
		prePayRequestMap.put("sign", getSign(prePayRequestMap,signKey));
		return prePayRequestMap;
	}
	private static String getAppSignKeyByAppCode(Integer appCode) {
		String signKey = "";
		if (appCode == 1) {
			signKey = getCommonProperties().get("key").toString();
		}else if (appCode == 2) {
			signKey = getCommonProperties().get("1dgjSignKey").toString();
		} else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"该app没有微信支付权限，appCode:"+appCode);
		}
		return signKey;
	}
	public static String getNonceStr(int stringLength) {
		if (stringLength <= 0)
			return "";
		Random random = new Random();
		StringBuilder nonceStr = new StringBuilder();
		
		for (int i=0; i < stringLength; i++) {
			nonceStr.append((char)getRandomNum(random, 65, 122));
		}
		return nonceStr.toString();
	}
	
	private static int getRandomNum(Random random,int start,int end){
		int randomNum = 0;
		for (;;) {
			randomNum = (int)start+random.nextInt(end - start + 1);
			if (!excludeASCIISet.contains(randomNum))
				break;
		}
		return randomNum;
	}
	
	private static String getSign(Map<String, String> paramMap,String signKey) {
		Map<String, String> lexicographicalOrderMap  = getLexicographicalOrderMap(paramMap);
		StringBuilder sortedStr = new StringBuilder();
		
		for (String name : lexicographicalOrderMap.keySet()) {
			sortedStr.append(name).append("=").append(lexicographicalOrderMap.get(name)).append("&");
		}
		
		String stringSignTemp  = sortedStr.toString()  + "key=" + signKey;
		String sign = MD5Util.getMD5Str(stringSignTemp).toUpperCase();
		return sign;
	}

	private static Map<String, String> getLexicographicalOrderMap(Map<String, String> paramMap){
		return new TreeMap<String, String>(paramMap);
	}

	private static String getTimeStamp(Date date) {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	public static String  map2Xml(Map<String, String> paramMap) {
		if (paramMap == null || paramMap.size() == 0)
			return "";
		StringBuilder xmlBuilder = new StringBuilder();
		xmlBuilder.append("<xml>");
		for (Entry<String, String> item : paramMap.entrySet()) {
			xmlBuilder.append(lineSeparator).append("<").append(item.getKey()).append(">")
			.append(item.getValue()).append("</").append(item.getKey()).append(">");
		}
		xmlBuilder.append(lineSeparator);
		xmlBuilder.append("</xml>");
		return xmlBuilder.toString();
	}
	
	public static Map<String, Object> getReturnInfoMap(String url,String xmlParam){
		 HttpPost httpost= getPostMethod(url);
		 Map<String, Object> returnInfoMap = new HashMap<String, Object>();
	     try {
			 httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			 HttpResponse response = httpClient.execute(httpost);
		     String xmlStr = EntityUtils.toString(response.getEntity(), "UTF-8");
		     returnInfoMap = doXMLParse(xmlStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnInfoMap;
	 }
	
	public static HttpPost getPostMethod(String url) {
		HttpPost pmethod = new HttpPost(url); // 设置响应头信息
		pmethod.addHeader("Connection", "keep-alive");
		pmethod.addHeader("Accept", "*/*");
		pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		pmethod.addHeader("Host", "api.mch.weixin.qq.com");
		pmethod.addHeader("X-Requested-With", "XMLHttpRequest");
		pmethod.addHeader("Cache-Control", "max-age=0");
		pmethod.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
		return pmethod;
	}
	public static Map<String, Object> doXMLParse(String strxml) throws Exception {
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		Map<String, Object> m = new HashMap<String, Object>();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}
			
			m.put(k, v);
		}
		in.close();
		return m;
	}
		
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		return sb.toString();
	}
		
	 public static InputStream String2Inputstream(String str) {
			return new ByteArrayInputStream(str.getBytes());
	}
}
