---
title: redis使用工具类进行保存数据和Ehcache注解缓存类实现缓存value
date: 2018-03-20 03:33:00
tags: 
- 缓存
- redis
- Ehcache
category: 
- 缓存
- redis
- Ehcache
description: redis使用工具类进行保存数据和Ehcache注解缓存类实现缓存value
---
<!-- image url 
https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages
　　首行缩进
<font color="red">  </font>
<br/>
-->

## 前言
 本文主要是key value的形式。
这里我先说下序列化吧
 <font color="red">
GenericToStringSerializer：使用Spring转换服务进行序列化（可以用来专门转化存放Double等类型，我下面的工具类有介绍）；
JacksonJsonRedisSerializer：使用Jackson 1，将对象序列化为JSON；
Jackson2JsonRedisSerializer：使用Jackson 2，将对象序列化为JSON；
JdkSerializationRedisSerializer：使用Java序列化；
StringRedisSerializer：序列化String类型的key和value。实际上是String和byte数组之间的转换，那么Key只能是String类型的，不能为Long，Integer，否则会报错抛异常。

</font>

StringSerializer就是通过String.getBytes()来实现的，而且在Redis中，所有存储的值都是字符串类型的。所以这种方法保存后，通过Redis-cli控制台，是可以清楚的查看到我们保存了什么key,value是什么。它只能对String类型进行序列化

JdkSerializationRedisSerializer，这个序列化方法是Idk提供的，要求要被序列化的类继承自Serializeable接口，然后通过Jdk对象序列化的方法保存，这个序列化保存的对象，即使是个String类型的，在redis控制台，也是看不出来的，还是类似于 乱码的东西。因为它保存了一些对象的类型什么的额外信息。除非中间再加上一些objectmappr就可以看到内容了，如下
 

```
@Bean
public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
	RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
	redisTemplate.setConnectionFactory(factory);

	//key序列化方式,StringXX是转为String，JacksonXX是将对象转为json。
	// 需要注意这里Key使用了StringRedisSerializer，那么Key只能是String类型的，不能为Long，Integer，否则会报错抛异常。
	// 就是假如PostRepository里定义的@Cacheable（key="0"）的话就会报错，因为这样作为key的是int型，key必须为String。
	//所以在没有自己定义key生成策略的时候，可以不配置,我就没有配置
	RedisSerializer<String> redisSerializer = new StringRedisSerializer();//Long类型不可以会出现异常信息;
	redisTemplate.setKeySerializer(redisSerializer);

	Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
	ObjectMapper om = new ObjectMapper();
	om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	jackson2JsonRedisSerializer.setObjectMapper(om);

	redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);


	return redisTemplate;
}


```
![WX20180320-193953@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-193953@2x.png)


## Ehcache和redis应该怎么用

我公司的项目中既有Ehcache 又有Redis，所以这里都会讲解
ehcache直接在jvm虚拟机中缓存，速度快，效率高；但是缓存共享麻烦，集群分布式应用不方便。

redis是通过socket访问到缓存服务，效率比ecache低，比数据库要快很多，处理集群和分布式缓存方便，有成熟的方案。

如果是单个应用或者对缓存访问要求很高的应用，用ehcache。
如果是大型系统，存在缓存共享、分布式部署、缓存内容很大的，建议用redis。

补充下：ehcache也有缓存共享方案，不过是通过RMI或者Jgroup多播方式进行广播缓存通知更新，缓存共享复杂，维护不方便；简单的共享可以，但是涉及到缓存恢复，大数据缓存，则不合适





## 1、springBoot引入spring配置文件，进行redis的搭建<br/>


```
@ImportResource(value = "classpath:applicationContext.xml")
@SpringBootApplication
public class SinoredisSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SinoredisSpringbootApplication.class, args);
	}
}

```

### 1.1、spring配置文件如下<br/>


```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <description>spring configuration</description>


    <import resource="applicationContext-redis.xml"/>

</beans>

```

### 1.2、redis配置信息在properties中如下<br/>


