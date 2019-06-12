package com.idcq.appserver.common.msgPusher.baseMsg.sender.sys;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.infrastructure.InfrastructureType;
import com.idcq.appserver.listeners.ContextInitListener;
import com.idcq.appserver.utils.BeanFactory;

/**
 * 这是系统默认的消息发送设施，处理诸如默认消息发送，默认短信推送的具体通道的选择 并且，这里会集中处理此类的线程问题
 * @author Administrator
 *
 */
@Component(value = "DEFAULT_MSG_SENDER")
// @Lazy(value = true)
@DependsOn(value =
{ "beanFactory" })
public class DefaultMsgSenderImpl implements MsgSender
{

    private static final Logger log = LoggerFactory.getLogger(DefaultMsgSenderImpl.class);

    private static final String DEFAULT_PUSH_SENDER_FLAG = "defaultPushSender";

    private static final String DEFAULT_SMS_SENDER_FLAG = "defaultSmsSender";

    /**
     * 默认的app消息推送设施
     */
    private final MsgSender defaultPushSenderInfrastructure;

    /**
     * 默认的短信推送设施
     */
    private final MsgSender defaultSmsSenderInfrastructure;

    /**
     * 管理app消息推送的线程池
     */
    private final ExecutorService pushExecutors;

    /**
     * 管理短信发送的线程池
     */
    private final ExecutorService msgExecutors;
    
    private final Map<InfrastructureType, MsgSender> infrastructures = new HashMap<InfrastructureType, MsgSender>();

    // private final

    public DefaultMsgSenderImpl()
    {
        Properties props = ContextInitListener.JPUSH_PROPS;
        /*
         * 初始化默认消息发送设施
         */
        String propertyPushFlag = props.getProperty(DEFAULT_PUSH_SENDER_FLAG);
        // 默认采用JPUSH
        if (null == propertyPushFlag)
        {
            propertyPushFlag = "MQTT_PAHO";
        }
        defaultPushSenderInfrastructure = (MsgSender) BeanFactory.getBean(propertyPushFlag);
        infrastructures.put(defaultPushSenderInfrastructure.getSendChannel(null), defaultPushSenderInfrastructure);
        log.info("默认app推送设施：" + defaultPushSenderInfrastructure.getSendChannel(null));
        /*
         * 初始化默认短信发送设施
         */
        String propertyMsgFlag = props.getProperty(DEFAULT_SMS_SENDER_FLAG);
        // 默认的短信发送
        if (null == propertyMsgFlag)
        {
            propertyMsgFlag = "SMSSENDER";
        }
        defaultSmsSenderInfrastructure = (MsgSender) BeanFactory.getBean(propertyMsgFlag);
        infrastructures.put(defaultSmsSenderInfrastructure.getSendChannel(null), defaultSmsSenderInfrastructure);
        log.info("默认短信推送设施：" + defaultSmsSenderInfrastructure.getSendChannel(null));
        String tempNum = null;
        /*
         * 初始化app推送线程池
         */
        BlockingQueue<Runnable> pushWorkQueue = new LinkedBlockingDeque<Runnable>(10);
        tempNum = props.getProperty("maxPushThreadNum");
        Integer maximumPushPoolSize = null == tempNum ? 3 : Integer.valueOf(tempNum.trim());
        pushExecutors = new ThreadPoolExecutor(1, maximumPushPoolSize, 30000, TimeUnit.MILLISECONDS, pushWorkQueue,
                new ThreadPoolExecutor.CallerRunsPolicy());

        /*
         * 初始化短信发送线程池
         */
        tempNum = null;
        BlockingQueue<Runnable> msgWorkQueue = new LinkedBlockingDeque<Runnable>(10);
        tempNum = props.getProperty("maxSmsThreadNum");
        Integer maximumSmsPoolSize = null == tempNum ? 2 : Integer.valueOf(tempNum.trim());
        msgExecutors = new ThreadPoolExecutor(1, maximumSmsPoolSize, 30000, TimeUnit.MILLISECONDS, msgWorkQueue,
                new ThreadPoolExecutor.CallerRunsPolicy());
        // Runtime.getRuntime().addShutdownHook(hook);
        // 注意这里暂时没有处理线程池的关闭问题
        // TODO

    }

    @Override
    public void sendMsg(final PushMsg pushMsg)
    {
        InfrastructureType type = pushMsg.getInfrastructureType();
        ExecutorService executor = null;
        MsgSender sender = null;
        if(null != type){
            sender = infrastructures.get(type);
        }
        switch (pushMsg.getMsgType())
        {
            case SMS: // 短信发送
                executor = msgExecutors;
                sender = null != sender ?  sender : defaultSmsSenderInfrastructure;
                break;
            case APP_PUSH: // app推送
                executor = pushExecutors;
                sender = null != sender ?  sender : defaultPushSenderInfrastructure;
                break;
            default:
                throw new UnsupportedOperationException("不受支持的消息类型：" + pushMsg.getMsgType());
        }
        log.debug("消息id '" + pushMsg.getIdStr() + "'使用的设施为：" + sender.getSendChannel(null));
        final MsgSender infrastructure = sender;
        executor.execute(new Runnable()
        {
            @Override
            public void run()
            {
                infrastructure.sendMsg(pushMsg);
            }

        });
    }

    @Override
    public InfrastructureType getSendChannel(String code)
    {
        return defaultSmsSenderInfrastructure.getSendChannel(null);
    }

}
