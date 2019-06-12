package com.idcq.appserver.common.msgPusher.busMsg.msgSender;

import com.idcq.appserver.common.msgPusher.busMsg.model.BusMsg;
import com.idcq.appserver.exception.AppException;
/**
 * 该接口为业务消息发送的最顶层接口
 * @author Administrator
 *
 */
public interface BusMsgSender {
    /**
     * 发送业务消息
     * @param busMsg
     * @throws AppException
     */
    void sendBusMsg(BusMsg busMsg) throws AppException;
}
