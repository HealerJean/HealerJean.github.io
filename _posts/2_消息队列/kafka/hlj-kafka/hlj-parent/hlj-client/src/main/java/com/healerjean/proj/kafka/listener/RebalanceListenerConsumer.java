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
 * @date 2021/2/24  9:44 下午.
 * @description
 */
@Service
@Slf4j
public class RebalanceListenerConsumer {

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
        consumer.subscribe(Collections.singletonList(TOPIC), new RebalanceListener(consumer));
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
   public static class RebalanceListener implements ConsumerRebalanceListener {


        private KafkaConsumer<String, String> consumer;

        /**
         * 初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
         */
        public RebalanceListener(KafkaConsumer<String, String> consumer) {
            this.consumer = consumer;
        }


        /**
         * 该方法会在再均衡之后和消费者读取之前被调用
         * 使用：在获得新分区后开始读取消息，不需要做其他事情。
         */
        @Override
        public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
            long committedOffset = -1;
            for (TopicPartition topicPartition : partitions) {
                // 获取该分区已经提交的偏移量
                committedOffset = consumer.committed(topicPartition).offset();
                System.out.println("重新分配分区，提交的偏移量：" + committedOffset);
                // 重置偏移量到上一次提交的偏移量的下一个位置处开始消费
                consumer.seek(topicPartition, committedOffset + 1);
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
            System.out.println("分区再均衡，提交当前偏移量：" + currentOffsets);
            consumer.commitSync(currentOffsets);
        }

    }


    @Test
    public void consumer() {
        try {
            int count = 1;
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.info("topic=[{}], partition = [{}], offset = [{}], key = [{}], value =[{}]", record.partition(), record.offset(), record.key(), record.value());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1, "no metadata");
                    currentOffsets.put(new TopicPartition(record.topic(), record.partition()), offsetAndMetadata);
                    if (count++ % 1000 == 0) {
                        // 注释4
                        consumer.commitAsync(currentOffsets, null);
                    }
                }
            }
        } catch (Exception e) {
            log.error("消费者处理数据失败", e);
        } finally {
            try {
                //注释2
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }

}

