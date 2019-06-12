package com.idcq.appserver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 令牌工具
 * 
 * @author Administrator
 * 
 * @date 2015年3月7日
 * @time 下午1:40:53
 */
public class TokenUtil {
	
	/**
	 * 生产token
	 * 
	 * @return
	 */
	public static String getNewToken(){
		Random rd = new Random();
		int int8 = 10000000+rd.nextInt(89999999);
		SimpleDateFormat sdp = new SimpleDateFormat("yyyyMMddHHmmss");
		String token = int8+sdp.format(new Date());
		return token;
	}
}
