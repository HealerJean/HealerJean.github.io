package com.healerjean.proj.config;

import com.healerjean.proj.annotation.condition.EncryptCondition;
import com.healerjean.proj.bean.DataBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName DataConfit
 * @date 2020/6/4  18:58.
 * @Description
 */
@Conditional(EncryptCondition.class)
@Configuration
@Slf4j
@Data
public class DataConfig {


    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}

