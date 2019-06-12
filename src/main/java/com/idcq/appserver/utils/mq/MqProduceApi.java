package com.idcq.appserver.utils.mq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public class MqProduceApi
{
	private final static Logger logger = LoggerFactory.getLogger(MqProduceApi.class);
    public static String resultMessage;

    private static MqProduceApi mqApi;

    @Autowired
    private MqProduceApi()
    {
    }

    public static synchronized MqProduceApi getInstance()
    {
        if (mqApi == null)
        {
            mqApi = new MqProduceApi();
        }
        return mqApi;
    }

    /**
     * 发送消息到服务器
     * 
     * @Function: com.idcq.appserver.utils.mq.MqProduceApi.getMessage
     * @Description:
     * 
     * @param topic
     * @param tag
     * @param message
     * 
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月30日 下午4:32:09
     * 
     *                   Modification History: Date Author Version Description
     *                   --
     *                   ------------------------------------------------------
     *                   --------- 2015年12月30日 ChenYongxin v1.0.0 create
     */
    public synchronized static void setMessage(final String topic, String tag, String message, String... keys)
    {
    	
        try
        {
            logger.info("发送消息start，topic为：" + topic);
            final DefaultMQProducer producer = MqContext.getProducer();
            /**
             * Producer对象在使用之前必须要调用start初始化，初始化一次即可<br>
             * 注意：切记不可以在每次发送消息时，都调用start方法 producer.start();
             */
            /**
             * 下面这段代码表明一个Producer对象可以发送多个topic，多个tag的消息。
             * 注意：send方法是同步调用，只要不抛异常就标识成功。但是发送成功也可会有多种状态，<br>
             * 例如消息写入Master成功，但是Slave不成功，这种情况消息属于成功，但是对于个别应用如果对消息可靠性要求极高，<br>
             * 需要对这种情况做处理。另外，消息可能会存在发送失败的情况，失败重试由应用来处理。
             */
            String key = "";
            if (keys != null)
            {
                key = keys.toString();
            }
            Message msg = new Message(topic,// topic 
                    tag,// tag
                    key,// key可以不填，消费者可以通过getkeys获取
                    (message).getBytes());// body
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);

            /**
             * 应用退出时，要调用shutdown来清理资源，关闭网络连接，从MetaQ服务器上注销自己
             * 注意：我们建议应用在JBOSS、Tomcat等容器的退出钩子里调用shutdown方法
             */
        }
        catch (MQClientException e)
        {
            logger.error("RocketMQ路由错误" + e.toString());
            e.printStackTrace();
        }
        catch (RemotingException e)
        {
            logger.error("已经断开连接或不在服务器上" + e.toString());
            e.printStackTrace();
        }
        catch (MQBrokerException e)
        {
            logger.error("MQBroker异常" + e.toString());
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            logger.error("线程在处于等待状态的情况下被中断" + e.toString());
            e.printStackTrace();
        }
        
    }

}
