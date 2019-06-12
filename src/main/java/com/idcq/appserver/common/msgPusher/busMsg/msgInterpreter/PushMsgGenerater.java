package com.idcq.appserver.common.msgPusher.busMsg.msgInterpreter;
/**
 * 实现该接口应该分别完成三个抽象事件：
 * 1.实现接口{@link BusMsgInterpreter}，即生成对应业务的具体基础消息；
 * 2.处理消息相关的业务信息，如将生成的验证码放入缓存等；
 * 3.记录消息发送日志；
 * 4.定义消息回调；
 * @author Administrator
 *
 */
public interface PushMsgGenerater extends BusMsgInterpreter
{
    
}
