package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Llama3Controller
 *
 * @author zhangyujin
 * @date 2025/11/18
 */
@RestController("/ai")
public class OllamaChatController {

    @Resource
    private OllamaChatModel ollamaChatModel;

    @Resource
    private ChatClient chatClient;

    /**
     * 普通对话
     *
     * @param message 用户指令
     * @return
     */
    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        message = "请使用中文简体回答：" + message;
        Prompt prompt = new Prompt(new UserMessage(message));
        ChatResponse chatResponse = ollamaChatModel.call(prompt);
        return chatResponse.getResult().getOutput().getText();
    }

    /**
     * 普通对话
     *
     * @param message 用户指令
     */
    @GetMapping("/mcp")
    public String mcp(@RequestParam("message") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }



}