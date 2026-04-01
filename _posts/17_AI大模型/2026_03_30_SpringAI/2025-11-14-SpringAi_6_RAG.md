---
title: SpringAi_6_RAG
date: 2025-11-14 00:00:00
tags: 
- AI
category: 
- AI
description: SpringAi_6_RAG
---

**前言**     

 Github：[https://github.com/HealerJean](https://github.com/HealerJean)         

 博客：[http://blog.healerjean.com](http://HealerJean.github.io)          



# 一、核心概念

> `RAG`（`Retrieval-Augmented Generation`，检索增强生成）是一种结合了**信息检索**（Information Retrieval）与**生成式语言模型**（`Generative Language Model`）的技术框架，旨在提升大语言模型在回答问题时的**准确性、时效性和可解释性**。

## 1、`RAG` 的基本原理

> 在生成答案之前，先从外部知识源（如文档库、数据库、网页等）中检索出与用户问题相关的信息，然后将这些信息作为上下文输入给生成模型，从而生成更准确、可靠的回答。

- RAG = **先从向量库检索相关私有数据 → 把数据作为上下文给大模型 → 大模型基于真实数据回答**

- **检索阶段**
  - 用户输入一个问题（query）。
  - 使用检索模型（如 BM25、DPR、ColBERT 或向量数据库中的嵌入检索）从大规模文档集合中找出最相关的若干文档片段
- **生成阶段**
  - 将原始问题 + 检索到的相关文档作为提示（`prompt`）输入给大语言模型（如 `LLaMA`、`GPT`、`Qwen` 等）。
  - 模型基于这些上下文生成最终答案。



## 2、`RAG` 的优势

- **减少幻觉**（`Hallucination`）：模型依赖真实检索到的证据，而非仅靠内部参数记忆。
- **知识可更新**：只需更新外部知识库，无需重新训练模型。
- **可解释性强**：可以展示引用来源（“根据以下文档……”）。
- **适用于专业领域**：如医疗、法律、金融等需要高准确性的场景。



## 3、典型应用场景

- 企业知识库问答系统（如客服、内部文档查询）
- 学术研究助手（基于论文库回答问题）
- 实时新闻或政策问答（结合最新数据）
- 智能客服/虚拟助手（结合产品手册）



## 4、`Spring` `AI` `RAG` 核心架构）

| 类型         | 优点                               | 适合             |
| ------------ | ---------------------------------- | ---------------- |
| **普通 RAG** | 简单、几行代码                     | 测试、小项目     |
| **高级 RAG** | **可自由组装、可扩展、生产级稳定** | 正式项目、企业级 |

- **简易版**：`Advisor API`（开箱即用，几行代码实现 `RAG`）
  - **`QuestionAnswerAdvisor`**：最简单的 `RAG`，直接检索并拼接上下文。
  - **`VectorStoreChatMemoryAdvisor`**：结合了向量数据库的记忆功能。

- **高级版**：模块化架构（像搭乐高一样自定义 RAG 流程）
  - 使用 `RetrievalAugmentationAdvisor`，你可以自由组合上述的 `QueryTransformer`、`DocumentRetriever` 等组件，构建高级 `RAG` 流程。

```xml
<!-- 基础RAG Advisor（QuestionAnswerAdvisor） -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-advisors-vector-store</artifactId>
</dependency>

<!-- 高级模块化RAG（RetrievalAugmentationAdvisor） -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-rag</artifactId>
</dependency>
```



### 1）`QuestionAnswerAdvisor`

**作用**：自动完成「检索 + 拼接上下文 + 提问」，是**入门首选**。

工作流程：用户提问 → `Advisor` 检索向量库 → 把检索结果拼到问题里 → 发给大模型回答

**核心配置**：

- 相似度阈值（`similarityThreshold`）：只返回匹配度高于阈值的文档
- 结果数量（`topK`）：返回最相关的 N 条文档
- 过滤表达式：按文档元数据过滤（如类型、分类）



### 2）`RetrievalAugmentationAdvisor`

**作用**：乐高式组装 RAG 全流程，支持**查询重写、多查询扩展、文档压缩、重新排序**等高级功能。

支持两种模式：

- **朴素 RAG**：基础检索 + 生成
- **高级 RAG**：查询优化 + 检索 + 后处理 + 生成

| 模块       | 作用                             | 核心组件                     |
| ---------- | -------------------------------- | ---------------------------- |
| **预检索** | 优化用户问题，提高检索准确率     | 查询重写、翻译、多查询扩展   |
| **检索**   | 从向量库获取相关文档             | VectorStoreDocumentRetriever |
| **后检索** | 清洗检索结果（去重、排序、压缩） | 文档后处理器                 |
| **生成**   | 用上下文增强问题，让大模型回答   | ContextualQueryAugmenter     |



# 二、实战案例

```java
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
```

## 1、案例1：基础 `RAG`（`QuestionAnswerAdvisor`）

```java
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

http://localhost:8080/rag/basic?question=Spring AI是什么
```



## 2、案例2：动态过滤 `RAG`（只查指定类型文档）

```java
@GetMapping("/rag/filter")
public String dynamicFilterRag(@RequestParam String question) {
    return chatClient.prompt()
            .user(question)
            // 动态过滤：只查询 type = Spring 的文档
            .advisors(a -> a.param(QuestionAnswerAdvisor.FILTER_EXPRESSION, "type == 'Spring'"))
            .call()
            .content();
}



GET http://localhost:8081/rag/filter?question=Spring AI是什么
Spring AI 是 Spring 官方推出的 AI 开发框架，支持 RAG（检索增强生成）、智能对话、函数调用等功能。
```



## 3、 案例 3：自定义提示模板（严格约束大模型）

>  模板里必须有的两个固定占位符

- `{query}` → 用户问的问题
- `{question_answer_context}` → 从你的私有文档里检索到的参考内容

- 执行流程
  - **去向量库查**和用户问题相关的文档
  - **把查到的文档填进 `{question_answer_context}`**
  - **把用户问题填进 `{query}`**
  - 把完整提示发给 AI，让 AI 按规则回答

```java
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
                                                       
                                                       
                                                       
GET http://localhost:8081/rag/custom-template?question=Java 是什么
Java 是跨平台编程语言，广泛用于企业级开发。   
                                                       
                                                       
GET http://localhost:8081/rag/custom-template?question=Python是什么
我不知道。                                                       
```



# 三、高阶`RAG`

## 1、【预检索】压缩历史对话 `CompressionQueryTransformer`

`CompressionQueryTransformer` ：**把上下文+问题压缩成精准查询**

- 使用大型语言模型将对话历史记录和后续查询压缩成一个独立的查询，该查询捕获了对话的精髓。

- 当对话历史记录很长且后续查询与对话上下文相关时，此转换器非常有用。

```java
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
```



## 2、【预检索】查询重写`RewriteQueryTransformer`

`RewriteQueryTransformer` ：**优化模糊问题**

- **场景：当用户问题很口语化或模糊时，利用 LLM 将其改写为更专业的检索关键词。**

- 使用大型语言模型重写用户查询，以便在查询目标系统（例如向量存储或网络搜索引擎）时提供更好的结果。

- 当用户查询冗长、模糊或包含可能影响搜索结果质量的不相关信息时，此转换器非常有用。

```java
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
```

## 3、【预检索】语言翻译 `TranslationQueryTransformer`

`TranslationQueryTransformer` ：解决用户提问语言与知识库语言不一致的问题。

- 使用大型语言模型将查询 翻译成嵌入模型用于生成文档嵌入的目标语言。
  - 如果查询已经使用目标语言，则原样返回。如果查询的语言未知，也原样返回。

- 当嵌入模型使用特定语言进行训练而用户查询使用不同语言时，此转换器非常有用。

```java
// ========================================================================
// 【预检索】3. 查询转换：翻译查询（自动转成英文检索）
// ========================================================================
@GetMapping("/rag/transform/translate")
public String translateQuery(@RequestParam String question) {
    Query query = new Query(question);
    TranslationQueryTransformer transformer = TranslationQueryTransformer.builder()
            .chatClientBuilder(chatClientBuilder)
            .targetLanguage("english")
            .build();

    Query translated = transformer.transform(query);

    return chatClient.prompt()
            .user(translated.text())
            .call()
            .content();
}
```





## 4、【预检索】多查询扩展 `MultiQueryExpander`

`MultiQueryExpander` ：将一个复杂问题拆解为多个子问题，分别去检索，从而提高召回率。

- 使用大型语言模型将查询扩展为多个语义多样化的变体，以捕捉不同的视角
- 这对于检索额外的上下文信息和增加找到相关结果的机会非常有用。

```java
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
```





## 5、【检索】向量库检索 `VectorStoreDocumentRetriever`

`VectorStoreDocumentRetriever` 

- 从向量存储中检索与输入查询语义相似的文档。它支持基于元数据、相似性阈值和 top-k 结果进行过滤。
- 过滤表达式可以是静态的或动态的。对于动态过滤表达式，您可以传递一个 `Supplier`。

```java
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
```



## 6、【检索】运行时过滤 `VectorStoreDocumentRetriever` 

`VectorStoreDocumentRetriever` 

- 从向量存储中检索与输入查询语义相似的文档。它支持基于元数据、相似性阈值和 top-k 结果进行过滤。
- 过滤表达式可以是静态的或动态的。对于动态过滤表达式，您可以传递一个 `Supplier`。

```java
// ========================================================================
// 【检索】2. 动态过滤检索（运行时过滤）
// ========================================================================
@GetMapping("/rag/retrieve/dynamic-filter")
public String dynamicFilterRetrieve(@RequestParam String question) {
    VectorStoreDocumentRetriever retriever = VectorStoreDocumentRetriever.builder()
            .vectorStore(vectorStore)
            .build();

    Query query = Query.builder()
            .text(question)
            .context(Map.of(VectorStoreDocumentRetriever.FILTER_EXPRESSION, "author == 'Spring官方'"))
            .build();

    List<Document> docs = retriever.retrieve(query);
    return docs.toString();
}

```



## 7、【检索】文档合并 `ConcatenationDocumentJoiner`

**`ConcatenationDocumentJoiner` ：多个查询结果 → 合并去重**

- 通过将基于多个查询和来自多个数据源检索到的文档连接成一个文档集合来合并它们。
- 如果存在重复文档，则保留第一个出现的文档。每个文档的得分保持不变。

```java
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
```



## 8、【后检索与生成】空上下文处理 `ContextualQueryAugmenter`

**`ContextualQueryAugmenter`  ：上下文拼接**

- 使用提供文档内容中的上下文数据增强用户查询。

- 默认情况下，`ContextualQueryAugmenter` 不允许检索到的上下文为空。当这种情况发生时，它会指示模型不回答用户查询。

```java
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
```



## 9、【终极完整版】全模块化 RAG：

```java
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
```



# 四、`ETL`

## 1、核心概念

### 1）`Spring` `AI` `ETL` 是什么

**ETL = 把本地文件（PDF/Word/MD/JSON）变成向量库可检索的文档块**

流程固定三步：

1. **Extract 提取**：用 DocumentReader 读取文件 → 生成 Document
2. **Transform 转换**：切分文本、加关键词、加摘要 → 处理文档
3. **Load 加载**：用 DocumentWriter 写入向量库 → 供 RAG 检索



### 2）三个重要组件

| 阶段        | 核心接口              | 常用实现类                                                   | 作用                                        |
| ----------- | --------------------- | ------------------------------------------------------------ | ------------------------------------------- |
| `Extract`   | `DocumentReader`      | `JsonReader`, `JsoupDocumentReader`, `ParagraphPdfDocumentReader` | 将原始文件读取为 `List<Document>`           |
| `Transform` | `DocumentTransformer` | `TokenTextSplitter`, `KeywordMetadataEnricher`               | 切分长文本、清洗数据、利用 `LLM` 增强元数据 |
| `Load`      | `DocumentWriter`      | `VectorStore` (accept方法), `FileDocumentWriter`             | 将数据存入向量数据库或本地文件              |



#### a、`DocumentReader` 

> 提供来自不同来源的文档。

```java
public interface DocumentReader extends Supplier<List<Document>> {

    default List<Document> read() {
		return get();
	}
}
```



#### b、`DocumentTransformer`

> 作为处理工作流的一部分，转换一批文档。

```java
public interface DocumentTransformer extends Function<List<Document>, List<Document>> {

    default List<Document> transform(List<Document> transform) {
		return apply(transform);
	}
}
```

#### c、`DocumentWriter` 

> 管理 ETL 过程的最后阶段，准备文档以进行存储。

```java
public interface DocumentWriter extends Consumer<List<Document>> {

    default void write(List<Document> documents) {
		accept(documents);
	}
}
```



## 2、`DocumentReaders`

### 1）`JSON` -> `JsonReader` 

> `JsonReader` 处理 JSON 文档，将其转换为 `Document` 对象的列表。

```java
/**
 * 读取 JSON 文件
 */
@GetMapping("/read/json")
public List<Document> readJson(@Value("classpath:document/data.json") Resource resource) {
    // 提取 content 和 description 字段作为内容
    JsonReader jsonReader = new JsonReader(resource, "content", "description");
    return jsonReader.read();
}



GET http://localhost:8081/etl/read/json
[
  {
    "id": "f12b635e-9d64-4b75-9e88-79329a5fbc66",
    "media": null,
    "metadata": {},
    "score": null,
    "text": "{date=26日星期一, sunrise=06:11, high=高温 21.0℃, low=低温 8.0℃, sunset=18:31, aqi=97, fx=西南风, fl=<3级, type=多云, notice=阴晴之间，谨防紫外线侵扰}"
  }
]
```



### 2）文本 -> `TextReader` 

> `TextReader` 处理纯文本文档，将其转换为 `Document` 对象的列表。

```java
/**
 * 读取 TXT 文本文件
 */
@GetMapping("/read/txt")
public List<Document> readTxt(@Value("classpath:document/data.txt") Resource resource) {
    TextReader textReader = new TextReader(resource);
    textReader.getCustomMetadata().put("type", "text"); // 加元数据
    return textReader.read();
}



[
  {
    "id": "1bd389be-aa6e-4190-b9bd-8e74ba83ccbe",
    "media": null,
    "metadata": {
      "type": "text",
      "charset": "UTF-8",
      "source": "data.txt"
    },
    "score": null,
    "text": "{\n  \"date\": \"26日星期一\",\n  \"sunrise\": \"06:11\",\n  \"high\": \"高温 21.0℃\",\n  \"low\": \"低温 8.0℃\",\n  \"sunset\": \"18:31\",\n  \"aqi\": 97,\n  \"fx\": \"西南风\",\n  \"fl\": \"<3级\",\n  \"type\": \"多云\",\n  \"notice\": \"阴晴之间，谨防紫外线侵扰\"\n}"
  }
]
```

### 3）`Markdown` -> `MarkdownDocumentReader`

> `MarkdownDocumentReader` 处理 Markdown 文档，将其转换为 `Document` 对象的列表。

```java
/**
 * 读取 Markdown
 */
@GetMapping("/read/markdown")
public List<Document> readMarkdown(@Value("classpath:document/code.md") Resource resource) {
    MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
            .withHorizontalRuleCreateDocument(true) // 按 --- 分割
            .withIncludeCodeBlock(true) // 包含代码块
            .build();

    MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
    return reader.read();
}

GET http://localhost:8081/etl/read/markdown
[
  {
    "id": "2d6269b4-d4e2-4193-b557-f84420daa775",
    "media": null,
    "metadata": {
      "category": "code_block",
      "lang": "java"
    },
    "score": null,
    "text": "This is a Java sample application: package com.example.demo;\n\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n\n@SpringBootApplication\npublic class DemoApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(DemoApplication.class, args);\n    }\n}\n"
  },
  {
    "id": "769b3f36-75d3-49cd-a851-b65a523fcf51",
    "media": null,
    "metadata": {
      "category": "code_inline"
    },
    "score": null,
    "text": "Markdown also provides the possibility to use inline code formatting throughout the entire sentence."
  },
  {
    "id": "230a9bc5-466c-4c21-b004-f25a857d7035",
    "media": null,
    "metadata": {
      "category": "code_block",
      "lang": ""
    },
    "score": null,
    "text": "Another possibility is to set block code without specific highlighting: ./mvnw spring-javaformat:apply\n"
  }
]

```





### 4）`HTML`  -> `JsoupDocumentReader` 处

> `JsoupDocumentReader` 处理 HTML 文档，使用 JSoup 库将其转换为 `Document` 对象的列表。

```java
/**
 * 读取 html 文件
 */
@GetMapping("/html")
public List<Document> readHtml(@Value("classpath:document/my-page.html") Resource resource) {
    // 配置读取器：只提取 article 标签下的段落，包含链接 URL，指定编码
    JsoupDocumentReaderConfig config = JsoupDocumentReaderConfig.builder()
            .selector("article p")           // CSS 选择器
            .charset("UTF-8")                // 字符集
            .includeLinkUrls(true)           // 是否在元数据中包含链接
            .metadataTags(List.of("author", "date")) // 提取特定的 meta 标签
            .additionalMetadata("source", "web-scrape-1") // 添加自定义元数据
            .build();
    JsoupDocumentReader reader = new JsoupDocumentReader(resource, config);
    List<Document> documents = reader.get();
    return reader.read();
}

GET http://localhost:8081/etl/html
[
  {
    "id": "eea02076-b778-4590-a196-47ebc40ec75f",
    "media": null,
    "metadata": {
      "date": "2024-01-15",
      "author": "John Doe"
      "linkUrls": [
        "",
        "",
        "https://www.example.com"
      ],
      "source": "web-scrape-1",
      "title": "My Web Page"
    },
    "score": null,
    "text": "This is the main content of my web page.\nIt contains multiple paragraphs."
  }
]
```



### 5）`PDF `

#### a、页面  ->  `PagePdfDocumentReader` 

> `PagePdfDocumentReader` 使用 Apache PdfBox 库解析 PDF 文档

```java
/**
 * 读取 PDF（按页读取）
 */
@GetMapping("/read/pdf")
public List<Document> readPdf(@Value("classpath:document/manual.pdf") Resource resource) {
    PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(
            resource,
            PdfDocumentReaderConfig.builder()
                    .withPagesPerDocument(1) // 一页一个文档
                    .build()
    );
    return pdfReader.read();
}
```



#### b、`PDF` 段落 -> `ParagraphPdfDocumentReader`

> `ParagraphPdfDocumentReader` 使用 PDF 目录（例如 TOC）信息将输入 PDF 拆分为文本段落，并为每个段落输出一个 `Document`。注意：并非所有 PDF 文档都包含 PDF 目录。

```java
List<Document> getDocsFromPdfWithCatalog() {
    ParagraphPdfDocumentReader pdfReader = new ParagraphPdfDocumentReader("classpath:/sample1.pdf",
            PdfDocumentReaderConfig.builder()
                .withPageTopMargin(0)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                    .withNumberOfTopTextLinesToDelete(0)
                    .build())
                .withPagesPerDocument(1)
                .build());

  return pdfReader.read();
}
```



#### 7）万能 `Tika` -> `TikaDocumentReader` 

> `TikaDocumentReader` 使用 Apache Tika 从各种文档格式（例如 PDF、DOC/DOCX、PPT/PPTX 和 HTML）中提取文本。

```java
 /**
   * 读取 Word / PPT / HTML / PDF（万能 Tika）
   */
  @GetMapping("/read/tika")
  public List<Document> readWord(@Value("classpath:document/code.md") Resource resource) {
      TikaDocumentReader reader = new TikaDocumentReader(resource);
      return reader.read();
  }

[
  {
    "id": "061cd140-c7ee-41e6-80fb-7fd2fd3b5232",
    "media": null,
    "metadata": {
      "source": "code.md"
    },
    "score": null,
    "text": "This is a Java sample application:\n\n```java\npackage com.example.demo;\n\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n\n@SpringBootApplication\npublic class DemoApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(DemoApplication.class, args);\n    }\n}\n```\n\nMarkdown also provides the possibility to `use inline code formatting throughout` the entire sentence.\n\n---\n\nAnother possibility is to set block code without specific highlighting:\n\n```\n./mvnw spring-javaformat:apply\n```\n"
  }
]
```





