/**
 * Copyright (C) 2015 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.common.PushConsumer
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2015年12月29日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.utils.mq.demo;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
/**
 *  mq 消费者者demo
 * @author ChenYongxin
 *
 */
public class MqConsumerDemo
{

    /**
     * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。<br>
     * 但是实际PushConsumer内部是使用长轮询Pull方式从MetaQ服务器拉消息，然后再回调用户Listener方法<br>
     */
    public static void main(String[] args) throws InterruptedException, MQClientException
    {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         * 各个项目组可以用topic区分，tag
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("1dcq");
        consumer.setNamesrvAddr("localhost:9876");
        consumer.setInstanceName("Consumber111");

        /**
         * 订阅指定topic下tags分别等于TagA或TagC或TagD
         */
        consumer.subscribe("topic1", "tag1");
        /**
         * 订阅指定topic下所有消息<br>
         * 注意：一个consumer对象可以订阅多个topic
         */
        consumer.subscribe("topic1", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently()
        {

            /**
             * 默认msgs里只有一条消息，可以通过设置consumeMessageBatchMaxSize参数来批量接收消息
             */
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context)
            {
                System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + msgs.size());

                MessageExt msg = msgs.get(0);
                //通过topic标示取哪种类型消息
                if (msg.getTopic().equals("topic1"))
                {
                    // 执行TopicTest1的消费逻辑
                    if (msg.getTags() != null && msg.getTags().equals("tag1"))
                    {
                        // 执行TagA的消费
                        System.out.println("接受到的消息为:"+new String(msg.getBody()));
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

        });
        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         */
        consumer.start();

        System.out.println("Consumer Started.");
    }

}
