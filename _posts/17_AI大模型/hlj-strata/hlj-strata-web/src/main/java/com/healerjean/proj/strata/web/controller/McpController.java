package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * McpController
 *
 * @author zhangyujin
 * @date 2026/3/31
 */
@RestController
public class McpController {

    @Resource
    private ChatClient chatClient;


    /**
     * 基础对话
     */
    @GetMapping("mcp/chat")
    public String chat(@RequestParam(value = "message") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
