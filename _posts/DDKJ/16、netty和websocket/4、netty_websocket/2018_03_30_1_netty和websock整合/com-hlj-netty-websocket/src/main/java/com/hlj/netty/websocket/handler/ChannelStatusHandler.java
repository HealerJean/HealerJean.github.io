package com.hlj.netty.websocket.handler;

import com.hlj.netty.websocket.selector.ChannelSelector;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by j.sh on 27/11/2017.
 */
public class ChannelStatusHandler implements ChannelInboundHandler {

    private Logger logger = LoggerFactory.getLogger(ChannelStatusHandler.class);


    @Override/**/
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
    }
    //Channel已创建，还未注册到一个EventLoop上
    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        ChannelSelector.removeChannel(channelHandlerContext.channel());
    }
    //Channel是活跃状态（连接到某个远端），可以收发数据
    //通道选择器中添加通道
    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        ChannelSelector.addChannel(channelHandlerContext.channel());
    }
    //Channel未连接到远端
    //通道选择器中移除通道
    @Override
    public void channelInactive(ChannelHandlerContext channelHandlerContext) throws Exception {
        ChannelSelector.removeChannel(channelHandlerContext.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {
    }

    @Override
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext channelHandlerContext) throws Exception {
    }
}
