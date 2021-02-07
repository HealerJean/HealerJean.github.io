package com.hlj.activemq.d06_多线程集群访问ActiveMQ消息和回流功能_容错功能.d04_均衡消费;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer61616 {


    public static final String QUEUE_NAME = "average.Consumer.queue";
    public static final String TCP_URL = "tcp://localhost:61616";
    public static final int COUNT = 1;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                TCP_URL);

        try {
            for(int i = 1 ; i <= COUNT; i++ ){
                Connection connection = connectionFactory.createConnection();
                connection.start();
                Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
                Destination destination = session.createQueue(QUEUE_NAME);
                MessageConsumer consumer = session.createConsumer(destination);
                int size = i ;
                consumer.setMessageListener(message -> {
                    TextMessage txtMsg = (TextMessage) message;
                    try {
                        System.out.println("61616消费者"+size+txtMsg.getText());
                        session.commit();
                        //一直监听，不要关闭，关闭的话，这个consumer 也就没了
                        // session.close();
                        // connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

}