```
########################################################
###REDIS (RedisProperties) redis
########################################################
hlj.redis.host-name=127.0.0.1
hlj.redis.password=
hlj.redis.max-total=64
hlj.redis.max-idle=30
hlj.redis.port=6379
hlj.redis.pool.max-wait=-1

```
### 1.3、redis配置文件如下<br/>

这里可以看到我使用了自定义的key 和value的序列化方式

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig" >
        <property name="maxTotal" value="${hlj.redis.max-total}"/>
        <property name="maxIdle" value="${hlj.redis.max-idle}"/>
        <property name="maxWaitMillis" value="${hlj.redis.pool.max-wait}"/>
    </bean>

    <bean id="jedisFactory" class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory" destroy-method="destroy">
        <property name="password" value="${hlj.redis.password}"/>
        <property name="hostName" value="${hlj.redis.host-name}"/>
        <property name="port" value="${hlj.redis.port}"/>
        <property name="usePool" value="true"/>
        <property name="poolConfig" ref="jedisPoolConfig"/>
    </bean>

    <bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate" scope="prototype">
        <property name="connectionFactory" ref="jedisFactory"/>
        <property name="keySerializer">
            <bean class="com.hlj.redis.cache.CustomStringRedisSerializer"/>
        </property>
        <property name="valueSerializer">
            <bean class="com.hlj.redis.cache.CustomJSONStringRedisSerializer"/>
        </property>
    </bean>
</beans>

```
### 1.3.1 自定义key的序列化方式


```
package com.hlj.config.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;

/**
 * 类名称：CustomStringRedisSerializer
 * 类描述：定制String 序列转换
 * 创建人：qingxu
 * 修改人：
 * 修改时间：2016/3/2 15:04
 * 修改备注：
 *
 * @version 1.0.0
 */
public class CustomStringRedisSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    public CustomStringRedisSerializer() {
        this(Charset.forName("UTF8"));
    }

    public CustomStringRedisSerializer(Charset charset) {
        Assert.notNull(charset);
        this.charset = charset;
    }

    public String deserialize(byte[] bytes) {
        return bytes == null?null:new String(bytes, this.charset);
    }

    public byte[] serialize(Object string) {
        if(string == null ) {
            return null;
        }
        if(string instanceof Long){
            return String.valueOf(string).getBytes(this.charset);

        }
        if(string instanceof Integer){
            return String.valueOf(string).getBytes(this.charset);

        }
        if(string instanceof BigDecimal){
            return ((BigDecimal)string).toString().getBytes(this.charset);
        }

        if(string instanceof BigInteger){
            return ((BigInteger)string).toString().getBytes(this.charset);
        }

        if(string instanceof Double){
            return ((Double)string).toString().getBytes(this.charset);
        }

        if(string instanceof Float){
            return ((Float)string).toString().getBytes(this.charset);
        }
        return ((String)string).getBytes(this.charset);
    }
}


```


#### 1.3.2、自定义value的序列化方式


```
package com.hlj.config.serializer;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * 类名称：CustomJSONStringRedisSerializer
 * 类描述：转换对象为json字符串
 * 创建人：qingxu
 * 修改人：
 * 修改时间：2016/3/2 15:04
 * 修改备注：
 *
 * @version 1.0.0
 */
public class CustomJSONStringRedisSerializer implements RedisSerializer<Object> {

    public static final String EMPTY_JSON = "{}";

    public static final byte[] EMPTY_ARRAY = new byte[0];

    private final ObjectMapper mapper;

    /**
     * Creates {@link CustomJSONStringRedisSerializer} and configures {@link ObjectMapper} for default typing.
     */
    public CustomJSONStringRedisSerializer() {
        this((String) null);
    }

