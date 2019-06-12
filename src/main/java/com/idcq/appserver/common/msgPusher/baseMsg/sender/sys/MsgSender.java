package com.idcq.appserver.common.msgPusher.baseMsg.sender.sys;

import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.infrastructure.InfrastructureType;
/**
 * 基础标准消息发送接口。该接口不处理任何业务相关功能，如发送记录等
 * @author Administrator
 *
 */
public interface MsgSender
{
    void sendMsg(PushMsg pushMsg);
    InfrastructureType getSendChannel(String code);
}