## 3、转换器

> `TextSplitter` 是一个抽象基类，用于帮助分割文档以适应 AI 模型的上下文窗口。

### 1）`TokenTextSplitter`：文本拆分

> `TokenTextSplitter` 是 `TextSplitter` 的一个实现，它使用 `CL100K_BASE`  编码根据令牌计数将文本分割成块。

| 参数名                  | 默认 | 说明                                                         |
| ----------------------- | ---- | ------------------------------------------------------------ |
| `encodingType`          |      | 关键参数。必须与你的 LLM 模型匹配。OpenAI GPT-3.5/4 使用 `CL100K_BASE`。 |
| `chunkSize`             | 800  | 每个文档块的目标 Token 数量。                                |
| `minChunkSizeChars`     | 350  | 最小字符数。如果切分后小于这个值，它会尝试与下一段合并，避免产生太碎的片段。 |
| `minChunkLengthToEmbed` | 5    | 最小嵌入长度。如果片段太短（Token 数小于 10），可能不值得单独向量化，会被丢弃或合并。 |
| `maxNumChunks`          | 1000 | 安全限制，防止单个文件产生过多的块导致内存溢出。             |
| `keepSeparator`         | true | 是否在切分时保留分隔符（如换行符 `\n`）。                    |

`TokenTextSplitter` 按如下方式处理文本内容

