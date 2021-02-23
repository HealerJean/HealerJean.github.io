package com.healerjean.proj.kafka.java;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;

import java.util.Collections;
import java.util.Properties;


@Slf4j
public class Consumer {

    private static final String TOPIC = "HLJ_TOPIC_JAVA";
    private static final String BROKER_LIST = "localhost:9092";
    private static final String GROUP_ID = "HTEST_GROUP";

    private static KafkaConsumer<String, String> consumer = null;

    static {
        Properties properties = initConfig();
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList(TOPIC));
    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        // 指定 broker 的地址清单，表示 Kafka 集群， 如果集群中有多台物理服务器 ，则服务器地址之间用逗号分。
        // 清单里不需要包含所有的 broker 地址，生产者会从给定的 broker 里查找到其他 broker 的信息。不过建议至少要 提供两个 broker 的信息，一旦其中一个宕机，生产者仍然能够连接到集群上。
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);

        // group.id 表示消费者的分组 ID
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        // key.deserializer和 value.deserializer表示消息的反序列化类型。
        //它的用途与在KafkaProducer 中的用途是一样的，另外两个属性 key. deserializer 和 value.deserializer 与生产者的 serializer 定义也很类似，
        // 不过它们不是使 用指定的类把 Java 对象转成字节数组，而是使用指定的类把字节数组转成 Java 对象。
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }


    /**
     * 消费者消费消息
     *     轮询不只是获取数据那么简单。在第一次调用新消费者的 poll() 方法时，它会负责查找 GroupCoordinator，然后加入群组，接受分配的分区。如果发生了再均衡，整个过程也是 在轮询期间进行的。当然，心跳也是从轮询里发送出去的。所以，我们要确保在轮询期间 所做的任何处理工作都应该尽快完成。
     * 注释1：这是一个无限循环。消费者实际上是一个长期运行的应用程序，它通过持续轮询向 Kafka 请求数据(消费者必须持续对 Kafka 进行轮询，否则会被认为已经死亡，它的分区会被移交给群组里的其他消费者)。稍后我们会介绍如何退出循环，并关闭消费者。
     * 注释2：传给 poll() 方法的参数是一个超时时间，用于控制 poll() 方法的阻塞时间(在消费者的缓冲区里没有可用数据时会发生阻塞)。如果该参数被设为 0，poll() 会立即返回，否则 它会在指定的毫秒数内一直等待 broker 返回数据。poll() 方法有一个超时参数，它指定了方法在多久之后可以返回， 不管有没有可用的数据都要返回。超时时间的设置取决于应用程序对响应速度的要求， 比如要在多长时间内把控制权归还给执行轮询的线程。
     * 注释3：poll() 方法返回一个记录列表。每条记录都包含了记录所属主题的信息、记录所在分区的信息、记录在分区里的偏移量，以及记录的键值对。我们一般会遍历这个列表，逐 条处理这些记录。
     * 注释4：在退出应用程序之前使用 close() 方法关闭消费者。网络连接和 socket 也会随之关闭， 并立即触发一次再均衡，而不是等待群组协调器发现它不再发送心跳并认定它已死亡， 因为那样需要更长的时间，导致整个群组在一段时间内无法读取消息。
     */
    @Test
    public void consumer(){
        try {
            //注释1
            while (true) {
                //注释2
                ConsumerRecords<String, String> records = consumer.poll(100);
                //注释3
                for (ConsumerRecord<String, String> record : records) {
                    log.info("topic=[{}], partition = [{}], offset = [{}], key = [{}], value =[{}]", record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            log.error("消费者处理数据失败", e);
        } finally {
            //注解4
            consumer.close();
        }
    }


}