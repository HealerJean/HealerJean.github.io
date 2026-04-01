package com.healerjean.proj.strata.web.controller;

import com.knuddels.jtokkit.api.EncodingType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.ParagraphPdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.writer.FileDocumentWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/etl")
public class EtlController {

    // 注入向量库 + AI模型（用于关键词/摘要）
    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private ChatModel chatModel;


    // ========================================================================
    // ======================== 1. 各种文件读取（Reader） =====================
    // ========================================================================

    /**
     * 读取 JSON 文件
     */
    @GetMapping("/read/json")
    public List<Document> readJson(@Value("classpath:document/data.json") Resource resource) {
        // 提取 content 和 description 字段作为内容
        JsonReader jsonReader = new JsonReader(resource, "content", "description");
        return jsonReader.read();
    }

    /**
     * 读取 TXT 文本文件
     */
    @GetMapping("/read/txt")
    public List<Document> readTxt(@Value("classpath:document/data.txt") Resource resource) {
        TextReader textReader = new TextReader(resource);
        textReader.getCustomMetadata().put("type", "text"); // 加元数据
        return textReader.read();
    }


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


    /**
     * 读取 Word / PPT / HTML / PDF（万能 Tika）
     */
    @GetMapping("/read/tika")
    public List<Document> readWord(@Value("classpath:document/code.md") Resource resource) {
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        return reader.read();
    }


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

}
