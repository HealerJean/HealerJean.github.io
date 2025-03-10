package com.hlj.activemq.d07_destination.d01_多个queue同时发送;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Producer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "queue_1,queue_2";
    /** 发送消息的数量 */
    private static final int SEND_NUMBER = 5;

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
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);

            // 根据目的地获取一个生产者
            MessageProducer producer = session.createProducer(destination);

            //构造消息
            //1 、创建TextMessage
            sendTextMessage(session, producer);

            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void sendTextMessage(Session session, MessageProducer producer) throws JMSException {
        for (int i = 1; i <= SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMq 发送的消息" + i);
            // 发送消息到目的地方
            System.out.println("发送消息：" + "ActiveMq 发送的消息" + i);
            producer.send(message);
        }
    }


}
