package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ConversationController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@RestController
@RequestMapping("/ai/chat")
public class ConversationController {

    @Resource
    private  ChatClient chatClient;

    /**
     * 会话存储（生产环境用 Redis）
     */
    private final Map<String, List<Message>> sessionStore = new ConcurrentHashMap<>();


    /**
     * 多轮对话接口
     */
    @GetMapping("/conversation")
    public String conversationChat(
            @RequestParam String sessionId,
            @RequestParam String message) {
        // 获取或创建会话历史
        List<Message> messages = sessionStore.getOrDefault(sessionId, new ArrayList<>());

        // 添加当前用户消息
        messages.add(new UserMessage(message));

        // 调用 AI（传入完整会话历史）
        String response = chatClient.prompt()
                .messages(messages)
                .call()
                .content();

        // 保存 AI 响应到会话历史
        messages.add(new AssistantMessage(response));
        sessionStore.put(sessionId, messages);

        return response;
    }


}
