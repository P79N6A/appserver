package com.idcq.appserver.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义拦截器
 * 
 * @author Administrator
 * 
 * @date 2015年3月26日
 * @time 下午6:03:58
 */
public class MyInterceptor implements HandlerInterceptor{

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		System.out.println(request.getParameter("key"));
		String json = IOUtils.toString(request.getInputStream());
		System.out.println("===========================");
		System.out.println(json);
		return true;
	}

	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String json = IOUtils.toString(request.getInputStream());
		System.out.println("===========================");
		System.out.println(json);
	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String json = IOUtils.toString(request.getInputStream());
		System.out.println("===========================");
		System.out.println(json);
	}

	
}
