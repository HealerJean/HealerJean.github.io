package com.hlj.activemq.d06_多线程集群访问ActiveMQ消息和回流功能_容错功能.d02_集群下消息回流;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.ConnectionFactory;


public class Consumer61616 {


    public static final String QUEUE_NAME = "back.queue";
    public static final String TCP_URL = "tcp://localhost:61616";
    public static final  int THREAD_COUNT = 10 ;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                TCP_URL);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            Thread thread = new ConsumerThread(connectionFactory, QUEUE_NAME);
            thread.start();
            //延迟一秒，观察日志，保证正在监听
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程"+i+"启动");
        }
    }

}



