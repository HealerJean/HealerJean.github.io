package com.hlj.activemq.d01_简单的生产者消费者_事务的签收;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer {


    public static final String QUEUE_NAME = "FirstQueue";
    public static final Long   WITE_TIME = (100L * 1000L);


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

            // 消息的目的地;消息发送给谁.
            Destination destination = session.createQueue(QUEUE_NAME);

            //根据目的地获取一个消费者
            MessageConsumer consumer = session.createConsumer(destination);


            //消费消息
            //1、接收TestMessage
            reveiveTestMessage(consumer);
            //2、接收MapMessage
            // receiveMapMessage(consumer);


            // 没有事务，下面提交会报错
            // session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 同步接收：主线程阻塞式等待下一个消息的到来，可以设置timeout，超时则返回null。
    // 异步接收：主线程设置MessageListener，然后继续做自己的事，子线程负责监听。

    /**
     * 3、监听接收消息
     */
    private static void reveiveByListeneMessage(MessageConsumer consumer) throws JMSException {
        consumer.setMessageListener(msg -> {
            try {
                TextMessage txtMsg = (TextMessage)msg;
                System.out.println("Receiver11111===="+txtMsg.getText());
            } catch (JMSException e) {
            }
        });
    }


    /**
     * 2、接收MapMessage
     */
    private static void receiveMapMessage(MessageConsumer consumer) throws JMSException {
        int i = 1;
        while (true) {
            //100s内阻塞等待消息的传入
            MapMessage message = (MapMessage) consumer.receive(WITE_TIME);
            if (null != message) {
                System.out.printf("收到消息：");
                System.out.printf(message.getString("setString_key_" + i));
                System.out.printf(message.getStringProperty("setStringProperty_key_" + i));
                System.out.println();
                i++;
            } else {
                break;
            }
        }
    }


    /**
     * 1、接收TestMessage
     */
    private static void reveiveTestMessage(MessageConsumer consumer) throws JMSException {
        while (true) {
            //100s内阻塞等待消息的传入
            TextMessage message = (TextMessage) consumer.receive(WITE_TIME);
            if (null != message) {
                System.out.println("收到消息" + message.getText());
            } else {
                break;
            }
        }
    }




}