1. 它使用 CL100K_BASE 编码将输入文本编码为令牌。
2. 它根据 `chunkSize` 将编码文本分割成块。
3. 对于每个块
   1. 它将块解码回文本。
   2. 它尝试在 `minChunkSizeChars` 之后找到合适的断点（句号、问号、感叹号或换行符）。
   3. 如果找到断点，它会在该点截断块。
   4. 它根据 `keepSeparator` 设置修剪块并可选地删除换行符。
   5. 如果生成的块长于 `minChunkLengthToEmbed`，则将其添加到输出中。
4. 此过程一直持续到所有令牌都处理完毕或达到 `maxNumChunks`。
5. 任何剩余文本如果长于 `minChunkLengthToEmbed`，则作为最后一个块添加。

```java
// ----------------------------------------------------------------------------
// 6、文本拆分（长文本切块）
// 响应：切成多块的文档列表
// ----------------------------------------------------------------------------
@GetMapping("/transform/split")
public List<Document> split(
        @Value("classpath:document/data.txt") Resource resource
) {
    List<Document> docs = new TextReader(resource).read();
    TokenTextSplitter splitter = TokenTextSplitter.builder()
            // 关键参数。必须与你的 LLM 模型匹配。OpenAI GPT-3.5/4 使用 CL100K_BASE
            .withEncodingType(EncodingType.CL100K_BASE)
            //每个文档块的目标 Token 数量。
            .withChunkSize(800)
            //最小嵌入长度。如果片段太短（Token 数小于 10），可能不值得单独向量化，会被丢弃或合并。
            .withMinChunkLengthToEmbed(5)
            //最小字符数。如果切分后小于这个值，它会尝试与下一段合并，避免产生太碎的片段。
            .withMinChunkSizeChars(350)
            //安全限制，防止单个文件产生过多的块导致内存溢出。
            .withMaxNumChunks(10000)
            // 是否在切分时保留分隔符（如换行符 \n）。
            .withKeepSeparator(true) 
            .build();
    return splitter.apply(docs);
}




[
  {
    "id": "7588e7c6-dbd0-4c1e-9d57-b68434a0e15e",
    "media": null,
    "metadata": {
      "charset": "UTF-8",
      "chunk_index": 0,
      "parent_document_id": "22fff980-7034-4eaa-ac1c-6e15f7a2e9f0",
      "source": "data.txt",
      "total_chunks": 1
    },
    "score": null,
    "text": "{\n  \"date\": \"26日星期一\",\n  \"sunrise\": \"06:11\",\n  \"high\": \"高温 21.0℃\",\n  \"low\": \"低温 8.0℃\",\n  \"sunset\": \"18:31\",\n  \"aqi\": 97,\n  \"fx\": \"西南风\",\n  \"fl\": \"<3级\",\n  \"type\": \"多云\",\n  \"notice\": \"阴晴之间，谨防紫外线侵扰\"\n}"
  }
]
```





