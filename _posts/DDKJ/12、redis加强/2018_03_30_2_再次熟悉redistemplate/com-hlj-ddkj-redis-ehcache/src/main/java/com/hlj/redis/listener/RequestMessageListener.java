package com.hlj.redis.listener;

import com.hlj.redis.cacheSerializer.CustomJSONStringRedisSerializer;
import com.hlj.redis.cacheSerializer.CustomStringRedisSerializer;
import com.hlj.redis.listener.data.ConvertBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * 通过监听redistemplate进行发送消息
 */
public class RequestMessageListener implements MessageListener {

    private CustomStringRedisSerializer stringRedisSerializer = new CustomStringRedisSerializer();

    private CustomJSONStringRedisSerializer jsonStringRedisSerializer = new CustomJSONStringRedisSerializer();

    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println("message监听");
        ConvertBean convertBean = (ConvertBean) jsonStringRedisSerializer.deserialize(message.getBody());

//        System.out.println(convertBean.toString());

    }
}
