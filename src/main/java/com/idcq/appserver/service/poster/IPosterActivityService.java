package com.idcq.appserver.service.poster;

import java.util.List;
import java.util.Map;
/**
 * 海报dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */

public interface IPosterActivityService {
	/**
	 * 获取海报列表
	 * @return map
	 * @throws Exception 
	 */
	List<Map> getPosterList() throws Exception;

	Map saveAttachment(Map map) throws Exception;
}
