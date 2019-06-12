package com.idcq.appserver.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.HttpEntity;

/**
 * jackson工具
 * 
 * @author Administrator
 * 
 * @date 2015年3月10日
 * @time 上午10:43:33
 */
public class JacksonUtil {
	private static final Log logger = LogFactory.getLog(JacksonUtil.class);
	public static final String NULL_JSONARRAY="[]";
	
	public static void main(String[] args) throws Exception{
		String d = "好好的好";
		ObjectMapper m = new ObjectMapper();
		String s = m.writeValueAsString(d);
		System.out.println(s);
		
	}
	
	public static String objToString(Object obj)  throws Exception{
		ObjectMapper map = new ObjectMapper();
		String jsonStr = map.writeValueAsString(obj);
		return jsonStr;
	}
	
	public static <T> T jsonToObject(String jsonStr,Class<T> clazz,String dateFormat)  throws Exception{
		ObjectMapper map = new ObjectMapper();
		if(!StringUtils.isBlank(dateFormat)){
			map.setDateFormat(new SimpleDateFormat(dateFormat));
		}
		return map.readValue(jsonStr, clazz);
	}
	
	public static Object postJsonToObj(HttpEntity<String> entity,Class clazz,String dateFormat)  
			throws Exception{
		String json = entity.getBody();
		logger.info("#####接收数据 json : "+json);
		Object obj = jsonToObject(json,clazz,dateFormat);
		return obj;
	}
	public static Map<String, String> postJsonToMap(HttpEntity<String> entity)  
			throws Exception{
		String json = entity.getBody();
		logger.info("#####接收数据 json : "+json);
		return simpleParseJson2Map(json);
	}
	public static Object postJsonToObj(HttpEntity<String> entity,Class clazz)  
			throws Exception{
		String json = entity.getBody();
		logger.info("##### json : "+json);
		Object obj = jsonToObject(json,clazz,null);
		return obj;
	}
	
	public static Map<String, String> simpleParseJson2Map(String jsonStr) throws Exception {
		Map<String, String> resultmap = new HashMap<String, String>();
		com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(jsonStr);
		for (Object key : json.keySet()) {
			Object value = json.get(key);
			if (value != null) {
				resultmap.put(key.toString(), value.toString());
			}
		}
		return resultmap;
	}
	/**
	 * 在没有实体类，以Map实例返回时，调用此方法，能将时间格式化为自己想要的格式
	 * @param obj
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String objectToString(Object obj,String format) throws Exception{
		ObjectMapper map = new ObjectMapper();
		if (null != format) {
			map.setDateFormat(new SimpleDateFormat(format));
		}
		String jsonStr = map.writeValueAsString(obj);
		return jsonStr;
	}
	
	public static Map<String, Object> parseJson2Map(String jsonStr) throws Exception {
	    if (StringUtils.isEmpty(jsonStr))
        {
            return new HashMap<String, Object>();
        }
		Map<String, Object> resultmap = new HashMap<String, Object>();
		JSONObject json = JSONObject.fromObject(jsonStr);
		for (Object key : json.keySet()) {
			Object value = json.get(key);
			if (value instanceof JSONArray) {
				List<Map<String, Object>> valueList = new ArrayList<Map<String,Object>>();
				Iterator<JSONObject> iterator = ((JSONArray)value).iterator();
				while (iterator.hasNext()) {
					JSONObject child = iterator.next();
					valueList.add(parseJson2Map(child.toString()));
				}
				resultmap.put(key.toString(),valueList);
			}
			else {
				resultmap.put(key.toString(), value);
			}
		}
		return resultmap;
	}
	//1262 7703 3696 0166 52
	
//	000M123456180119141631056c19f1
	
	public static String map2Json(Map<String, String> jsonMap) throws Exception {
		JSONObject json = JSONObject.fromObject(jsonMap);
		return json.toString();
	}
	
	public static String object2Json(Object jsonMap) throws Exception {
		JSONObject json = JSONObject.fromObject(jsonMap);
		return json.toString();
	}
}
