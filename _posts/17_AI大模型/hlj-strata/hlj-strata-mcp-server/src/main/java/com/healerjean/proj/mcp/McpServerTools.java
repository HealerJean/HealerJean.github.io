package com.healerjean.proj.mcp;

import org.springframework.ai.mcp.annotation.McpPrompt;
import org.springframework.ai.mcp.annotation.McpResource;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class McpServerTools {

    /**
     * 1. @McpTool: 暴露一个工具给 LLM
     * 2. @McpToolParam: 描述参数，帮助 LLM 理解如何传参
     */
    @McpTool(name = "calculate_weather", description = "获取指定城市的天气信息")
    public String getWeather(
            @McpToolParam(description = "城市名称，例如：北京、上海", required = true) String city) {
        return "当前 " + city + " 的天气是：晴朗，25摄氏度。";
    }


    /**
     * 3. @McpResource: 暴露静态资源（如文档、数据文件）给 LLM 读取
     * uri 是资源的唯一标识
     */
    @McpResource(name = "company_info", uri = "file://company/data.txt", description = "公司基本信息文档")
    public String getCompanyData() {
        return "公司名称：Spring AI Inc.\n成立时间：2023年\n业务：AI 基础设施";
    }

    /**
     * 4. @McpPrompt: 定义一个预置提示词模板，LLM 可以直接调用这个模板
     */
    @McpPrompt(name = "code_reviewer", description = "用于代码审查的提示词模板")
    public String codeReviewPrompt() {
        return "你是一位资深代码审查员。请审查以下代码，指出潜在的性能问题和安全隐患：\n{{code}}";
    }

    /**
     * 5. @McpComplete: 通常用于流式响应或特定的补全操作
     * 注意：在 M4 版本中，这可能表现为返回 Flux 或特定的 ToolResult 类型
     */
    @McpTool(name = "stream_data")
    public Flux<String> streamDataProcess(@McpToolParam String query) {
        return Flux.just("处理中...", "步骤1完成", "步骤2完成", "最终结果：" + query);
    }
}