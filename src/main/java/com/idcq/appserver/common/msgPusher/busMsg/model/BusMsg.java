package com.idcq.appserver.common.msgPusher.busMsg.model;

import java.util.Date;
import java.util.Map;

/**
 * 这是标准业务消息实体，在业务调用消息发送实体时构造该实体
 * @author Administrator
 *
 */
public class BusMsg
{
    /**
     * 业务编码，可以对应表1dcq_execute_point中的code
     */
    private String busCode;

    /**
     * 传入参数，该参数只允许非常必要的数据与事实已存在的数据
     */
    private Map<String, Object> params;

    /**
     * 消息创建时间，非必须消息
     */
    private Date createTime;

    public String getBusCode()
    {
        return busCode;
    }

    public void setBusCode(String busCode)
    {
        this.busCode = busCode;
    }

    public Map<String, Object> getParams()
    {
        return params;
    }

    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }

    public Date getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }

}
