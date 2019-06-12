package com.idcq.appserver.common.msgPusher.baseMsg.model;

public class PushResult
{   
    /**
     * 是否发送成功，该字段在回调时为必须字段
     */
    private boolean success = false;
    
    /**
     * 发送生成的消息凭据，多个以逗号相隔，默认如下
     */
    private String msgId = "NAN";
    /**
     * 0-发送成功，500-系统内部异常，400-第三方异常，，该字段在回调时为必须字段
     */
    private Integer code;
    /**
     * 如果失败，则为产生的异常,当code=500的时候，该字段不为空
     */
    private Exception e;
    
    /**
     * 处理结果提示信息
     */
    private String infoMsg;
    
    /**
     * 发送的标准消息体，为必须字段
     */
    private PushMsg source;

    public boolean getSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMsgId()
    {
        return msgId;
    }

    public void setMsgId(String msgId)
    {
        this.msgId = msgId;
    }

    public Integer getCode()
    {
        return code;
    }

    public void setCode(Integer code)
    {
        this.code = code;
    }

    public Exception getE()
    {
        return e;
    }

    public void setE(Exception e)
    {
        this.e = e;
    }

    public PushMsg getSource()
    {
        return source;
    }

    public void setSource(PushMsg source)
    {
        this.source = source;
    }

    public String getInfoMsg()
    {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg)
    {
        this.infoMsg = infoMsg;
    }
    
    
}
