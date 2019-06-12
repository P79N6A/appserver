package com.idcq.appserver.dao.user;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.user.AgentDto;

/**
 * 代理dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class AgentDaoImpl extends BaseDao<AgentDto> implements IAgentDao{

	@Override
	public AgentDto getAgent(Long userId,Long cityId,Long districtId,Long townId, Integer agentType,Date orderTime) throws Exception {
		Map param=new HashMap();
		param.put("userId", userId);
		param.put("cityId", cityId);
		param.put("districtId", districtId);
		param.put("townId", townId);
		param.put("agentType", agentType);
		param.put("orderTime", orderTime);
		return (AgentDto) super.selectOne("getAgent", param);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.user.IAgentDao#getAgentListByUserId(java.lang.Long)
     */
    @Override
    public AgentDto getAgentByUserIdAndAgentId(Long userId,Long agentId) throws Exception
    {
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("agentId", agentId);
        return super.getSqlSession().selectOne(generateStatement("getAgentByUserIdAndAgentId"), param);
    }

    @Override
    public List<AgentDto> getAgentByUserIdAndAgentTypes(Long userId,List<Integer> agentTypes) throws Exception
    {
        Map param = new HashMap();
        param.put("userId", userId);
        param.put("agentTypes", agentTypes);
        return super.getSqlSession().selectList(generateStatement("getAgentByUserIdAndAgentTypes"), param);
    }
    
    @Override
    public void addAgent(AgentDto agent) throws Exception {
    	this.insert(generateStatement("addAgent"), agent);
    }

}
