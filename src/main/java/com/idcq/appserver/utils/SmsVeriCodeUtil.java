package com.idcq.appserver.utils;

import java.util.Random;

public class SmsVeriCodeUtil {
	/**
	 * 随机生成验证码
	 * 
	 * @param start 开始数值
	 * @param end 结束数值
	 * @return
	 */
	public static int getIntNum(int start,int end){
		Random rd = new Random();
		int num = (int)start+rd.nextInt(end);
		return num;
	}
}	
