package com.healerjean.proj.strata.web.controller;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * AiPromptController
 *
 * @author zhangyujin
 * @date 2026/3/30
 */
@RestController
@RequestMapping("/ai/prompt")
@RequiredArgsConstructor
public class AiPromptController {

    @Resource
    private ChatClient chatClient;


    /**
     * 案例 1：基础动态提示词
     * <p>
     * 场景描述：根据用户输入的形容词和话题，让 AI 生成一个笑话
     * 接口地址：GET /ai/prompt/joke?adjective=幽默的&topic=程序员
     *
     * @param adjective 形容词（例如：幽默的、讽刺的）
     * @param topic     话题（例如：程序员、Java）
     * @return AI 生成的笑话内容
     */
    @GetMapping("/joke")
    public String generateJoke(@RequestParam String adjective, @RequestParam String topic) {
        // 1. 定义提示词模板：使用 {} 作为占位符
        String templateStr = "给我讲一个关于 {topic} 的 {adjective} 笑话";

        // 2. 创建模板对象
        PromptTemplate promptTemplate = new PromptTemplate(templateStr);

        // 3. 准备参数模型
        Map<String, Object> model = Map.of("adjective", adjective, "topic", topic);

        // 4. 渲染模板并构建 Prompt
        Prompt prompt = promptTemplate.create(model);

        // 5. 调用 AI 模型并返回内容
        return chatClient.prompt(prompt).call().content();
    }

    /**
     * 案例 2：多角色对话与系统指令
     * <p>
     * 场景描述：通过系统提示词设定 AI 的角色（如海盗风格），并让其以特定名字回复用户
     * 接口地址：GET /ai/prompt/chat?userName=杰克&userQuestion=宝藏在哪里
     *
     * @param userName     用户的名字（用于 AI 称呼用户）
     * @param userQuestion 用户的问题
     * @return AI 以特定角色风格回复的内容
     */
    @GetMapping("/chat")
    public String roleplayChat(@RequestParam String userName, @RequestParam String userQuestion) {

        // 1. 构建用户消息
        Message userMessage = new UserMessage(userQuestion);

        // 2. 构建系统消息：定义 AI 的角色、名字和说话风格
        String systemTemplate = """
                你是一个乐于助人的 AI 助手。
                你的名字是 {name}。
                你应该用 {voice} 的风格回复用户的请求，并在回复中提到你的名字。
                """;

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemTemplate);
        Message systemMessage = systemPromptTemplate.createMessage(
                Map.of("name", userName, "voice", "海盗")
        );

        // 3. 组合提示词：系统消息在前，用户消息在后
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        // 4. 调用 AI 模型
        ChatResponse response = chatClient.prompt(prompt).call().chatResponse();

        // 5. 提取并返回文本内容
        return response.getResult().getOutput().getText();
    }

}
