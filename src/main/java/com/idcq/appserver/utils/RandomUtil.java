package com.idcq.appserver.utils;

import java.util.Random;

/**
 * 随机数工具
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午8:35:13
 */
public class RandomUtil {
	private static Random randomNum=new Random();
	/**
	 * 随机生成整数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getIntNum(int start,int end){
		Random rd = new Random();
		int num = (int)start+rd.nextInt(end);
		return num;
	}
	
	/**
	 * 获取随机字符串
	* @Title: getRandomStrByNum 
	* @param @param num
	* @param @return
	* @return String    返回类型 
	* @throws
	 */
	public static String getRandomStrByNum(int num){
		StringBuffer buffer=new StringBuffer("");
		for(int i=1;i<=num;i++){
			buffer.append(randomNum.nextInt(9));
		}
		return buffer.toString();
	}
}
