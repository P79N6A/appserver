package com.idcq.appserver.dao.rebates;

import java.util.List;

import com.idcq.appserver.dto.rebates.AgentRebatesDto;

public interface IAgentRebatesDao {
	
	/**
	 * 查询记录代理返利
	 * 
	 */
	public List<AgentRebatesDto> getAgentRebates(AgentRebatesDto agentRebatesDto) throws Exception;
	/**
	 * 更新记录代理返利
	 * 
	 */
	public int updateAgentRebates(AgentRebatesDto agentRebates) throws Exception;
	
	public int insertAgentRebates(AgentRebatesDto agentRebates) throws Exception;
	
	public Double getUserTotalSlottingFee(Long userId) throws Exception; 

	
}
