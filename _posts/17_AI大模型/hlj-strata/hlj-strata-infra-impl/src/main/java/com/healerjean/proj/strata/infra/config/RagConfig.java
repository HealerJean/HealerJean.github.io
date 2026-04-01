package com.healerjean.proj.strata.infra.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
public class RagConfig {

    /**
     * 初始化向量库（内存版）
     */
    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }

    /**
     * 加载私有文档到向量库（模拟业务数据）
     */
    @Bean
    public List<Document> loadDocuments(VectorStore vectorStore) {
        // 模拟3条私有业务文档
        String doc1 = "Spring AI 是Spring官方推出的AI开发框架，支持RAG、智能对话、函数调用。";
        String doc2 = "Spring Boot 是快速开发Spring应用的框架，自动配置、内嵌服务器。";
        String doc3 = "Java 是跨平台编程语言，广泛用于企业级开发。";

        // 构建文档（带元数据，用于动态过滤）
        Document document1 = Document.builder()
                .text(doc1)
                .metadata(Map.of("type", "Spring", "author", "Spring官方"))
                .build();
        Document document2 = Document.builder()
                .text(doc2)
                .metadata(Map.of("type", "Spring", "author", "Spring官方"))
                .build();
        Document document3 = Document.builder()
                .text(doc3)
                .metadata(Map.of("type", "Java", "author", "Oracle"))
                .build();

        List<Document> documents = List.of(document1, document2, document3);
        // 将文档存入向量库
        vectorStore.add(documents);
        return documents;
    }
}