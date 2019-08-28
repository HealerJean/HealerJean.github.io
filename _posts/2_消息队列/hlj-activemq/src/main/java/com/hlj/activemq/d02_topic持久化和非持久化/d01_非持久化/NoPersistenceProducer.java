package com.hlj.activemq.d02_topic持久化和非持久化.d01_非持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class NoPersistenceProducer {

    /**
     * 非持久化topic名称
     */
    public static final String TOPIC_NAME = "no_persiterce_topic_name";
    /**
     * 发送消息的数量
     */
    private static final int SEND_NUMBER = 5;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(destination);


            for (int i = 0; i < SEND_NUMBER; i++) {
                TextMessage message = session.createTextMessage("message" + i);
                producer.send(message);
            }

            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
