package com.idcq.appserver.dto.rebates;

import java.io.Serializable;
import java.util.Date;
/**
 * 代理返利实体
 * 
 * @author ChenYongxin
 *
 */
public class AgentRebatesDto implements Serializable{
	private static final long serialVersionUID = 2808892404452685065L;
	
	
	/*	 CREATE TABLE `1dcq_agent_rebates` (
	  `agent_id` int(10) unsigned NOT NULL COMMENT '代理ID',
	  `user_id` int(10) unsigned NOT NULL COMMENT '会员ID',
	  `slotting_fee` decimal(12,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '代理费(代理佣金)',
	  `agent_wait_rebates_money` decimal(12,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '待返还金额',
	  `agent_rebates_ratio_year` decimal(6,4) unsigned NOT NULL DEFAULT '0.0000' COMMENT '年返还比例',
	  `is_finish` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '是否完成返利：0=未完成，1=完成',
	  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
	  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
	  PRIMARY KEY (`agent_id`) USING BTREE,
	  KEY `user_id` (`user_id`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代理返利表，记录代理费返利';
	*/
	private Long agentId;
	private Long userId;
    private Double agentWaitRebatesMoney;
    private Double slottingFee;
    private Double agentRebatesRatioYear;
    private Integer isFinish;
    private Date lastUpdateTime;
    private Date createTime;
    private Integer limit;
    private Integer pageSize;
    
    
	public Long getAgentId() {
		return agentId;
	}
	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Double getAgentWaitRebatesMoney() {
		return agentWaitRebatesMoney;
	}
	public void setAgentWaitRebatesMoney(Double agentWaitRebatesMoney) {
		this.agentWaitRebatesMoney = agentWaitRebatesMoney;
	}
	public Double getSlottingFee() {
		return slottingFee;
	}
	public void setSlottingFee(Double slottingFee) {
		this.slottingFee = slottingFee;
	}
	public Double getAgentRebatesRatioYear() {
		return agentRebatesRatioYear;
	}
	public void setAgentRebatesRatioYear(Double agentRebatesRatioYear) {
		this.agentRebatesRatioYear = agentRebatesRatioYear;
	}
	public Integer getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	

}