### 2）`KeywordMetadataEnricher`：AI 关键词

> 它使用生成式 `AI` 模型从文档内容中提取关键字并将其添加为元数据。

**构造函数选项：`KeywordMetadataEnricher` 提供两个构造函数选项**

1. `KeywordMetadataEnricher(ChatModel chatModel, int keywordCount)`：使用默认模板并提取指定数量的关键字。
2. `KeywordMetadataEnricher(ChatModel chatModel, PromptTemplate keywordsTemplate)`：使用自定义模板进行关键字提取。

**行为：`KeywordMetadataEnricher` 按如下方式处理文档**

1. 对于每个输入文档，它使用文档内容创建一个提示。
2. 它将此提示发送到提供的 `ChatModel` 以生成关键字。
3. 生成的关键字将以 "excerpt_keywords" 键添加到文档的元数据中。
4. 返回经过富集的文档。

注意

- `KeywordMetadataEnricher` 需要一个功能正常的 `ChatModel` 来生成关键字。
- 关键字计数必须大于等于 1。
- 富集器为每个处理过的文档添加 "excerpt_keywords" 元数据字段。
- 生成的关键字以逗号分隔的字符串形式返回。
- 此富集器对于提高文档可搜索性以及为文档生成标签或类别特别有用。
- 在 Builder 模式中，如果设置了 `keywordsTemplate` 参数，则 `keywordCount` 参数将被忽略。



