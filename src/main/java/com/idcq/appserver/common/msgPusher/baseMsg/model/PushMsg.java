package com.idcq.appserver.common.msgPusher.baseMsg.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.idcq.appserver.common.msgPusher.baseMsg.enums.MsgTargetCategory;
import com.idcq.appserver.common.msgPusher.baseMsg.enums.MsgType;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.infrastructure.InfrastructureType;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.SendCallBack;

/**
 * 该实体为基础消息发送设施使用的标准消息实体
 * 该实体的所有数据为业务相关数据，内部字段意义与直接业务具体相关
 * @author Administrator
 *
 */
public class PushMsg implements Serializable
{
    private static final long serialVersionUID = -799001091685948753L;

    /**
     * 消息类型，默认是app推送
     */
    private MsgType msgType = MsgType.APP_PUSH;
    
    /**
     * 目标类型
     */
    private MsgTargetCategory msgTargetCategory;
    /**
     * 目标地址，如要发送短信的手机号码等
     */
    private String[] targetId;
    

    /**
     * 用户类型，会员-0,店铺管理者-10
     */
    private Integer userType;

    /**
     * 是否为发送给某一类型所有 0-否,1-是
     */
    private Integer toAll = new Integer(0);

    /**
     * 消息内容,作为message body直接发送，程序不作处理
     */
    private byte[] content;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 发送回调,可以为空，为空时采用默认回调，即打出Log
     */
    private SendCallBack sendCallBack;
    
    /**
     * 系统回调
     */
    private SendCallBack sysSendCallBack;

    /**
     * 创建时间
     */
    private Date crateTime;
    
    /**
     * 消息到达要求
     */
    @JsonIgnore
    private transient Integer qos = 2;
    
    @JsonIgnore
    private transient InfrastructureType infrastructureType;
    
    /**
     * 冗余字段，供回调处理,在基础消息发送组件中，该参数不会被使用
     */
    @JsonIgnore
    private transient Map<String, Object> contextInfos;
    
    /**
     * 将id字符数组转成字符串，方便调试等用处
     */
    @JsonIgnore
    private transient String idStr;
    
    private transient boolean publicBroadcaster = false;

    public MsgType getMsgType()
    {
        return msgType;
    }

    public void setMsgType(MsgType msgType)
    {
        this.msgType = msgType;
    }

    public String[] getTargetId()
    {
        return targetId;
    }

    public void setTargetId(String[] targetId)
    {
        this.targetId = targetId;
    }

    public MsgTargetCategory getMsgTargetCategory()
    {
        return msgTargetCategory;
    }

    public void setMsgTargetCategory(MsgTargetCategory msgTargetCategory)
    {
        this.msgTargetCategory = msgTargetCategory;
    }

    public Integer getToAll()
    {
        return toAll;
    }

    public void setToAll(Integer toAll)
    {
        this.toAll = toAll;
    }

    public byte[] getContent()
    {
        return content;
    }

    public void setContent(byte[] content)
    {
        this.content = content;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public SendCallBack getSendCallBack()
    {
        return sendCallBack;
    }

    public void setSendCallBack(SendCallBack sendCallBack)
    {
        this.sendCallBack = sendCallBack;
    }

    public Date getCrateTime()
    {
        return crateTime;
    }

    public void setCrateTime(Date crateTime)
    {
        this.crateTime = crateTime;
    }

    public Integer getUserType()
    {
        return userType;
    }

    public void setUserType(Integer userType)
    {
        this.userType = userType;
    }
    
    public Integer getQos()
    {
        return qos;
    }

    public void setQos(Integer qos)
    {
        this.qos = qos;
    }

    public Map<String, Object> getContextInfos()
    {
        return contextInfos;
    }

    public void setContextInfos(Map<String, Object> contextInfos)
    {
        this.contextInfos = contextInfos;
    }

    public InfrastructureType getInfrastructureType()
    {
        return infrastructureType;
    }

    public void setInfrastructureType(InfrastructureType infrastructureType)
    {
        this.infrastructureType = infrastructureType;
    }
    
    
    public void setIdStr(String idStr)
    {
        this.idStr = idStr;
    }

    public synchronized String getIdStr()
    {   
        if(null == idStr && null != targetId){
            String temp = "";
                for(String tempId : targetId){
                    temp = temp + "," + tempId;
                }
                idStr= temp;
        }
        return idStr;
    }

    public boolean getPublicBroadcaster()
    {
        return publicBroadcaster;
    }

    public void setPublicBroadcaster(boolean publicBroadcaster)
    {
        this.publicBroadcaster = publicBroadcaster;
    }

    public SendCallBack getSysSendCallBack()
    {
        return sysSendCallBack;
    }

    public void setSysSendCallBack(SendCallBack sysSendCallBack)
    {
        this.sysSendCallBack = sysSendCallBack;
    }
    
}
