package com.hlj.activemq.d06_多线程集群访问ActiveMQ消息和回流功能_容错功能.d01_多线程集群消费;

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
                    System.out.println("Receiver===" + txtMsg.getText()+consumer.toString());
                    session.commit();
                    //如果注释掉下面的话，是会一直监听，
                    // 而这里呢，表示每个消费者只读取一次就好，关闭的话，这个consumer 也就没了，这个线程就会结束
                    session.close();
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }catch(Exception err){
            err.printStackTrace();
        }
    }

}
