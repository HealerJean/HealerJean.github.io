package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * AiEvaluationController
 *
 * @author zhangyujin
 * @date 2026/4/1
 */
@RestController
@RequestMapping("/ai/eval")
public class AiEvaluationController {

    @Resource
    private ChatClient chatClient;

    @Resource
    private VectorStore vectorStore;

    /**
     * 工厂
     */
    @Resource
    private ChatClient.Builder chatClientBuilder;



    // ========================================================================
    // 1. 相关性评估（RelevancyEvaluator）
    // 功能：判断 AI 回答是否与问题 + 上下文相关
    // URL: GET  http://localhost:8080/ai/eval/relevancy?question=地球有几颗天然卫星？&context=地球只有一颗天然卫星，叫月球。&aiResponse=地球有一颗天然卫星，是月球。
    // ========================================================================
    @GetMapping("/relevancy")
    public EvaluationResponse relevancy(
            @RequestParam String question,
            @RequestParam String context,
            @RequestParam String aiResponse) {
        // 构建评估器
        RelevancyEvaluator evaluator = new RelevancyEvaluator(chatClientBuilder);

        // 构建评估材料
        EvaluationRequest request = new EvaluationRequest(question, List.of(new Document(context)), aiResponse);

        // 执行评估
        return evaluator.evaluate(request);
    }


    // ========================================================================
    // 2. 事实核查评估（FactCheckingEvaluator）
    // 功能：检查 AI 是否造假、幻觉
    // URL:GET http://localhost:8080/ai/eval/fact-check?context=地球是太阳系第三颗行星。&aiResponse=地球是第四颗行星。
    // ========================================================================
    @GetMapping("/fact-check")
    public EvaluationResponse factCheck(
            @RequestParam String context,
            @RequestParam String aiResponse
    ) {
        FactCheckingEvaluator evaluator =  FactCheckingEvaluator.builder(chatClientBuilder).build();

        EvaluationRequest request = new EvaluationRequest(context, List.of(new Document(context)), aiResponse);

        return evaluator.evaluate(request);
    }


    // ========================================================================
    // 3. RAG 完整流程 + 自动双重评估（检索→回答→相关性→事实核查）
    // URL: GET http://localhost:8080/ai/eval/rag-evaluate?question=地球有几颗天然卫星？
    // ========================================================================
    @GetMapping("/rag-evaluate")
    public String ragEvaluate(@RequestParam String question) {
        // 1. 从向量库检索相关上下文
        List<Document> docs = vectorStore.similaritySearch(question);
        String context = docs.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n---\n" + b);

        // 2. AI 生成回答
        String aiAnswer = chatClient.prompt()
                .system("基于以下上下文回答，不要编造：\n" + context)
                .user(question)
                .call()
                .content();

        // 3. 构建评估请求
        EvaluationRequest request = new EvaluationRequest(question, List.of(new Document(context)), aiAnswer);

        // 4. 双重评估
        RelevancyEvaluator relEval = new RelevancyEvaluator(chatClientBuilder);
        FactCheckingEvaluator factEval = FactCheckingEvaluator.builder(chatClientBuilder).build();

        boolean relPass = relEval.evaluate(request).isPass();
        boolean factPass = factEval.evaluate(request).isPass();

        // 5. 返回结果
        return """
                【用户问题】
                %s

                【参考上下文摘要】
                %s

                【AI 回答】
                %s

                【评估结果】
                相关性：%s
                事实准确性：%s
                """.formatted(
                question,
                context.length() > 200 ? context.substring(0, 200) + "..." : context,
                aiAnswer,
                relPass ? "✅ 相关" : "❌ 不相关",
                factPass ? "✅ 事实正确" : "❌ 事实错误/幻觉"
        );
    }
}
