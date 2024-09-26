package com.healerjean.proj.config;

import com.healerjean.proj.util.id.SnowflakeIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tongdong
 * @Date: 2020/9/10
 * @Description:
 */
@ConditionalOnProperty(prefix = "id.generater", name = "enable", havingValue = "true")
@Configuration
@Slf4j
public class IdGenerateConfig {

    /**
     * zkAddress
     */
    @Value("${dubbo.registry.address:}")
    private String zkAddress;
    /**
     * port
     */
    @Value("${dubbo.protocols.port:0}")
    private Integer port;

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerater() {
        int i = zkAddress.indexOf("://") + 3;
        int j = zkAddress.indexOf("?", i) == -1 ? zkAddress.length() : zkAddress.indexOf("?", i);
        zkAddress = (zkAddress.substring(i, j));
        SnowflakeIdGenerator snowflakeIdGenerator = new SnowflakeIdGenerator(zkAddress, String.valueOf(port));
        log.info("ID生成器已启动了");
        return snowflakeIdGenerator;
    }

}
