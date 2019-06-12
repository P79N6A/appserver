package com.idcq.appserver.web.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.jedis.DataCacheApi;

public class AuthFilter implements Filter {

	/** 日志记录 */
	private final Log logger = LogFactory.getLog(getClass());

	/** 不需要过滤的URL */
	private Set<String> doNotFilterURL;

	public void init(FilterConfig filterConfig) throws ServletException {
		String params = filterConfig.getInitParameter("doNotFilterURL");
		if (params != null) { 
			String[] urls = params.split(",");
			doNotFilterURL = new HashSet<String>(Arrays.asList(urls));
		}
	}

	@SuppressWarnings("unchecked")
    public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		/**
		 * 1,doFilter方法的第一个参数为ServletRequest对象。此对象给过滤器提供了对进入的信息（包括
		 * 表单数据、cookie和HTTP请求头）的完全访问。第二个参数为ServletResponse，通常在简单的过
		 * 滤器中忽略此参数。最后一个参数为FilterChain，此参数用来调用servlet或JSP页。
		 */

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		/**
		 * 如果处理HTTP请求，并且需要访问诸如getHeader或getCookies等在ServletRequest中
		 * 无法得到的方法，就要把此request对象构造成HttpServletRequest
		 */
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String contextRoot = request.getContextPath();
		String interceptPath = contextRoot + CommonConst.INTERFACE;
		String requestPath = request.getRequestURI();
		logger.info("filter fetch the ContentType: " + request.getContentType());
		if (requestPath.contains(interceptPath)) {
			requestPath = requestPath.substring(interceptPath.length());
		}

		// 获取HTTP请求的URI
		logger.info("filter fetch the requestURI: " + requestPath);

		if (CollectionUtils.isNotEmpty(doNotFilterURL)) {
			if (doNotFilterURL.contains(requestPath)) {
				// 不进行拦截
				// 调用FilterChain对象的doFilter方法。Filter接口的doFilter方法取一个FilterChain对象作
				// 为它
				// 的一个参数。在调用此对象的doFilter方法时，激活下一个相关的过滤器。
				// 加入filter链继续向下执行
				chain.doFilter(request, response);
				return;
			}
			if(requestPath.startsWith(CommonConst.TOKEN_PREFIX))
			{
			    //校验token
			    try
                {
			        ServletRequest requestWrapper = null;
			        if(null != request.getContentType() && request.getContentType().contains("application/json")) {
	                    String requestMethod = request.getMethod();
	                    String shopIdStr = "";
	                    String token = "";
	                    if("GET".equals(requestMethod))
	                    {
	                        shopIdStr = RequestUtils.getQueryParam(request, "shopId");
	                        token = RequestUtils.getQueryParam(request, "token");
	                    }
	                    else
	                    {
	                        //获取json对象
	                        requestWrapper = new RequestWrapper(request);
	                        Map<String, Object> map = JacksonUtil.parseJson2Map(IOUtils.toString(requestWrapper.getInputStream()));
	                        if(null != map)
	                        {
	                            shopIdStr = map.get("shopId") == null ? "" : map.get("shopId").toString();
	                            token = map.get("token") == null ? "" : map.get("token").toString();
	                        }
	                    }
	                    // token不能为空
	                    CommonValidUtil.validStrNull(token, CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_TOKEN);
	                    CommonValidUtil.validStrNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "shopId不能为空");
	                    Long shopId = NumberUtil.strToLong(shopIdStr, "shopId");
	                    ICollectService collectService = BeanFactory.getBean(ICollectService.class);
	                    // 商铺存在性
	                    collectService.queryShopAndTokenExists(shopId, token);
	                }
			    	
	                chain.doFilter(requestWrapper == null ? request : requestWrapper, response);
	                return;
                }
			    catch (ServiceException e)
		        {
			        write(response, e.getCode(), e.getMessage());
			        return;
		        }
		        catch (Exception e)
		        {
		            logger.error("系统异常:"+e);
		            write(response, CodeConst.CODE_SYSTEM_ERROR, "系统异常");
		        }
		 	}
		}
		HttpSession session = request.getSession(false);
		String id = null;
		if (null != session) {
			 id = session.getId();
		}
		ICommonDao commomDao = BeanFactory.getBean(ICommonDao.class);
		//防止cookie没传从参数中获取下jsessionId
		boolean flag = false;
		String jsessionId = RequestUtils.getQueryParam(request, CommonConst.JSESSIONID);
		if (StringUtils.isNotBlank(jsessionId)) {
			String value = DataCacheApi.get(CommonConst.KEY_JSESSIONID + jsessionId);
			if (StringUtils.isNotBlank(value)) {
				flag = true;
			} else {
				//如果缓存没有在比较服务器session
				if(jsessionId.equals(id)) {
					flag = true;
				}
			}
			String dbSession = commomDao.getSessionById(value);
            logger.info("DB中存储的cookie信息：" + dbSession);
            if (StringUtils.isNotBlank(dbSession)) {
                flag = true;
            }
		}
		Cookie[] cookies = request.getCookies();
		logger.info("request.getCookies()方式获取cookies: "+cookies);
		if (null != cookies) {

			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				String value = cookie.getValue();
				if (CommonConst.JSESSIONID.equalsIgnoreCase(name)) {
					String sessionId = DataCacheApi.get(CommonConst.KEY_JSESSIONID + value);
					logger.info("缓存中存储的cookie信息：" + sessionId);
					if (StringUtils.isNotBlank(sessionId)) {
						chain.doFilter(request, response);
						return;
					}
					
					String dbSession = commomDao.getSessionById(value);
					logger.info("DB中存储的cookie信息：" + dbSession);
					if (StringUtils.isNotBlank(dbSession)) {
                        chain.doFilter(request, response);
                        DataCacheApi.set(CommonConst.KEY_JSESSIONID + dbSession, dbSession);
                        return;
                    }
					if (null != id && id.equals(value)) {
						chain.doFilter(request, response);
						return;
					} else {
						if(flag)
						{
							chain.doFilter(request, response);
							return;
						}
						
						logger.error(CodeConst.CODE_COOKIES_TIMEOUT + CommonConst.KEY_SEPARATOR_COLON + CodeConst.MSG_COOKIES_TIMEOUT
								+ CommonConst.KEY_SEPARATOR_COLON + value);
						write(response, CodeConst.CODE_COOKIES_TIMEOUT, CodeConst.MSG_COOKIES_TIMEOUT);
						
						return;
					}
				}
			}
			if(flag)
			{
				chain.doFilter(request, response);
				return;
			}
			logger.error(CodeConst.CODE_COOKIES_INVALIDATE + CommonConst.KEY_SEPARATOR_COLON + CodeConst.MSG_COOKIES_INVALIDATE);
