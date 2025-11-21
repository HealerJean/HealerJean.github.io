package com.healerjean.proj.strata.infra.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * ChatClientConfig
 *
 * @author zhangyujin
 * @date 2025/11/18
 */

@Configuration
public class ChatClientConfig {

    private final ToolCallbackProvider toolCallbackProvider;

    public ChatClientConfig(@Lazy ToolCallbackProvider toolCallbackProvider) {
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("你是一个图书管理助手，可以帮助用户查询图书信息。" +
                        "你可以根据书名模糊查询、根据作者查询和根据分类查询图书。" +
                        "回复时，请使用简洁友好的语言，并将图书信息整理为易读的格式。")
                .defaultTools(toolCallbackProvider)
                .build();
    }


}
