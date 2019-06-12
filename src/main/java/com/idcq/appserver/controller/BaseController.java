package com.idcq.appserver.controller;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.annotation.Check;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.sensitiveWords.SensitiveWordsUtil;

public class BaseController {

	private final static Logger logger = LoggerFactory.getLogger(BaseController.class);
	
	protected final <T> T getRequestModel(HttpServletRequest request,Class<T> clazz) throws Exception{
		String requestJson = getRequestJson(request);
		T model = null;
		try {
			model = JSON.parseObject(requestJson, clazz);
		} catch (JSONException e) {
			logger.error("请求参数类型或格式错误",e);
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,"请求参数类型或格式错误");
		}
		
		checkRequestParamValid(model, clazz);
		return model;
	}
	
	protected final Map<String, Object> getRequestMap(HttpServletRequest request) throws Exception {
		String requestJson = getRequestJson(request);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> requestMap = JSON.parseObject(requestJson, HashMap.class);
		if (requestMap == null) {
			return new HashMap<String, Object>();
		}
		
		return requestMap;
	}
	
	private final String getRequestJson(HttpServletRequest request) throws Exception {
		String requestJson = "";
		if (request.getMethod().equalsIgnoreCase("POST")) {
			requestJson = IOUtils.toString(request.getReader());
		} else if (request.getMethod().equalsIgnoreCase("GET")) {
			Map<String, String> jsonMap = convert2JsonMap(request);
			requestJson = JSON.toJSONString(jsonMap);
		}
		
		logger.info("-------接收请求JSON : {}",requestJson);
		return requestJson;
	}
	
	private final Map<String, String> convert2JsonMap(HttpServletRequest request) {
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		@SuppressWarnings("unchecked")
		Map<String, String[]> requestMap = request.getParameterMap();
		
		for (String key : requestMap.keySet()) {
			String[] values = requestMap.get(key);
			for (String value : values) {
				jsonMap.put(key, value);
			}
		}
		
		return jsonMap;
	}
	
	private final void checkRequestParamValid(Object model,Class<?> clazz) throws Exception {
		
		for (Field filed : clazz.getDeclaredFields()) {
			if (filed.isAnnotationPresent(Check.class)) { 
				Check check = filed.getAnnotation(Check.class);
				
				if (filed.isAccessible() == false) {
					filed.setAccessible(true);
				}
				
				String filedName = filed.getName();
				Object filedValue = filed.get(model);
				Class<?> filedType = filed.getType();
				
				if (check.required() && (filedValue == null || filedValue.equals(""))) { 
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,filedName+"不能为空");
				}
				
				if (StringUtils.isNotBlank(check.pattern()) && 
					(!Pattern.compile(check.pattern()).matcher(filedValue.toString()).matches())) {
					
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,filedName+"格式错误");
				}
			
				if (check.recurse()) {
					if (filedType == List.class) {
						@SuppressWarnings("unchecked")
						List<Object> itemList = (List<Object>)filedValue;
						for (Object item : itemList) {
							checkRequestParamValid(item, item.getClass());
						}
					} else {
						checkRequestParamValid(filedValue, filedType);
					}
				}
				
				if (check.sensitive()) {
					if (filedValue == null) {
						continue;
					}
					
					SensitiveWordsUtil.checkSensitiveWords(filedName, filedValue);
				}
			}
		}
	}
}
