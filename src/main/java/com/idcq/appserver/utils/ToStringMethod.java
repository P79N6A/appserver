package com.idcq.appserver.utils;

import java.lang.reflect.Field;

/**
 * toString通用调用工具
 * 
 * @author Administrator
 * 
 */
public class ToStringMethod {
	public static String toString(Object obj) {
		try {
			if (obj == null)
				return null;
			Field[] fields = obj.getClass().getDeclaredFields();
			StringBuffer buffer = new StringBuffer();
			buffer.append("{");
			for (int i = 0; i < fields.length && fields.length > 0; i++) {
				fields[i].setAccessible(true);
				if ("serialVersionUID".equals(fields[i].getName())) {
					continue;
				}

				if (i == fields.length - 1) {
					buffer.append(fields[i].getName() + ":"
							+ fields[i].get(obj) + "}");
				} else {
					buffer.append(fields[i].getName() + ":"
							+ fields[i].get(obj) + ",");
				}
			}
			return buffer.toString();
		} catch (Exception e) {
			return null;
		}
	}

}
