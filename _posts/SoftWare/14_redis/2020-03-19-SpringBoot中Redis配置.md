---
title: SpringBoot中Redis配置
date: 2020-03-19 03:33:00
tags: 
- Redis
category: 
- Redis
description: SpringBoot中Redis配置
---



<!--
https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/blogImages/ 
　　首行缩进
-->






**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)    



# 1、单节点 

## 1.1、自定义变量 



### 1.1.1、配置`application.properties`

```properties
####################################
### redis
####################################
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
spring.redis.database=0
spring.redis.timeout=3000ms
spring.redis.jedis.pool.max-active=100
spring.redis.jedis.pool.min-idle=0
spring.redis.jedis.pool.max-idle=5
spring.redis.jedis.pool.max-wait=3000ms
####################################
```



### 1.1.2、RedisConfig



```java
package com.healerjean.proj.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.Duration;

@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.database}")
    private int database;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private String timeout;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private String maxWaitMillis;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(database);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration.builder();
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(Long.valueOf(timeout.substring(0, timeout.length() - 2))));
        JedisConnectionFactory factory = new JedisConnectionFactory(redisStandaloneConfiguration,
                jedisClientConfiguration.build());
        return factory;
    }

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setEnableTransactionSupport(false);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

}

```



# 2、集群 

## 2.1、方式1  

### 2.1.1、配置`application.properties`

```properties
####################################
### redis
####################################
spring.redis.lettuce.pool.max-active=20
spring.redis.lettuce.pool.max-idle=20
spring.redis.lettuce.pool.min-idle=5
spring.redis.lettuce.pool.max-wait=3000ms
spring.redis.cluster.max-redirects=3
spring.redis.cluster.nodes=127.0.0.1:7001,127.0.0.1:7002,127.0.0.1:7003,127.0.0.1:7004,127.0.0.1:7005,127.0.0.1:7006
spring.redis.password=password
spring.redis.timeout=3000ms
```



### 2.1.2、`RedisConfig`

```java

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fintech.scf.service.core.dto.CompanyCertificateDTO;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Configuration
public class RedisConfig {


    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Serializable> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setEnableTransactionSupport(false);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class,new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class,new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModule(javaTimeModule);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

```





# 工具类 



## 1、  CacheService

### 1.1、CacheService

```java
package com.healerjean.proj.service.system.cache;

import java.util.concurrent.TimeUnit;

public interface CacheService {

    /**
     * 存放缓存数据
     */
    void set(String key, Object value, long timeout, TimeUnit timeUnit);

    /**
     * 存放不过期数据
     **/
    void set(String key, Object value);


    /**
     * 获取缓存数据
     */
    Object get(String key);

    /**
     * 删除缓存数据
     */
    void delete(String key);

    /**
     * 生成序列号不重复
     */
    String generateSeqNo(String prefixKey);

    /**
     * 生成序列号不重复
     */
    String generateSeqNo();

    /**
     * 缓存计数
     */
    Long increment(String key, long number);

    /**
     * 设置过期时间
     */
    void expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 分布式锁
     *
     * @param key
     * @return
     */
    boolean lock(String key, long timeout, TimeUnit timeUnit);

    /**
     * 分布式锁-释放锁
     *
     * @param key
     * @return
     */
    void unlock(String key);
}

```



### 1.2、`CacheServiceImpl`

```java
package com.healerjean.proj.service.system;

import com.healerjean.proj.service.system.cache.CacheService;
import com.healerjean.proj.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.healerjean.proj.constant.CommonConstants.REDIS_HLJ;
import static com.healerjean.proj.constant.CommonConstants.REDIS_LOCK;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName CacheServiceImpl
 * @Date 2019/10/18  14:55.
 * @Description 缓存服务
 */
@Service
public class CacheServiceImpl implements CacheService {

    private static final String SEQNO_FORMAT = "0000";

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void set(String key, Object value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public String generateSeqNo(String prefixKey) {
        String minuteStr = DateUtils.toDateString(new Date(), DateUtils.YYYYMMDDHHMMSS);
        StringBuffer sb = new StringBuffer();
        sb.append(prefixKey).append(minuteStr);
        String temp = sb.toString();
        Long number = this.redisTemplate.opsForValue().increment(temp, 1L);
        this.redisTemplate.expire(temp, 2L, TimeUnit.SECONDS);
        DecimalFormat decimalFormat = new DecimalFormat(SEQNO_FORMAT);
        return sb.append(decimalFormat.format(number)).toString();
    }

    @Override
    public String generateSeqNo() {
        String minuteStr = DateUtils.toDateString(new Date(), DateUtils.YYYYMMDDHHMMSS);
        StringBuffer sb = new StringBuffer().append(minuteStr);
        String temp = sb.toString();
        Long number = this.redisTemplate.opsForValue().increment(temp, 1L);
        this.redisTemplate.expire(temp, 2L, TimeUnit.SECONDS);
        DecimalFormat decimalFormat = new DecimalFormat(SEQNO_FORMAT);
        return sb.append(decimalFormat.format(number)).toString();
    }

    @Override
    public Long increment(String key, long number) {
        return redisTemplate.opsForValue().increment(key, number);
    }

    @Override
    public void expire(String key, long timeout, TimeUnit timeUnit) {
        redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public boolean lock(String key, long timeout, TimeUnit timeUnit) {
        try {
            Long lock = increment(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, 1);
            if (lock == 1) {
                expire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeout, timeUnit);
                return true;
            } else {
                Long expire = redisTemplate.getExpire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeUnit);
                if (expire != null && expire.equals(-1L)) {
                    expire(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key, timeout, timeUnit);
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            delete(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key);
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        delete(REDIS_HLJ + ":" + REDIS_LOCK + ":" + key);
    }
}

```






  **<font  color="red">感兴趣的，欢迎添加博主微信 </font>**       

​    

哈，博主很乐意和各路好友交流，如果满意，请打赏博主任意金额，感兴趣的在微信转账的时候，备注您的微信或者其他联系方式。添加博主微信哦。    

请下方留言吧。可与博主自由讨论哦   



|微信 | 微信公众号|支付宝|
|:-------:|:-------:|:------:|
| ![微信](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|![支付宝](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/tctip/alpay.jpg) |



<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'HCR1LxaBUWsXFTZ9',
    });
    gitalk.render('gitalk-container');
</script> 
