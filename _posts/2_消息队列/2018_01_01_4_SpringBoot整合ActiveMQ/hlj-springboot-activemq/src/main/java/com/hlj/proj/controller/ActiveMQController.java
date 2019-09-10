package com.hlj.proj.controller;

import com.hlj.proj.constant.JmsConstant;
import com.hlj.proj.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("hlj/activemq")
public class ActiveMQController {

    @Autowired
    private ProducerService producerService;

    @RequestMapping("/defaultQueue")
    public String defaultQueue(String msg) {
        producerService.jmsTemplateSendMsgToQueue(JmsConstant.QUEUE_NAME_DEFAULT, msg);
        return "defaultQueue";
    }

    @RequestMapping("/defaultTopic")
    public String defaultTopic(String msg) {
        producerService.jmsTemplateSendMsgTopTopic(JmsConstant.TOPIC_NAME_DEFAULT, msg);
        return "defaultTopic";
    }




    /**
     * 点对点
     */
    @RequestMapping("/queue")
    public String queue(String msg) {
        producerService.sendMsgToQueue(JmsConstant.QUEUE_NAME, msg);
        return "queue";
    }
    /**
     * 非持久化 发布/订阅
     */
    @RequestMapping("/topic")
    public String topic(String msg) {
        producerService.sendMsgToTopic(JmsConstant.TOPIC_NAME, msg);
        return "topic";
    }
    /**
     * 持久化 发布/订阅
     */
    @RequestMapping("/persistentTopic")
    public String persistentTopic(String msg) {
        producerService.sendMsgTopPrsistentTopic(JmsConstant.PRSISTENT_TOPIC_NAME, msg);
        return "persistentTopic";
    }

}
