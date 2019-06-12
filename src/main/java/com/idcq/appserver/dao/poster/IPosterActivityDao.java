package com.idcq.appserver.dao.poster;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
/**
 * 海报dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */

public interface IPosterActivityDao {
	/**
	 * 获取海报列表
	 * @return map
	 */
	Map<String, Object> getPosterList();
}
