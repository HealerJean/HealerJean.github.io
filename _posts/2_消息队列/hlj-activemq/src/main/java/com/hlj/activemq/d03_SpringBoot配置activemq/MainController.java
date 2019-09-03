package com.hlj.activemq.d03_SpringBoot配置activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * @author HealerJean
 * @ClassName Controller
 * @date 2019/9/3  11:32.
 * @Description
 */
@RestController
@RequestMapping("hlj/activemq")
public class MainController {

    @Autowired
    private JMSProducer jmsProducer;

    /**
     * 发送消息的数量
     */
    public static final String QUEUE_NAME = "SpringBoot:Queue";
    public static final String TOPIC_NAME = "SpringBoot:Topic";

    /**
     * 发送queue类型消息
     */
    @GetMapping("/queue")
    @ResponseBody
    public String sendQueueMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return "msg不能为空";
        }
        Queue destination = new ActiveMQQueue(QUEUE_NAME);
       jmsProducer.sendMessage(destination, msg);
        return "queue send success";
    }


    /**
     * 发送topic类型消息
     */
    @GetMapping("/topic")
    @ResponseBody
    public String sendTopicMsg(String msg) {
        if (StringUtils.isBlank(msg)) {
            return "msg不能为空";
        }
        Topic destination = new ActiveMQTopic(TOPIC_NAME);
        jmsProducer.sendMessage(destination, msg);
        return "topic send success";
    }



}
