package com.healerjean.proj.strata.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * BasicChatController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@RestController
@RequestMapping("/ai/chat")
@RequiredArgsConstructor
public class BasicChatController {

    // 注入配置好的 ChatClient
    private final ChatClient chatClient;

    /**
     * 基础同步聊天接口
     */
    @GetMapping("/sync")
    public String syncChat(@RequestParam String message) {
        return chatClient.prompt()
                // 用户提问
                .user(message)
                // 同步调用
                .call()
                // 获取响应文本
                .content();
    }

    /**
     * 带角色设定的聊天（覆盖全局系统提示）
     */
    @GetMapping("/sync/role")
    public String syncChatWithRole(@RequestParam String message) {
        return chatClient.prompt()
                // 临时角色
                .system("你是幽默的段子手，用搞笑风格回答问题")
                .user(message)
                .call()
                .content();
    }
}