package com.healerjean.proj;

import com.healerjean.proj.bean.AppBean;
import com.healerjean.proj.bean.DataBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class SpringBoot_Test_Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot_Test_Application.class, args);


        // AnnotationConfigApplicationContext annotationConfigApplicationContext = new AnnotationConfigApplicationContext(SpringBoot_Test_Application.class);
        // AppBean appBean = annotationConfigApplicationContext.getBean(AppBean.class);
        // DataBean dataBean1 = annotationConfigApplicationContext.getBean(DataBean.class);
        // DataBean dataBean2 = appBean.getDataBean();
        // System.out.println(dataBean1 == dataBean2);
    }

}
