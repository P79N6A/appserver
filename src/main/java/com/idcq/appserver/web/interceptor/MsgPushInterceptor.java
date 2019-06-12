package com.idcq.appserver.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
/**
 * 消息推送拦截器
 * <p>
 * 使用激光消息推送
 * </p>
 * 
 * @author Administrator
 * 
 * @date 2015年3月26日
 * @time 下午6:03:58
 */
public class MsgPushInterceptor implements HandlerInterceptor{
	
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}
	
	//主业务完成后，判断是否启动消息推送
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String requestURI = request.getRequestURI();
		
		
		
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	
}
