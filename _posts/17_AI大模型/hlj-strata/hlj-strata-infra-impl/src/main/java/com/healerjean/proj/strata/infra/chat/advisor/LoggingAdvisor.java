package com.healerjean.proj.strata.infra.chat.advisor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * ：日志监控 Advisor（同步 + 流式）：同时实现CallAdvisor+StreamAdvisor，支持双模式
 */
@Component
public class LoggingAdvisor implements CallAdvisor, StreamAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAdvisor.class);

    @Override
    public String getName() {
        return "LoggingAdvisor";
    }

    /**
     * 低优先级，最后处理请求、最先处理响应
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    /**
     * 同步拦截实现
     */
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {
        long start = System.currentTimeMillis();
        logger.info("[同步请求] {}", request.prompt().getUserMessage().getText());

        // 执行下一个Advisor/LLM调用
        ChatClientResponse response = chain.nextCall(request);

        long cost = System.currentTimeMillis() - start;
        logger.info("[同步响应] {} | 耗时:{}ms", response.chatResponse().getResults().get(0), cost);
        return response;
    }

    // 流式拦截实现
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {
        long start = System.currentTimeMillis();
        logger.info("[流式请求] {}", request.prompt().getUserMessage().getText());

        return chain.nextStream(request)
                .doOnNext(response -> {
                    String content = response.chatResponse().getResults().get(0).getOutput().getText();
                    logger.info("[流式响应片段] {}", content);
                })
                .doFinally(signalType -> {
                    long cost = System.currentTimeMillis() - start;
                    logger.info("[流式结束] 耗时:{}ms", cost);
                });
    }
}