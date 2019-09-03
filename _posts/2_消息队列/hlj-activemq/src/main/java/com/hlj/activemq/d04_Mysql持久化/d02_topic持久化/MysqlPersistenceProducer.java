package com.hlj.activemq.d04_Mysql持久化.d02_topic持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MysqlPersistenceProducer {


    public static final String TOPIC_NAME = "mysql_persiterce_topic_name";
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

            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(TOPIC_NAME);
            MessageProducer producer = session.createProducer(topic);
            //设置持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            //一定要砸在上面持久化订阅设置完成之后再start这个connection，否则会有问题
            connection.start();
            System.out.println("创建持久化生产者");

            for (int i = 1; i <= SEND_NUMBER; i++) {
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