```java
// ----------------------------------------------------------------------------
// 7、AI 关键词
// 请求：GET http://localhost:8080/etl/transform/keywords
// ----------------------------------------------------------------------------
@GetMapping("/transform/keywords")
public List<Document> keywords(@Value("classpath:document/code.md") Resource resource) {
    List<Document> docs = new TextReader(resource).read();
    KeywordMetadataEnricher enricher = KeywordMetadataEnricher.builder(chatModel)
            .keywordCount(5)
            // 提示词
            // .keywordsTemplate("")
            .build();
    return enricher.apply(docs);
}



[
  {
    "id": "d41c39ba-8ba7-42b9-9107-1951b7036453",
    "media": null,
    "metadata": {
      "charset": "UTF-8",
      "excerpt_keywords": "Spring Boot, SpringApplication, @SpringBootApplication, Maven, mvnw",
      "source": "code.md"
    },
    "score": null,
    "text": "This is a Java sample application:\n\n```java\npackage com.example.demo;\n\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n\n@SpringBootApplication\npublic class DemoApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(DemoApplication.class, args);\n    }\n}\n```\n\nMarkdown also provides the possibility to `use inline code formatting throughout` the entire sentence.\n\n---\n\nAnother possibility is to set block code without specific highlighting:\n\n```\n./mvnw spring-javaformat:apply\n```"
  }
]
```



