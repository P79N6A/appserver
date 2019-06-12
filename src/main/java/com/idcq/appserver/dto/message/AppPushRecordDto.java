package com.idcq.appserver.dto.message;

import java.util.Date;

public class AppPushRecordDto
{
    private String messageId;
    private String targetId;
    private String messageContent;
    private Integer messageStatus;
    private Date sendTimeServer;
    //客户端接收时间
    private Date receiveTimeClient;
    //服务端收到反馈时间
    private Date reportTime;
    //客户端是否使用 0-否，1-是
    private Integer isUsed;
    public String getMessageId()
    {
        return messageId;
    }
    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }
    public String getTargetId()
    {
        return targetId;
    }
    public void setTargetId(String targetId)
    {
        this.targetId = targetId;
    }
    public String getMessageContent()
    {
        return messageContent;
    }
    public void setMessageContent(String messageContent)
    {
        this.messageContent = messageContent;
    }
    public Integer getMessageStatus()
    {
        return messageStatus;
    }
    public void setMessageStatus(Integer messageStatus)
    {
        this.messageStatus = messageStatus;
    }
    public Date getSendTimeServer()
    {
        return sendTimeServer;
    }
    public void setSendTimeServer(Date sendTimeServer)
    {
        this.sendTimeServer = sendTimeServer;
    }
    public Date getReceiveTimeClient()
    {
        return receiveTimeClient;
    }
    public void setReceiveTimeClient(Date receiveTimeClient)
    {
        this.receiveTimeClient = receiveTimeClient;
    }
    public Date getReportTime()
    {
        return reportTime;
    }
    public void setReportTime(Date reportTime)
    {
        this.reportTime = reportTime;
    }
    public Integer getIsUsed()
    {
        return isUsed;
    }
    public void setIsUsed(Integer isUsed)
    {
        this.isUsed = isUsed;
    } 
    
}
