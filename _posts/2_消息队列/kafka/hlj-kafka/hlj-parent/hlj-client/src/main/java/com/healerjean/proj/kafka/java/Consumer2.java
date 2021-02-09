package com.healerjean.proj.kafka.java;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.Properties;


public class Consumer2 {

    private static final String TOPIC="HLJ_TOPIC_JAVA";
    private static final String BROKER_LIST="localhost:9092";
    private static KafkaConsumer<String,String> kafkaConsumer = null;

    static {
        Properties properties = initConfig();
        kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(TOPIC));
    }

    private static Properties initConfig(){
        Properties properties = new Properties();
        // bootstrap.servers  表示 Kafka 集群 。 如果集群中有多台物理服务器 ，则服务器地址之间用逗号分隔，
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,BROKER_LIST);
        // group.id 表示消费者的分组 ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,"group.hgroup2");
        // client.id 表示客户端的 Id
        properties.put(ConsumerConfig.CLIENT_ID_CONFIG,"client.hid");

        // key.deserializer和 value.deserializer表示消息的反序列化类型。
        // 把来自 Kafka集群的二进制消息反序列化为指定的类型，
        // 因为序列化用的是 String 类型 ，所以用 StringDeserializer 来反序列化。
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        return properties;
    }

    public static void main(String[] args){
        try{

            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                for (ConsumerRecord<String, String> record : records)
                    System.out.printf("partition = %d, offset = %d, key = %s, value = %s%n", record.partition(), record.offset(), record.key(), record.value());
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            kafkaConsumer.close();
        }
    }

}