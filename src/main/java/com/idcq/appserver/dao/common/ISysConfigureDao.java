package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.SysConfigureDto;
/**
 * 该类为兼容旧版本，操作的数据是1dcq_config，不再提供更新
 * @Deprecated
 * @Date 2016-05-26
 * @author zc
 *
 */
@Deprecated
public interface ISysConfigureDao {
	/**
	 * 根据配置key查询系统配置信息
	 * @param configureKey
	 * @return
	 * @throws Exception
	 */
	SysConfigureDto getSysConfigureDtoByKey(String configureKey) throws Exception;
	
	/**
     * 根据配置key查询系统配置信息
     * @param configureKey
     * @return
     * @throws Exception
     */
	List<Map> getSysConfiguresByKeys(Map<String, Object> requestMap) throws Exception;
}
