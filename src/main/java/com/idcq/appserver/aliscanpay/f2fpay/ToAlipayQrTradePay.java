/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.idcq.appserver.aliscanpay.f2fpay;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.google.gson.Gson;
import com.idcq.appserver.aliscanpay.factory.AlipayAPIClientFactory;


public class ToAlipayQrTradePay {
	
	
	private static final Log logger = LogFactory.getLog(ToAlipayQrTradePay.class);
	public static String NOTIFY_URL=null;
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	
		
		//201504210011041195
		String out_trade_no="20150528207429889987655"; //商户唯一订单号
		String total_amount="0.01";
		String subject = "测试扫码付订单";
		
		qrPay(out_trade_no,total_amount,subject);
	}
	
	
	/**
	 * 条码下单支付
	 * @param out_trade_no
	 * @param auth_code
	 * @author jinlong.rhj
	 * @date 2015年4月28日
	 * @version 1.0
	 * @return 
	 */
	public static String qrPay(String out_trade_no,String total_amount,String subject) {
		Gson gson=new Gson();
		Map<String,String>result=new HashMap<String,String>();
		result.put("code","10000");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time_expire= sdf.format(System.currentTimeMillis()+24*60*60*1000);
			
			StringBuilder sb = new StringBuilder();
			sb.append("{\"out_trade_no\":\"" + out_trade_no + "\",");
			sb.append("\"total_amount\":\""+total_amount+"\",\"discountable_amount\":\"0.00\",");
			sb.append("\"subject\":\""+subject+"\",\"body\":\"test\",");
			sb.append("\"goods_detail\":[{\"goods_id\":\"00000000\",\"goods_name\":\"一点传奇收银\",\"price\":\""+total_amount+"\",\"quantity\":\"1\"}],");
			sb.append("\"operator_id\":\"op001\",\"store_id\":\"pudong001\",\"terminal_id\":\"t_001\",");
			sb.append("\"time_expire\":\""+time_expire+"\"}");
	
			AlipayClient alipayClient = AlipayAPIClientFactory.getAlipayClient();
	
			// 使用SDK，构建群发请求模型
			AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
			request.setBizContent(sb.toString());
			if(NOTIFY_URL==null){
				NOTIFY_URL="http://pczhang.1dcq.com:8688/appServer/interface/aliScanPayNotify";//默认本机器
			}
			request.setNotifyUrl(NOTIFY_URL);
			logger.error("扫码支付回调地址"+NOTIFY_URL);
			//request.setNotifyUrl("http://61.144.170.171:8690/appServer/interface/aliScanPayNotify");
//			/request.setNotifyUrl("http://61.144.170.170:9091/appServer/interface/aliScanPayNotify");
	//		request.putOtherTextParam("ws_service_url", "http://unitradeprod.t15032aqcn.alipay.net:8080");
			AlipayTradePrecreateResponse response = null;
	
				// 使用SDK，调用交易下单接口
				response = alipayClient
						.execute(request);
					
				System.out.println(response.getBody());
				System.out.println(response.isSuccess());
				System.out.println(response.getMsg());
				// 这里只是简单的打印，请开发者根据实际情况自行进行处理
				if (null != response && response.isSuccess()) {
					if (response.getCode().equals("10000")) {
						result.put("code","0");
						result.put("qrcode", response.getQrCode());
						result.put("outTradeNo",response.getOutTradeNo());
						System.out.println("商户订单号："+response.getOutTradeNo());
						System.out.println("二维码值："+response.getQrCode());//商户将此二维码值生成二维码，然后展示给用户，用户用支付宝手机钱包扫码完成支付
						//二维码的生成，网上有许多开源方法，可以参看：http://blog.csdn.net/feiyu84/article/details/9089497
						
					} else {
	
					//打印错误码
					System.out.println("错误码："+response.getSubCode());
					System.out.println("错误描述："+response.getSubMsg());
					}
			}
		} catch (AlipayApiException e) {
			result.put("msg", "请求产生了异常");
			e.printStackTrace();
		}
		return gson.toJson(result);
	}
	
	


}