### 3）`SummaryMetadataEnricher`：AI 摘要

> 它使用生成式 AI 模型为文档创建摘要并将其添加为元数据。它可以为当前文档以及相邻文档（上一文档和下一文档）生成摘要。

构造函数：`SummaryMetadataEnricher` 提供两个构造函数

1. `SummaryMetadataEnricher(ChatModel chatModel, List<SummaryType> summaryTypes)`
2. `SummaryMetadataEnricher(ChatModel chatModel, List<SummaryType> summaryTypes, String summaryTemplate, MetadataMode metadataMode)`

参数：

- `chatModel`：用于生成摘要的 AI 模型。
- `summaryTypes`：一个 `SummaryType` 枚举值列表，指示要生成哪些摘要（PREVIOUS、CURRENT、NEXT）。
- `summaryTemplate`：用于摘要生成的自定义模板（可选）。
- `metadataMode`：指定在生成摘要时如何处理文档元数据（可选）。

行为：`SummaryMetadataEnricher` 按如下方式处理文档

1. 对于每个输入文档，它使用文档内容和指定的摘要模板创建一个提示。
2. 它将此提示发送到提供的 `ChatModel` 以生成摘要。
3. 根据指定的 `summaryTypes`，它会向每个文档添加以下元数据
   - `SummaryMetadataEnricher.SummaryType.CURRENT`：当前文档的摘要。
   - `SummaryMetadataEnricher.SummaryType.PREVIOUS`：上一文档的摘要（如果可用且已请求）。
   - `SummaryMetadataEnricher.SummaryType.NEXT`：下一文档的摘要（如果可用且已请求）。
