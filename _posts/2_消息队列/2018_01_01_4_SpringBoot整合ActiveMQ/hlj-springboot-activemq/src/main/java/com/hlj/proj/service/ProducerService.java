package com.hlj.proj.service;

/**
 * 消息生产者服务
 * 转载请注明出处，更多技术文章欢迎大家访问我的个人博客站点：https://www.doufuplus.com
 *
 * @author 丶doufu
 * @date 2019/08/15
 */
public interface ProducerService {

    /**
     * 默认的 jmsTemplate 发送queue消息（默认是持久化的）
     */
    void jmsTemplateSendMsgToQueue(String destination, final String msg);
    /**
     * 默认的 jmsTemplate 发送topic消息（默认是非持久化的）
     */
    void jmsTemplateSendMsgTopTopic(String destination, String msg);


    /**
     * 发送消息（点对点）
     */
    void sendMsgToQueue(String destination, String msg);

    /**
     * 发送消息（非持久化 发布-订阅模式）
     */
    void sendMsgToTopic(String destination, String msg);


    /**
     * 发送消息（持久化 发布-订阅模式）
     */
    void sendMsgTopPrsistentTopic(String destination, String msg);
}
