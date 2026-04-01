package com.healerjean.proj.strata.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * StreamChatController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class StreamChatController {

    private final ChatClient chatClient;

    /**
     * 流式聊天接口（SSE 实时响应）
     */
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE )
    public Flux<String> streamChat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .stream() // 流式调用
                .content(); // 逐字返回文本流
    }


    /**
     * 流式+自定义参数（临时调整温度、模型）
     */
    @GetMapping(value = "/stream/options", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChatWithOptions(
            @RequestParam String message,
            @RequestParam(defaultValue = "1.0") Double temperature) {
        return chatClient.prompt()
                .user(message)
                // 临时参数：温度+最大token
                .options(ChatOptions.builder()
                        .temperature(temperature)
                        .maxTokens(2000)
                        .build())
                .stream()
                .content();
    }
}