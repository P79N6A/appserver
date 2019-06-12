package com.idcq.appserver.dao.rebates;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.rebates.AgentRebatesDto;
/**
 * 我的订单daooo
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午5:08:53
 */
@Repository
public class AgentRebatesDaoImpl extends BaseDao<AgentRebatesDto>implements IAgentRebatesDao{
	

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.rebates.IAgentRebatesDao#getAgentRebates(com.idcq.appserver.dto.rebates.AgentRebatesDto)
	 */
	@Override
	public List<AgentRebatesDto> getAgentRebates(AgentRebatesDto agentRebatesDto)
			throws Exception {
		return (List<AgentRebatesDto>)super.findList("getAgentRebates", agentRebatesDto);
	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.rebates.IAgentRebatesDao#updateAgentRebates(com.idcq.appserver.dto.rebates.AgentRebatesDto)
	 */
	@Override
	public int updateAgentRebates(AgentRebatesDto agentRebates)
			throws Exception {
		
		return update("updateAgentRebates", agentRebates);

	}

	/* (non-Javadoc)
	 * @see com.idcq.appserver.dao.rebates.IAgentRebatesDao#insertAgentRebates(com.idcq.appserver.dto.rebates.AgentRebatesDto)
	 */
	@Override
	public int insertAgentRebates(AgentRebatesDto agentRebates)
			throws Exception {
		return insert("insertAgentRebates", agentRebates);
	}

    @Override
    public Double getUserTotalSlottingFee(Long userId) throws Exception {
        return this.getSqlSession().selectOne("getUserTotalSlottingFee", userId);
    }
	


}
