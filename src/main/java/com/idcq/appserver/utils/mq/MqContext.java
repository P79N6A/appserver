package com.idcq.appserver.utils.mq;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.listeners.ContextInitListener;

/**
 * mq 工具
 * 
 */
public class MqContext
{

    private static final Log logger = LogFactory.getLog(MqContext.class);

    private static MqContext mqContext = new MqContext();

    public static DefaultMQProducer defaultMQProducer;

    String nginxHost = null;


    private MqContext()
    {
        initProducer();
    }


    /**
     * 初始化生产者
     * 
     */
    public static DefaultMQProducer initProducer()
    {
        /**
         * 一个应用创建一个Producer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ProducerGroupName(1dcq)需要由应用来保证唯一<br>
         * ProducerGroup这个概念发送普通的消息时，作用不大，但是发送分布式事务消息时，比较关键，
         * 因为服务器会回查这个Group下的任意一个Producer
         */
        defaultMQProducer = new DefaultMQProducer(CommonConst.MQ_SERVER_PRODUCER_GROUP_NAME);
        try
        {
            Properties props = ContextInitListener.MQ_PROPS;
            String host = props.getProperty(CommonConst.MQ_SERVER_HOST);
            String port = props.getProperty(CommonConst.MQ_SERVER_PORT);
            defaultMQProducer.setNamesrvAddr(host + ":" + port);
            //实例名称
            defaultMQProducer.setInstanceName(CommonConst.MQ_SERVER_INSTANCE_NAME);
            defaultMQProducer.start();
        }
        catch (MQClientException e)
        {
            logger.error("mq服务器初始化异常");
            e.printStackTrace();
        }
        finally {
        	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				
				@Override
				public void run() {
					logger.info("------关闭MqProducer------");
					defaultMQProducer.shutdown();
				}
			}));
        }
        return defaultMQProducer;
    }


    public static MqContext getInstance()
    {
        return mqContext;
    }
    public static DefaultMQProducer getProducer(){
        return defaultMQProducer;
    }

}
