package com.healerjean.proj.strata.infra.config;

import com.healerjean.proj.strata.infra.mcp.server.BookMcpServer;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HelloMcpServerConfiguration
 *
 * @author zhangyujin
 * @date 2025/11/18
 */
@Configuration
public class McpServerConfiguration {

    @Bean
    public ToolCallbackProvider toolCallbackProvider(BookMcpServer bookMcpServer) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(bookMcpServer)
                .build();
    }
}
