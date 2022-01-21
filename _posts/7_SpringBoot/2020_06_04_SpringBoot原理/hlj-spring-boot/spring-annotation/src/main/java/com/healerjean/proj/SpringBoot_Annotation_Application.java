package com.healerjean.proj;

import com.custom.proj.annotation.EnableCustomerService;
import com.custom.proj.enums.MonitorSelectorEnum;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableCustomerService(
        monitor = MonitorSelectorEnum.MONITOR_ENABLE_B_SERVICE)
@SpringBootApplication
public class SpringBoot_Annotation_Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringBoot_Annotation_Application.class, args);
    }

}
