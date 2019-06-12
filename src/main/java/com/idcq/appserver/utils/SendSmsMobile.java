package com.idcq.appserver.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class SendSmsMobile {
	static String URL = "http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend";
	static String URL1 = "http://sdk2.entinfo.cn:8061/webservice.asmx/mdsmssend";
	//http://sdk.entinfo.cn:8061/webservice.asmx/mdsmssend
	static String SN = "SDK-WSS-010-07020";
	//SN = "SDK-WSS-010-07020";
	static String PWD = "8@8c8b@4";
	//PWD = "8@8c8b@4";
	public static void main(String[] args) throws UnsupportedEncodingException {
		String rrid = new SimpleDateFormat("yyyyMMddHHmmssSSSS").format(new Date());
		System.out.println(rrid);
		SN = "SDK-WSS-010-07020";
		PWD = "8@8c8b@4";
		/*
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sn",SN));
		params.add(new BasicNameValuePair("pwd",MD5Util.getMD5Str(SN+PWD).toUpperCase()));
		params.add(new BasicNameValuePair("mobile","13684921276"));
		params.add(new BasicNameValuePair("content", URLEncoder.encode("您的验证码是：589548，请勿告诉任何人您收到的验证码", "utf-8")));
		params.add(new BasicNameValuePair("ext", ""));
		params.add(new BasicNameValuePair("stime", ""));
		params.add(new BasicNameValuePair("rrid", ""));
		params.add(new BasicNameValuePair("msgfmt", ""));
		*/
//		Map<String, String> formParams = new HashMap<String, String>();
//		formParams.put("mobile", "15899773751,18503052895,18319034360,13684921276,13424183952,13902479582,18566232008,18657981038,13714050306,13510120404");
//		formParams.put("Sn", SN);
//		formParams.put("Pwd", MD5Util.getMD5Str(SN+PWD).toUpperCase());
//		formParams.put("content",URLEncoder.encode("一点传奇祝大家：双节快乐，身体健康，万事如意，发大财，早日走向人生巅峰【1点传奇】", "utf-8"));
//		formParams.put("ext", "");
//		formParams.put("stime", "");
//		formParams.put("rrid", "");
//		formParams.put("msgfmt", "");
//		System.out.println(formParams);
//		try {
//			HttpClientUtils httpUtil = HttpClientUtils.getInstance();
//			System.out.println(httpUtil.doPost(URL, formParams));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		//201124520673385962
		send();
	}
	
	public static void send(){
		String url = "http://124.172.250.160/WebService.asmx/mt";
		Map<String, String> formParams = new HashMap<String, String>();
		formParams.put("mobile", "15810403739");
		formParams.put("sn", "SDK-ZQ-SJHY-0817");
		formParams.put("pwd", "n871vcz");
		formParams.put("content", "一点明月寄相思，传奇千里送真情！在全体家人的努力下，公司产品全面落地、伦敦上市在即，特遥祝您及家人：阖家团圆，幸福美满！");
		try {
			HttpClientUtils httpUtil = HttpClientUtils.getInstance();
			System.out.println(httpUtil.doPost(url, formParams));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
