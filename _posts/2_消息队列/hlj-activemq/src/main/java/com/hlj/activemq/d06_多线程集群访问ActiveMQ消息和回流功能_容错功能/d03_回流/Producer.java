package com.hlj.activemq.d06_多线程集群访问ActiveMQ消息和回流功能_容错功能.d03_回流;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Producer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "FirstQueue";
    /** 发送消息的数量 */
    private static final int SEND_NUMBER = 5;

    public static void main(String[] args) {

        String linuxIp = "myLinuxQj";
        ConnectionFactory cf = new ActiveMQConnectionFactory(
                );


        // failover故障转移
        // randomize false 不允许随机，如果能够连上61616 则用它，连不上在用61617
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                "failover:(tcp://localhost:61616,tcp://localhost:61617)?randomize=false");
        try {
            // 构造从工厂得到连接对象
            Connection connection = connectionFactory.createConnection();
            connection.start();

            // 获取操作连接,一个发送或接收消息的线程
            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);

            // 根据目的地获取一个生产者
            MessageProducer producer = session.createProducer(destination);

            //构造消息
            //1 、创建TextMessage
            sendTextMessage(session, producer);
            //2 、创建MapMessage
            // sendMapMessage(session, producer);


            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 2、创建MapMessage
     */
    private static void sendMapMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setStringProperty("setStringProperty_key_" + i, "setStringProperty_key_" + i);
            mapMessage.setString("setString_key_" + i, "setString_value " + i);
            producer.send(mapMessage);
        }
    }

    /**
     * 1、创建TextMessage
     */
    private static void sendTextMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMq 发送的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }






}