//			 cookies无效
			write(response, CodeConst.CODE_COOKIES_INVALIDATE, CodeConst.MSG_COOKIES_INVALIDATE);
			
			return;

		}else {
			//从头里面获取cookie信息  add by huangrui 2015.12.30
			String value = request.getHeader("cookie");
			logger.info("从头获取cookie信息："+value);
			if(StringUtils.isNotBlank(value)){
				String sessionId = DataCacheApi.get(CommonConst.KEY_JSESSIONID + value);
				logger.info("缓存中存储的cookie信息：" + sessionId);
				if (StringUtils.isNotBlank(sessionId)) {
					flag = true;
				}
			}
			
			
		}
		if(flag)
		{
			chain.doFilter(request, response);
			return;
		}
		logger.error(CodeConst.CODE_COOKIES_NULL + CommonConst.KEY_SEPARATOR_COLON + CodeConst.MSG_COOKIES_NULL);
		write(response, CodeConst.CODE_COOKIES_NULL, CodeConst.MSG_COOKIES_NULL);
		// 未登录
//		response.setStatus(CodeConst.CODE_COOKIES_NULL);
//		response.sendError(CodeConst.CODE_COOKIES_NULL,
//				CodeConst.MSG_COOKIES_NULL);
		
		return;

	}
	
	/**
	 * 返回json
	 * 
	 * @Function: com.idcq.appserver.web.filter.AuthFilter.write
	 * @Description:
	 *
	 * @param response
	 * @param code
	 * @param message
	 * @throws IOException
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月6日 上午10:51:07
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月6日    shengzhipeng       v1.0.0         create
	 */
	public void write(HttpServletResponse response, int code, String message) throws IOException{  
	    /* 
	     * 在调用getWriter之前未设置编码(既调用setContentType或者setCharacterEncoding方法设置编码), 
	     * HttpServletResponse则会返回一个用默认的编码(既ISO-8859-1)编码的PrintWriter实例。这样就会 
	     * 造成中文乱码。而且设置编码时必须在调用getWriter之前设置,不然是无效的。 
	     * */  
	    response.setContentType("text/html;charset=utf-8");  
	    //response.setCharacterEncoding("UTF-8");  
	    PrintWriter out = response.getWriter();  
	    JSONObject obj = new JSONObject();
	    obj.put("code", code);
	    obj.put("msg", message);
	    out.println(obj);  
	    out.flush();  
	    out.close();  
	}  

	public void destroy() {
		this.doNotFilterURL = null;
	}
}
