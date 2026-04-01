package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * VectorStoreController
 *
 * @author zhangyujin
 * @date 2026/4/1
 */
@RestController
@RequestMapping("/vector")
public class VectorStoreController {

    // 注入向量库 + AI模型（用于关键词/摘要）
    @Autowired
    private VectorStore vectorStore;

    @Resource
    private ChatClient chatClient;


    // ========================================================================
    // 1. 写入文档（add）
    // 请求：GET http://localhost:8080/vector/add
    // 入参：content、元数据
    // ========================================================================
    @GetMapping("/add")
    public String add(
            @RequestParam String content,
            @RequestParam String country,
            @RequestParam int year) {
        Document doc = new Document(content, Map.of("country", country, "year", year));
        vectorStore.add(List.of(doc));

        return "✅ 写入向量库成功！";
    }


    // ========================================================================
    // 2. 简单相似搜索（topK=4）
    // 请求：GET http://localhost:8080/vector/search
    // ========================================================================
    @GetMapping("/search")
    public List<Document> search(@RequestParam String query) {
        return vectorStore.similaritySearch(query);
    }


    // ========================================================================
    // 3. 高级搜索（指定topK + 相似度阈值）
    // 请求：GET http://localhost:8080/vector/search-advanced
    // ========================================================================
    @GetMapping("/search-advanced")
    public List<Document> searchAdvanced(
            @RequestParam String query,
            @RequestParam(defaultValue = "5") int topK,
            @RequestParam(defaultValue = "0.6") double threshold
    ) {
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(threshold)
                .build();
        return vectorStore.similaritySearch(request);
    }


    // ========================================================================
    // 4. 带元数据过滤搜索（类似 WHERE）
    // 请求：GET http://localhost:8080/vector/search-filter
    // ========================================================================
    @GetMapping("/search-filter")
    public List<Document> searchWithFilter(
            @RequestParam String query,
            @RequestParam String country
    ) {
        String filter = "country == '" + country + "' && year >= 2020";
        SearchRequest request = SearchRequest.builder()
                .query(query)
                .filterExpression(filter)
                .build();
        return vectorStore.similaritySearch(request);
    }

    // ========================================================================
    // 5. 按 ID 删除
    // 请求：GET http://localhost:8080/vector/delete
    // ========================================================================
    @GetMapping("/delete")
    public String delete(@RequestParam String id) {
        vectorStore.delete(List.of(id));
        return "✅ 删除成功：" + id;
    }

    // ========================================================================
    // 6. 按过滤条件删除（WHERE）
    // 请求：GET http://localhost:8080/vector/delete-by-filter
    // ========================================================================
    @GetMapping("/delete-by-filter")
    public String deleteByFilter(@RequestParam String country) {
        String filter = "country == '" + country + "'";
        vectorStore.delete(filter);
        return "✅ 按条件删除：" + filter;
    }


    // ========================================================================
    // 7. 批量写入（自动分块，解决token超限）
    // 请求：GET http://localhost:8080/vector/batch-add
    // ========================================================================
    @GetMapping("/batch-add")
    public String batchAdd() {
        List<Document> docs = List.of(
                new Document("北京是中国首都", Map.of("country", "China", "year", 2025)),
                new Document("华盛顿是美国首都", Map.of("country", "USA", "year", 2025)),
                new Document("伦敦是英国首都", Map.of("country", "UK", "year", 2025))
        );
        vectorStore.add(docs); // 自动使用 BatchingStrategy
        return "✅ 批量写入完成！";
    }

    // ========================================================================
    // 8. 完整 RAG 流程（检索 → AI回答）
    // 请求：GET http://localhost:8080/vector/rag
    // ========================================================================

    @GetMapping("/rag")
    public String rag(@RequestParam String question) {
        // 1. 检索
        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(3)
                        .similarityThreshold(0.6)
                        .build()
        );

        String context = docs.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);

        // 2. AI 生成
        return chatClient.prompt()
                .system("基于上下文回答：\n" + context)
                .user(question)
                .call()
                .content();
    }


}
