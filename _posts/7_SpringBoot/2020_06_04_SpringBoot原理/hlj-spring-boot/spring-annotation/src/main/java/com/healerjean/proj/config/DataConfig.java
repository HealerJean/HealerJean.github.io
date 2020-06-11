package com.healerjean.proj.config;

import com.healerjean.proj.config.bean.DataBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName DataConfit
 * @date 2020/6/4  18:58.
 * @Description
 */
@Configuration
@Slf4j
@Data
public class DataConfig {

    @Value("var.encrypt")
   private String encrypt;
    @Value("var.demo")
    private String demo;
    @Bean
    public DataBean dataBean() {
        log.info(encrypt);
        log.info(demo);
        return new DataBean();
    }

}

