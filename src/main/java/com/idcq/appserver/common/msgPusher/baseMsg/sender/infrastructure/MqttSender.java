package com.idcq.appserver.common.msgPusher.baseMsg.sender.infrastructure;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.idcq.appserver.common.msgPusher.baseMsg.model.PushMsg;
import com.idcq.appserver.common.msgPusher.baseMsg.model.PushResult;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.MsgSender;
import com.idcq.appserver.common.msgPusher.baseMsg.sender.sys.SendCallBack;
import com.idcq.appserver.listeners.ContextInitListener;

/**
 * 该类是向MQTT server发送消息的基础设施
 * @author Administrator
 *
 */
@Component("MQTT_PAHO")
public class MqttSender implements MsgSender
{
    private static final Logger log = LoggerFactory.getLogger(MqttSender.class);

    private static final String MQTT_SERVER_URL = "mqttServerUrl";

    private static final String MQTT_SERVER_USERNAME = "mqttServerUserName";

    private static final String MQTT_SERVER_PASSWORD = "mqttServerPassword";

    // 该发送器发送的message可持久化，亦即会等待client上线
    private MqttClient persistClient;

    // 该发送器发送的message不持久化，亦即不会等待client上线
    private MqttClient transientClient;

    private static String localMac = "";

    private synchronized void getMacAddr()
    {
        if (!"".equals(localMac))
        {
            return;
        }
        byte[] mac = null;
        try
        {
            mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        }
        catch (SocketException e1)
        {
            log.error(e1.getMessage(), e1);
        }
        catch (UnknownHostException e1)
        {
            log.error(e1.getMessage(), e1);
        }
        if (null != mac)
        {
            StringBuffer sb = new StringBuffer("");
            for (int i = 0; i < mac.length; i++)
            {
                // 字节转换为整数
                int temp = mac[i] & 0xff;
                String str = Integer.toHexString(temp);
                // if (i != 0)
                // {
                // sb.append("-");
                // }
                if (str.length() == 1)
                {
                    sb.append("0" + str);
                }
                else
                {
                    sb.append(str);
                }
            }
            localMac = sb.toString();
            // System.out.println("**************************mac:" + localMac);
        }
        // 取不到mac地址则采用时间毫秒
        if ("".equals(localMac))
        {
            // 不稳定因素,有待改进
            // TODO
            localMac = System.currentTimeMillis() + "";
            try
            {
                Thread.sleep(2);
            }
            catch (InterruptedException e)
            {
                log.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 初始化连接
     */
    @PostConstruct
    private synchronized void init()
    {
        this.getMacAddr();
        Properties props = ContextInitListener.JPUSH_PROPS;
        String serverUrl = props.getProperty(MQTT_SERVER_URL);
        if (null == serverUrl)
        {
            throw new IllegalStateException("mqttServerUrl should be set in property file");
        }
        serverUrl = serverUrl.trim();
        serverUrl = serverUrl.startsWith("tcp://") ? serverUrl : "tcp://" + serverUrl;
        String userName = props.getProperty(MQTT_SERVER_USERNAME);
        String password = props.getProperty(MQTT_SERVER_PASSWORD);
        log.info("Mqtt server url : " + serverUrl);
        String clientId = "sys001" + localMac;

        MemoryPersistence persistence = null;
        /* 初始化可持久发送器 */
        if (null == persistClient || !persistClient.isConnected())
        {
            log.info("初始化 persistClient");
            try
            {
                MqttClient tem = null;
                persistence = new MemoryPersistence();
                tem = new MqttClient(serverUrl, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(false);
                connOpts.setUserName(userName);
                connOpts.setPassword(password.toCharArray());
                log.info("emqtt用户名密码******" + userName + ":" + password);
                tem.connect(connOpts);
                persistClient = tem;
                log.info("emqtt persistClient初始化完毕");
            }
            catch (MqttException e)
            {
                log.error(e.getMessage(), e);
            }
        }

        /* 初始化非可持久发送器 */
        if (null == transientClient || !transientClient.isConnected())
        {
            log.info("初始化 transientClient");
            clientId = "sys002" + localMac;
            try
            {
                MqttClient tem = null;
                persistence = new MemoryPersistence();
                tem = new MqttClient(serverUrl, clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                // connOpts.setConnectionTimeout(connectionTimeout);
                // connOpts.setKeepAliveInterval(keepAliveInterval);
                connOpts.setCleanSession(true);
                connOpts.setUserName(userName);
                connOpts.setPassword(password.toCharArray());
                tem.connect(connOpts);
                transientClient = tem;
                log.info("emqtt transientClient初始化完毕");
            }
            catch (MqttException e)
            {
                log.error(e.getMessage(), e);
            }
        }

    }

    /**
     * 销毁连接
     */
    @PreDestroy
    private synchronized void destroy()
    {
        log.debug("start to shut down MQTT_PAHO infrastructure...");
        if (null != persistClient && persistClient.isConnected())
        {
            try
            {
                persistClient.disconnect();
            }
            catch (MqttException e)
            {
                log.error(e.getMessage(), e);
            }
        }
        if (null != transientClient && transientClient.isConnected())
        {
            try
            {
                transientClient.disconnect();
            }
            catch (MqttException e)
            {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void sendMsg(PushMsg pushMsg)
    {
        MqttClient sendClient = pushMsg.getPublicBroadcaster() ? transientClient : persistClient;
        if (null == sendClient || !sendClient.isConnected())
        {
            this.init();
            sendClient = pushMsg.getPublicBroadcaster() ? transientClient : persistClient;
        }
        // System.out.println(sendClient.isConnected());
        SendCallBack callback = pushMsg.getSendCallBack();
        SendCallBack sysCallback = pushMsg.getSendCallBack();
        PushResult rs = new PushResult();
        rs.setSource(pushMsg);
        boolean sendOk = false;
        for (String topic : pushMsg.getTargetId())
        {
            int i = 0;
            while (true)
            {
                try
                {
                    sendClient.publish(topic, pushMsg.getContent(), pushMsg.getQos(), false);
                    log.debug("成功发送消息:" + pushMsg.getIdStr());
                    sendOk = true;
                    break;
                }
                catch (MqttException e)
                {
                    if (i > 3)
                    {
                        break;
                    }
                    i++;
                    this.init();
                    sendClient = pushMsg.getPublicBroadcaster() ? transientClient : persistClient;
                    log.error(e.getMessage(), e);
                    rs.setE(e);
                }
            }
            // 调用用户回调
            if (null != callback)
            {
                if (sendOk)
                {
                    callback.onSuccess(rs);
                }
                else
                {
                    callback.onFailed(rs);
                }
            }
            // 调用系统默认回调
            if (null != sysCallback)
            {
                if (sendOk)
                {
                    sysCallback.onSuccess(rs);
                }
                else
                {
                    sysCallback.onFailed(rs);
                }
            }
        }

    }

    @Override
    public InfrastructureType getSendChannel(String code)
    {
        return InfrastructureType.MQTT_PAHO;
    }

}
