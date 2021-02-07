package com.hlj.proj.listener;

import com.hlj.proj.constant.JmsConstant;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * 消息监听器
 */
@Component
public class JmsMsgListener {

    //默认的的JmsTeamplate发送的消息监听
    @JmsListener(destination = JmsConstant.QUEUE_NAME_DEFAULT)
    public void receivedefaultJmsTeamplateQueue(String msg) {
        System.out.println("[" + JmsConstant.QUEUE_NAME_DEFAULT + "]消息:" + msg);
    }

    @JmsListener(destination = JmsConstant.TOPIC_NAME_DEFAULT, containerFactory = JmsConstant.TOPIC_LISTENER_FACTORY)
    public void receivedefaultJmsTeamplateTopic(String msg) {
        System.out.println("[" + JmsConstant.TOPIC_NAME_DEFAULT + "]消息:" + msg);
    }



    //自定的JmsTeamplate发送的消息监听
    @JmsListener(destination = JmsConstant.QUEUE_NAME)
    public void receiveQueue(String msg) {
        System.out.println("[" + JmsConstant.QUEUE_NAME + "]消息:" + msg);
    }

    @JmsListener(destination = JmsConstant.TOPIC_NAME, containerFactory = JmsConstant.TOPIC_LISTENER_FACTORY)
    public void receiveTopicName(String msg) {
        System.out.println("[" + JmsConstant.TOPIC_NAME + "]消费者:receive=" + msg);
    }

    @JmsListener(destination = JmsConstant.PRSISTENT_TOPIC_NAME, containerFactory = JmsConstant.PRSISTENT_TOPIC_LISTENER_FACTORY)
    public void receivePrsistentTopicName(String msg) {
        System.out.println("[" + JmsConstant.PRSISTENT_TOPIC_NAME + "]消费者:receive=" + msg);
    }

}
