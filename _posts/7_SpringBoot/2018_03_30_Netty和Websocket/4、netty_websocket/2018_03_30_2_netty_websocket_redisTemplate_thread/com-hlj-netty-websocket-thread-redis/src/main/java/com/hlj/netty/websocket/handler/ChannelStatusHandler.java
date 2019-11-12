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

    @Override
    public void channelRegistered(ChannelHandlerContext channelHandlerContext) throws Exception {
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext channelHandlerContext) throws Exception {
        ChannelSelector.removeChannel(channelHandlerContext.channel());
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        ChannelSelector.addChannel(channelHandlerContext.channel());
    }

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
