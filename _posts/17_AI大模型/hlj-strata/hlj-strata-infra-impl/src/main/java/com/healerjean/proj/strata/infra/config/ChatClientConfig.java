package com.healerjean.proj.strata.infra.config;

import com.healerjean.proj.strata.infra.chat.advisor.LoggingAdvisor;
import io.modelcontextprotocol.client.McpClient;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ChatClientConfig
 *
 * @author zhangyujin
 * @date 2025/11/18
 */

@Configuration
public class ChatClientConfig {


    @Resource
    private LoggingAdvisor loggingAdvisor;

    @Resource
    private VectorStore vectorStore;

    /**
     * 配置 Ollama ChatClient Bean
     */
    @Bean
    public ChatClient ollamaChatClient(OllamaChatModel ollamaChatModel) {
        // 1. 构建RAG Advisor：阈值0.7，返回前2条结果
        QuestionAnswerAdvisor qaAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.3)  // 相似度阈值
                        .topK(2)                   // 最多返回2条
                        .build())
                .build();

        return ChatClient.builder(ollamaChatModel)
                // 加入自动循环
                .defaultAdviors(loggingAdvisor, qaAdvisor)
                // 全局系统提示（角色：专业Java技术助手）
                .defaultSystem("""
                        你是专业的Java后端技术助手，精通Spring Boot、Spring AI、Ollama技术栈。
                        回答要求：简洁、精准、可运行，提供完整代码案例，避免冗余解释。
                        """)
                // 全局默认参数（可覆盖）
                .defaultOptions(ollamaChatModel.getDefaultOptions())
                .build();
    }


}
