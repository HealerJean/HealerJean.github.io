package com.healerjean.proj.kafka.java;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

@Data
@Slf4j
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

        // broker 希望接收到的消息的键和值都是字节数组  不过生 产者需要知道如何把这些 Java 对象转换成字节数组
        // 1、key.serializer 必须被设置为一 个实现了 org.apache.kafka.common.serialization.Serializer 接口的类，生产者会使 用这个类把键对象序列化成字节数组
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 2、与 key.serializer 一样，value.serializer 指定的类会将值序列化。如果键和值都是字 符串，可以使用与 key.serializer 一样的序列化器。如果键是整数类型而值是字符串， 那么需要使用不同的序列化器。
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());


        // acks 参数指定了必须要有多少个分区副本收到消息，生产者才会认为消息写入是成功的。 这个参数对消息丢失的可能性有重要影响。该参数有如下选项。
        // KafkaProducer把消息发送出去，不需要等待任何确认收到的消息，没有任何保障可以保证此种情况下server已经成功接收到数据，同时重试配置也不会发生作用(因为KafkaProducer并不知道此次发送是否失败)。
        // 该情况，当数据已经发送出去，还在半路，此时leader挂了，但是producer还是认为消息发送成功了，这个时候就会导致这条消息丢失；
        // 数据可靠性是最低的，传输效率也是最高的

        //如果 acks=1，只要集群的首领节点收到消息，生产者就会收到一个来自服务器的成功 响应。
        // 如果消息无法到达首领节点(比如首领节点崩溃，新的首领还没有被选举出来)， 生产者会收到一个错误响应，为了避免数据丢失，生产者会重发消息。不过，如果一个 没有收到消息的节点成为新首领，消息还是会丢失。
        // 这个时候的吞吐量取决于使用的是同步发送还是异步发送。
        // 如果让发送客户端等待服务器的响应(通过调用 Future 对象 的 get() 方法)，显然会增加延迟(在网络上传输一个来回的延迟)。
        // 如果客户端使用回调，延迟问题就可以得到缓解，

        // 如果 acks=all，只有当所有参与复制的节点全部收到消息时，生产者才会收到一个来自服务器的成功响应。这种模式是最安全的，它可以保证不止一个服务器收到消息，
        // 就算有服务器发生崩溃，整个集群仍然可以运行。不过，它的延迟比 acks=1 时更高，因为我们要等待不只一个服务器节点接收消息。
        properties.put(ProducerConfig.ACKS_CONFIG, "0");

        return properties;
    }

    public static void main(String[] args){

        String key = "Precision_Products";
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, "Hello fireAndForgetRecord");

        //以下3种发送方式
        //1、发送并忘记：我们把消息发送给服务器，但并不关心它是否正常到达。大多数情况下，消息会正常到达，因为 Kafka 是高可用的，而且生产者会自动尝试重发。不过，使用这种方式有时候 也会丢失一些消息
        // 我们使用生产者的 send() 方法发送 ProducerRecord 对象。从生产者的架构图里可以看 到，消息先是被放进缓冲区，然后使用单独的线程发送到服务器端。
        // send() 方法会返 回一个包含 RecordMetadata 的 Future 对象，不过因为我们会忽略返回值，所以无法知 道消息是否发送成功。如果不关心发送结果，那么可以使用这种发送方式。比如，记录不太重要的应用程序日志。
        // try {
        //     producer.send(record);
        // } catch (Exception e) {
        //     log.info("消息发送失败", e);
        // }


        //2、同步发送：我们使用 send() 方法发送消息，它会返回一个 Future 对象，调用 get() 方法进行等待， 就可以知道消息是否发送成功。
        // //producer.send() 方法先返回一个 Future 对象，然后调用 Future 对象的 get() 方法等待 Kafka 响应。如果服务器返回错误，get() 方法会抛出异常。如果没有发生错 误，我们会得到一个 RecordMetadata 对象，可以用它获取消息的偏移量。
        // try {
        //     //在这里，producer.send() 方法先返回一个 Future 对象，然后调用 Future 对象的 get() 方法等待Kafka 响应。如果服务器返回错误，get() 方法会抛出异常。如果没有发生错 误，我们会得到一个 RecordMetadata 对象，可以用它获取消息的偏移量。
        //     RecordMetadata recordMetadata = producer.send(record).get();
        //     log.info("消息发送成功，返回结果【{}】",recordMetadata);
        // } catch (Exception e) {
        //     //如果在发送数据之前或者在发送过程中发生了任何错误，比如 broker 返回了一个不允许重发消息的异常或者已经超过了重发的次数，那么就会抛出异常。我们只是简单地把 异常信息打印出来。
        //     log.error("消息发送失败", e);
        // }
        // 打印日志
        // 2021-02-18 18:44:34 INFO  -[                                ]- 消息发送成功，返回结果【{"offset":0,"timestamp":1613645074360,"serializedKeySize":18,"serializedValueSize":25,"topicPartition":{"hash":-1133698772,"partition":0,"topic":"HLJ_TOPIC_JAVA"}}】 com.healerjean.proj.kafka.java.Producer.main[68]
        // 2021-02-18 18:45:14 INFO  -[                                ]- 消息发送成功，返回结果【{"offset":1,"timestamp":1613645114891,"serializedKeySize":18,"serializedValueSize":25,"topicPartition":{"hash":-1133698772,"partition":0,"topic":"HLJ_TOPIC_JAVA"}}】 com.healerjean.proj.kafka.java.Producer.main[68]


        ProducerRecord<String, String> recordAck = new ProducerRecord<>("TEST_ACK_1", "TEST_ACK_KEY", "Hello");
        for (int i = 0; i < 100000; i++) {
            try {
                //在这里，producer.send() 方法先返回一个 Future 对象，然后调用 Future 对象的 get() 方法等待Kafka 响应。如果服务器返回错误，get() 方法会抛出异常。如果没有发生错 误，我们会得到一个 RecordMetadata 对象，可以用它获取消息的偏移量。
                RecordMetadata recordMetadata = producer.send(recordAck).get();
                log.info("消息发送成功，返回结果【{}】",recordMetadata);
            } catch (Exception e) {
                //如果在发送数据之前或者在发送过程中发生了任何错误，比如 broker 返回了一个不允许重发消息的异常或者已经超过了重发的次数，那么就会抛出异常。我们只是简单地把 异常信息打印出来。
                log.error("消息发送失败==========================", e);
            }
        }

        // //3、异步发送
        // producer.send(record, (metadata, exception) -> {
        //     //如果 Kafka 返回一个错误，onCompletion 方法会抛出一个非空(non null)异常。这里 我们只是简单地把它打印出来，但是在生产环境应该有更好的处理方式。
        //     log.info("消息发送成功，返回结果【{}】",metadata);
        //     if(exception != null){
        //         exception.printStackTrace();
        //     }
        // });
        // 打印日志



        // // 其中 topic 和 value 是必填的， partition 和 key 是可选 的 。
        // // 1、如果指定了 partition，那么消息会 被发送至指定的 partition;
        // // 2、如果没指定 partition  但指定了 Key，那么消息会按照、 hash(key)发送 至对应的partition:
        // // 3、如果既没指定partition也没指定 key，那么消息会按照 round-robin模式发送 (即以轮询的方式依次发送〉到每一个 partition。
        //
        // producer.send(new ProducerRecord<>(TOPIC, 0, "key", "(String topic, K key, V value)"));
        // producer.send(new ProducerRecord<>(TOPIC, "key", "(String topic, K key, V value)"));
        // producer.send(new ProducerRecord<>(TOPIC, "key", "(String topic, K key, V value)"));
        // producer.send(new ProducerRecord<>(TOPIC, "(String topic, V value)"));
        // producer.close();

    }


}
