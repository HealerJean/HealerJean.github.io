package com.healerjean.proj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBoot_Application {

    public static void main(String[] args) {
        // System.out.println(SpringBoot_Demo_Application.class.getName());
        SpringApplication.run(SpringBoot_Application.class, args);


        // AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringBoot_Test_Application.class);
        // AppBean appBean = annotationConfigApplicationContext.getBean(AppBean.class);
        // DataBean dataBean1 = annotationConfigApplicationContext.getBean(DataBean.class);
        // DataBean dataBean2 = appBean.getDataBean();
        // System.out.println(dataBean1 == dataBean2);
    }

}
