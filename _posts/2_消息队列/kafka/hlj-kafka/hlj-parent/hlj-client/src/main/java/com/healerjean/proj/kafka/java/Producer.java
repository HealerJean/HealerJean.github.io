package com.healerjean.proj.kafka.java;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {



    private static final String TOPIC="HLJ_TOPIC_JAVA";
    private static final String BROKER_LIST="localhost:9092";
    private static KafkaProducer<String,String> producer = null;

    static{
        Properties configs = initConfig();
        producer = new KafkaProducer<>(configs);
    }

    private static Properties initConfig(){
        Properties properties = new Properties();

        // bootstrap.servers  表示 Kafka 集群 。 如果集群中有多台物理服务器 ，则服务器地址之间用逗号分隔，
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);

        // 在考虑请求完成之前，生产者要求leader收到的确认数量，这将控制发送的记录的持久性。
        // acks=0   如果设置为零，则生产者不会等待来自服务器的任何确认。该记录将被立即添加到套接字缓冲区并被视为已发送。在这种情况下，retries不能保证服务器已经收到记录，并且配置不会生效（因为客户端通常不会知道任何故障）。为每个记录返回的偏移量将始终设置为-1。
        // acks=1   这意味着领导者会将记录写入其本地日志中，但会在未等待所有追随者完全确认的情况下作出响应。在这种情况下，如果领导者在承认记录后但在追随者复制之前立即失败，那么记录将会丢失。
        // acks=all 这意味着领导者将等待全套的同步副本确认记录。这保证只要至少有一个同步副本保持活动状态，记录就不会丢失。这是最强有力的保证。这相当于acks = -1设置。
        properties.put(ProducerConfig.ACKS_CONFIG, "all");


        // key.serializer 和 value.serializer 表示消息的序列化类型 。
        // Kafka 的消息是以键值对的形式发送到 Kafka 服务器的，在消息被发送到服务器之前 ，
        // 消息生产者需要把不同类型的消息序列化为 二进制类型，
        // 示例中是发送文本消息到服务器 ， 所以使用的是 StringSerializer。
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
        return properties;
    }

    public static void main(String[] args){

        // 其中 topic 和 value 是必填的， partition 和 key 是可选 的 。
        // 1、如果指定了 partition，那么消息会 被发送至指定的 partition;
        // 2、如果没指定 partition  但指定了 Key，那么消息会按照、 hash(key)发送 至对应的partition:
        // 3、如果既没指定partition也没指定 key，那么消息会按照 round-robin模式发送 (即以轮询的方式依次发送〉到每一个 partition。

        producer.send(new ProducerRecord<>(TOPIC, 0, "key", "(String topic, K key, V value)"));
        producer.send(new ProducerRecord<>(TOPIC, "key", "(String topic, K key, V value)"));
        producer.send(new ProducerRecord<>(TOPIC, "key", "(String topic, K key, V value)"));
        producer.send(new ProducerRecord<>(TOPIC, "(String topic, V value)"));
        producer.close();
    }


}
