package com.idcq.appserver.dao.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import com.idcq.appserver.dto.common.ConfigDto;
import com.idcq.appserver.dto.common.ConfigQueryCondition;
@Repository
public class CommonDaoImpl extends SqlSessionDaoSupport implements ICommonDao {

	@Resource
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory){
		super.setSqlSessionFactory(sqlSessionFactory);
	}
	public String getSmsValueByKey(String configKey) throws Exception {
		return this.getSqlSession().selectOne("getSmsValueByKey", configKey);
	}
	public String getConfigValueByKey(String configKey) throws Exception {
		return this.getSqlSession().selectOne("getSmsValueByKey", configKey);
	}
	
	public List<Map> getConfigValueByKeys(List<String> list)
			throws Exception {
		return (List)this.getSqlSession().selectList("getConfigValueByKeys", list);
	}
	public String getSettingValue(Map<String, String> param)  throws Exception {
		return this.getSqlSession().selectOne("getSettingValue", param);
	}
	public Map getMsgSettingContent(String settingKey) throws Exception {
		return (Map)this.getSqlSession().selectOne("getMsgSettingContent", settingKey);
	}
	@Override
	public Map getSettingValueByKey(Map param) throws Exception {
		return (Map)this.getSqlSession().selectOne("getSettingValueByKey", param);
	}
	@Override
	public int updateConfigValueByKey(String configValue, String configKey)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("configValue",configValue);
		map.put("configKey",configKey);
		return this.getSqlSession().update("updateConfigValueByKey",map);
	}
	
	@Override
	public int addConfigValueByKey(String configValue, String configKey)
			throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("configValue",configValue);
		map.put("configKey",configKey);
		return this.getSqlSession().insert("addConfigValueByKey",map);
	}
	@Override
	public void writeQueryLog(String userName, String mobile) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userName", userName);
		map.put("mobile", mobile);
		getSqlSession().insert("writeQueryLog", map);
	}
	
	
	@Override
	public Map getMobileInfo() throws Exception {
		return this.getSqlSession().selectOne("getMobileInfo", null);
	}
	@Override
	public int updateStatus(String mobile) throws Exception {
		return this.getSqlSession().update("updateStatus",mobile);
	}
    @Override
    public void deleteSession(String sessionId) throws Exception {
        this.getSqlSession().delete("deleteSession", sessionId);
        
    }
    @Override
    public void insertSession(String sessionId) throws Exception {
        this.getSqlSession().insert("insertSession", sessionId);
    }
    @Override
    public String getSessionById(String sessionId){
        return this.getSqlSession().selectOne("getSessionById", sessionId);
    }
    public int deleteConfigs(ConfigQueryCondition searchCondition) {
        return this.getSqlSession().delete("deleteConfigs", searchCondition);
    }
}
