/**
 * Assert.java	  V1.0   2013-12-31 下午1:57:57
 *
 * Copyright GTA Information System Co. ,Ltd. All rights reserved.
 *
 * Modification history(By    Time    Reason):
 * 
 * Description:
 */

package com.idcq.appserver.utils;


public abstract class Assert {

	/**
	 * 
	 * 功能描述：判断对象是否空，抛出异常
	 *
	 * @author  biyun.huang
	 * <p>创建日期 ：2014年6月9日 下午1:10:49</p>
	 *
	 * @param object
	 * @param message
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public static void notNull(Object object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 
	 * 功能描述：判断字符串是否为空，抛出异常
	 *
	 * @author  biyun.huang
	 * <p>创建日期 ：2014年6月9日 下午1:11:49</p>
	 *
	 * @param s
	 * @param message
	 *
	 * <p>修改历史 ：(修改人，修改时间，修改原因/内容)</p>
	 */
	public static void notEmpty(String s, String message) {
		if (s == null || "".equals(s)) {
			throw new IllegalArgumentException(message);
		}
	}
}
