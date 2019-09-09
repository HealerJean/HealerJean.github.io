package com.hlj.proj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;

/**
 * @author HealerJean
 * @ClassName ActiveMQConfig
 * @date 2019/9/3  11:27.
 * @Description
 */
@Configuration
@EnableJms
public class ActiveMQConfig {

    /**
     * springboot默认只配置queue类型消息，如果要使用topic类型的消息，则需要配置该bean
     */
    @Bean(name = "jmsTopicListenerContainerFactory")
    public JmsListenerContainerFactory jmsTopicListenerContainerFactory(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这里必须设置为true，false则表示是queue类型
        factory.setPubSubDomain(true);
        return factory;
    }

}
