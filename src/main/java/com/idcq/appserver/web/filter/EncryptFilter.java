package com.idcq.appserver.web.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.encrypt.util.LdcqVerify;
import com.idcq.appserver.utils.RequestUtils;

/**
 * 验证接口安全性 
 * 所有请求采取数字签名方式，需要验证签名正确才能进入业务处理
 * 
 * @author Administrator
 * 
 */
public class EncryptFilter implements Filter {

	private final Log logger = LogFactory.getLog(EncryptFilter.class);

	// 不需要验证接口
	private Set<String> doNotFilterURL;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String params = filterConfig.getInitParameter("doNotFilterURL");
		if (params != null) {
			String[] paramArray = params.split(",");
			doNotFilterURL = new HashSet<>(Arrays.asList(paramArray));
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;

		// 获取请求uri
		String contentRoot = httpRequest.getContextPath();
		String interceptPath = contentRoot + CommonConst.INTERFACE;
		String requestPath = httpRequest.getRequestURI();
		if (requestPath.contains(interceptPath)) {
			requestPath = requestPath.substring(interceptPath.length());
		}
		logger.debug("filter fetch the requestURI: " + requestPath);

		// 不需要校验签名的接口直接往下走
		if (CollectionUtils.isNotEmpty(doNotFilterURL)
				&& doNotFilterURL.contains(requestPath)) {
			chain.doFilter(request, response);
			return;
		}

		// 下面是校验签名
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = httpRequest.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			params.put(name, RequestUtils.getQueryParam(httpRequest, name));
		}

		if (LdcqVerify.verify(params)) {
			chain.doFilter(request, response);
			return;
		} else {
			logger.error("签名验证失败，请求无效！");
			httpResponse.setStatus(400);
			httpResponse.sendError(400);
		}

	}

	@Override
	public void destroy() {
		doNotFilterURL = null;
	}

}
