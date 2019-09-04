package com.hlj.activemq.d04_Mysql持久化.d02_topic持久化;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class MysqlPersistenceConsumer {

    public static final String TOPIC_NAME = "mysql_persiterce_topic_name";
    public static final Long WITE_TIME = (1000L);


    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                ActiveMqConstant.BROKER_URL);
        try {
            Connection connection = connectionFactory.createConnection();
            //设置连接客户端 id
            connection.setClientID("ClientId_Blog");


            Session session = connection.createSession(
                    Boolean.TRUE,
                    Session.AUTO_ACKNOWLEDGE);

            Topic topic = session.createTopic(TOPIC_NAME);
            //创建持久化的订阅者，订阅者的名称 name
            // TopicSubscriber consumer = session.createDurableSubscriber(topic, "name");
            TopicSubscriber consumer = session.createDurableSubscriber(topic, "Sub_HealerJean");
            //一定要砸在上面持久化订阅设置（createDurableSubscriber）完成之后再start这个connection，否则会有问题
            connection.start();
            System.out.println("创建持久化消费者");

            Message message = consumer.receive();
            while (message != null) {
                TextMessage txtMsg = (TextMessage) message;
                System.out.println("收到消 息：" + txtMsg.getText());
                message = consumer.receive(WITE_TIME);
            }
            session.commit();
            session.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
