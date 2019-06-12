package com.idcq.appserver.dto.log;

import java.util.Date;

/**
 * 商铺操作日志实体
 * Created by Administrator on 2016/8/15 0015.
 */
public class ShopOperationLog {
    //日志ID,唯一主键
    private Long logId;

    //商铺ID
    private Long shopId;

    //日志内容
    private String logContent;

    //请求报文
    private String requestMessage;

    //日志类型:0=退出,1=登陆,2=修改资料,3=注册,4=下单,5=退单,6=支付,7=充值,8=提现,9=商圈活动;
    private Integer logType;

    //客户端系统类型:1=收银机,2=一点管家,3=消费者APP,4=微信商城,5=公众号,6=商铺后台,7=收银PAD,8=路由器,9=O2O管理后台,10=盒子
    private Integer clientSystemType;

    //操作人id
    private Long operateUserId;

    //用户类型ID：1=operate_user_id对应到1dcq_user,2=operate_user_id对应到1dcq_admin,3=operate_user_id对应到1dcq_shop_employee
    private Integer userTypeId;

    //操作人ip
    private String handlerUserIp;

    //审核人user_id
    private  Long auditUserId;

    //操作说明
    private String operateDesc;

    //告警等级
    private Integer alarmLevel;

    //创建时间
    private Date createTime;

    private String mobile;

    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
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

    public Integer getClientSystemTypel() {
        return clientSystemType;
    }

    public void setClientSystemTypel(Integer clientSystemType) {
        this.clientSystemType = clientSystemType;
    }

    public Long getOperateUserId() {
        return operateUserId;
    }

    public void setOperateUserId(Long operateUserId) {
        this.operateUserId = operateUserId;
    }

    public Integer getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(Integer userTypeId) {
        this.userTypeId = userTypeId;
    }

    public String getHandlerUserIp() {
        return handlerUserIp;
    }

    public void setHandlerUserIp(String handlerUserIp) {
        this.handlerUserIp = handlerUserIp;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getClientSystemType() {
        return clientSystemType;
    }

    public void setClientSystemType(Integer clientSystemType) {
        this.clientSystemType = clientSystemType;
    }
}
