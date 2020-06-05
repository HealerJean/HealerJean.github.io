package com.healerjean.proj.config;

import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.bean.DataBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HealerJean
 * @ClassName DataConfit
 * @date 2020/6/4  18:58.
 * @Description
 */
@Configuration
public class DataConfig {

    @Bean
    public AppBean appBean() {
        AppBean appBean = new AppBean();
        appBean.setDataBean(dataBean());
        return appBean;
    }

    @Bean
    public DataBean dataBean() {
        return new DataBean();
    }

}
