package com.hlj.netty.websocket.topic;


import com.hlj.netty.websocket.bean.ConvertBean;
import com.hlj.netty.websocket.redis.cacheSerializer.CustomJSONStringRedisSerializer;
import com.hlj.netty.websocket.redis.cacheSerializer.CustomStringRedisSerializer;
import com.hlj.netty.websocket.selector.ChannelSelector;
import com.hlj.netty.websocket.selector.ClientChannelRelation;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
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
        String channelId = ClientChannelRelation.getChannelId(convertBean.getToUid());

        if (channelId != null) {
            Channel clientChannel = ChannelSelector.getChannel(channelId);
            if (clientChannel != null) {
                clientChannel.writeAndFlush(new TextWebSocketFrame(convertBean.getContent()));
            }
        }
    }
}
