/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.dto.log.LogDto
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年6月15日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.dto.log;

import java.io.Serializable;
import java.util.Date;

public class UserOperatorLogDto implements Serializable{

	private static final long serialVersionUID = -2914144062065385934L;
	
	private Long logId;//主键
	private String logContent;//日志内容
	private String requestMessage;//请求报文
	private Integer logType;//'日志类型:1=登陆,2=修改资料,3=注册,4=下单,5=退单,6=支付,7=充值,8=提现,9=商圈活动
	private Integer clientSystemType;//客户端系统类型:1=收银机,2=一点管家,3=消费者APP,4=微信商城,5=公众号,6=商铺后台,7=收银PAD,8=路由器,9=O2O管理后台
	private Long operateUserId;//操作人id
	private String operateUserIp;//操作人ip
	private Long auditUserId;//审核人user_id
	private String  operateDesc;//操作说明
	private Integer  alarmLevel;//告警等级
	private Date  createTime;//创建时间
	public Long getLogId() {
		return logId;
	}
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
	public String getRequestMessage() {
		return requestMessage;
	}
	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}
	public Integer getLogType() {
		return logType;
	}
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	public Integer getClientSystemType() {
		return clientSystemType;
	}
	public void setClientSystemType(Integer clientSystemType) {
		this.clientSystemType = clientSystemType;
	}
	public Long getOperateUserId() {
		return operateUserId;
	}
	public void setOperateUserId(Long operateUserId) {
		this.operateUserId = operateUserId;
	}
	public String getOperateUserIp() {
		return operateUserIp;
	}
	public void setOperateUserIp(String operateUserIp) {
		this.operateUserIp = operateUserIp;
	}
	public Long getAuditUserId() {
		return auditUserId;
	}
	public void setAuditUserId(Long auditUserId) {
		this.auditUserId = auditUserId;
	}
	public String getOperateDesc() {
		return operateDesc;
	}
	public void setOperateDesc(String operateDesc) {
		this.operateDesc = operateDesc;
	}
	public Integer getAlarmLevel() {
		return alarmLevel;
	}
	public void setAlarmLevel(Integer alarmLevel) {
		this.alarmLevel = alarmLevel;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
}
