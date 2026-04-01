package com.healerjean.proj.strata.infra.config;

import io.modelcontextprotocol.client.McpSyncClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * McpConfig
 *
 * @author zhangyujin
 * @date 2026/3/31
 */
@Slf4j
@Configuration
public class McpClientConfig {


    @Resource
    private List<McpSyncClient> clients;


    @PostConstruct
    public void init(){
        clients.forEach(client -> {
            log.info("Connected to MCP Server: {} ", client.getServerInfo().name());
            client.listTools().tools().forEach(tool ->
                    log.info("Available Tool: {}", tool.name())
            );
        });
    }

}