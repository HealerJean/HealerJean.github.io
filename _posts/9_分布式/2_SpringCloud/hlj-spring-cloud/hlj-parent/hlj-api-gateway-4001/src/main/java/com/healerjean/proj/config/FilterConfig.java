package com.healerjean.proj.config;

import com.healerjean.proj.config.filter.TokenFilter;
import com.healerjean.proj.config.filter.ZuulTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName FIterConfig
 * @date 2020/4/14  19:41.
 * @Description
 */
@Configuration
public class FilterConfig {

    // @Bean
    // public TokenFilter accessFilter() {
    //     return new TokenFilter();
    // }

    @Bean
    public ZuulTokenFilter accessFilter() {
        return new ZuulTokenFilter();
    }

}
