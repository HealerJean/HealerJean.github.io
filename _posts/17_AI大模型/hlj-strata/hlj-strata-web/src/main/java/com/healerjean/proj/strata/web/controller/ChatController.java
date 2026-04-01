package com.healerjean.proj.strata.web.controller;

import com.healerjean.proj.strata.infra.chat.tool.WeatherTool;
import com.healerjean.proj.strata.web.vo.OrderVO;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Llama3Controller
 *
 * @author zhangyujin
 * @date 2025/11/18
 */

@RestController
@RequestMapping("/ai")
public class ChatController {

    @Resource
    private ChatClient chatClient;

    @Resource
    private WeatherTool weatherTools;


    /**
     * 基础对话
     */
    @GetMapping("/chat")
    public String chat(@RequestParam(value = "message", defaultValue = "你好，请介绍一下你自己") String msg) {
        return chatClient.prompt()
                .user(msg)
                .call()
                .content();
    }


    /**
     * 动态提示模板（占位符传参）
     */
    @GetMapping("chat/template")
    public String templateChat(
            @RequestParam String topic,
            @RequestParam(defaultValue = "中文") String language) {
        // 提示模板（占位符：{language}、{topic}）
        String promptTemplate = """
                用{language}写一段关于{topic}的技术介绍，要求：
                1. 简洁明了，300字以内
                2. 包含核心特点和使用场景
                3. 提供1个可运行的小案例
                """;
        return chatClient.prompt()
                .user(user -> user
                        // 模板文本
                        .text(promptTemplate)
                        .param("language", language)
                        .param("topic", topic))
                .call()
                .content();
    }


    /**
     * 结构化数据
     */
    @GetMapping("/structured")
    public OrderVO structuredChat(@RequestParam String orderInfo) {
        return chatClient.prompt()
                .user(u -> u
                        // 修正点：先使用 String.format 格式化字符串，再传入 text()
                        .text(String.format("请分析 '%s' 并提取关键信息。", orderInfo))
                )
                .call()
                .entity(OrderVO.class);
    }


    /**
     * 获取完整响应
     */
    @GetMapping("/metadata")
    public Map<String, Object> chatWithMetadata(@RequestParam String message) {
        // 1. 获取完整响应
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .call()
                .chatResponse();

        // 2. 提取元数据
        ChatResponseMetadata metadata = chatResponse.getMetadata();
        Usage usage = metadata.getUsage();

        // 4. 获取内容 (安全获取)
        // getResults()：返回一个列表，因为理论上 AI 可以一次生成多个备选答案（虽然通常我们只取第一个）。
        String content = "";
        if (!chatResponse.getResults().isEmpty()) {
            content = chatResponse.getResults().get(0).getOutput().getText();
        }

        // 5. 组装返回
        return Map.of(
                "content", content,            // AI 回答内容
                "model", metadata.getModel(),  // 使用的模型
                "id", metadata.getId(),        // 会话ID
                "totalTokens", usage.getTotalTokens(),    // 总Token
                "promptTokens", usage.getPromptTokens(),  // 提问Token
                "completionTokens", usage.getCompletionTokens() // 回答Token
        );
    }

    /**
     * 递归顾问 ToolCallAdvisor：获取北京、上海天气
     */
    @GetMapping("/chat/weather")
    public String weather(@RequestParam(value = "message", defaultValue = "先查北京天气，再查上海天气，然后一起告诉我") String msg) {
        return chatClient.prompt()
                .tools(weatherTools)
                .user(msg)
                .call()
                .content();
    }

}