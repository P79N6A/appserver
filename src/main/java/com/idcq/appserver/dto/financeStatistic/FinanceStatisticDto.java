/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.financeStatistic.FinanceStatisticDto
 * @description:财务统计
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年8月24日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.financeStatistic;

import java.io.Serializable;
import java.util.Date;

public class FinanceStatisticDto implements Serializable{
	
	private static final long serialVersionUID = -4592830716466462224L;
/*	  `statistic_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '对账ID',
	  `begin_date` date NOT NULL COMMENT '统计日期开始',
	  `end_date` date NOT NULL COMMENT '统计日期结束',
	  `statistic_result` varchar(500) DEFAULT NULL COMMENT '对账结果-详细',
	  `statistic_time` datetime NOT NULL COMMENT '对账时间',
	  `operator_id` int(10) unsigned NOT NULL COMMENT '对账ID对应1dcq_admin.admin_id',
	  `operator_name` varchar(50) NOT NULL COMMENT '对账人姓名',
	  `statistic_type` tinyint(2) unsigned NOT NULL COMMENT '对账类型：1=入账',
	  `is_pass` tinyint(2) unsigned NOT NULL DEFAULT '0' COMMENT '是否匹配通过：0=否，1=是',
	  `remark` varchar(200) DEFAULT NULL COMMENT '对账备注',*/
	
	private Long statisticId;
	private Date beginDate;
	private Date endDate;
	private String statisticResult;
	private Date statisticTime;
	private Date beginStatisticTime;
	private Date endStatisticTime;
	public Date getBeginStatisticTime() {
		return beginStatisticTime;
	}
	public void setBeginStatisticTime(Date beginStatisticTime) {
		this.beginStatisticTime = beginStatisticTime;
	}
	public Date getEndStatisticTime() {
		return endStatisticTime;
	}
	public void setEndStatisticTime(Date endStatisticTime) {
		this.endStatisticTime = endStatisticTime;
	}
	private Long operatorId;
	private String operatorName;
	private Integer statisticType;
	private Integer isPass;
	private String remark;
	private Integer limit;
	private Integer pageSize;
	
	public Long getStatisticId() {
		return statisticId;
	}
	public void setStatisticId(Long statisticId) {
		this.statisticId = statisticId;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatisticResult() {
		return statisticResult;
	}
	public void setStatisticResult(String statisticResult) {
		this.statisticResult = statisticResult;
	}
	public Date getStatisticTime() {
		return statisticTime;
	}
	public void setStatisticTime(Date statisticTime) {
		this.statisticTime = statisticTime;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Integer getStatisticType() {
		return statisticType;
	}
	public void setStatisticType(Integer statisticType) {
		this.statisticType = statisticType;
	}
	public Integer getIsPass() {
		return isPass;
	}
	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
