package com.idcq.appserver.encrypt.util;

import java.util.HashMap;
import java.util.Map;

import com.idcq.appserver.alipay.sign.MD5;
import com.idcq.appserver.encrypt.config.LdcqConfig;

/**
 * 签名校验
 * @author Administrator
 *
 */
public class LdcqVerify {


    /**
     * 验证消息是否是app发出的合法消息
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
    public static boolean verify(Map<String, String> params) {

	    String sign = "";
	    if(params.get("sign") != null) 
	    {
	    	sign = params.get("sign");
	    }
	    boolean isSign = getSignVeryfy(params, sign);

        //打印信息
       // String sWord = "isSign=" + isSign + "\n 请求参数：" + LdcqCore.createLinkString(params);
 		//System.out.println(sWord);
 		
 		return isSign;
       
    }
    
    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	private static boolean getSignVeryfy(Map<String, String> Params, String sign) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = LdcqCore.paraFilter(Params);
        //获取待签名字符串
        String preSignStr = LdcqCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
       
        isSign = MD5.verify(preSignStr, sign, LdcqConfig.key, LdcqConfig.input_charset);
		
        return isSign;
    }
	
	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", "10001");
		map.put("subject", "充值");
		map.put("fee", "0.01");
		String preSignStr = LdcqCore.createLinkString(map);
		System.err.println(MD5.sign(preSignStr, LdcqConfig.key, LdcqConfig.input_charset));
		

	}
}
