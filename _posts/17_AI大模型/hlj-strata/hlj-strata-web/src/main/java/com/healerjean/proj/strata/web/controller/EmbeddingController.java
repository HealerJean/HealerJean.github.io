package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * EmbeddingController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@RestController
@RequestMapping("/ai/embedding")
public class EmbeddingController {

    @Resource
    private EmbeddingModel embeddingModel;


    /**
     * 案例 1：获取文本向量
     * 场景：将一段文本转换为高维向量数组
     * URL: GET /ai/embedding/vector?text=Spring AI is awesome
     */
    @GetMapping("/vector")
    public Map<String, Object> getVector(@RequestParam String text) {
        // 1. 调用模型生成向量
        float[] vector = embeddingModel.embed(text);

        // 2. 返回结果（通常前端只需要向量用于计算或存储）
        return Map.of(
                "text", text,
                "dimensions", vector.length, // 维度，例如 1536
                "vector", Arrays.toString(vector) // 实际向量数据
        );
    }

    /**
     * 案例 2：语义相似度计算
     * 场景：判断两段文本在语义上是否相似
     * URL: GET /ai/embedding/similarity?text1=如何做西红柿炒鸡蛋&text2=西红柿炒鸡蛋的食谱
     */
    @GetMapping("/similarity")
    public Map<String, Object> calculateSimilarity(
            @RequestParam String text1,
            @RequestParam String text2) {

        // 1. 将两段文本转换为向量
        float[] vector1 = embeddingModel.embed(text1);
        float[] vector2 = embeddingModel.embed(text2);

        // 2. 计算余弦相似度
        double similarity = cosineSimilarity(vector1, vector2);

        // 3. 结果解读
        String interpretation;
        if (similarity > 0.8) {
            interpretation = "语义高度相似";
        } else if (similarity > 0.5) {
            interpretation = "语义有一定相关性";
        } else {
            interpretation = "语义不相关";
        }

        return Map.of(
                "text1", text1,
                "text2", text2,
                "score", similarity,
                "interpretation", interpretation
        );
    }

    /**
     * 案例 3：底层 API 调用
     * 场景：获取更详细的元数据（如 Token 消耗）
     * URL: GET /ai/embedding/raw
     */
    @GetMapping("/raw")
    public Map<String, Object> rawEmbedding() {
        String text = "Testing raw API call";

        // 1. 构建请求对象
        EmbeddingRequest request = new EmbeddingRequest(
                List.of(text),
                EmbeddingOptions.builder().build()
        );

        // 2. 调用底层接口
        EmbeddingResponse response = embeddingModel.call(request);

        // 3. 提取元数据
        // 注意：不同模型的元数据字段可能不同，这里以 OpenAI 为例
        return Map.of(
                "input_text", text,
                "vector_length", response.getResult().getOutput().length,
                "usage", response.getMetadata().getUsage() // 查看消耗了多少 Token
        );
    }

    /**
     * 辅助方法：计算余弦相似度
     * 公式：(A·B) / (||A|| * ||B||)
     */
    private double cosineSimilarity(float[] vectorA, float[] vectorB) {
        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException("向量维度必须一致");
        }

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}