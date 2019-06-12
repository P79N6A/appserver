package com.idcq.appserver.common.msgPusher.busMsg.msgSender;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.baseMsg.model.PushResult;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.MsgSender;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.SendCallBack;
import com.idcq.appserver.common.msgPusher.busMsg.model.BusMsg;
import com.idcq.appserver.common.msgPusher.busMsg.msgInterpreter.BusMsgInterpreter;
import com.idcq.appserver.dao.programconfig.IProgramConfigDao;
import com.idcq.appserver.dto.programconfig.ExecutePointDto;
import com.idcq.appserver.dto.programconfig.ProgramConfigDto;
import com.idcq.appserver.exception.AppException;
import com.idcq.appserver.exception.SystemException;
import com.idcq.appserver.utils.BeanFactory;

@Component
// @Lazy(value=true)
public class DefaultBusMsgSenderImpl implements BusMsgSender
{

    private static final Logger log = LoggerFactory.getLogger(DefaultBusMsgSenderImpl.class);

    @Resource(name = "DEFAULT_MSG_SENDER")
    private MsgSender baseMsgSender;

    @Autowired
    private IProgramConfigDao programConfigDao;

    /**
     * 默认回调
     */
    private final SendCallBack defaultSendCallBack = new SendCallBack()
    {
        @Override
        public void onSuccess(PushResult rs)
        {
            PushMsg pushMsg = rs.getSource();
            String targetId = pushMsg.getToAll() == 0 ? "to all " + pushMsg.getMsgTargetCategory()
                    : pushMsg.getTargetId()[0];
            log.info("send msg successfully," + targetId + "," + pushMsg.getTitle() + "," + pushMsg.getContent() + ","
                    + pushMsg.getCrateTime());
        }

        @Override
        public void onFailed(PushResult rs)
        {
            PushMsg pushMsg = rs.getSource();
            String targetId = pushMsg.getToAll() == 0 ? "to all " + pushMsg.getMsgTargetCategory()
                    : pushMsg.getTargetId()[0];
            log.error("send msg failed," + targetId + "," + pushMsg.getTitle() + "," + pushMsg.getContent() + ","
                    + pushMsg.getCrateTime() + "," + rs.getCode(), rs.getE());

        }
    };

    @Override
    public void sendBusMsg(BusMsg busMsg) throws AppException
    {
        if (null == busMsg)
        {
            throw new SystemException("传入的业务消息实体不能为空BusMsg", new NullPointerException());
        }
        String debugInfo = busMsg.getBusCode() + "," + busMsg.getParams();
        log.info("开始处理业务消息 :" + debugInfo);
        // 取出业务消息解析器
        BusMsgInterpreter interpreter = this.getInterpreter(busMsg);
        if (null == interpreter)
        {
            log.error("没有取到对应的业务消息解释器BusMsgInterpreter");
            return;
        }
        // 调用解析器解析出基础标准消息
        PushMsg[] msgs = interpreter.interpret(busMsg);
        if (null == msgs || msgs.length == 0)
        {
            log.error("业务消息解释器BusMsgInterpreter未解析出可发送的消息实体");
            return;
        }
        // 发送消息
        for (PushMsg msg : msgs)
        {
            this.sendMsg(msg);
        }
        log.info("业务消息发送结束：" + debugInfo);
    }

    /**
     * 解析出基础消息
     */
    private BusMsgInterpreter getInterpreter(BusMsg busMsg)
    {
        BusMsgInterpreter result = null;
        ExecutePointDto executePointDto = new ExecutePointDto();
        executePointDto.setCode(busMsg.getBusCode());
        ProgramConfigDto program = null;
        try
        {
            program = programConfigDao.getBeanByExecutePointCode(executePointDto);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        if (null != program && null != program.getSpringBean())
        {
            result = (BusMsgInterpreter) BeanFactory.getBean(program.getSpringBean());
        }
        return result;
    }

    // 调用基础消息组件发送消息
    private void sendMsg(PushMsg pushMsg)
    {
        // 如果没有设置加高函数，那么就采用默认的回调，即打出Log
        // if(null == pushMsg.getSendCallBack()){
        pushMsg.setSysSendCallBack(defaultSendCallBack);
        // }
        baseMsgSender.sendMsg(pushMsg);
    }

}
