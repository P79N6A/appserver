package com.idcq.appserver.utils;

/**
 * 数组工具类
 * @author hr
 *
 */
public class ArrayUtil {
	public static final String DEFAULT_SEPARATOR = ",";
	
	/**
	 * 字符串转换为数组
	 * @param input 输入字符串
	 * @param separator 分隔符
	 * @return
	 */
	public static String[] toArray(String input, String separator){
		try {
			String regex = separator;
			if(input == null || "".equals(input)){
				return null;
			}
			if(separator == null){
				regex = DEFAULT_SEPARATOR;
			}
			return input.split(regex);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		String[] a = toArray("1,2", null); 
		System.out.println(a);
	}

}
