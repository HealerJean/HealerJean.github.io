package com.healerjean.proj.utils.gray;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * DuccBypassInsuranceConfiguration
 *
 * @author zhangyujin
 * @date 2022/9/21  21:21.
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties("gray")
public class GrayConfiguration {

    /**
     * 灰度对象
     */
    private Map<String, GrayBizBO> grayBizMap;


}
