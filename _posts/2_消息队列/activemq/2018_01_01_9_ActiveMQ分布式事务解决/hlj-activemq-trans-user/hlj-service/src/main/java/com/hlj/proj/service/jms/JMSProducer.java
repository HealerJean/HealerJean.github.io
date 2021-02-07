package com.hlj.proj.service.jms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;

/**
 * @author HealerJean
 * @ClassName JMSProducer
 * @date 2019/9/3  10:26.
 * @Description
 */
@Service
public class JMSProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    /**
     * 发送Queue消息
     */
    public void sendMessage(Destination destination, String message) {
        this.jmsTemplate.convertAndSend(destination, message);
    }


}
