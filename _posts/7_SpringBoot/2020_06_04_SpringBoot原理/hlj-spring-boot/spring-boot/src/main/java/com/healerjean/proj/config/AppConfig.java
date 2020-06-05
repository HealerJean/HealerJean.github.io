// package com.healerjean.proj.config;
//
// import com.healerjean.proj.bean.AppBean;
// import com.healerjean.proj.bean.DataBean;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.Import;
//
// /**
//  * @author HealerJean
//  * @ClassName AppConfig
//  * @date 2020/6/4  17:17.
//  * @Description
//  */
// @Configuration
// @Import(DataConfig.class)
// public class AppConfig {
//
//     @Autowired
//     private DataBean dataBean;
//
//     @Bean
//     public AppBean appBean() {
//         dataBean.method();
//         return new AppBean();
//     }
//
// }
