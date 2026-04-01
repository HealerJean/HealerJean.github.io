package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.generation.augmentation.QueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.expansion.QueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.join.ConcatenationDocumentJoiner;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BasicRagController {

    @Resource
    private  ChatClient chatClient;

    @Resource
    private VectorStore vectorStore;

    /**
     * 工厂
     */
    @Resource
    private ChatClient.Builder chatClientBuilder;


    /**
     * 基础RAG问答接口
     */
    @GetMapping("/rag/basic")
    public String basicRag(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/rag/filter")
    public String dynamicFilterRag(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                // 动态过滤：只查询 type = Spring 的文档
                .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == 'Spring'"))
                .call()
                .content();
    }


    @GetMapping("/rag/custom-template")
    public String customTemplateRag(@RequestParam String question) {
        // 自定义提示模板（核心规则：不知道就说不知道，只看上下文）
        PromptTemplate promptTemplate = new PromptTemplate( """
            问题：{query}
            
            上下文信息：
            ---------------------
            {question_answer_context}
            ---------------------
            
            规则：
            1. 只根据上下文回答，禁止编造
            2. 上下文没有答案，直接回复"我不知道"
            3. 语言必须用中文
            """);
        // 构建自定义模板的Advisor
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(promptTemplate)
                .build();

        return chatClient
                .prompt(question)
                .advisors(advisor)
                .call()
                .content();
    }


    @GetMapping("/rag/advanced")
    public String advancedRag(@RequestParam String question) {
        // 1. 文档检索器
        VectorStoreDocumentRetriever  retriever = VectorStoreDocumentRetriever.builder()
                // 从哪个向量库查
                .vectorStore(vectorStore)
                // 匹配度超过 50% 才返回
                .similarityThreshold(0.5)
                .build();

        // 2. 查询增强器：允许空上下文
        QueryAugmenter augmenter = ContextualQueryAugmenter.builder()
                // 允许：没查到资料也可以回答
                .allowEmptyContext(true)
                .build();

        // 3. 模块化RAG Advisor 把零件组装成高级 RAG
        var ragAdvisor = RetrievalAugmentationAdvisor.builder()
                // 装上检索器
                .documentRetriever(retriever)
                // 装上查询增强
                .queryAugmenter(augmenter)
                .build();

        return chatClient.prompt()
                .advisors(ragAdvisor)
                .user(question)
                .call().content();
    }



    // ========================================================================
    // 【预检索】1. 查询转换：压缩历史对话（把上下文+问题压缩成精准查询）
    // ========================================================================
    @GetMapping("/rag/transform/compress")
    public String compressHistory(@RequestParam String question) {
        // 1. 构建包含历史的查询对象
        // 注意：在实际Web应用中，这里通常从 Redis 或 Session 中获取历史
        Query queryWithHistory = Query.builder()
                .text(question) // 当前问题，例如 "它的第二大城市是？"
                .history(
                        new UserMessage("丹麦的首都是哪里？"),
                        new AssistantMessage("丹麦的首都是哥本哈根。")
                )
                .build();

        // 2. 创建压缩转换器
        // 关键点：建议设置低 Temperature (0.0) 以保证输出确定，不随机
        QueryTransformer compressor = CompressionQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder
                .defaultOptions(
                        ChatOptions.builder()
                                .temperature(0.0) // 确保重写精准
                                .maxTokens(2000)
                                .build()))
                .build();

        // 3. 执行转换 (通常这一步由 RAG Advisor 自动完成，这里演示手动过程)
        Query compressedQuery = compressor.transform(queryWithHistory);

        // 返回重写后的查询，供后续检索使用
        return "原始问题: " + question +
                "<br>重写后问题: " + compressedQuery.text();
    }



    // ========================================================================
    // 【预检索】2. 查询转换：查询重写（优化模糊问题）
    // ========================================================================
    @GetMapping("/rag/transform/rewrite")
    public String rewriteQuery(@RequestParam String question) {
        // 创建重写器
        QueryTransformer rewriter = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                // 可选：自定义提示词，告诉模型如何重写
                // .promptTemplate("你是一个搜索查询优化器。请将以下查询重写为适合搜索引擎的关键词...")
                .build();

        // 执行重写
        Query rewritten = rewriter.transform(new Query(question));
        System.out.println("原始: " + question + " -> 重写: " + rewritten.text()) ;
        return chatClient.prompt()
                .user(rewritten.text())
                .call()
                .content();
    }



    // ========================================================================
    // 【预检索】3. 查询转换：翻译查询（自动转成英文检索）
    // ========================================================================
    @GetMapping("/rag/transform/translate")
    public String translateQuery(@RequestParam String question) {
        Query query = new Query(question);
        TranslationQueryTransformer transformer = TranslationQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .targetLanguage("english") // 假设你的向量库是英文的
                .build();

        Query translated = transformer.transform(query);

        return chatClient.prompt()
                .user(translated.text())
                .call()
                .content();
    }


    // ========================================================================
    // 【预检索】4. 查询扩展：多查询扩展（生成3个相似问题，提高召回率）
    // ========================================================================
    /**
     * 演示：多查询扩展
     * 场景：用户问 "如何运行 Spring Boot 应用？"
     * 扩展为:
     * 1. Spring Boot 应用的编译步骤是什么？
     * 2. 如何配置 Spring Boot 的运行环境？
     * 3. Spring Boot 的启动命令是什么？
     */
    @GetMapping("/rag/expand/multi-query")
    public String multiQueryExpand(@RequestParam String question) {
        // 创建扩展器，生成3个变体，并包含原始查询
        MultiQueryExpander expander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(3)
                .includeOriginal(true) //// 是否包含原始问题
                .build();

        // 扩展查询
        List<Query> queryList = expander.expand(new Query(question));

        // 把扩展后的问题拼起来
        String allQuestions = queryList.stream()
                .map(Query::text)
                .reduce("", (a, b) -> a + "\n" + b);

        return chatClient.prompt()
                .user(allQuestions)
                .call()
                .content();
    }


    // ========================================================================
    // 【检索】1. 向量库检索（带过滤 + 阈值 + topK）
    // ========================================================================
    @GetMapping("/rag/retrieve/basic")
    public String retrieveDocs(@RequestParam String question) {
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.5)
                .topK(3)
                .filterExpression(new FilterExpressionBuilder()
                        .eq("type", "Spring")
                        .build())
                .build();

        List<Document> docs = retriever.retrieve(new Query(question));
        return docs.toString();
    }


    // ========================================================================
    // 【检索】3. 文档合并（多个查询结果 → 合并去重）
    // ========================================================================
    @GetMapping("/rag/retrieve/join")
    public String joinDocuments(@RequestParam String question) {

        // 1. 假设我们有两个检索器 (例如: 一个查内部库，一个查外部库)
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .build();

        // 2. 分别检索 (伪代码)
        List<Document> docs1 = retriever.retrieve(new Query(question));
        List<Document> docs2 = retriever.retrieve(new Query(question + " 用途"));


        // 3. 准备数据结构 Map<Query, List<List<Document>>>
        Map<Query, List<List<Document>>> map = Map.of(
                new Query(question), List.of(docs1),
                new Query(question + " 用途"), List.of(docs2)
        );

        // 4. 执行合并
        ConcatenationDocumentJoiner joiner = new ConcatenationDocumentJoiner();
        List<Document> finalDocs = joiner.join(map);

        return finalDocs.toString();
    }


    // ========================================================================
    // 【生成】查询增强（上下文拼接 + 允许空上下文）
    // ========================================================================
    @GetMapping("/rag/augment/context")
    public String contextualAugment(@RequestParam String question) {
        // 构建检索器 (假设这个查询在库里找不到)
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.99) // 设置极高的阈值，强制找不到
                .build();

        // 核心配置点：
        // allowEmptyContext(false): 检索不到就不回答 (默认严格模式)
        // allowEmptyContext(true): 检索不到也让模型基于自身知识回答
        ContextualQueryAugmenter augmenter = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)
                .build();

        // 高级 RAG
        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .queryAugmenter(augmenter)
                .build();

        return chatClient.prompt()
                .advisors(advisor)
                .user(question)
                .call()
                .content();
    }

    // ========================================================================
    // 【终极完整版】全模块化 RAG：
    // 查询重写 → 多查询扩展 → 检索 → 文档合并 → 增强 → 生成
    // ========================================================================
    @GetMapping("/rag/full-module")
    public String fullModuleRag(@RequestParam String question) {
        // 1. 查询重写（优化问题）
        QueryTransformer queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();

        // 2. 多查询扩展（自动生成多个语义查询）
        QueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                .numberOfQueries(2)
                .build();

        // 3. 向量检索（设置相似度阈值）
        VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .similarityThreshold(0.5)
                .build();

        // 4. 文档合并（多查询结果自动合并去重）
        ConcatenationDocumentJoiner joiner = new ConcatenationDocumentJoiner();

        // 5. 查询增强（把上下文拼回用户问题）
        ContextualQueryAugmenter augmenter = ContextualQueryAugmenter.builder()
                .allowEmptyContext(true)
                .build();

        // ===================== ✅ 核心：你要的流水线装配 =====================
        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(queryTransformer)    // 查询重写
                .queryExpander(queryExpander)            // 多查询扩展
                .documentRetriever(retriever)            // 检索
                .documentJoiner(joiner)                  // 合并
                .queryAugmenter(augmenter)               // 上下文增强
                .build();

        // 执行
        return chatClient.prompt()
                .advisors(advisor)
                .user(question)
                .call()
                .content();
    }
}