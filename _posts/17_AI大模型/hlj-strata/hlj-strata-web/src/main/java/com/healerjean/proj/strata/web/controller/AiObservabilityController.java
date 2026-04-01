package com.healerjean.proj.strata.web.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai/observability")
public class AiObservabilityController {

    private final ChatClient chatClient;
    private final EmbeddingModel embeddingModel;
    private final VectorStore vectorStore;

    public AiObservabilityController(ChatClient.Builder chatClientBuilder,
                                     EmbeddingModel embeddingModel,
                                     VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }


    /**
     * 场景1：基础聊天观测 (ChatClient & ChatModel)
     * 演示如何获取 OpenAI 调用的详细指标
     */
    @GetMapping("/chat")
    public ChatResponse chat(@RequestParam String message) {
        // 1. 调用 ChatClient
        // 根据配置，如果 log-prompt=true，这里会记录 Prompt
        ChatResponse response = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();
        return response;
    }


}