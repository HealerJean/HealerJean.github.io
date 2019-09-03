package com.hlj.activemq.d03_SpringBoot配置activemq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author HealerJean
 * @ClassName JMSConsumer
 * @date 2019/9/3  10:27.
 * @Description
 */
@Slf4j
@Component
public class JMSConsumer {

    public static final String QUEUE_NAME = "SpringBoot:Queue";
    public static final String TOPIC_NAME = "SpringBoot:Topic";


    /**
     * 接收queue类型消息
     * destination对应配置类中ActiveMQQueue("SpringBoot:Queue")设置的名字
     */
    @JmsListener(destination = QUEUE_NAME)
    public void listenQueue(String msg) {
        System.out.println("接收到queue消息：" + msg);
    }

    /**
     * 接收topic类型消息
     * destination对应配置类中ActiveMQTopic("SpringBoot:Topic")设置的名字
     * containerFactory对应配置类中注册JmsListenerContainerFactory的bean名称
     */
    @JmsListener(destination = TOPIC_NAME, containerFactory = "jmsTopicListenerContainerFactory")
    public void listenTopic(String msg) {
        System.out.println("接收到topic消息：" + msg);
    }
}
