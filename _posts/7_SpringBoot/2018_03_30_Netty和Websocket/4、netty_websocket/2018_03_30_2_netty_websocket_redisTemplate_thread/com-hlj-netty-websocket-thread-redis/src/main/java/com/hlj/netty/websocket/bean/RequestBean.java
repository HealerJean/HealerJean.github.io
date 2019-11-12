package com.hlj.netty.websocket.bean;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Promise;

/**
 * Created by j.sh on 27/11/2017.
 */
public class RequestBean {

    private String request;
    private Channel channel;
    private Promise<String> promise;

    public RequestBean(String request, Channel channel, Promise<String> promise) {
        this.request = request;
        this.channel = channel;
        this.promise = promise;
    }

    public String getRequest() {
        return request;
    }

    public Channel getChannel() {
        return channel;
    }

    public Promise<String> getPromise() {
        return promise;
    }
}
