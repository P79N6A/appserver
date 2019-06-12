package com.idcq.appserver.dao.user;


import java.util.Date;
import java.util.List;

import com.idcq.appserver.dto.user.AgentDto;

public interface IAgentDao {
	
	
	/**
	 * 根据区域代理信息
	 * 
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	AgentDto getAgent(Long userId,Long cityId,Long districtId,Long townId,Integer agentType,Date orderTime) throws Exception;
	/**
	 * 根据用户id查询所有代理角色
	 * 
	 */
	AgentDto getAgentByUserIdAndAgentId(Long userId,Long agentId) throws Exception;

	public void addAgent(AgentDto agent) throws Exception;

	List<AgentDto> getAgentByUserIdAndAgentTypes(Long userId,List<Integer> agentTypes) throws Exception;
}
