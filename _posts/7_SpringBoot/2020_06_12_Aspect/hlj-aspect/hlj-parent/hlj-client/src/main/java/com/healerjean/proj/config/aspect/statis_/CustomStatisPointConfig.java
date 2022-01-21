package com.healerjean.proj.config.aspect.statis_;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangyujin
 * @date 2022/1/20  4:05 下午.
 * @description
 */
@Configuration
public class CustomStatisPointConfig {

    @Bean
    public CustomStatusAdvisor init() {
        CustomStatusAdvisor customStatusAdvisor = new CustomStatusAdvisor();
        customStatusAdvisor.setCustomStatusPointcut(new CustomStatusPointcut());
        customStatusAdvisor.setAdvice(new CustomStatusAdvice());
        return customStatusAdvisor;
    }
}
