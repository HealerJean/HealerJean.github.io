package com.hlj.activemq.d06_多线程;

import com.hlj.activemq.constants.ActiveMqConstant;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Consumer61617 {


    public static final String QUEUE_NAME = "Thread.Consumer.queue";
    public static final String TCP_URL = "tcp://localhost:61617";
    public static final  int THREAD_COUNT = 30 ;

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMqConstant.USERNAME,
                ActiveMqConstant.PASSWORD,
                TCP_URL);

        for (int i = 1; i <= THREAD_COUNT; i++) {
            Thread thread = new ConsumerThread(connectionFactory, QUEUE_NAME);
            thread.start();
            System.out.println("线程"+i+"启动");
        }
    }


}



