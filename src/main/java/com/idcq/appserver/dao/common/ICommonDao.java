package com.idcq.appserver.dao.common;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.ConfigQueryCondition;

public interface ICommonDao {
	
	/**
	 * 根据传入的key获取短信验证码所需的value
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	public String getSmsValueByKey(String configKey) throws Exception;

	/**
	 * 根据configkey获取value
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	String getConfigValueByKey(String configKey) throws Exception;
	
	/**
	 * 根据configkey获取value
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	List<Map> getConfigValueByKeys(List<String> configKeys) throws Exception;
	
	/**
	 * 设置指定key的value
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	int updateConfigValueByKey(String configValue,String configKey) throws Exception;
	
	/**
	 * 新增app配置
	 * @param configValue
	 * @param configKey
	 * @return
	 * @throws Exception
	 */
	int addConfigValueByKey(String configValue,String configKey) throws Exception;
	
	/**
	 * * 获取1dcq_setting表中的配置value信
	 * @param param
	 * @return
	 */
	public String getSettingValue(Map<String, String> param)throws Exception;
	
	/**
	 * 根据key查询1dcq_message_setting中的content信息
	 * @param settingKey
	 * @return
	 * @throws Exception
	 */
	public Map getMsgSettingContent(String settingKey)throws Exception;
	
	public Map getSettingValueByKey(Map param) throws Exception;

	public void writeQueryLog(String userName, String mobile) throws Exception;
	
	/**
	 * 短信群发，获取手机号码
	 * @return
	 * @throws Exception
	 */
	public Map getMobileInfo() throws Exception;
	
	/**
	 * 更新手机号码发送状态
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	public int updateStatus(String mobile) throws Exception;
	
	void deleteSession(String sessionId)throws Exception;
	
	void insertSession(String sessionId)throws Exception;
	
	String getSessionById(String sessionId);
	
	int deleteConfigs(ConfigQueryCondition searchCondition);
}