4. 返回经过富集的文档。

```java

// ----------------------------------------------------------------------------
// 8、AI 摘要
// ----------------------------------------------------------------------------
@GetMapping("/transform/summary")
public List<Document> summary(@Value("classpath:document/code.md") Resource resource) {
    List<Document> docs = new TextReader(resource).read();
    SummaryMetadataEnricher enricher = new SummaryMetadataEnricher(
            chatModel,
            List.of(SummaryMetadataEnricher.SummaryType.CURRENT)
    );
    return enricher.apply(docs);
}


[
  {
    "id": "18b2b2f8-cf41-46f8-92a5-0377353ac019",
    "media": null,
    "metadata": {
      "charset": "UTF-8",
      "source": "code.md",
      "section_summary": "**Summary:**  \nThe section highlights two key topics:  \n1. **Java Spring Boot Application Structure**: Demonstrates a basic Spring Boot application with the `DemoApplication` class, annotated with `@SpringBootApplication`, and the standard `main` method for launching the application.  \n2. **Markdown Code Formatting**: Explains how to use inline code formatting in Markdown (`use inline code formatting throughout`) and block code formatting without syntax highlighting (e.g., the Maven command `./mvnw spring-javaformat:apply`).  \n\n**Key Entities**:  \n- `DemoApplication` (Spring Boot main class)  \n- `@SpringBootApplication` (annotation for Spring Boot configuration)  \n- `./mvnw spring-javaformat:apply` (Maven command for code formatting)  \n- Markdown syntax for inline and block code formatting."
    },
    "score": null,
    "text": "This is a Java sample application:\n\n```java\npackage com.example.demo;\n\nimport org.springframework.boot.SpringApplication;\nimport org.springframework.boot.autoconfigure.SpringBootApplication;\n\n@SpringBootApplication\npublic class DemoApplication {\n    public static void main(String[] args) {\n        SpringApplication.run(DemoApplication.class, args);\n    }\n}\n```\n\nMarkdown also provides the possibility to `use inline code formatting throughout` the entire sentence.\n\n---\n\nAnother possibility is to set block code without specific highlighting:\n\n```\n./mvnw spring-javaformat:apply\n```"
  }
]

```



## 4、写入器

### 1）文件：`FileDocumentWriter` 

> `FileDocumentWriter` 是 `DocumentWriter` 的实现，它将 `Document` 对象列表的内容写入文件。

**构造函数：`FileDocumentWriter` 提供三个构造函数**

1. `FileDocumentWriter(String fileName)`
2. `FileDocumentWriter(String fileName, boolean withDocumentMarkers)`
3. `FileDocumentWriter(String fileName, boolean withDocumentMarkers, MetadataMode metadataMode, boolean append)`

