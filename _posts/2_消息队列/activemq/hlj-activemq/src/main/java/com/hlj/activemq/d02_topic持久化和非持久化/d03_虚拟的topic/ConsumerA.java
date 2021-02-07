package com.hlj.activemq.d02_topic持久化和非持久化.d03_虚拟的topic;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class ConsumerA {

    public static final String CONSUMER_VIRTUAL_TOPIC_NAME = "Consumer.AA.VirtualTopic.Name";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            // 构造从工厂得到连接对象
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // 获取操作连接,一个发送或接收消息的线程
            Session session = connection.createSession(
                    Boolean.FALSE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.//名称为A的区别
            Destination destination = session.createQueue(CONSUMER_VIRTUAL_TOPIC_NAME);

            //根据目的地获取一个消费者
            MessageConsumer consumer = session.createConsumer(destination);


            //消费消息
            //1、接收TestMessage
            reveiveTestMessage(consumer);

            // 没有事务，下面提交会报错
            // session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void reveiveTestMessage(MessageConsumer consumer) throws JMSException {
        while (true) {
            //100s内阻塞等待消息的传入
            TextMessage message = (TextMessage) consumer.receive();
            if (null != message) {
                System.out.println(CONSUMER_VIRTUAL_TOPIC_NAME + "收到消息" + message.getText());
            } else {
                break;
            }
        }
    }

}



