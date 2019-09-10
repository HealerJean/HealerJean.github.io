package com.hlj.proj.config;

import com.hlj.proj.constant.JmsConstant;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName ActiveMQConfig
 * @Date 2019/9/10  15:51.
 * @Description
 */
@Configuration
@EnableJms
public class ActiveMQConfig {

    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String jmsUser;

    @Value("${spring.activemq.password}")
    private String jsmPass;


    @Bean(name = "connectionFactory")
    public ActiveMQConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(jmsUser, jsmPass, brokerUrl);
        activeMQConnectionFactory.setTrustAllPackages(true);
        return activeMQConnectionFactory;
    }

    @Primary
    @Bean("jmsTemplate")
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
        return new JmsTemplate(connectionFactory);
    }


    /**
     * 持久化的 prsistentJmsTemplate
     */
    @Bean("persistentJmsTemplate")
    public JmsTemplate persistentJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate persistentJmsTemplate = new JmsTemplate();
        persistentJmsTemplate.setConnectionFactory(connectionFactory);
        //设置为true，deliveryMode, priority, timeToLive等设置才会起作用，否则使用默认的值
        persistentJmsTemplate.setExplicitQosEnabled(true);
        persistentJmsTemplate.setDeliveryMode(DeliveryMode.PERSISTENT);
        return persistentJmsTemplate;
    }

    /**
     * 非持久化的 noPrsistentJmsTemplate
     */
    @Bean("noPersistentJmsTemplate")
    public JmsTemplate noPersistentJmsTemplate(ConnectionFactory connectionFactory) {
        JmsTemplate persistentJmsTemplate = new JmsTemplate();
        persistentJmsTemplate.setConnectionFactory(connectionFactory);
        //设置为true，deliveryMode, priority, timeToLive等设置才会起作用，否则使用默认的值
        persistentJmsTemplate.setExplicitQosEnabled(true);
        persistentJmsTemplate.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        return persistentJmsTemplate;
    }


    // springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置bean
    /**
     * 发布/订阅 非持久化Topic
     */
    @Bean(JmsConstant.TOPIC_LISTENER_FACTORY)
    public JmsListenerContainerFactory topicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        // 开启非持久化
        factory.setSubscriptionDurable(false);
        return factory;
    }



    /**
     * 发布/订阅 持久化Topic
     */
    @Bean(JmsConstant.PRSISTENT_TOPIC_LISTENER_FACTORY)
    public DefaultJmsListenerContainerFactory prsistentTopicListenerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        // 开启持久化
        factory.setSubscriptionDurable(true);
        // 设置clientId
        factory.setClientId(JmsConstant.PRSISTENT_TOPIC_NAME_CLIENT);
        return factory;
    }
}
