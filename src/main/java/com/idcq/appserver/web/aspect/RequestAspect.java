package com.idcq.appserver.web.aspect;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;

import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 针对所有请求的切面类
 * <p>
 * 	主要在请求响应之前对请求数据进行部分处理及日志记录
 * </p>
 * @author Administrator
 * 
 * @date 2015年3月26日
 * @time 下午1:49:05
 */
public class RequestAspect implements MethodInterceptor{
	private final Logger logger = Logger.getLogger(RequestAspect.class);
	
	@Override
	public Object invoke(MethodInvocation mi) throws Throwable {
		Object[] arguments = mi.getArguments();
		HttpServletRequest request = null;  
		String requestMethod=null;
		for (int i = 0 ; i < arguments.length ; i++ )  
        {  
			if (arguments[i] instanceof HttpEntity){
				logger.info("参数:"+arguments[i]);
			}
			 if (arguments[i] instanceof HttpServletRequest){
            	request = (HttpServletRequest)arguments[i];
            	String ip = RequestUtils.getIpAddr(request);
            	logger.info("访问者的IP地址: "+ip);
            	requestMethod=request.getMethod();
            	logger.info("请求方式："+ requestMethod);
            	if("POST".equals(requestMethod)){
            		logger.info("请求URL:"+request.getRequestURL());
            	}else{
            		logger.info("请求URL:"+request.getRequestURL()+"?"+request.getQueryString());
            	}
            }
        }
		long start=System.currentTimeMillis();
		Object proceed = mi.proceed();
		logger.info("该接口执行时间:"+(System.currentTimeMillis()-start)+"ms");
		
		if(proceed instanceof ResultDto){
			ResultDto r=(ResultDto) proceed;
			logger.info("响应结果:"+ResultUtil.getResultJsonStr(r.getCode(), r.getMsg(), r.getData()));
		}else{
			logger.info("响应结果:"+proceed);
		}
		return proceed;
	}
}
