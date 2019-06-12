package com.idcq.appserver.service.pay.ali.util;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipaySystemOauthTokenRequest;
import com.alipay.api.request.AlipayTradeCreateRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipaySystemOauthTokenResponse;
import com.alipay.api.response.AlipayTradeCreateResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.idcq.appserver.alipay.config.AlipayConfig;
import com.idcq.appserver.alipay.sign.RSA;
import com.idcq.appserver.alipay.util.AlipayNotify;
import com.idcq.appserver.aliscanpay.factory.AlipayAPIClientFactory;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;

public class AliPayUtil {
	private final static Logger logger = LoggerFactory.getLogger(AliPayUtil.class);
	
	private static final String directPayGateWay = "https://mapi.alipay.com/gateway.do?";
	
	private static final AlipayClient payClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
																			AlipayConfig.appId,AlipayConfig.our_private_key,
																			"json","GBK",AlipayConfig.ali_open_public_key);
	
	public static void main(String[] args) throws Exception{
		System.out.println(queryOauthToken("98f1c857afb449148a3c0858bff2RX72"));
	}
	
	private static Properties getCommonProperties() {
		return ContextInitListener.COMMON_PROPS;
	}
	public static String getPrePayDataFromAli(Map<String, Object> requestMap) throws Exception {
		String out_trade_no = requestMap.get("out_trade_no").toString();
		String total_amount = NumberUtil.formatDoubleStr2BigDecimalStr(requestMap.get("total_amount").toString(), 2);
		String body = requestMap.get("body").toString();
		String subject = requestMap.get("subject").toString();
		
		Integer subPayModel = Integer.valueOf(requestMap.get("subPayModel").toString());
		String prePayData = "";
		if (subPayModel == 17) {
			prePayData =  getQrCode(out_trade_no, total_amount, subject, body);
		} else if (subPayModel == 2) {
			prePayData =  getPayDataByApp(out_trade_no, total_amount, subject, body);
		} else if (subPayModel == 13) {
			prePayData =  getPayDataByDirectPay(out_trade_no, total_amount, subject, body);
		} else if (subPayModel == 18) {
			prePayData = getTradeNo(out_trade_no, total_amount, subject, body,requestMap.get("buyer_id").toString());
		}
		else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"支付宝支付暂时只支持扫码支付,移动支付,即时到账支付");
		}
		return prePayData;
	}
	
	private static String getTradeNo(String out_trade_no,String total_amount,
			  							String subject,String body,String buyer_id) throws Exception {

		AlipayTradeCreateRequest request = new AlipayTradeCreateRequest();
		request.setBizContent(buildBizContent(out_trade_no, total_amount, subject, body,buyer_id,18));
		request.setNotifyUrl(getCommonProperties().getProperty("aliPayNotifyUrl"));
		AlipayTradeCreateResponse response = null;
		try {
			response =  payClient.execute(request);
		} catch (AlipayApiException e) {
			logger.error("请求支付宝扫商户二维码支付异常",e);
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"请求支付宝扫商户二维码支付异常:"+e.getMessage());
		}
		
		if (null != response) {
			if (response.getCode().equals("10000")) {
				logger.info("请求支付宝支付成功       商户订单号：{}",response.getOutTradeNo());
				logger.info("请求支付宝支付成功       支付宝交易号：{}",response.getTradeNo());
			} 
			else {
				logger.error("请求支付宝支付失败       错误信息：{}",response.getMsg());
				logger.error("请求支付宝支付失败       错误码：{}",response.getSubCode());
				logger.error("请求支付宝支付失败       错误描述：{}",response.getSubMsg());
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,response.getSubMsg());
			}
		}
		else {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"请求支付宝扫商户二维码返回数据为空");
		}
		return response.getTradeNo();
	}
	
	public static String queryTradeNo(String outTradeNo) throws Exception {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.setBizContent("{" +
				"    \"out_trade_no\":\""+outTradeNo+"\"" +
				"  }");
		
		AlipayTradeQueryResponse response = payClient.execute(request);
		
		if (!response.isSuccess()) {
			logger.error("查询支付宝交易失败    错误信息：{}，错误码：{},错误描述：{},",response.getMsg(),response.getSubCode(),response.getSubMsg());
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,response.getSubMsg());
		}
		
		return response.getTradeNo();
	}
	
	public static String queryOauthToken(String code) throws Exception {
		if (StringUtils.isBlank(code)) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"授权码不能为空");
		}
		
		Map<String, String> oauthToken = new HashMap<String, String>();
		
		AlipaySystemOauthTokenRequest request = new AlipaySystemOauthTokenRequest();
		request.setGrantType("authorization_code");
		request.setCode(code);
		AlipaySystemOauthTokenResponse response = null;
		try {
			response =  payClient.execute(request);
		} catch (AlipayApiException e) {
			logger.error("获取支付宝授权访问令牌异常",e);
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"获取支付宝授权访问令牌异常:"+e.getMessage());
		}
		
		if (!response.isSuccess()) {
			logger.error("获取支付宝授权访问令牌失败       错误信息：{}",response.getMsg());
			logger.error("获取支付宝授权访问令牌失败       错误码：{}",response.getSubCode());
			logger.error("获取支付宝授权访问令牌失败       错误描述：{}",response.getSubMsg());
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,response.getSubMsg());
		}
		
		oauthToken.put("user_id", response.getUserId());
		return JacksonUtil.map2Json(oauthToken);
	}
	private static String getPayDataByApp(String out_trade_no,String total_amount,
										  String subject,String body) throws Exception {
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("service", getFormatValue("mobile.securitypay.pay"));
		param.put("partner", getFormatValue(AlipayConfig.partner));
		param.put("_input_charset",getFormatValue(AlipayConfig.input_charset));
		param.put("notify_url", getFormatValue(getCommonProperties().
											   getProperty("aliPayNotifyUrl")));
		param.put("out_trade_no", getFormatValue(out_trade_no));
		param.put("subject", getFormatValue(subject));
		param.put("payment_type", getFormatValue("1"));
		param.put("seller_id", getFormatValue(AlipayConfig.seller_email));
		param.put("total_fee", getFormatValue(total_amount));
		param.put("body", getFormatValue(body));
		
		param.put("sign", getFormatValue(getSign(param)));
		param.put("sign_type", getFormatValue("RSA"));
		return getKVString(getLexicographicalOrderMap(param));
	}
	
	private static String getPayDataByDirectPay(String out_trade_no,String total_amount,
			  String subject,String body) throws Exception {

		Map<String, String> directPayData = buildDirectPayData(out_trade_no, total_amount, subject, body);
		return getAliPayUrl(getKVString(directPayData));
	}
	
	private static String getAliPayUrl(String directPayParam) {
		StringBuilder payUrl = new StringBuilder();
		payUrl.append(directPayGateWay).append(directPayParam);
		return payUrl.toString();
	}
	
	private static Map<String, String> buildDirectPayData(String out_trade_no,String total_amount,
			  												String subject,String body) throws Exception{
		
		Map<String, String> param = new TreeMap<String, String>();
		param.put("service", "create_direct_pay_by_user");
		param.put("partner", AlipayConfig.partner);
		param.put("_input_charset",AlipayConfig.input_charset);
		param.put("notify_url", getCommonProperties().getProperty("aliPayNotifyUrl"));
		//param.put("return_url", "");
		param.put("out_trade_no", out_trade_no);
		param.put("subject", subject);
		param.put("payment_type", "1");
		param.put("total_fee", total_amount);
		param.put("seller_email", AlipayConfig.seller_email);
		param.put("extra_common_param", body);
		
		param.put("sign", getSign(param));
		param.put("sign_type", "RSA");
		
		return param;
	}
	private static String getFormatValue(String value) {
		return "\""+value+"\"";
	}
	
	private static String getQrCode(String out_trade_no,String total_amount,
								 	String subject,String body) throws Exception{
		AlipayTradePrecreateResponse response = requestAliPay(out_trade_no, 
															  total_amount,
															  subject,body);
		String qrCode = null;
		if (null != response && response.isSuccess()) {
			if (response.getCode().equals("10000")) {
				logger.info("请求支付宝支付成功       商户订单号:"+response.getOutTradeNo());
				logger.info("请求支付宝支付成功           二维码值:"+response.getQrCode());
				qrCode = response.getQrCode();
			} 
			else {
				logger.error("请求支付宝支付失败       错误码:"+response.getSubCode());
				logger.error("请求支付宝支付失败       错误描述:"+response.getSubMsg());
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,response.getSubMsg());
			}
		}
		else {
			logger.error("请求支付宝支付 isSuccess:"+response.isSuccess());
			logger.error("请求支付宝支付 处理Msg:"+response.getMsg());
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,response.getMsg());
		}
		return qrCode;
	}
	private static AlipayTradePrecreateResponse requestAliPay(String out_trade_no,String total_amount,String subject,String body) throws Exception{
		AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.setBizContent(buildBizContent(out_trade_no, total_amount, subject, body,null,17));
		request.setNotifyUrl(getCommonProperties().getProperty("aliPayNotifyUrl"));
		AlipayTradePrecreateResponse response = null;
		try {
			response = alipayClient.execute(request);
			
			logger.info("请求支付宝支付 响应body:"+response.getBody());
			logger.info("请求支付宝支付 isSuccess:"+response.isSuccess());
			logger.info("请求支付宝支付 处理Msg:"+response.getMsg());
		} catch (AlipayApiException e) {
			logger.error("请求支付宝支付异常",e);
		}
		return response;
	}
	
	private static String buildBizContent(String out_trade_no,String total_amount,String subject,String body,String buyer_id,Integer subPayModel) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time_expire= sdf.format(System.currentTimeMillis()+24*60*60*1000);

		Map<String, String> bizContentData = new HashMap<String, String>();
		bizContentData.put("out_trade_no", out_trade_no);
		bizContentData.put("total_amount", total_amount);
		bizContentData.put("discountable_amount", "0.00");
		bizContentData.put("subject", subject);
		bizContentData.put("body", body);
		if (subPayModel == 17) {
			bizContentData.put("operator_id", "op001");
			bizContentData.put("store_id", "pudong001");
			bizContentData.put("terminal_id", "t_001");
			bizContentData.put("time_expire", time_expire);
		}
		
		if (subPayModel == 18) {
			if (buyer_id == null) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"缺少buyer_id(买家的支付宝唯一用户号)");
			}
			bizContentData.put("buyer_id", buyer_id);
		}
		
		String bizContent = JacksonUtil.map2Json(bizContentData);
		logger.info("bizContent:{}", bizContent);
		return bizContent;
	}
	
	private static String getSign(Map<String, String> paramMap) throws Exception{
		Map<String, String> lexicographicalOrderMap  = getLexicographicalOrderMap(paramMap);
		String sign = RSA.sign(getKVString(lexicographicalOrderMap), AlipayConfig.ali_private_key, AlipayConfig.input_charset);
		sign = URLEncoder.encode(sign, "UTF-8");
		return sign;
	}
	private static String getKVString(Map<String, String> map) {
		StringBuilder sortedStr = new StringBuilder();
		
		for (String name : map.keySet()) {
			sortedStr.append(name).append("=").append(map.get(name)).append("&");
		}
		int len = sortedStr.length();
		String preSignString  = sortedStr.substring(0, len-1);
		return preSignString;
	}
	private static Map<String, String> getLexicographicalOrderMap(Map<String, String> paramMap){
		return new TreeMap<String, String>(paramMap);
	}
	public static Boolean verify(Map<String, String> notifyParamMap) {
		Boolean aliPaySign = AlipayNotify.verify(notifyParamMap, AlipayConfig.sign_type_RSA);
		Boolean aliScanSign = com.idcq.appserver.aliscanpay.util.AlipayNotify.verify(notifyParamMap);
		return aliPaySign || aliScanSign;
	}
}