**参数**

- `fileName`：要写入文档的文件名。
- `withDocumentMarkers`：是否在输出中包含文档标记（默认值：`false`）。
- `metadataMode`：指定要写入文件的文档内容（默认值：`MetadataMode.NONE`）。
- `append`：如果为 true，数据将写入文件末尾而不是开头（默认值：false）。

**行为：`FileDocumentWriter` 按如下方式处理文档**

1. 它为指定的文件名打开一个 FileWriter。
2. 对于输入列表中的每个文档
   1. 如果 `withDocumentMarkers` 为 true，它会写入包含文档索引和页码的文档标记。
   2. 它根据指定的 `metadataMode` 写入文档的格式化内容。
3. 写入所有文档后，文件将关闭。

**文档标记：当 `withDocumentMarkers` 设置为 `true` 时，写入器会为每个文档包含以下格式的标记**

```none
### Doc: [index], pages:[start_page_number,end_page_number]
```

元数据处理：写入器使用两个特定的元数据键

- `page_number`：表示文档的起始页码。
- `end_page_number`：表示文档的结束页码。



```javaz
/**
 * 写入本地文件 (调试用)
 * 场景：将文档导出为 txt 文件，用于检查 ETL 处理效果。
 */
@GetMapping("/file")
public String loadToFile() {
    List<Document> documents = List.of(new Document("测试内容1"), new Document("测试内容2"));
    // 写入 output.txt
    // 参数：文件名, 是否包含文档标记, 元数据模式, 是否追加
    FileDocumentWriter writer = new FileDocumentWriter(
            "output.txt",
            true,
            MetadataMode.ALL,
            false
    );

    writer.accept(documents);

    return "文档已写入本地文件 output.txt";
}





### Doc: 0, pages:[null,null]


测试内容1
### Doc: 1, pages:[null,null]


测试内容2
```



### 2）写入向量数据库

```java
/**
 * 1. 写入向量数据库 (核心 RAG 步骤)
 * 场景：将切分和增强后的文档存入 VectorStore。
 */
@GetMapping("/vectorstore")
public String loadToVectorStore(@Value("classpath:document/code.md") Resource resource) {
    // 1. 提取
    MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
            .withHorizontalRuleCreateDocument(true) // 按 --- 分割
            .withIncludeCodeBlock(true) // 包含代码块
            .build();
    MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);
    List<Document> docs = reader.read();

    // 2. 转换 (切分)
    TokenTextSplitter splitter = TokenTextSplitter.builder()
            // 关键参数。必须与你的 LLM 模型匹配。OpenAI GPT-3.5/4 使用 CL100K_BASE
            .withEncodingType(EncodingType.CL100K_BASE)
            //每个文档块的目标 Token 数量。
            .withChunkSize(800)
            //最小嵌入长度。如果片段太短（Token 数小于 10），可能不值得单独向量化，会被丢弃或合并。
            .withMinChunkLengthToEmbed(5)
            //最小字符数。如果切分后小于这个值，它会尝试与下一段合并，避免产生太碎的片段。
            .withMinChunkSizeChars(350)
            //安全限制，防止单个文件产生过多的块导致内存溢出。
            .withMaxNumChunks(10000)
            // 是否在切分时保留分隔符（如换行符 \n）。
            .withKeepSeparator(true)
            .build();
    List<Document> transformedDocs = splitter.apply(docs);

    // 3. 加载
    // 注意：VectorStore 通常实现了 DocumentWriter 接口
    vectorStore.accept(transformedDocs);

    return "成功加载 " + transformedDocs.size() + " 个文档片段到向量库";
}


成功加载 3 个文档片段到向量库

```







![ContactAuthor](https://raw.githubusercontent.com/HealerJean/HealerJean.github.io/master/assets/img/artical_bottom.jpg)



<!-- Gitalk 评论 start  -->

<link rel="stylesheet" href="https://unpkg.com/gitalk/dist/gitalk.css">

<script src="https://unpkg.com/gitalk@latest/dist/gitalk.min.js"></script> 
<div id="gitalk-container"></div>    
 <script type="text/javascript">
    var gitalk = new Gitalk({
		clientID: `1d164cd85549874d0e3a`,
		clientSecret: `527c3d223d1e6608953e835b547061037d140355`,
		repo: `HealerJean.github.io`,
		owner: 'HealerJean',
		admin: ['HealerJean'],
		id: 'LIr4hiey7tuTWKBqsdCx',
    });
    gitalk.render('gitalk-container');
</script> 




<!-- Gitalk end -->