    /**
     * Creates {@link CustomJSONStringRedisSerializer} and configures {@link ObjectMapper} for default typing using the
     * given {@literal name}. In case of an {@literal empty} or {@literal null} String the default
     * {@link JsonTypeInfo.Id#CLASS} will be used.
     *
     * @param classPropertyTypeName Name of the JSON property holding type information. Can be {@literal null}.
     */
    public CustomJSONStringRedisSerializer(String classPropertyTypeName) {

        this(new RedisObjectMapper());

        if (StringUtils.hasText(classPropertyTypeName)) {
            mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
    }

    /**
     * Setting a custom-configured {@link ObjectMapper} is one way to take further control of the JSON serialization
     * process. For example, an extended {@link} can be configured that provides custom serializers for
     * specific types.
     *
     * @param mapper must not be {@literal null}.
     */
    public CustomJSONStringRedisSerializer(ObjectMapper mapper) {
        Assert.notNull(mapper, "ObjectMapper must not be null!");

        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
        this.mapper = mapper;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
     */
    @Override
    public byte[] serialize(Object source) throws SerializationException {
        if (source == null) {
            return EMPTY_ARRAY;
        }

        try {
            return mapper.writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
     */
    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        return deserialize(source, Object.class);
    }

    /**
     * @param source can be {@literal null}.
     * @param type must not be {@literal null}.
     * @return {@literal null} for empty source.
     * @throws SerializationException
     */
    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {

        Assert.notNull(type,
                "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");

        if (source == null || source.length == 0) {
            return null;
        }

        try {
            return mapper.readValue(source, type);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}


```


1.3.4、如果使用上面序列化的话，那么redis库总可以看到是class或者是请他的java数据类型，下图是后期的答案，这里只是简单的介绍

![WX20180320-194420@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-194420@2x.png)

![WX20180320-194333@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-194333@2x.png)

### 1.4、至此，其实redis就已经搭建好了<br/>


## 2、、reids存储对象工具类<br/>

### 2.1、工具类<br/>

1、这个工具类，在描述的时候，就说道，分不清Long和Integer，Double和Float。所以我们一般只用来存放对象
2、本类中可以看到有两个服务类`ValueOperations`和`RedisOperations`，但是其实是同一个类只不过名字不同。只是其作用的范围表面上意思不一样，**第一个是用来存取数据，第二个用来操作参数数据的**


```
package com.hlj.redis.redisTool;

import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 类描述：
 * 操作redis 对象数据的工具类
 * 提供存取数字外对象的存取，数字类型使用RedisLongData/RedisIntegerData进行操作
 * 这个类分不清Long和Integer,Float和Double。也无法进行原子操作
 * 创建人： j.sh
 * 创建时间： 2016/3/1
 * version：1.0.0
 */
@Component
public class RedisObjectData {

    @Resource(name="redisTemplate")
    private ValueOperations<String, Object> valueOperations;

    @Resource(name = "redisTemplate")
    private RedisOperations<String,Object> operations;

    /**
     * 根据key获取数据
     * @param key
     * @return
     */
    public Object getData(String key) {
        return valueOperations.get(key);
    }

    /**
     * 设置数据
     * @param key
     * @param object
     */
    public void setData(String key,Object object) {
        valueOperations.set(key,object);
    }

    /**
     * 根据key 删除对应的数据
     * @param key
     */
    public void delete(String key) {
        operations.delete(key);
    }
}

```

### 2.2、controller测试

#### 2.2.1、添加缓存对象和读取缓存对象

```
package com.hlj.controller;

import com.hlj.Format.ResponseBean;
import com.hlj.bean.Person;
import com.hlj.redis.redisTool.RedisLongData;
import com.hlj.redis.redisTool.RedisObjectData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@Controller
public class RedisObjectDataController {

	@Autowired
	private RedisObjectData redisObjectData;

	/**
	 * 添加缓存对象
	 */
    @RequestMapping("/setRedisObjectData")
    public @ResponseBody ResponseBean setRedisObjectData(Long id){
		try {
			Person person = new Person();
			person.setName("HealerJean");
			person.setPwd("123456");
			person.setId(id);
			redisObjectData.setData("person", person);
			return  ResponseBean.buildSuccess();
		}catch (Exception e){
			return ResponseBean.buildFailure(e.getMessage());
		}
    }

	/**
	 * 根据key获取缓存对象
	 * @return
	 */
	@RequestMapping("/getRedisObjectData")
	public @ResponseBody ResponseBean getRedisObjectData(String key){
	   	try {
			Person person = (Person) redisObjectData.getData(key);
			return ResponseBean.buildSuccess(person);
		}catch (Exception e){
	   		return  ResponseBean.buildFailure(e.getMessage());
		}
	}

```

服务器启动：
1.1、浏览器打开输入 [http://localhost:8080/setRedisObjectData?id=1](http://localhost:8080/setRedisObjectData?id=1)<br/>

![WX20180320-122358@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-122358@2x.png)

1.2、查看redis中的数据,发现已经有数据了数据了<br/>

![WX20180320-122446@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-122446@2x.png)

2.1、对数据进行读取，浏览器中打开[http://localhost:8080/getRedisObjectData?key=person](http://localhost:8080/getRedisObjectData?key=person)<br/>

![WX20180320-122622@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-122622@2x.png)

2.2、上述读取数据成功<br/>



#### 2.2.2、 根据key值删除缓存<br/>

```
/**
 * 根据key删除缓存对象
 * @return
 */
@RequestMapping("/delRedisObjectData")
public @ResponseBody ResponseBean delRedisObjectData(String key){
	try {
		redisObjectData.delete(key);
		return ResponseBean.buildSuccess();
	}catch (Exception e){
		return  ResponseBean.buildFailure(e.getMessage());
	}
}


```

1.1、浏览器中打开 [http://localhost:8080/delRedisObjectData?key=person](http://localhost:8080/delRedisObjectData?key=person)<br/>

![WX20180320-122846@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-122846@2x.png)

1.2、查看redis库,删除成功

![WX20180320-122931@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-122931@2x.png)

#### 2.2.3、存储List对象集合 <br/>


```

/**
 * 添加List缓存对象
 * @param id
 * @return
 */
@RequestMapping("/setListRedisObjectData")
public @ResponseBody ResponseBean setListRedisObjectData(Long id){
	try {
		List<Person> persons = new ArrayList<>();
		Person person1= new Person(id,"HealerJean"+id,"password"+id);
		Person person2 = new Person(id+1,"HuangLiang"+id,"HuangLiang"+id);
		persons.add(person1);
		persons.add(person2);
		redisObjectData.setData("persons", persons);
		return  ResponseBean.buildSuccess();
	}catch (Exception e){
		return ResponseBean.buildFailure(e.getMessage());
	}
}

/**
 * 根据key获取缓存List对象
 * @return
 */
@RequestMapping("/getListRedisObjectData")
public @ResponseBody ResponseBean getListRedisObjectData(String key){
	try {
		List<Person> persons = (List<Person>) redisObjectData.getData(key);
		return ResponseBean.buildSuccess(persons);
	}catch (Exception e){
		return  ResponseBean.buildFailure(e.getMessage());
	}
}


```

1.1、浏览器中打开[http://localhost:8080/setListRedisObjectData?id=1](http://localhost:8080/setListRedisObjectData?id=1)

![WX20180320-123152@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-123152@2x.png)

1.2、查看redis库,有数据

![WX20180320-123233@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-123233@2x.png)

2.1、根据key值获取list对象，浏览器中打开[http://localhost:8080/getListRedisObjectData?key=persons](http://localhost:8080/getListRedisObjectData?key=persons)

![WX20180320-123356@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-123356@2x.png)

2.2、成功,下面要测试，清除下redis库

```
fulshdb
```


#### 2.2.4、测试放入Long，报错误异常Integer不能转化为Long


```
/**
 * 添加Long类型缓存对象，这是个错误的演示，仅供测试
 * @param id
 * @return
 */
@RequestMapping("/set")
public @ResponseBody ResponseBean set(Long id){
	try {

		redisObjectData.setData("id", id);
		return  ResponseBean.buildSuccess();
	}catch (Exception e){
		return ResponseBean.buildFailure(e.getMessage());
	}
}

/**
 * 根据key获取Long，这是个错误的演示，仅供测试
 * @return
 */
@RequestMapping("/get")
public @ResponseBody ResponseBean get(String key){
	try {
		Long id = (Long) redisObjectData.getData("id");
		return ResponseBean.buildSuccess(id);
	}catch (Exception e){
		return  ResponseBean.buildFailure(e.getMessage());
	}
}


```

1.1、浏览器中打开[http://localhost:8080/set?id=1](http://localhost:8080/set?id=1)

![WX20180320-131826@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-131826@2x.png)


1.2、查看redis库

![WX20180320-131858@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-131858@2x.png)

2.1、根据key值获取，这个时候 就会报错了，朋友们,提示Integer不能转化为long

![WX20180320-132003@2x](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/blogImages/WX20180320-132003@2x.png)


## 3、redis存取Long类型工具类<br/>

### 3.1、工具类代码<br/>


```
package com.hlj.redis.redisTool;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis操作Long类型数据的工具类
 * 提供Long类型原子操作
 * 默认一天过期,其他过期时间调用setExpire
 * 创建人： j.sh
 * 创建时间： 2016/3/1
 * version：1.0.0
 */
@Component
public class RedisLongData implements InitializingBean {

    @Resource(name="redisTemplate")
    private ValueOperations<String, Long> valueOperations;

    @Resource(name="redisTemplate")
    private RedisTemplate<String,Long> redisTemplate;


    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setValueSerializer(new GenericToStringSerializer<Long>(Long.class));
        valueOperations = redisTemplate.opsForValue();
    }

    /**
     * 根据key 删除
     * @param key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 进行数值的增加
     * @param key
     * @param value
     * @return
     */
    public Long increase(String key,long value){
        Long result = valueOperations.increment(key,value);
        this.setExpire(key,1L,TimeUnit.DAYS);
        return result;
    }

    /**
     * 进行数值的增加
     * @param key
     * @return
     */
    public Long increase(String key){
        return increase(key,1);
    }

    /**
     * 进行数值的递减
     * @param key
     * @param value
     * @return
     */
    public Long decrease(String key,long value){
        Long result =  valueOperations.increment(key,0-value);
        this.setExpire(key,1L,TimeUnit.DAYS);
        return result;
    }

    /**
     * 进行数值的递减
     * @param key
     * @return
     */
    public Long decrease(String key){
        return decrease(key,1);
    }

    /**
     * 根据key获取
     * @param key
     * @return
     */
    public Long get(String key) {
        return valueOperations.get(key);
    }

    /**
     * 设置
     * @param key
     * @param value
     */
    public void set(String key,Long value) {
        valueOperations.set(key,value);
        this.setExpire(key,1L,TimeUnit.DAYS);
    }

    /**
     * 过期时间，默认单位秒
     * @param key
     * @param time
     */
    public void setExpire(String key,Long time){
        redisTemplate.expire(key,time, TimeUnit.SECONDS);
    }

    /**
     * 过期时间
     * @param key
     * @param time
     * @param timeUnit
     */
    public void setExpire(String key,Long time,TimeUnit timeUnit){
        redisTemplate.expire(key,time,timeUnit);
    }

}


```

### 3.2、controller测试。没啥问题。自己测试吧


```
package com.hlj.controller;

import com.hlj.Format.ResponseBean;
import com.hlj.redis.redisTool.RedisLongData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedisLongDataController {


    @Autowired
    private RedisLongData redisLongData;

    /**
     * 添加Long类型的缓存数据
     */
    @RequestMapping("/setRedisLongData")
    public @ResponseBody  ResponseBean setRedisLongData(Long id){
        try {
            redisLongData.set("long",id);
            return  ResponseBean.buildSuccess();
        }catch (Exception e){
            return ResponseBean.buildFailure(e.getMessage());
        }
    }

	/**
	 * 获取Long类型的缓存数据
	 */
	
    @RequestMapping("/getRedisLongData")
    public @ResponseBody ResponseBean getRedisLongData(String key){
        try {
            Long id = redisLongData.get(key);
            return ResponseBean.buildSuccess(id);
        }catch (Exception e){
            return  ResponseBean.buildFailure(e.getMessage());
        }
    }

}

```

## 4、Ehcache
那么关于它的使用，我先说明，这里是要使用注解的，而且是非常完美的注解

### 1、依赖的导入


```
<!--
        包含支持UI模版（Velocity，FreeMarker，JasperReports），
        邮件服务，
        脚本服务(JRuby)，
        缓存Cache（EHCache），
        任务计划Scheduling（uartz）。
     -->
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-context-support</artifactId>
</dependency>
<!-- 集成ehcache需要的依赖-->
<dependency>
	<groupId>net.sf.ehcache</groupId>
	<artifactId>ehcache</artifactId>
</dependency>

```


### 2、缓存配置文件

之前学习的时候，这个配置文件中药写入value的缓存策略，但是我做了一个自定义的缓存管理器，直接在Java类中配置常量就可以了，非常小巧和方便


```
<!--
  ~ Hibernate, Relational Persistence for Idiomatic Java
  ~
  ~ Copyright (c) 2007, Red Hat Middleware LLC or third-party contributors as
  ~ indicated by the @author tags or express copyright attribution
  ~ statements applied by the authors. All third-party contributions are
  ~ distributed under license by Red Hat Middleware LLC.
  ~
  ~ This copyrighted material is made available to anyone wishing to use, modify,
  ~ copy, or redistribute it subject to the terms and conditions of the GNU
  ~ Lesser General Public License, as published by the Free Software Foundation.
  ~
  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
  ~ or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
  ~ for more details.
  ~
  ~ You should have received a copy of the GNU Lesser General Public License
  ~ along with this distribution; if not, write to:
  ~ Free Software Foundation, Inc.
  ~ 51 Franklin Street, Fifth Floor
  ~ Boston, MA  02110-1301  USA
  -->
<ehcache updateCheck="false">

    <!-- Sets the path to the directory where cache .data files are created.

         If the path is a Java System Property it is replaced by
         its value in the running VM.

         The following properties are translated:
         user.home - User's home directory
         user.dir - User's current working directory
         java.io.tmpdir - Default temp file path -->
    <!--<diskStore path="c:\dev\cache"/>-->

    <!--
       name：Cache的唯一标识
       maxElementsInMemory：内存中最大缓存对象数
       maxElementsOnDisk：磁盘中最大缓存对象数，若是0表示无穷大
       eternal：Element是否永久有效，一但设置了，timeout将不起作用
       overflowToDisk：配置此属性，当内存中Element数量达到maxElementsInMemory时，Ehcache将会Element写到磁盘中
       timeToIdleSeconds：设置Element在失效前的允许闲置时间。仅当element不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大
       timeToLiveSeconds：设置Element在失效前允许存活时间。最大时间介于创建时间和失效时间之间。仅当element不是永久有效时使用，默认是0.，也就是element存活时间无穷大
       diskPersistent：是否缓存虚拟机重启期数据
       diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒
       diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区
       memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）
       -->

    <defaultCache
            maxElementsInMemory="10000"
            eternal="false"
            timeToIdleSeconds="600"
            timeToLiveSeconds="600"
            overflowToDisk="true"
            maxElementsOnDisk="10000000"
            diskPersistent="false"
            diskExpiryThreadIntervalSeconds="120"
            diskSpoolBufferSizeMB="64"
            memoryStoreEvictionPolicy="LRU"
    />
    <!--中间可以包含其他的缓存key-->

</ehcache>

```

### 3、spring中进行配置


```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:cache="http://www.springframework.org/schema/cache"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
 http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd " >


    <description>spring configuration</description>


    <import resource="applicationContext-redis.xml"/>


    <!-- 自定义Ehcache缓存，根据java类加入更多的key 支持注解缓存   -->
    <cache:annotation-driven/>

    <bean id="cacheManager" class="com.hlj.Ehcache.config.CustomEhCacheManager">
        <property name="cacheManager" ref="ehcacheManager"/>
    </bean>

    <bean id="ehcacheManager" class="org.springframework.cache.ehcache.EhCacheManagerFactoryBean">
        <property name="configLocation" value="classpath:ehcache.xml"/>
        <property name="shared" value="true"/>
    </bean>

</beans>

```

#### 3.1、配置自定义的缓存管理器


```
package com.hlj.Ehcache.config;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.config.CacheConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.ehcache.EhCacheCacheManager;

import java.lang.reflect.Field;
import java.util.*;

/**
 *  类描述：继承 EhCacheCacheManager
 * @Description 初始化缓存。加载@CacheConstants中缓存定义及缓存自定义过期时间
 * @Date   2018/3/21 下午2:46.
 */
public class CustomEhCacheManager extends EhCacheCacheManager {

    private static String CACHE_PREFIX = "CACHE_";
    private static String EXPIRE_PREFIX = "EXPIRE_";
    private static Logger logger = LoggerFactory.getLogger(CustomEhCacheManager.class);

    private static List<String> cacheNames = new ArrayList<>();
    private static Map<String,Long> expires = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        try {
            Class clazz = CacheConstants.class;
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){
                if (field.getName().startsWith(CACHE_PREFIX)){
                    cacheNames.add(field.get(clazz).toString());
                } else if (field.getName().startsWith(EXPIRE_PREFIX)){
                    expires.put(
                            clazz.getField(field.getName().replace(EXPIRE_PREFIX,"")).get(clazz).toString(),
                            Long.parseLong(field.get(clazz).toString())
                    );
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            throw new RuntimeException("init cache failure!",e);
        }
        super.afterPropertiesSet();
    }

    @Override
    protected Collection<Cache> loadCaches() {
        LinkedHashSet<Cache> caches = new LinkedHashSet<Cache>(cacheNames.size());
        for(String name:cacheNames){
            Ehcache exist = this.getCacheManager().addCacheIfAbsent(name);
            if(exist != null){
                Cache cache = new EhCacheCache(exist);
                Ehcache ehcache = (Ehcache) cache.getNativeCache();
                CacheConfiguration configuration = ehcache.getCacheConfiguration();
                Long time = expires.get(name);
                configuration.setTimeToIdleSeconds(time);
                configuration.setTimeToLiveSeconds(time);
                caches.add(cache);
            }
        }
        return caches;
    }
}


```


#### 3.2、字段以缓存策略的名称，也叫缓存位置名称


```
package com.hlj.Ehcache.config;

/**
 * 类名称：CacheConstants
 * 类描述：缓存常量类
 * 创建人：HealerJean
 * 需要初始化的缓存定义名称需要以CACHE_为前缀。如：CACHE_XXX
 * 如果需要增加自定义过期时间，则增加过期时间变量定义EXPIRE_为前缀的缓存过期时间.如：EXPIRE_CACHE_XXX
 * 如不设置自定义过期时间即默认spring cache中设置过期时间
 *
 * @version 1.0.0
 */
public class CacheConstants {

    //公共缓存，1分钟过期时间
    public static final String CACHE_PUBLIC_PERSON = "cache.public.person";
    public static final Long EXPIRE_CACHE_PUBLIC_PERSON = 60L;


    public static final String CACHE_PUBLIC = "cache.public";
    public static final Long EXPIRE_CACHE_PUBLIC = 60L;

    public static final String CACHE_PUBLIC_TEN_MINUTE = "cache.public.ten.minute";
    public static final Long EXPIRE_CACHE_PUBLIC_TEN_MINUTE = 10 * 60L;

}
```

### 4、开始测试吧，朋友们，这里不像多讲解了，主要还是看代码吧，注释也非常详细

#### 1、controller

```
package com.hlj.Ehcache.controller;


import com.hlj.Ehcache.service.EhcacheService;
import com.hlj.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/21  下午1:58.
 */
@Controller
public class EhCacheController {


    @Autowired
    private EhcacheService ehcacheService;

    @GetMapping("save")
    public @ResponseBody Person save(Long id) {
        Person person = new Person(id,"EhcacheHealerJean","123465");
        return ehcacheService.save(person);
    }

    @GetMapping("findById")
    public @ResponseBody Person findById(Long id) {
        return ehcacheService.findById(id);
    }

    @GetMapping("update")
    public @ResponseBody Person update(Person person) {
        return ehcacheService.update(person);
    }

    @GetMapping("delete")
    public @ResponseBody String delete(Long id) {
        ehcacheService.delete(id);
        return "删除成功";
    }

    @GetMapping("listPerson")
    public@ResponseBody  List<Person> listPerson() {
        return ehcacheService.listPerson();
    }
}

```

#### 4.2、service

```
package com.hlj.Ehcache.service.impl;

import com.hlj.Ehcache.config.CacheConstants;
import com.hlj.Ehcache.service.EhcacheService;
import com.hlj.bean.Person;
import com.hlj.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/21  下午1:59.
 */
@Service
public class EhcacheServiceImpl implements EhcacheService{

    @Autowired
    private PersonRepository personRepository;


    //这里的单引号不能少，否则会报错，被识别是一个对象;
    public static final String CACHE_KEY = "'person'";

    /**
     * value属性表示使用哪个缓存策略，缓存策略在ehcache.xml,现在存放在下面的实体类中，在启动的时候自动加载了
     * 也叫缓存存放位置名称，不能为空
     */
    public static final String DEMO_CACHE_NAME = CacheConstants.CACHE_PUBLIC_PERSON;


    /**
     * 保存数据，防止是更新的操作，所以将之前缓存的删除,事实上，我也并没有很成功的实现它，后来明白啦，哈，原来是list集合缓存的时候，添加要删除的哦
     * @param Person
     */

    @CacheEvict(value=DEMO_CACHE_NAME,key=CACHE_KEY)
    public Person save(Person Person){
        return personRepository.save(Person);
    }

    /**
     * 查询数据.
     * @param id
     * @return
     */
    @Cacheable(value=DEMO_CACHE_NAME ,key="'Person_'+#id")
    public Person findById(Long id){
        return personRepository.findOne(id);
    }

    /**
     * 修改数据.
     * 在支持Spring Cache的环境下，与@Cacheable不同的是使用@CachePut标注的方法在执行前不会去检查缓存中是否存在之前执行过的结果，而是每次都会执行该方法，并将执行结果以键值对的形式存入指定的缓存中。
     @CachePut也可以标注在类上和方法上。使用@CachePut时我们可以指定的属性跟@Cacheable是一样的。
     */
    @CachePut(value = DEMO_CACHE_NAME,key = "'Person_'+#person.getId()")
    public Person update(Person person)  {
        Person Person = personRepository.findOne(person.getId());
        Person.setName(person.getName());
        Person.setPwd(person.getPwd());
        return Person;
    }


    /**
     * 删除数据.
     * @param id
     */
    @CacheEvict(value = DEMO_CACHE_NAME,key = "'Person_'+#id")//这是清除缓存.
    public void delete(Long id){
        personRepository.delete(id);
    }

    @Cacheable(value=DEMO_CACHE_NAME ,key=CACHE_KEY)
    public List<Person> listPerson() {
        return personRepository.findAll();
    }
}

```

## [代码下载]()


<br/><br/><br/>
如果满意，请打赏博主任意金额，感兴趣的请下方留言吧。可与博主自由讨论哦

|支付包 | 微信|微信公众号|
|:-------:|:-------:|:------:|
|![支付宝](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/alpay.jpg) | ![微信](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/tctip/weixin.jpg)|![微信公众号](https://raw.githubusercontent.com/HealerJean123/HealerJean123.github.io/master/assets/img/my/qrcode_for_gh_a23c07a2da9e_258.jpg)|




<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">
<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean123.github.io`,
		owner: 'HealerJean123',
		admin: ['HealerJean123'],
		id: 'PmGTBiTsWCwEWim1',
    });
    gitalk.render('gitalk-container');
</script> 

<!-- Gitalk end -->

