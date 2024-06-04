package com.healerjean.proj.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Arrays;

/**
 * ElasticSearchConfig
 */
@Slf4j
@Data
@Component
@Configuration
@ConfigurationProperties(prefix = "es.log")
public class ElasticSearchConfig {
    public static final RequestOptions COMMON_OPTIONS;

    /**
     * es相关地址
     */
    private String[] urls;
    /**
     * user
     */
    private String user = "user";

    /**
     * password
     */
    private String password = "password";

    /**
     * restHighLevelClient
     */
    RestHighLevelClient restHighLevelClient;

    /**
     * 通用设置项
     */
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        COMMON_OPTIONS = builder.build();
    }

    /**
     * 初始化es 索引
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        log.info("开始初始化es连接");

        RestHighLevelClient restHighLevelClient = null;
        try {
            final CredentialsProvider credentialsProvider =
                    new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(user, password));

            RestClientBuilder restClientBuilder = RestClient.builder(Arrays.stream(urls).map(HttpHost::create).toArray(HttpHost[]::new))
                    .setRequestConfigCallback(requestConfigBuilder -> {
                        requestConfigBuilder.setConnectTimeout(-1);
                        requestConfigBuilder.setSocketTimeout(-1);
                        requestConfigBuilder.setConnectionRequestTimeout(-1);
                        return requestConfigBuilder;
                    })
                    .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
            restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        } catch (Exception e) {
            log.error("初始化es连接失败：{}", e.getMessage());
        }
        this.restHighLevelClient = restHighLevelClient;
        return restHighLevelClient;
    }

    /**
     * destroy
     */
    @PreDestroy
    public void destroy() throws Exception {
        if (null != restHighLevelClient) {
            restHighLevelClient.close();
            log.info("关闭ES链接restHighLevelClient!!!!!!");

        }
    }
}
