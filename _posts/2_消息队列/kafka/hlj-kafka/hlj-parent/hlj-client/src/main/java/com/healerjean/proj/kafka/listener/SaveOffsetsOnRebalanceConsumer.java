package com.healerjean.proj.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author zhangyujin
 * @date 2021/3/10  9:04 下午.
 * @description
 */
@Service
@Slf4j
public class SaveOffsetsOnRebalanceConsumer {

    private static final String TOPIC = "HLJ_TOPIC_JAVA";
    private static final String BROKER_LIST = "localhost:9092";
    private static final String GROUP_ID = "HTEST_GROUP";
    //记录当前偏移量
    private static Map<TopicPartition, OffsetAndMetadata> currentOffsets = new HashMap<>();
    private static KafkaConsumer<String, String> consumer = null;

    static {
        Properties properties = initConfig();
        consumer = new KafkaConsumer<>(properties);
        //消费者添加再均衡监听器
        consumer.subscribe(Collections.singletonList(TOPIC), new SaveOffsetsOnRebalance(consumer));
        // 注释3
        consumer.poll(0);
        for (TopicPartition partition: consumer.assignment()){
            //注释4：
            // consumer.seek(partition, getOffsetFromDB(partition));
        }

    }

    private static Properties initConfig() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return properties;
    }

    /**
     * 再均衡监听器
     */
    @Slf4j
    public static class SaveOffsetsOnRebalance implements ConsumerRebalanceListener {


        private KafkaConsumer<String, String> consumer;

        /**
         * 初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
         */
        public SaveOffsetsOnRebalance(KafkaConsumer<String, String> consumer) {
            this.consumer = consumer;
        }


        /**
         * 该方法会在再均衡之后和消费者读取之前被调用
         * 使用：在获得新分区后开始读取消息，不需要做其他事情。
         */
        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            for(TopicPartition partition: partitions){
                //注释2：
                // consumer.seek(partition, getOffsetFromDB(partition));
            }
        }

        /**
         * 该方法会在再均衡开始之前和消费者停止读取之后被调用。如果在这个方法中提交偏移量，则下一个消费者就可以获得读取的偏移量。
         * 使用：在失去分区所有权之前通过 onPartitionsRevoked() 方法来提交偏移量
         * 解释：如果发生再均衡，我们要在即将失去分区所有权时提交偏移量。
         * 要注意，提交的是最近处理过的偏移量，而不是批次中还在处理的最后一个偏移量。因为分区有可能在我们还在处理消息的时候被撤回。
         *        我们要提交所有分区的偏移量，而不只是那些即将失去所有权的分区的偏移量——因为提交的偏移量是已经处理过的，所以不会有什么问题。调用 commitSync() 方法，确保在再均衡发生之前提交偏移量。
         */
        @Override
        public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
            //注释1：
            // commitDBTransaction();
        }

    }


    @Test
    public void consumer() {
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                log.info("topic=[{}], partition = [{}], offset = [{}], key = [{}], value =[{}]", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                // processRecord(record);
                //保存记录结果
                // storeRecordInDB(record);
                //保存位移
                //storeOffsetInDB(record.topic(), record.partition(), record.offset());
            }
            //提交数据库事务，保存消费的记录以及位移
            // commitDBTransaction();
        }
    }

}

