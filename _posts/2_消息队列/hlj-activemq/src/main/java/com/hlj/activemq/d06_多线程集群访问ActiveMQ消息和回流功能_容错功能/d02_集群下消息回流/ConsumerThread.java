package com.hlj.activemq.d06_多线程集群访问ActiveMQ消息和回流功能_容错功能.d02_集群下消息回流;

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
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(message -> {
                TextMessage txtMsg = (TextMessage) message;
                try {
                    System.out.println( txtMsg.getText()+consumer.toString());
                    session.commit();
                    //一直监听，不要关闭，关闭的话，这个consumer 也就没了
                    // session.close();
                    // connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch(Exception err){
            err.printStackTrace();
        }
    }

}
