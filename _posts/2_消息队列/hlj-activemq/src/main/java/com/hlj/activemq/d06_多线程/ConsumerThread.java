package com.hlj.activemq.d06_多线程;

import javax.jms.*;

/**
 * @author HealerJean
 * @ClassName ConsumerThread
 * @date 2019/9/4  16:26.
 * @Description
 */
public class ConsumerThread extends Thread {

    private ConnectionFactory cf ;
    private String queueName ;

    public ConsumerThread(ConnectionFactory cf,String queueName){
        this.cf = cf;
        this.queueName = queueName;
    }

    @Override
    public void run(){
        try{
            Connection connection = cf.createConnection();
            connection.start();
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);
            while (true) {
                //100s内阻塞等待消息的传入
                TextMessage message = (TextMessage) consumer.receive();
               System.out.println("收到消息" + message.getText());
            }

        }catch(Exception err){
            err.printStackTrace();
        }
    }

}
