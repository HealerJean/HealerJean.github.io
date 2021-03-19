package com.healerjean.proj.kafka.java;

/**
 * @author zhangyujin
 * @date 2021/3/11  3:59 下午.
 * @description
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;

import java.util.Collections;
import java.util.Properties;

@Slf4j
public class QuitConsumer {


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

        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }


    /**
     * 注释1： ShutdownHook 运行在单独的线程里，所以退出循环最安全的方式只能是调用 wakeup() 方法。
     * 注释2：在另一个线程里调用 wakeup() 方法，导致 poll() 抛出 WakeupException。你可能想捕获 异常以确保应用不会意外终止，但实际上这不是必需的。
     * 注释3： 在退出之前，确保彻底关闭了消费者，关闭消费者会马上通知群组协调器进行一次分区再均衡，而不需要通过心跳判断
     */
    @Test
    public void test() {
        final Thread mainThread = Thread.currentThread();
        //注释1：
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("Starting exit...");
            consumer.wakeup();
            try {
                // 主线程继续执行，以便可以关闭consumer，提交偏移量
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));

        try {
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(1000);
                for (ConsumerRecord<String, String> record : records) {
                    log.info("topic=[{}], partition = [{}], offset = [{}], key = [{}], value =[{}]", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
                consumer.commitAsync();
            }
        } catch (WakeupException e) {
            //注释2：
            log.info("消费者退出", e);
        } finally {
            //注释3
            consumer.close();
            log.info("Closed consumer and we are done");
        }
    }
}
