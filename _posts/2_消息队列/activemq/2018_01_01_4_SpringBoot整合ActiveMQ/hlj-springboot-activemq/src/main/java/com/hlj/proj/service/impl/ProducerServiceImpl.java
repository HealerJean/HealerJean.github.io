package com.hlj.proj.service.impl;

import com.hlj.proj.service.ProducerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * 消息生产者服务实现类
 */
@Service
public class ProducerServiceImpl implements ProducerService {

    /**
     * 系统默认的 jmsTemplate *************************************
     */
    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 默认的 jmsTemplate 发送queue消息（默认是持久化的）
     */
    @Override
    public void jmsTemplateSendMsgToQueue(String destination, final String msg) {
        jmsTemplate.convertAndSend(new ActiveMQQueue(destination), msg);
    }
    /**
     * 默认的 jmsTemplate 发送topic消息（默认是非持久化的）
     */
    @Override
    public void jmsTemplateSendMsgTopTopic(String destination,  String msg) {
        jmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }


    /**
     * 自定义的jmsTemplate *************************************
     */

    @Autowired
    @Qualifier("persistentJmsTemplate")
    private JmsTemplate persistentJmsTemplate;

    @Autowired
    @Qualifier("persistentJmsTemplate")
    private JmsTemplate noPersistentJmsTemplate;

    @Override
    public void sendMsgToQueue(String destination, final String msg) {
        persistentJmsTemplate.convertAndSend(new ActiveMQQueue(destination), msg);
    }

    @Override
    public void sendMsgToTopic(String destination, final String msg) {
        noPersistentJmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }

    @Override
    public void sendMsgTopPrsistentTopic(String destination,  String msg) {
        persistentJmsTemplate.convertAndSend(new ActiveMQTopic(destination), msg);
    }

}
