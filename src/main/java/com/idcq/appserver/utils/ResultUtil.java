package com.idcq.appserver.utils;

import com.idcq.appserver.dto.ResultDto;


/**
 * 结果工具
 * 
 * @author Administrator
 * 
 * @date 2015年3月5日
 * @time 上午9:37:09
 * @param <T>
 */
public class ResultUtil{
	
	/**
	 * 封装响应结果
	 * 
	 * @param code
	 * @param msg
	 * @param data
	 * @return
	 */
	public static ResultDto getResult(int code,String msg,Object data){
		ResultDto result=new ResultDto();
		result.setCode(code);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}
	
	public static String getResultJsonStr(int code,String msg,Object data) throws Exception{
		return JacksonUtil.objToString(getResult(code, msg, data));
	}
	
	public static String getResultJson(int code,String msg,Object data,String format) throws Exception{
		return JacksonUtil.objectToString(getResult(code, msg, data),format);
	}
}
