package com.idcq.appserver.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * 属性文件读写工具
 * <p>
 * 	主要使用apache commons-configuration jar包，方便快捷对properties文件做操作
 * </p>
 * 
 * @author Administrator
 * 
 * @date 2015年3月17日
 * @time 上午9:20:47
 */
public class PropertyUtil {
	
	
	
	/**
	 * 读
	 * @param filePath
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String readProperty(String filePath,String key) throws Exception{
		PropertiesConfiguration prop = new PropertiesConfiguration(filePath);
		return (String)prop.getProperty(key);
	}
	
	/**
	 * 写
	 * @param filePath
	 * @param map
	 * @throws Exception
	 */
	public static void writeProperty(String filePath,Map map) throws Exception{
		PropertiesConfiguration prop = new PropertiesConfiguration(filePath);
		Set<String> keys = map.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = (String)map.get(key);
			prop.setProperty(key, value);
		}
		prop.save();
	}
	
	
	/**
	 * 读取配置文件
	 * 
	 * @return
	 */
	public static Properties getProperties(String file) {
		Properties pro = null;
		// 从文件mdxbu.properties中读取网元ID和模块ID信息
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			
			pro = new Properties();
			pro.load(new InputStreamReader(in, "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pro;
	}
	
	public static void main(String[] args) throws Exception{
//		String file = "src/main/java/a.properties";
		String file = "WEB-INF/classes/properties/application.properties";
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("name", "卢建平");
		writeProperty(file, map);
	}
	
}
