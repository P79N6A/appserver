package com.idcq.appserver.common.msgPusher.busMsg.msgInterpreter;

import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.busMsg.model.BusMsg;
/**
 * 由业务消息产生标准消息，不推荐直接继承该接口，推荐继承类{@link PushMsgGenerater}以实现该接口
 * @author Administrator
 *
 */
public interface BusMsgInterpreter
{
    PushMsg[] interpret(BusMsg busMsg);
}
