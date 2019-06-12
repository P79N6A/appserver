package com.idcq.appserver.common.exceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.ResponseUtils;
import com.idcq.appserver.utils.ResultUtil;


public class PlatformExceptionHandler implements HandlerExceptionResolver{

	private final static Logger logger = LoggerFactory.getLogger(PlatformExceptionHandler.class);
	
	public ModelAndView resolveException(HttpServletRequest request,
										 HttpServletResponse response, 
										 Object obj, Exception e) {
		
		
		if (e instanceof ValidateException) {
			ValidateException validException = (ValidateException)e;
			logger.error("接口：{} ---业务校验异常：{}",request.getRequestURI(),validException.getMessage());
			String responseData = JSONObject.fromObject(ResultUtil.getResult(validException.getCode(), 
					validException.getMessage(), null)).toString();
			ResponseUtils.renderJson(response,responseData);
		} else if(e instanceof APIBusinessException){
			logger.error("接口：{} ---业务校验异常：{}",request.getRequestURI(),e.getMessage());
			ResponseUtils.renderHtmlText(response,
					JSONObject.fromObject(ResultUtil.getResult(((APIBusinessException)e).getCode(), e.getMessage(), null)).toString());
		} else if(e instanceof APISystemException){
			logger.error("接口：{} ---系统异常：{}",request.getRequestURI(),e.getMessage());
			logger.error("异常信息", e);
			ResponseUtils.renderHtmlText(response,
					JSONObject.fromObject(ResultUtil.getResult(CodeConst.CODE_SYSTEM_ERROR, "系统异常",null)).toString());
		} else {
			logger.error("接口：{} ---系统异常：{}",request.getRequestURI(),e.getMessage());
			logger.error("异常信息", e);
			ResponseUtils.renderHtmlText(response,
					JSONObject.fromObject(ResultUtil.getResult(CodeConst.CODE_SYSTEM_ERROR, "系统异常",null)).toString());
		}
		return new ModelAndView();
	}
	
}
