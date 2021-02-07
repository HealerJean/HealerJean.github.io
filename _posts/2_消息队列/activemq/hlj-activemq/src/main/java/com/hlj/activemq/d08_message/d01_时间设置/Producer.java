package com.hlj.activemq.d08_message.d01_时间设置;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;


public class Producer {

    /**
     * 队列的名称
     */
    public static final String QUEUE_NAME = "queue";

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
            //延迟3秒
            Long delay = 3L * 1000L;
            //周期3秒
            Long period = 3L * 1000L;
            //重复5次 消费者受到的为 1（不重复的）  + 5 = 6
            int repeat = 5;
            TextMessage message = session.createTextMessage("MESSAGE消息");
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, delay);
            message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD, period);
            message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT, repeat);
            producer.send(message);
    }


}
