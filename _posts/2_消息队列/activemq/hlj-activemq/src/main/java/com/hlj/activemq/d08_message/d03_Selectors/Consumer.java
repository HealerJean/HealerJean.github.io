package com.hlj.activemq.d08_message.d03_Selectors;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer {

    public static final String QUEUE_NAME = "FirstQueue";
    public static final Long WITE_TIME = (100L * 1000L);
    public static final int COUNT = 2;


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
            MessageConsumer consumer = session.createConsumer(destination, "age>24");
            consumer.setMessageListener(msg -> {
                try {
                    TextMessage txtMsg = (TextMessage) msg;
                    System.out.println("消费者" + txtMsg.getText());
                } catch (JMSException e) {
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}



