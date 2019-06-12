package com.idcq.appserver.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dao.common.ICommonDao;
import com.idcq.appserver.utils.jedis.DataCacheApi;

/**
 * 会话工具类
 * @author shengzhipeng
 * @date:2015年8月3日 下午2:38:50
 */
public class SessionUtil {

	/**
	 * 处理session信息
	 * @param request
	 * @throws Exception 
	 */
	public static String dealSession(HttpServletRequest request) throws Exception {
	    ICommonDao commomDao = BeanFactory.getBean(ICommonDao.class);
		HttpSession session = request.getSession(false);
		if (session != null) {
			
			//缓存中的session也要失效
			String oldSessionId = session.getId();
			DataCacheApi.del(CommonConst.KEY_JSESSIONID + oldSessionId);
			session.invalidate();
			commomDao.deleteSession(oldSessionId);
		}
		session = request.getSession();
		String sessionId = session.getId();
	
		DataCacheApi.set(CommonConst.KEY_JSESSIONID + sessionId, sessionId);
		commomDao.insertSession(sessionId);
		return sessionId;
	}
	
}
